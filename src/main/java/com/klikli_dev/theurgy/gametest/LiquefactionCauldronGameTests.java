// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlock;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlock;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.FluidRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.fluids.FluidStack;

public class LiquefactionCauldronGameTests {

    private static final BlockPos BRAZIER_POS = new BlockPos(2, 1, 2);
    private static final BlockPos CAULDRON_LOWER_POS = new BlockPos(2, 2, 2);
    private static final BlockPos CAULDRON_UPPER_POS = new BlockPos(2, 3, 2);

    /**
     * Helper: places the two-block-tall liquefaction cauldron at the expected position.
     * Must manually set the upper half since helper.setBlock() does not call setPlacedBy().
     */
    private static void placeCauldron(GameTestHelper helper) {
        helper.setBlock(CAULDRON_LOWER_POS, BlockRegistry.LIQUEFACTION_CAULDRON.get());
        var lowerState = helper.getBlockState(CAULDRON_LOWER_POS);
        var absPos = helper.absolutePos(CAULDRON_LOWER_POS);
        helper.getLevel().setBlock(
                absPos.above(),
                lowerState.setValue(LiquefactionCauldronBlock.HALF, DoubleBlockHalf.UPPER),
                3
        );
    }

    /**
     * Helper: places a lit pyromantic brazier below the cauldron to provide heat.
     */
    private static void placeLitBrazier(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());
        helper.runAfterDelay(1, () -> {
            var brazier = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            brazier.inventory.setStackInSlot(0, new ItemStack(Items.COAL, 64));
        });
    }

    /**
     * Helper: sets up a full processing scenario (brazier + cauldron + input item + solvent fluid).
     */
    private static void setupProcessing(GameTestHelper helper) {
        placeLitBrazier(helper);
        placeCauldron(helper);

        helper.runAfterDelay(2, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            // Insert input item (bone)
            cauldron.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.BONE, 1));
            // Insert solvent fluid (sal ammoniac, plenty for the recipe)
            cauldron.storageBehaviour.solventTank.fill(
                    new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1000), false
            );
        });
    }

    // ==================== Placement & State ====================

    /**
     * Tests that placing a Liquefaction Cauldron creates a two-block-tall structure
     * with LOWER and UPPER halves, both with LIT=false by default.
     */
    public static void placementCreatesTwoBlockStructure(GameTestHelper helper) {
        placeCauldron(helper);

        helper.assertBlockPresent(BlockRegistry.LIQUEFACTION_CAULDRON.get(), CAULDRON_LOWER_POS);
        helper.assertBlockProperty(CAULDRON_LOWER_POS, LiquefactionCauldronBlock.HALF, DoubleBlockHalf.LOWER);
        helper.assertBlockProperty(CAULDRON_LOWER_POS, LiquefactionCauldronBlock.LIT, false);

        helper.assertBlockPresent(BlockRegistry.LIQUEFACTION_CAULDRON.get(), CAULDRON_UPPER_POS);
        helper.assertBlockProperty(CAULDRON_UPPER_POS, LiquefactionCauldronBlock.HALF, DoubleBlockHalf.UPPER);
        helper.assertBlockProperty(CAULDRON_UPPER_POS, LiquefactionCauldronBlock.LIT, false);

        helper.succeed();
    }

    /**
     * Tests that a block entity exists only on the lower half of the cauldron.
     */
    public static void blockEntityOnlyOnLowerHalf(GameTestHelper helper) {
        placeCauldron(helper);

        var lowerBE = helper.getLevel().getBlockEntity(helper.absolutePos(CAULDRON_LOWER_POS));
        helper.assertTrue(
                lowerBE instanceof LiquefactionCauldronBlockEntity,
                "Lower half should have a LiquefactionCauldronBlockEntity"
        );

        var upperBE = helper.getLevel().getBlockEntity(helper.absolutePos(CAULDRON_UPPER_POS));
        helper.assertTrue(
                upperBE == null,
                "Upper half should not have a block entity"
        );

        helper.succeed();
    }

    /**
     * Tests that breaking the lower half also destroys the upper half.
     */
    public static void breakingLowerDestroysUpper(GameTestHelper helper) {
        placeCauldron(helper);

        helper.runAfterDelay(1, () -> {
            helper.destroyBlock(CAULDRON_LOWER_POS);
        });

        helper.runAfterDelay(5, () -> {
            helper.assertBlockNotPresent(BlockRegistry.LIQUEFACTION_CAULDRON.get(), CAULDRON_UPPER_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that breaking the upper half also destroys the lower half.
     */
    public static void breakingUpperDestroysLower(GameTestHelper helper) {
        placeCauldron(helper);

        helper.runAfterDelay(1, () -> {
            helper.destroyBlock(CAULDRON_UPPER_POS);
        });

        helper.runAfterDelay(5, () -> {
            helper.assertBlockNotPresent(BlockRegistry.LIQUEFACTION_CAULDRON.get(), CAULDRON_LOWER_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that items in the cauldron's inventory are dropped when the block is broken.
     */
    public static void dropsItemsWhenBroken(GameTestHelper helper) {
        placeCauldron(helper);

        helper.runAfterDelay(1, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            cauldron.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.BONE, 3));
        });

        helper.runAfterDelay(2, () -> {
            helper.destroyBlock(CAULDRON_LOWER_POS);
        });

        helper.succeedWhen(() -> {
            helper.assertItemEntityPresent(Items.BONE, CAULDRON_LOWER_POS, 2.0);
        });
    }

    // ==================== Item & Fluid Handling ====================

    /**
     * Tests that a valid input item (bone) can be inserted into the input inventory.
     */
    public static void insertInputItem(GameTestHelper helper) {
        placeCauldron(helper);

        helper.runAfterDelay(1, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            cauldron.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.BONE, 1));

            helper.assertTrue(
                    !cauldron.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty(),
                    "Input inventory should contain the inserted bone"
            );
            helper.assertTrue(
                    cauldron.storageBehaviour.inputInventory.getStackInSlot(0).is(Items.BONE),
                    "Input inventory should contain a bone item"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that solvent fluid (sal ammoniac) can be inserted into the solvent tank.
     */
    public static void insertSolventFluid(GameTestHelper helper) {
        placeCauldron(helper);

        helper.runAfterDelay(1, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            var filled = cauldron.storageBehaviour.solventTank.fill(
                    new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 500), false
            );

            helper.assertTrue(
                    filled > 0,
                    "Solvent tank should accept sal ammoniac fluid"
            );
            helper.assertTrue(
                    cauldron.storageBehaviour.solventTank.getFluidAmount() == 500,
                    "Solvent tank should contain 500mb of sal ammoniac"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that solvent fluid can be extracted from the cauldron using a bucket.
     * The OneTankFluidHandlerBehaviour allows bucket interaction via FluidUtil.
     */
    public static void extractSolventFluid(GameTestHelper helper) {
        placeCauldron(helper);

        helper.runAfterDelay(1, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            cauldron.storageBehaviour.solventTank.fill(
                    new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1000), false
            );
            helper.assertTrue(
                    cauldron.storageBehaviour.solventTank.getFluidAmount() == 1000,
                    "Solvent tank should contain 1000mb before extraction"
            );
        });

        helper.runAfterDelay(2, () -> {
            // Simulate right-click with a bucket to extract fluid
            var player = helper.makeMockPlayer(GameType.SURVIVAL);
            player.getInventory().setItem(0, new ItemStack(Items.BUCKET));
            helper.useBlock( helper.relativePos(CAULDRON_LOWER_POS), player);
        });

        helper.succeedWhen(() -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            helper.assertTrue(
                    cauldron.storageBehaviour.solventTank.getFluidAmount() < 1000,
                    "Solvent tank should have less fluid after bucket extraction"
            );
        });
    }

    /**
     * Tests that the output item can be extracted from the output inventory after processing.
     */
    public static void extractOutputItem(GameTestHelper helper) {
        placeCauldron(helper);

        helper.runAfterDelay(1, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            // Manually place a result in the output for extraction testing
            cauldron.storageBehaviour.outputInventory.setStackInSlot(0, new ItemStack(SulfurRegistry.BONE.get(), 1));

            var extracted = cauldron.storageBehaviour.outputInventory.extractItem(0, 1, false);
            helper.assertTrue(
                    !extracted.isEmpty(),
                    "Should be able to extract from output inventory"
            );
            helper.assertTrue(
                    cauldron.storageBehaviour.outputInventory.getStackInSlot(0).isEmpty(),
                    "Output inventory should be empty after extraction"
            );
            helper.succeed();
        });
    }

    // ==================== Processing ====================

    /**
     * Tests that processing starts when heat, valid input item, and solvent fluid are all present.
     */
    public static void processingStartsWithHeatInputAndSolvent(GameTestHelper helper) {
        setupProcessing(helper);

        // Wait for heat to be detected (checked every 20 ticks) and processing to begin
        helper.succeedWhen(() -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            helper.assertTrue(
                    cauldron.craftingBehaviour().isProcessing(),
                    "Cauldron should be processing with heat + input item + solvent"
            );
        });
    }

    /**
     * Tests that the LIT blockstate becomes true during processing.
     */
    public static void litStateTrueDuringProcessing(GameTestHelper helper) {
        setupProcessing(helper);

        // LIT state is set by HeatConsumerBehaviour when heat is detected
        helper.succeedWhen(() -> {
            helper.assertBlockProperty(CAULDRON_LOWER_POS, BlockStateProperties.LIT, true);
        });
    }

    /**
     * Tests the full recipe cycle: input item and solvent fluid are consumed, output item is produced.
     * Recipe: bone + 10mb sal ammoniac -> alchemical_sulfur_bone, 100 ticks.
     */
    public static void inputConsumedSolventDrainedOutputProduced(GameTestHelper helper) {
        setupProcessing(helper);

        helper.succeedWhen(() -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);

            // Check output was produced
            var output = cauldron.storageBehaviour.outputInventory.getStackInSlot(0);
            helper.assertTrue(
                    !output.isEmpty(),
                    "Output inventory should contain the crafted alchemical sulfur"
            );

            // Check input was consumed
            helper.assertTrue(
                    cauldron.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty(),
                    "Input item (bone) should be consumed"
            );

            // Check solvent was partially drained (recipe uses 10mb out of 1000mb)
            helper.assertTrue(
                    cauldron.storageBehaviour.solventTank.getFluidAmount() == 990,
                    "Solvent fluid should be consumed by 10mb, leaving 990mb"
            );
        });
    }

    /**
     * Tests that processing stops when heat is removed (brazier destroyed).
     */
    public static void processingStopsWhenHeatRemoved(GameTestHelper helper) {
        setupProcessing(helper);

        // Wait for processing to start
        helper.runAfterDelay(25, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            if (!cauldron.craftingBehaviour().isProcessing()) {
                helper.fail("Processing should have started before removing heat");
                return;
            }

            // Remove heat by destroying the brazier
            helper.destroyBlock(BRAZIER_POS);
        });

        // After heat check interval (20 ticks), processing should stop
        helper.succeedWhen(() -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            // Brazier is gone
            helper.assertBlockNotPresent(BlockRegistry.PYROMANTIC_BRAZIER.get(), BRAZIER_POS);
            // Processing should have stopped
            helper.assertTrue(
                    !cauldron.craftingBehaviour().isProcessing(),
                    "Processing should stop after heat source is removed"
            );
        });
    }

    /**
     * Tests that processing does not start when there is no input item,
     * even with heat and solvent present.
     */
    public static void processingRequiresInput(GameTestHelper helper) {
        placeLitBrazier(helper);
        placeCauldron(helper);

        // Add only solvent, no input item
        helper.runAfterDelay(2, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            cauldron.storageBehaviour.solventTank.fill(
                    new FluidStack(FluidRegistry.SAL_AMMONIAC.get(), 1000), false
            );
        });

        // Wait long enough for heat detection and potential processing start
        helper.runAfterDelay(50, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            helper.assertTrue(
                    !cauldron.craftingBehaviour().isProcessing(),
                    "Processing should not start without an input item"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that processing does not start when there is no solvent fluid,
     * even with heat and input item present.
     */
    public static void processingRequiresSolvent(GameTestHelper helper) {
        placeLitBrazier(helper);
        placeCauldron(helper);

        // Add only input item, no solvent
        helper.runAfterDelay(2, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            cauldron.storageBehaviour.inputInventory.setStackInSlot(0, new ItemStack(Items.BONE, 1));
        });

        // Wait long enough for heat detection and potential processing start
        helper.runAfterDelay(50, () -> {
            var cauldron = helper.getBlockEntity(CAULDRON_LOWER_POS, LiquefactionCauldronBlockEntity.class);
            helper.assertTrue(
                    !cauldron.craftingBehaviour().isProcessing(),
                    "Processing should not start without solvent fluid"
            );
            helper.succeed();
        });
    }
}
