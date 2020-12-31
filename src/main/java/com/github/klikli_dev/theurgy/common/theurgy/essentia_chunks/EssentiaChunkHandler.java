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

package com.github.klikli_dev.theurgy.common.theurgy.essentia_chunks;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaCache;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EssentiaChunkHandler {

    //region Fields
    public static final ResourceLocation SAVE_DATA_NAME = new ResourceLocation(Theurgy.MODID, "essentia_chunks");

    private static Map<RegistryKey<World>, EssentiaDimension> essentiaDimensions =
            new HashMap<>();
    //endregion Fields

    //region Static Methods

    /**
     * Gets the essentia dimension for the given dimension.
     * @throws IllegalStateException if the dimension was never loaded and no essentia dimension exists for it therefore.
     * @param dimension the dimension to look up.
     * @return the essentia dimension for the given dimension.
     */
    public static EssentiaDimension getEssentiaDimension(RegistryKey<World> dimension){
        if(!essentiaDimensions.containsKey(dimension))
            throw new IllegalStateException(String.format("Requesting Essentia chunk for dimension that was never loaded: %s", dimension));

        return  essentiaDimensions.get(dimension);
    }

    /**
     * Gets the essentia chunk in the given dimension at the given position.
     * @throws IllegalStateException if the dimension was never loaded and no essentia dimension exists for it therefore.
     * @param dimension the dimension to look up.
     * @param chunkPos the chunk pos to look up.
     * @return the essentia chunk, or null if no chunk exists.
     */
    public static EssentiaChunk getEssentiaChunk(RegistryKey<World> dimension, ChunkPos chunkPos){
        return  getEssentiaDimension(dimension).essentiaChunks.get(chunkPos);
    }

    /**
     * Gets the essentia chunk in the given dimension at the given position or creates a new one if none exists.
     * @throws IllegalStateException if the dimension was never loaded and no essentia dimension exists for it therefore.
     * @param dimension the dimension to look up.
     * @param chunkPos the chunk pos to look up.
     * @return the essentia chunk.
     */
    public static EssentiaChunk getOrCreateEssentiaChunk(RegistryKey<World> dimension, ChunkPos chunkPos){
        EssentiaChunk chunk = getEssentiaChunk(dimension, chunkPos);
        if(chunk == null)
            chunk = addEssentiaChunk(dimension, chunkPos);
        return chunk;
    }

    /**
     * Adds a new essentia chunk in the given dimension at the given position.
     * @throws IllegalStateException if the dimension was never loaded and no essentia dimension exists for it therefore.
     * @param dimension the dimension to look up.
     * @param chunkPos the chunk pos to insert at.
     * @return the new essentia chunk.
     */
    public static EssentiaChunk addEssentiaChunk(RegistryKey<World> dimension, ChunkPos chunkPos){
        EssentiaDimension essentiaDimension = getEssentiaDimension(dimension);
        EssentiaChunk chunk = new EssentiaChunk(essentiaDimension, chunkPos);
        return essentiaDimension.essentiaChunks.put(chunkPos, chunk);
    }

    /**
     * Gets the essentia cache for the chunk in the given dimension at the given position.
     * @throws IllegalStateException if the dimension was never loaded and no essentia dimension exists for it therefore.
     * @param dimension the dimension to look up.
     * @param chunkPos the chunk pos to look up.
     * @return the essentia cache at the given position.
     */
    public static EssentiaCache getEssentiaCache(RegistryKey<World> dimension, ChunkPos chunkPos){
        return getOrCreateEssentiaChunk(dimension, chunkPos).essentia;
    }

    /**
     * Marks the essentia chunk in the given dimension at the given position for saving.
     * @throws IllegalStateException if the dimension was never loaded and no essentia dimension exists for it therefore.
     * @param dimension the dimension to look up.
     * @param chunkPos the chunk pos to look up.
     */
    public static void markDirty(RegistryKey<World> dimension, ChunkPos chunkPos){
        //As chunks currently just forward mark dirty to the dimension anyway, we can just mark the essentia dimension dirty directly.
        markDirty(dimension);
    }

    /**
     * Marks the essentia dimension for the given dimension for saving.
     * @throws IllegalStateException if the dimension was never loaded and no essentia dimension exists for it therefore.
     * @param dimension the dimension to look up.
     */
    public static void markDirty(RegistryKey<World> dimension){
        EssentiaDimension essentiaDimension = getEssentiaDimension(dimension);
        essentiaDimension.markDirty();
    }

    /**
     * When a dimension is loaded, attach world save data to it
     */
    public static void onDimensionLoaded(ServerWorld world) {
        //get save data manager for this dimension
        DimensionSavedDataManager storage = world.getSavedData();
        //provide a supplier to create a new empty world save data
        Supplier<EssentiaDimension> supplier = () -> new EssentiaDimension(SAVE_DATA_NAME.toString(), world.getDimensionKey());
        //get the world save data for this dimension
        EssentiaDimension worldSaveData = storage.getOrCreate(supplier, SAVE_DATA_NAME.toString());

        //store the save data in the lookup table
        essentiaDimensions.put(world.getDimensionKey(),worldSaveData);
    }
    //endregion Static Methods
}
