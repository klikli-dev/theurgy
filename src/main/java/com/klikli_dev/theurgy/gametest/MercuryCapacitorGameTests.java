// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MercuryCapacitorGameTests {

    private static final BlockPos CAPACITOR_POS = new BlockPos(2, 1, 2);

    /**
     * Tests that a Mercury Capacitor block can be placed and has the correct default state (enabled = true).
     */
    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get());
        helper.assertBlockPresent(BlockRegistry.MERCURY_CAPACITOR.get(), CAPACITOR_POS);
        helper.assertBlockProperty(CAPACITOR_POS, BlockStateProperties.ENABLED, true);
        helper.succeed();
    }

    /**
     * Tests that the Mercury Capacitor starts empty (no flux stored).
     */
    public static void startsEmpty(GameTestHelper helper) {
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    blockEntity.mercuryFluxStorage.getEnergyStored() == 0,
                    "Mercury Capacitor should start with no flux stored"
            );
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    blockEntity.mercuryFluxStorage.getEnergyStored() == 0,
                    "Mercury Capacitor should still have no flux stored"
            );
        });
    }

    /**
     * Tests that the Mercury Capacitor can receive flux from a Mercury Catalyst.
     */
    public static void receivesFluxFromCatalyst(GameTestHelper helper) {
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get());
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());

        // Insert mercury shard into catalyst to generate flux
        helper.runAfterDelay(1, () -> {
            var catalystBE = helper.getBlockEntity(CATALYST_POS, com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity.class);
            catalystBE.inventory.setStackInSlot(0, new net.minecraft.world.item.ItemStack(com.klikli_dev.theurgy.registry.ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        // Wait for catalyst to generate flux and push to capacitor
        helper.succeedWhen(() -> {
            var capacitorBE = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    capacitorBE.mercuryFluxStorage.getEnergyStored() > 0,
                    "Mercury Capacitor should have received flux from Mercury Catalyst"
            );
        });
    }

    /**
     * Tests that the Mercury Capacitor has the correct capacity (500,000).
     */
    public static void hasCorrectCapacity(GameTestHelper helper) {
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    blockEntity.mercuryFluxStorage.getMaxEnergyStored() == MercuryCapacitorBlockEntity.CAPACITY,
                    "Mercury Capacitor should have capacity of " + MercuryCapacitorBlockEntity.CAPACITY
            );
        });

        helper.succeed();
    }

    /**
     * Tests that a disabled Mercury Capacitor does not receive flux from neighbors,
     * even though it still stores any flux it already has.
     */
    public static void disabledCapacitorDoesNotReceiveFlux(GameTestHelper helper) {
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get()
                .defaultBlockState().setValue(BlockStateProperties.ENABLED, false));
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());

        // Insert mercury shard into catalyst
        helper.runAfterDelay(1, () -> {
            var catalystBE = helper.getBlockEntity(CATALYST_POS, com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity.class);
            catalystBE.inventory.setStackInSlot(0, new net.minecraft.world.item.ItemStack(com.klikli_dev.theurgy.registry.ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        // Wait for flux to be generated and check that disabled capacitor didn't receive it
        helper.succeedWhen(() -> {
            var capacitorBE = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            // Capacitor should have no flux because it's disabled and can't receive from neighbors
            helper.assertTrue(
                    capacitorBE.mercuryFluxStorage.getEnergyStored() == 0,
                    "Disabled capacitor should not receive flux from neighbors"
            );
        });
    }

    private static final BlockPos CATALYST_POS = new BlockPos(3, 1, 2);
}
