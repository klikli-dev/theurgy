// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.mode.EnabledSetter;
import com.klikli_dev.theurgy.content.item.mode.ItemModeRenderHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SwitchLogisticsEnabledMode extends MercurialWandItemMode {

    private final Lazy<ItemModeRenderHandler<SwitchLogisticsEnabledMode>> renderHandler;

    protected SwitchLogisticsEnabledMode() {
        super();
        //We're using a lazy so that on the serverside we don't construct the client-only renderer UNLESS code actually calls it in which case we will crash which is fine as a warning.
        this.renderHandler = Lazy.of(() -> new ItemModeRenderHandler<>(this));
    }

    @Override
    public String descriptionId() {
        return TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED;
    }

    @Override
    public void appendHUDText(Player pPlayer, HitResult pHitResult, ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents) {
        var description = this.description(pStack, pLevel);
        if (pHitResult instanceof BlockHitResult blockHitResult) {
            var blockEntity = pLevel.getBlockEntity(blockHitResult.getBlockPos());
            if (blockEntity instanceof EnabledSetter enabledSetter) {
                var enabled = enabledSetter.enabled();

                var component = Component.translatable(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED_HUD,
                        Component.translatable(
                                enabled ? TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_ENABLED :
                                        TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_DISABLED
                        ).withStyle(
                                enabled ? ChatFormatting.GREEN :
                                        ChatFormatting.RED
                        )

                );

                description.append(component);
            }
        }
        pTooltipComponents.add(description);
    }

    @Override
    public ItemModeRenderHandler<?> renderHandler() {
        return this.renderHandler.get();
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        //get the target block and set its direction if it is a fitting block

        var blockPos = context.getClickedPos();
        var level = context.getLevel();

        var blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof EnabledSetter enabledSetter) {

            if (!level.isClientSide) {
                enabledSetter.enabled(!enabledSetter.enabled());
                var enabled = enabledSetter.enabled();

                context.getPlayer().displayClientMessage(Component.translatable(TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_SWITCH_LOGISTICS_ENABLED_SUCCESS,
                        Component.translatable(
                                enabled ? TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_ENABLED :
                                        TheurgyConstants.I18n.Item.Mode.MERCURIAL_WAND_DISABLED
                        ).withStyle(
                                enabled ? ChatFormatting.GREEN :
                                        ChatFormatting.RED
                        )
                ), true);
            }

            return InteractionResult.SUCCESS;
        }


        return super.onItemUseFirst(stack, context);
    }
}
