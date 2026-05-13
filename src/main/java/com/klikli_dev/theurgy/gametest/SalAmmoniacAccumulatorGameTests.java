// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.SalAmmoniacAccumulatorBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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
            ItemStack remainder;
            try (var tx = Transaction.openRoot()) {
                remainder = ItemUtil.insertItemReturnRemaining(blockEntity.inventory, 0, new ItemStack(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get(), 1), false, tx);
                tx.commit();
            }
            helper.assertTrue(remainder.isEmpty(), "Sal ammoniac crystal should be accepted");
            helper.assertTrue(!ItemUtil.getStack(blockEntity.inventory, 0).isEmpty(), "Inventory should contain item");
            helper.succeed();
        });
    }

    public static void insertWater(GameTestHelper helper) {
        helper.setBlock(ACCUMULATOR_POS, BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(ACCUMULATOR_POS, SalAmmoniacAccumulatorBlockEntity.class);
            var filled = blockEntity.waterTank.fill(
                    new FluidStack(Fluids.WATER, 1000),
                    false
            );
            helper.assertTrue(filled == 1000, "Water tank should accept 1000mb of water");
            helper.assertTrue(blockEntity.waterTank.getFluidAmount() == 1000, "Water tank should contain 1000mb of water");
            helper.succeed();
        });
    }
}
