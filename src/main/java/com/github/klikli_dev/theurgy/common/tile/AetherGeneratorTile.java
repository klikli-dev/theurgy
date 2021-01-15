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

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.client.particle.SparkleParticleData;
import com.github.klikli_dev.theurgy.common.capability.DefaultAetherCapability;
import com.github.klikli_dev.theurgy.common.capability.IAetherCapability;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.github.klikli_dev.theurgy.registry.MultiblockRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AetherGeneratorTile extends NetworkedTileEntity implements ITickableTileEntity {

    //region Fields
    public static final BlockPos[] CRYSTAL_POSITIONS = new BlockPos[]{
            new BlockPos(-4, -2, 0),
            new BlockPos(4, -2, 0),
            new BlockPos(0, -2, -4),
            new BlockPos(0, -2, 4)
    };

    public static final int GENERATOR_AETHER_CAPACITY = 10000;
    public static final int MULTIBLOCK_VALIDITY_RATE = 20 * 120;
    public static final int MULTIBLOCK_VALIDITY_RATE_CLIENT = 20 * 10;
    public final LazyOptional<IAetherCapability> aetherCapabilityLazyOptional;
    public IAetherCapability aetherCapability;
    public boolean hasValidMultiblock;
    //endregion Fields

    //region Initialization
    public AetherGeneratorTile() {
        super(TileRegistry.AETHER_GENERATOR.get());
        this.aetherCapability = new DefaultAetherCapability(GENERATOR_AETHER_CAPACITY) {
            //region Overrides
            @Override
            public void onContentsChanged() {
                super.onContentsChanged();
                AetherGeneratorTile.this.markNetworkDirty();
            }
            //endregion Overrides
        };
        this.aetherCapabilityLazyOptional = LazyOptional.of(() -> this.aetherCapability);
    }
    //endregion Initialization

    //region Overrides

    @Nonnull
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
        long time = this.world.getGameTime();

        //Validate the multiblock. Do this rarely on server, but more often on client
        if (this.world.isRemote && time % MULTIBLOCK_VALIDITY_RATE_CLIENT == 0) {
            this.hasValidMultiblock = this.validateMultiblock();
        }
        if (!this.world.isRemote && time % MULTIBLOCK_VALIDITY_RATE == 0) {
            this.hasValidMultiblock = this.validateMultiblock();
        }

        //show client side particle effects
        if (this.world.isRemote && this.hasValidMultiblock && time % 10 == 0) {
            BlockPos crystalPos = this.pos.add(CRYSTAL_POSITIONS[this.world.rand.nextInt(CRYSTAL_POSITIONS.length)]);
            //target is one block below our pos -> the condenser
            SparkleParticleData data = new SparkleParticleData(
                    0.6f + this.world.rand.nextFloat() * 0.3f,
                    0.2f,
                    0.6f + this.world.rand.nextFloat() * 0.3f,
                    this.pos.getX() + 0.5f, this.pos.getY() - 1 + 0.5f, this.pos.getZ() + 0.5f);

            this.world
                    .addParticle(data, crystalPos.getX() + this.world.rand.nextFloat(),
                            crystalPos.getY() + this.world.rand.nextFloat(),
                            crystalPos.getZ() + this.world.rand.nextFloat(), 0, 0, 0);

        }

        //generate energy
        if (!this.world.isRemote && this.hasValidMultiblock &&
            time % Theurgy.CONFIG.aetherSettings.tier1GenerationRate.get() == 0) {
            int amountToGenerate = Theurgy.CONFIG.aetherSettings.tier1GenerationAmount.get();
            this.aetherCapability.receiveEnergy(amountToGenerate, false);
        }
    }
    //endregion Overrides

    //region Methods
    public boolean validateMultiblock() {
        return MultiblockRegistry.AETHER_GENERATOR.get().getMultiblock().validate(this.world, this.pos) != null;
    }
    //endregion Methods
}
