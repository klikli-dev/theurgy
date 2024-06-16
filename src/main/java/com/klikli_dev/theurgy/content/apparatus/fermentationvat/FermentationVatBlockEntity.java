// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class FermentationVatBlockEntity extends BlockEntity implements HasCraftingBehaviour<ItemHandlerWithFluidRecipeInput, FermentationRecipe, FermentationCachedCheck> {

    public FermentationStorageBehaviour storageBehaviour;

    public FermentationCraftingBehaviour craftingBehaviour;

    public FermentationVatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.FERMENTATION_VAT.get(), pPos, pBlockState);

        this.storageBehaviour = new FermentationStorageBehaviour(this, () -> this.craftingBehaviour);

        this.craftingBehaviour = new FermentationCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory, () -> this.storageBehaviour.fluidTank);
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
        this.storageBehaviour.writeNetwork(tag, pRegistries);;
        this.craftingBehaviour.writeNetwork(tag, pRegistries);;
    }

    public void tickServer() {
        //TODO: isProcessing syncs to client - should we act on that?
        //      a bubbling sound would be good
        //      we may be able to just use the closed state clientside to not have to tick the TE -> however, not really a relevant optimization
        boolean isOpen = this.getBlockState().getValue(BlockStateProperties.OPEN);
        boolean hasInput = this.hasInput();

        this.craftingBehaviour.tickServer(!isOpen, hasInput);

        if (!this.craftingBehaviour.isProcessing() && !isOpen) {
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.OPEN, true), Block.UPDATE_CLIENTS);
        }
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
    public FermentationCraftingBehaviour craftingBehaviour() {
        return this.craftingBehaviour;
    }

}
