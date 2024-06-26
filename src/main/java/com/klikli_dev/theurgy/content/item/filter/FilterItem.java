package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterItem extends Item implements MenuProvider {
    public FilterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getPlayer() == null)
            return InteractionResult.PASS;
        return this.use(pContext.getLevel(), pContext.getPlayer(), pContext.getHand()).getResult();
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack heldItem = pPlayer.getItemInHand(pUsedHand);

        if (!pPlayer.isShiftKeyDown() && pUsedHand == InteractionHand.MAIN_HAND) {
            if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer)
                serverPlayer.openMenu(this, buf -> {
                    ItemStack.STREAM_CODEC.encode(buf, heldItem);
                });
            return InteractionResultHolder.success(heldItem);
        }

        return InteractionResultHolder.pass(heldItem);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        if (!Screen.hasShiftDown()) {
            List<Component> makeSummary = this.makeSummary(pStack);
            if (makeSummary.isEmpty())
                return;
            pTooltipComponents.add(Component.literal(" ")); //empty line.
            pTooltipComponents.addAll(makeSummary);
        }
    }

    private List<Component> makeSummary(ItemStack filter) {
        List<Component> list = new ArrayList<>();

        if (!filter.has(DataComponentRegistry.FILTER_ITEMS))
            return list;

        var filterItems = filter.get(DataComponentRegistry.FILTER_ITEMS);
        boolean blacklist = filter.getOrDefault(DataComponentRegistry.FILTER_IS_DENY_LIST, false);

        list.add((blacklist ?
                Component.translatable(TheurgyConstants.I18n.Gui.FILTER_DENY_LIST_BUTTON_TOOLTIP)
                : Component.translatable(TheurgyConstants.I18n.Gui.FILTER_ACCEPT_LIST_BUTTON_TOOLTIP)).withStyle(ChatFormatting.GOLD));

        int count = 0;
        for (int i = 0; i < filterItems.getSlots(); i++) {
            if (count > 3) {
                list.add(Component.literal("- ...")
                        .withStyle(ChatFormatting.DARK_GRAY));
                break;
            }

            ItemStack filterStack = filterItems.getStackInSlot(i);
            if (filterStack.isEmpty())
                continue;
            list.add(Component.literal("- ")
                    .append(filterStack.getHoverName())
                    .withStyle(ChatFormatting.GRAY));
            count++;
        }

        if (count == 0)
            return Collections.emptyList();

        return list;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.getDescription();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return ListFilterMenu.create(pContainerId, pPlayerInventory, pPlayer.getMainHandItem());
    }
}
