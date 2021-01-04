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

import com.github.klikli_dev.theurgy.client.particle.GlowingBallParticleData;
import com.github.klikli_dev.theurgy.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class GlowingBallEntity extends Entity {
    //region Fields
    protected static final DataParameter<BlockPos> TARGET =
            EntityDataManager.createKey(GlowingBallEntity.class, DataSerializers.BLOCK_POS);
    public int lifespan = 80;

    public Vector3f color;
    //endregion Fields

    //region Initialization
    public GlowingBallEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public GlowingBallEntity(World worldIn, double x, double y, double z) {
        this(EntityRegistry.GLOWING_BALL_TYPE.get(), worldIn);
        this.setPosition(x, y, z);
    }
    //endregion Initialization

    //region Getter / Setter
    public BlockPos getTarget() {
        return this.dataManager.get(TARGET);
    }
    //endregion Getter / Setter

    //region Overrides
    @Override
    protected void registerData() {
        this.dataManager.register(TARGET, new BlockPos(0, 0, 0));
    }

    @Override
    public void tick() {
        super.tick();

        this.lifespan--;
        if (this.lifespan <= 0) {
            this.remove();
        }

        if (this.isAlive()) {
            BlockPos target = this.getTarget();
            double targetX = target.getX() + 0.5;
            double targetY = target.getY() + 0.5;
            double targetZ = target.getZ() + 0.5;
            Vector3d targetVector = new Vector3d(
                    targetX - this.getPosX(),
                    targetY - this.getPosY(),
                    targetZ - this.getPosZ());

            double length = targetVector.length();
            targetVector = targetVector.scale(0.3 / length);
            double weight = 0;
            if (length <= 3) {
                weight = 0.9 * ((3.0 - length) / 3.0);
            }

            Vector3d currentMotion = this.getMotion();
            this.setMotion(
                    (0.9 - weight) * currentMotion.x + (0.1 + weight) * targetVector.x,
                    (0.9 - weight) * currentMotion.y + (0.1 + weight) * targetVector.y,
                    (0.9 - weight) * currentMotion.z + (0.1 + weight) * targetVector.z
            );

            this.prevPosX = this.getPosX();
            this.prevPosY = this.getPosY();
            this.prevPosZ = this.getPosZ();
            this.setPosition(
                    this.prevPosX + currentMotion.x,
                    this.prevPosY + currentMotion.y,
                    this.prevPosZ + currentMotion.z
            );

            //if target reached
            if (this.getPosition().withinDistance(this.getTarget(), 0.5f)) {
                this.onTargetReached();
            }

            if (this.world.isRemote) {
                double deltaX = this.getPosX() - this.prevPosX;
                double deltaY = this.getPosY() - this.prevPosY;
                double deltaZ = this.getPosZ() - this.prevPosZ;
                double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 20);
                for (double i = 0; i < dist; i++) {
                    double coeff = i / dist;

                    GlowingBallParticleData data = new GlowingBallParticleData(
                            this.color.getX(),
                            this.color.getY(),
                            this.color.getZ(),
                            0.5f, 0.5f, 12);

                    this.world.addParticle(data,
                            (float) (this.prevPosX + deltaX * coeff),
                            (float) (this.prevPosY + deltaY * coeff),
                            (float) (this.prevPosZ + deltaZ * coeff),
                            0.0125f * (this.world.rand.nextFloat() - 0.5f),
                            0.0125f * (this.world.rand.nextFloat() - 0.5f),
                            0.0125f * (this.world.rand.nextFloat() - 0.5f)
                    );
                }
            }
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.lifespan = compound.getByte("lifespan");
        if (compound.contains("target")) {
            this.dataManager.set(TARGET, BlockPos.fromLong(compound.getLong("target")));
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putByte("lifespan", (byte) this.lifespan);
        compound.putLong("target", this.dataManager.get(TARGET).toLong());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    //endregion Overrides

    //region Methods
    public void init(BlockPos target, double motionX, double motionY,
                     double motionZ, Vector3f color) {
        this.dataManager.set(TARGET, target);
        this.setMotion(motionX, motionY, motionZ);
        this.color = color;
    }

    protected void onTargetReached() {
        this.lifespan = 0;
        this.setMotion(0, 0, 0);
        this.remove();
    }
    //endregion Methods
}
