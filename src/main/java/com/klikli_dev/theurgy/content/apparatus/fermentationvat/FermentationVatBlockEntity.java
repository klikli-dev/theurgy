// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.fermentationvat;

import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.redstone.VatRedstoneAutoCloseBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.HasStorageBehaviour;
import com.klikli_dev.theurgy.content.recipe.DigestionRecipe;
import com.klikli_dev.theurgy.content.recipe.FermentationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.content.render.HeldStackFitProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredFluidTank;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FermentationVatBlockEntity extends BlockEntity implements HasCraftingBehaviour<ItemHandlerWithFluidRecipeInput, FermentationRecipe, FermentationCachedCheck>, HasStorageBehaviour<FermentationStorageBehaviour>, HeldStackFitProvider {

    public FermentationStorageBehaviour storageBehaviour;
    public FermentationCraftingBehaviour craftingBehaviour;
    public VatRedstoneAutoCloseBehaviour<DigestionRecipe> redstoneBehaviour;

    public FermentationVatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.FERMENTATION_VAT.get(), pPos, pBlockState);

        this.storageBehaviour = new FermentationStorageBehaviour(this, () -> this.craftingBehaviour);

        this.craftingBehaviour = new FermentationCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory, () -> this.storageBehaviour.fluidTank);
        this.redstoneBehaviour = new VatRedstoneAutoCloseBehaviour<>(this);
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
        this.storageBehaviour.readNetwork(input);
        this.craftingBehaviour.readNetwork(input);
    }

    public void writeNetwork(ValueOutput output) {
        this.storageBehaviour.writeNetwork(output);
        this.craftingBehaviour.writeNetwork(output);
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

    public boolean hasInput() {
        for (int i = 0; i < this.storageBehaviour.inputInventory.getSlots(); i++) {
            if (!this.storageBehaviour.inputInventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        for (int i = 0; i < this.storageBehaviour.inventory.getSlots(); i++) {
            Containers.dropItemStack(this.level, pPos.getX(), pPos.getY(), pPos.getZ(), this.storageBehaviour.inventory.getStackInSlot(i));
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        this.storageBehaviour.saveAdditional(output);
        this.craftingBehaviour.saveAdditional(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.storageBehaviour.loadAdditional(input);
        this.craftingBehaviour.loadAdditional(input);
    }


    @Override
    public FermentationCraftingBehaviour craftingBehaviour() {
        return this.craftingBehaviour;
    }

    @Override
    public FermentationStorageBehaviour storageBehaviour() {
        return this.storageBehaviour;
    }

    @Override
    public List<? extends SettableItemStorage> heldStackFitItemStorages() {
        return List.of(this.storageBehaviour.inputInventory);
    }

    @Override
    public List<? extends MonitoredFluidTank> heldStackFitFluidTanks() {
        return List.of(this.storageBehaviour.fluidTank);
    }
}
