/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.block.calcinationoven.CalcinationOvenBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Theurgy.MODID);
    public static final RegistryObject<BlockEntityType<CalcinationOvenBlockEntity>> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () ->
                    BlockEntityType.Builder.of(CalcinationOvenBlockEntity::new, BlockRegistry.CALCINATION_OVEN.get()).build(null));
}
