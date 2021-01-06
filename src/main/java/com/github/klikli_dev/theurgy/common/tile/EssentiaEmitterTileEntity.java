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
import com.github.klikli_dev.theurgy.common.entity.EssentiaBallEntity;
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaCache;
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaType;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class EssentiaEmitterTileEntity extends NetworkedTileEntity implements ITickableTileEntity, IEssentiaEmitter {

    //region Fields
    public static final int EMITTER_ESSENTIA_CAPACITY = 500;
    public static final int BURST_RATE = 50;
    public static final int PULL_RATE = 50;
    public static final int BURST_TICKS = 40;
    public static final Vector3d BURST_DOWN = new Vector3d(0, -0.5, 0);
    public static final Vector3d BURST_UP = new Vector3d(0, 0.5, 0);
    public static final Vector3d BURST_NORTH = new Vector3d(0, -0.01, -0.5);
    public static final Vector3d BURST_SOUTH = new Vector3d(0, -0.01, 0.5);
    public static final Vector3d BURST_WEST = new Vector3d(-0.5, -0.01, 0);
    public static final Vector3d BURST_EAST = new Vector3d(0.5, -0.01, 0);
    public final LazyOptional<IEssentiaCapability> essentiaCapabilityLazyOptional;
    public IEssentiaCapability essentiaCapability;
    public int burstOffset;
    public EssentiaType burstType;
    public boolean isEnabled;
    protected Optional<BlockPos> target;
    //endregion Fields
    //region Initialization
    public EssentiaEmitterTileEntity() {
        super(TileRegistry.ESSENTIA_EMITTER.get());
        this.target = Optional.empty();
        this.essentiaCapability = new EssentiaCache(EMITTER_ESSENTIA_CAPACITY) {
            //region Overrides
            @Override
            public void onContentsChanged() {
                super.onContentsChanged();
                EssentiaEmitterTileEntity.this.markDirty();
            }
            //endregion Overrides
        };
        this.essentiaCapabilityLazyOptional = LazyOptional.of(() -> this.essentiaCapability);
        this.burstType = EssentiaType.AER;
        this.isEnabled = true;
    }
    //endregion Initialization

    //region Getter / Setter
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        this.markDirty();
    }

    public Optional<BlockPos> getTarget() {
        return this.target;
    }

    public void setTarget(Optional<BlockPos> target) {
        this.target = target;
        this.markDirty();
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
            //on slow tick, pull essentia from attached tile
            if (this.world.getGameTime() % 10 == 0) {
                BlockState state = this.world.getBlockState(this.pos);
                Direction facing = state.get(BlockStateProperties.FACING);
                TileEntity attachedTile = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));
                if (attachedTile != null) {
                    attachedTile.getCapability(CapabilityRegistry.ESSENTIA, facing).ifPresent(attachedCap -> {
                        attachedCap.getEssentia().forEach((essentia, amount) -> {
                            if (amount > 0 &&
                                this.essentiaCapability.get(essentia) < this.essentiaCapability.getCapacity()) {
                                int removed = attachedCap.remove(essentia, PULL_RATE, false);
                                this.essentiaCapability.add(essentia, removed, false);
                            }
                        });
                    });
                }
            }
            //on even slower tick, send essentia to target
            this.target.ifPresent(target -> {
                if ((this.world.getGameTime() + this.burstOffset) % BURST_TICKS == 0) {
                    //get the current burst type
                    Item burstEssentia = this.burstType.getEssentiaItem().get();
                    //if current burst type has no essentia, skip
                    if (this.essentiaCapability.get(burstEssentia) > 0) {
                        //find the target tile
                        TileEntity targetTile = this.world.getTileEntity(target);
                        //if target tile has space, send packet
                        if (targetTile instanceof IEssentiaReceiver && !((IEssentiaReceiver) targetTile).isFull()) {

                            //take essentia to send
                            int essentiaAmount = this.essentiaCapability.remove(burstEssentia, BURST_RATE, false);

                            //get the motion vector
                            Vector3d motion = this.getBurstMotion(
                                    this.world.getBlockState(this.pos).get(BlockStateProperties.FACING));
                            EssentiaBallEntity essentiaBallEntity = new EssentiaBallEntity(this.world,
                                    this.pos.getX() + 0.5, this.pos.getY() + 0.75, this.pos.getZ() + 0.5,
                                    this.burstType, essentiaAmount,
                                    target, motion.x, motion.y, motion.z);
                            //spawn entity
                            this.world.addEntity(essentiaBallEntity);
                            //TODO: Sound effect for essentia burst?
                        }
                    }
                    this.burstType = this.burstType.next(); //advance to next type for next burst
                }
            });

        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == CapabilityRegistry.ESSENTIA) {
            return this.essentiaCapabilityLazyOptional.cast();
        }
        return super.getCapability(cap, direction);
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.isEnabled = compound.getBoolean("isEnabled");
        this.essentiaCapability.deserializeNBT(compound.getCompound("essentia"));
        if (compound.contains("target")) {
            this.target = Optional.of(BlockPos.fromLong(compound.getLong("target")));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("isEnabled", this.isEnabled);
        compound.put("essentia", this.essentiaCapability.serializeNBT());
        this.target.ifPresent(target -> compound.putLong("target", target.toLong()));

        return super.write(compound);
    }
    //endregion Overrides

    //region Methods
    protected Vector3d getBurstMotion(Direction facing) {
        switch (facing) {
            case DOWN:
                return BURST_DOWN;
            case UP:
                return BURST_UP;
            case NORTH:
                return BURST_NORTH;
            case SOUTH:
                return BURST_SOUTH;
            case WEST:
                return BURST_WEST;
            case EAST:
                return BURST_EAST;
            default:
                return Vector3d.ZERO;
        }
    }
    //endregion Methods
}
