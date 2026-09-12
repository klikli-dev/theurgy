// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.fluids.FluidStack;

public class SalAmmoniacTankGameTests {

    private static final BlockPos TANK_POS = new BlockPos(2, 2, 2);

    // --- Placement & State ---

    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(TANK_POS, BlockRegistry.SAL_AMMONIAC_TANK.get());
        helper.assertBlockPresent(BlockRegistry.SAL_AMMONIAC_TANK.get(), TANK_POS);
        helper.succeed();
    }

    // --- Fluid Handling ---

    public static void insertSalAmmoniacFluid(GameTestHelper helper) {
        helper.setBlock(TANK_POS, BlockRegistry.SAL_AMMONIAC_TANK.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(TANK_POS, SalAmmoniacTankBlockEntity.class);
            var filled = blockEntity.tank.fill(
                    new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1000), null
            );
            helper.assertTrue(filled > 0, "Tank should accept sal ammoniac fluid");
            helper.assertTrue(blockEntity.tank.getFluidAmount() == 1000, "Tank should contain 1000mb");
            helper.succeed();
        });
    }

    public static void extractSalAmmoniacFluid(GameTestHelper helper) {
        helper.setBlock(TANK_POS, BlockRegistry.SAL_AMMONIAC_TANK.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(TANK_POS, SalAmmoniacTankBlockEntity.class);
            blockEntity.tank.fill(new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1000), null);
        });

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(TANK_POS, SalAmmoniacTankBlockEntity.class);
            var drained = blockEntity.tank.drain(500, null);
            helper.assertTrue(drained.getAmount() == 500, "Should drain 500mb");
            helper.assertTrue(blockEntity.tank.getFluidAmount() == 500, "Tank should have 500mb remaining");
            helper.succeed();
        });
    }

    public static void fluidPersistence(GameTestHelper helper) {
        helper.setBlock(TANK_POS, BlockRegistry.SAL_AMMONIAC_TANK.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(TANK_POS, SalAmmoniacTankBlockEntity.class);
            blockEntity.tank.fill(new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1500), null);
            helper.assertTrue(blockEntity.tank.getFluidAmount() == 1500, "Tank should contain 1500mb");
            helper.succeed();
        });
    }
}
