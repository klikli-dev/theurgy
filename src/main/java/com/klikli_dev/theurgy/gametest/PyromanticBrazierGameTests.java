// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier.PyromanticBrazierBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PyromanticBrazierGameTests {

    private static final BlockPos BRAZIER_POS = new BlockPos(2, 1, 2);
    /**
     * The block above the brazier, where heat-consuming blocks are placed.
     */
    private static final BlockPos ABOVE_BRAZIER_POS = BRAZIER_POS.above();

    // --- Placement & State ---

    /**
     * Tests that a Pyromantic Brazier can be placed and has the correct default state (LIT=false).
     */
    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());
        helper.assertBlockPresent(BlockRegistry.PYROMANTIC_BRAZIER.get(), BRAZIER_POS);
        helper.assertBlockProperty(BRAZIER_POS, BlockStateProperties.LIT, false);
        helper.succeed();
    }

    /**
     * Tests that the LIT state becomes true when fuel is inserted and consumed.
     */
    public static void litStateUpdatesWhenFuelInserted(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            blockEntity.inventory.setStackInSlot(0, new ItemStack(Items.COAL, 1));
        });

        helper.succeedWhen(() -> {
            helper.assertBlockProperty(BRAZIER_POS, BlockStateProperties.LIT, true);
        });
    }

    // --- Fuel Handling ---

    /**
     * Tests that a fuel item can be inserted into the brazier via the inventory.
     */
    public static void insertFuelItem(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            var remainder = blockEntity.inventory.insertItem(0, new ItemStack(Items.COAL, 1), false);
            helper.assertTrue(
                    remainder.isEmpty(),
                    "Coal should be accepted as fuel, remainder should be empty"
            );
            helper.assertTrue(
                    !blockEntity.inventory.getStackInSlot(0).isEmpty(),
                    "Brazier inventory should contain the inserted coal"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that non-burnable items are rejected by the brazier inventory.
     */
    public static void rejectsNonBurnableItems(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            var remainder = blockEntity.inventory.insertItem(0, new ItemStack(Items.DIAMOND, 1), false);
            helper.assertTrue(
                    remainder.getCount() == 1,
                    "Diamond should be rejected as non-fuel, remainder should be 1"
            );
            helper.assertTrue(
                    blockEntity.inventory.getStackInSlot(0).isEmpty(),
                    "Brazier inventory should still be empty after rejecting non-fuel"
            );
            helper.succeed();
        });
    }

    /**
     * Tests that fuel is consumed over time (stack shrinks and eventually becomes empty).
     */
    public static void fuelIsConsumedOverTime(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            // Insert exactly 1 coal - it should be consumed when lit
            blockEntity.inventory.setStackInSlot(0, new ItemStack(Items.COAL, 1));
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            helper.assertTrue(
                    blockEntity.inventory.getStackInSlot(0).isEmpty(),
                    "Coal should be consumed after burning"
            );
        });
    }

    /**
     * Tests that fuel can be removed by right-clicking the brazier with an empty hand.
     * The block's useItemOn handler ejects the fuel into the player's inventory.
     */
    public static void removeFuelViaEmptyHand(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            blockEntity.inventory.setStackInSlot(0, new ItemStack(Items.COAL, 1));
            helper.assertTrue(
                    !blockEntity.inventory.getStackInSlot(0).isEmpty(),
                    "Brazier should contain coal before removal"
            );
        });

        helper.runAfterDelay(2, () -> {
            // Simulate right-click with empty hand (survival mode player)
            helper.useBlock(BRAZIER_POS);
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            helper.assertTrue(
                    blockEntity.inventory.getStackInSlot(0).isEmpty(),
                    "Brazier inventory should be empty after removing fuel with empty hand"
            );
        });
    }

    /**
     * Tests that fuel items are dropped as entities when the brazier is broken.
     */
    public static void dropsItemsWhenBroken(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            blockEntity.inventory.setStackInSlot(0, new ItemStack(Items.COAL, 3));
        });

        helper.runAfterDelay(2, () -> {
            helper.destroyBlock(BRAZIER_POS);
        });

        helper.succeedWhen(() -> {
            // Verify coal items were dropped at the brazier position
            helper.assertItemEntityPresent(Items.COAL, BRAZIER_POS, 2.0);
        });
    }

    // --- Heat Provision ---

    /**
     * Helper method that tests the brazier provides heat to a given block placed above it.
     */
    private static void testProvidesHeatTo(GameTestHelper helper, Block blockToHeat, String blockName) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());
        helper.setBlock(ABOVE_BRAZIER_POS, blockToHeat);

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            blockEntity.inventory.setStackInSlot(0, new ItemStack(Items.COAL, 64));
        });

        helper.succeedWhen(() -> {
            var heatProvider = helper.getLevel().getCapability(
                    CapabilityRegistry.HEAT_PROVIDER,
                    helper.absolutePos(BRAZIER_POS),
                    Direction.UP
            );
            helper.assertTrue(
                    heatProvider != null && heatProvider.isHot(),
                    "Brazier should provide heat to " + blockName + " when lit"
            );
        });
    }

    /**
     * Tests that the brazier provides heat to an adjacent Calcination Oven placed above it.
     */
    public static void providesHeatToCalcinationOven(GameTestHelper helper) {
        testProvidesHeatTo(helper, BlockRegistry.CALCINATION_OVEN.get(), "calcination oven");
    }

    /**
     * Tests that the brazier provides heat to an adjacent Liquefaction Cauldron placed above it.
     */
    public static void providesHeatToLiquefactionCauldron(GameTestHelper helper) {
        testProvidesHeatTo(helper, BlockRegistry.LIQUEFACTION_CAULDRON.get(), "liquefaction cauldron");
    }

    /**
     * Tests that the brazier provides heat to an adjacent Distiller placed above it.
     */
    public static void providesHeatToDistiller(GameTestHelper helper) {
        testProvidesHeatTo(helper, BlockRegistry.DISTILLER.get(), "distiller");
    }

    /**
     * Tests that the brazier stops providing heat when fuel runs out.
     * Uses a single piece of fuel and waits for it to burn out, then verifies heat is no longer provided.
     */
    public static void stopsProvidingHeatWhenFuelRunsOut(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            // Insert a single stick (100 ticks burn time) - short burn time for faster test
            blockEntity.inventory.setStackInSlot(0, new ItemStack(Items.STICK, 1));
        });

        // Wait for fuel to run out, then verify no heat
        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            // First ensure fuel has been consumed
            helper.assertTrue(
                    blockEntity.inventory.getStackInSlot(0).isEmpty(),
                    "Fuel should have been consumed"
            );
            // Then verify the brazier is no longer lit
            helper.assertBlockProperty(BRAZIER_POS, BlockStateProperties.LIT, false);

            // Finally, verify it's not providing heat
            var heatProvider = helper.getLevel().getCapability(
                    CapabilityRegistry.HEAT_PROVIDER,
                    helper.absolutePos(BRAZIER_POS),
                    Direction.UP
            );
            helper.assertTrue(
                    heatProvider != null && !heatProvider.isHot(),
                    "Brazier should stop providing heat when fuel runs out"
            );
        });
    }
}
