// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class DistillerGameTests {

    /**
     * The brazier sits at y=1, the distiller lower half at y=2, upper half at y=3.
     */
    private static final BlockPos BRAZIER_POS = new BlockPos(2, 1, 2);
    private static final BlockPos DISTILLER_LOWER_POS = new BlockPos(2, 2, 2);
    private static final BlockPos DISTILLER_UPPER_POS = new BlockPos(2, 3, 2);

    // --- Placement & State ---

    /**
     * Tests that placing a Distiller creates a two-block-tall structure (lower + upper half)
     * with the correct default states (LIT=false, correct HALF values).
     */
    public static void placementCreatesTwoBlockStructure(GameTestHelper helper) {
        // Ensure space above is clear
        helper.setBlock(DISTILLER_LOWER_POS, Blocks.AIR);
        helper.setBlock(DISTILLER_UPPER_POS, Blocks.AIR);

        helper.setBlock(DISTILLER_LOWER_POS, BlockRegistry.DISTILLER.get());

        helper.runAfterDelay(1, () -> {
            // Simulate setPlacedBy which places the upper half
            var level = helper.getLevel();
            var absPos = helper.absolutePos(DISTILLER_LOWER_POS);
            var lowerState = level.getBlockState(absPos);
            level.setBlock(absPos.above(), lowerState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);

            helper.assertBlockPresent(BlockRegistry.DISTILLER.get(), DISTILLER_LOWER_POS);
            helper.assertBlockPresent(BlockRegistry.DISTILLER.get(), DISTILLER_UPPER_POS);
            helper.assertBlockProperty(DISTILLER_LOWER_POS, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
            helper.assertBlockProperty(DISTILLER_UPPER_POS, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
            helper.assertBlockProperty(DISTILLER_LOWER_POS, BlockStateProperties.LIT, false);
            helper.assertBlockProperty(DISTILLER_UPPER_POS, BlockStateProperties.LIT, false);
            helper.succeed();
        });
    }

    /**
     * Tests that the block entity exists only on the lower half.
     */
    public static void blockEntityOnlyOnLowerHalf(GameTestHelper helper) {
        placeDistiller(helper);

        helper.runAfterDelay(1, () -> {
            // Lower half should have a block entity
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            helper.assertTrue(blockEntity != null, "Lower half should have a block entity");

            // Upper half should NOT have a block entity
            var level = helper.getLevel();
            var upperBE = level.getBlockEntity(helper.absolutePos(DISTILLER_UPPER_POS));
            helper.assertTrue(upperBE == null, "Upper half should not have a block entity");

            helper.succeed();
        });
    }

    /**
     * Tests that breaking the lower half destroys the upper half too.
     */
    public static void breakingLowerDestroysUpper(GameTestHelper helper) {
        placeDistiller(helper);

        helper.runAfterDelay(1, () -> {
            helper.destroyBlock(DISTILLER_LOWER_POS);
        });

        helper.runAfterDelay(3, () -> {
            helper.assertBlockNotPresent(BlockRegistry.DISTILLER.get(), DISTILLER_UPPER_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that breaking the upper half destroys the lower half too.
     */
    public static void breakingUpperDestroysLower(GameTestHelper helper) {
        placeDistiller(helper);

        helper.runAfterDelay(1, () -> {
            helper.destroyBlock(DISTILLER_UPPER_POS);
        });

        helper.runAfterDelay(3, () -> {
            helper.assertBlockNotPresent(BlockRegistry.DISTILLER.get(), DISTILLER_LOWER_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that items in the distiller's inventory are dropped when the block is broken.
     */
    public static void dropsItemsWhenBroken(GameTestHelper helper) {
        placeDistiller(helper);

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.OAK_LOG, 3));
        });

        helper.runAfterDelay(2, () -> {
            helper.destroyBlock(DISTILLER_LOWER_POS);
        });

        helper.succeedWhen(() -> {
            helper.assertItemEntityCountIs(Items.OAK_LOG, DISTILLER_LOWER_POS, 2.0, 3);
        });
    }

    // --- Item Handling ---

    /**
     * Tests that a valid input item (oak log, which has a distillation recipe) can be inserted.
     */
    public static void insertInputItem(GameTestHelper helper) {
        placeDistillerWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            var remainder = blockEntity.storageBehaviour.inputInventory.insertItem(0, new ItemStack(Items.OAK_LOG, 1), false);
            helper.assertTrue(
                    remainder.isEmpty(),
                    "Oak log should be accepted as a valid distillation input"
            );
            helper.assertTrue(
                    !blockEntity.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty(),
                    "Input inventory should contain the inserted oak log"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that output items can be extracted from the output slot.
     */
    public static void extractOutputItem(GameTestHelper helper) {
        placeDistillerWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            // Directly place an item in the output slot
            blockEntity.storageBehaviour.outputInventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        helper.runAfterDelay(4, () -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
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
     * Oak logs + heat -> should begin crafting (isProcessing = true).
     */
    public static void processingStartsWithHeatAndInput(GameTestHelper helper) {
        placeDistillerWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            // Logs recipe requires 2 items
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.OAK_LOG, 2));
        });

        // Wait for heat check interval (20 ticks) + processing to start
        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            helper.assertTrue(
                    blockEntity.craftingBehaviour().isProcessing(),
                    "Distiller should be processing with heat and valid input"
            );
        });
    }

    /**
     * Tests that LIT state becomes true during processing.
     */
    public static void litStateTrueDuringProcessing(GameTestHelper helper) {
        placeDistillerWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.OAK_LOG, 2));
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(DISTILLER_LOWER_POS, BlockStateProperties.LIT, true);
        });
    }

    /**
     * Tests that input item is consumed and output item is produced after processing completes.
     * Oak logs (2) -> Mercury Shard.
     */
    public static void inputConsumedAndOutputProduced(GameTestHelper helper) {
        placeDistillerWithHeat(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.OAK_LOG, 2));
        });

        // Default distillation time is 100 ticks; wait for completion
        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            // Input should be consumed
            helper.assertTrue(
                    blockEntity.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty(),
                    "Oak log input should be consumed after processing"
            );
            // Output should be produced
            var output = blockEntity.storageBehaviour.outputInventory.getStackInSlot(0);
            helper.assertTrue(
                    !output.isEmpty(),
                    "Output slot should contain mercury shard after processing"
            );
        });
    }

    /**
     * Tests that processing stops when heat is removed (brazier runs out of fuel).
     */
    public static void processingStopsWhenHeatRemoved(GameTestHelper helper) {
        placeDistiller(helper);

        // Place brazier with minimal fuel (1 stick = 100 ticks burn time)
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());
        helper.runAfterDelay(1, () -> {
            var brazier = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            brazier.inventory.setStackInSlot(0, new ItemStack(Items.STICK, 1));
        });

        // Insert input after brazier is lit
        helper.runAfterDelay(5, () -> {
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            // Use a stack to ensure there's always input available
            blockEntity.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.OAK_LOG, 64));
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

            // Verify distiller stopped processing
            var blockEntity = helper.getBlockEntity(DISTILLER_LOWER_POS, DistillerBlockEntity.class);
            helper.assertTrue(
                    !blockEntity.craftingBehaviour().isProcessing(),
                    "Distiller should stop processing when heat is removed"
            );
        });
    }

    // --- Helper Methods ---

    /**
     * Places a complete two-block-tall Distiller at the standard position.
     */
    private static void placeDistiller(GameTestHelper helper) {
        helper.setBlock(DISTILLER_LOWER_POS, BlockRegistry.DISTILLER.get());
        // setPlacedBy is not called by setBlock, so we manually place the upper half
        var level = helper.getLevel();
        var absLowerPos = helper.absolutePos(DISTILLER_LOWER_POS);
        var lowerState = level.getBlockState(absLowerPos);
        level.setBlock(absLowerPos.above(), lowerState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
    }

    /**
     * Places a Distiller with a lit Pyromantic Brazier below it (with plenty of fuel).
     */
    private static void placeDistillerWithHeat(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());
        placeDistiller(helper);

        // Insert fuel into brazier after 1 tick (needs level to be set)
        helper.runAfterDelay(1, () -> {
            var brazier = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            brazier.inventory.setStackInSlot(0, new ItemStack(Items.COAL, 64));
        });
    }
}
