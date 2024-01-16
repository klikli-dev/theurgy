// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven;

import com.klikli_dev.theurgy.content.behaviour.AnimationBehaviour;
import com.klikli_dev.theurgy.content.behaviour.HeatConsumerBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultHeatReceiver;
import com.klikli_dev.theurgy.content.capability.HeatReceiver;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;


public class CalcinationOvenBlockEntity extends BlockEntity implements GeoBlockEntity {


    public DefaultHeatReceiver heatReceiver;
    public LazyOptional<HeatReceiver> heatReceiverCapability;

    protected CalcinationStorageBehaviour storageBehaviour;


    protected CalcinationCraftingBehaviour craftingBehaviour;
    protected HeatConsumerBehaviour heatConsumerBehaviour;
    protected AnimationBehaviour<?> animationBehaviour;


    public CalcinationOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALCINATION_OVEN.get(), pPos, pBlockState);

        this.storageBehaviour = new CalcinationStorageBehaviour(this, () -> this.craftingBehaviour);

        this.heatReceiver = new DefaultHeatReceiver();
        this.heatReceiverCapability = LazyOptional.of(() -> this.heatReceiver);

        this.craftingBehaviour = new CalcinationCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory);
        this.heatConsumerBehaviour = new HeatConsumerBehaviour(this);
        this.animationBehaviour = new CalcinationAnimationBehaviour(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        this.writeNetwork(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.readNetwork(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        var tag = packet.getTag();
        if (tag != null) {
            this.readNetwork(tag);
        }
    }

    public void readNetwork(CompoundTag tag) {
        this.storageBehaviour.readNetwork(tag);
        this.craftingBehaviour.readNetwork(tag);
    }

    public void writeNetwork(CompoundTag tag) {
        this.storageBehaviour.writeNetwork(tag);
        this.craftingBehaviour.writeNetwork(tag);
    }

    public void tickServer() {
        boolean isHeated = this.heatConsumerBehaviour.isHeated();
        boolean hasInput = !this.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        var storage = this.storageBehaviour.getCapability(cap, side);
        if (storage.isPresent()) {
            return storage;
        }

        if (cap == CapabilityRegistry.HEAT_RECEIVER) {
            return this.heatReceiverCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.heatReceiverCapability.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("heatReceiver", this.heatReceiver.serializeNBT());

        this.storageBehaviour.saveAdditional(pTag);
        this.craftingBehaviour.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("heatReceiver"))
            this.heatReceiver.deserializeNBT(pTag.get("heatReceiver"));

        this.storageBehaviour.load(pTag);
        this.craftingBehaviour.load(pTag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoBlockEntity>(this, "controller", 10, this.animationBehaviour::animationHandler));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationBehaviour.getAnimatableInstanceCache();
    }
}
