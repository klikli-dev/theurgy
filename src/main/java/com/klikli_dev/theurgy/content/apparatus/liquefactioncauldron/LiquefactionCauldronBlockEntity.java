// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.heat.HeatConsumerBehaviour;
import com.klikli_dev.theurgy.content.capability.CraftingHeatReceiver;
import com.klikli_dev.theurgy.content.particle.ParticleColor;
import com.klikli_dev.theurgy.content.particle.coloredbubble.ColoredBubbleParticleProvider;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerWithFluidRecipeInput;
import com.klikli_dev.theurgy.content.render.HeldStackFitProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredFluidTank;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class LiquefactionCauldronBlockEntity extends BlockEntity implements HasCraftingBehaviour<ItemHandlerWithFluidRecipeInput, LiquefactionRecipe, LiquefactionCachedCheck>, HeldStackFitProvider {

    public CraftingHeatReceiver heatReceiver;

    public LiquefactionStorageBehaviour storageBehaviour;

    protected LiquefactionCraftingBehaviour craftingBehaviour;
    protected HeatConsumerBehaviour heatConsumerBehaviour;

    public LiquefactionCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(), pPos, pBlockState);


        this.storageBehaviour = new LiquefactionStorageBehaviour(this, () -> this.craftingBehaviour);

        this.heatReceiver = new CraftingHeatReceiver(this);

        this.craftingBehaviour = new LiquefactionCraftingBehaviour(this, () -> this.storageBehaviour.inputInventory, () -> this.storageBehaviour.outputInventory, () -> this.storageBehaviour.solventTank);
        this.heatConsumerBehaviour = new HeatConsumerBehaviour(this);
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

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide())
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    public void tickServer() {
        boolean isHeated = this.heatConsumerBehaviour.isHeated();
        boolean hasInput = !ItemUtil.getStack(this.storageBehaviour.inputInventory, 0).isEmpty();

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
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        if (this.level != null) {
            for (int i = 0; i < this.storageBehaviour.inventory.size(); i++) {
                Containers.dropItemStack(this.level, pPos.getX(), pPos.getY(), pPos.getZ(), ItemUtil.getStack(this.storageBehaviour.inventory, i));
            }
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
    public CraftingBehaviour<ItemHandlerWithFluidRecipeInput, LiquefactionRecipe, LiquefactionCachedCheck> craftingBehaviour() {
        return this.craftingBehaviour;
    }

    @Override
    public List<? extends SettableItemStorage> heldStackFitItemStorages() {
        return List.of(this.storageBehaviour.inputInventory);
    }

    @Override
    public List<? extends MonitoredFluidTank> heldStackFitFluidTanks() {
        return List.of(this.storageBehaviour.solventTank);
    }
}
