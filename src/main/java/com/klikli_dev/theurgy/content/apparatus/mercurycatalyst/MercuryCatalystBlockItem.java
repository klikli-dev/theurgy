// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycatalyst;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MercuryCatalystBlockItem extends BlockItem {

    public MercuryCatalystBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, tooltipFlag);

        if (stack.has(DataComponentRegistry.MERCURY_FLUX_STORAGE.get())) {
            int stored = stack.get(DataComponentRegistry.MERCURY_FLUX_STORAGE.get());
            int capacity = MercuryCatalystBlockEntity.CAPACITY;

            tooltipAdder.accept(Component.translatable(
                    TheurgyConstants.I18n.JEI.MERCURY_FLUX,
                    stored + " / " + capacity
            ).withStyle(ChatFormatting.GRAY));
        }
    }
}
