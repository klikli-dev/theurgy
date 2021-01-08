/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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

package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.tile.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileRegistry {
    //region Fields
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(
            ForgeRegistries.TILE_ENTITIES, Theurgy.MODID);

    public static final RegistryObject<TileEntityType<CrucibleTileEntity>> CRUCIBLE = TILES.register(
            "crucible", () -> TileEntityType.Builder.create(CrucibleTileEntity::new,
                    BlockRegistry.CRUCIBLE.get()).build(null));

    public static final RegistryObject<TileEntityType<EssentiaEmitterTileEntity>> ESSENTIA_EMITTER = TILES.register(
            "essentia_emitter", () -> TileEntityType.Builder.create(EssentiaEmitterTileEntity::new,
                    BlockRegistry.ESSENTIA_EMITTER.get()).build(null));

    public static final RegistryObject<TileEntityType<EssentiaReceiverTileEntity>> ESSENTIA_RECEIVER = TILES.register(
            "essentia_receiver", () -> TileEntityType.Builder.create(EssentiaReceiverTileEntity::new,
                    BlockRegistry.ESSENTIA_RECEIVER.get()).build(null));

    public static final RegistryObject<TileEntityType<CreativeEssentiaSourceTileEntity>> CREATIVE_ESSENTIA_SOURCE = TILES.register(
            "creaitve_essentia_source", () -> TileEntityType.Builder.create(CreativeEssentiaSourceTileEntity::new,
                    BlockRegistry.CREATIVE_ESSENTIA_SOURCE.get()).build(null));

    public static final RegistryObject<TileEntityType<AetherEmitterTile>> AETHER_EMITTER = TILES.register(
            "aether_emitter", () -> TileEntityType.Builder.create(AetherEmitterTile::new,
                    BlockRegistry.AETHER_EMITTER.get()).build(null));

    public static final RegistryObject<TileEntityType<AetherReceiverTile>> AETHER_RECEIVER = TILES.register(
            "aether_receiver", () -> TileEntityType.Builder.create(AetherReceiverTile::new,
                    BlockRegistry.AETHER_RECEIVER.get()).build(null));

    public static final RegistryObject<TileEntityType<CreativeAetherSourceTile>> CREATIVE_AETHER_SOURCE = TILES.register(
            "creaitve_aether_source", () -> TileEntityType.Builder.create(CreativeAetherSourceTile::new,
                    BlockRegistry.CREATIVE_AETHER_SOURCE.get()).build(null));
    //endregion Fields
}
