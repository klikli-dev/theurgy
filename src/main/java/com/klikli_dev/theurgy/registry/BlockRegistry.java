/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.block.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.content.block.distiller.DistillerBlock;
import com.klikli_dev.theurgy.content.block.liquefactioncauldron.LiquefactionCauldronBlock;
import com.klikli_dev.theurgy.content.block.pyromanticbrazier.PyromanticBrazierBlock;
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
                    .strength(1.0f)));

    public static final RegistryObject<PyromanticBrazierBlock> PYROMANTIC_BRAZIER =
            BLOCKS.register("pyromantic_brazier", () -> new PyromanticBrazierBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f)
                    .lightLevel((state) -> state.getValue(BlockStateProperties.LIT) ? 14 : 0)));

    public static final RegistryObject<LiquefactionCauldronBlock> LIQUEFACTION_CAULDRON =
            BLOCKS.register("liquefaction_cauldron", () -> new LiquefactionCauldronBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f)));

    public static final RegistryObject<DistillerBlock> DISTILLER =
            BLOCKS.register("distiller", () -> new DistillerBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .strength(1.0f)));

}
