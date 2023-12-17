// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.entity;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.glow.GlowParticleProvider;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.registry.EntityDataSerializerRegistry;
import com.klikli_dev.theurgy.registry.EntityRegistry;
import com.klikli_dev.theurgy.registry.ParticleRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FollowProjectile extends ColoredProjectile {

    public static final EntityDataAccessor<Vec3> TO = SynchedEntityData.defineId(FollowProjectile.class, EntityDataSerializerRegistry.VEC3_FLOAT.get());
    public static final EntityDataAccessor<Vec3> FROM = SynchedEntityData.defineId(FollowProjectile.class, EntityDataSerializerRegistry.VEC3_FLOAT.get());
    public static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(FollowProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> SPAWN_TOUCH = SynchedEntityData.defineId(FollowProjectile.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> DESPAWN = SynchedEntityData.defineId(FollowProjectile.class, EntityDataSerializers.INT);
    private int maxAge = 500;
    private int age;

    private Consumer<FollowProjectile> onArrival;

    public FollowProjectile(Level level, Vec3 from, Vec3 to, Color color, @Nullable Color finalColor, float size, Consumer<FollowProjectile> onArrival){
        this(EntityRegistry.FOLLOW_PROJECTILE.get(), level);
        this.entityData.set(FollowProjectile.TO, to);
        this.entityData.set(FollowProjectile.FROM, from);

        this.setPos(from.x, from.y, from.z); // no +0.5 as we are already providing vec3s
        this.entityData.set(COLOR,color.getRGB());
        this.entityData.set(FINAL_COLOR, finalColor == null ? color.getRGB() : finalColor.getRGB());
        this.entityData.set(SIZE, size);

        double distance = from.distanceTo(to);
        this.onArrival = onArrival;
        this.setDespawnDistance((int) (distance + 10));
    }

    public FollowProjectile(Level level, Vec3 from, Vec3 to, int r, int g, int b, float size){
        this(level, from, to, new Color(r, g, b), new Color(r,g,b), size, (p) -> {});
    }

    public FollowProjectile(Level level, Vec3 from, Vec3 to, Color color, float size, Consumer<FollowProjectile> onArrival) {
        this(level, from, to, color, color, size, onArrival);
    }

    public FollowProjectile(Level level, Vec3 from, Vec3 to, Color color, float size) {
        this(level, from, to, color, size, (p) -> {});
    }

    public FollowProjectile(Level level, Vec3 from, Vec3 to, Color color, Color finalColor, float size) {
        this(level, from, to, color, finalColor, size, (p) -> {});
    }


    public FollowProjectile(EntityType<? extends FollowProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public FollowProjectile(PlayMessages.SpawnEntity packet, Level world) {
        super(EntityRegistry.FOLLOW_PROJECTILE.get(), world);
    }


    public void setDespawnDistance(int distance) {
        this.getEntityData().set(DESPAWN, distance);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TO, new Vec3(0, 0, 0));
        this.entityData.define(FROM, new Vec3(0, 0, 0));
        this.entityData.define(SIZE, 0.0f);
        this.entityData.define(SPAWN_TOUCH, this.defaultsBurst());
        this.entityData.define(DESPAWN, 10);
    }

    public boolean defaultsBurst() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        this.age++;
        if (this.age > this.maxAge) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 to = this.entityData.get(FollowProjectile.TO);
        Vec3 from = this.entityData.get(FollowProjectile.FROM);
        var totalDist = from.distanceTo(to);
        var coveredDist = this.position().distanceTo(from);

        //handle arrival
        if (Math.sqrt(this.position().distanceToSqr(to)) < 1 || this.age > 1000 ||
                Math.sqrt(this.position().distanceToSqr(to)) > this.entityData.get(DESPAWN)) {
            if (this.level().isClientSide && this.entityData.get(SPAWN_TOUCH)) {
                //no interpolation here as we are at the end
                ParticleRegistry.spawnTouch((ClientLevel) this.level(), this.getOnPos(), ParticleColor.fromInt(this.finalColor()));
            }
            this.onArrival.accept(this);
            this.remove(RemovalReason.DISCARDED);
            return;
        }

        double posX = this.getX();
        double posY = this.getY();
        double posZ = this.getZ();
        double motionX = deltaMovement.x;
        double motionY = deltaMovement.y;
        double motionZ = deltaMovement.z;

        if (to.x() != 0 || to.y() != 0 || to.z() != 0) {
            double targetX = to.x();// + 0.5; we needed this when we used blockpos
            double targetY = to.y();// + 0.5;
            double targetZ = to.z();// + 0.5;
            Vec3 targetVector = new Vec3(targetX - posX, targetY - posY, targetZ - posZ);
            double length = targetVector.length();
            targetVector = targetVector.scale(0.3 / length);
            double weight = 0;
            if (length <= 3) {
                weight = 0.9 * ((3.0 - length) / 3.0);
            }

            motionX = (0.9 - weight) * motionX + (0.1 + weight) * targetVector.x;
            motionY = (0.9 - weight) * motionY + (0.1 + weight) * targetVector.y;
            motionZ = (0.9 - weight) * motionZ + (0.1 + weight) * targetVector.z;
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        this.setPos(posX, posY, posZ);

        this.setDeltaMovement(motionX, motionY, motionZ);
        var speed = 1.0f; //entity speed
        this.setDeltaMovement(this.getDeltaMovement().scale(speed));

        if (this.level().isClientSide && this.age > 1) {
            int particleAge = 50; //increase to make tracing the particle easier
            double deltaX = this.getX() - this.xOld;
            double deltaY = this.getY() - this.yOld;
            double deltaZ = this.getZ() - this.zOld;
            float dist = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 8.0f);

            var startColor = this.color();
            var endColor = this.finalColor();
            //TODO: the problem we have is that the distance to target increases before it decreases again
            //interpolate based on distance
            var currentColor = startColor == endColor ? endColor : Color.mixColors(startColor, endColor, (float) (coveredDist / totalDist));

            for (double i = 0.0; i <= dist; i++) {
                double coeff = (i / dist);
                this.level().addParticle(GlowParticleProvider.createOptions(ParticleColor.fromInt(currentColor),  this.entityData.get(SIZE), 0.75f, particleAge),
                        (this.getX() + deltaX * coeff), (this.getY() + deltaY * coeff), (this.getZ() + deltaZ * coeff),
                        0.0125f * (this.random.nextFloat() - 0.5f), 0.0125f * (this.random.nextFloat() - 0.5f), 0.0125f * (this.random.nextFloat() - 0.5f));
            }
        }
    }

    @Override
    public void setRemoved(RemovalReason reason) {
        if (reason == RemovalReason.UNLOADED_TO_CHUNK)
            reason = RemovalReason.DISCARDED;
        super.setRemoved(reason);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(FollowProjectile.FROM, Vec3.CODEC.parse(NbtOps.INSTANCE, compound.get("from")).result().get());
        this.entityData.set(FollowProjectile.TO, Vec3.CODEC.parse(NbtOps.INSTANCE, compound.get("to")).result().get());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        Vec3.CODEC.encodeStart(NbtOps.INSTANCE, this.entityData.get(FollowProjectile.FROM)).result().ifPresent((e) -> compound.put("from", e));
        Vec3.CODEC.encodeStart(NbtOps.INSTANCE, this.entityData.get(FollowProjectile.TO)).result().ifPresent((e) -> compound.put("to", e));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityType<?> getType() {
        return EntityRegistry.FOLLOW_PROJECTILE.get();
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public Vec3 to(){
        return this.entityData.get(FollowProjectile.TO);
    }

    public Vec3 from(){
        return this.entityData.get(FollowProjectile.FROM);
    }

}
