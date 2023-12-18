// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.datagen.SulfurMappings;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ReformationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = ReformationRecipe.DEFAULT_REFORMATION_TIME;
    private final Map<List<AlchemicalSulfurItem>, Integer> fluxPerTier = Map.of(
            SulfurMappings.ABUNDANT, 50,
            SulfurMappings.COMMON, 100,
            SulfurMappings.RARE, 150,
            SulfurMappings.PRECIOUS, 200
    );


    public ReformationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "reformation");
    }

    private int getFlux(AlchemicalSulfurItem sulfur) {
        return this.fluxPerTier.keySet().stream().filter(s -> s.contains(sulfur)).findFirst().map(this.fluxPerTier::get).orElse(-1);
    }

    private void makeXtoXRecipes(List<Pair<List<AlchemicalSulfurItem>, TagKey<Item>>> sulfurToTag) {
        sulfurToTag.forEach((entry) -> {
            entry.getFirst().forEach((sulfur) -> {
                this.makeRecipe(sulfur, entry.getSecond(), this.getFlux(sulfur));
            });
        });
    }

    /**
     * Convert N of the tag to 1 sulfur
     */
    private void makeNYtoXRecipes(int n, List<Pair<List<AlchemicalSulfurItem>, TagKey<Item>>> sulfurToTag) {
        sulfurToTag.forEach((entry) -> {
            entry.getFirst().forEach((sulfur) -> {
                this.makeRecipe(sulfur, Collections.nCopies(n, entry.getSecond()), this.getFlux(sulfur));
            });
        });
    }

    /**
     * Convert 1 of the tag to N sulfur
     */
    private void makeYtoNXRecipes(int n, List<Pair<List<AlchemicalSulfurItem>, TagKey<Item>>> sulfurToTag) {
        sulfurToTag.forEach((entry) -> {
            entry.getFirst().forEach((sulfur) -> {
                this.makeRecipe(sulfur, n, entry.getSecond(), this.getFlux(sulfur));
            });
        });
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        var metalsFromMetals = List.of(
                Pair.of(SulfurMappings.METALS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT),
                Pair.of(SulfurMappings.METALS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON),
                Pair.of(SulfurMappings.METALS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE),
                Pair.of(SulfurMappings.METALS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS)
        );
        this.makeXtoXRecipes(metalsFromMetals);

        var metalsFromOtherMinerals = List.of(
                Pair.of(SulfurMappings.METALS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT),
                Pair.of(SulfurMappings.METALS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON),
                Pair.of(SulfurMappings.METALS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE),
                Pair.of(SulfurMappings.METALS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS)
        );
        this.makeNYtoXRecipes(2, metalsFromOtherMinerals);

        var metalsFromGems = List.of(
                Pair.of(SulfurMappings.METALS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT),
                Pair.of(SulfurMappings.METALS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON),
                Pair.of(SulfurMappings.METALS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE),
                Pair.of(SulfurMappings.METALS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS)
        );
        this.makeYtoNXRecipes(2, metalsFromGems);

        var gemsFromGems = List.of(
                Pair.of(SulfurMappings.GEMS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT),
                Pair.of(SulfurMappings.GEMS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON),
                Pair.of(SulfurMappings.GEMS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE),
                Pair.of(SulfurMappings.GEMS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS)
        );
        this.makeXtoXRecipes(gemsFromGems);

        var gemsFromMetals = List.of(
                Pair.of(SulfurMappings.GEMS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT),
                Pair.of(SulfurMappings.GEMS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON),
                Pair.of(SulfurMappings.GEMS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE),
                Pair.of(SulfurMappings.GEMS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS)
        );
        this.makeNYtoXRecipes(2, gemsFromMetals);

        var gemsFromOtherMinerals = List.of(
                Pair.of(SulfurMappings.GEMS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT),
                Pair.of(SulfurMappings.GEMS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON),
                Pair.of(SulfurMappings.GEMS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE),
                Pair.of(SulfurMappings.GEMS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS)
        );
        this.makeNYtoXRecipes(4, gemsFromOtherMinerals);

        var otherMineralsFromOtherMinerals = List.of(
                Pair.of(SulfurMappings.OTHER_MINERALS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT),
                Pair.of(SulfurMappings.OTHER_MINERALS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON),
                Pair.of(SulfurMappings.OTHER_MINERALS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE),
                Pair.of(SulfurMappings.OTHER_MINERALS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS)
        );
        this.makeXtoXRecipes(otherMineralsFromOtherMinerals);

        var otherMineralsFromMetals = List.of(
                Pair.of(SulfurMappings.OTHER_MINERALS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT),
                Pair.of(SulfurMappings.OTHER_MINERALS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON),
                Pair.of(SulfurMappings.OTHER_MINERALS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE),
                Pair.of(SulfurMappings.OTHER_MINERALS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS)
        );
        this.makeYtoNXRecipes(2, otherMineralsFromMetals);

        var otherMineralsFromGems = List.of(
                Pair.of(SulfurMappings.OTHER_MINERALS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT),
                Pair.of(SulfurMappings.OTHER_MINERALS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON),
                Pair.of(SulfurMappings.OTHER_MINERALS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE),
                Pair.of(SulfurMappings.OTHER_MINERALS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS)
        );
        this.makeYtoNXRecipes(4, otherMineralsFromGems);
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
        for (var source : sources) {
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
