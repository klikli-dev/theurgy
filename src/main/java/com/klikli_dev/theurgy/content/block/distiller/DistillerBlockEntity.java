/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.distiller;

import com.klikli_dev.theurgy.content.block.HeatConsumer;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
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

public class DistillerBlockEntity extends BlockEntity implements GeoBlockEntity, HeatConsumer {

    private static final RawAnimation START_AND_ON_ANIM = RawAnimation.begin()
            .thenPlay("animation.distiller.start")
            .thenLoop("animation.distiller.on");
    private static final RawAnimation STOP_AND_OFF_ANIM = RawAnimation.begin()
            .thenPlay("animation.distiller.stop")
            .thenLoop("animation.distiller.off");
    private static final RawAnimation OFF_ANIM = RawAnimation.begin()
            .thenLoop("animation.distiller.off");
    private static final RawAnimation ON_ANIM = RawAnimation.begin()
            .thenLoop("animation.distiller.on");
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    private final RecipeManager.CachedCheck<RecipeWrapper, ? extends DistillationRecipe> recipeCachedCheck;
    public ItemStackHandler inputInventory;
    public ItemStackHandler outputInventory;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;
    public RecipeWrapper inputRecipeWrapper;
    int progress;
    int totalTime;

    boolean heatedCache;
    /**
     * Client-side we only use the blockstate to determine our animation state.
     */
    boolean wasLitLastTick;

    public DistillerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DISTILLER.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventory);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventory);

        this.inputRecipeWrapper = new RecipeWrapper(this.inputInventory);

        this.recipeCachedCheck = RecipeManager.createCheck(RecipeTypeRegistry.DISTILLATION.get());
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

        if (hasInput) {
            //only even check for recipe if we have fuel and input or are currently burning to avoid unnecessary lookups
            DistillationRecipe recipe;
            if (hasInput) {
                recipe = this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).orElse(null);
            } else {
                recipe = null;
            }

            //if we are lit and have a recipe, update progress
            if (isHeated && this.canCraft(recipe)) {
                ++this.progress;

                //if we hit max progress, craft the item and reset progress
                if (this.progress == this.totalTime) {
                    this.progress = 0;
                    this.totalTime = this.getTotalTime();
                    this.craft(recipe);
                    //TODO: advancement?
                }
            } else {
                this.progress = 0;
            }
        }
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

    private boolean craft(@Nullable DistillationRecipe pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var inputStack = this.inputInventory.getStackInSlot(0);
        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper);
        var outputStack = this.outputInventory.getStackInSlot(0);
        if (outputStack.isEmpty()) {
            this.outputInventory.setStackInSlot(0, assembledStack.copy());
        } else if (outputStack.is(assembledStack.getItem())) {
            outputStack.grow(assembledStack.getCount());
        }

        inputStack.shrink(pRecipe.getIngredientCount());
        return true;

    }

    private boolean canCraft(@Nullable DistillationRecipe pRecipe) {
        if (pRecipe == null)
            return false;

        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper);
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var outputStack = this.outputInventory.getStackInSlot(0);
            if (outputStack.isEmpty()) {
                return true;
            } else if (!outputStack.sameItem(assembledStack)) {
                return false;
            } else if (outputStack.getCount() + assembledStack.getCount() <= this.outputInventory.getSlotLimit(0)
                    && outputStack.getCount() + assembledStack.getCount() <= outputStack.getMaxStackSize()) {
                return true;
            } else {
                return outputStack.getCount() + assembledStack.getCount() <= assembledStack.getMaxStackSize();
            }
        }

    }

    protected int getTotalTime() {
        return this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).map(DistillationRecipe::getDistillationTime).orElse(DistillationRecipe.DEFAULT_DISTILLATION_TIME);
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
        var isLit = blockState.getValue(BlockStateProperties.LIT);

        if (this.wasLitLastTick && !isLit && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(STOP_AND_OFF_ANIM);
        }

        if (!this.wasLitLastTick && isLit && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(START_AND_ON_ANIM);
        }

        if (!this.wasLitLastTick && !isLit && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(OFF_ANIM);
        }

        if (this.wasLitLastTick && isLit && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(ON_ANIM);
        }


        this.wasLitLastTick = isLit;

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoBlockEntity>(this, "controller", 0, this::animationHandler));
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

            boolean sameItem = !newStack.isEmpty() && newStack.sameItem(oldStack) && ItemStack.tagMatches(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                DistillerBlockEntity.this.totalTime = DistillerBlockEntity.this.getTotalTime();
                DistillerBlockEntity.this.progress = 0;
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return DistillerBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            DistillerBlockEntity.this.setChanged();
        }
    }

    public class OutputInventory extends ItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            DistillerBlockEntity.this.setChanged();
        }
    }
}
