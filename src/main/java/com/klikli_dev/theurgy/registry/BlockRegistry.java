/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.block.calcinationoven.CalcinationOvenBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Theurgy.MODID);
    public static final RegistryObject<CalcinationOvenBlock> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () -> new CalcinationOvenBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(2.0f)
                    .lightLevel((state) -> state.getValue(BlockStateProperties.LIT) ? 14 : 0)));


    //TODO: Switch block type
    public static final RegistryObject<CalcinationOvenBlock> PYROMANTIC_BRAZIER =
            BLOCKS.register("pyromantic_brazier", () -> new CalcinationOvenBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(2.0f)
                    .lightLevel((state) -> state.getValue(BlockStateProperties.LIT) ? 14 : 0)));
}
