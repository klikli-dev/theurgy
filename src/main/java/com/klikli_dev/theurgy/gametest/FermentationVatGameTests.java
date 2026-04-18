// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.fermentationvat.FermentationVatBlock;
import com.klikli_dev.theurgy.content.apparatus.fermentationvat.FermentationVatBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class FermentationVatGameTests {

    private static final BlockPos VAT_POS = new BlockPos(2, 2, 2);

    // --- Placement & State ---

    /**
     * Tests that a Fermentation Vat can be placed with the correct default state.
     * Default: OPEN=true, HAS_OUTPUT=false
     */
    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get());

        helper.assertBlockPresent(BlockRegistry.FERMENTATION_VAT.get(), VAT_POS);
        helper.assertBlockProperty(VAT_POS, BlockStateProperties.OPEN, true);
        helper.assertBlockProperty(VAT_POS, FermentationVatBlock.HAS_OUTPUT, false);
        helper.succeed();
    }

    /**
     * Tests that the Fermentation Vat is placed with the correct HORIZONTAL_FACING.
     */
    public static void placementWithFacing(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get()
                .defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));

        helper.assertBlockPresent(BlockRegistry.FERMENTATION_VAT.get(), VAT_POS);
        helper.assertBlockProperty(VAT_POS, BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        helper.succeed();
    }

    // --- Item & Fluid Handling ---

    /**
     * Tests that input items can be inserted into the input inventory.
     */
    public static void insertInputItem(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            ItemStack remainder;
            try (var tx = Transaction.openRoot()) {
                remainder = ItemUtil.insertItemReturnRemaining(blockEntity.storageBehaviour.inputInventory, 0, new ItemStack(Items.OAK_LOG, 1), false, tx);
                tx.commit();
            }
            helper.assertTrue(
                    remainder.isEmpty(),
                    "Oak log should be accepted as input"
            );
            helper.assertTrue(
                    !ItemUtil.getStack(blockEntity.storageBehaviour.inputInventory, 0).isEmpty(),
                    "Input inventory should contain the inserted item"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that fluid can be inserted into the fluid tank.
     */
    public static void insertFluid(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            var filled = blockEntity.storageBehaviour.fluidTank.fill(
                    new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1000), false
            );

            helper.assertTrue(filled == 1000, "Fluid tank should accept 1000mb of fluid");
            helper.assertTrue(blockEntity.storageBehaviour.fluidTank.getFluidAmount() == 1000, "Fluid tank should contain 1000mb of fluid");
            helper.succeed();
        });
    }

    /**
     * Tests that output items can be extracted from the output inventory.
     */
    public static void extractOutputItem(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            // Manually place an item in the output
            blockEntity.storageBehaviour.outputInventory.set(0, ItemResource.of(new ItemStack(Items.COBBLESTONE, 1)), new ItemStack(Items.COBBLESTONE, 1).getCount());
        });

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            ItemStack extracted;
            try (var tx = Transaction.openRoot()) {
                var resource = blockEntity.storageBehaviour.outputInventory.getResource(0);
                extracted = resource.toStack(blockEntity.storageBehaviour.outputInventory.extract(0, resource, 1, tx));
                tx.commit();
            }
            helper.assertTrue(
                    !extracted.isEmpty(),
                    "Should be able to extract from output inventory"
            );
            helper.assertTrue(
                    ItemUtil.getStack(blockEntity.storageBehaviour.outputInventory, 0).isEmpty(),
                    "Output inventory should be empty after extraction"
            );
            helper.succeed();
        });
    }

    // --- Processing ---

    /**
     * Tests that the vat lid can be manually closed.
     * Processing requires the lid to be closed.
     */
    public static void vatCanBeClosed(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            // Insert input items
            blockEntity.storageBehaviour.inputInventory.set(0, ItemResource.of(new ItemStack(Items.OAK_LOG, 1)), new ItemStack(Items.OAK_LOG, 1).getCount());
            // Insert fluid
            blockEntity.storageBehaviour.fluidTank.fill(
                    new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1000), false
            );
        });

        // Close the lid to start processing
        helper.runAfterDelay(3, () -> {
            var level = helper.getLevel();
            var absPos = helper.absolutePos(VAT_POS);
            level.setBlock(absPos, level.getBlockState(absPos).setValue(BlockStateProperties.OPEN, false), 3);
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(VAT_POS, BlockStateProperties.OPEN, false);
        });
    }

    /**
     * Tests that HAS_OUTPUT becomes true when output is placed in the output slot.
     */
    public static void hasOutputBecomesTrue(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            blockEntity.storageBehaviour.outputInventory.set(0, ItemResource.of(new ItemStack(Items.COBBLESTONE, 1)), new ItemStack(Items.COBBLESTONE, 1).getCount());
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(VAT_POS, FermentationVatBlock.HAS_OUTPUT, true);
        });
    }

    /**
     * Tests that HAS_OUTPUT becomes false when output is extracted.
     */
    public static void hasOutputBecomesFalseWhenExtracted(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            blockEntity.storageBehaviour.outputInventory.set(0, ItemResource.of(new ItemStack(Items.COBBLESTONE, 1)), new ItemStack(Items.COBBLESTONE, 1).getCount());
        });

        helper.runAfterDelay(5, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            try (var tx = Transaction.openRoot()) {
                var resource = blockEntity.storageBehaviour.outputInventory.getResource(0);
                blockEntity.storageBehaviour.outputInventory.extract(0, resource, 1, tx);
                tx.commit();
            }
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(VAT_POS, FermentationVatBlock.HAS_OUTPUT, false);
        });
    }

    /**
     * Tests that items are dropped when the block is broken.
     */
    public static void dropsItemsWhenBroken(GameTestHelper helper) {
        helper.setBlock(VAT_POS, BlockRegistry.FERMENTATION_VAT.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(VAT_POS, FermentationVatBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.set(0, ItemResource.of(new ItemStack(Items.OAK_LOG, 3)), new ItemStack(Items.OAK_LOG, 3).getCount());
        });

        helper.runAfterDelay(2, () -> {
            helper.destroyBlock(VAT_POS);
        });

        helper.succeedWhen(() -> {
            helper.assertItemEntityCountIs(Items.OAK_LOG, VAT_POS, 2.0, 3);
        });
    }
}
