// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier;

import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.content.capability.HeatProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.util.ValueIOUtils;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PyromanticBrazierBlockEntity extends BlockEntity {
    public MonitoredItemStackHandler inventory;

    public HeatProvider heatProvider;

    private int remainingLitTime;

    public PyromanticBrazierBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.PYROMANTIC_BRAZIER.get(), pPos, pBlockState);

        this.inventory = new Inventory();
        this.heatProvider = () -> this.getBlockState().getValue(PyromanticBrazierBlock.LIT);
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide())
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return ValueIOUtils.serialize(pRegistries, this::writeNetwork);
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
        input.child("inventory").ifPresent(this.inventory::deserialize);
    }

    public void writeNetwork(ValueOutput output) {
        ValueOutput inventoryOutput = output.child("inventory");
        this.inventory.serialize(inventoryOutput);
        if (inventoryOutput.isEmpty()) {
            output.discard("inventory");
        }
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

    protected int getBurnDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            return pFuel.getBurnTime(RecipeTypeRegistry.PYROMANTIC_BRAZIER.get(), null);
        }
    }

    public boolean isLit() {
        return this.remainingLitTime > 0;
    }

    public void tickClient() {
//        RandomSource randomsource = this.level.random;
//        if (randomsource.nextFloat() < 0.11F) {
//            for (int i = 0; i < randomsource.nextInt(2) + 2; ++i) {
//                CampfireBlock.makeParticles(this.level, this.getBlockPos(), false, false);
//            }
//        }
    }

    public void tickServer() {
        boolean wasTurnedOnDuringThisTick = false;
        boolean wasLitAtBeginOfTick = this.isLit();
        if (wasLitAtBeginOfTick) {
            --this.remainingLitTime;
        }

        var fuelStack = this.inventory.getStackInSlot(0);
        boolean hasFuel = !fuelStack.isEmpty();

        //light the block / turn it on if we have fuel
        if (!this.isLit() && hasFuel) {
            this.remainingLitTime = this.getBurnDuration(fuelStack);

            if (this.isLit()) {
                wasTurnedOnDuringThisTick = true;
                //handle lava bucket
                if (fuelStack.getCraftingRemainder() != null)
                    this.inventory.setStackInSlot(0, fuelStack.getCraftingRemainder().create());
                    //handle all other fuel items
                else if (hasFuel) {
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                    }
                }
            }
        }


        if (wasLitAtBeginOfTick != this.isLit()) {
            wasTurnedOnDuringThisTick = true;
            var newState = this.getBlockState().setValue(CalcinationOvenBlock.LIT, this.isLit());
            this.level.setBlock(this.getBlockPos(), newState, 3);
        }

        if (wasTurnedOnDuringThisTick) {
            this.setChanged();
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putShort("remainingLitTime", (short) this.remainingLitTime);
        this.writeNetwork(output);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.remainingLitTime = input.getShortOr("remainingLitTime", (short) 0);
        this.readNetwork(input);
    }

    private class Inventory extends MonitoredItemStackHandler {
        public Inventory() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return PyromanticBrazierBlockEntity.this.getBurnDuration(stack) > 0;
        }

        @Override
        protected void onContentTypeChanged(int slot, ItemStack oldStack, ItemStack newStack) {
            //we also need to network sync our BE, because if the content type changes then the interaction behaviour client side changes
            PyromanticBrazierBlockEntity.this.sendBlockUpdated();
        }


        @Override
        protected void onContentsChanged(int slot) {
            PyromanticBrazierBlockEntity.this.setChanged();
        }
    }
}
