// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.extractor.LogisticsFluidExtractorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsfluidconnector.inserter.LogisticsFluidInserterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor.LogisticsItemExtractorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.inserter.LogisticsItemInserterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.SalAmmoniacTankBlockEntity;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class LogisticsGameTests {

    private static final BlockPos NODE_POS = new BlockPos(2, 2, 2);
    private static final BlockPos INSERTER_POS = new BlockPos(3, 2, 2);
    private static final BlockPos EXTRACTOR_POS = new BlockPos(4, 2, 2);
    private static final BlockPos FLUID_SOURCE_POS = new BlockPos(1, 2, 2);
    private static final BlockPos FLUID_EXTRACTOR_POS = new BlockPos(2, 2, 2);
    private static final BlockPos FLUID_INSERTER_POS = new BlockPos(4, 2, 2);
    private static final BlockPos FLUID_TANK_POS = new BlockPos(5, 2, 2);
    private static final BlockPos ITEM_INSERTER_TARGET_POS = new BlockPos(2, 2, 2);
    private static final BlockPos ITEM_INSERTER_POS = new BlockPos(3, 2, 2);
    private static final BlockPos ITEM_EXTRACTOR_POS = new BlockPos(5, 2, 2);
    private static final BlockPos ITEM_EXTRACTOR_TARGET_POS = new BlockPos(6, 2, 2);

    // --- Connection Node ---

    public static void connectionNodePlacement(GameTestHelper helper) {
        helper.setBlock(NODE_POS, BlockRegistry.LOGISTICS_CONNECTION_NODE.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH));

        helper.assertBlockPresent(BlockRegistry.LOGISTICS_CONNECTION_NODE.get(), NODE_POS);
        helper.assertBlockProperty(NODE_POS, BlockStateProperties.FACING, Direction.NORTH);
        helper.succeed();
    }

    // --- Item Inserter ---

    public static void itemInserterPlacement(GameTestHelper helper) {
        helper.setBlock(INSERTER_POS, BlockRegistry.LOGISTICS_ITEM_INSERTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH));

        helper.assertBlockPresent(BlockRegistry.LOGISTICS_ITEM_INSERTER.get(), INSERTER_POS);
        helper.assertBlockProperty(INSERTER_POS, BlockStateProperties.FACING, Direction.NORTH);
        helper.succeed();
    }

    // --- Item Extractor ---

    public static void itemExtractorPlacement(GameTestHelper helper) {
        helper.setBlock(EXTRACTOR_POS, BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH));

        helper.assertBlockPresent(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), EXTRACTOR_POS);
        helper.assertBlockProperty(EXTRACTOR_POS, BlockStateProperties.FACING, Direction.NORTH);
        helper.succeed();
    }

    public static void itemExtractorFindsInserterTargetRegardlessOfRegistrationOrder(GameTestHelper helper) {
        assertItemExtractorFindsInserterTarget(helper, true);
    }

    public static void itemExtractorFindsInserterTargetRegardlessOfRegistrationOrderReversed(GameTestHelper helper) {
        assertItemExtractorFindsInserterTarget(helper, false);
    }

    public static void fluidExtractorPullsFromWorldSource(GameTestHelper helper) {
        helper.setBlock(FLUID_SOURCE_POS.west(), Blocks.STONE);
        helper.setBlock(FLUID_SOURCE_POS.north(), Blocks.STONE);
        helper.setBlock(FLUID_SOURCE_POS.south(), Blocks.STONE);
        helper.setBlock(FLUID_SOURCE_POS.above(), Blocks.STONE);
        helper.setBlock(FLUID_SOURCE_POS.below(), Blocks.STONE);
        helper.setBlock(FLUID_SOURCE_POS, Blocks.WATER);
        helper.setBlock(FLUID_INSERTER_POS, BlockRegistry.LOGISTICS_FLUID_INSERTER.get()
                .defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));
        helper.setBlock(FLUID_TANK_POS, BlockRegistry.SAL_AMMONIAC_TANK.get());

        helper.runAfterDelay(1, () -> {
            var level = helper.getLevel();
            var player = helper.makeMockPlayer(GameType.SURVIVAL);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.LOGISTICS_FLUID_EXTRACTOR.get()));
            helper.useBlock(
                    FLUID_SOURCE_POS,
                    player,
                    new BlockHitResult(Vec3.atCenterOf(helper.absolutePos(FLUID_SOURCE_POS)), Direction.EAST, helper.absolutePos(FLUID_SOURCE_POS), false)
            );

            var extractorPos = helper.absolutePos(FLUID_EXTRACTOR_POS);
            var inserterPos = helper.absolutePos(FLUID_INSERTER_POS);
            var sourcePos = helper.absolutePos(FLUID_SOURCE_POS);
            var tankPos = helper.absolutePos(FLUID_TANK_POS);

            helper.assertBlockPresent(BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get(), FLUID_EXTRACTOR_POS);
            helper.assertTrue(level.getBlockState(sourcePos).is(Blocks.WATER), "Extractor placement should not replace the world water source");

            var extractor = helper.getBlockEntity(FLUID_EXTRACTOR_POS, LogisticsFluidExtractorBlockEntity.class);
            var inserter = helper.getBlockEntity(FLUID_INSERTER_POS, LogisticsFluidInserterBlockEntity.class);
            var tank = helper.getBlockEntity(FLUID_TANK_POS, SalAmmoniacTankBlockEntity.class);

            extractor.leafNode().rebuildExtractTargets();
            inserter.leafNode().targets().add(tankPos);
            inserter.leafNode().directionOverride(Direction.WEST);

            helper.assertTrue(extractor.leafNode().targets().getFirst().equals(sourcePos), "Extractor should target the adjacent source block");
            helper.assertTrue(inserter.leafNode().targets().getFirst().equals(tankPos), "Inserter should target the adjacent tank");

            Logistics.get().add(GlobalPos.of(level.dimension(), extractorPos), GlobalPos.of(level.dimension(), inserterPos));
            Logistics.get().add(extractor.leafNode());
            Logistics.get().add(inserter.leafNode());

            helper.assertTrue(level.getBlockState(sourcePos).is(Blocks.WATER), "Source block should still be water before transfer");
            helper.assertTrue(level.getFluidState(sourcePos).isSource(), "Source fluid should be a source block before transfer");
            helper.assertTrue(tank.tank.getFluidAmount() == 0, "Tank should start empty");
        });

        helper.succeedWhen(() -> {
            var level = helper.getLevel();
            var sourcePos = helper.absolutePos(FLUID_SOURCE_POS);
            var tank = helper.getBlockEntity(FLUID_TANK_POS, SalAmmoniacTankBlockEntity.class);

            helper.assertTrue(tank.tank.getFluidAmount() > 0, "Tank should receive water from the world source");
            helper.assertTrue(tank.tank.getFluid().getFluid() == Blocks.WATER.defaultBlockState().getFluidState().getType(), "Tank should contain water");
            helper.assertTrue(level.getBlockState(sourcePos).is(Blocks.WATER), "Water source block should remain after extraction");
            helper.assertTrue(level.getFluidState(sourcePos).isSource(), "Water source should remain a source after extraction");
        });
    }

    private static void assertItemExtractorFindsInserterTarget(GameTestHelper helper, boolean extractorFirst) {
        helper.setBlock(ITEM_INSERTER_TARGET_POS, Blocks.CHEST);
        helper.setBlock(ITEM_INSERTER_POS, BlockRegistry.LOGISTICS_ITEM_INSERTER.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.EAST));
        helper.setBlock(ITEM_EXTRACTOR_POS, BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST));
        helper.setBlock(ITEM_EXTRACTOR_TARGET_POS, Blocks.CHEST);

        helper.runAfterDelay(1, () -> {
            var level = helper.getLevel();
            var inserterPos = helper.absolutePos(ITEM_INSERTER_POS);
            var inserterTargetPos = helper.absolutePos(ITEM_INSERTER_TARGET_POS);
            var extractorPos = helper.absolutePos(ITEM_EXTRACTOR_POS);
            var extractorTargetPos = helper.absolutePos(ITEM_EXTRACTOR_TARGET_POS);

            var inserter = helper.getBlockEntity(ITEM_INSERTER_POS, LogisticsItemInserterBlockEntity.class);
            var extractor = helper.getBlockEntity(ITEM_EXTRACTOR_POS, LogisticsItemExtractorBlockEntity.class);

            Logistics.get().remove(inserter.leafNode(), false);
            Logistics.get().remove(extractor.leafNode(), false);

            inserter.leafNode().targets().clear();
            inserter.leafNode().targets().add(inserterTargetPos);
            inserter.leafNode().directionOverride(Direction.EAST);

            extractor.leafNode().targets().clear();
            extractor.leafNode().targets().add(extractorTargetPos);
            extractor.leafNode().directionOverride(Direction.WEST);

            Logistics.get().add(GlobalPos.of(level.dimension(), inserterPos), GlobalPos.of(level.dimension(), extractorPos));

            if (extractorFirst) {
                Logistics.get().add(extractor.leafNode());
                Logistics.get().add(inserter.leafNode());
            } else {
                Logistics.get().add(inserter.leafNode());
                Logistics.get().add(extractor.leafNode());
            }

            helper.assertTrue(
                    extractor.leafNode().insertTargets().stream().anyMatch(target ->
                            target.inserter().globalPos().equals(GlobalPos.of(level.dimension(), inserterPos))
                                    && target.capability().pos().equals(inserterTargetPos)),
                    "Extractor should discover the inserter target regardless of registration order"
            );

            helper.succeed();
        });
    }
}
