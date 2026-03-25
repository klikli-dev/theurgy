// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LogisticsGameTests {

    private static final BlockPos NODE_POS = new BlockPos(2, 2, 2);
    private static final BlockPos INSERTER_POS = new BlockPos(3, 2, 2);
    private static final BlockPos EXTRACTOR_POS = new BlockPos(4, 2, 2);

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
}
