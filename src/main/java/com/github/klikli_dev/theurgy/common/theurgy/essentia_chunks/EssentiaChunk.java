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

import com.github.klikli_dev.theurgy.common.capability.IEssentiaCapability;
import com.github.klikli_dev.theurgy.common.capability.DefaultEssentiaCapability;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;

public class EssentiaChunk implements net.minecraftforge.common.util.INBTSerializable<CompoundNBT> {

    //region Fields
    public ChunkPos chunkPos;
    public IEssentiaCapability essentiaCapability;
    public EssentiaDimension essentiaDimension;
    //endregion Fields

    //region Initialization

    public EssentiaChunk(EssentiaDimension essentiaDimension) {
        this.essentiaDimension = essentiaDimension;
        this.chunkPos = new ChunkPos(0, 0);
        this.essentiaCapability = new DefaultEssentiaCapability();
    }

    public EssentiaChunk(EssentiaDimension essentiaDimension, ChunkPos chunkPos) {
        this.essentiaDimension = essentiaDimension;
        this.chunkPos = chunkPos;
        this.essentiaCapability = new DefaultEssentiaCapability();
    }
    //endregion Initialization

    //region Overrides
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putLong("chunkPos", this.chunkPos.asLong());
        compound.put("essentia", this.essentiaCapability.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.chunkPos = new ChunkPos(nbt.getLong("chunkPos"));
        this.essentiaCapability.deserializeNBT(nbt.getCompound("essentia"));
    }
    //endregion Overrides

    //region Methods

    /**
     * Should be called after each essentia operation
     */
    public void markDirty() {
        this.essentiaDimension.markDirty();
    }
    //endregion Methods
}
