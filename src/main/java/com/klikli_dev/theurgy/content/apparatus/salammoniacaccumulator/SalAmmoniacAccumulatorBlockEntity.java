// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator;

import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Predicate;

public class SalAmmoniacAccumulatorBlockEntity extends BlockEntity implements GeoBlockEntity {

    protected final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public ItemStackHandler inventory;

    public FluidTank waterTank;

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
        if (tag.contains("waterTank")) {
            this.waterTank.readFromNBT(pRegistries, tag.getCompound("waterTank"));
        }

        if (tag.contains("inventory")) {
            this.inventory.deserializeNBT(pRegistries, tag.getCompound("inventory"));
        }

        this.craftingBehaviour.readNetwork(tag, pRegistries);
    }

    public void writeNetwork(CompoundTag tag, HolderLookup.Provider pRegistries) {
        tag.put("waterTank", this.waterTank.writeToNBT(pRegistries, new CompoundTag()));
        tag.put("inventory", this.inventory.serializeNBT(pRegistries));

        this.craftingBehaviour.writeNetwork(tag, pRegistries);;
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

    public IFluidHandler getOutputTank() {
        var below = this.getBlockPos().below();

        return this.level.getCapability(Capabilities.FluidHandler.BLOCK, below, null);
    }

    public void validateOutputTank() {
        var below = this.getBlockPos().below();
        this.hasOutputTank = this.level.getCapability(Capabilities.FluidHandler.BLOCK, below, null) != null;
    }

    public boolean hasOutputTank() {
        if (this.checkOutputTankOnNextQuery) {
            this.checkOutputTankOnNextQuery = false;
            this.validateOutputTank();
        }
        return this.hasOutputTank;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        pTag.put("inventory", this.inventory.serializeNBT(pRegistries));
        pTag.put("waterTank", this.waterTank.writeToNBT(pRegistries, new CompoundTag()));

        this.craftingBehaviour.saveAdditional(pTag, pRegistries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("inventory")) this.inventory.deserializeNBT(pRegistries, pTag.getCompound("inventory"));

        if (pTag.contains("waterTank")) {
            this.waterTank.readFromNBT(pRegistries, pTag.getCompound("waterTank"));
        }

        this.craftingBehaviour.loadAdditional(pTag, pRegistries);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
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
        public boolean isItemValid(int slot, ItemStack stack) {
            return SalAmmoniacAccumulatorBlockEntity.this.craftingBehaviour.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            SalAmmoniacAccumulatorBlockEntity.this.setChanged();
            //network update only if presence of crystal chanced, stack size changes are irrelevant
        }
    }

}
