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
import com.github.klikli_dev.theurgy.common.crafting.recipe.PurificationRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.ReplicationRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.TransmutationRecipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class CrucibleRecipeBuilder {
    //region Fields
    public final IRecipeSerializer<?> serializer;
    public final Ingredient result;
    public final int count;
    public final List<ItemStack> essentia = new ArrayList<>();
    public Ingredient ingredient;
    public String group;
    public String folder;
    public String recipeName;
    //endregion Fields

    //region Initialization

    public CrucibleRecipeBuilder(IRecipeSerializer<?> serializer, Ingredient result, int count) {
        this.result = result;
        this.serializer = serializer;
        this.count = count;
    }
    //endregion Initialization

    //region Getter / Setter
    public CrucibleRecipeBuilder setRecipeName(String recipeName) {
        this.recipeName = recipeName;
        return this;
    }
    public CrucibleRecipeBuilder setGroup(String groupIn) {
        this.group = groupIn;
        return this;
    }
    public CrucibleRecipeBuilder setFolder(String folder) {
        this.folder = folder;
        return this;
    }
    //endregion Getter / Setter

    //region Static Methods
    public static CrucibleRecipeBuilder transmutation(Ingredient result, int count) {
        return new CrucibleRecipeBuilder(TransmutationRecipe.SERIALIZER, result, count).setFolder("transmutation");
    }
    public static CrucibleRecipeBuilder transmutation(ITag<Item> result, int count) {
        return transmutation(Ingredient.fromTag(result), count);
    }
    public static CrucibleRecipeBuilder transmutation(IItemProvider result, int count) {
        return transmutation(Ingredient.fromItems(result), count);
    }

    public static CrucibleRecipeBuilder purification(Ingredient result, int count) {
        return new CrucibleRecipeBuilder(PurificationRecipe.SERIALIZER, result, count).setFolder("purification");
    }
    public static CrucibleRecipeBuilder purification(ITag<Item> result, int count) {
        return purification(Ingredient.fromTag(result), count);
    }
    public static CrucibleRecipeBuilder purification(IItemProvider result, int count) {
        return purification(Ingredient.fromItems(result), count);
    }

    public static CrucibleRecipeBuilder replication(Ingredient result, int count) {
        return new CrucibleRecipeBuilder(ReplicationRecipe.SERIALIZER, result, count).setFolder("replication");
    }
    public static CrucibleRecipeBuilder replication(ITag<Item> result, int count) {
        return replication(Ingredient.fromTag(result), count);
    }
    public static CrucibleRecipeBuilder replication(IItemProvider result, int count) {
        return replication(Ingredient.fromItems(result), count);
    }
    //endregion Static Methods

    //region Methods
    public CrucibleRecipeBuilder essentia(List<ItemStack> essentia) {
        this.essentia.addAll(essentia);
        return this;
    }

    public CrucibleRecipeBuilder essentia(IItemProvider essentia, int count) {
        this.essentia.add(new ItemStack(essentia, count));
        return this;
    }

    public CrucibleRecipeBuilder ingredient(ITag<Item> ingredient) {
        return this.ingredient(Ingredient.fromTag(ingredient));
    }

    public CrucibleRecipeBuilder ingredient(IItemProvider ingredient) {
        return this.ingredient(Ingredient.fromItems(ingredient));
    }

    public CrucibleRecipeBuilder ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public CrucibleRecipeBuilder build(Consumer<IFinishedRecipe> consumerIn) {
        return this.build(consumerIn, this.recipeName);
    }

    public CrucibleRecipeBuilder build(Consumer<IFinishedRecipe> consumerIn, Item recipeNameItem) {
        ResourceLocation itemLocation = recipeNameItem.getRegistryName();
        return this.build(consumerIn,  itemLocation.getPath());
    }

    public CrucibleRecipeBuilder build(Consumer<IFinishedRecipe> consumerIn, String recipeName) {
        ResourceLocation recipeLocation = new ResourceLocation(Theurgy.MODID, this.folder
                                                                              + "/" + recipeName);
        return this.build(consumerIn, recipeLocation);
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     */
    public CrucibleRecipeBuilder build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(
                new CrucibleRecipeBuilder.Result(id, this.serializer, this.group == null ? "" : this.group,
                        this.result, this.count, this.essentia, this.ingredient));
        return this;
    }
    //endregion Methods

    public class Result implements IFinishedRecipe {

        //region Fields
        private final ResourceLocation id;
        private final IRecipeSerializer<?> serializer;
        private final Ingredient result;
        private final int count;
        private final List<ItemStack> essentia;
        private final Ingredient ingredient;
        private final String group;
        //endregion Fields

        //region Initialization
        public Result(ResourceLocation id, IRecipeSerializer<?> serializer, String group, Ingredient result,
                      int count, List<ItemStack> essentia, Ingredient ingredient) {
            this.id = id;
            this.serializer = serializer;
            this.group = group;
            this.result = result;
            this.count = count;
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

            JsonObject resultJson = (JsonObject) this.result.serialize();
            resultJson.addProperty("count", this.count);

            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return this.serializer;
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
