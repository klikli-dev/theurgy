package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SelectDirectionMode extends MercurialWandItemMode {

    protected SelectDirectionMode() {
        super();
    }

    @Override
    public String descriptionId() {
        return TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SELECT_DIRECTION;
    }

    @Override
    public boolean supportsScrollWithRightDown() {
        return true;
    }

    @Override
    public void onScrollWithRightDown(Player player, ItemStack stack, int shift) {
        super.onScrollWithRightDown(player, stack, shift);

        var modeTag = this.getModeTag(stack);
        if (!modeTag.contains("direction")) {
            modeTag.putInt("direction", Direction.fromYRot(player.getYRot()).ordinal());
        } else {
            modeTag.putInt("direction", this.shiftDirection(this.getDirection(modeTag), shift).ordinal());
        }
    }

    public Direction getDirection(CompoundTag modeTag) {
        return Direction.values()[modeTag.getInt("direction")];
    }

    public Direction getDirection(ItemStack stack) {
        return this.getDirection(this.getModeTag(stack));
    }

    protected Direction nextDirection(Direction direction) {
        int next = direction.ordinal() + 1;
        if (next >= Direction.values().length) {
            next = 0;
        }
        return Direction.values()[next];
    }

    protected Direction previousDirection(Direction direction) {
        int previous = direction.ordinal() - 1;
        if (previous < 0) {
            previous = Direction.values().length - 1;
        }
        return Direction.values()[previous];
    }

    public Direction shiftDirection(Direction direction, int shift) {
        if (shift > 0) {
            return this.nextDirection(direction);
        } else if (shift < 0) {
            return this.previousDirection(direction);
        }
        return direction;
    }
}
