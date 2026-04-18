// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationResultPedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationSourcePedestalBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.ReformationTargetPedestalBlockEntity;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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
            try (var tx = Transaction.openRoot()) { ItemUtil.insertItemReturnRemaining(blockEntity.inputInventory, 0, new ItemStack(SulfurRegistry.BONE.get(), 1), false, tx); tx.commit(); }
            helper.assertTrue(!ItemUtil.getStack(blockEntity.inputInventory, 0).isEmpty(), "Inventory should contain item");
            helper.succeed();
        });
    }

    public static void sourceRejectsInvalidHeldItem(GameTestHelper helper) {
        helper.setBlock(SOURCE_POS, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(SOURCE_POS, ReformationSourcePedestalBlockEntity.class);
            var player = helper.makeMockPlayer(GameType.SURVIVAL);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.SULFURIC_FLUX_EMITTER.get(), 1));

            helper.useBlock(SOURCE_POS, player, centeredHitResult(helper, SOURCE_POS));
            helper.assertTrue(ItemUtil.getStack(blockEntity.inputInventory, 0).isEmpty(), "Source pedestal should reject invalid held items");
            helper.assertTrue(!player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), "Rejected item should remain in hand");
            helper.succeed();
        });
    }

    public static void sourceExtractItem(GameTestHelper helper) {
        helper.setBlock(SOURCE_POS, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get());

        helper.runAfterDelay(1, () -> {
            var blockEntity = helper.getBlockEntity(SOURCE_POS, ReformationSourcePedestalBlockEntity.class);
            blockEntity.inputInventory.set(0, ItemResource.of(new ItemStack(SulfurRegistry.BONE.get(), 1)), new ItemStack(SulfurRegistry.BONE.get(), 1).getCount());
        });

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(SOURCE_POS, ReformationSourcePedestalBlockEntity.class);
            ItemStack extracted;
            try (var tx = Transaction.openRoot()) {
                var resource = blockEntity.inputInventory.getResource(0);
                extracted = resource.toStack(blockEntity.inputInventory.extract(0, resource, 1, tx));
                tx.commit();
            }
            helper.assertTrue(!extracted.isEmpty(), "Should extract item");
            helper.assertTrue(ItemUtil.getStack(blockEntity.inputInventory, 0).isEmpty(), "Inventory should be empty");
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
            try (var tx = Transaction.openRoot()) { ItemUtil.insertItemReturnRemaining(blockEntity.inputInventory, 0, new ItemStack(SulfurRegistry.BONE.get(), 1), false, tx); tx.commit(); }
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
            blockEntity.outputInventory.set(0, ItemResource.of(new ItemStack(Items.COBBLESTONE, 1)), new ItemStack(Items.COBBLESTONE, 1).getCount());
        });

        helper.runAfterDelay(2, () -> {
            var blockEntity = helper.getBlockEntity(RESULT_POS, ReformationResultPedestalBlockEntity.class);
            ItemStack extracted;
            try (var tx = Transaction.openRoot()) {
                var resource = blockEntity.outputInventory.getResource(0);
                extracted = resource.toStack(blockEntity.outputInventory.extract(0, resource, 1, tx));
                tx.commit();
            }
            helper.assertTrue(!extracted.isEmpty(), "Should extract from result pedestal");
            helper.succeed();
        });
    }

    private static BlockHitResult centeredHitResult(GameTestHelper helper, BlockPos pos) {
        return new BlockHitResult(Vec3.atCenterOf(helper.absolutePos(pos)), Direction.UP, helper.absolutePos(pos), false);
    }

}
