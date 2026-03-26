// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SulfuricFluxEmitterGameTests {

    private static final BlockPos EMITTER_POS = new BlockPos(0, 1, 0); // Structures usually start at 0, 1, 0 if 0, 0, 0 is the floor

    // --- Placement & State ---

    public static void placementWithFacing(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));

        helper.assertBlockPresent(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), EMITTER_POS);
        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.FACING, Direction.UP);
        helper.succeed();
    }

    public static void defaultEnabledState(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get());

        helper.assertBlockPresent(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), EMITTER_POS);
        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.ENABLED, true);
        helper.succeed();
    }

    // --- Redstone Interaction ---

    public static void redstoneDisablesEmitter(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get());
        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.ENABLED, true);

        helper.runAfterDelay(1, () -> {
            helper.setBlock(EMITTER_POS.below(), Blocks.REDSTONE_BLOCK);
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.ENABLED, false);
        });
    }

    // --- Energy ---

    public static void hasEnergyStorage(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(EMITTER_POS, SulfuricFluxEmitterBlockEntity.class);
            helper.assertTrue(blockEntity != null, "Block entity should exist");
            helper.assertTrue(blockEntity.mercuryFluxStorage.getMaxEnergyStored() > 0, "Should have energy capacity");
            helper.succeed();
        });
    }
}
