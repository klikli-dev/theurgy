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
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class EssentiaChunkHandler {

    //region Fields
    private static final ConcurrentHashMap<RegistryKey<World>, EssentiaChunkDimension> ESSENTIA_DIMENSIONS =
            new ConcurrentHashMap<>();
    public static ResourceLocation SAVE_DATA_NAME = new ResourceLocation(Theurgy.MODID, "essentia_chunks");
    //endregion Fields

    //region Static Methods

    /**
     * When a dimension is loaded, attach world save data to it
     */
    public static void onDimensionLoaded(ServerWorld world) {
        //get save data manager for this dimension
        DimensionSavedDataManager storage = world.getSavedData();
        //provide a supplier to create a new empty world save data
        Supplier<EssentiaChunkWorldSaveData> supplier = () -> new EssentiaChunkWorldSaveData(SAVE_DATA_NAME.toString());
        //get the world save data for this dimension
        EssentiaChunkWorldSaveData worldSaveData = storage.getOrCreate(supplier, SAVE_DATA_NAME.toString());

        //create and store an EssentiaChunkDimension with the save data
        ESSENTIA_DIMENSIONS
                .put(world.getDimensionKey(), new EssentiaChunkDimension(world.getDimensionKey(), worldSaveData));
    }
    //endregion Static Methods
}
