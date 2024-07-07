package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListFilterItem extends FilterItem {
    public ListFilterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected List<Component> makeSummary(ItemStack filter, HolderLookup.Provider provider) {
        List<Component> list = new ArrayList<>();

        if (!filter.has(DataComponentRegistry.FILTER_ITEMS))
            return list;

        var filterItems = filter.get(DataComponentRegistry.FILTER_ITEMS);
        boolean blacklist = filter.getOrDefault(DataComponentRegistry.FILTER_IS_DENY_LIST, false);

        list.add((blacklist ?
                Component.translatable(TheurgyConstants.I18n.Gui.LIST_FILTER_DENY_LIST_BUTTON_TOOLTIP)
                : Component.translatable(TheurgyConstants.I18n.Gui.LIST_FILTER_ACCEPT_LIST_BUTTON_TOOLTIP)).withStyle(ChatFormatting.GOLD));

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

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return ListFilterMenu.create(pContainerId, pPlayerInventory, pPlayer.getMainHandItem());
    }
}
