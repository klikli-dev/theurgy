/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ShapedRecipeProvider extends JsonRecipeProvider {

    public ShapedRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "crafting/shaped");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        //Divination Rods
        this.makeRecipe("divination_rod_t1", new RecipeBuilder(
                ItemRegistry.DIVINATION_ROD_T1.get(), 1, this.makeDivinationRodSettings(ItemRegistry.DIVINATION_ROD_T1.get()))
                .pattern(" GR")
                .pattern(" RG")
                .pattern("R  ")
                .define('G', Tags.Items.GLASS)
                .define('R', Tags.Items.RODS_WOODEN)
        );

        this.makeRecipe("divination_rod_t2", new RecipeBuilder(
                ItemRegistry.DIVINATION_ROD_T2.get(), 1, this.makeDivinationRodSettings(ItemRegistry.DIVINATION_ROD_T2.get()))
                .pattern(" GM")
                .pattern(" AG")
                .pattern("R  ")
                .define('G', Tags.Items.GLASS)
                .define('R', Tags.Items.RODS_WOODEN)
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('M', Tags.Items.NUGGETS_GOLD)
        );

        this.makeRecipe("divination_rod_t3", new RecipeBuilder(
                ItemRegistry.DIVINATION_ROD_T3.get(), 1, this.makeDivinationRodSettings(ItemRegistry.DIVINATION_ROD_T3.get()))
                .pattern(" GD")
                .pattern(" QG")
                .pattern("A  ")
                .define('G', Tags.Items.GLASS)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('A', Tags.Items.GEMS_AMETHYST)
        );

        this.makeRecipe("divination_rod_t4", new RecipeBuilder(
                ItemRegistry.DIVINATION_ROD_T4.get(), 1, this.makeDivinationRodSettings(ItemRegistry.DIVINATION_ROD_T4.get()))
                .pattern(" GM")
                .pattern(" RG")
                .pattern("A  ")
                .define('G', Tags.Items.GLASS)
                .define('R', Tags.Items.RODS_BLAZE)
                .define('M', Tags.Items.ORES_NETHERITE_SCRAP)
                .define('A', Tags.Items.GEMS_AMETHYST)
        );
    }

    public JsonObject makeDivinationRodSettings(DivinationRodItem rodItem) {
        return this.makeDivinationRodSettings(rodItem.defaultTier, rodItem.defaultAllowedBlocksTag, rodItem.defaultDisallowedBlocksTag, rodItem.defaultRange, rodItem.defaultDuration, rodItem.defaultDurability);
    }

    public JsonObject makeDivinationRodSettings(Tier defaultTier, TagKey<Block> defaultAllowedBlocksTag, TagKey<Block> defaultDisallowedBlocksTag, int defaultRange, int defaultDuration, int defaultDurability) {
        JsonObject settings = new JsonObject();

        settings.addProperty(TheurgyConstants.Nbt.Divination.SETTING_TIER, TierSortingRegistry.getName(defaultTier).toString());
        settings.addProperty(TheurgyConstants.Nbt.Divination.SETTING_ALLOWED_BLOCKS_TAG, defaultAllowedBlocksTag.location().toString());
        settings.addProperty(TheurgyConstants.Nbt.Divination.SETTING_DISALLOWED_BLOCKS_TAG, defaultDisallowedBlocksTag.location().toString());
        settings.addProperty(TheurgyConstants.Nbt.Divination.SETTING_RANGE, defaultRange);
        settings.addProperty(TheurgyConstants.Nbt.Divination.SETTING_DURATION, defaultDuration);
        settings.addProperty(TheurgyConstants.Nbt.Divination.SETTING_DURABILITY, defaultDurability);

        return settings;
    }

    public void makeRecipe(String name, RecipeBuilder recipe) {
        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    @Override
    public String getName() {
        return "Shaped Crafting Recipes";
    }

    private class RecipeBuilder {

        JsonObject recipe;

        public RecipeBuilder(ItemLike result) {
            this(result, 1);
        }

        public RecipeBuilder(ItemLike result, int count) {
            this(result, count, null);
        }

        public RecipeBuilder(ItemLike result, int count, @Nullable JsonObject nbt) {
            this(ShapedRecipeProvider.this.makeItemResult(ShapedRecipeProvider.this.locFor(result), count, nbt));
        }

        public RecipeBuilder(JsonObject result) {
            this.recipe = new JsonObject();
            this.recipe.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(RecipeSerializer.SHAPED_RECIPE).toString());
            this.recipe.add("result", result);
            this.recipe.add("key", new JsonObject());
            this.recipe.add("pattern", new JsonArray());
        }

        public RecipeBuilder define(char key, TagKey<Item> tag) {
            return this.define(key, ShapedRecipeProvider.this.makeTagIngredient(ShapedRecipeProvider.this.locFor(tag)));
        }

        public RecipeBuilder define(char key, ItemLike item) {
            return this.define(key, ShapedRecipeProvider.this.makeItemIngredient(ShapedRecipeProvider.this.locFor(item)));
        }

        public RecipeBuilder define(char key, JsonObject ingredient) {
            this.recipe.getAsJsonObject("key").add(String.valueOf(key), ingredient);
            return this;
        }

        public RecipeBuilder pattern(String pattern) {
            this.recipe.getAsJsonArray("pattern").add(pattern);
            return this;
        }

        public JsonObject build() {
            return this.recipe;
        }

    }
}
