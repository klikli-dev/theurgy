// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MercuryCatalystGameTests {

    private static final BlockPos CATALYST_POS = new BlockPos(2, 1, 2);

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
                    blockEntity.mercuryFluxStorage.getEnergyStored() > 0,
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
                    blockEntity.mercuryFluxStorage.getEnergyStored() > 0,
                    "Crafting should still generate flux even when disabled"
            );
            helper.succeed();
        });
    }
}
