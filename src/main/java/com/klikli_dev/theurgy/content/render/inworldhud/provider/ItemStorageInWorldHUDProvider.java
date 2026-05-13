// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud.provider;

import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDBuilder;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDProvider;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jetbrains.annotations.Nullable;

public class ItemStorageInWorldHUDProvider implements InWorldHUDProvider {

    @Override
    public boolean activatesHUD() {
        return false;
    }

    @Override
    public boolean applies(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        return level.getCapability(CapabilityRegistry.ITEM_HANDLER, pos, state, blockEntity, null) != null;
    }

    @Override
    public void appendServerData(InWorldHUDBuilder builder, ServerPlayer player, ServerLevel level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        ResourceHandler<ItemResource> itemHandler = level.getCapability(CapabilityRegistry.ITEM_HANDLER, pos, state, blockEntity, null);
        if (itemHandler == null) {
            return;
        }

        for (int slot = 0; slot < itemHandler.size(); slot++) {
            builder.addItem(ItemUtil.getStack(itemHandler, slot));
        }
    }
}
