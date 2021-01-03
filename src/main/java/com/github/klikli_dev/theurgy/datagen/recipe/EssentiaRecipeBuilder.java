/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.datagen.recipe;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.crafting.recipe.EssentiaRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.TransmutationRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class EssentiaRecipeBuilder {
    //region Fields
    public final List<ItemStack> essentia = new ArrayList<>();
    public Ingredient ingredient;
    public String group;
    public String recipeName;
    //endregion Fields

    //region Getter / Setter
    public EssentiaRecipeBuilder setGroup(String groupIn) {
        this.group = groupIn;
        return this;
    }
    public EssentiaRecipeBuilder setRecipeName(String recipeName) {
        this.recipeName = recipeName;
        return this;
    }
    //endregion Getter / Setter

    //region Static Methods
    public static EssentiaRecipeBuilder create() {
        return new EssentiaRecipeBuilder();
    }
    //endregion Static Methods

    //region Methods
    public EssentiaRecipeBuilder essentia(List<ItemStack> essentia) {
        essentia.forEach(stack -> this.essentia(stack.getItem(), stack.getCount()));
        return this;
    }

    public EssentiaRecipeBuilder essentia(IItemProvider essentia, int count) {
        Optional<ItemStack> existing = this.essentia.stream()
                                               .filter(stack -> stack.getItem() == essentia)
                                               .findFirst();
        if(existing.isPresent()){
            existing.get().setCount(existing.get().getCount() + count);
        } else {
            this.essentia.add(new ItemStack(essentia, count));
        }
        return this;
    }

    public EssentiaRecipeBuilder ingredient(ITag<Item> ingredient) {
        return this.ingredient(Ingredient.fromTag(ingredient));
    }

    public EssentiaRecipeBuilder ingredient(IItemProvider ingredient) {
        return this.ingredient(Ingredient.fromItems(ingredient));
    }

    public EssentiaRecipeBuilder ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public EssentiaRecipeBuilder build(Consumer<IFinishedRecipe> consumerIn) {
        return this.build(consumerIn, this.recipeName);
    }

    public EssentiaRecipeBuilder build(Consumer<IFinishedRecipe> consumerIn, Item recipeNameItem) {
        ResourceLocation itemLocation = recipeNameItem.getRegistryName();
        return this.build(consumerIn, itemLocation.getPath());
    }

    public EssentiaRecipeBuilder build(Consumer<IFinishedRecipe> consumerIn, String recipeName) {
        ResourceLocation recipeLocation = new ResourceLocation(Theurgy.MODID, "essentia/" + recipeName);
        return this.build(consumerIn, recipeLocation);
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     */
    public EssentiaRecipeBuilder build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(
                new EssentiaRecipeBuilder.Result(id, this.group == null ? "" : this.group,
                        this.essentia, this.ingredient));
        return this;
    }
    //endregion Methods

    public class Result implements IFinishedRecipe {

        //region Fields
        private final ResourceLocation id;
        private final List<ItemStack> essentia;
        private final Ingredient ingredient;
        private final String group;
        //endregion Fields

        //region Initialization
        public Result(ResourceLocation id, String group, List<ItemStack> essentia, Ingredient ingredient) {
            this.id = id;
            this.group = group;
            this.essentia = essentia;
            this.ingredient = ingredient;
        }
        //endregion Initialization

        //region Overrides
        @Override
        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.add("ingredient", this.ingredient.serialize());

            JsonArray essentiaJson = new JsonArray();
            for (ItemStack essentiaStack : this.essentia) {
                JsonObject essentiaStackjson = new JsonObject();
                essentiaStackjson.addProperty("item", ForgeRegistries.ITEMS.getKey(essentiaStack.getItem()).toString());
                essentiaStackjson.addProperty("count", essentiaStack.getCount());
                essentiaJson.add(essentiaStackjson);
            }

            json.add("essentia", essentiaJson);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return EssentiaRecipe.SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
        //endregion Overrides
    }
}
