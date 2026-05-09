// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.incubator;

import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.heat.HeatConsumerBehaviour;
import com.klikli_dev.theurgy.content.behaviour.storage.StorageBehaviour;
import com.klikli_dev.theurgy.content.capability.CraftingHeatReceiver;
import com.klikli_dev.theurgy.content.recipe.IncubationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.IncubatorRecipeInput;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareCachedCheck;
import com.klikli_dev.theurgy.util.NetworkTagHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jetbrains.annotations.Nullable;


public class IncubatorBlockEntity extends BlockEntity implements Clearable, HasCraftingBehaviour<IncubatorRecipeInput, IncubationRecipe, LevelAwareCachedCheck<IncubatorRecipeInput, IncubationRecipe>> {
    public IncubatorMercuryVesselBlockEntity mercuryVessel;
    public IncubatorSulfurVesselBlockEntity sulfurVessel;
    public IncubatorSaltVesselBlockEntity saltVessel;

    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public MonitoredItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;

    public IncubatorRecipeInput ItemHandlerRecipeInput;

    public CraftingHeatReceiver heatReceiver;

    public boolean isValidMultiblock;

    protected IncubatorCraftingBehaviour craftingBehaviour;
    protected HeatConsumerBehaviour heatConsumerBehaviour;
    protected boolean checkValidMultiblockOnNextQuery;

    public IncubatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.INCUBATOR.get(), pPos, pBlockState);

        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);
        this.checkValidMultiblockOnNextQuery = true;

        this.heatReceiver = new CraftingHeatReceiver(this);

        this.craftingBehaviour = new IncubatorCraftingBehaviour(this, () -> this.ItemHandlerRecipeInput, () -> null, () -> this.outputInventory);
        this.heatConsumerBehaviour = new HeatConsumerBehaviour(this);
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide())
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
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
        input.child("outputInventory").ifPresent(tag -> this.outputInventory.deserialize(tag));

        this.craftingBehaviour.readNetwork(input);
    }

    public void writeNetwork(ValueOutput output) {
        ValueOutput outputInventoryOutput = output.child("outputInventory");
        this.outputInventory.serialize(outputInventoryOutput);
        if (outputInventoryOutput.isEmpty()) {
            output.discard("outputInventory");
        }

        this.craftingBehaviour.writeNetwork(output);
    }

    public void tickServer() {
        boolean isHeated = this.heatConsumerBehaviour.isHeated();

        boolean hasInput = this.isValidMultiblock()
                && !ItemUtil.getStack(this.mercuryVessel.inputInventory, 0).isEmpty()
                && !ItemUtil.getStack(this.saltVessel.inputInventory, 0).isEmpty()
                && !ItemUtil.getStack(this.sulfurVessel.inputInventory, 0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    public void tickClient() {
        var isProcessing = this.craftingBehaviour.isProcessing();
        if (isProcessing) {
            var random = this.getLevel().getRandom();
            if (random.nextFloat() < 0.11F) {
                for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                    this.getLevel().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            this.getBlockPos().getX() + 0.5 + random.nextFloat() / 3.0 * (random.nextBoolean() ? 1 : -1),
                            this.getBlockPos().getY() + 2 + random.nextFloat(),
                            this.getBlockPos().getZ() + 0.5 + random.nextFloat() / 3.0 * (random.nextBoolean() ? 1 : -1),
                            0, 0.07, 0
                    );
                }
            }
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        if (this.level != null) {
            for (int i = 0; i < this.outputInventory.size(); i++) {
                Containers.dropItemStack(this.level, pPos.getX(), pPos.getY(), pPos.getZ(), ItemUtil.getStack(this.outputInventory, i));
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

        this.writeNetwork(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        input.child("heatReceiver").ifPresent(this.heatReceiver::deserialize);

        this.readNetwork(input);
    }

    @Override
    public void clearContent() {
        StorageBehaviour.clearItemHandler(this.outputInventory);
    }

    private void checkForVessel(BlockPos pos) {
        BlockEntity blockEntity = this.level.getBlockEntity(pos);
        if (blockEntity instanceof IncubatorMercuryVesselBlockEntity mercury) {
            this.mercuryVessel = mercury;
            mercury.setIncubator(this);
        } else if (blockEntity instanceof IncubatorSulfurVesselBlockEntity sulfur) {
            this.sulfurVessel = sulfur;
            sulfur.setIncubator(this);
        } else if (blockEntity instanceof IncubatorSaltVesselBlockEntity salt) {
            this.saltVessel = salt;
            salt.setIncubator(this);
        }
    }

    public void validateMultiblock() {
        var oldMercuryVessel = this.mercuryVessel;
        var oldSaltVessel = this.saltVessel;
        var oldSulfurVessel = this.sulfurVessel;
        var wasValidMultiblock = this.isValidMultiblock;

        this.mercuryVessel = null;
        this.saltVessel = null;
        this.sulfurVessel = null;

        Direction.Plane.HORIZONTAL.stream().forEach(direction -> {
            BlockPos pos = this.getBlockPos().relative(direction);
            this.checkForVessel(pos);
        });

        this.isValidMultiblock = this.mercuryVessel != null && this.sulfurVessel != null && this.saltVessel != null;
        if (wasValidMultiblock != this.isValidMultiblock) {
            if (this.isValidMultiblock) {
                this.onAssembleMultiblock();
            } else {
                this.onDisassembleMultiblock();
            }
        }

        if (oldMercuryVessel != this.mercuryVessel || oldSaltVessel != this.saltVessel || oldSulfurVessel != this.sulfurVessel) {
            this.craftingBehaviour.onInputItemChanged(ItemStack.EMPTY, ItemStack.EMPTY);
        }
    }

    public void onAssembleMultiblock() {
        this.ItemHandlerRecipeInput = new IncubatorRecipeInput(this.mercuryVessel.inputInventory, this.saltVessel.inputInventory, this.sulfurVessel.inputInventory);
    }

    public void onDisassembleMultiblock() {
        this.ItemHandlerRecipeInput = null;
    }

    @Override
    public void setRemoved() {
        if (this.mercuryVessel != null) {
            this.mercuryVessel.setIncubator(null);
            this.mercuryVessel = null;
        }
        if (this.sulfurVessel != null) {
            this.sulfurVessel.setIncubator(null);
            this.sulfurVessel = null;
        }
        if (this.saltVessel != null) {
            this.saltVessel.setIncubator(null);
            this.saltVessel = null;
        }
        super.setRemoved();
    }

    public boolean isValidMultiblock() {
        if (this.checkValidMultiblockOnNextQuery) {
            this.checkValidMultiblockOnNextQuery = false;
            this.validateMultiblock();
        }
        return this.isValidMultiblock;
    }

    @Override
    public IncubatorCraftingBehaviour craftingBehaviour() {
        return this.craftingBehaviour;
    }

    public class OutputInventory extends MonitoredItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            IncubatorBlockEntity.this.sendBlockUpdated();
        }


        @Override
        protected void onContentsChanged(int slot) {
            IncubatorBlockEntity.this.setChanged();
        }
    }
}
