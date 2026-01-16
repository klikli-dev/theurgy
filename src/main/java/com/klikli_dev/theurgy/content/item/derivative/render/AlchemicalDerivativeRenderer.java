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
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AlchemicalDerivativeRenderer implements SpecialModelRenderer<ItemStack> {

    private static final ItemStack labeledEmptyJarStack = new ItemStack(ItemRegistry.EMPTY_JAR_LABELED_ICON.get());
    private static final Map<AlchemicalDerivativeTier, ItemStack> tierToIconMap = Map.of(
            AlchemicalDerivativeTier.ABUNDANT, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON.get()),
            AlchemicalDerivativeTier.COMMON, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON.get()),
            AlchemicalDerivativeTier.RARE, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_RARE_ICON.get()),
            AlchemicalDerivativeTier.PRECIOUS, new ItemStack(ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON.get())
    );

    @Override
    public void render(@Nullable ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, boolean hasFoil) {
        if (stack == null || stack.isEmpty()) return;

        boolean renderSource = ClientConfig.get().rendering.renderSulfurSourceItem.get();
        var itemRenderer = Minecraft.getInstance().getItemRenderer();

        var jarStack = renderSource ? AlchemicalDerivativeItem.getEmptyJarStack(stack) : labeledEmptyJarStack;

        // Render Jar
        itemRenderer.renderStatic(jarStack, displayContext, light, overlay, poseStack, bufferSource, null, 0);

        // Render Frame
        // AlchemicalDerivativeItem.getTier is static
        var tierStack = tierToIconMap.get(AlchemicalDerivativeItem.getTier(stack));
        if (tierStack != null) {
            itemRenderer.renderStatic(tierStack, displayContext, light, overlay, poseStack, bufferSource, null, 0);
        }

        if (renderSource) {
            // Render Label
            var labelStack = new ItemStack(ItemRegistry.JAR_LABEL_ICON.get());
            itemRenderer.renderStatic(labelStack, displayContext, light, overlay, poseStack, bufferSource, null, 0);

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


                itemRenderer.renderStatic(containedStack, displayContext, light, overlay, poseStack, bufferSource, null, 0);

                poseStack.popPose();
            }
        }
    }

    @Override
    public @Nullable ItemStack extractArgument(ItemStack stack) {
        return stack;
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public @Nullable SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new AlchemicalDerivativeRenderer();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
