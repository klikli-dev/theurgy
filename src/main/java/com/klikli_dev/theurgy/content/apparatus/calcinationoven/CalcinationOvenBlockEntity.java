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
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;


public class CalcinationOvenBlockEntity extends BlockEntity implements GeoBlockEntity, HasCraftingBehaviour<ItemHandlerRecipeInput, CalcinationRecipe, CalcinationCachedCheck> {

    public CraftingHeatReceiver heatReceiver;

    public CalcinationStorageBehaviour storageBehaviour;

    protected CalcinationCraftingBehaviour craftingBehaviour;
    protected HeatConsumerBehaviour heatConsumerBehaviour;
    protected AnimationBehaviour<?> animationBehaviour;


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
        var tag = new CompoundTag();
        this.writeNetwork(tag, pRegistries);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider pRegistries) {
        this.readNetwork(tag, pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(@NotNull Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.@NotNull Provider pRegistries) {
        var tag = packet.getTag();
        if (tag != null) {
            this.readNetwork(tag, pRegistries);
        }
    }

    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.storageBehaviour.readNetwork(pTag, pRegistries);
        this.craftingBehaviour.readNetwork(pTag, pRegistries);
    }

    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        this.storageBehaviour.writeNetwork(pTag, pRegistries);
        this.craftingBehaviour.writeNetwork(pTag, pRegistries);
    }

    public void tickServer() {
        boolean isHeated = this.heatConsumerBehaviour.isHeated();
        boolean hasInput = !this.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        pTag.put("heatReceiver", this.heatReceiver.serializeNBT(pRegistries));

        this.storageBehaviour.saveAdditional(pTag, pRegistries);
        this.craftingBehaviour.saveAdditional(pTag, pRegistries);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag pTag, HolderLookup.@NotNull Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("heatReceiver"))
            this.heatReceiver.deserializeNBT(pRegistries, pTag.get("heatReceiver"));

        this.storageBehaviour.loadAdditional(pTag, pRegistries);
        this.craftingBehaviour.loadAdditional(pTag, pRegistries);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoBlockEntity>(this, "controller", 10, this.animationBehaviour::animationHandler));
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
