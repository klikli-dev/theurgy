// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.klikli_dev.theurgy.content.recipe.ingredient.FluidIngredient;
import com.klikli_dev.theurgy.content.recipe.wrapper.RecipeWrapperWithFluid;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.RecipeSerializerRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AccumulationRecipe implements Recipe<RecipeWrapperWithFluid> {
    public static final int DEFAULT_ACCUMULATION_TIME = 100;

    public static final Codec<AccumulationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    FluidIngredient.CODEC.fieldOf("evaporant").forGetter((r) -> r.evaporant),
                    Codec.INT.fieldOf("evaporantAmount").forGetter((r) -> r.evaporantAmount),
                    TheurgyExtraCodecs.INGREDIENT.optionalFieldOf("solute").forGetter(r -> Optional.ofNullable(r.solute)),
                    FluidStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("accumulationTime", DEFAULT_ACCUMULATION_TIME).forGetter(r -> r.accumulationTime)
            ).apply(instance, (evaporant, evaporantAmount, solute, result, accumulation_time) -> new AccumulationRecipe(evaporant, evaporantAmount, solute.orElse(null), result, accumulation_time))
    );
    /**
     * The fluid to evaporate to obtain the result.
     */
    protected final FluidIngredient evaporant;
    protected final int evaporantAmount;
    /**
     * The (optional) item to dissolve in the evaporant to obtain the result.
     */
    @Nullable
    protected final Ingredient solute;
    /**
     * The result of the recipe.
     */
    protected final FluidStack result;
    protected final int accumulationTime;
    protected ResourceLocation id;

    public AccumulationRecipe(FluidIngredient evaporant, int evaporantAmount, @Nullable Ingredient solute, FluidStack result, int pAccumulationTime) {
        this.evaporant = evaporant;
        this.evaporantAmount = evaporantAmount;
        this.solute = solute;
        this.result = result;
        this.accumulationTime = pAccumulationTime;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.ACCUMULATION.get();
    }

    @Override
    public boolean matches(RecipeWrapperWithFluid pContainer, Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        boolean evaporantMatches = this.evaporant.test(fluid) && fluid.getAmount() >= this.evaporantAmount;
        boolean soluteMatches =
                pContainer.getItem(0).isEmpty() && !this.hasSolute() || //if recipe requires no solute and container does not have one we're ok
                        this.hasSolute() && this.solute.test(pContainer.getItem(0)); // if recipe requires solute we check if the container has it

        //note: it is important that if the container HAS a solute but the recipe does not require one, we do not match -> otherwise water -> sal ammoniac recipes would always match, even if the faster water + sal ammoniac crystal -> sal ammoniac recipe is available

        return soluteMatches && evaporantMatches;
    }

    @Override
    public ItemStack assemble(RecipeWrapperWithFluid pInv, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    public FluidStack assembleFluid(RecipeWrapperWithFluid pInv, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        if (this.solute != null)
            nonnulllist.add(this.solute);
        return nonnulllist;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.ACCUMULATION.get();
    }

    public int getAccumulationTime() {
        return this.accumulationTime;
    }

    public FluidIngredient getEvaporant() {
        return this.evaporant;
    }

    public int getEvaporantAmount() {
        return this.evaporantAmount;
    }

    @Nullable
    public Ingredient getSolute() {
        return this.solute;
    }

    public boolean hasSolute() {
        return this.solute != null;
    }

    public FluidStack getResult() {
        return this.result;
    }

    public static class Serializer implements RecipeSerializer<AccumulationRecipe> {

        @Override
        public AccumulationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var recipe = CODEC.parse(JsonOps.INSTANCE, pJson).getOrThrow(false, s -> {
                throw new JsonParseException(s);
            });
            recipe.id = pRecipeId;
            return recipe;
        }

        @Override
        public AccumulationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var recipe = pBuffer.readJsonWithCodec(CODEC);
            recipe.id = pRecipeId;
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AccumulationRecipe pRecipe) {
            pBuffer.writeJsonWithCodec(CODEC, pRecipe);
        }
    }
}
