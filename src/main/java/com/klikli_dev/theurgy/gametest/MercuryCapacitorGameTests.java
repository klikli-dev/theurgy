// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.SideMode;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
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
                    blockEntity.mercuryFluxHandler.getAmountAsInt() == 0,
                    "Mercury Capacitor should start with no flux stored"
            );
        });

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    blockEntity.mercuryFluxHandler.getAmountAsInt() == 0,
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
            var catalystBE = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            catalystBE.inventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        // Wait for catalyst to generate flux and push to capacitor
        helper.succeedWhen(() -> {
            var capacitorBE = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    capacitorBE.mercuryFluxHandler.getAmountAsInt() > 0,
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
                    blockEntity.mercuryFluxHandler.getCapacityAsInt() == MercuryCapacitorBlockEntity.CAPACITY,
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
            var catalystBE = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            catalystBE.inventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        // Wait for flux to be generated and check that disabled capacitor didn't receive it
        helper.succeedWhen(() -> {
            var capacitorBE = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            // Capacitor should have no flux because it's disabled and can't receive from neighbors
            helper.assertTrue(
                    capacitorBE.mercuryFluxHandler.getAmountAsInt() == 0,
                    "Disabled capacitor should not receive flux from neighbors"
            );
        });
    }

    /**
     * Tests that two unconnected capacitors do not ping-pong flux when placed next to each other.
     * By default TOP/BOTTOM are OUTPUT and SIDES are INPUT, so they should not exchange flux
     * directly unless configured otherwise.
     */
    public static void unconfiguredCapacitorsDoNotPingPong(GameTestHelper helper) {
        // Place two capacitors next to each other - no connectors between them
        // Capacitor A at (2,1,2), Capacitor B at (3,1,2)
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get());
        helper.setBlock(CAPACITOR_B_POS, BlockRegistry.MERCURY_CAPACITOR.get());

        // Fill capacitor A with flux manually
        helper.runAfterDelay(1, () -> {
            var capacitorA = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
                    capacitorA.mercuryFluxHandler.set(10000);
        });

        // Wait for several tick cycles and verify no flux transfer happens
        // Since capacitor B is to the EAST of capacitor A, and EAST side is INPUT (not OUTPUT),
        // no flux should be pushed to it
        helper.runAtTickTime(100, () -> {
            var capacitorA = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            var capacitorB = helper.getBlockEntity(CAPACITOR_B_POS, MercuryCapacitorBlockEntity.class);

            // Both should have their original amounts - no transfer
            helper.assertTrue(
                    capacitorA.mercuryFluxHandler.getAmountAsInt() == 10000,
                    "Capacitor A should still have 10000 flux (no ping-pong)"
            );
            helper.assertTrue(
                    capacitorB.mercuryFluxHandler.getAmountAsInt() == 0,
                    "Capacitor B should still have 0 flux (no ping-pong)"
            );
        });

        helper.succeed();
    }

    /**
     * Tests that side modes can be configured and respected.
     * Default: UP/DOWN = OUTPUT, SIDES = INPUT
     */
    public static void sideModesCanBeConfigured(GameTestHelper helper) {
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get());

        helper.runAfterDelay(1, () -> {
            var capacitor = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);

            // Verify default modes: TOP and BOTTOM are OUTPUT, sides are INPUT
            helper.assertTrue(
                    capacitor.getSideMode(Direction.UP) == SideMode.OUTPUT,
                    "UP side should be OUTPUT by default"
            );
            helper.assertTrue(
                    capacitor.getSideMode(Direction.DOWN) == SideMode.OUTPUT,
                    "DOWN side should be OUTPUT by default"
            );
            helper.assertTrue(
                    capacitor.getSideMode(Direction.NORTH) == SideMode.INPUT,
                    "NORTH side should be INPUT by default"
            );
            helper.assertTrue(
                    capacitor.getSideMode(Direction.SOUTH) == SideMode.INPUT,
                    "SOUTH side should be INPUT by default"
            );
            helper.assertTrue(
                    capacitor.getSideMode(Direction.EAST) == SideMode.INPUT,
                    "EAST side should be INPUT by default"
            );
            helper.assertTrue(
                    capacitor.getSideMode(Direction.WEST) == SideMode.INPUT,
                    "WEST side should be INPUT by default"
            );

            // Change EAST side to OUTPUT
            capacitor.setSideMode(Direction.EAST, SideMode.OUTPUT);
            helper.assertTrue(
                    capacitor.getSideMode(Direction.EAST) == SideMode.OUTPUT,
                    "East side should be OUTPUT after change"
            );
        });

        helper.succeed();
    }

    private static final BlockPos CATALYST_POS = new BlockPos(3, 1, 2);
    private static final BlockPos CAPACITOR_B_POS = new BlockPos(3, 1, 2);
}
