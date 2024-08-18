// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniactank;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Predicate;

public class SalAmmoniacTankBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public FluidTank tank;

    public SalAmmoniacTankBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.SAL_AMMONIAC_TANK.get(), pPos, pBlockState);

        this.tank = new Tank(FluidType.BUCKET_VOLUME * 2, e -> true);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        var tag = new CompoundTag();
        this.writeNetwork(tag, pRegistries);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider pRegistries) {
        this.readNetwork(tag, pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.@NotNull Provider pRegistries) {
        var tag = packet.getTag();
        if (tag != null) {
            this.readNetwork(tag, pRegistries);
        }
    }

    public void readNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        if (tag.contains("tank")) {
            this.tank.readFromNBT(pRegistries, tag.getCompound("tank"));
        }
    }

    public void writeNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        tag.put("tank", this.tank.writeToNBT(pRegistries, new CompoundTag()));
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("tank", this.tank.writeToNBT(pRegistries, new CompoundTag()));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("tank")) {
            this.tank.readFromNBT(pRegistries, pTag.getCompound("tank"));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public class Tank extends FluidTank {

        public Tank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            SalAmmoniacTankBlockEntity.this.setChanged();
            SalAmmoniacTankBlockEntity.this.sendBlockUpdated();
        }
    }
}
