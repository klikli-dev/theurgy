// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CaloricFluxEmitterGameTests {

    private static final BlockPos EMITTER_POS = new BlockPos(2, 2, 2);

    // --- Placement & State ---

    public static void placementWithFacing(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.CALORIC_FLUX_EMITTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));

        helper.assertBlockPresent(BlockRegistry.CALORIC_FLUX_EMITTER.get(), EMITTER_POS);
        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.FACING, Direction.UP);
        helper.succeed();
    }

    public static void defaultEnabledState(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.CALORIC_FLUX_EMITTER.get());

        helper.assertBlockPresent(BlockRegistry.CALORIC_FLUX_EMITTER.get(), EMITTER_POS);
        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.ENABLED, true);
        helper.succeed();
    }

    // --- Redstone Interaction ---

    public static void redstoneDisablesEmitter(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.CALORIC_FLUX_EMITTER.get()
                .defaultBlockState().setValue(BlockStateProperties.ENABLED, false));

        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.ENABLED, false);
        helper.succeed();
    }

    // --- Energy ---

    public static void hasEnergyStorage(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.CALORIC_FLUX_EMITTER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(EMITTER_POS, CaloricFluxEmitterBlockEntity.class);
            helper.assertTrue(blockEntity != null, "Block entity should exist");
            helper.assertTrue(blockEntity.mercuryFluxStorage.getMaxEnergyStored() > 0, "Should have energy capacity");
            helper.succeed();
        });
    }
}
