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
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class FermentationRecipe implements Recipe<RecipeWrapperWithFluid> {
    public static final int DEFAULT_TIME = 200;

    public static final Codec<FermentationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    FluidIngredient.CODEC.fieldOf("fluid").forGetter((r) -> r.fluid),
                    Codec.INT.fieldOf("fluidAmount").forGetter((r) -> r.fluidAmount),
                    TheurgyExtraCodecs.INGREDIENT.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients),
                    ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
                    Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(r -> r.time)
            ).apply(instance, FermentationRecipe::new)
    );
    protected final FluidIngredient fluid;
    protected final int fluidAmount;

    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    protected final int time;
    private final boolean hasOnlySimpleIngredients;
    protected ResourceLocation id;

    public FermentationRecipe(FluidIngredient fluid, int fluidAmount, List<Ingredient> ingredients, ItemStack result, int time) {
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
        this.ingredients = ingredients.stream().collect(NonNullList::create, NonNullList::add, NonNullList::addAll);
        this.hasOnlySimpleIngredients = ingredients.stream().allMatch(Ingredient::isSimple);
        this.result = result;
        this.time = time;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.FERMENTATION.get();
    }

    @Override
    public boolean matches(RecipeWrapperWithFluid pContainer, Level pLevel) {
        var fluid = pContainer.getTank().getFluidInTank(0);
        var fluidMatches = this.fluid.test(fluid) && fluid.getAmount() >= this.fluidAmount;
        if (!fluidMatches)
            return false;

        //logic from shapeless recipe to match ingredients without double-dipping
        var stackedcontents = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        int containerItemsCount = 0;

        for (int j = 0; j < pContainer.getContainerSize(); ++j) {
            var itemstack = pContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                containerItemsCount++;
                if (this.hasOnlySimpleIngredients)
                    stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        if (containerItemsCount != this.ingredients.size())
            return false;

        return this.hasOnlySimpleIngredients ?
                stackedcontents.canCraft(this, null) :
                net.neoforged.neoforge.common.util.RecipeMatcher.findMatches(inputs, this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(RecipeWrapperWithFluid pInv, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.FERMENTATION_VAT.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.FERMENTATION.get();
    }

    public FluidIngredient getFluid() {
        return this.fluid;
    }

    public int getFluidAmount() {
        return this.fluidAmount;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getTime() {
        return this.time;
    }

    public static class Serializer implements RecipeSerializer<FermentationRecipe> {

        @Override
        public FermentationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            var recipe = CODEC.parse(JsonOps.INSTANCE, pJson).getOrThrow(false, s -> {
                throw new JsonParseException(s);
            });
            recipe.id = pRecipeId;
            return recipe;
        }

        @Override
        public FermentationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var recipe = pBuffer.readJsonWithCodec(CODEC);
            recipe.id = pRecipeId;
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, FermentationRecipe pRecipe) {
            pBuffer.writeJsonWithCodec(CODEC, pRecipe);
        }
    }
}
