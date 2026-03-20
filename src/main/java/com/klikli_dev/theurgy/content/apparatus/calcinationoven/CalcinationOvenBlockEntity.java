// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven;

import com.klikli_dev.theurgy.content.behaviour.animation.AnimationBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.heat.HeatConsumerBehaviour;
import com.klikli_dev.theurgy.content.capability.CraftingHeatReceiver;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;


public class CalcinationOvenBlockEntity extends BlockEntity implements GeoBlockEntity, HasCraftingBehaviour<ItemHandlerRecipeInput, CalcinationRecipe, CalcinationCachedCheck> {

    public CraftingHeatReceiver heatReceiver;

    public CalcinationStorageBehaviour storageBehaviour;

    protected CalcinationCraftingBehaviour craftingBehaviour;
    protected HeatConsumerBehaviour heatConsumerBehaviour;
    protected AnimationBehaviour<CalcinationOvenBlockEntity> animationBehaviour;


    public CalcinationOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALCINATION_OVEN.get(), pPos, pBlockState);

        this.storageBehaviour = new CalcinationStorageBehaviour(this, () -> this.craftingBehaviour);

        this.heatReceiver = new CraftingHeatReceiver(this);

        this.craftingBehaviour = new CalcinationCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory);
        this.heatConsumerBehaviour = new HeatConsumerBehaviour(this);
        this.animationBehaviour = new CalcinationAnimationBehaviour(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return ValueIOUtils.serialize(pRegistries, this::writeNetwork);
    }

    @Override
    public void handleUpdateTag(@NotNull ValueInput input) {
        this.readNetwork(input);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection connection, @NotNull ValueInput input) {
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
        boolean isHeated = this.heatConsumerBehaviour.isHeated();
        boolean hasInput = !this.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        for (int i = 0; i < this.storageBehaviour.inventory.getSlots(); i++) {
            Containers.dropItemStack(this.level, pPos.getX(), pPos.getY(), pPos.getZ(), this.storageBehaviour.inventory.getStackInSlot(i));
        }
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput heatReceiverOutput = output.child("heatReceiver");
        this.heatReceiver.serialize(heatReceiverOutput);
        if (heatReceiverOutput.isEmpty()) {
            output.discard("heatReceiver");
        }

        this.storageBehaviour.saveAdditional(output);
        this.craftingBehaviour.saveAdditional(output);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        input.child("heatReceiver").ifPresent(this.heatReceiver::deserialize);

        this.storageBehaviour.loadAdditional(input);
        this.craftingBehaviour.loadAdditional(input);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<CalcinationOvenBlockEntity>("controller", 10, this.animationBehaviour::animationHandler));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationBehaviour.getAnimatableInstanceCache();
    }

    @Override
    public CalcinationCraftingBehaviour craftingBehaviour() {
        return this.craftingBehaviour;
    }
}
