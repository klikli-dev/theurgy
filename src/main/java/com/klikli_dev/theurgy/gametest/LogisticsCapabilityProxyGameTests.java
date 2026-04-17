// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy.LogisticsCapabilityProxyBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockEntity;
import com.klikli_dev.theurgy.content.storage.FluidStorageHelper;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.List;

public class LogisticsCapabilityProxyGameTests {

    private static final BlockPos PROXY_POS = new BlockPos(2, 2, 2);
    private static final BlockPos PROBE_POS = new BlockPos(3, 2, 2);

    public static void placementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(PROXY_POS, BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH));
        helper.setBlock(PROBE_POS, BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.EAST));

        helper.assertBlockPresent(BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get(), PROXY_POS);
        helper.assertBlockProperty(PROXY_POS, BlockStateProperties.FACING, Direction.NORTH);
        
        helper.assertBlockPresent(BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get(), PROBE_POS);
        helper.assertBlockProperty(PROBE_POS, BlockStateProperties.FACING, Direction.EAST);
        
        helper.succeed();
    }

    public static void fluidCapabilityForwarding(GameTestHelper helper) {
        BlockPos tankPos = new BlockPos(2, 2, 2);
        BlockPos probePos = new BlockPos(1, 2, 2); // Faces WEST, targets EAST (2,2,2)
        BlockPos proxyPos = new BlockPos(3, 2, 2);

        helper.setBlock(tankPos, BlockRegistry.SAL_AMMONIAC_TANK.get().defaultBlockState());
        helper.setBlock(probePos, BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));
        helper.setBlock(proxyPos, BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get().defaultBlockState());

        helper.runAfterDelay(1, () -> {
            var tankBE = helper.getBlockEntity(tankPos, SalAmmoniacTankBlockEntity.class);

            // Connect proxy to probe with wires
            var logistics = Logistics.get();
            logistics.add(GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(proxyPos)),
                          GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(probePos)));

            // Fill tank with some fluid
            tankBE.tank.setFluid(new FluidStack(Fluids.WATER, 1000));

            // Access capability through proxy
            var proxyFluidHandler = helper.getLevel().getCapability(CapabilityRegistry.FLUID_HANDLER, helper.absolutePos(proxyPos), null);
            
            helper.assertTrue(proxyFluidHandler != null, "Proxy should expose fluid capability");
            helper.assertTrue(FluidStorageHelper.getFluidInTank(proxyFluidHandler, 0).getAmount() == 1000, "Proxy should show correct fluid amount from tank");
            
            // Test drain through proxy
            try (var tx = Transaction.openRoot()) {
                int extracted = proxyFluidHandler.extract(FluidResource.of(Fluids.WATER), 500, tx);
                tx.commit();
                helper.assertTrue(extracted == 500, "Should be able to drain fluid through proxy");
            }
            helper.assertTrue(tankBE.tank.getFluidAmount() == 500, "Tank should be drained");
            
            helper.succeed();
        });
    }

    public static void fluidCapabilityRoundRobin(GameTestHelper helper) {
        BlockPos proxyPos = new BlockPos(1, 2, 2);
        BlockPos probe1Pos = new BlockPos(2, 2, 1); // North of proxy
        BlockPos probe2Pos = new BlockPos(2, 2, 3); // South of proxy
        BlockPos tank1Pos = new BlockPos(3, 2, 1); // East of probe1
        BlockPos tank2Pos = new BlockPos(3, 2, 3); // East of probe2

        // Setup tanks
        helper.setBlock(tank1Pos, BlockRegistry.SAL_AMMONIAC_TANK.get().defaultBlockState());
        helper.setBlock(tank2Pos, BlockRegistry.SAL_AMMONIAC_TANK.get().defaultBlockState());

        // Setup probes targeting tanks (probe targets the block opposite its facing)
        helper.setBlock(probe1Pos, BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));
        helper.setBlock(probe2Pos, BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));

        // Setup proxy
        helper.setBlock(proxyPos, BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get().defaultBlockState());

        helper.runAfterDelay(1, () -> {
            var tank1BE = helper.getBlockEntity(tank1Pos, SalAmmoniacTankBlockEntity.class);
            var tank2BE = helper.getBlockEntity(tank2Pos, SalAmmoniacTankBlockEntity.class);

            // Connect everything in the network
            var logistics = Logistics.get();
            var dimension = helper.getLevel().dimension();
            var gProxy = GlobalPos.of(dimension, helper.absolutePos(proxyPos));
            var gProbe1 = GlobalPos.of(dimension, helper.absolutePos(probe1Pos));
            var gProbe2 = GlobalPos.of(dimension, helper.absolutePos(probe2Pos));

            logistics.add(gProxy, gProbe1);
            logistics.add(gProxy, gProbe2);

            // Fill tanks with distinct amounts
            tank1BE.tank.setFluid(new FluidStack(Fluids.WATER, 1000));
            tank2BE.tank.setFluid(new FluidStack(Fluids.WATER, 2000));

            // First request
            var handler1 = helper.getLevel().getCapability(CapabilityRegistry.FLUID_HANDLER, helper.absolutePos(proxyPos), null);
            helper.assertTrue(handler1 != null, "Proxy should expose fluid capability (1)");
            long amount1 = FluidStorageHelper.getFluidInTank(handler1, 0).getAmount();

            // Second request
            var handler2 = helper.getLevel().getCapability(CapabilityRegistry.FLUID_HANDLER, helper.absolutePos(proxyPos), null);
            helper.assertTrue(handler2 != null, "Proxy should expose fluid capability (2)");
            long amount2 = FluidStorageHelper.getFluidInTank(handler2, 0).getAmount();

            // Verify they are different (round-robin)
            helper.assertTrue(amount1 != amount2, "Successive requests should hit different tanks. Amounts: " + amount1 + ", " + amount2);
            helper.assertTrue((amount1 == 1000 && amount2 == 2000) || (amount1 == 2000 && amount2 == 1000), "Amounts should be 1000 and 2000 in some order");

            helper.succeed();
        });
    }
}
