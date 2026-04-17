// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleProvider;
import com.klikli_dev.theurgy.content.render.HeldStackFitProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredFluidTank;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class SalAmmoniacAccumulatorBlockEntity extends BlockEntity implements GeoBlockEntity, HeldStackFitProvider {

    protected final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public MonitoredItemStackHandler inventory;

    public WaterTank waterTank;

    protected SalAmmoniacAccumulatorCraftingBehaviour craftingBehaviour;

    private boolean checkOutputTankOnNextQuery;
    private boolean hasOutputTank;

    public SalAmmoniacAccumulatorBlockEntity(BlockPos pPos, BlockState pState) {
        super(BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), pPos, pState);

        this.inventory = new Inventory();

        this.craftingBehaviour = new SalAmmoniacAccumulatorCraftingBehaviour(this, () -> this.inventory, () -> this.inventory, () -> this.waterTank, this::getOutputTank);

        this.waterTank = new WaterTank(FluidType.BUCKET_VOLUME * 10, this.craftingBehaviour::canProcess);

        this.checkOutputTankOnNextQuery = true;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput input) {
        this.loadWithComponents(input);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection connection, @NotNull ValueInput input) {
        this.loadWithComponents(input);
    }

    public void readNetwork(ValueInput input) {
        input.child("waterTank").ifPresent(this.waterTank::deserialize);
        input.child("inventory").ifPresent(this.inventory::deserialize);
        this.craftingBehaviour.readNetwork(input);
    }

    public void writeNetwork(ValueOutput output) {
        ValueOutput waterTankOutput = output.child("waterTank");
        this.waterTank.serialize(waterTankOutput);
        if (waterTankOutput.isEmpty()) {
            output.discard("waterTank");
        }

        ValueOutput inventoryOutput = output.child("inventory");
        this.inventory.serialize(inventoryOutput);
        if (inventoryOutput.isEmpty()) {
            output.discard("inventory");
        }

        this.craftingBehaviour.writeNetwork(output);
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide())
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {
        if (!this.hasOutputTank()) {
            return;
        }

        boolean hasInput = !this.waterTank.isEmpty() || !this.inventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(true, hasInput); //does not need heat
    }

    public void tickClient() {
        var isProcessing = this.craftingBehaviour.isProcessing();
        if (isProcessing) {
            if (this.getLevel().getGameTime() % 2 == 0) { // only spawn particles every 2 ticks

                var fluidStack = this.waterTank.getFluid();
                int waterColor = 0xFFFFFFFF; // SAL_AMMONIAC uses water base, default no-tint (IClientFluidTypeExtensions.getTintColor removed)

                var particleColor = this.inventory.getStackInSlot(0).is(ItemTagRegistry.GEMS_SAL_AMMONIAC) ?
                        new ParticleColor(255, 192, 128) : ParticleColor.fromInt(waterColor);

                var fluidHeight = fluidStack.getAmount() / (float) this.waterTank.getCapacity();

                //move fluid plane between bottom and top of the model
                fluidHeight += 0.3f;
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

    public ResourceHandler<FluidResource> getOutputTank() {
        var below = this.getBlockPos().below();

        return this.level.getCapability(CapabilityRegistry.FLUID_HANDLER, below, null);
    }

    public void validateOutputTank() {
        var below = this.getBlockPos().below();
        this.hasOutputTank = this.level.getCapability(CapabilityRegistry.FLUID_HANDLER, below, null) != null;
    }

    public boolean hasOutputTank() {
        if (this.checkOutputTankOnNextQuery) {
            this.checkOutputTankOnNextQuery = false;
            this.validateOutputTank();
        }
        return this.hasOutputTank;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        if (this.level != null) {
            for (int i = 0; i < this.inventory.getSlots(); i++) {
                Containers.dropItemStack(this.level, pPos.getX(), pPos.getY(), pPos.getZ(), this.inventory.getStackInSlot(i));
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        this.writeNetwork(output);
        this.craftingBehaviour.saveAdditional(output);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.readNetwork(input);
        this.craftingBehaviour.loadAdditional(input);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public class WaterTank extends MonitoredFluidTank {
        public WaterTank(int capacity, Predicate<FluidStack> validator) {
            super(capacity, validator);
        }

        @Override
        protected void onContentsChanged() {
            SalAmmoniacAccumulatorBlockEntity.this.setChanged();
            SalAmmoniacAccumulatorBlockEntity.this.sendBlockUpdated();
        }
    }


    public class Inventory extends MonitoredItemStackHandler {

        public Inventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            SalAmmoniacAccumulatorBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            SalAmmoniacAccumulatorBlockEntity.this.sendBlockUpdated();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return SalAmmoniacAccumulatorBlockEntity.this.craftingBehaviour.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            SalAmmoniacAccumulatorBlockEntity.this.setChanged();
            //network update only if presence of crystal chanced, stack size changes are irrelevant
        }
    }

    @Override
    public List<? extends SettableItemStorage> heldStackFitItemStorages() {
        return List.of(this.inventory);
    }

    @Override
    public List<? extends MonitoredFluidTank> heldStackFitFluidTanks() {
        return List.of(this.waterTank);
    }

}
