package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DigestionVatBEWLR extends BlockEntityWithoutLevelRenderer {
    private static final DigestionVatBEWLR instance = new DigestionVatBEWLR();
    private static final DigestionVatBlockEntity blockEntity = new DigestionVatBlockEntity(BlockPos.ZERO, BlockRegistry.DIGESTION_VAT.get().defaultBlockState());
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public DigestionVatBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
    }

    public static DigestionVatBEWLR get() {
        return instance;
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        this.blockEntityRenderDispatcher.renderItem(blockEntity, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }
}
