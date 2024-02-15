package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

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

}
