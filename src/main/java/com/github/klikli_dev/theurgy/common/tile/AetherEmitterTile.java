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

import com.github.klikli_dev.theurgy.client.particle.SparkleParticleData;
import com.github.klikli_dev.theurgy.common.capability.DefaultAetherCapability;
import com.github.klikli_dev.theurgy.common.capability.IAetherCapability;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class AetherEmitterTile extends NetworkedTileEntity implements ITickableTileEntity, IAetherEmitter {

    //region Fields
    public static final int EMITTER_AETHER_CAPACITY = 500;
    public static final int BURST_RATE = 50;
    public static final int PULL_RATE = 50;
    public static final int BURST_TICKS = 40;
    public final LazyOptional<IAetherCapability> aetherCapabilityLazyOptional;
    public IAetherCapability aetherCapability;
    public int burstOffset;
    public boolean isEnabled;
    protected Optional<BlockPos> target;

    //endregion Fields
    //region Initialization
    public AetherEmitterTile() {
        super(TileRegistry.AETHER_EMITTER.get());
        this.target = Optional.empty();

        this.aetherCapability = new DefaultAetherCapability(EMITTER_AETHER_CAPACITY) {
            //region Overrides
            @Override
            public void onContentsChanged() {
                super.onContentsChanged();
                AetherEmitterTile.this.markNetworkDirty();
            }
            //endregion Overrides
        };

        this.aetherCapabilityLazyOptional = LazyOptional.of(() -> this.aetherCapability);
        this.isEnabled = true;
    }
    //endregion Initialization

    //region Getter / Setter
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        this.markNetworkDirty();
    }

    public Optional<BlockPos> getTarget() {
        return this.target;
    }

    public void setTarget(Optional<BlockPos> target) {
        this.target = target;
        this.markNetworkDirty();
    }
    //endregion Getter / Setter

    //region Overrides
    @Override
    public void onLoad() {
        this.burstOffset = this.world.rand.nextInt(40);
    }

    @Override
    public void tick() {
        if (!this.world.isRemote && this.isEnabled) {
            //on slow tick, pull aether from attached tile
            if (this.world.getGameTime() % 10 == 0) {
                BlockState state = this.world.getBlockState(this.pos);
                Direction facing = state.get(BlockStateProperties.FACING);
                TileEntity attachedTile = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));
                if (attachedTile != null) {
                    attachedTile.getCapability(CapabilityRegistry.AETHER, facing).ifPresent(attachedCap -> {
                        if (attachedCap.canExtract() && this.aetherCapability.canReceive()) {
                            int available = attachedCap.extractEnergy(PULL_RATE, true);
                            int received = this.aetherCapability.receiveEnergy(available, false);
                            //now extract for real
                            attachedCap.extractEnergy(received, false);
                        }
                    });
                }
            }
        }
        if (this.isEnabled) {
            //on even slower tick, send aether to target
            this.target.ifPresent(target -> {
                if ((this.world.getGameTime() + this.burstOffset) % BURST_TICKS == 0) {
                    TileEntity targetTile = this.world.getTileEntity(target);
                    if (targetTile != null) {
                        if (!world.isRemote) {
                            //on server perform transfer
                            targetTile.getCapability(CapabilityRegistry.AETHER).ifPresent(targetCap -> {
                                if (this.aetherCapability.canExtract() && targetCap.canReceive()) {
                                    int available = this.aetherCapability.extractEnergy(BURST_RATE, true);
                                    int received = targetCap.receiveEnergy(available, false);
                                    //now extract for real
                                    this.aetherCapability.extractEnergy(received, false);
                                }
                            });
                        }
                        else {
                            //on client show particles
                            //TODO: decide on particle count for sparkle
                            for (int i = 0; i < 5; i++) {
                                SparkleParticleData data = new SparkleParticleData(
                                        0.6f + this.world.rand.nextFloat() * 0.3f,
                                        0.2f,
                                        0.6f + this.world.rand.nextFloat() * 0.3f,
                                        target.getX() + 0.5f, target.getY()+ 0.5f, target.getZ()+ 0.5f);

                                this.world
                                        .addParticle(data, this.pos.getX() + this.world.rand.nextFloat(),
                                                this.pos.getY() + this.world.rand.nextFloat(),
                                                this.pos.getZ() + this.world.rand.nextFloat(), 0, 0, 0);
                            }
                        }
                    }
                }
            });
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == CapabilityRegistry.AETHER) {
            return this.aetherCapabilityLazyOptional.cast();
        }
        return super.getCapability(cap, direction);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.isEnabled = compound.getBoolean("isEnabled");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("isEnabled", this.isEnabled);
        return super.write(compound);
    }

    @Override
    public void readNetwork(CompoundNBT compound) {
        super.readNetwork(compound);
        if (compound.contains("target")) {
            this.target = Optional.of(BlockPos.fromLong(compound.getLong("target")));
        }
        this.aetherCapability.deserializeNBT(compound.getCompound("aether"));
    }

    @Override
    public CompoundNBT writeNetwork(CompoundNBT compound) {
        this.target.ifPresent(target -> compound.putLong("target", target.toLong()));
        compound.put("aether", this.aetherCapability.serializeNBT());
        return super.writeNetwork(compound);
    }
    //endregion Overrides
}
