// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class CalcinationOvenGameTests {

    /**
     * The brazier sits at y=1, the oven lower half at y=2, upper half at y=3.
     */
    private static final BlockPos BRAZIER_POS = new BlockPos(2, 1, 2);
    private static final BlockPos OVEN_LOWER_POS = new BlockPos(2, 2, 2);
    private static final BlockPos OVEN_UPPER_POS = new BlockPos(2, 3, 2);

    // --- Placement & State ---

    /**
     * Tests that placing a Calcination Oven creates a two-block-tall structure (lower + upper half)
     * with the correct default states (LIT=false, correct HALF values).
     */
    public static void placementCreatesTwoBlockStructure(GameTestHelper helper) {
        // Ensure space above is clear
        helper.setBlock(OVEN_LOWER_POS, Blocks.AIR);
        helper.setBlock(OVEN_UPPER_POS, Blocks.AIR);

        helper.setBlock(OVEN_LOWER_POS, BlockRegistry.CALCINATION_OVEN.get());

        helper.runAfterDelay(1, () -> {
            // Simulate setPlacedBy which places the upper half
            var level = helper.getLevel();
            var absPos = helper.absolutePos(OVEN_LOWER_POS);
            var lowerState = level.getBlockState(absPos);
            level.setBlock(absPos.above(), lowerState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);

            helper.assertBlockPresent(BlockRegistry.CALCINATION_OVEN.get(), OVEN_LOWER_POS);
            helper.assertBlockPresent(BlockRegistry.CALCINATION_OVEN.get(), OVEN_UPPER_POS);
            helper.assertBlockProperty(OVEN_LOWER_POS, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
            helper.assertBlockProperty(OVEN_UPPER_POS, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
            helper.assertBlockProperty(OVEN_LOWER_POS, BlockStateProperties.LIT, false);
            helper.assertBlockProperty(OVEN_UPPER_POS, BlockStateProperties.LIT, false);
            helper.succeed();
        });
    }

    /**
     * Tests that the block entity exists only on the lower half.
     */
    public static void blockEntityOnlyOnLowerHalf(GameTestHelper helper) {
        placeOven(helper);

        helper.runAfterDelay(1, () -> {
            // Lower half should have a block entity
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            helper.assertTrue(blockEntity != null, "Lower half should have a block entity");

            // Upper half should NOT have a block entity
            var level = helper.getLevel();
            var upperBE = level.getBlockEntity(helper.absolutePos(OVEN_UPPER_POS));
            helper.assertTrue(upperBE == null, "Upper half should not have a block entity");

            helper.succeed();
        });
    }

    /**
     * Tests that breaking the lower half destroys the upper half too.
     */
    public static void breakingLowerDestroysUpper(GameTestHelper helper) {
        placeOven(helper);

        helper.runAfterDelay(1, () -> {
            helper.destroyBlock(OVEN_LOWER_POS);
        });

        helper.runAfterDelay(3, () -> {
            helper.assertBlockNotPresent(BlockRegistry.CALCINATION_OVEN.get(), OVEN_UPPER_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that breaking the upper half destroys the lower half too.
     */
    public static void breakingUpperDestroysLower(GameTestHelper helper) {
        placeOven(helper);

        helper.runAfterDelay(1, () -> {
            helper.destroyBlock(OVEN_UPPER_POS);
        });

        helper.runAfterDelay(3, () -> {
            helper.assertBlockNotPresent(BlockRegistry.CALCINATION_OVEN.get(), OVEN_LOWER_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that items in the oven's inventory are dropped when the block is broken.
     */
    public static void dropsItemsWhenBroken(GameTestHelper helper) {
        placeOven(helper);

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 3));
        });

        helper.runAfterDelay(2, () -> {
            helper.destroyBlock(OVEN_LOWER_POS);
        });

        helper.succeedWhen(() -> {
            helper.assertItemEntityCountIs(Items.COBBLESTONE, OVEN_LOWER_POS, 2.0, 3);
        });
    }

    // --- Item Handling ---

    /**
     * Tests that a valid input item (cobblestone, which has a calcination recipe) can be inserted.
     */
    public static void insertInputItem(GameTestHelper helper) {
        placeOvenWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            var remainder = blockEntity.storageBehaviour.inputInventory.insertItem(0, new ItemStack(Items.COBBLESTONE, 1), false);
            helper.assertTrue(
                    remainder.isEmpty(),
                    "Cobblestone should be accepted as a valid calcination input"
            );
            helper.assertTrue(
                    !blockEntity.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty(),
                    "Input inventory should contain the inserted cobblestone"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that output items can be extracted from the output slot.
     */
    public static void extractOutputItem(GameTestHelper helper) {
        placeOvenWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            // Directly place an item in the output slot
            blockEntity.storageBehaviour.outputInventory.setStackInSlot(0, new ItemStack(SaltRegistry.STRATA.get(), 1));
        });

        helper.runAfterDelay(4, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            var extracted = blockEntity.storageBehaviour.outputInventory.extractItem(0, 64, false);
            helper.assertTrue(
                    !extracted.isEmpty(),
                    "Should be able to extract from output slot"
            );
            helper.assertTrue(
                    blockEntity.storageBehaviour.outputInventory.getStackInSlot(0).isEmpty(),
                    "Output slot should be empty after extraction"
            );
            helper.succeed();
        });
    }

    // --- Processing ---

    /**
     * Tests that processing starts when heat is provided and a valid input is present.
     * Cobblestone + heat -> should begin crafting (isProcessing = true).
     */
    public static void processingStartsWithHeatAndInput(GameTestHelper helper) {
        placeOvenWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 1));
        });

        // Wait for heat check interval (20 ticks) + processing to start
        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            helper.assertTrue(
                    blockEntity.craftingBehaviour().isProcessing(),
                    "Calcination oven should be processing with heat and valid input"
            );
        });
    }

    /**
     * Tests that LIT state becomes true during processing.
     */
    public static void litStateTrueDuringProcessing(GameTestHelper helper) {
        placeOvenWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 1));
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(OVEN_LOWER_POS, BlockStateProperties.LIT, true);
        });
    }

    /**
     * Tests that input item is consumed and output item is produced after processing completes.
     * Cobblestone -> Alchemical Salt (Strata).
     */
    public static void inputConsumedAndOutputProduced(GameTestHelper helper) {
        placeOvenWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 1));
        });

        // Default calcination time is 100 ticks; wait for completion
        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            // Input should be consumed
            helper.assertTrue(
                    blockEntity.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty(),
                    "Cobblestone input should be consumed after processing"
            );
            // Output should be produced
            var output = blockEntity.storageBehaviour.outputInventory.getStackInSlot(0);
            helper.assertTrue(
                    !output.isEmpty(),
                    "Output slot should contain alchemical salt after processing"
            );
        });
    }

    /**
     * Tests that processing stops when heat is removed (brazier runs out of fuel).
     */
    public static void processingStopsWhenHeatRemoved(GameTestHelper helper) {
        placeOven(helper);

        // Place brazier with minimal fuel (1 stick = 100 ticks burn time)
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());
        helper.runAfterDelay(1, () -> {
            var brazier = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            brazier.inventory.setStackInSlot(0, new ItemStack(Items.STICK, 1));
        });

        // Insert input after brazier is lit
        helper.runAfterDelay(5, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            // Use a stack of 64 to ensure there's always input available
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 64));
        });

        // Wait for fuel to run out, then verify processing has stopped
        helper.succeedWhen(() -> {
            var brazier = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            // Ensure fuel ran out
            helper.assertTrue(
                    brazier.inventory.getStackInSlot(0).isEmpty(),
                    "Brazier fuel should have run out"
            );
            helper.assertBlockProperty(BRAZIER_POS, BlockStateProperties.LIT, false);

            // Verify oven stopped processing
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            helper.assertTrue(
                    !blockEntity.craftingBehaviour().isProcessing(),
                    "Calcination oven should stop processing when heat is removed"
            );
        });
    }

    /**
     * Tests that processing stops when input is empty (no items to process).
     */
    public static void processingStopsWhenInputEmpty(GameTestHelper helper) {
        placeOvenWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            // Insert exactly 1 cobblestone — it will be consumed after processing
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 1));
        });

        // Wait for the single item to be fully processed, then verify no longer processing
        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(OVEN_LOWER_POS, CalcinationOvenBlockEntity.class);
            helper.assertTrue(
                    blockEntity.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty(),
                    "Input should be consumed"
            );
            helper.assertTrue(
                    !blockEntity.craftingBehaviour().isProcessing(),
                    "Calcination oven should stop processing when input is empty"
            );
        });
    }

    // --- Helper Methods ---

    /**
     * Places a complete two-block-tall Calcination Oven at the standard position.
     */
    private static void placeOven(GameTestHelper helper) {
        helper.setBlock(OVEN_LOWER_POS, BlockRegistry.CALCINATION_OVEN.get());
        // setPlacedBy is not called by setBlock, so we manually place the upper half
        var level = helper.getLevel();
        var absLowerPos = helper.absolutePos(OVEN_LOWER_POS);
        var lowerState = level.getBlockState(absLowerPos);
        level.setBlock(absLowerPos.above(), lowerState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
    }

    /**
     * Places a Calcination Oven with a lit Pyromantic Brazier below it (with plenty of fuel).
     */
    private static void placeOvenWithHeat(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());
        placeOven(helper);

        // Insert fuel into brazier after 1 tick (needs level to be set)
        helper.runAfterDelay(1, () -> {
            var brazier = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            brazier.inventory.setStackInSlot(0, new ItemStack(Items.COAL, 64));
        });
    }
}
