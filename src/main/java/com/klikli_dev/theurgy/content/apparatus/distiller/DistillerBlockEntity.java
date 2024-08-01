// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.behaviour.animation.AnimationBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.heat.HeatConsumerBehaviour;
import com.klikli_dev.theurgy.content.capability.CraftingHeatReceiver;
import com.klikli_dev.theurgy.content.capability.DefaultHeatReceiver;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;


public class DistillerBlockEntity extends BlockEntity implements GeoBlockEntity, HasCraftingBehaviour<ItemHandlerRecipeInput, DistillationRecipe, DistillationCachedCheck> {

    public CraftingHeatReceiver heatReceiver;

    public DistillationStorageBehaviour storageBehaviour;

    protected DistillationCraftingBehaviour craftingBehaviour;
    protected HeatConsumerBehaviour heatConsumerBehaviour;
    protected AnimationBehaviour<?> animationBehaviour;

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
        boolean isHeated = this.heatConsumerBehaviour.isHeated();
        boolean hasInput = !this.storageBehaviour.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        pTag.put("heatReceiver", this.heatReceiver.serializeNBT(pRegistries));

        this.storageBehaviour.saveAdditional(pTag, pRegistries);
        this.craftingBehaviour.saveAdditional(pTag, pRegistries);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("heatReceiver"))
            this.heatReceiver.deserializeNBT(pRegistries, pTag.get("heatReceiver"));

        this.storageBehaviour.loadAdditional(pTag, pRegistries);
        this.craftingBehaviour.loadAdditional(pTag, pRegistries);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoBlockEntity>(this, "controller", 0, this.animationBehaviour::animationHandler));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationBehaviour.getAnimatableInstanceCache();
    }

    @Override
    public CraftingBehaviour<ItemHandlerRecipeInput, DistillationRecipe, DistillationCachedCheck> craftingBehaviour() {
        return this.craftingBehaviour;
    }
}
