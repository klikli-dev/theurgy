// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.derivative.render;

import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeItem;
import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeTier;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.util.TagUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AlchemicalDerivativeRenderer implements SpecialModelRenderer<AlchemicalDerivativeRenderState> {

    private static final Supplier<ItemStack> labeledEmptyJarStack = Lazy.of(() -> new ItemStack(ItemRegistry.EMPTY_JAR_LABELED_ICON.get()));
    private static final Supplier<Map<AlchemicalDerivativeTier, ItemStack>> tierToIconMap = Lazy.of(() -> Map.of(
            AlchemicalDerivativeTier.ABUNDANT, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON.get()),
            AlchemicalDerivativeTier.COMMON, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON.get()),
            AlchemicalDerivativeTier.RARE, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_RARE_ICON.get()),
            AlchemicalDerivativeTier.PRECIOUS, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON.get())
    ));

    @Override
    public void submit(@Nullable AlchemicalDerivativeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, boolean hasFoil, int outlineColor) {
        if (state == null) return;

        // Counteract the -0.5 translation applied by ItemTransform.NO_TRANSFORM (from the derivative_base model)
        // so that our sub-items render centered rather than offset to the bottom-left corner of the slot.
        poseStack.translate(0.5f, 0.5f, 0.5f);

        // Resolve the source stack from the render state
        ItemStack sourceStack = this.resolveSourceStack(state);

        // If shift is down in GUI, just render the contained item in full size
        if (state.shiftDown()) {
            if (!sourceStack.isEmpty()) {
                this.submitItem(sourceStack, ItemDisplayContext.NONE, poseStack, submitNodeCollector, light, overlay, outlineColor);
            }
            return;
        }

        boolean renderSource = state.renderSource();

        // If we do not render the source we show a simplified labeled icon with pixels representing fictional text
        var jarStack = renderSource ? new ItemStack(state.jarIconItem()) : labeledEmptyJarStack.get();

        // Render Jar
        this.submitItem(jarStack, ItemDisplayContext.NONE, poseStack, submitNodeCollector, light, overlay, outlineColor);

        // Render Frame
        var tierStack = tierToIconMap.get().get(state.tier());
        if (tierStack != null) {
            float pixel = 1f / 16f;
            poseStack.pushPose();
            // Compensate for the jar using actual display context transforms while overlays use GUI
            this.applyOverlayContextCompensation(ItemDisplayContext.NONE, false, poseStack);
            poseStack.translate(0, 0, pixel * 0.5); // move it in front of the jar
            poseStack.scale(1F, 1F, 0.01F); // flatten
            this.submitItem(tierStack, ItemDisplayContext.GUI, poseStack, submitNodeCollector, light, overlay, outlineColor);
            poseStack.popPose();
        }

        if (renderSource) {
            // Render Label
            var labelStack = new ItemStack(ItemRegistry.JAR_LABEL_ICON.get());
            float pixel = 1f / 16f;
            poseStack.pushPose();
            this.applyOverlayContextCompensation(ItemDisplayContext.NONE, false, poseStack);
            poseStack.translate(0, 0, pixel * 0.5); // move it in front of the jar
            poseStack.scale(1F, 1F, 0.01F); // flatten
            this.submitItem(labelStack, ItemDisplayContext.GUI, poseStack, submitNodeCollector, light, overlay, outlineColor);
            poseStack.popPose();

            // Render Contained Item
            if (!sourceStack.isEmpty()) {
                poseStack.pushPose();
                this.applyOverlayContextCompensation(ItemDisplayContext.NONE, false, poseStack);

                // Restore the transform chain from the old BEWLR:
                // 1. Move in front of the label (z-axis)
                poseStack.translate(0, 0, pixel * 0.6);
                // 2. Pre-scale to make the contained item small
                var scale = 0.36f;
                poseStack.scale(scale, scale, scale);
                // 3. Position it on the label area (in scaled coordinates)
                poseStack.translate(0, -pixel * 3.2, 0);
                // 4. Flatten the item
                poseStack.scale(0.74F, 0.74F, 0.01F);

                this.submitItem(sourceStack, ItemDisplayContext.GUI, poseStack, submitNodeCollector, light, overlay, outlineColor);

                poseStack.popPose();
            }
        }
    }

    /**
     * Resolves the source ItemStack from the render state's non-level-owned data.
     */
    private ItemStack resolveSourceStack(AlchemicalDerivativeRenderState state) {
        if (state.sourceItem() != null) {
            return new ItemStack(state.sourceItem());
        }
        if (state.sourceTag() != null) {
            return TagUtil.getItemStackForTag(state.sourceTag());
        }
        return ItemStack.EMPTY;
    }

    private void submitItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, int outlineColor) {
        ItemStackRenderState renderState = new ItemStackRenderState();
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getItemModelResolver().updateForTopItem(renderState, itemStack, displayContext, minecraft.level, null, 0);
        renderState.submit(poseStack, submitNodeCollector, light, overlay, outlineColor);
    }

    /**
     * Applies the item/generated display transforms (translation, rotation, scale) for the given context
     * to the PoseStack. This compensates for the fact that overlays are rendered with GUI context
     * (which has identity transforms) while the jar uses the actual display context.
     * <p>
     * The values here match the "display" block from minecraft:models/item/generated.json.
     * Translation values are pre-divided by 16 (as done by ItemTransform deserialization).
     */
    private void applyOverlayContextCompensation(ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack) {
        switch (displayContext) {
            case GROUND -> {
                // ground: translation [0, 2, 0], rotation [0, 0, 0], scale [0.5, 0.5, 0.5]
                poseStack.translate(0, 2f / 16f, 0);
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> {
                // thirdperson: translation [0, 3, 1], rotation [0, 0, 0], scale [0.55, 0.55, 0.55]
                poseStack.translate(0, 3f / 16f, 1f / 16f);
                poseStack.scale(0.55f, 0.55f, 0.55f);
            }
            case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> {
                // firstperson: translation [1.13, 3.2, 1.13], rotation [0, -90, 25], scale [0.68, 0.68, 0.68]
                float tx = leftHand ? -(1.13f / 16f) : (1.13f / 16f);
                float rotY = leftHand ? 90f : -90f;
                float rotZ = leftHand ? -25f : 25f;
                poseStack.translate(tx, 3.2f / 16f, 1.13f / 16f);
                poseStack.mulPose(new Quaternionf().rotationXYZ(0, rotY * 0.017453292f, rotZ * 0.017453292f));
                poseStack.scale(0.68f, 0.68f, 0.68f);
            }
            case HEAD -> {
                // head: translation [0, 13, 7], rotation [0, 180, 0], scale [1, 1, 1]
                poseStack.translate(0, 13f / 16f, 7f / 16f);
                float rotY = leftHand ? -180f : 180f;
                poseStack.mulPose(new Quaternionf().rotationXYZ(0, rotY * 0.017453292f, 0));
            }
            case FIXED -> {
                // fixed: rotation [0, 180, 0], scale [1, 1, 1]
                float rotY = leftHand ? -180f : 180f;
                poseStack.mulPose(new Quaternionf().rotationXYZ(0, rotY * 0.017453292f, 0));
            }
            // GUI and NONE: no compensation needed (identity transforms)
            default -> {
            }
        }
    }

    @Override
    public @Nullable AlchemicalDerivativeRenderState extractArgument(ItemStack stack) {
        if (stack.isEmpty()) return null;

        var item = stack.getItem();
        if (!(item instanceof AlchemicalDerivativeItem derivativeItem)) return null;

        var sourceItem = stack.get(DataComponentRegistry.SOURCE_ITEM);
        var sourceTag = stack.get(DataComponentRegistry.SOURCE_TAG);
        var tier = derivativeItem.tier();
        var jarIconItem = derivativeItem.emptyJarStackSupplier.get().getItem();
        var renderSource = ClientConfig.get().rendering.renderSulfurSourceItem.get();
        var shiftDown = Minecraft.getInstance().hasShiftDown();

        return new AlchemicalDerivativeRenderState(sourceItem, sourceTag, tier, jarIconItem, renderSource, shiftDown);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(-0.5f, -0.5f, -0.5f));
        output.accept(new Vector3f(0.5f, 0.5f, 0.5f));
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<AlchemicalDerivativeRenderState> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public @Nullable SpecialModelRenderer<AlchemicalDerivativeRenderState> bake(SpecialModelRenderer.BakingContext context) {
            return new AlchemicalDerivativeRenderer();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<AlchemicalDerivativeRenderState>> type() {
            return MAP_CODEC;
        }
    }
}
