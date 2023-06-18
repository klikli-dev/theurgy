/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.calcinationoven;

import com.klikli_dev.theurgy.content.block.HeatConsumer;
import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.util.wrapper.PreventInsertWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
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

public class CalcinationOvenBlockEntity extends BlockEntity implements GeoBlockEntity, HeatConsumer {

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
    private final RecipeManager.CachedCheck<RecipeWrapper, ? extends CalcinationRecipe> recipeCachedCheck;
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
    public RecipeWrapper inputRecipeWrapper;
    private int progress;
    private int totalTime;

    private boolean heatedCache;

    private boolean isRunning;

    /**
     * Client-side we only use the blockstate to determine our animation state.
     */
    private boolean wasLitLastTick;

    public CalcinationOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALCINATION_OVEN.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();
        this.outputInventoryTakeOnlyWrapper = new PreventInsertWrapper(this.outputInventory) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return false;
            }
        };

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventoryTakeOnlyWrapper);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventoryTakeOnlyWrapper);

        this.inputRecipeWrapper = new RecipeWrapper(this.inputInventory);

        this.recipeCachedCheck = RecipeManager.createCheck(RecipeTypeRegistry.CALCINATION.get());
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
        this.isRunning = tag.contains("isRunning") && tag.getBoolean("isRunning");
    }

    public void writeNetwork(CompoundTag tag) {
        tag.putBoolean("isRunning", this.isRunning);
    }

    @Override
    public boolean getHeatedCache() {
        return this.heatedCache;
    }

    @Override
    public void setHeatedCache(boolean heated) {
        this.heatedCache = heated;
    }

    public void tickServer() {
        boolean isHeated = this.isHeated();
        boolean hasInput = !this.inputInventory.getStackInSlot(0).isEmpty();

        this.tickCrafting(isHeated, hasInput);
    }

    protected void tickCrafting(boolean isHeated, boolean hasInput){
        if (hasInput) {
            //only even check for recipe if we have input to avoid unnecessary lookups
            var recipe = this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).orElse(null);

            //if we are lit and have a recipe, update progress
            if (isHeated && this.canCraft(recipe)) {
                this.tryStartRunning();

                this.progress++;

                this.tryFinishCrafting(recipe);
            } else {
                this.stopRunning();
            }
        } else {
            this.stopRunning();
        }
    }

    protected void tryFinishCrafting(CalcinationRecipe pRecipe){
        //if we hit max progress, craft the item and reset progress
        if (this.progress >= this.totalTime) {
            this.progress = 0;
            this.totalTime = this.getTotalTime();

            this.craft(pRecipe);

            //currently we do not set isRunning to false here, because if we have more input we do not interrupt the client side visuals. If we do not have more input, we set it to false in the next tick anyway.
            this.sendBlockUpdated();
        }
    }

    protected void tryStartRunning(){
        //if we are not running, start running
        if (this.progress == 0) {
            this.isRunning = true;
            this.sendBlockUpdated();
        }

        //we don't have to worry about total time here, as it is set when an item is put into the inventory.
    }

    protected void stopRunning(){
        //only do state updates if we actually changed something
        if (this.progress != 0 || this.isRunning) {
            this.isRunning = false;
            this.progress = 0;
            this.sendBlockUpdated();
            this.setChanged();
        }

        //we don't have to worry about total time here, as it is set when an item is put into the inventory.
    }

    protected void sendBlockUpdated() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_INVISIBLE);
    }

    private boolean canCraft(@Nullable CalcinationRecipe pRecipe) {
        if (pRecipe == null)
            return false;

        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper, this.getLevel().registryAccess());
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var remainingStack = ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, true);
            return remainingStack.isEmpty(); //only allow crafting if we have room for the full output
        }
    }

    private boolean craft(@Nullable CalcinationRecipe pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper, this.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, false);
        this.inputInventory.extractItem(0, pRecipe.getIngredientCount(), false);

        return true;
    }

    protected int getTotalTime() {
        return this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).map(CalcinationRecipe::getCalcinationTime).orElse(CalcinationRecipe.DEFAULT_CALCINATION_TIME);
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
        pTag.putShort("progress", (short) this.progress);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("progress"))
            this.progress = pTag.getShort("progress");
    }

    private <E extends GeoBlockEntity> PlayState animationHandler(AnimationState<E> event) {

        var blockState = this.getBlockState();
        var isRunning = this.isRunning;
//        var isLit = blockState.getValue(BlockStateProperties.LIT);

        if (!this.wasLitLastTick && !isRunning && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(PLACE_AND_OFF_ANIM);
        } else if (this.wasLitLastTick && !isRunning && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(STOP_AND_OFF_ANIM);
        } else if (!this.wasLitLastTick && isRunning && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(START_AND_ON_ANIM);
        } else if (!this.wasLitLastTick && !isRunning && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(OFF_ANIM);
        } else if (this.wasLitLastTick && isRunning && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(ON_ANIM);
        }

        this.wasLitLastTick = isRunning;

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

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tempInv = new ItemStackHandler(1);
        tempInv.setStackInSlot(0, stack);
        RecipeWrapper tempRecipeWrapper = new RecipeWrapper(tempInv);

        return this.recipeCachedCheck.getRecipeFor(tempRecipeWrapper, this.level).isPresent();
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
                CalcinationOvenBlockEntity.this.totalTime = CalcinationOvenBlockEntity.this.getTotalTime();
                CalcinationOvenBlockEntity.this.progress = 0;
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return CalcinationOvenBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
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

        //We are using ItemStackHandler to insert results into the output inv, so we can't block here!
        //Consequence is that pipes can pipe in ..
//        @Override
//        public boolean isItemValid(int slot, ItemStack stack) {
//            return false;
//        }

        @Override
        protected void onContentsChanged(int slot) {
            CalcinationOvenBlockEntity.this.setChanged();
        }
    }
}
