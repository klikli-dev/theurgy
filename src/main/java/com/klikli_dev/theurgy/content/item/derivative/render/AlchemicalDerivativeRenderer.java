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
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Map;
import java.util.function.Consumer;

public class AlchemicalDerivativeRenderer implements SpecialModelRenderer<ItemStack> {

    private static final ItemStack labeledEmptyJarStack = new ItemStack(ItemRegistry.EMPTY_JAR_LABELED_ICON.get());
    private static final Map<AlchemicalDerivativeTier, ItemStack> tierToIconMap = Map.of(
            AlchemicalDerivativeTier.ABUNDANT, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON.get()),
            AlchemicalDerivativeTier.COMMON, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON.get()),
            AlchemicalDerivativeTier.RARE, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_RARE_ICON.get()),
            AlchemicalDerivativeTier.PRECIOUS, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON.get())
    );

    @Override
    public void submit(@Nullable ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, boolean hasFoil, int outlineColor) {
        if (stack == null || stack.isEmpty()) return;

        boolean renderSource = ClientConfig.get().rendering.renderSulfurSourceItem.get();

        var jarStack = renderSource ? AlchemicalDerivativeItem.getEmptyJarStack(stack) : labeledEmptyJarStack;

        // Render Jar
        this.submitItem(jarStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);

        // Render Frame
        // AlchemicalDerivativeItem.getTier is static
        var tierStack = tierToIconMap.get(AlchemicalDerivativeItem.getTier(stack));
        if (tierStack != null) {
            this.submitItem(tierStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);
        }

        if (renderSource) {
            // Render Label
            var labelStack = new ItemStack(ItemRegistry.JAR_LABEL_ICON.get());
            this.submitItem(labelStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);

            // Render Contained Item
            ItemStack containedStack = ItemStack.EMPTY;
            if (stack.getItem() instanceof AlchemicalDerivativeItem item) {
                containedStack = item.getSourceStack(stack);
            }

            if (!containedStack.isEmpty()) {
                poseStack.pushPose();

                // Mimic the transforms from BEWLR
                float pixel = 1f / 16f;
                // Original transform logic comment:
                poseStack.translate(0, 0, pixel * 0.6); 
                poseStack.translate(0, -pixel * 3.2, 0); 
                poseStack.scale(0.74F, 0.74F, 0.01F);


                this.submitItem(containedStack, displayContext, poseStack, submitNodeCollector, light, overlay, outlineColor);

                poseStack.popPose();
            }
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
