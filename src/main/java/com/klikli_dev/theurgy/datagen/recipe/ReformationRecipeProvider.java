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
import net.minecraft.world.item.ItemStack;

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

    private void makeNiterToSulfurRecipe(AlchemicalSulfurItem source, AlchemicalSulfurItem target) {
        this.makeNiterToSulfurRecipe(source, 1, List.of(target), 1);
    }

    private void makeNiterToSulfurRecipe(AlchemicalSulfurItem source, int sourceCount, AlchemicalSulfurItem target, int targetCount, boolean respectNoAutomaticRecipes) {
        this.makeNiterToSulfurRecipe(source, sourceCount, List.of(target), targetCount, respectNoAutomaticRecipes);
    }

    private void makeNiterToSulfurRecipe(AlchemicalSulfurItem source, List<AlchemicalSulfurItem> targets) {
        this.makeNiterToSulfurRecipe(source, 1, targets, 1);
    }

    private void makeNiterToSulfurRecipe(AlchemicalSulfurItem source, int sourceCount, List<AlchemicalSulfurItem> targets, int targetCount){
        this.makeNiterToSulfurRecipe(source, sourceCount, targets, targetCount, true);
    }

    private void makeNiterToSulfurRecipe(AlchemicalSulfurItem source, int sourceCount, List<AlchemicalSulfurItem> targets, int targetCount, boolean respectNoAutomaticRecipes) {
        targets.stream().filter(t -> !respectNoAutomaticRecipes || !this.noAutomaticRecipesFor.contains(t)).forEach((target) -> {
            this.makeRecipe(target, targetCount, source, sourceCount, this.getFlux(target));
        });
    }

    private void makeNiterToNiterRecipe(AlchemicalSulfurItem source, int sourceCount, AlchemicalSulfurItem target, int targetCount) {
        this.makeRecipe(target, targetCount, source, sourceCount, this.getFlux(target));
    }

    private void earthenMatters() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), SulfurMappings.earthenMattersAbundant());
        this.makeNiterToSulfurRecipe(SulfurRegistry.EARTHEN_MATTERS_COMMON.get(), SulfurMappings.earthenMattersCommon());

        //Also allow direct conversion between specific sulfurs of the same tier
        var earthenMattersFromEarthenMatters = List.of(
                Pair.of(SulfurMappings.earthenMattersAbundant(), ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_ABUNDANT),
                Pair.of(SulfurMappings.earthenMattersCommon(), ItemTagRegistry.ALCHEMICAL_SULFURS_EARTHEN_MATTERS_COMMON)
        );
        this.makeXtoXRecipes(earthenMattersFromEarthenMatters);

        //Further, allow conversion between types
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 1, SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_COMMON.get(), 1, SulfurRegistry.EARTHEN_MATTERS_COMMON.get(), 4);

        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_ABUNDANT.get(), 1, SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 8);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_COMMON.get(), 1, SulfurRegistry.EARTHEN_MATTERS_COMMON.get(), 8);

        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), 1, SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 16);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_COMMON.get(), 1, SulfurRegistry.EARTHEN_MATTERS_COMMON.get(), 16);

        this.makeNiterToNiterRecipe(SulfurRegistry.LOGS_ABUNDANT.get(), 1, SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.CROPS_ABUNDANT.get(), 2, SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.ANIMALS_ABUNDANT.get(), 1, SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 8);
    }

    private void metals() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.METALS_ABUNDANT.get(), SulfurMappings.metalsAbundant());
        this.makeNiterToSulfurRecipe(SulfurRegistry.METALS_COMMON.get(), SulfurMappings.metalsCommon());
        this.makeNiterToSulfurRecipe(SulfurRegistry.METALS_RARE.get(), SulfurMappings.metalsRare());
        this.makeNiterToSulfurRecipe(SulfurRegistry.METALS_PRECIOUS.get(), SulfurMappings.metalsPrecious());

        //Also allow direct conversion between specific sulfurs of the same tier
        var metalsFromMetals = List.of(
                Pair.of(SulfurMappings.metalsAbundant(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_ABUNDANT),
                Pair.of(SulfurMappings.metalsCommon(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_COMMON),
                Pair.of(SulfurMappings.metalsRare(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_RARE),
                Pair.of(SulfurMappings.metalsPrecious(), ItemTagRegistry.ALCHEMICAL_SULFURS_METALS_PRECIOUS)
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

        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 8, SulfurRegistry.METALS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_COMMON.get(), 8, SulfurRegistry.METALS_COMMON.get(), 1);
    }

    private void gems() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), SulfurMappings.gemsAbundant());
        this.makeNiterToSulfurRecipe(SulfurRegistry.GEMS_COMMON.get(), SulfurMappings.gemsCommon());
        this.makeNiterToSulfurRecipe(SulfurRegistry.GEMS_RARE.get(), SulfurMappings.gemsRare());
        this.makeNiterToSulfurRecipe(SulfurRegistry.GEMS_PRECIOUS.get(), SulfurMappings.gemsPrecious());

        //Also allow direct conversion between specific sulfurs of the same tier
        var gemsFromGems = List.of(
                Pair.of(SulfurMappings.gemsAbundant(), ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_ABUNDANT),
                Pair.of(SulfurMappings.gemsCommon(), ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_COMMON),
                Pair.of(SulfurMappings.gemsRare(), ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_RARE),
                Pair.of(SulfurMappings.gemsPrecious(), ItemTagRegistry.ALCHEMICAL_SULFURS_GEMS_PRECIOUS)
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

        this.makeNiterToNiterRecipe(SulfurRegistry.MOBS_ABUNDANT.get(), 2, SulfurRegistry.GEMS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.MOBS_COMMON.get(), 4, SulfurRegistry.GEMS_COMMON.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.MOBS_RARE.get(), 8, SulfurRegistry.GEMS_RARE.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_PRECIOUS.get(), 32, SulfurRegistry.GEMS_RARE.get(), 1);

        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 16, SulfurRegistry.GEMS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_COMMON.get(), 16, SulfurRegistry.GEMS_COMMON.get(), 1);
    }

    private void otherMinerals() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), SulfurMappings.otherMineralsAbundant());
        this.makeNiterToSulfurRecipe(SulfurRegistry.OTHER_MINERALS_COMMON.get(), SulfurMappings.otherMineralsCommon());
        this.makeNiterToSulfurRecipe(SulfurRegistry.OTHER_MINERALS_RARE.get(), SulfurMappings.otherMineralsRare());
        this.makeNiterToSulfurRecipe(SulfurRegistry.OTHER_MINERALS_PRECIOUS.get(), SulfurMappings.otherMineralsPrecious());

        //Also allow direct conversion between specific sulfurs of the same tier
        var otherMineralsFromOtherMinerals = List.of(
                Pair.of(SulfurMappings.otherMineralsAbundant(), ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_ABUNDANT),
                Pair.of(SulfurMappings.otherMineralsCommon(), ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_COMMON),
                Pair.of(SulfurMappings.otherMineralsRare(), ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_RARE),
                Pair.of(SulfurMappings.otherMineralsPrecious(), ItemTagRegistry.ALCHEMICAL_SULFURS_OTHER_MINERALS_PRECIOUS)
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

        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 4, SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_COMMON.get(), 4, SulfurRegistry.OTHER_MINERALS_COMMON.get(), 1);
    }

    private void logs() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.LOGS_ABUNDANT.get(), SulfurMappings.logsAbundant());

        //Also allow direct conversion between specific sulfurs of the same tier
        var logsFromLogs = List.of(
                Pair.of(SulfurMappings.logsAbundant(), ItemTagRegistry.ALCHEMICAL_SULFURS_LOGS_ABUNDANT)
        );
        this.makeXtoXRecipes(logsFromLogs);

        //Further, allow conversion between types

        //logs should not convert to minerals, we have log->coal furnace recipes to enable that
//        this.makeNiterToNiterRecipe(SulfurRegistry.LOGS_ABUNDANT.get(), 2, SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 1);

        //but the reverse is fine
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 1, SulfurRegistry.LOGS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_ABUNDANT.get(), 1, SulfurRegistry.LOGS_ABUNDANT.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), 1, SulfurRegistry.LOGS_ABUNDANT.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.CROPS_ABUNDANT.get(), 1, SulfurRegistry.LOGS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.ANIMALS_ABUNDANT.get(), 1, SulfurRegistry.LOGS_ABUNDANT.get(), 2);

        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 4, SulfurRegistry.LOGS_ABUNDANT.get(), 1);
    }

    private void crops() {
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.CROPS_ABUNDANT.get(), SulfurMappings.cropsAbundant());

        //Also allow direct conversion between specific sulfurs of the same tier
        var cropsFromCrops = List.of(
                Pair.of(SulfurMappings.cropsAbundant(), ItemTagRegistry.ALCHEMICAL_SULFURS_CROPS_ABUNDANT)
        );
        this.makeXtoXRecipes(cropsFromCrops);

        //Further, allow conversion between types
        this.makeNiterToNiterRecipe(SulfurRegistry.LOGS_ABUNDANT.get(), 1, SulfurRegistry.CROPS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.ANIMALS_ABUNDANT.get(), 1, SulfurRegistry.CROPS_ABUNDANT.get(), 2);


        //crops should not convert to minerals, we have crop->log reformation, and log->coal furnace recipes to enable that
//        this.makeNiterToNiterRecipe(SulfurRegistry.CROPS_ABUNDANT.get(), 2, SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 1);

        //minerals to crops is fine though
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 1, SulfurRegistry.CROPS_ABUNDANT.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_ABUNDANT.get(), 1, SulfurRegistry.CROPS_ABUNDANT.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), 1, SulfurRegistry.CROPS_ABUNDANT.get(), 8);
        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 4, SulfurRegistry.CROPS_ABUNDANT.get(), 2);
    }

    private void animals(){
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.ANIMALS_ABUNDANT.get(), SulfurMappings.animalsAbundant());
        this.makeNiterToSulfurRecipe(SulfurRegistry.ANIMALS_COMMON.get(), SulfurMappings.animalsCommon());
        this.makeNiterToSulfurRecipe(SulfurRegistry.ANIMALS_RARE.get(), SulfurMappings.animalsRare());

        //Also allow direct conversion between specific sulfurs of the same tier
        var animalsFromAnimal = List.of(
                Pair.of(SulfurMappings.animalsAbundant(), ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_ABUNDANT),
                Pair.of(SulfurMappings.animalsCommon(), ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_COMMON),
                Pair.of(SulfurMappings.animalsRare(), ItemTagRegistry.ALCHEMICAL_SULFURS_ANIMALS_RARE)
        );
        this.makeXtoXRecipes(animalsFromAnimal);

        //Further, allow conversion between types
        //Note: Animals -> Minerals is covered via Animals -> Logs -> (Charcoal in Furnace) -> Minerals, only the reverse is via reformation

        this.makeNiterToNiterRecipe(SulfurRegistry.CROPS_ABUNDANT.get(), 2, SulfurRegistry.ANIMALS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.LOGS_ABUNDANT.get(), 2, SulfurRegistry.ANIMALS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.EARTHEN_MATTERS_ABUNDANT.get(), 8, SulfurRegistry.ANIMALS_ABUNDANT.get(), 1);

        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_ABUNDANT.get(), 1, SulfurRegistry.ANIMALS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_COMMON.get(), 1, SulfurRegistry.ANIMALS_COMMON.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.OTHER_MINERALS_RARE.get(), 1, SulfurRegistry.ANIMALS_RARE.get(), 1);

        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_ABUNDANT.get(), 1, SulfurRegistry.ANIMALS_ABUNDANT.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_COMMON.get(), 1, SulfurRegistry.ANIMALS_COMMON.get(), 2);
        this.makeNiterToNiterRecipe(SulfurRegistry.METALS_RARE.get(), 1, SulfurRegistry.ANIMALS_RARE.get(), 2);

        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), 1, SulfurRegistry.ANIMALS_ABUNDANT.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_COMMON.get(), 1, SulfurRegistry.ANIMALS_COMMON.get(), 4);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_RARE.get(), 1, SulfurRegistry.ANIMALS_RARE.get(), 4);
    }

    private void mobs(){
        //Add conversion from the niter (representing the whole tier) to the single specific sulfurs
        //This enables conversion between tiers by way of digestion
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_ABUNDANT.get(), SulfurMappings.mobsAbundant());
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_COMMON.get(), SulfurMappings.mobsCommon());
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_RARE.get(), SulfurMappings.mobsRare());
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_PRECIOUS.get(), SulfurMappings.mobsPrecious());

        //For some items we add a special conversion with different multipliers
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_COMMON.get(), 2, SulfurRegistry.SKELETON_SKULL.get(), 1, false);
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_RARE.get(), 2, SulfurRegistry.WITHER_SKELETON_SKULL.get(), 1, false);
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_RARE.get(), 2, SulfurRegistry.GHAST_TEAR.get(), 1, false);
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_RARE.get(), 2, SulfurRegistry.SHULKER_SHELL.get(), 1, false);
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_RARE.get(), 2, SulfurRegistry.ELYTRA.get(), 1, false);
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_PRECIOUS.get(), 2, SulfurRegistry.NETHER_STAR.get(), 1, false);
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_PRECIOUS.get(), 4, SulfurRegistry.DRAGON_EGG.get(), 1, false);
        this.makeNiterToSulfurRecipe(SulfurRegistry.MOBS_PRECIOUS.get(), 2, SulfurRegistry.HEART_OF_THE_SEA.get(), 1, false);

        //Also allow direct conversion between specific sulfurs of the same tier
        var mobsFromMob = List.of(
                Pair.of(SulfurMappings.mobsAbundant(), ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_ABUNDANT),
                Pair.of(SulfurMappings.mobsCommon(), ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_COMMON),
                Pair.of(SulfurMappings.mobsRare(), ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_RARE),
                Pair.of(SulfurMappings.mobsPrecious(), ItemTagRegistry.ALCHEMICAL_SULFURS_MOBS_PRECIOUS)
        );
        this.makeXtoXRecipes(mobsFromMob);

        //Further, allow (some) conversion between types
        //with mob drops that is super complicated, so try with this for now.
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_ABUNDANT.get(), 4, SulfurRegistry.MOBS_ABUNDANT.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_COMMON.get(), 8, SulfurRegistry.MOBS_COMMON.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_RARE.get(), 16, SulfurRegistry.MOBS_RARE.get(), 1);
        this.makeNiterToNiterRecipe(SulfurRegistry.GEMS_PRECIOUS.get(), 64, SulfurRegistry.MOBS_PRECIOUS.get(), 1);

        //TODO: niter -> sulfur recipes not available apprently
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {

        //Set up materials that should not get the automatic conversion rates
        this.noAutomaticRecipesFor = Set.of(
                SulfurRegistry.ALLTHEMODIUM.get(),
                SulfurRegistry.UNOBTAINIUM.get(),
                SulfurRegistry.VIBRANIUM.get()
        );

        this.earthenMatters();
        this.metals();
        this.gems();
        this.otherMinerals();
        this.logs();
        this.crops();
        this.animals();
        this.mobs();

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
