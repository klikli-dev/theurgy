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

package com.github.klikli_dev.theurgy.common.tile;

import com.github.klikli_dev.theurgy.common.capability.IEssentiaCapability;
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaCache;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EssentiaReceiverTileEntity extends NetworkedTileEntity implements ITickableTileEntity, IEssentiaReceiver {

    //region Fields
    public static final int EMITTER_RECEIVER_CAPACITY = 500;
    public static final int PUSH_RATE = 50;

    public final LazyOptional<IEssentiaCapability> essentiaCapabilityLazyOptional;
    public IEssentiaCapability essentiaCapability;
    //endregion Fields

    //region Initialization
    public EssentiaReceiverTileEntity() {
        super(TileRegistry.ESSENTIA_RECEIVER.get());

        this.essentiaCapability = new EssentiaCache(EMITTER_RECEIVER_CAPACITY) {
            //region Overrides
            @Override
            public void onContentsChanged() {
                super.onContentsChanged();
                EssentiaReceiverTileEntity.this.markNetworkDirty();
            }
            //endregion Overrides
        };
        this.essentiaCapabilityLazyOptional = LazyOptional.of(() -> this.essentiaCapability);
    }
    //endregion Initialization

    //region Overrides
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == CapabilityRegistry.ESSENTIA) {
            return this.essentiaCapabilityLazyOptional.cast();
        }
        return super.getCapability(cap, direction);
    }


    @Override
    public void readNetwork(CompoundNBT compound) {
        super.readNetwork(compound);
        this.essentiaCapability.deserializeNBT(compound.getCompound("essentia"));
    }

    @Override
    public CompoundNBT writeNetwork(CompoundNBT compound) {
        compound.put("essentia", this.essentiaCapability.serializeNBT());

        return super.writeNetwork(compound);
    }

    @Override
    public boolean hasCapacity(Item essentia) {
        return this.essentiaCapability.hasCapacity(essentia);
    }

    @Override
    public void tick() {
        if (this.world.getGameTime() % 10 == 0) {
            BlockState state = this.world.getBlockState(this.pos);
            Direction facing = state.get(BlockStateProperties.FACING);
            TileEntity attachedTile = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));

            attachedTile.getCapability(CapabilityRegistry.ESSENTIA, facing).ifPresent(attachedCap -> {
                this.essentiaCapability.getEssentia().forEach((essentia, amount) -> {
                    if (amount > 0 && attachedCap.hasCapacity(essentia)) {
                        int added = attachedCap.add(essentia, PUSH_RATE, false);
                        this.essentiaCapability.remove(essentia, added, false);
                    }
                });
            });
        }
    }
    //endregion Overrides
}
