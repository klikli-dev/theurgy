// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.derivative.render;

import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeItem;
import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeTier;
import com.klikli_dev.theurgy.registry.ItemRegistry;
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
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AlchemicalDerivativeRenderer implements SpecialModelRenderer<ItemStack> {

    private static final Supplier<ItemStack> labeledEmptyJarStack = Lazy.of(() -> new ItemStack(ItemRegistry.EMPTY_JAR_LABELED_ICON.get()));
    private static final Supplier<Map<AlchemicalDerivativeTier, ItemStack>> tierToIconMap = Lazy.of(() -> Map.of(
            AlchemicalDerivativeTier.ABUNDANT, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON.get()),
            AlchemicalDerivativeTier.COMMON, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON.get()),
            AlchemicalDerivativeTier.RARE, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_RARE_ICON.get()),
            AlchemicalDerivativeTier.PRECIOUS, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON.get())
    ));

    @Override
    public void submit(@Nullable ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, boolean hasFoil, int outlineColor) {
        if (stack == null || stack.isEmpty()) return;

        // Counteract the -0.5 translation applied by ItemTransform.NO_TRANSFORM (from the builtin/entity base model)
        // so that our sub-items render centered rather than offset to the bottom-left corner of the slot.
        poseStack.translate(0.5f, 0.5f, 0.5f);

        boolean renderSource = ClientConfig.get().rendering.renderSulfurSourceItem.get();

        // If shift is down in GUI, just render the contained item in full size
        if (displayContext == ItemDisplayContext.GUI && Minecraft.getInstance().hasShiftDown()) {
            this.renderContainedItemFull(stack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);
            return;
        }

        // If we do not render the source we show a simplified labeled icon with pixels representing fictional text
        var jarStack = renderSource ? AlchemicalDerivativeItem.getEmptyJarStack(stack) : labeledEmptyJarStack.get();

        // Render Jar
        this.submitItem(jarStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);

        // Render Frame
        var tierStack = tierToIconMap.get().get(AlchemicalDerivativeItem.getTier(stack));
        if (tierStack != null) {
            float pixel = 1f / 16f;
            poseStack.pushPose();
            poseStack.translate(0, 0, pixel * 0.5); // move it in front of the jar
            poseStack.scale(1F, 1F, 0.01F); // flatten
            this.submitItem(tierStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);
            poseStack.popPose();
        }

        if (renderSource) {
            // Render Label
            var labelStack = new ItemStack(ItemRegistry.JAR_LABEL_ICON.get());
            float pixel = 1f / 16f;
            poseStack.pushPose();
            poseStack.translate(0, 0, pixel * 0.5); // move it in front of the jar
            poseStack.scale(1F, 1F, 0.01F); // flatten
            this.submitItem(labelStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);
            poseStack.popPose();

            // Render Contained Item
            ItemStack containedStack = ItemStack.EMPTY;
            if (stack.getItem() instanceof AlchemicalDerivativeItem item) {
                containedStack = item.getSourceStack(stack);
            }

            if (!containedStack.isEmpty()) {
                poseStack.pushPose();

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

                this.submitItem(containedStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);

                poseStack.popPose();
            }
        }
    }

    /**
     * Renders the contained item at full size when Shift is held in GUI.
     */
    private void renderContainedItemFull(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, int outlineColor) {
        if (!(stack.getItem() instanceof AlchemicalDerivativeItem item))
            return;

        var containedStack = item.getSourceStack(stack);
        if (!containedStack.isEmpty()) {
            this.submitItem(containedStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);
        }
    }

    private void submitItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, int outlineColor) {
        ItemStackRenderState renderState = new ItemStackRenderState();
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getItemModelResolver().updateForTopItem(renderState, itemStack, displayContext, minecraft.level, null, 0);
        renderState.submit(poseStack, submitNodeCollector, light, overlay, outlineColor);
    }

    @Override
    public @Nullable ItemStack extractArgument(ItemStack stack) {
        return stack;
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(-0.5f, -0.5f, -0.5f));
        output.accept(new Vector3f(0.5f, 0.5f, 0.5f));
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<ItemStack> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public @Nullable SpecialModelRenderer<ItemStack> bake(SpecialModelRenderer.BakingContext context) {
            return new AlchemicalDerivativeRenderer();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<ItemStack>> type() {
            return MAP_CODEC;
        }
    }
}
