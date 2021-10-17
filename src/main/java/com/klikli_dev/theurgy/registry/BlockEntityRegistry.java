/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.blockentity.GraftingHedgeBlockEntity;
import com.klikli_dev.theurgy.blockentity.SteamDistillerBaseBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITIES, Theurgy.MODID);

    public static final RegistryObject<BlockEntityType<GraftingHedgeBlockEntity>> GRAFTING_HEDGE = BLOCK_ENTITIES.register(
            "grafting_hedge", () -> BlockEntityType.Builder.of(GraftingHedgeBlockEntity::new,
                    BlockRegistry.GRAFTING_HEDGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SteamDistillerBaseBlockEntity>> STEAM_DISTILLER_BASE = BLOCK_ENTITIES.register(
            "steam_distiller_base", () -> BlockEntityType.Builder.of(SteamDistillerBaseBlockEntity::new,
                    BlockRegistry.STEAM_DISTILLER_BASE.get()).build(null));

}
