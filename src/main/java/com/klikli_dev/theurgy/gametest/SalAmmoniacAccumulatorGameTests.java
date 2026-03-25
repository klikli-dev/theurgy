// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class SalAmmoniacAccumulatorGameTests {

    private static final BlockPos ACCUMULATOR_POS = new BlockPos(2, 2, 2);

    // --- Placement & State ---

    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(ACCUMULATOR_POS, BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
        helper.assertBlockPresent(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), ACCUMULATOR_POS);
        helper.succeed();
    }

    // --- Item & Fluid Handling ---

    public static void insertSalAmmoniacItem(GameTestHelper helper) {
        helper.setBlock(ACCUMULATOR_POS, BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(ACCUMULATOR_POS, SalAmmoniacAccumulatorBlockEntity.class);
            var remainder = blockEntity.inventory.insertItem(0, new ItemStack(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get(), 1), false);
            helper.assertTrue(remainder.isEmpty(), "Sal ammoniac crystal should be accepted");
            helper.assertTrue(!blockEntity.inventory.getStackInSlot(0).isEmpty(), "Inventory should contain item");
            helper.succeed();
        });
    }

    public static void insertWater(GameTestHelper helper) {
        helper.setBlock(ACCUMULATOR_POS, BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(ACCUMULATOR_POS, SalAmmoniacAccumulatorBlockEntity.class);
            var filled = blockEntity.waterTank.fill(
                    new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 1000), false
            );
            helper.assertTrue(filled > 0, "Water tank should accept water");
            helper.succeed();
        });
    }
}
