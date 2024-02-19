package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class SelectDirectionMode extends MercurialWandItemMode {

    private ItemModeRenderHandler renderHandler;

    protected SelectDirectionMode() {
        super();
        this.renderHandler = new ItemModeRenderHandler();
    }

    @Override
    public String descriptionId() {
        return TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SELECT_DIRECTION;
    }

    @Override
    public ItemModeRenderHandler renderHandler() {
        return this.renderHandler;
    }

    @Override
    public boolean supportsScrollWithRightDown() {
        return true;
    }

    @Override
    public void onScrollWithRightDown(Player player, ItemStack stack, int shift) {
        super.onScrollWithRightDown(player, stack, shift);

        var modeTag = this.getModeTag(stack);
        var direction = !modeTag.contains("direction") ? Direction.fromYRot(player.getYRot()) : this.shiftDirection(this.getDirection(modeTag), shift);

        player.displayClientMessage(Component.translatable(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SELECT_DIRECTION_SUCCESS,
                Component.translatable(direction.getName()).withStyle(ChatFormatting.GREEN)
        ), true);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        //the select direction mode does not have a "use" function, it just listens to the scroll
        return InteractionResult.CONSUME; //we need to consume because SUCCESS causes a swing animation
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
