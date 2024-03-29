// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CatalysationRecipe implements Recipe<RecipeWrapper> {
    public static final int DEFAULT_MERCURY_FLUX_PER_TICK = 20;

    public static final Codec<CatalysationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    TheurgyExtraCodecs.INGREDIENT.fieldOf("ingredient").forGetter(r -> r.ingredient),
                    Codec.INT.fieldOf("totalMercuryFlux").forGetter(r -> r.totalMercuryFlux),
                    Codec.INT.fieldOf("mercuryFluxPerTick").forGetter(r -> r.mercuryFluxPerTick)
            ).apply(instance, CatalysationRecipe::new)
    );
    protected final Ingredient ingredient;

    /**
     * The amount of mercury flux to generate per tick, until the total amount has been reached
     */
    protected final int mercuryFluxPerTick;

    /**
     * The total amount of mercury flux generated by consuming the ingredient.
     */
    protected final int totalMercuryFlux;
    protected ResourceLocation id;


    public CatalysationRecipe(Ingredient ingredient, int totalMercuryFlux, int mercuryFluxPerTick) {
        this.ingredient = ingredient;
        this.totalMercuryFlux = totalMercuryFlux;
        this.mercuryFluxPerTick = mercuryFluxPerTick;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.CATALYSATION.get();
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getMercuryFluxPerTick() {
        return this.mercuryFluxPerTick;
    }

    public int getTotalMercuryFlux() {
        return this.totalMercuryFlux;
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        var stack = pContainer.getItem(0);
        return this.ingredient.test(stack);
    }

    @Override
    public ItemStack assemble(RecipeWrapper pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
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
        return NonNullList.of(Ingredient.EMPTY, this.ingredient);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.MERCURY_CATALYST.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.CATALYSATION.get();
    }


    public static class Serializer implements RecipeSerializer<CatalysationRecipe> {

        @Override
        public CatalysationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var recipe = CODEC.parse(JsonOps.INSTANCE, pJson).getOrThrow(false, s -> {
                throw new JsonParseException(s);
            });
            recipe.id = pRecipeId;
            return recipe;
        }

        @Override
        public CatalysationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var recipe = pBuffer.readJsonWithCodec(CODEC);
            recipe.id = pRecipeId;
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CatalysationRecipe pRecipe) {
            pBuffer.writeJsonWithCodec(CODEC, pRecipe);
        }
    }
}
