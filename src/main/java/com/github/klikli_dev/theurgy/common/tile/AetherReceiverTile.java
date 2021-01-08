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

import com.github.klikli_dev.theurgy.common.capability.DefaultAetherCapability;
import com.github.klikli_dev.theurgy.common.capability.IAetherCapability;
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaCache;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AetherReceiverTile extends NetworkedTileEntity implements ITickableTileEntity, IAetherReceiver {

    //region Fields
    public static final int RECEIVER_AETHER_CAPACITY = 500;
    public static final int PUSH_RATE = 50;

    public final LazyOptional<IAetherCapability> aetherCapabilityLazyOptional;
    public IAetherCapability aetherCapability;
    //endregion Fields

    //region Initialization
    public AetherReceiverTile() {
        super(TileRegistry.AETHER_RECEIVER.get());

        this.aetherCapability = new DefaultAetherCapability(RECEIVER_AETHER_CAPACITY){
            @Override
            public void onContentsChanged() {
                super.onContentsChanged();
                AetherReceiverTile.this.markNetworkDirty();
            }
        };

        this.aetherCapabilityLazyOptional = LazyOptional.of(() -> this.aetherCapability);
    }
    //endregion Initialization

    //region Overrides
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == CapabilityRegistry.AETHER) {
            return this.aetherCapabilityLazyOptional.cast();
        }
        return super.getCapability(cap, direction);
    }


    @Override
    public void readNetwork(CompoundNBT compound) {
        super.readNetwork(compound);
        this.aetherCapability.deserializeNBT(compound.getCompound("aether"));
    }

    @Override
    public CompoundNBT writeNetwork(CompoundNBT compound) {
        compound.put("aether", this.aetherCapability.serializeNBT());
        return super.writeNetwork(compound);
    }

    @Override
    public void tick() {
        if (!this.world.isRemote && this.world.getGameTime() % 10 == 0) {
            BlockState state = this.world.getBlockState(this.pos);
            Direction facing = state.get(BlockStateProperties.FACING);
            TileEntity attachedTile = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));
            if(attachedTile != null){
                attachedTile.getCapability(CapabilityRegistry.AETHER, facing).ifPresent(attachedCap -> {
                    if(this.aetherCapability.canExtract() && attachedCap.canReceive()){
                        int available = this.aetherCapability.extractEnergy(PUSH_RATE, true);
                        int received = attachedCap.receiveEnergy(available, false);
                        //now extract for real
                        this.aetherCapability.extractEnergy(received, false);
                    }
                });
            }
        }
    }
    //endregion Overrides
}
