// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.logisticscapabilityproxy.LogisticsCapabilityProxyBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
            var logistics = com.klikli_dev.theurgy.logistics.Logistics.get();
            logistics.add(net.minecraft.core.GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(proxyPos)),
                          net.minecraft.core.GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(probePos)));

            // Fill tank with some fluid
            tankBE.tank.setFluid(new FluidStack(net.minecraft.world.level.material.Fluids.WATER, 1000));

            // Access capability through proxy
            var proxyFluidHandler = helper.getLevel().getCapability(CapabilityRegistry.FLUID_HANDLER, helper.absolutePos(proxyPos), null);
            
            helper.assertTrue(proxyFluidHandler != null, "Proxy should expose fluid capability");
            helper.assertTrue(com.klikli_dev.theurgy.content.storage.FluidStorageHelper.getFluidInTank(proxyFluidHandler, 0).getAmount() == 1000, "Proxy should show correct fluid amount from tank");
            
            // Test drain through proxy
            try (var tx = Transaction.openRoot()) {
                int extracted = proxyFluidHandler.extract(FluidResource.of(net.minecraft.world.level.material.Fluids.WATER), 500, tx);
                tx.commit();
                helper.assertTrue(extracted == 500, "Should be able to drain fluid through proxy");
            }
            helper.assertTrue(tankBE.tank.getFluidAmount() == 500, "Tank should be drained");
            
            helper.succeed();
        });
    }
}
