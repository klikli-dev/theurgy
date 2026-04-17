// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationResultPedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationSourcePedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationTargetPedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.SulfuricFluxEmitterSelectedPoint;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.NiterRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;
import java.lang.Runnable;

public class SulfuricFluxEmitterGameTests {

    private static final BlockPos EMITTER_POS = new BlockPos(2, 1, 2);
    private static final BlockPos TARGET_PEDESTAL_POS = new BlockPos(2, 1, 0);
    private static final BlockPos SOURCE_PEDESTAL_POS = new BlockPos(0, 1, 0);
    private static final BlockPos RESULT_PEDESTAL_POS = new BlockPos(4, 1, 0);

    // --- Helpers ---

    private static void setupArray(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get());
        helper.setBlock(TARGET_PEDESTAL_POS, BlockRegistry.REFORMATION_TARGET_PEDESTAL.get());
        helper.setBlock(SOURCE_PEDESTAL_POS, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get());
        helper.setBlock(RESULT_PEDESTAL_POS, BlockRegistry.REFORMATION_RESULT_PEDESTAL.get());

        helper.runAtTickTime(1, () -> {
            var emitter = helper.getBlockEntity(EMITTER_POS, SulfuricFluxEmitterBlockEntity.class);
            emitter.setSelectedPoints(
                    List.of(new SulfuricFluxEmitterSelectedPoint(helper.absolutePos(SOURCE_PEDESTAL_POS), SulfuricFluxEmitterSelectedPoint.Type.SOURCE)),
                    new SulfuricFluxEmitterSelectedPoint(helper.absolutePos(TARGET_PEDESTAL_POS), SulfuricFluxEmitterSelectedPoint.Type.TARGET),
                    new SulfuricFluxEmitterSelectedPoint(helper.absolutePos(RESULT_PEDESTAL_POS), SulfuricFluxEmitterSelectedPoint.Type.RESULT)
            );
        });
    }

    // --- Placement & State ---

    public static void placementWithFacing(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));

        helper.assertBlockPresent(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), EMITTER_POS);
        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.FACING, Direction.UP);
        helper.succeed();
    }

    // --- Energy ---

    public static void hasEnergyStorage(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.SULFURIC_FLUX_EMITTER.get());

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(EMITTER_POS, SulfuricFluxEmitterBlockEntity.class);
            helper.assertTrue(blockEntity != null, "Block entity should exist");
            helper.assertTrue(blockEntity.mercuryFluxHandler.getCapacityAsInt() == SulfuricFluxEmitterBlockEntity.CAPACITY, "Should have correct energy capacity (" + SulfuricFluxEmitterBlockEntity.CAPACITY + ")");
        });
    }

    // --- Target Selection (Linking) ---

    public static void linkPedestals(GameTestHelper helper) {
        setupArray(helper);

        helper.succeedWhen(() -> {
            var emitter = helper.getBlockEntity(EMITTER_POS, SulfuricFluxEmitterBlockEntity.class);
            helper.assertTrue(emitter.isValidMultiblock(), "Multiblock should be valid after linking");
        });
    }

    // --- Reformation Processing ---

    public static void reformationProcessing(GameTestHelper helper) {
        setupArray(helper);

        helper.runAtTickTime(2, () -> {
            var emitter = helper.getBlockEntity(EMITTER_POS, SulfuricFluxEmitterBlockEntity.class);
                // Add initial energy
                    try (var tx = net.neoforged.neoforge.transfer.transaction.Transaction.openRoot()) {
                        emitter.mercuryFluxHandler.insert(500, tx);
                        tx.commit();
                    }

            var source = helper.getBlockEntity(SOURCE_PEDESTAL_POS, ReformationSourcePedestalBlockEntity.class);
            source.inputInventory.setStackInSlot(0, new ItemStack(NiterRegistry.MOBS_ABUNDANT.get(), 1));

            var target = helper.getBlockEntity(TARGET_PEDESTAL_POS, ReformationTargetPedestalBlockEntity.class);
            target.inputInventory.setStackInSlot(0, new ItemStack(SulfurRegistry.BONE.get(), 1));
        });

        // Wait for processing to start and complete. Recipe takes 100 ticks.
        helper.succeedWhen(() -> {
            var result = helper.getBlockEntity(RESULT_PEDESTAL_POS, ReformationResultPedestalBlockEntity.class);
            var resultStack = result.outputInventory.getStackInSlot(0);

            helper.assertTrue(ItemStack.matches(resultStack, new ItemStack(SulfurRegistry.BONE.get())), "Result pedestal should contain one bone sulfur");

            var source = helper.getBlockEntity(SOURCE_PEDESTAL_POS, ReformationSourcePedestalBlockEntity.class);
            helper.assertTrue(source.inputInventory.getStackInSlot(0).isEmpty(), "Source item should be consumed");

            var target = helper.getBlockEntity(TARGET_PEDESTAL_POS, ReformationTargetPedestalBlockEntity.class);
            helper.assertTrue(target.inputInventory.getStackInSlot(0).isEmpty(), "Target item should be consumed");
        });
    }
}


