// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.entity;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public abstract class ColoredProjectile extends Projectile {

    public static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ColoredProjectile.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> FINAL_COLOR = SynchedEntityData.defineId(ColoredProjectile.class, EntityDataSerializers.INT);

    public ColoredProjectile(EntityType<? extends ColoredProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public ColoredProjectile(EntityType<? extends ColoredProjectile> type, Level worldIn, double x, double y, double z) {
        super(type, worldIn);
        this.setPos(x, y, z);
    }

    public ColoredProjectile(EntityType<? extends ColoredProjectile> type, Level worldIn, LivingEntity shooter) {
        super(type, worldIn);
        this.setOwner(shooter);
    }

    public void color(int color) {
        this.entityData.set(COLOR, color);
    }
    public void finalColor(int color) {
        this.entityData.set(FINAL_COLOR, color);
    }

    public int color() {
        return this.entityData.get(COLOR);
    }
    public int finalColor() {
        return this.entityData.get(FINAL_COLOR);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.entityData.set(COLOR, compound.getInt("color"));
        this.entityData.set(FINAL_COLOR, compound.getInt("final_color"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("color", this.entityData.get(COLOR));
        compound.putInt("final_color", this.entityData.get(FINAL_COLOR));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLOR, new ParticleColor(255, 25, 180).getColor());
        this.entityData.define(FINAL_COLOR, new ParticleColor(255, 25, 180).getColor());
    }


    @Nullable
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return ProjectileUtil.getEntityHitResult(this.level(), this, pStartVec, pEndVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

}
