// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SulfuricFluxEmitterGameTests {

    private static final BlockPos EMITTER_POS = new BlockPos(0, 1, 0);

    // --- Placement & State ---

    public static void placementWithFacing(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));

        helper.assertBlockPresent(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), EMITTER_POS);
        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.FACING, Direction.UP);
        helper.succeed();
    }

    // --- Energy ---

    public static void hasEnergyStorage(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get());

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(EMITTER_POS, SulfuricFluxEmitterBlockEntity.class);
            helper.assertTrue(blockEntity != null, "Block entity should exist");
            helper.assertTrue(blockEntity.mercuryFluxStorage.getMaxEnergyStored() == SulfuricFluxEmitterBlockEntity.CAPACITY, "Should have correct energy capacity (" + SulfuricFluxEmitterBlockEntity.CAPACITY + ")");
        });
    }
}


