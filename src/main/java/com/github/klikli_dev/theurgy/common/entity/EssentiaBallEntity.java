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

package com.github.klikli_dev.theurgy.common.entity;

import com.github.klikli_dev.theurgy.common.theurgy.EssentiaType;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.github.klikli_dev.theurgy.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EssentiaBallEntity extends GlowingBallEntity {

    //region Fields
    public EssentiaType essentiaType;
    public int value;
    //endregion Fields

    //region Initialization
    public EssentiaBallEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EssentiaBallEntity(World worldIn, double x, double y, double z,
                              EssentiaType essentiaType, int value,
                              BlockPos target, double motionX, double motionY, double motionZ) {
        super(EntityRegistry.ESSENTIA_BALL_TYPE.get(), worldIn, x, y, z, target, motionX, motionY, motionZ,
                essentiaType.getEssentiaBallColor());
        this.essentiaType = essentiaType;
        this.value = value;
    }
    //endregion Initialization

    //region Overrides
    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.essentiaType = EssentiaType.values()[compound.getByte("essentiaType")];
        this.color = this.essentiaType.getEssentiaBallColor();
        this.value = compound.getShort("value");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("essentiaType", (byte) this.essentiaType.ordinal());
        compound.putShort("value", (short) this.value);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        super.writeSpawnData(buffer);
        buffer.writeEnumValue(this.essentiaType);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        super.readSpawnData(additionalData);
        this.essentiaType = additionalData.readEnumValue(EssentiaType.class);
        this.color = this.essentiaType.getEssentiaBallColor();
    }

    @Override
    protected void onTargetReached() {
        if (!this.world.isRemote) {
            TileEntity tile = this.world.getTileEntity(this.getPosition());
            if(tile != null){
                tile.getCapability(CapabilityRegistry.ESSENTIA).ifPresent(cap -> {
                    cap.receiveEssentia(this.essentiaType.getEssentiaItem().get(), this.value, false);
                    tile.markDirty();
                });
            }
        }
        super.onTargetReached();
    }

    //endregion Overrides
}
