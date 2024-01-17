// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.pyromanticbrazier;

import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.content.capability.HeatProvider;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PyromanticBrazierBlockEntity extends BlockEntity {
    public ItemStackHandler inventory;

    public HeatProvider heatProvider;

    private int remainingLitTime;

    public PyromanticBrazierBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.PYROMANTIC_BRAZIER.get(), pPos, pBlockState);

        this.inventory = new Inventory();
        this.heatProvider = () -> this.getBlockState().getValue(PyromanticBrazierBlock.LIT);
    }

    public void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
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

    public void readNetwork(CompoundTag pTag) {
        if (pTag.contains("inventory"))
            this.inventory.deserializeNBT(pTag.getCompound("inventory"));
    }

    public void writeNetwork(CompoundTag pTag) {
        pTag.put("inventory", this.inventory.serializeNBT());
    }

    protected int getBurnDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            return CommonHooks.getBurnTime(pFuel, RecipeTypeRegistry.PYROMANTIC_BRAZIER.get());
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
                if (fuelStack.hasCraftingRemainingItem())
                    this.inventory.setStackInSlot(0, fuelStack.getCraftingRemainingItem());
                    //handle all other fuel items
                else if (hasFuel) {
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        this.inventory.setStackInSlot(0, fuelStack.getCraftingRemainingItem());
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
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.putShort("remainingLitTime", (short) this.remainingLitTime);

        this.writeNetwork(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("remainingLitTime"))
            this.remainingLitTime = pTag.getShort("remainingLitTime");

        this.readNetwork(pTag);
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
