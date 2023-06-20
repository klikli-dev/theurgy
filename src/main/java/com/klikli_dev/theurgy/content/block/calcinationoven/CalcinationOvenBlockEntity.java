/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.calcinationoven;

import com.klikli_dev.theurgy.content.block.behaviour.HeatedBehaviour;
import com.klikli_dev.theurgy.content.block.itemhandler.PreventInsertWrapper;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CalcinationOvenBlockEntity extends BlockEntity implements GeoBlockEntity {

    private static final RawAnimation START_AND_ON_ANIM = RawAnimation.begin()
            .thenPlay("animation.calcination_oven.start")
            .thenLoop("animation.calcination_oven.on");
    private static final RawAnimation STOP_AND_OFF_ANIM = RawAnimation.begin()
            .thenPlay("animation.calcination_oven.stop")
            .thenLoop("animation.calcination_oven.off");
    private static final RawAnimation OFF_ANIM = RawAnimation.begin()
            .thenLoop("animation.calcination_oven.off");
    private static final RawAnimation ON_ANIM = RawAnimation.begin()
            .thenLoop("animation.calcination_oven.on");

    private static final RawAnimation PLACE_AND_OFF_ANIM = RawAnimation.begin()
            .thenPlay("animation.calcination_oven.place")
            .thenLoop("animation.calcination_oven.off");
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private final CalcinationCraftingBehaviour craftingBehaviour;
    private final HeatedBehaviour heatedBehaviour;

    public ItemStackHandler inputInventory;
    /**
     * The underlying outputInventory which allows inserting too - we use this when crafting.
     */
    public ItemStackHandler outputInventory;
    /**
     * A wrapper that only allows taking from the outputInventory - this is what we show to the outside.
     */
    public PreventInsertWrapper outputInventoryTakeOnlyWrapper;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    /**
     * Client-side we only use the blockstate to determine our animation state.
     */
    private boolean wasProcessingLastTick;


    public CalcinationOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALCINATION_OVEN.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory);

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryTakeOnlyWrapper);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventoryTakeOnlyWrapper);

        this.craftingBehaviour = new CalcinationCraftingBehaviour(this, () -> this.inputInventory, () -> this.outputInventory);
        this.heatedBehaviour = new HeatedBehaviour(this);
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

    public void readNetwork(CompoundTag tag) {
        this.craftingBehaviour.readNetwork(tag);
    }

    public void writeNetwork(CompoundTag tag) {
        this.craftingBehaviour.writeNetwork(tag);
    }

    public void tickServer() {
        boolean isHeated = this.heatedBehaviour.isHeated();
        boolean hasInput = !this.inputInventory.getStackInSlot(0).isEmpty();

        this.craftingBehaviour.tickServer(isHeated, hasInput);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return this.inputInventoryCapability.cast();
            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
            return this.inventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());

        this.craftingBehaviour.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));

        this.craftingBehaviour.load(pTag);
    }

    private <E extends GeoBlockEntity> PlayState animationHandler(AnimationState<E> event) {

        var isProcessing = this.craftingBehaviour.isProcessing();

        if (!this.wasProcessingLastTick && !isProcessing && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(PLACE_AND_OFF_ANIM);
        } else if (this.wasProcessingLastTick && !isProcessing && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(STOP_AND_OFF_ANIM);
        } else if (!this.wasProcessingLastTick && isProcessing && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(START_AND_ON_ANIM);
        } else if (!this.wasProcessingLastTick && !isProcessing && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(OFF_ANIM);
        } else if (this.wasProcessingLastTick && isProcessing && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(ON_ANIM);
        }

        this.wasProcessingLastTick = isProcessing;

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoBlockEntity>(this, "controller", 10, this::animationHandler));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public class InputInventory extends ItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack newStack) {

            var oldStack = this.getStackInSlot(slot);

            boolean sameItem = !newStack.isEmpty() && ItemStack.isSameItemSameTags(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                CalcinationOvenBlockEntity.this.craftingBehaviour.onInputItemChanged(oldStack, newStack);
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return CalcinationOvenBlockEntity.this.craftingBehaviour.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            CalcinationOvenBlockEntity.this.setChanged();
        }
    }

    public class OutputInventory extends ItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        protected void onContentsChanged(int slot) {
            CalcinationOvenBlockEntity.this.setChanged();
        }
    }
}
