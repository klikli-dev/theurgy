package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.mode.TargetDirectionSetter;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class SetSelectedDirectionMode extends MercurialWandItemMode {

    protected SetSelectedDirectionMode() {
        super();
    }

    @Override
    public String descriptionId() {
        return TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SET_SELECTED_DIRECTION;
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
            directionSettable.targetDirection(this.getDirection(stack));
            return InteractionResult.SUCCESS;
        }

        //TODO: implement the TargetDirectionSetter interface in our logistics block entities - or rather their behaviours to also store the override

        return super.onItemUseFirst(stack, context);
    }
}
