package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.mode.EnabledSetter;
import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import com.klikli_dev.theurgy.content.item.mode.TargetDirectionSetter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetSelectedDirectionMode extends MercurialWandItemMode {

    private SetSelectedDirectionModeRenderHandler renderHandler;

    protected SetSelectedDirectionMode() {
        super();
        this.renderHandler = new SetSelectedDirectionModeRenderHandler(this);
    }

    @Override
    public String descriptionId() {
        return TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_DIRECTION;
    }

    @Override
    public MutableComponent description(ItemStack pStack, @Nullable Level pLevel) {
        return Component.translatable(
                this.descriptionId(),
                Component.translatable(this.getDirection(pStack).getName()).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void appendHUDText(Player pPlayer, HitResult pHitResult, ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        var description = this.description(pStack, pLevel);
        if (pHitResult instanceof BlockHitResult blockHitResult) {
            var blockEntity = pLevel.getBlockEntity(blockHitResult.getBlockPos());
            if (blockEntity instanceof TargetDirectionSetter directionSettable) {
                var currentDirection = directionSettable.targetDirection();
                var newDirection = this.getDirection(pStack);

                var component = Component.translatable(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_DIRECTION_WITH_TARGET,
                        Component.translatable(currentDirection.getName()).withStyle(currentDirection != newDirection ? ChatFormatting.YELLOW : ChatFormatting.GREEN),
                        Component.translatable(newDirection.getName()).withStyle(ChatFormatting.GREEN)
                );

                description = component;
            }
        }
        pTooltipComponents.add(description );
    }

    @Override
    public SetSelectedDirectionModeRenderHandler renderHandler() {
        return this.renderHandler;
    }

    public Direction getDirection(ItemStack stack) {
        var mode = (SelectDirectionMode) Type.SELECT_DIRECTION.mode();
        return mode.getDirection(stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        //get the target block and set its direction if it is a fitting block

        var blockPos = context.getClickedPos();
        var level = context.getLevel();

        var blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof TargetDirectionSetter directionSettable) {
            if(!level.isClientSide){
                var direction = this.getDirection(stack);
                directionSettable.targetDirection(direction);

                context.getPlayer().displayClientMessage(Component.translatable(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_DIRECTION_SUCCESS,
                        Component.translatable(direction.getName()).withStyle(ChatFormatting.GREEN)
                ), true);
            }
            return InteractionResult.SUCCESS;
        }

        //TODO: when looking at valid target block its current direction should be
        //      to that end we need to know its target block
        //      Concrete: We need to sync most node data to the client!
        //TODO: we should also show the new direction in a different color


        return super.onItemUseFirst(stack, context);
    }
}
