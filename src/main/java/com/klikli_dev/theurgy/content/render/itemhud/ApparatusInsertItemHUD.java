package com.klikli_dev.theurgy.content.render.itemhud;

import com.klikli_dev.theurgy.content.behaviour.itemhandler.HasItemHandlerBehaviour;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;

public class ApparatusInsertItemHUD {

    private static final ApparatusInsertItemHUD instance = new ApparatusInsertItemHUD();

    private ItemInteractionResult lastInteractionResult = null;
    private BlockPos lastHitBlockPos = null;
    private ItemStack lastStack = ItemStack.EMPTY;

    public static ApparatusInsertItemHUD get() {
        return instance;
    }

    public void tick(Player player) {
        var hitResult = Minecraft.getInstance().hitResult;
        var level = Minecraft.getInstance().level;
        var stack = player.getMainHandItem();

        if (stack.isEmpty()) {
            this.reset();
            return;
        }

        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
            var blockPos = blockHitResult.getBlockPos();
            var blockState = level.getBlockState(blockPos);
            if (blockState.getBlock() instanceof HasItemHandlerBehaviour<?> hasItemHandlerBehaviour) {

                if (!blockPos.equals(this.lastHitBlockPos) || !ItemStack.isSameItemSameComponents(stack, this.lastStack)) {
                    this.lastHitBlockPos = blockPos;
                    this.lastStack = stack;
                    this.lastInteractionResult = hasItemHandlerBehaviour.itemHandlerBehaviour().useItemOn(stack, blockState, level, blockPos, player, InteractionHand.MAIN_HAND, blockHitResult, true);
                }

            } else {
                this.reset();
            }
        } else {
            this.reset();
        }

        if (this.lastInteractionResult != null && this.lastHitBlockPos != null) {
            if (this.lastInteractionResult == ItemInteractionResult.SUCCESS) {
                Outliner.get().showAABB(this.lastHitBlockPos, Shapes.block().bounds()
                                .move(this.lastHitBlockPos))
                        .colored(Color.GREEN)
                        .lineWidth(1 / 32f);
            } else {
                Outliner.get().showAABB(this.lastHitBlockPos, Shapes.block().bounds()
                                .move(this.lastHitBlockPos))
                        .colored(Color.RED)
                        .lineWidth(1 / 32f);
            }
        }
    }

    private void reset() {
        if (this.lastHitBlockPos != null)
            Outliner.get().remove(this.lastHitBlockPos);

        this.lastInteractionResult = null;
        this.lastHitBlockPos = null;
        this.lastStack = ItemStack.EMPTY;
    }
}
