// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud;

import com.klikli_dev.theurgy.content.render.inworldhud.provider.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InWorldHUDRegistry {

    private static final Map<Block, List<InWorldHUDProvider>> BLOCK_PROVIDERS = new HashMap<>();
    private static final List<InWorldHUDProvider> GENERIC_PROVIDERS = new ArrayList<>();

    private static boolean defaultsRegistered;

    public static void registerDefaults() {
        if (defaultsRegistered) {
            return;
        }

        registerGenericProvider(new BlockTitleInWorldHUDProvider());
        registerGenericProvider(new GenericCraftingProgressInWorldHUDProvider());
        registerGenericProvider(new MercuryFluxHandlerInWorldHUDProvider());
        registerGenericProvider(new FluidStorageInWorldHUDProvider());
        registerGenericProvider(new ItemStorageInWorldHUDProvider());

        registerBlockProvider(MercuryCatalystInWorldHUDProvider.appliesBlock(), new MercuryCatalystInWorldHUDProvider());

        defaultsRegistered = true;
    }

    public static void registerBlockProvider(Block block, InWorldHUDProvider provider) {
        BLOCK_PROVIDERS.computeIfAbsent(block, $ -> new ArrayList<>()).add(provider);
    }

    public static void registerGenericProvider(InWorldHUDProvider provider) {
        GENERIC_PROVIDERS.add(provider);
    }

    public static boolean hasProviders(Level level, BlockPos pos) {
        return !getApplicableProviders(level, pos).isEmpty();
    }

    public static InWorldHUDSnapshot gatherClientSnapshot(Level level, BlockPos pos) {
        List<InWorldHUDProvider> providers = getApplicableProviders(level, pos);
        if (providers.isEmpty()) {
            return InWorldHUDSnapshot.EMPTY;
        }

        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        InWorldHUDBuilder builder = new InWorldHUDBuilder();
        providers.forEach(provider -> provider.appendClientData(builder, level, pos, state, blockEntity));
        return builder.build();
    }

    public static InWorldHUDSnapshot gatherServerSnapshot(ServerPlayer player, ServerLevel level, BlockPos pos) {
        List<InWorldHUDProvider> providers = getApplicableProviders(level, pos);
        if (providers.isEmpty()) {
            return InWorldHUDSnapshot.EMPTY;
        }

        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        InWorldHUDBuilder builder = new InWorldHUDBuilder();
        providers.forEach(provider -> provider.appendServerData(builder, player, level, pos, state, blockEntity));
        return builder.build();
    }

    private static List<InWorldHUDProvider> getApplicableProviders(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        List<InWorldHUDProvider> applicableBlockProviders = new ArrayList<>();
        boolean hasActivatingProvider = false;

        for (InWorldHUDProvider provider : BLOCK_PROVIDERS.getOrDefault(state.getBlock(), List.of())) {
            if (!provider.applies(level, pos, state, blockEntity)) {
                continue;
            }

            applicableBlockProviders.add(provider);
            hasActivatingProvider |= provider.activatesHUD();
        }

        List<InWorldHUDProvider> applicableGenericProviders = new ArrayList<>();
        for (InWorldHUDProvider provider : GENERIC_PROVIDERS) {
            if (!provider.applies(level, pos, state, blockEntity)) {
                continue;
            }

            applicableGenericProviders.add(provider);
            hasActivatingProvider |= provider.activatesHUD();
        }

        if (!hasActivatingProvider) {
            return List.of();
        }

        List<InWorldHUDProvider> result = new ArrayList<>(applicableBlockProviders);
        result.addAll(applicableGenericProviders);
        return result;
    }
}
