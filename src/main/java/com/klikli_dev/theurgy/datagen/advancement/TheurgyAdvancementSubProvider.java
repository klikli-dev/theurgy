// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.advancement;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class TheurgyAdvancementSubProvider implements AdvancementProvider.AdvancementGenerator {

    protected Consumer<AdvancementHolder> saver;
    protected HolderLookup.Provider registries;
    protected ExistingFileHelper existingFileHelper;

    protected void doGenerate() {
        var bookRoot = this.add(
                Advancement.Builder.advancement()
                        .addCriterion("theurgy_present", PlayerTrigger.TriggerInstance.tick())
                        .build(this.modLoc("book_root"))
        );

        var hasBasicRod = this.add(
                Advancement.Builder.advancement()
                        .parent(bookRoot)
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .addCriterion("has_t1_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.DIVINATION_ROD_T1.get()).build()))
                        .addCriterion("has_abundant_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get()).build()))
                        .addCriterion("has_common_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get()).build()))
                        .build(this.modLoc("has_basic_rod"))
        );

        var hasAmethystRod = this.add(
                Advancement.Builder.advancement()
                        .parent(bookRoot)
                        .addCriterion("has_amethyst_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.AMETHYST_DIVINATION_ROD.get()).build()))
                        .build(this.modLoc("has_amethyst_rod"))
        );

        var hasT2Rod = this.add(
                Advancement.Builder.advancement()
                        .parent(bookRoot)
                        .addCriterion("has_t2_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.DIVINATION_ROD_T2.get()).build()))
                        .build(this.modLoc("has_t2_rod"))
        );

        var hasT3Rod = this.add(
                Advancement.Builder.advancement()
                        .parent(bookRoot)
                        .addCriterion("has_t3_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.DIVINATION_ROD_T3.get()).build()))
                        .build(this.modLoc("has_t3_rod"))
        );

        var hasT4Rod = this.add(
                Advancement.Builder.advancement()
                        .parent(bookRoot)
                        .addCriterion("has_t4_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.DIVINATION_ROD_T4.get()).build()))
                        .build(this.modLoc("has_t4_rod"))
        );

        var hasRareRod = this.add(
                Advancement.Builder.advancement()
                        .parent(bookRoot)
                        .addCriterion("has_rare_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get()).build()))
                        .build(this.modLoc("has_rare_rod"))
        );

        var hasPreciousRod = this.add(
                Advancement.Builder.advancement()
                        .parent(bookRoot)
                        .addCriterion("has_precious_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get()).build()))
                        .build(this.modLoc("has_precious_rod"))
        );


        var hasLiquefactionCauldron = this.add(
                Advancement.Builder.advancement()
                        .parent(bookRoot)
                        .addCriterion("has_liquefaction_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemRegistry.LIQUEFACTION_CAULDRON.get()).build()))
                        .build(this.modLoc("has_liquefaction_cauldron"))
        );

    }

    private AdvancementHolder add(AdvancementHolder advancement) {
        this.saver.accept(advancement);
        return advancement;
    }


    protected ResourceLocation modLoc(String path) {
        return Theurgy.loc(path);
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        this.registries = registries;
        this.saver = saver;
        this.existingFileHelper = existingFileHelper;
        this.doGenerate();
    }
}
