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

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class EssentiaDimension extends WorldSavedData {

    //region Fields
    public RegistryKey<World> dimensionKey;
    public Map<ChunkPos, EssentiaChunk> essentiaChunks;
    //endregion Fields

    //region Initialization
    public EssentiaDimension(String name, RegistryKey<World> dimensionKey) {
        super(name);
        this.dimensionKey = dimensionKey;
        this.essentiaChunks = new HashMap<>();
    }
    //endregion Initialization

    //region Overrides
    @Override
    public void read(CompoundNBT nbt) {
        this.dimensionKey =
                RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(nbt.getString("dimensionKey")));
        if (nbt.contains("essentiaChunks")) {
            ListNBT essentiaList = nbt.getList("essentiaChunks", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < essentiaList.size(); i++) {
                CompoundNBT entry = essentiaList.getCompound(i);
                EssentiaChunk chunk = new EssentiaChunk(this);
                chunk.deserializeNBT(entry);
                this.essentiaChunks.put(chunk.chunkPos, chunk);
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("dimensionKey", this.dimensionKey.getLocation().toString());
        ListNBT chunks = new ListNBT();
        compound.put("essentiaChunks", chunks);
        this.essentiaChunks.forEach((chunkPos, chunk) -> {
            chunks.add(chunk.serializeNBT());
        });
        return compound;
    }
    //endregion Overrides
}
