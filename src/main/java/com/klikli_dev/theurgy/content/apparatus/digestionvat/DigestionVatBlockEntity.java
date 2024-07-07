// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.digestionvat;

import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.redstone.VatRedstoneAutoCloseBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.HasStorageBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class DigestionVatBlockEntity extends BlockEntity implements HasCraftingBehaviour<ItemHandlerWithFluidRecipeInput, DigestionRecipe, DigestionCachedCheck>, HasStorageBehaviour<DigestionStorageBehaviour> {

    public DigestionCraftingBehaviour craftingBehaviour;
    public DigestionStorageBehaviour storageBehaviour;
    public VatRedstoneAutoCloseBehaviour<DigestionRecipe> redstoneBehaviour;

    public DigestionVatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DIGESTION_VAT.get(), pPos, pBlockState);

        this.storageBehaviour = new DigestionStorageBehaviour(this, () -> this.craftingBehaviour);

        this.craftingBehaviour = new DigestionCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory, () -> this.storageBehaviour.fluidTank);

        this.redstoneBehaviour = new VatRedstoneAutoCloseBehaviour<>(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        var tag = new CompoundTag();
        this.writeNetwork(tag, pRegistries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider pRegistries) {
        this.readNetwork(tag, pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider pRegistries) {
        var tag = packet.getTag();
        if (tag != null) {
            this.readNetwork(tag, pRegistries);
        }
    }

    public void readNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        this.storageBehaviour.readNetwork(tag, pRegistries);
        this.craftingBehaviour.readNetwork(tag, pRegistries);
    }

    public void writeNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        this.storageBehaviour.writeNetwork(tag, pRegistries);
        this.craftingBehaviour.writeNetwork(tag, pRegistries);
    }

    public void tickServer() {
        this.redstoneBehaviour.tickServer();

        boolean isOpen = this.getBlockState().getValue(BlockStateProperties.OPEN);
        boolean hasInput = this.hasInput();

        this.craftingBehaviour.tickServer(!isOpen, hasInput);

        if (!this.craftingBehaviour.isProcessing() && !isOpen) {
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.OPEN, true), Block.UPDATE_CLIENTS);
        }
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    public boolean hasInput() {
        for (int i = 0; i < this.storageBehaviour.inputInventory.getSlots(); i++) {
            if (!this.storageBehaviour.inputInventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        this.storageBehaviour.saveAdditional(pTag, pRegistries);
        this.craftingBehaviour.saveAdditional(pTag, pRegistries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        this.storageBehaviour.loadAdditional(pTag, pRegistries);
        this.craftingBehaviour.loadAdditional(pTag, pRegistries);
    }


    @Override
    public DigestionCraftingBehaviour craftingBehaviour() {
        return this.craftingBehaviour;
    }

    @Override
    public StorageBehaviour<DigestionStorageBehaviour> storageBehaviour() {
        return this.storageBehaviour;
    }
}
