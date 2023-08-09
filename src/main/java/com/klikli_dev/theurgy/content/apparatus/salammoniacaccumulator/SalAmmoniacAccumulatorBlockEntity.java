// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator;

import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleProvider;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
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
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SalAmmoniacAccumulatorBlockEntity extends BlockEntity {

    public ItemStackHandler inventory;
    public LazyOptional<IItemHandler> inventoryCapability;

    public FluidTank waterTank;
    public LazyOptional<IFluidHandler> waterTankCapability;

    protected SalAmmoniacAccumulatorCraftingBehaviour craftingBehaviour;

    private boolean checkOutputTankOnNextQuery;
    private boolean hasOutputTank;

    public SalAmmoniacAccumulatorBlockEntity(BlockPos pPos, BlockState pState) {
        super(BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), pPos, pState);

        this.inventory = new Inventory();
        this.inventoryCapability = LazyOptional.of(() -> this.inventory);

        this.craftingBehaviour = new SalAmmoniacAccumulatorCraftingBehaviour(this, () -> this.inventory, () -> this.inventory, () -> this.waterTank, () -> this.getOutputTank());

        this.waterTank = new WaterTank(FluidType.BUCKET_VOLUME * 10, this.craftingBehaviour::canProcess);
        this.waterTankCapability = LazyOptional.of(() -> this.waterTank);

        this.checkOutputTankOnNextQuery = true;
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
        if (tag.contains("waterTank")) {
            this.waterTank.readFromNBT(tag.getCompound("waterTank"));
        }

        if (tag.contains("inventory")) {
            this.inventory.deserializeNBT(tag.getCompound("inventory"));
        }

        this.craftingBehaviour.readNetwork(tag);
    }

    public void writeNetwork(CompoundTag tag) {
        tag.put("waterTank", this.waterTank.writeToNBT(new CompoundTag()));
        tag.put("inventory", this.inventory.serializeNBT());

        this.craftingBehaviour.writeNetwork(tag);
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {
        if (!this.hasOutputTank()) {
            return;
        }

        boolean hasInput = !this.waterTank.isEmpty();

        this.craftingBehaviour.tickServer(true, hasInput); //does not need heat
    }

    public void tickClient() {
        var isProcessing = this.craftingBehaviour.isProcessing();
        if (isProcessing) {
            if (this.getLevel().getGameTime() % 2 == 0) { // only spawn particles every 2 ticks

                var fluidStack = this.waterTank.getFluid();
                var fluidClientExtension = IClientFluidTypeExtensions.of(fluidStack.getFluid());
                int waterColor = fluidClientExtension.getTintColor(fluidStack);

                var particleColor = this.inventory.getStackInSlot(0).is(ItemTagRegistry.SAL_AMMONIAC_GEMS) ?
                        new ParticleColor(255, 192, 128) : ParticleColor.fromInt(waterColor);

                var fluidHeight = fluidStack.getAmount() / (float) this.waterTank.getCapacity();

                //move fluid plane between bottom and top of the model
                fluidHeight += 0.5f;
                fluidHeight *= 0.60f;

                this.getLevel().addParticle(
                        ColoredBubbleParticleProvider.createOptions(particleColor),
                        this.getBlockPos().getX() + 0.33 + 0.33 * this.getLevel().getRandom().nextFloat(),

                        this.getBlockPos().getY() + fluidHeight,
                        this.getBlockPos().getZ() + 0.33 + 0.33 * this.getLevel().getRandom().nextFloat(),
                        0.0D, 0.015D, 0.0D

                );
            }
        }
    }

    public IFluidHandler getOutputTank() {
        var below = this.getBlockPos().below();
        var blockEntity = this.level.getBlockEntity(below);
        return blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElseThrow(() -> new IllegalStateException("No fluid handler capability found on block entity below at location: " + below));
    }

    public void validateOutputTank() {
        var below = this.getBlockPos().below();
        var blockEntity = this.level.getBlockEntity(below);
        this.hasOutputTank = blockEntity != null && blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).isPresent();
    }

    public boolean hasOutputTank() {
        if (this.checkOutputTankOnNextQuery) {
            this.checkOutputTankOnNextQuery = false;
            this.validateOutputTank();
        }
        return this.hasOutputTank;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.inventoryCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER) return this.waterTankCapability.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryCapability.invalidate();
        this.waterTankCapability.invalidate();
    }
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inventory", this.inventory.serializeNBT());
        pTag.put("waterTank", this.waterTank.writeToNBT(new CompoundTag()));

        this.craftingBehaviour.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inventory")) this.inventory.deserializeNBT(pTag.getCompound("inventory"));

        if (pTag.contains("waterTank")) {
            this.waterTank.readFromNBT(pTag.getCompound("waterTank"));
        }

        this.craftingBehaviour.load(pTag);
    }

    public class WaterTank extends FluidTank {

        public WaterTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            SalAmmoniacAccumulatorBlockEntity.this.setChanged();
            SalAmmoniacAccumulatorBlockEntity.this.sendBlockUpdated();
        }
    }


    public class Inventory extends ItemStackHandler {

        public Inventory() {
            super(1);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack newStack) {

            var oldStack = this.getStackInSlot(slot);

            boolean sameItem = !newStack.isEmpty() && ItemStack.isSameItemSameTags(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                SalAmmoniacAccumulatorBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack newStack, boolean simulate) {
            if (!simulate) {
                var oldStack = this.getStackInSlot(slot);
                var result = super.insertItem(slot, newStack, simulate);

                if (result != newStack) {
                    SalAmmoniacAccumulatorBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
                }

                return result;
            }
            return super.insertItem(slot, newStack, simulate);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return SalAmmoniacAccumulatorBlockEntity.this.craftingBehaviour.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            SalAmmoniacAccumulatorBlockEntity.this.setChanged();
            SalAmmoniacAccumulatorBlockEntity.this.sendBlockUpdated();
        }
    }

}
