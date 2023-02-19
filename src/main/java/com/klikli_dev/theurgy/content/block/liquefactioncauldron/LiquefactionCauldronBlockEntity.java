/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.liquefactioncauldron;

import com.klikli_dev.theurgy.content.block.HeatConsumer;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class LiquefactionCauldronBlockEntity extends BlockEntity implements HeatConsumer {

    private final CachedCheck<RecipeWrapperWithFluid, ? extends LiquefactionRecipe> recipeCachedCheck;
    public ItemStackHandler inputInventory;
    public ItemStackHandler outputInventory;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public RecipeWrapperWithFluid inputRecipeWrapper;
    public LazyOptional<IFluidHandler> solventTankCapability;
    protected FluidTank solventTank;
    int liquificationProgress;
    int liquificationTotalTime;

    boolean heatedCache;

    public LiquefactionCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventory);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventory);

        this.solventTank = new FluidTank(FluidType.BUCKET_VOLUME, (fluidStack ->
                ForgeRegistries.FLUIDS.tags().getTag(FluidTagRegistry.SOLVENT).contains(fluidStack.getFluid())
        ));
        this.solventTankCapability = LazyOptional.of(() -> this.solventTank);

        this.inputRecipeWrapper = new RecipeWrapperWithFluid(this.inputInventory, this.solventTank);

        this.recipeCachedCheck = this.createCheck(RecipeTypeRegistry.LIQUEFACTION.get());
    }

    public static CachedCheck<RecipeWrapperWithFluid, ? extends LiquefactionRecipe> createCheck(final RecipeType<LiquefactionRecipe> type) {

        var internal = RecipeManager.createCheck(type);
        // our custom cached check has an additional method checks only the ingredient, bypassing the fluid check, which we need for checking if an item can be added
        return new CachedCheck<RecipeWrapperWithFluid, LiquefactionRecipe>() {

            @Nullable
            private ResourceLocation lastRecipe;

            public Optional<Pair<ResourceLocation, LiquefactionRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

                var recipeManager = level.getRecipeManager();
                Map<ResourceLocation, LiquefactionRecipe> map = recipeManager.byType(type);
                if (lastRecipe != null) {
                    var recipe = map.get(lastRecipe);
                    if (recipe != null && recipe.getIngredient().test(stack)) {
                        return Optional.of(Pair.of(lastRecipe, recipe));
                    }
                }

                return map.entrySet().stream().filter((entry) -> entry.getValue().getIngredient().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
            }

            @Override
            public Optional<LiquefactionRecipe> getRecipeFor(ItemStack stack, Level level) {
                var optional = this.getRecipeFor(stack, level, this.lastRecipe);
                if (optional.isPresent()) {
                    var pair = optional.get();
                    this.lastRecipe = pair.getFirst();
                    return Optional.of(pair.getSecond());
                } else {
                    return Optional.empty();
                }
            }

            @Override
            public Optional<LiquefactionRecipe> getRecipeFor(RecipeWrapperWithFluid container, Level level) {
                var recipe = internal.getRecipeFor(container, level);
                if (recipe.isPresent()) {
                    this.lastRecipe = recipe.get().getId();
                }

                return recipe;
            }
        };
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
            LiquefactionRecipe recipe;
            if (hasInput) {
                recipe = this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).orElse(null);
            } else {
                recipe = null;
            }

            //if we are lit and have a recipe, update progress
            if (isHeated && this.canCraft(recipe)) {
                ++this.liquificationProgress;

                //if we hit max progress, craft the item and reset progress
                if (this.liquificationProgress == this.liquificationTotalTime) {
                    this.liquificationProgress = 0;
                    this.liquificationTotalTime = this.getTotalLiquefactionTime();
                    this.craft(recipe);
                    //TODO: advancement?
                }
            } else {
                this.liquificationProgress = 0;
            }
        }
    }

    private boolean canCraft(@Nullable LiquefactionRecipe pRecipe) {
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

    private boolean craft(@Nullable LiquefactionRecipe pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var inputStack = this.inputInventory.getStackInSlot(0);
        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper);
        var outputStack = this.outputInventory.getStackInSlot(0);
        if (outputStack.isEmpty()) {
            this.inputInventory.setStackInSlot(0, assembledStack.copy());
        } else if (outputStack.is(assembledStack.getItem())) {
            outputStack.grow(assembledStack.getCount());
        }

        inputStack.shrink(1);
        this.solventTank.drain(pRecipe.getSolvent().getAmount(), IFluidHandler.FluidAction.EXECUTE);
        return true;

    }

    protected int getTotalLiquefactionTime() {
        return this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).map(LiquefactionRecipe::getLiquefactionTime).orElse(LiquefactionRecipe.DEFAULT_LIQUEFACTION_TIME);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return this.inputInventoryCapability.cast();
            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
            return this.inventoryCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return this.solventTankCapability.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.putShort("liquificationProgress", (short) this.liquificationProgress);
        var solventTankTag = new CompoundTag();
        this.solventTank.writeToNBT(pTag);
        pTag.put("solventTank", solventTankTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("liquificationProgress"))
            this.liquificationProgress = pTag.getShort("liquificationProgress");

        if (pTag.contains("solventTank")) {
            var solventTankTag = pTag.getCompound("solventTank");
            this.solventTank.readFromNBT(solventTankTag);
        }
    }

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tempInv = new ItemStackHandler(1);
        tempInv.setStackInSlot(0, stack);

        if (ItemHandlerHelper.canItemStacksStack(stack, this.inputInventory.getStackInSlot(0)))
            return true; //early out if we are already processing this type of item

        //now we use our custom cached check that ignores liquids:
        return this.recipeCachedCheck.getRecipeFor(stack, this.level).isPresent();
    }

    public interface CachedCheck<C extends Container, T extends Recipe<C>> extends RecipeManager.CachedCheck<C, T> {
        Optional<T> getRecipeFor(ItemStack stack, Level level);
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
                LiquefactionCauldronBlockEntity.this.liquificationTotalTime = LiquefactionCauldronBlockEntity.this.getTotalLiquefactionTime();
                LiquefactionCauldronBlockEntity.this.liquificationProgress = 0;
                LiquefactionCauldronBlockEntity.this.setChanged();
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return LiquefactionCauldronBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
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
    }
}