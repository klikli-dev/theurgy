// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud.provider;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDBuilder;
import com.klikli_dev.theurgy.content.render.inworldhud.InWorldHUDProvider;
import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jetbrains.annotations.Nullable;

public class FluidStorageInWorldHUDProvider implements InWorldHUDProvider {

    @Override
    public boolean activatesHUD() {
        return false;
    }

    @Override
    public boolean applies(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        return level.getCapability(CapabilityRegistry.FLUID_HANDLER, pos, state, blockEntity, null) != null;
    }

    @Override
    public void appendServerData(InWorldHUDBuilder builder, ServerPlayer player, ServerLevel level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        ResourceHandler<FluidResource> fluidHandler = level.getCapability(CapabilityRegistry.FLUID_HANDLER, pos, state, blockEntity, null);
        if (fluidHandler == null) {
            return;
        }

        for (int tank = 0; tank < FluidStorageHelper.getTanks(fluidHandler); tank++) {
            var fluidStack = FluidStorageHelper.getFluidInTank(fluidHandler, tank);
            int amount = fluidStack.getAmount();
            int capacity = FluidStorageHelper.getTankCapacity(fluidHandler, tank);
            var displayName = fluidStack.isEmpty() ? Component.translatable("container.empty") : fluidStack.getHoverName();
            var amountText = Component.translatable(TheurgyConstants.I18n.Misc.UNIT_MILLIBUCKETS, amount + " / " + capacity);

            builder.addLine(Component.empty()
                    .append(displayName)
                    .append(Component.literal(" ("))
                    .append(amountText)
                    .append(Component.literal(")"))
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
