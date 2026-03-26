// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatBlock;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;

public class DigestionVatGameTests {

    private static final BlockPos VAT_POS = new BlockPos(2, 2, 2);

    // --- Placement & State ---

    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.DIGESTION_VAT.get());

        helper.assertBlockPresent(BlockRegistry.DIGESTION_VAT.get(), VAT_POS);
        helper.assertBlockProperty(VAT_POS, BlockStateProperties.OPEN, true);
        helper.succeed();
    }

    public static void placementWithFacing(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.DIGESTION_VAT.get()
                .defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));

        helper.assertBlockPresent(BlockRegistry.DIGESTION_VAT.get(), VAT_POS);
        helper.assertBlockProperty(VAT_POS, BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
        helper.succeed();
    }

    // --- Item & Fluid Handling ---

    public static void insertInputItem(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.DIGESTION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, DigestionVatBlockEntity.class);
            var remainder = blockEntity.storageBehaviour.inputInventory.insertItem(0, new ItemStack(Items.COBBLESTONE, 1), false);
            helper.assertTrue(remainder.isEmpty(), "Item should be accepted as input");
            helper.assertTrue(!blockEntity.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty(), "Input should contain item");
            helper.succeed();
        });
    }

    public static void insertFluid(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.DIGESTION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, DigestionVatBlockEntity.class);
            var filled = blockEntity.storageBehaviour.fluidTank.fill(
                    new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1000), false
            );
            helper.assertTrue(filled == 1000, "Fluid tank should accept 1000mb of fluid");
            helper.assertTrue(blockEntity.storageBehaviour.fluidTank.getFluidAmount() == 1000, "Fluid tank should contain 1000mb of fluid");
            helper.succeed();
        });
    }

    public static void extractOutputItem(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.DIGESTION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, DigestionVatBlockEntity.class);
            blockEntity.storageBehaviour.outputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 1));
        });

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, DigestionVatBlockEntity.class);
            var extracted = blockEntity.storageBehaviour.outputInventory.extractItem(0, 1, false);
            helper.assertTrue(!extracted.isEmpty(), "Should extract from output");
            helper.assertTrue(blockEntity.storageBehaviour.outputInventory.getStackInSlot(0).isEmpty(), "Output should be empty");
            helper.succeed();
        });
    }

    // --- Processing ---

    public static void vatCanBeClosed(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.DIGESTION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var level = helper.getLevel();
            var absPos = helper.absolutePos(VAT_POS);
            level.setBlock(absPos, level.getBlockState(absPos).setValue(BlockStateProperties.OPEN, false), 3);
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(VAT_POS, BlockStateProperties.OPEN, false);
        });
    }

    public static void dropsItemsWhenBroken(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.DIGESTION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, DigestionVatBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 3));
        });

        helper.runAfterDelay(2, () -> {
            helper.destroyBlock(VAT_POS);
        });

        helper.succeedWhen(() -> {
            helper.assertItemEntityCountIs(Items.COBBLESTONE, VAT_POS, 2.0, 3);
        });
    }
}
