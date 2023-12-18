// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.datagen.SulfurMappings;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class ReformationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = ReformationRecipe.DEFAULT_REFORMATION_TIME;

    public ReformationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "reformation");
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        SulfurMappings.METALS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT, 50);
        });
        //2 other minerals for 1 metal
        SulfurMappings.METALS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(2, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT), 50);
        });
        //1 gem for 4 metal
        SulfurMappings.METALS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 4, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT, 50);
        });

        SulfurMappings.METALS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
        });
        //2 other minerals for 1 metal
        SulfurMappings.METALS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(2, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON), 100);
        });
        //1 gem for 4 metal
        SulfurMappings.METALS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 4, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON, 100);
        });

        SulfurMappings.METALS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE, 150);
        });
        //2 other minerals for 1 metal
        SulfurMappings.METALS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(2, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE), 150);
        });
        //1 gem for 4 metal
        SulfurMappings.METALS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 4, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE, 150);
        });

        SulfurMappings.METALS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS, 200);
        });
        //2 other minerals for 1 metal
        SulfurMappings.METALS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(2, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS), 200);
        });
        //1 gem for 4 metal
        SulfurMappings.METALS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 4, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS, 200);
        });

        SulfurMappings.GEMS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT, 50);
        });
        //8 other minerals for 1 gem
        SulfurMappings.GEMS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(8, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT), 50);
        });
        //4 metals for 1 gem
        SulfurMappings.GEMS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(4, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT), 50);
        });

        SulfurMappings.GEMS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON, 100);
        });
        //8 other minerals for 1 gem
        SulfurMappings.GEMS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(8, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON), 100);
        });
        //4 metals for 1 gem
        SulfurMappings.GEMS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(4, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON), 100);
        });

        SulfurMappings.GEMS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE, 150);
        });
        //8 other minerals for 1 gem
        SulfurMappings.GEMS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(8, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE), 150);
        });
        //4 metals for 1 gem
        SulfurMappings.GEMS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(4, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE), 150);
        });

        SulfurMappings.GEMS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS, 200);
        });
        //8 other minerals for 1 gem
        SulfurMappings.GEMS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(8, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS), 200);
        });
        //4 metals for 1 gem
        SulfurMappings.GEMS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, Collections.nCopies(4, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS), 200);
        });

        SulfurMappings.OTHER_MINERALS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT, 50);
        });
        //1 metal for 2 other minerals
        SulfurMappings.OTHER_MINERALS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 2, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT, 50);
        });
        //1 gem for 8 other minerals
        SulfurMappings.OTHER_MINERALS_ABUNDANT.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 8, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT, 50);
        });

        SulfurMappings.OTHER_MINERALS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON, 100);
        });

        //1 metal for 2 other minerals
        SulfurMappings.OTHER_MINERALS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 2, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON, 100);
        });

        //1 gem for 8 other minerals
        SulfurMappings.OTHER_MINERALS_COMMON.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 8, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON, 100);
        });

        SulfurMappings.OTHER_MINERALS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE, 150);
        });

        //1 metal for 2 other minerals
        SulfurMappings.OTHER_MINERALS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 2, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE, 150);
        });

        //1 gem for 8 other minerals
        SulfurMappings.OTHER_MINERALS_RARE.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 8, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE, 150);
        });

        SulfurMappings.OTHER_MINERALS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS, 200);
        });

        //1 metal for 2 other minerals
        SulfurMappings.OTHER_MINERALS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 2, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS, 200);
        });

        //1 gem for 8 other minerals
        SulfurMappings.OTHER_MINERALS_PRECIOUS.forEach((sulfur) -> {
            this.makeRecipe(sulfur, 8, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS, 200);
        });
    }

    public void makeRecipe(Item result, TagKey<Item> source, int mercuryFlux) {
        this.makeRecipe(result, 1, source, mercuryFlux);
    }

    public void makeRecipe(Item result, int resultCount, TagKey<Item> source, int mercuryFlux) {
        this.makeRecipe(this.name(result) + "_from_" + this.name(source), result, resultCount, List.of(source), mercuryFlux, TIME);
    }

    public void makeRecipe(Item result, List<TagKey<Item>> sources, int mercuryFlux) {
        this.makeRecipe(result, 1, sources, mercuryFlux);
    }

    public void makeRecipe(Item result, int resultCount, List<TagKey<Item>> sources, int mercuryFlux) {
        this.makeRecipe(this.name(result) + "_from_" + this.name(sources), result, resultCount, sources, mercuryFlux, TIME);
    }


    public void makeRecipe(String recipeName, Item result, int resultCount, List<TagKey<Item>> sources, int mercuryFlux, int reformationTime) {
        var recipe = this.makeRecipeJson(
                sources.stream().map(s -> this.makeTagIngredient(this.locFor(s))).toList(),
                this.makeItemIngredient(this.locFor(result)),
                mercuryFlux,
                this.makeItemStackCodecResult(this.locFor(result), resultCount),
                reformationTime);

        var conditions = new JsonArray();
        for (var source : sources){
            conditions.add(this.makeTagNotEmptyCondition(source.location().toString()));
        }
        recipe.add("conditions", conditions);

        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                recipe
        );
    }

    public void makeRecipe(Item result, Item source, int mercuryFlux) {
        this.makeRecipe(this.name(result) + "_from_" + this.name(source), result, source, 1, mercuryFlux, TIME);
    }

    public void makeRecipe(String recipeName, Item result, Item source, int mercuryFlux) {
        this.makeRecipe(recipeName, result, source, 1, mercuryFlux, TIME);
    }

    public void makeRecipe(String recipeName, Item result, Item source, int mercuryFlux, int reformationTime) {
        this.makeRecipe(recipeName, result, source, 1, mercuryFlux, reformationTime);
    }

    public void makeRecipe(String recipeName, Item result, Item source, int sourceCount, int mercuryFlux, int reformationTime) {
        this.recipeConsumer.accept(
                this.modLoc(recipeName),
                this.makeRecipeJson(
                        List.of(this.makeItemIngredient(this.locFor(source), sourceCount)),
                        this.makeItemIngredient(this.locFor(result)),
                        mercuryFlux,
                        this.makeItemStackCodecResult(this.locFor(result)),
                        reformationTime));
    }

    public JsonObject makeRecipeJson(List<JsonObject> sources, JsonObject target, int mercuryFlux, JsonObject result, int reformationTime) {
        var sourcesArray = new JsonArray();
        for (var source : sources) {
            sourcesArray.add(source);
        }

        var recipe = new JsonObject();
        recipe.addProperty("type", RecipeTypeRegistry.REFORMATION.getId().toString());
        recipe.add("sources", sourcesArray);
        recipe.add("target", target);
        recipe.addProperty("mercury_flux", mercuryFlux);
        recipe.add("result", result);
        recipe.addProperty("reformation_time", reformationTime);
        return recipe;
    }

    @Override
    public String getName() {
        return "Reformation Recipes";
    }
}
