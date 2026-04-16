// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.logisticsmercuryfluxconnector.LogisticsMercuryFluxConnectorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Game tests for the Mercury Flux Logistics Connector.
 * <p>
 * Layout (all on y=2):
 * <pre>
 *   [Catalyst] [ConnectorA]  ...  [ConnectorB] [Capacitor1]
 *                                   [ConnectorC] [Capacitor2]
 * </pre>
 * ConnectorA is attached to the catalyst (faces EAST, attached block is opposite the facing).
 * ConnectorB is attached to Capacitor1 (faces WEST, attached block is opposite the facing).
 * ConnectorC is attached to Capacitor2 (faces WEST, attached block is opposite the facing).
 * All connectors are wired into the same logistics network.
 * <p>
 * The catalyst pushes flux into ConnectorA's buffer (source→conduit).
 * ConnectorA forwards through the network to ConnectorB and ConnectorC's attached blocks (sinks).
 */
public class MercuryFluxConnectorGameTests {

    // Catalyst at (1,2,2), connectorA at (2,2,2) facing EAST so it targets the catalyst to its WEST
    private static final BlockPos CATALYST_POS = new BlockPos(1, 2, 2);
    private static final BlockPos CONNECTOR_A_POS = new BlockPos(2, 2, 2);

    // Capacitor1 at (5,2,2), connectorB at (4,2,2) facing WEST so it targets the capacitor to its EAST
    private static final BlockPos CONNECTOR_B_POS = new BlockPos(4, 2, 2);
    private static final BlockPos CAPACITOR_1_POS = new BlockPos(5, 2, 2);

    // Capacitor2 at (5,2,4), connectorC at (4,2,4) facing WEST so it targets the capacitor to its EAST
    private static final BlockPos CONNECTOR_C_POS = new BlockPos(4, 2, 4);
    private static final BlockPos CAPACITOR_2_POS = new BlockPos(5, 2, 4);

    /**
     * Tests that a Mercury Flux Connector can be placed and has the correct default state.
     */
    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(CONNECTOR_A_POS, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));

        helper.assertBlockPresent(BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get(), CONNECTOR_A_POS);
        helper.assertBlockProperty(CONNECTOR_A_POS, BlockStateProperties.FACING, Direction.WEST);
        helper.succeed();
    }

    /**
     * Tests that the connector's buffer starts empty.
     */
    public static void bufferStartsEmpty(GameTestHelper helper) {
        helper.setBlock(CONNECTOR_A_POS, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));

        helper.runAfterDelay(1, () -> {
            var be = helper.getBlockEntity(CONNECTOR_A_POS, LogisticsMercuryFluxConnectorBlockEntity.class);
            helper.assertTrue(
                    be.leafNode().buffer().getEnergyStored() == 0,
                    "Connector buffer should start empty"
            );
        });

        helper.succeedWhen(() -> {
            var be = helper.getBlockEntity(CONNECTOR_A_POS, LogisticsMercuryFluxConnectorBlockEntity.class);
            helper.assertTrue(
                    be.leafNode().buffer().getEnergyStored() == 0,
                    "Connector buffer should still be empty"
            );
        });
    }

    /**
     * Tests that the connector exposes a MercuryFluxStorage capability
     * that source blocks can push into.
     */
    public static void exposesFluxCapability(GameTestHelper helper) {
        helper.setBlock(CONNECTOR_A_POS, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));

        helper.runAfterDelay(1, () -> {
            var fluxStorage = helper.getLevel().getCapability(
                    CapabilityRegistry.MERCURY_FLUX_HANDLER,
                    helper.absolutePos(CONNECTOR_A_POS),
                    null
            );
            helper.assertTrue(fluxStorage != null, "Connector should expose MERCURY_FLUX_HANDLER capability");

            // Push some flux into the buffer
            int received = fluxStorage.receiveEnergy(500, false);
            helper.assertTrue(received == 500, "Should be able to push 500 flux into connector buffer, got " + received);

            var be = helper.getBlockEntity(CONNECTOR_A_POS, LogisticsMercuryFluxConnectorBlockEntity.class);
            helper.assertTrue(
                    be.leafNode().buffer().getEnergyStored() == 500,
                    "Connector buffer should have 500 flux after push"
            );

            helper.succeed();
        });
    }

    /**
     * Tests that flux is forwarded from a catalyst through a connector to a capacitor.
     * Setup: [Catalyst] [ConnectorA]---wire---[ConnectorB] [Capacitor1]
     * The catalyst pushes into ConnectorA's buffer, which forwards through the network
     * to ConnectorB's attached capacitor.
     */
    public static void forwardsFluxFromCatalystToCapacitor(GameTestHelper helper) {
        // Place blocks
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());
        helper.setBlock(CONNECTOR_A_POS, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.EAST));
        helper.setBlock(CONNECTOR_B_POS, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));
        helper.setBlock(CAPACITOR_1_POS, BlockRegistry.MERCURY_CAPACITOR.get());

        helper.runAfterDelay(1, () -> {
            // Insert mercury shard into catalyst to generate flux
            var catalystBE = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            catalystBE.inventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));

            connectWithWire(helper, CONNECTOR_A_POS, CONNECTOR_B_POS);
        });

        // Wait for flux generation + push + forward (catalyst pushes every 20 ticks, connector forwards every 20 ticks)
        helper.succeedWhen(() -> {
            var capacitorBE = helper.getBlockEntity(CAPACITOR_1_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    capacitorBE.mercuryFluxStorage.getEnergyStored() > 0,
                    "Capacitor should have received flux forwarded through the logistics network"
            );
        });
    }

    /**
     * Tests that flux is distributed to multiple capacitors through the logistics network.
     * Setup: [Catalyst] [ConnectorA]---wire---[ConnectorB] [Capacitor1]
     *                                      \---[ConnectorC] [Capacitor2]
     * Both capacitors should receive some flux.
     */
    public static void forwardsFluxToMultipleCapacitors(GameTestHelper helper) {
        // Place blocks
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());
        helper.setBlock(CONNECTOR_A_POS, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.EAST));
        helper.setBlock(CONNECTOR_B_POS, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));
        helper.setBlock(CAPACITOR_1_POS, BlockRegistry.MERCURY_CAPACITOR.get());
        helper.setBlock(CONNECTOR_C_POS, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));
        helper.setBlock(CAPACITOR_2_POS, BlockRegistry.MERCURY_CAPACITOR.get());

        helper.runAfterDelay(1, () -> {
            // Insert mercury shard into catalyst
            var catalystBE = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            catalystBE.inventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));

            connectWithWire(helper, CONNECTOR_A_POS, CONNECTOR_B_POS);
            connectWithWire(helper, CONNECTOR_A_POS, CONNECTOR_C_POS);
        });

        // Wait for flux to be generated, pushed, and forwarded to both capacitors
        helper.succeedWhen(() -> {
            var cap1BE = helper.getBlockEntity(CAPACITOR_1_POS, MercuryCapacitorBlockEntity.class);
            var cap2BE = helper.getBlockEntity(CAPACITOR_2_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    cap1BE.mercuryFluxStorage.getEnergyStored() > 0,
                    "Capacitor 1 should have received flux through the logistics network"
            );
            helper.assertTrue(
                    cap2BE.mercuryFluxStorage.getEnergyStored() > 0,
                    "Capacitor 2 should have received flux through the logistics network"
            );
        });
    }

    private static void connectWithWire(GameTestHelper helper, BlockPos from, BlockPos to) {
        var player = helper.makeMockPlayer(GameType.CREATIVE);
        player.getAbilities().instabuild = true;
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.COPPER_WIRE.get(), 1));

        helper.useBlock(from, player, centeredHitResult(helper, from));
        helper.useBlock(to, player, centeredHitResult(helper, to));
    }

    private static BlockHitResult centeredHitResult(GameTestHelper helper, BlockPos pos) {
        return new BlockHitResult(Vec3.atCenterOf(helper.absolutePos(pos)), Direction.UP, helper.absolutePos(pos), false);
    }
}
