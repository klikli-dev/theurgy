/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.block.calcinationoven.CalcinationOvenBlockEntity;
import com.klikli_dev.theurgy.content.block.distiller.DistillerBlockEntity;
import com.klikli_dev.theurgy.content.block.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.block.pyromanticbrazier.PyromanticBrazierBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Theurgy.MODID);

    public static final RegistryObject<BlockEntityType<CalcinationOvenBlockEntity>> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () ->
                    BlockEntityType.Builder.of(CalcinationOvenBlockEntity::new, BlockRegistry.CALCINATION_OVEN.get()).build(null));

    public static final RegistryObject<BlockEntityType<PyromanticBrazierBlockEntity>> PYROMANTIC_BRAZIER =
            BLOCKS.register("pyromantic_brazier", () ->
                    BlockEntityType.Builder.of(PyromanticBrazierBlockEntity::new, BlockRegistry.PYROMANTIC_BRAZIER.get()).build(null));

    public static final RegistryObject<BlockEntityType<LiquefactionCauldronBlockEntity>> LIQUEFACTION_CAULDRON =
            BLOCKS.register("liquefaction_cauldron", () ->
                    BlockEntityType.Builder.of(LiquefactionCauldronBlockEntity::new, BlockRegistry.LIQUEFACTION_CAULDRON.get()).build(null));

    public static final RegistryObject<BlockEntityType<DistillerBlockEntity>> DISTILLER =
            BLOCKS.register("distiller", () ->
                    BlockEntityType.Builder.of(DistillerBlockEntity::new, BlockRegistry.DISTILLER.get()).build(null));
}
