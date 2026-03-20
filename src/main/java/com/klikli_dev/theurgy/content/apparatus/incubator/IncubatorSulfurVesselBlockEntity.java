// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;


public class IncubatorSulfurVesselBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public IncubatorBlockEntity incubator;

    public ItemStackHandler inputInventory;

    public IncubatorSulfurVesselBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.INCUBATOR_SULFUR_VESSEL.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return ValueIOUtils.serialize(pRegistries, this::writeNetwork);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        this.readNetwork(input);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ValueInput input) {
        this.readNetwork(input);
    }

    public void readNetwork(ValueInput input) {
        input.child("inputInventory").ifPresent(tag -> this.inputInventory.deserialize(tag));
    }

    public void writeNetwork(ValueOutput output) {
        ValueOutput inputInventoryOutput = output.child("inputInventory");
        this.inputInventory.serialize(inputInventoryOutput);
        if (inputInventoryOutput.isEmpty()) {
            output.discard("inputInventory");
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        if (this.level != null) {
            for (int i = 0; i < this.inputInventory.getSlots(); i++) {
                Containers.dropItemStack(this.level, pPos.getX(), pPos.getY(), pPos.getZ(), this.inputInventory.getStackInSlot(i));
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        this.writeNetwork(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.readNetwork(input);
    }

    public void setIncubator(IncubatorBlockEntity incubator) {
        this.incubator = incubator;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public class InputInventory extends MonitoredItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            if (IncubatorSulfurVesselBlockEntity.this.incubator != null)
                IncubatorSulfurVesselBlockEntity.this.incubator.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            IncubatorSulfurVesselBlockEntity.this.sendBlockUpdated();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(ItemTagRegistry.ALCHEMICAL_SULFURS) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            IncubatorSulfurVesselBlockEntity.this.setChanged();
        }
    }
}
