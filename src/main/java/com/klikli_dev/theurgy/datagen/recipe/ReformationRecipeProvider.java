// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurTier;
import com.klikli_dev.theurgy.content.recipe.ReformationRecipe;
import com.klikli_dev.theurgy.datagen.SulfurMappings;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.*;
import java.util.function.BiConsumer;

public class ReformationRecipeProvider extends JsonRecipeProvider {

    public static final int TIME = ReformationRecipe.DEFAULT_REFORMATION_TIME;
    private final Map<AlchemicalSulfurTier, Integer> fluxPerTier = Map.of(
            AlchemicalSulfurTier.ABUNDANT, 50,
            AlchemicalSulfurTier.COMMON, 100,
            AlchemicalSulfurTier.RARE, 150,
            AlchemicalSulfurTier.PRECIOUS, 200
    );
    private final Map<ResourceLocation, JsonObject> recipeCache = new HashMap<>();
    private Set<AlchemicalSulfurItem> noAutomaticRecipesFor = Set.of();

    public ReformationRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "reformation");
    }

    private int getFlux(AlchemicalSulfurItem sulfur) {
        return this.fluxPerTier.get(sulfur.tier());
    }

    private void makeXtoXRecipes(List<Pair<List<AlchemicalSulfurItem>, TagKey<Item>>> sulfurToTag) {
        sulfurToTag.forEach((entry) -> {
            entry.getFirst().stream().filter(s -> !this.noAutomaticRecipesFor.contains(s))
                    .forEach((sulfur) -> {
                        this.makeTagRecipe(sulfur, entry.getSecond(), this.getFlux(sulfur));
                    });
        });
    }

    /**
     * Convert N of the tag to 1 sulfur
     */
    private void makeNYtoXRecipes(int n, List<Pair<List<AlchemicalSulfurItem>, TagKey<Item>>> sulfurToTag) {
        sulfurToTag.forEach((entry) -> {
            entry.getFirst().stream().filter(s -> !this.noAutomaticRecipesFor.contains(s))
                    .forEach((sulfur) -> {
                        this.makeTagRecipe(sulfur, Collections.nCopies(n, entry.getSecond()), this.getFlux(sulfur));
                    });
        });
    }

    /**
     * Convert 1 of the tag to N sulfur
     */
    private void makeYtoNXRecipes(int n, List<Pair<List<AlchemicalSulfurItem>, TagKey<Item>>> sulfurToTag) {
        sulfurToTag.forEach((entry) -> {
            entry.getFirst().stream().filter(s -> !this.noAutomaticRecipesFor.contains(s))
                    .forEach((sulfur) -> {
                        this.makeTagRecipe(sulfur, n, entry.getSecond(), this.getFlux(sulfur));
                    });
        });
    }

    private void makeNiterToSulfurRecipe(AlchemicalSulfurItem source, List<AlchemicalSulfurItem> targets) {
        targets.stream().filter(t -> !this.noAutomaticRecipesFor.contains(t)).forEach((target) -> {
            this.makeRecipe(target, source, this.getFlux(target));
        });
    }

    private void makeNiterToNiterRecipe(AlchemicalSulfurItem source, int sourceCount, AlchemicalSulfurItem target, int targetCount) {
        this.makeRecipe(target, targetCount, source, sourceCount, this.getFlux(target));
    }

    private void metals() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.METALS_ABUNDANT.get(), SulfurMappings.METALS_ABUNDANT);
        this.makeNiterToSulfurRecipe(SulfurRegistry.METALS_COMMON.get(), SulfurMappings.METALS_COMMON);
        this.makeNiterToSulfurRecipe(SulfurRegistry.METALS_RARE.get(), SulfurMappings.METALS_RARE);
        this.makeNiterToSulfurRecipe(SulfurRegistry.METALS_PRECIOUS.get(), SulfurMappings.METALS_PRECIOUS);

        //Also allow direct conversion between specific sulfurs of the same tier
        var metalsFromMetals = List.of(
                Pair.of(SulfurMappings.METALS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT),
                Pair.of(SulfurMappings.METALS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON),
                Pair.of(SulfurMappings.METALS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE),
                Pair.of(SulfurMappings.METALS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS)
        );
        this.makeXtoXRecipes(metalsFromMetals);

        //Further, allow conversion between types
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 2, SulfurRegistry.METALS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_COMMON.get(), 2, SulfurRegistry.METALS_COMMON.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_RARE.get(), 2, SulfurRegistry.METALS_RARE.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_PRECIOUS.get(), 2, SulfurRegistry.METALS_PRECIOUS.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), 1, SulfurRegistry.METALS_ABUNDANT.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_COMMON.get(), 1, SulfurRegistry.METALS_COMMON.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_RARE.get(), 1, SulfurRegistry.METALS_RARE.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_PRECIOUS.get(), 1, SulfurRegistry.METALS_PRECIOUS.get(), 2);
    }

    private void gems() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), SulfurMappings.GEMS_ABUNDANT);
        this.makeNiterToSulfurRecipe(SulfurRegistry.GEMS_COMMON.get(), SulfurMappings.GEMS_COMMON);
        this.makeNiterToSulfurRecipe(SulfurRegistry.GEMS_RARE.get(), SulfurMappings.GEMS_RARE);
        this.makeNiterToSulfurRecipe(SulfurRegistry.GEMS_PRECIOUS.get(), SulfurMappings.GEMS_PRECIOUS);

        //Also allow direct conversion between specific sulfurs of the same tier
        var gemsFromGems = List.of(
                Pair.of(SulfurMappings.GEMS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT),
                Pair.of(SulfurMappings.GEMS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON),
                Pair.of(SulfurMappings.GEMS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE),
                Pair.of(SulfurMappings.GEMS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS)
        );
        this.makeXtoXRecipes(gemsFromGems);

        //Further, allow conversion between types
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_ABUNDANT.get(), 2, SulfurRegistry.GEMS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_COMMON.get(), 2, SulfurRegistry.GEMS_COMMON.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_RARE.get(), 2, SulfurRegistry.GEMS_RARE.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_PRECIOUS.get(), 2, SulfurRegistry.GEMS_PRECIOUS.get(), 1);

        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 4, SulfurRegistry.GEMS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_COMMON.get(), 4, SulfurRegistry.GEMS_COMMON.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_RARE.get(), 4, SulfurRegistry.GEMS_RARE.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_PRECIOUS.get(), 4, SulfurRegistry.GEMS_PRECIOUS.get(), 1);
    }

    private void otherMinerals() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), SulfurMappings.OTHER_MINERALS_ABUNDANT);
        this.makeNiterToSulfurRecipe(SulfurRegistry.OTHER_MINERALS_COMMON.get(), SulfurMappings.OTHER_MINERALS_COMMON);
        this.makeNiterToSulfurRecipe(SulfurRegistry.OTHER_MINERALS_RARE.get(), SulfurMappings.OTHER_MINERALS_RARE);
        this.makeNiterToSulfurRecipe(SulfurRegistry.OTHER_MINERALS_PRECIOUS.get(), SulfurMappings.OTHER_MINERALS_PRECIOUS);

        //Also allow direct conversion between specific sulfurs of the same tier
        var otherMineralsFromOtherMinerals = List.of(
                Pair.of(SulfurMappings.OTHER_MINERALS_ABUNDANT, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT),
                Pair.of(SulfurMappings.OTHER_MINERALS_COMMON, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON),
                Pair.of(SulfurMappings.OTHER_MINERALS_RARE, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE),
                Pair.of(SulfurMappings.OTHER_MINERALS_PRECIOUS, ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS)
        );
        this.makeXtoXRecipes(otherMineralsFromOtherMinerals);

        //Further, allow conversion between types
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_ABUNDANT.get(), 1, SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_COMMON.get(), 1, SulfurRegistry.OTHER_MINERALS_COMMON.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_RARE.get(), 1, SulfurRegistry.OTHER_MINERALS_RARE.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_PRECIOUS.get(), 1, SulfurRegistry.OTHER_MINERALS_PRECIOUS.get(), 2);

        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), 1, SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_COMMON.get(), 1, SulfurRegistry.OTHER_MINERALS_COMMON.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_RARE.get(), 1, SulfurRegistry.OTHER_MINERALS_RARE.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_PRECIOUS.get(), 1, SulfurRegistry.OTHER_MINERALS_PRECIOUS.get(), 4);
    }

    @Override
    void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        //Set up materials that should not get the automatic conversion rates
        this.noAutomaticRecipesFor = Set.of(
                SulfurRegistry.ALLTHEMODIUM.get(),
                SulfurRegistry.UNOBTAINIUM.get(),
                SulfurRegistry.VIBRANIUM.get()
        );

        this.metals();
        this.gems();
        this.otherMinerals();

        //now flush cache.
        this.recipeCache.forEach(recipeConsumer);
    }

    public void makeTagRecipe(Item result, TagKey<Item> source, int mercuryFlux) {
        this.makeTagRecipe(result, 1, source, mercuryFlux);
    }

    public void makeTagRecipe(Item result, int resultCount, TagKey<Item> source, int mercuryFlux) {
        this.makeTagRecipe(this.name(result) + "_from_" + this.name(source), result, resultCount, List.of(source), mercuryFlux, TIME);
    }

    public void makeTagRecipe(Item result, List<TagKey<Item>> sources, int mercuryFlux) {
        this.makeTagRecipe(result, 1, sources, mercuryFlux);
    }

    public void makeTagRecipe(Item result, int resultCount, List<TagKey<Item>> sources, int mercuryFlux) {
        this.makeTagRecipe(this.name(result) + "_from_" + this.name(sources), result, resultCount, sources, mercuryFlux, TIME);
    }


    public void makeTagRecipe(String recipeName, Item result, int resultCount, List<TagKey<Item>> sources, int mercuryFlux, int reformationTime) {
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

        this.recipeCache.put(this.modLoc(recipeName), recipe);
    }

    public void makeRecipe(Item result, Item source, int mercuryFlux) {
        this.makeRecipe(result, 1, source, 1, mercuryFlux);
    }

    public void makeRecipe(Item result, int resultCount, Item source, int sourceCount, int mercuryFlux) {
        this.makeRecipe(this.name(result) + "_from_" + this.name(source), result, resultCount, Collections.nCopies(sourceCount, source).stream().toList(), mercuryFlux, TIME);
    }

    public void makeRecipe(String recipeName, Item result, int resultCount, List<Item> sources, int mercuryFlux, int reformationTime) {
        this.recipeCache.put(
                this.modLoc(recipeName),
                this.makeRecipeJson(
                        sources.stream().map(s -> this.makeItemIngredient(this.locFor(s))).toList(),
                        this.makeItemIngredient(this.locFor(result)),
                        mercuryFlux,
                        this.makeItemStackCodecResult(this.locFor(result), resultCount),
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
