/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.block.CalcinationOvenBlock;
import com.klikli_dev.theurgy.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.item.DivinationRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Theurgy.MODID);
    public static final RegistryObject<CalcinationOvenBlock> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () -> new CalcinationOvenBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
}
