// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiConsumer;

public class DistillationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = DistillationRecipe.DEFAULT_TIME;

    public DistillationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "distillation");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        this.makeMercuryShardRecipe(1, Tags.Items.STONES, 10);
        this.makeMercuryShardRecipe(1, Tags.Items.SANDSTONE_BLOCKS, 10);
        this.makeMercuryShardRecipe(1, ItemTags.STONE_BRICKS, 10);
        this.makeMercuryShardRecipe(1, Tags.Items.COBBLESTONES, 10);
        this.makeMercuryShardRecipe(1, Tags.Items.GRAVELS, 15);
        this.makeMercuryShardRecipe(1, ItemTags.DIRT, 25);
        this.makeMercuryShardRecipe(1, Tags.Items.SANDS, 15);
        this.makeMercuryShardRecipe(1, Tags.Items.GLASS_BLOCKS, 10);

        this.makeMercuryShardRecipe(5, ItemTagRegistry.LOW_MERCURY_ORES, 1);
        this.makeMercuryShardRecipe(25, ItemTagRegistry.MEDIUM_MERCURY_ORES, 1);
        this.makeMercuryShardRecipe(50, ItemTagRegistry.HIGH_MERCURY_ORES, 1);

        this.makeMercuryShardRecipe(1, ItemTagRegistry.LOW_MERCURY_RAW_MATERIALS, 1);
        this.makeMercuryShardRecipe(5, ItemTagRegistry.MEDIUM_MERCURY_RAW_MATERIALS, 1);
        this.makeMercuryShardRecipe(10, ItemTagRegistry.HIGH_MERCURY_RAW_MATERIALS, 1);

        this.makeMercuryShardRecipe(1, ItemTagRegistry.LOW_MERCURY_METALS, 1);
        this.makeMercuryShardRecipe(5, ItemTagRegistry.MEDIUM_MERCURY_METALS, 1);
        this.makeMercuryShardRecipe(10, ItemTagRegistry.HIGH_MERCURY_METALS, 1);

        this.makeMercuryShardRecipe(1, ItemTagRegistry.LOW_MERCURY_GEMS, 1);
        this.makeMercuryShardRecipe(5, ItemTagRegistry.MEDIUM_MERCURY_GEMS, 1);
        this.makeMercuryShardRecipe(10, ItemTagRegistry.HIGH_MERCURY_GEMS, 1);

        this.makeMercuryShardRecipe(1, ItemTagRegistry.LOW_MERCURY_OTHER_MINERALS, 1);
        this.makeMercuryShardRecipe(5, ItemTagRegistry.MEDIUM_MERCURY_OTHER_MINERALS, 1);
        this.makeMercuryShardRecipe(10, ItemTagRegistry.HIGH_MERCURY_OTHER_MINERALS, 1);

        this.makeMercuryShardRecipe(1, Tags.Items.CROPS, 3);
        this.makeMercuryShardRecipe(1, Tags.Items.DYES, 3);
        this.makeMercuryShardRecipe(1, Items.PORKCHOP, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_PORKCHOP, 1);
        this.makeMercuryShardRecipe(1, Items.BEEF, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_BEEF, 1);
        this.makeMercuryShardRecipe(1, Items.CHICKEN, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_CHICKEN, 1);
        this.makeMercuryShardRecipe(1, Items.COD, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_COD, 1);
        this.makeMercuryShardRecipe(1, Items.SALMON, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_SALMON, 1);
        this.makeMercuryShardRecipe(1, Items.RABBIT, 2);
        this.makeMercuryShardRecipe(1, Items.COOKED_RABBIT, 1);
        this.makeMercuryShardRecipe(1, Items.BREAD, 1);
        this.makeMercuryShardRecipe(1, ItemTags.FLOWERS, 3);
        this.makeMercuryShardRecipe(1, ItemTags.SAPLINGS, 3);
        this.makeMercuryShardRecipe(1, ItemTags.LEAVES, 5);
        this.makeMercuryShardRecipe(1, ItemTags.LOGS, 2);
        this.makeMercuryShardRecipe(1, ItemTags.PLANKS, 8);
        this.makeMercuryShardRecipe(1, ItemTags.WOOL, 1);

    }

    public void makeMercuryShardRecipe(int resultCount, TagKey<Item> ingredient, int ingredientCount) {
        this.makeMercuryShardRecipe(resultCount, ingredient, ingredientCount, TIME);
    }

    public void makeMercuryShardRecipe(int resultCount, TagKey<Item> ingredient, int ingredientCount, int distillationTime) {
        this.makeMercuryShardRecipe(ingredient.location().getPath().replace("/", "."), resultCount, ingredient, ingredientCount, distillationTime);
    }

    public void makeMercuryShardRecipe(String recipeName, int resultCount, TagKey<Item> ingredient, int ingredientCount, int distillationTime) {
        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                new Builder(new ItemStack(ItemRegistry.MERCURY_SHARD.get(), resultCount))
                        .sizedIngredient(ingredient, ingredientCount)
                        .time(distillationTime)
                        .build());
    }

    public void makeMercuryShardRecipe(int resultCount, Item ingredient, int ingredientCount) {
        this.makeMercuryShardRecipe(resultCount, ingredient, ingredientCount, TIME);
    }

    public void makeMercuryShardRecipe(int resultCount, Item ingredient, int ingredientCount, int distillationTime) {
        this.makeMercuryShardRecipe(BuiltInRegistries.ITEM.getKey(ingredient).getPath(), resultCount, ingredient, ingredientCount, distillationTime);
    }

    public void makeMercuryShardRecipe(String recipeName, int resultCount, Item ingredient, int ingredientCount, int distillationTime) {
        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                new Builder(new ItemStack(ItemRegistry.MERCURY_SHARD.get(), resultCount))
                        .sizedIngredient(ingredient, ingredientCount)
                        .time(distillationTime)
                        .build());
    }

    @Override
    public String getName() {
        return "Distillation Recipes";
    }

    protected static class Builder extends RecipeBuilder<Builder> {
        protected Builder(ItemStack result) {
            super(RecipeTypeRegistry.DISTILLATION);
            this.result(result);
            this.time(TIME);
        }
    }
}
