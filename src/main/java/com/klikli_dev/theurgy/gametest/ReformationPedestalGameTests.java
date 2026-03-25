// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationResultPedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationSourcePedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationTargetPedestalBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ReformationPedestalGameTests {

    private static final BlockPos SOURCE_POS = new BlockPos(2, 2, 2);
    private static final BlockPos TARGET_POS = new BlockPos(3, 2, 2);
    private static final BlockPos RESULT_POS = new BlockPos(4, 2, 2);

    // --- Source Pedestal ---

    public static void sourcePlacementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(SOURCE_POS, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get());
        helper.assertBlockPresent(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get(), SOURCE_POS);
        helper.succeed();
    }

    public static void sourceInsertItem(GameTestHelper helper) {
        helper.setBlock(SOURCE_POS, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(SOURCE_POS, ReformationSourcePedestalBlockEntity.class);
            var remainder = blockEntity.inputInventory.insertItem(0, new ItemStack(SulfurRegistry.BONE.get(), 1), false);
            helper.assertTrue(remainder.isEmpty(), "Sulfur should be accepted");
            helper.assertTrue(!blockEntity.inputInventory.getStackInSlot(0).isEmpty(), "Inventory should contain item");
            helper.succeed();
        });
    }

    public static void sourceExtractItem(GameTestHelper helper) {
        helper.setBlock(SOURCE_POS, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(SOURCE_POS, ReformationSourcePedestalBlockEntity.class);
            blockEntity.inputInventory.setStackInSlot(0, new ItemStack(SulfurRegistry.BONE.get(), 1));
        });

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(SOURCE_POS, ReformationSourcePedestalBlockEntity.class);
            var extracted = blockEntity.inputInventory.extractItem(0, 1, false);
            helper.assertTrue(!extracted.isEmpty(), "Should extract item");
            helper.assertTrue(blockEntity.inputInventory.getStackInSlot(0).isEmpty(), "Inventory should be empty");
            helper.succeed();
        });
    }

    // --- Target Pedestal ---

    public static void targetPlacementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(TARGET_POS, BlockRegistry.REFORMATION_TARGET_PEDESTAL.get());
        helper.assertBlockPresent(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get(), TARGET_POS);
        helper.succeed();
    }

    public static void targetInsertItem(GameTestHelper helper) {
        helper.setBlock(TARGET_POS, BlockRegistry.REFORMATION_TARGET_PEDESTAL.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(TARGET_POS, ReformationTargetPedestalBlockEntity.class);
            var remainder = blockEntity.inputInventory.insertItem(0, new ItemStack(SulfurRegistry.BONE.get(), 1), false);
            helper.assertTrue(remainder.isEmpty(), "Item should be accepted");
            helper.succeed();
        });
    }

    // --- Result Pedestal ---

    public static void resultPlacementAndDefaultState(GameTestHelper helper) {
        helper.setBlock(RESULT_POS, BlockRegistry.REFORMATION_RESULT_PEDESTAL.get());
        helper.assertBlockPresent(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get(), RESULT_POS);
        helper.succeed();
    }

    public static void resultExtractItem(GameTestHelper helper) {
        helper.setBlock(RESULT_POS, BlockRegistry.REFORMATION_RESULT_PEDESTAL.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(RESULT_POS, ReformationResultPedestalBlockEntity.class);
            blockEntity.outputInventory.setStackInSlot(0, new ItemStack(Items.COBBLESTONE, 1));
        });

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(RESULT_POS, ReformationResultPedestalBlockEntity.class);
            var extracted = blockEntity.outputInventory.extractItem(0, 1, false);
            helper.assertTrue(!extracted.isEmpty(), "Should extract from result pedestal");
            helper.succeed();
        });
    }
}
