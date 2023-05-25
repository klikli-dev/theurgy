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
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

import java.util.Optional;

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

    private final CachedCheck recipeCachedCheck;
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

        this.recipeCachedCheck = new CachedCheck(RecipeTypeRegistry.DISTILLATION.get());
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
            //only even check for recipe if we have input to avoid unnecessary lookups
            var recipe = this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).orElse(null);

            //if we are lit and have a recipe, update progress
            if (isHeated && this.canCraft(recipe)) {
                ++this.progress;

                //if we hit max progress, craft the item and reset progress
                if (this.progress >= this.totalTime) {
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

        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper, this.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, false);
        this.inputInventory.extractItem(0, pRecipe.getIngredientCount(), false);

        return true;
    }

    private boolean canCraft(@Nullable DistillationRecipe pRecipe) {
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
        if (ItemHandlerHelper.canItemStacksStack(stack, this.inputInventory.getStackInSlot(0)))
            return true; //early out if we are already processing this type of item

        //now we use our custom cached check that ignores liquids:
        return this.recipeCachedCheck.getRecipeFor(stack, this.level).isPresent();
    }

    /**
     * A custom cached check
     */
    private static class CachedCheck implements RecipeManager.CachedCheck<RecipeWrapper, DistillationRecipe> {

        private final RecipeType<DistillationRecipe> type;
        private final RecipeManager.CachedCheck<RecipeWrapper, DistillationRecipe> internal;
        @Nullable
        private ResourceLocation lastRecipe;

        public CachedCheck(RecipeType<DistillationRecipe> type) {
            this.type = type;
            this.internal = RecipeManager.createCheck(type);
        }

        private Optional<Pair<ResourceLocation, DistillationRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

            var recipeManager = level.getRecipeManager();
            var map = recipeManager.byType(this.type);
            if (lastRecipe != null) {
                var recipe = map.get(lastRecipe);
                //test only the ingredient without the (separate) ingredient count check that the recipe.matches() would.
                if (recipe != null && recipe.getIngredient().test(stack)) {
                    return Optional.of(Pair.of(lastRecipe, recipe));
                }
            }

            return map.entrySet().stream().filter((entry) -> entry.getValue().getIngredient().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
        }

        /**
         * This checks only the ingredient, not the ingredient count
         */
        public Optional<DistillationRecipe> getRecipeFor(ItemStack stack, Level level) {
            var optional = this.getRecipeFor(stack, level, this.lastRecipe);
            if (optional.isPresent()) {
                var pair = optional.get();
                this.lastRecipe = pair.getFirst();
                return Optional.of(pair.getSecond());
            } else {
                return Optional.empty();
            }
        }

        /**
         * This checks full recipe validity: ingredients + ingredient count
         */
        @Override
        public Optional<DistillationRecipe> getRecipeFor(RecipeWrapper container, Level level) {
            var recipe = this.internal.getRecipeFor(container, level);
            if (recipe.isPresent()) {
                this.lastRecipe = recipe.get().getId();
            }

            return recipe;
        }
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
