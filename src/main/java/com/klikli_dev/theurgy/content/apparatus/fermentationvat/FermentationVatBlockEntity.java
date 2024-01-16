// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.*;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.PreventInsertExtractFluidWrapper;
import com.klikli_dev.theurgy.content.storage.PreventInsertExtractWrapper;
import com.klikli_dev.theurgy.content.storage.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FermentationVatBlockEntity extends BlockEntity implements HasCraftingBehaviour<RecipeWrapperWithFluid, FermentationRecipe, FermentationCachedCheck> {

    public FermentationStorageBehaviour storageBehaviour;

    public FermentationCraftingBehaviour craftingBehaviour;

    public FermentationVatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.FERMENTATION_VAT.get(), pPos, pBlockState);

        this.storageBehaviour = new FermentationStorageBehaviour(this, () -> this.craftingBehaviour);

        this.craftingBehaviour = new FermentationCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory, () -> this.storageBehaviour.fluidTank);
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
        //TODO: isProcessing syncs to client - should we act on that?
        //      a bubbling sound would be good
        //      we may be able to just use the closed state clientside to not have to tick the TE -> however, not really a relevant optimization
        boolean isOpen = this.getBlockState().getValue(BlockStateProperties.OPEN);
        boolean hasInput = this.hasInput();

        this.craftingBehaviour.tickServer(!isOpen, hasInput);

        if(!this.craftingBehaviour.isProcessing() && !isOpen){
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        var storage = this.storageBehaviour.getCapability(cap, side);
        if (storage.isPresent()) {
            return storage;
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        this.storageBehaviour.saveAdditional(pTag);
        this.craftingBehaviour.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.storageBehaviour.load(pTag);
        this.craftingBehaviour.load(pTag);
    }


    @Override
    public FermentationCraftingBehaviour craftingBehaviour() {
        return this.craftingBehaviour;
    }

}
