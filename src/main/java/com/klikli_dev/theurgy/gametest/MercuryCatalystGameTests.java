// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class MercuryCatalystGameTests {

    private static final BlockPos CATALYST_POS = new BlockPos(2, 1, 2);
    private static final BlockPos ABOVE_CATALYST_POS = CATALYST_POS.above();

    /**
     * Tests that a Mercury Catalyst block can be placed and has the correct default state (enabled = true).
     */
    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());
        helper.assertBlockPresent(BlockRegistry.MERCURY_CATALYST.get(), CATALYST_POS);
        helper.assertBlockProperty(CATALYST_POS, BlockStateProperties.ENABLED, true);
        helper.succeed();
    }

    /**
     * Tests that inserting a mercury shard into the catalyst starts the crafting process
     * and generates mercury flux over time.
     */
    public static void mercuryShardGeneratesFlux(GameTestHelper helper) {
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            blockEntity.inventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            helper.assertTrue(
                    blockEntity.mercuryFluxHandler.getAmountAsInt() > 0,
                    "Mercury Catalyst should have generated mercury flux from mercury shard"
            );
        });
    }

    /**
     * Tests that the Mercury Catalyst consumes the mercury shard input item during crafting.
     */
    public static void mercuryShardIsConsumed(GameTestHelper helper) {
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            blockEntity.inventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            helper.assertTrue(
                    blockEntity.inventory.getStackInSlot(0).isEmpty(),
                    "Mercury shard should be consumed during catalysation"
            );
        });
    }

    public static void rejectedPlaceableBlockDoesNotDuplicate(GameTestHelper helper) {
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            var player = helper.makeMockPlayer(GameType.SURVIVAL);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIRT, 1));

            helper.useBlock(CATALYST_POS, player, centeredHitResult(helper, CATALYST_POS));
            helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Placed block should be consumed on first click");
            helper.assertBlockPresent(Blocks.DIRT, ABOVE_CATALYST_POS);
            helper.assertTrue(blockEntity.inventory.getStackInSlot(0).isEmpty(), "Catalyst should not store rejected placeable blocks");

            helper.useBlock(CATALYST_POS, player, centeredHitResult(helper, CATALYST_POS));
            helper.assertTrue(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Second click should not recreate the consumed block");
            helper.assertBlockPresent(Blocks.DIRT, ABOVE_CATALYST_POS);
            helper.succeed();
        });
    }

    /**
     * Tests that the Mercury Catalyst still generates flux internally when disabled (e.g. by redstone),
     * even though it does not push it to neighbors.
     */
    public static void disabledCatalystStillGeneratesFlux(GameTestHelper helper) {
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get()
                .defaultBlockState().setValue(BlockStateProperties.ENABLED, false));

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            blockEntity.inventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        // Wait some ticks then verify flux was still generated (crafting still works when disabled,
        // but flux pushing to neighbors is disabled)
        helper.runAfterDelay(60, () -> {
            var blockEntity = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            // Crafting should still work, flux should be stored internally
            helper.assertTrue(
                    blockEntity.mercuryFluxHandler.getAmountAsInt() > 0,
                    "Crafting should still generate flux even when disabled"
            );
            helper.succeed();
        });
    }

    private static BlockHitResult centeredHitResult(GameTestHelper helper, BlockPos pos) {
        return new BlockHitResult(Vec3.atCenterOf(helper.absolutePos(pos)), Direction.UP, helper.absolutePos(pos), false);
    }
}
