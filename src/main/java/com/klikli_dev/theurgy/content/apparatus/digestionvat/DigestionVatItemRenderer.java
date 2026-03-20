// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Set;

public class DigestionVatItemRenderer implements net.minecraft.client.renderer.special.SpecialModelRenderer<ItemStack> {
    private static final DigestionVatBlockEntity blockEntity = new DigestionVatBlockEntity(BlockPos.ZERO, BlockRegistry.DIGESTION_VAT.get().defaultBlockState());

    @Override
    public void render(@org.jetbrains.annotations.Nullable ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, boolean hasFoil) {
        var renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);
        if (renderer != null) {
            renderer.render(blockEntity, 0, poseStack, bufferSource, light, overlay, Vec3.ZERO);
        }
    }

    @Override
    public @org.jetbrains.annotations.Nullable ItemStack extractArgument(ItemStack stack) {
        return stack;
    }

    @Override
    public void getExtents(Set<Vector3f> extents) {
        extents.add(new Vector3f(-0.5f, -0.5f, -0.5f));
        extents.add(new Vector3f(0.5f, 0.5f, 0.5f));
    }

    public record Unbaked() implements net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked {
        public static final com.mojang.serialization.MapCodec<Unbaked> MAP_CODEC = com.mojang.serialization.MapCodec.unit(new Unbaked());

        @Override
        public net.minecraft.client.renderer.special.SpecialModelRenderer<?> bake(net.minecraft.client.model.geom.EntityModelSet modelSet) {
            return new DigestionVatItemRenderer();
        }

        @Override
        public com.mojang.serialization.MapCodec<? extends net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
