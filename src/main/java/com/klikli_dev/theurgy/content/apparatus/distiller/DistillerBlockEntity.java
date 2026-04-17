// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.klikli_dev.theurgy.content.behaviour.animation.AnimationBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.heat.HeatConsumerBehaviour;
import com.klikli_dev.theurgy.content.capability.CraftingHeatReceiver;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.content.render.HeldStackFitProvider;
import com.klikli_dev.theurgy.content.storage.ItemStorageHelper;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.util.NetworkTagHelper;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class DistillerBlockEntity extends BlockEntity implements GeoBlockEntity, HasCraftingBehaviour<ItemHandlerRecipeInput, DistillationRecipe, DistillationCachedCheck>, HeldStackFitProvider {

    public CraftingHeatReceiver heatReceiver;

    public DistillationStorageBehaviour storageBehaviour;

    protected DistillationCraftingBehaviour craftingBehaviour;
    protected HeatConsumerBehaviour heatConsumerBehaviour;
    protected AnimationBehaviour<DistillerBlockEntity> animationBehaviour;

    public DistillerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DISTILLER.get(), pPos, pBlockState);


        this.storageBehaviour = new DistillationStorageBehaviour(this, () -> this.craftingBehaviour);

        this.heatReceiver = new CraftingHeatReceiver(this);

        this.craftingBehaviour = new DistillationCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory);
        this.heatConsumerBehaviour = new HeatConsumerBehaviour(this);
        this.animationBehaviour = new DistillerAnimationBehaviour(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return NetworkTagHelper.write(pRegistries, this::writeNetwork);
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
        boolean isHeated = this.heatConsumerBehaviour.isHeated();
        boolean hasInput = !this.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        for (int i = 0; i < ItemStorageHelper.getSlots(this.storageBehaviour.inventory); i++) {
            Containers.dropItemStack(this.level, pPos.getX(), pPos.getY(), pPos.getZ(), ItemStorageHelper.getStackInSlot(this.storageBehaviour.inventory, i));
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
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
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        input.child("heatReceiver").ifPresent(this.heatReceiver::deserialize);

        this.storageBehaviour.loadAdditional(input);
        this.craftingBehaviour.loadAdditional(input);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<DistillerBlockEntity>("controller", 0, this.animationBehaviour::animationHandler));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationBehaviour.getAnimatableInstanceCache();
    }

    @Override
    public CraftingBehaviour<ItemHandlerRecipeInput, DistillationRecipe, DistillationCachedCheck> craftingBehaviour() {
        return this.craftingBehaviour;
    }

    @Override
    public List<? extends SettableItemStorage> heldStackFitItemStorages() {
        return List.of(this.storageBehaviour.inputInventory);
    }
}
