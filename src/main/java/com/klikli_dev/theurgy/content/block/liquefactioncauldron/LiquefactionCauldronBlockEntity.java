/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.liquefactioncauldron;

import com.klikli_dev.theurgy.content.block.HeatConsumer;
import com.klikli_dev.theurgy.content.block.itemhandler.PreventInsertWrapper;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleProvider;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class LiquefactionCauldronBlockEntity extends BlockEntity implements HeatConsumer {

    private final LiquefactionCauldronCraftingBehaviour craftingBehaviour;

    public ItemStackHandler inputInventory;
    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public FluidTank solventTank;
    public LazyOptional<IFluidHandler> solventTankCapability;

    private boolean heatedCache;

    public LiquefactionCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryTakeOnlyWrapper);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventoryTakeOnlyWrapper);

        this.solventTank = new SolventTank(FluidType.BUCKET_VOLUME, (fluidStack -> ForgeRegistries.FLUIDS.tags().getTag(FluidTagRegistry.SOLVENT).contains(fluidStack.getFluid())));

        this.solventTankCapability = LazyOptional.of(() -> this.solventTank);

        this.craftingBehaviour = new LiquefactionCauldronCraftingBehaviour(this, () -> this.inputInventory, () -> this.outputInventory, () -> this.solventTank);
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
        if (tag.contains("solventTank")) {
            this.solventTank.readFromNBT(tag.getCompound("solventTank"));
        }

        this.craftingBehaviour.readNetwork(tag);
    }

    public void writeNetwork(CompoundTag tag) {
        tag.put("solventTank", this.solventTank.writeToNBT(new CompoundTag()));

        this.craftingBehaviour.writeNetwork(tag);
    }

    @Override
    public boolean getHeatedCache() {
        return this.heatedCache;
    }

    @Override
    public void setHeatedCache(boolean heated) {
        this.heatedCache = heated;
    }


    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {
        boolean isHeated = this.isHeated();
        boolean hasInput = !this.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    public void tickClient() {
        var isProcessing = this.craftingBehaviour.isProcessing();
        if (isProcessing) {
            if (this.getLevel().getGameTime() % 2 == 0) { // only spawn particles every 2 ticks
                this.getLevel().addParticle(
                        ColoredBubbleParticleProvider.createOptions(new ParticleColor(255, 0, 255)),
                        this.getBlockPos().getX() + 0.33 + 0.33 * this.getLevel().getRandom().nextFloat(),

                        this.getBlockPos().getY() + 1.1,
                        this.getBlockPos().getZ() + 0.33 + 0.33 * this.getLevel().getRandom().nextFloat(),
                        0.0D, 0.015D, 0.0D

                );
            }
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return this.inputInventoryCapability.cast();
            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
            return this.inventoryCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER) return this.solventTankCapability.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.put("solventTank", this.solventTank.writeToNBT(new CompoundTag()));

        this.craftingBehaviour.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));

        if (pTag.contains("solventTank")) {
            this.solventTank.readFromNBT(pTag.getCompound("solventTank"));
        }

        this.craftingBehaviour.load(pTag);
    }

    public class SolventTank extends FluidTank {

        public SolventTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            LiquefactionCauldronBlockEntity.this.setChanged();
            LiquefactionCauldronBlockEntity.this.sendBlockUpdated();
        }
    }


    public class InputInventory extends ItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack newStack) {

            var oldStack = this.getStackInSlot(slot);

            boolean sameItem = !newStack.isEmpty() && ItemStack.isSameItemSameTags(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                LiquefactionCauldronBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return LiquefactionCauldronBlockEntity.this.craftingBehaviour.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            LiquefactionCauldronBlockEntity.this.setChanged();
        }
    }

    public class OutputInventory extends ItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentsChanged(int slot) {
            LiquefactionCauldronBlockEntity.this.setChanged();
        }
    }
}
