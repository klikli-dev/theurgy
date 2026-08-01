// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.advancement;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TheurgyAdvancementSubProvider implements AdvancementSubProvider {

    protected Identifier modLoc(String path) {
        return Theurgy.loc(path);
    }

    @Override
    public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<AdvancementHolder> writer) {
        var bookRoot =
                Advancement.Builder.advancement()
                        .addCriterion("theurgy_present", PlayerTrigger.TriggerInstance.tick())
                        .save(writer, this.modLoc("book_root"));

        var hasBasicRod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("has_t1_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.DIVINATION_ROD_T1.get()))
                .addCriterion("has_abundant_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get()))
                .addCriterion("has_common_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get()))
                .save(writer, this.modLoc("has_basic_rod"));

        var hasAmethystRod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_amethyst_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.AMETHYST_DIVINATION_ROD.get()))
                .save(writer, this.modLoc("has_amethyst_rod"));

        var hasT2Rod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_t2_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.DIVINATION_ROD_T2.get()))
                .save(writer, this.modLoc("has_t2_rod"));

        var hasT3Rod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_t3_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.DIVINATION_ROD_T3.get()))
                .save(writer, this.modLoc("has_t3_rod"));

        var hasT4Rod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_t4_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.DIVINATION_ROD_T4.get()))
                .save(writer, this.modLoc("has_t4_rod"));

        var hasRareRod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_rare_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get()))
                .save(writer, this.modLoc("has_rare_rod"));

        var hasPreciousRod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_precious_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get()))
                .save(writer, this.modLoc("has_precious_rod"));


        var hasLiquefactionCauldron = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_liquefaction_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.LIQUEFACTION_CAULDRON.get()))
                .save(writer, this.modLoc("has_liquefaction_cauldron"));
    }
}
