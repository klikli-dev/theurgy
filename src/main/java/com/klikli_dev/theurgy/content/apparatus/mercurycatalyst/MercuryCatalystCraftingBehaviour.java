// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.mercurycatalyst;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.LevelAwareCachedCheck;
import com.klikli_dev.theurgy.content.capability.MercuryFluxHandler;
import com.klikli_dev.theurgy.content.recipe.CatalysationRecipe;
import com.klikli_dev.theurgy.content.recipe.input.ItemHandlerRecipeInput;
import com.klikli_dev.theurgy.content.storage.MonitoredItemStackHandler;
import com.klikli_dev.theurgy.content.storage.SettableItemStorage;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class MercuryCatalystCraftingBehaviour extends CraftingBehaviour<ItemHandlerRecipeInput, CatalysationRecipe, LevelAwareCachedCheck<ItemHandlerRecipeInput, CatalysationRecipe>> {

    private final Supplier<MercuryFluxHandler> mercuryFluxHandlerSupplier;
    private final MonitoredItemStackHandler ingredientCheckInventory = new MonitoredItemStackHandler() {
    };
    private final ItemHandlerRecipeInput ingredientCheckInput = new ItemHandlerRecipeInput(this.ingredientCheckInventory);

    protected int mercuryFluxToConvert;
    protected int totalMercuryFluxToConvert; // Total flux for this conversion cycle (for progress calculation)
    protected int currentMercuryFluxPerTick;


    public MercuryCatalystCraftingBehaviour(BlockEntity blockEntity, Supplier<SettableItemStorage> inputInventorySupplier, Supplier<SettableItemStorage> outputInventorySupplier, Supplier<MercuryFluxHandler> mercuryFluxHandlerSupplier) {
        super(blockEntity,
                Lazy.of(() -> new ItemHandlerRecipeInput(inputInventorySupplier.get())),
                inputInventorySupplier,
                outputInventorySupplier,
                new LevelAwareCachedCheck<>(RecipeTypeRegistry.CATALYSATION.get()));

        this.mercuryFluxHandlerSupplier = mercuryFluxHandlerSupplier;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        var level = this.blockEntity.getLevel();
        if (level == null) {
            return false;
        }

        this.ingredientCheckInventory.set(0, ItemResource.of(stack.copyWithCount(1)), stack.copyWithCount(1).getCount());

        return this.recipeCachedCheck.getRecipeFor(this.ingredientCheckInput, level).isPresent();
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        output.putInt("mercuryFluxToConvert", this.mercuryFluxToConvert);
        output.putInt("totalMercuryFluxToConvert", this.totalMercuryFluxToConvert);
        output.putInt("currentMercuryFluxPerTick", this.currentMercuryFluxPerTick);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        this.mercuryFluxToConvert = input.getIntOr("mercuryFluxToConvert", 0);
        this.totalMercuryFluxToConvert = input.getIntOr("totalMercuryFluxToConvert", 0);
        this.currentMercuryFluxPerTick = input.getIntOr("currentMercuryFluxPerTick", 0);
    }

    @Override
    public void applyImplicitComponents(DataComponentGetter pComponentGetter) {
        Integer mercuryFluxToConvert = pComponentGetter.get(DataComponentRegistry.MERCURY_FLUX_TO_CONVERT.get());
        Integer currentMercuryFluxPerTick = pComponentGetter.get(DataComponentRegistry.CURRENT_MERCURY_FLUX_PER_TICK.get());

        if (mercuryFluxToConvert != null)
            this.mercuryFluxToConvert = mercuryFluxToConvert;

        if (currentMercuryFluxPerTick != null)
            this.currentMercuryFluxPerTick = currentMercuryFluxPerTick;
    }

    @Override
    public void collectImplicitComponents(DataComponentMap.Builder pComponents) {
        pComponents.set(DataComponentRegistry.MERCURY_FLUX_TO_CONVERT, this.mercuryFluxToConvert);
        pComponents.set(DataComponentRegistry.CURRENT_MERCURY_FLUX_PER_TICK, this.currentMercuryFluxPerTick);
    }

    @Override
    protected int getIngredientCount(RecipeHolder<CatalysationRecipe> recipe) {
        return 1;
    }

    @Override
    protected int getCraftingTime(RecipeHolder<CatalysationRecipe> recipe) {
        return -1;
    }

    @Override
    protected int getDefaultCraftingTime() {
        return -1;
    }

    @Override
    public boolean canCraft(@Nullable RecipeHolder<CatalysationRecipe> pRecipe) {
        if (pRecipe == null) return false;

        var handler = this.mercuryFluxHandlerSupplier.get();
        // Check if there's any room available to start the process
        return handler.getAmountAsInt() < handler.getCapacityAsInt();
    }

    @Override
    public void tickServer(boolean canProcess, boolean hasInput) {
        //first see if we have leftover flux to convert
        if (this.mercuryFluxToConvert > 0) {
            if (canProcess) {
                this.tryStartProcessing(); // Mark as processing for HUD
                var handler = this.mercuryFluxHandlerSupplier.get();
                var maxFluxToConvert = Math.min(this.mercuryFluxToConvert, this.currentMercuryFluxPerTick);
                // Use addInternalFlux for internal crafting flux generation
                int fluxAdded = ((MercuryCatalystBlockEntity.MercuryCatalystMercuryFluxHandler) handler).addInternalFlux(maxFluxToConvert);
                this.mercuryFluxToConvert -= fluxAdded;
            }
        } else if (hasInput) {
            //only even check for recipe if we have input to avoid unnecessary lookups

            //if we have no flux available, consume more mercury
            var level = this.blockEntity.getLevel();
            if (level == null || level.isClientSide()) return;
            var recipe = this.recipeCachedCheck.getRecipeFor(this.recipeInputSupplier.get(), level).orElse(null);


            this.couldCraftLastTick = this.canCraft(recipe);

            //if we are lit and have a recipe, update progress
            if (canProcess && this.couldCraftLastTick) {
                this.craft(recipe);
            }
        } else {
            // No input, stop processing
            this.stopProcessing();
        }
    }

    @Override
    protected boolean craft(@Nullable RecipeHolder<CatalysationRecipe> pRecipe) {
        this.mercuryFluxToConvert = pRecipe.value().totalMercuryFlux();
        this.totalMercuryFluxToConvert = this.mercuryFluxToConvert; // Track total for progress calculation
        this.currentMercuryFluxPerTick = pRecipe.value().mercuryFluxPerTick();

        try (var tx = Transaction.openRoot()) {
            var input = this.inputInventorySupplier.get();
            var resource = input.getResource(0);
            input.extract(0, resource, this.getIngredientCount(pRecipe), tx);
            tx.commit();
        }

        return true;
    }

    @Override
    public int progressPercent() {
        if (this.totalMercuryFluxToConvert <= 0) {
            return 0;
        }
        // Calculate progress based on how much flux has been processed vs total
        int processed = this.totalMercuryFluxToConvert - this.mercuryFluxToConvert;
        return Math.clamp(processed * 100L / this.totalMercuryFluxToConvert, 0, 100);
    }
}
