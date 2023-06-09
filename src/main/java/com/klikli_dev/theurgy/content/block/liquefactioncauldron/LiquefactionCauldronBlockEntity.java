/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.liquefactioncauldron;

import com.klikli_dev.theurgy.content.block.HeatConsumer;
import com.klikli_dev.theurgy.content.recipe.LiquefactionRecipe;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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

import java.util.Optional;

public class LiquefactionCauldronBlockEntity extends BlockEntity implements HeatConsumer {

    private final CachedCheck recipeCachedCheck;
    public ItemStackHandler inputInventory;
    public ItemStackHandler outputInventory;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public RecipeWrapperWithFluid inputRecipeWrapper;
    public LazyOptional<IFluidHandler> solventTankCapability;
    public FluidTank solventTank;
    int progress;
    int totalTime;

    boolean heatedCache;

    public LiquefactionCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventory);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventory);

        this.solventTank = new FluidTank(FluidType.BUCKET_VOLUME, (fluidStack -> ForgeRegistries.FLUIDS.tags().getTag(FluidTagRegistry.SOLVENT).contains(fluidStack.getFluid()))) {
            @Override
            protected void onContentsChanged() {
                LiquefactionCauldronBlockEntity.this.setChanged();
                LiquefactionCauldronBlockEntity.this.sendUpdatePacket();
            }
        };
        this.solventTankCapability = LazyOptional.of(() -> this.solventTank);

        this.inputRecipeWrapper = new RecipeWrapperWithFluid(this.inputInventory, this.solventTank);

        this.recipeCachedCheck = new CachedCheck(RecipeTypeRegistry.LIQUEFACTION.get());
    }

    @Override
    public boolean getHeatedCache() {
        return this.heatedCache;
    }

    @Override
    public void setHeatedCache(boolean heated) {
        this.heatedCache = heated;
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();

        //we only sync liquid on update tag
        var solventTankTag = new CompoundTag();
        this.solventTank.writeToNBT(solventTankTag);
        tag.put("solventTank", solventTankTag);

        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void sendUpdatePacket() {
        if (this.level != null && !this.level.isClientSide)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2 | 4 | 16);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag.contains("solventTank")) {
            var solventTankTag = tag.getCompound("solventTank");
            this.solventTank.readFromNBT(solventTankTag);
        }
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

    private boolean canCraft(@Nullable LiquefactionRecipe pRecipe) {
        if (pRecipe == null) return false;

        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper, this.getLevel().registryAccess());
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var remainingStack = ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, true);
            return remainingStack.isEmpty(); //only allow crafting if we have room for the full output
        }
    }

    private boolean craft(@Nullable LiquefactionRecipe pRecipe) {
        if (!this.canCraft(pRecipe)) return false;

        var assembledStack = pRecipe.assemble(this.inputRecipeWrapper, this.getLevel().registryAccess());

        // Safely insert the assembledStack into the outputInventory and update the input stack.
        ItemHandlerHelper.insertItemStacked(this.outputInventory, assembledStack, false);

        this.inputInventory.extractItem(0, 1, false);
        this.solventTank.drain(pRecipe.getSolvent().getAmount(), IFluidHandler.FluidAction.EXECUTE);

        return true;
    }

    protected int getTotalTime() {
        return this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).map(LiquefactionRecipe::getLiquefactionTime).orElse(LiquefactionRecipe.DEFAULT_LIQUEFACTION_TIME);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return this.inputInventoryCapability.cast();
            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
            return this.inventoryCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER) return this.solventTankCapability.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.putShort("progress", (short) this.progress);
        var solventTankTag = new CompoundTag();
        this.solventTank.writeToNBT(solventTankTag);
        pTag.put("solventTank", solventTankTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory")) this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory")) this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("progress")) this.progress = pTag.getShort("progress");

        if (pTag.contains("solventTank")) {
            var solventTankTag = pTag.getCompound("solventTank");
            this.solventTank.readFromNBT(solventTankTag);
        }
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
    private static class CachedCheck implements RecipeManager.CachedCheck<RecipeWrapperWithFluid, LiquefactionRecipe> {

        private final RecipeType<LiquefactionRecipe> type;
        private final RecipeManager.CachedCheck<RecipeWrapperWithFluid, LiquefactionRecipe> internal;
        @Nullable
        private ResourceLocation lastRecipe;

        public CachedCheck(RecipeType<LiquefactionRecipe> type) {
            this.type = type;
            this.internal = RecipeManager.createCheck(type);
        }

        private Optional<Pair<ResourceLocation, LiquefactionRecipe>> getRecipeFor(ItemStack stack, Level level, @Nullable ResourceLocation lastRecipe) {

            var recipeManager = level.getRecipeManager();
            var map = recipeManager.byType(this.type);
            if (lastRecipe != null) {
                var recipe = map.get(lastRecipe);
                //test only the ingredient without the (separate) solvent fluid ingredient check that the recipe.matches() would.
                if (recipe != null && recipe.getIngredient().test(stack)) {
                    return Optional.of(Pair.of(lastRecipe, recipe));
                }
            }

            return map.entrySet().stream().filter((entry) -> entry.getValue().getIngredient().test(stack)).findFirst().map((entry) -> Pair.of(entry.getKey(), entry.getValue()));
        }

        /**
         * This only checks ingredients, not fluids
         */
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

        /**
         * This checks full recipe validity: ingredients + fluids
         */
        @Override
        public Optional<LiquefactionRecipe> getRecipeFor(RecipeWrapperWithFluid container, Level level) {
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

            boolean sameItem = !newStack.isEmpty() && ItemStack.isSameItemSameTags(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                LiquefactionCauldronBlockEntity.this.totalTime = LiquefactionCauldronBlockEntity.this.getTotalTime();
                LiquefactionCauldronBlockEntity.this.progress = 0;
                LiquefactionCauldronBlockEntity.this.setChanged();
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return LiquefactionCauldronBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            LiquefactionCauldronBlockEntity.this.setChanged();
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
            LiquefactionCauldronBlockEntity.this.setChanged();
        }
    }
}
