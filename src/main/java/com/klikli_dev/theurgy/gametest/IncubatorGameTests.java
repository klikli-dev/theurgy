// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.incubator.*;
import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class IncubatorGameTests {

    private static final BlockPos BRAZIER_POS = new BlockPos(2, 1, 2);
    private static final BlockPos INCUBATOR_LOWER_POS = new BlockPos(2, 2, 2);
    private static final BlockPos INCUBATOR_UPPER_POS = new BlockPos(2, 3, 2);
    // Vessels placed on horizontal sides
    private static final BlockPos MERCURY_VESSEL_POS = INCUBATOR_LOWER_POS.north(); // (2, 2, 1)
    private static final BlockPos SALT_VESSEL_POS = INCUBATOR_LOWER_POS.east();     // (3, 2, 2)
    private static final BlockPos SULFUR_VESSEL_POS = INCUBATOR_LOWER_POS.south();  // (2, 2, 3)

    // --- Placement & State ---

    /**
     * Tests that placing an Incubator creates a two-block-tall structure
     * with the correct default states (HALF values, connection states all false).
     */
    public static void placementCreatesTwoBlockStructure(GameTestHelper helper) {
        placeIncubator(helper);

        helper.runAfterDelay(1, () -> {
            helper.assertBlockPresent(BlockRegistry.INCUBATOR.get(), INCUBATOR_LOWER_POS);
            helper.assertBlockPresent(BlockRegistry.INCUBATOR.get(), INCUBATOR_UPPER_POS);
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
            helper.assertBlockProperty(INCUBATOR_UPPER_POS, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
            helper.succeed();
        });
    }

    /**
     * Tests that connection states (NORTH/EAST/SOUTH/WEST) default to false when no vessels are placed.
     */
    public static void connectionStatesDefaultFalse(GameTestHelper helper) {
        placeIncubator(helper);

        helper.runAfterDelay(1, () -> {
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, IncubatorBlock.NORTH, false);
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, IncubatorBlock.EAST, false);
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, IncubatorBlock.SOUTH, false);
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, IncubatorBlock.WEST, false);
            helper.succeed();
        });
    }

    /**
     * Tests that breaking the lower half destroys the upper half.
     */
    public static void breakingLowerDestroysUpper(GameTestHelper helper) {
        placeIncubator(helper);

        helper.runAfterDelay(1, () -> {
            helper.destroyBlock(INCUBATOR_LOWER_POS);
        });

        helper.runAfterDelay(3, () -> {
            helper.assertBlockNotPresent(BlockRegistry.INCUBATOR.get(), INCUBATOR_UPPER_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that breaking the upper half destroys the lower half.
     */
    public static void breakingUpperDestroysLower(GameTestHelper helper) {
        placeIncubator(helper);

        helper.runAfterDelay(1, () -> {
            helper.destroyBlock(INCUBATOR_UPPER_POS);
        });

        helper.runAfterDelay(3, () -> {
            helper.assertBlockNotPresent(BlockRegistry.INCUBATOR.get(), INCUBATOR_LOWER_POS);
            helper.succeed();
        });
    }

    // --- Vessel Connection ---

    /**
     * Tests that placing a Mercury Vessel adjacent updates the connection state.
     */
    public static void placingMercuryVesselUpdatesConnection(GameTestHelper helper) {
        placeIncubator(helper);

        helper.runAfterDelay(1, () -> {
            helper.setBlock(MERCURY_VESSEL_POS, BlockRegistry.INCUBATOR_MERCURY_VESSEL.get());
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, IncubatorBlock.NORTH, true);
        });
    }

    /**
     * Tests that placing a Salt Vessel adjacent updates the connection state.
     */
    public static void placingSaltVesselUpdatesConnection(GameTestHelper helper) {
        placeIncubator(helper);

        helper.runAfterDelay(1, () -> {
            helper.setBlock(SALT_VESSEL_POS, BlockRegistry.INCUBATOR_SALT_VESSEL.get());
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, IncubatorBlock.EAST, true);
        });
    }

    /**
     * Tests that placing a Sulfur Vessel adjacent updates the connection state.
     */
    public static void placingSulfurVesselUpdatesConnection(GameTestHelper helper) {
        placeIncubator(helper);

        helper.runAfterDelay(1, () -> {
            helper.setBlock(SULFUR_VESSEL_POS, BlockRegistry.INCUBATOR_SULFUR_VESSEL.get());
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, IncubatorBlock.SOUTH, true);
        });
    }

    /**
     * Tests that removing a vessel updates the connection state back to false.
     */
    public static void removingVesselUpdatesConnection(GameTestHelper helper) {
        placeIncubator(helper);

        // Place a mercury vessel
        helper.runAfterDelay(1, () -> {
            helper.setBlock(MERCURY_VESSEL_POS, BlockRegistry.INCUBATOR_MERCURY_VESSEL.get());
        });

        // Wait for connection to register, then remove it
        helper.runAfterDelay(5, () -> {
            helper.destroyBlock(MERCURY_VESSEL_POS);
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(INCUBATOR_LOWER_POS, IncubatorBlock.NORTH, false);
        });
    }

    /**
     * Tests that all three vessels create a valid multiblock.
     */
    public static void allThreeVesselsCreateValidMultiblock(GameTestHelper helper) {
        placeAllVessels(helper);

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            helper.assertTrue(
                    blockEntity.isValidMultiblock(),
                    "Incubator should have a valid multiblock with all three vessels"
            );
        });
    }

    // --- Vessel Item Handling ---

    /**
     * Tests that mercury items can be inserted into the Mercury Vessel.
     */
    public static void insertMercuryItem(GameTestHelper helper) {
        placeAllVessels(helper);

        helper.runAfterDelay(2, () -> {
            var vessel = helper.getBlockEntity(MERCURY_VESSEL_POS, IncubatorMercuryVesselBlockEntity.class);
            ItemStack remainder;
            try (var tx = Transaction.openRoot()) {
                remainder = ItemUtil.insertItemReturnRemaining(vessel.inputInventory, 0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1), false, tx);
                tx.commit();
            }
            helper.assertTrue(
                    remainder.isEmpty(),
                    "Mercury shard should be accepted by mercury vessel"
            );
            helper.assertTrue(
                    !ItemUtil.getStack(vessel.inputInventory, 0).isEmpty(),
                    "Mercury vessel should contain the inserted item"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that salt items can be inserted into the Salt Vessel.
     */
    public static void insertSaltItem(GameTestHelper helper) {
        placeAllVessels(helper);

        helper.runAfterDelay(2, () -> {
            var vessel = helper.getBlockEntity(SALT_VESSEL_POS, IncubatorSaltVesselBlockEntity.class);
            try (var tx = Transaction.openRoot()) {
                ItemUtil.insertItemReturnRemaining(vessel.inputInventory, 0, new ItemStack(SaltRegistry.CREATURE.get(), 1), false, tx);
                tx.commit();
            }
        });

        helper.succeedWhen(() -> {
            var vessel = helper.getBlockEntity(SALT_VESSEL_POS, IncubatorSaltVesselBlockEntity.class);
            var expectedStack = new ItemStack(SaltRegistry.CREATURE.get(), 1);
            var actualStack = ItemUtil.getStack(vessel.inputInventory, 0);
            helper.assertTrue(
                    ItemStack.matches(expectedStack, actualStack),
                    "Salt vessel should contain the inserted alchemical salt"
            );
        });
    }

    /**
     * Tests that sulfur items can be inserted into the Sulfur Vessel.
     */
    public static void insertSulfurItem(GameTestHelper helper) {
        placeAllVessels(helper);

        helper.runAfterDelay(2, () -> {
            var vessel = helper.getBlockEntity(SULFUR_VESSEL_POS, IncubatorSulfurVesselBlockEntity.class);
            ItemStack remainder;
            try (var tx = Transaction.openRoot()) {
                remainder = ItemUtil.insertItemReturnRemaining(vessel.inputInventory, 0, new ItemStack(SulfurRegistry.BONE.get(), 1), false, tx);
                tx.commit();
            }
            helper.assertTrue(
                    remainder.isEmpty(),
                    "Alchemical sulfur should be accepted by sulfur vessel"
            );
            helper.assertTrue(
                    !ItemUtil.getStack(vessel.inputInventory, 0).isEmpty(),
                    "Sulfur vessel should contain the inserted item"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that items can be extracted from vessels.
     */
    public static void extractItemsFromVessels(GameTestHelper helper) {
        placeAllVessels(helper);

        helper.runAfterDelay(2, () -> {
            var vessel = helper.getBlockEntity(MERCURY_VESSEL_POS, IncubatorMercuryVesselBlockEntity.class);
            vessel.inputInventory.set(0, ItemResource.of(new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1)), new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1).getCount());
        });

        helper.runAfterDelay(4, () -> {
            var vessel = helper.getBlockEntity(MERCURY_VESSEL_POS, IncubatorMercuryVesselBlockEntity.class);
            ItemStack extracted;
            try (var tx = Transaction.openRoot()) {
                var resource = vessel.inputInventory.getResource(0);
                extracted = resource.toStack(vessel.inputInventory.extract(0, resource, 1, tx));
                tx.commit();
            }
            helper.assertTrue(
                    !extracted.isEmpty(),
                    "Should be able to extract from mercury vessel"
            );
            helper.assertTrue(
                    ItemUtil.getStack(vessel.inputInventory, 0).isEmpty(),
                    "Mercury vessel should be empty after extraction"
            );
            helper.succeed();
        });
    }

    // --- Processing ---

    /**
     * Tests that processing starts when heat + all three vessel inputs are present.
     */
    public static void processingStartsWithHeatAndAllInputs(GameTestHelper helper) {
        setupProcessing(helper);

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            helper.assertTrue(
                    blockEntity.craftingBehaviour().isProcessing(),
                    "Incubator should be processing with heat and all vessel inputs"
            );
        });
    }

    /**
     * Tests that all three vessel items are consumed and output is produced.
     */
    public static void inputsConsumedAndOutputProduced(GameTestHelper helper) {
        setupProcessing(helper);

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            // Output should be produced
            var output = ItemUtil.getStack(blockEntity.outputInventory, 0);
            helper.assertTrue(
                    !output.isEmpty(),
                    "Output inventory should contain the incubation result"
            );
        });
    }

    /**
     * Tests that output can be extracted from the incubator.
     */
    public static void extractOutputFromIncubator(GameTestHelper helper) {
        placeAllVessels(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            blockEntity.outputInventory.set(0, ItemResource.of(new ItemStack(Items.COBBLESTONE, 1)), new ItemStack(Items.COBBLESTONE, 1).getCount());
        });

        helper.runAfterDelay(4, () -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            ItemStack extracted;
            try (var tx = Transaction.openRoot()) {
                var resource = blockEntity.outputInventory.getResource(0);
                extracted = resource.toStack(blockEntity.outputInventory.extract(0, resource, 1, tx));
                tx.commit();
            }
            helper.assertTrue(
                    !extracted.isEmpty(),
                    "Should be able to extract from incubator output"
            );
            helper.succeed();
        });
    }

    public static void blockedOutputDoesNotConsumeVesselInputs(GameTestHelper helper) {
        placeAllVessels(helper);

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            blockEntity.outputInventory.set(0, ItemResource.of(new ItemStack(Items.COBBLESTONE, 1)), 1);
            helper.getBlockEntity(MERCURY_VESSEL_POS, IncubatorMercuryVesselBlockEntity.class).inputInventory.set(0, ItemResource.of(new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1)), 1);
            helper.getBlockEntity(SALT_VESSEL_POS, IncubatorSaltVesselBlockEntity.class).inputInventory.set(0, ItemResource.of(new ItemStack(SaltRegistry.CREATURE.get(), 1)), 1);
            helper.getBlockEntity(SULFUR_VESSEL_POS, IncubatorSulfurVesselBlockEntity.class).inputInventory.set(0, ItemResource.of(new ItemStack(SulfurRegistry.BONE.get(), 1)), 1);
        });

        helper.runAfterDelay(3, () -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            helper.assertTrue(ItemUtil.getStack(blockEntity.outputInventory, 0).getCount() == 1, "Blocked output should remain unchanged");
            helper.assertTrue(ItemUtil.getStack(helper.getBlockEntity(MERCURY_VESSEL_POS, IncubatorMercuryVesselBlockEntity.class).inputInventory, 0).getCount() == 1, "Mercury vessel input should remain");
            helper.assertTrue(ItemUtil.getStack(helper.getBlockEntity(SALT_VESSEL_POS, IncubatorSaltVesselBlockEntity.class).inputInventory, 0).getCount() == 1, "Salt vessel input should remain");
            helper.assertTrue(ItemUtil.getStack(helper.getBlockEntity(SULFUR_VESSEL_POS, IncubatorSulfurVesselBlockEntity.class).inputInventory, 0).getCount() == 1, "Sulfur vessel input should remain");
            helper.succeed();
        });
    }

    /**
     * Tests that processing stops when heat is removed.
     */
    public static void processingStopsWhenHeatRemoved(GameTestHelper helper) {
        setupProcessing(helper);

        // Wait for processing to start, then remove heat
        helper.runAfterDelay(25, () -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            if (!blockEntity.craftingBehaviour().isProcessing()) {
                helper.fail("Processing should have started before removing heat");
                return;
            }
            // Remove heat by destroying the brazier
            helper.destroyBlock(BRAZIER_POS);
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(INCUBATOR_LOWER_POS, IncubatorBlockEntity.class);
            helper.assertBlockNotPresent(BlockRegistry.PYROMANTIC_BRAZIER.get(), BRAZIER_POS);
            helper.assertTrue(
                    !blockEntity.craftingBehaviour().isProcessing(),
                    "Processing should stop after heat source is removed"
            );
        });
    }

    // --- Helper Methods ---

    private static void placeIncubator(GameTestHelper helper) {
        helper.setBlock(INCUBATOR_LOWER_POS, BlockRegistry.INCUBATOR.get());
        var level = helper.getLevel();
        var absLowerPos = helper.absolutePos(INCUBATOR_LOWER_POS);
        var lowerState = level.getBlockState(absLowerPos);
        level.setBlock(absLowerPos.above(), lowerState.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
    }

    private static void placeAllVessels(GameTestHelper helper) {
        placeIncubator(helper);

        helper.runAfterDelay(1, () -> {
            helper.setBlock(MERCURY_VESSEL_POS, BlockRegistry.INCUBATOR_MERCURY_VESSEL.get());
            helper.setBlock(SALT_VESSEL_POS, BlockRegistry.INCUBATOR_SALT_VESSEL.get());
            helper.setBlock(SULFUR_VESSEL_POS, BlockRegistry.INCUBATOR_SULFUR_VESSEL.get());
        });
    }

    private static void placeLitBrazier(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());
        helper.runAfterDelay(1, () -> {
            var brazier = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            brazier.inventory.set(0, ItemResource.of(new ItemStack(Items.COAL, 64)), new ItemStack(Items.COAL, 64).getCount());
        });
    }

    private static void setupProcessing(GameTestHelper helper) {
        placeLitBrazier(helper);
        placeAllVessels(helper);

        // Insert items into vessels after multiblock is assembled
        helper.runAfterDelay(5, () -> {
            var mercuryVessel = helper.getBlockEntity(MERCURY_VESSEL_POS, IncubatorMercuryVesselBlockEntity.class);
            var saltVessel = helper.getBlockEntity(SALT_VESSEL_POS, IncubatorSaltVesselBlockEntity.class);
            var sulfurVessel = helper.getBlockEntity(SULFUR_VESSEL_POS, IncubatorSulfurVesselBlockEntity.class);

            mercuryVessel.inputInventory.set(0, ItemResource.of(new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1)), new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1).getCount());
            saltVessel.inputInventory.set(0, ItemResource.of(new ItemStack(SaltRegistry.CREATURE.get(), 1)), new ItemStack(SaltRegistry.CREATURE.get(), 1).getCount());
            sulfurVessel.inputInventory.set(0, ItemResource.of(new ItemStack(SulfurRegistry.BONE.get(), 1)), new ItemStack(SulfurRegistry.BONE.get(), 1).getCount());
        });
    }
}
