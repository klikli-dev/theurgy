// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;


public class IncubatorSaltVesselBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public IncubatorBlockEntity incubator;

    public ItemStackHandler inputInventory;

    public IncubatorSaltVesselBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.INCUBATOR_SALT_VESSEL.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
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

    public void readNetwork(CompoundTag pTag) {
        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
    }

    public void writeNetwork(CompoundTag pTag) {
        pTag.put("inputInventory", this.inputInventory.serializeNBT());
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        this.writeNetwork(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.readNetwork(pTag);
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
            if (IncubatorSaltVesselBlockEntity.this.incubator != null)
                IncubatorSaltVesselBlockEntity.this.incubator.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            IncubatorSaltVesselBlockEntity.this.sendBlockUpdated();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(ItemTagRegistry.ALCHEMICAL_SALTS) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            IncubatorSaltVesselBlockEntity.this.setChanged();
        }
    }
}
