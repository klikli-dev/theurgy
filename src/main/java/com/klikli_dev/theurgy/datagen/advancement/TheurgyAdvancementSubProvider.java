// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.advancement;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;

public class TheurgyAdvancementSubProvider extends AdvancementSubProvider {

    public TheurgyAdvancementSubProvider(BootstrapContext<Advancement> output) {
        super(output);
    }

    protected Identifier modLoc(String path) {
        return Theurgy.loc(path);
    }

    @Override
    public void generate() {
        var bookRoot =
                Advancement.Builder.advancement()
                        .addCriterion("theurgy_present", PlayerTrigger.TriggerInstance.tick())
                        .save(this.output, this.modLoc("book_root").toString());

        var hasBasicRod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("has_t1_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.DIVINATION_ROD_T1.get()))
                .addCriterion("has_abundant_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get()))
                .addCriterion("has_common_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get()))
                .save(this.output, this.modLoc("has_basic_rod").toString());

        var hasAmethystRod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_amethyst_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.AMETHYST_DIVINATION_ROD.get()))
                .save(this.output, this.modLoc("has_amethyst_rod").toString());

        var hasT2Rod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_t2_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.DIVINATION_ROD_T2.get()))
                .save(this.output, this.modLoc("has_t2_rod").toString());

        var hasT3Rod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_t3_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.DIVINATION_ROD_T3.get()))
                .save(this.output, this.modLoc("has_t3_rod").toString());

        var hasT4Rod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_t4_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.DIVINATION_ROD_T4.get()))
                .save(this.output, this.modLoc("has_t4_rod").toString());

        var hasRareRod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_rare_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get()))
                .save(this.output, this.modLoc("has_rare_rod").toString());

        var hasPreciousRod = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_precious_rod", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get()))
                .save(this.output, this.modLoc("has_precious_rod").toString());


        var hasLiquefactionCauldron = Advancement.Builder.advancement()
                .parent(bookRoot)
                .addCriterion("has_liquefaction_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.LIQUEFACTION_CAULDRON.get()))
                .save(this.output, this.modLoc("has_liquefaction_cauldron").toString());
    }
}
