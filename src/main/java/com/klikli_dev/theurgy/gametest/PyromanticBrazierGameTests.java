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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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
            blockEntity.inventory.set(0, ItemResource.of(new ItemStack(Items.COAL, 1)), new ItemStack(Items.COAL, 1).getCount());
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
            ItemStack remainder;
            try (var tx = Transaction.openRoot()) { remainder = ItemUtil.insertItemReturnRemaining(blockEntity.inventory, 0, new ItemStack(Items.COAL, 1), false, tx); tx.commit(); }
            helper.assertTrue(
                    remainder.isEmpty(),
                    "Coal should be accepted as fuel, remainder should be empty"
            );
            helper.assertTrue(
                    !ItemUtil.getStack(blockEntity.inventory, 0).isEmpty(),
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
            ItemStack remainder;
            try (var tx = Transaction.openRoot()) { remainder = ItemUtil.insertItemReturnRemaining(blockEntity.inventory, 0, new ItemStack(Items.DIAMOND, 1), false, tx); tx.commit(); }
            helper.assertTrue(
                    remainder.getCount() == 1,
                    "Diamond should be rejected as non-fuel, remainder should be 1"
            );
            helper.assertTrue(
                    ItemUtil.getStack(blockEntity.inventory, 0).isEmpty(),
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
            blockEntity.inventory.set(0, ItemResource.of(new ItemStack(Items.COAL, 1)), new ItemStack(Items.COAL, 1).getCount());
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            helper.assertTrue(
                    ItemUtil.getStack(blockEntity.inventory, 0).isEmpty(),
                    "Coal should be consumed after burning"
            );
        });
    }

    /**
     * Tests that stacked fuel is decremented when the brazier starts burning.
     */
    public static void stackedFuelShrinksWhenConsumed(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            blockEntity.inventory.set(0, ItemResource.of(new ItemStack(Items.COAL, 2)), new ItemStack(Items.COAL, 2).getCount());
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            var fuelStack = ItemUtil.getStack(blockEntity.inventory, 0);
            helper.assertBlockProperty(BRAZIER_POS, BlockStateProperties.LIT, true);
            helper.assertTrue(
                    fuelStack.is(Items.COAL) && fuelStack.getCount() == 1,
                    "Stacked fuel should shrink from 2 coal to 1 when the brazier starts burning"
            );
        });
    }

    /**
     * Tests that fuel with a crafting remainder leaves that remainder behind when consumed.
     */
    public static void fuelLeavesCraftingRemainder(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            blockEntity.inventory.set(0, ItemResource.of(new ItemStack(Items.LAVA_BUCKET, 1)), new ItemStack(Items.LAVA_BUCKET, 1).getCount());
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            var fuelStack = ItemUtil.getStack(blockEntity.inventory, 0);
            helper.assertBlockProperty(BRAZIER_POS, BlockStateProperties.LIT, true);
            helper.assertTrue(
                    fuelStack.is(Items.BUCKET) && fuelStack.getCount() == 1,
                    "Lava bucket fuel should leave one bucket behind when consumed"
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
            blockEntity.inventory.set(0, ItemResource.of(new ItemStack(Items.COAL, 1)), new ItemStack(Items.COAL, 1).getCount());
            helper.assertTrue(
                    !ItemUtil.getStack(blockEntity.inventory, 0).isEmpty(),
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
                    ItemUtil.getStack(blockEntity.inventory, 0).isEmpty(),
                    "Brazier inventory should be empty after removing fuel with empty hand"
            );
        });
    }

    public static void rejectedPlaceableBlockDoesNotDuplicate(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            var player = helper.makeMockPlayer(GameType.SURVIVAL);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIRT, 1));

            helper.useBlock(BRAZIER_POS, player, centeredHitResult(helper, BRAZIER_POS));
            helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Placed block should be consumed on first click");
            helper.assertBlockPresent(Blocks.DIRT, ABOVE_BRAZIER_POS);
            helper.assertTrue(ItemUtil.getStack(blockEntity.inventory, 0).isEmpty(), "Brazier should not accept rejected blocks as fuel");

            helper.useBlock(BRAZIER_POS, player, centeredHitResult(helper, BRAZIER_POS));
            helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Second click should not recreate the consumed block");
            helper.assertBlockPresent(Blocks.DIRT, ABOVE_BRAZIER_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that fuel items are dropped as entities when the brazier is broken.
     */
    public static void dropsItemsWhenBroken(GameTestHelper helper) {
        helper.setBlock(BRAZIER_POS, BlockRegistry.PYROMANTIC_BRAZIER.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            blockEntity.inventory.set(0, ItemResource.of(new ItemStack(Items.COAL, 3)), new ItemStack(Items.COAL, 3).getCount());
        });

        helper.runAfterDelay(2, () -> {
            helper.destroyBlock(BRAZIER_POS);
        });

        helper.succeedWhen(() -> {
            // One coal is consumed as soon as the brazier starts burning, so only the remaining fuel drops.
            helper.assertItemEntityCountIs(Items.COAL, BRAZIER_POS, 2.0, 2);
        });
    }

    private static BlockHitResult centeredHitResult(GameTestHelper helper, BlockPos pos) {
        return new BlockHitResult(Vec3.atCenterOf(helper.absolutePos(pos)), Direction.UP, helper.absolutePos(pos), false);
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
            blockEntity.inventory.set(0, ItemResource.of(new ItemStack(Items.COAL, 64)), new ItemStack(Items.COAL, 64).getCount());
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
            blockEntity.inventory.set(0, ItemResource.of(new ItemStack(Items.STICK, 1)), new ItemStack(Items.STICK, 1).getCount());
        });

        // Wait for fuel to run out, then verify no heat
        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(BRAZIER_POS, PyromanticBrazierBlockEntity.class);
            // First ensure fuel has been consumed
            helper.assertTrue(
                    ItemUtil.getStack(blockEntity.inventory, 0).isEmpty(),
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
