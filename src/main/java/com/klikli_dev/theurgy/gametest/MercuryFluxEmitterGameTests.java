// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercuryfluxemitter.MercuryFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.mercuryfluxemitter.MercuryFluxEmitterSelectedPoint;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class MercuryFluxEmitterGameTests {

    private static final BlockPos CATALYST_POS = new BlockPos(2, 1, 2);
    private static final BlockPos EMITTER_POS = new BlockPos(2, 2, 2);  // Above the catalyst
    private static final BlockPos CAPACITOR_POS = new BlockPos(5, 1, 2); // A few blocks away

    // --- Placement & State ---

    public static void placementWithFacing(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.MERCURY_FLUX_EMITTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));

        helper.assertBlockPresent(BlockRegistry.MERCURY_FLUX_EMITTER.get(), EMITTER_POS);
        helper.assertBlockProperty(EMITTER_POS, BlockStateProperties.FACING, Direction.UP);
        helper.succeed();
    }

    // --- Energy ---

    public static void hasEnergyStorage(GameTestHelper helper) {
        helper.setBlock(EMITTER_POS, BlockRegistry.MERCURY_FLUX_EMITTER.get());

        helper.succeedWhen(() -> {
            var blockEntity = helper.getBlockEntity(EMITTER_POS, MercuryFluxEmitterBlockEntity.class);
            helper.assertTrue(blockEntity != null, "Block entity should exist");
            helper.assertTrue(blockEntity.mercuryFluxHandler.getCapacityAsInt() == MercuryFluxEmitterBlockEntity.CAPACITY, "Should have correct energy capacity (" + MercuryFluxEmitterBlockEntity.CAPACITY + ")");
        });
    }

    // --- Target Selection (Linking) ---

    public static void linkTarget(GameTestHelper helper) {
        // Setup: Catalyst generates flux, Emitter on top, Capacitor a few blocks away
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());
        helper.setBlock(EMITTER_POS, BlockRegistry.MERCURY_FLUX_EMITTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get());

        helper.runAtTickTime(1, () -> {
            var emitter = helper.getBlockEntity(EMITTER_POS, MercuryFluxEmitterBlockEntity.class);
            emitter.setSelectedPoints(
                    List.of(new MercuryFluxEmitterSelectedPoint(helper.absolutePos(CAPACITOR_POS)))
            );
        });

        helper.succeedWhen(() -> {
            var emitter = helper.getBlockEntity(EMITTER_POS, MercuryFluxEmitterBlockEntity.class);
            helper.assertTrue(emitter.getSelectedPoints().size() == 1, "Should have one target linked");
        });
    }

    // --- Flux Transfer Test ---

    public static void transferFluxFromCatalystToCapacitor(GameTestHelper helper) {
        // Setup: Catalyst generates flux, Emitter on top, Capacitor a few blocks away
        helper.setBlock(CATALYST_POS, BlockRegistry.MERCURY_CATALYST.get());
        helper.setBlock(EMITTER_POS, BlockRegistry.MERCURY_FLUX_EMITTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));
        helper.setBlock(CAPACITOR_POS, BlockRegistry.MERCURY_CAPACITOR.get());

        // Link emitter to capacitor
        helper.runAtTickTime(1, () -> {
            var emitter = helper.getBlockEntity(EMITTER_POS, MercuryFluxEmitterBlockEntity.class);
            emitter.setSelectedPoints(
                    List.of(new MercuryFluxEmitterSelectedPoint(helper.absolutePos(CAPACITOR_POS)))
            );
        });

        // Insert mercury shard into catalyst to generate flux
        helper.runAtTickTime(2, () -> {
            var catalyst = helper.getBlockEntity(CATALYST_POS, MercuryCatalystBlockEntity.class);
            catalyst.inventory.setStackInSlot(0, new ItemStack(ItemRegistry.MERCURY_SHARD.get(), 1));
        });

        // Wait for flux to be transferred from catalyst -> emitter -> capacitor
        // The transfer happens every 20 ticks (1 second)
        helper.succeedWhen(() -> {
            var capacitor = helper.getBlockEntity(CAPACITOR_POS, MercuryCapacitorBlockEntity.class);
            helper.assertTrue(
                    capacitor.mercuryFluxHandler.getAmountAsInt() > 0,
                    "Mercury Capacitor should have received flux from Mercury Flux Emitter"
            );
        });
    }
}
