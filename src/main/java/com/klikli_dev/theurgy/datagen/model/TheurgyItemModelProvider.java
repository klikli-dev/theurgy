// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;


public class TheurgyItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {
    public TheurgyItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Theurgy.MODID, existingFileHelper);
    }

    protected String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private ItemModelBuilder registerAlchemicalSalt(String name) {
        return this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(this.modLoc("item/alchemical_salt")));
    }

    private ItemModelBuilder registerItemGenerated(String name) {
        return this.registerItemGenerated(name, name);
    }

    private ItemModelBuilder registerItemGenerated(String name, String texture) {
        return this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", this.modLoc("item/" + texture));
    }

    private void registerItemHandheld(String name) {
        this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", this.modLoc("item/" + name));
    }

    private void registerItemBuiltinEntity(String name) {
        this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    private void registerDivinationRod(Item divinationRodItem) {

        //somehow we have to prefix "item/" if we append "/0" instead of "_=". Probably something in the item builder or resource location logic it a "/" is present. Anyhow, this moves the variants into a subfolder
        var variant0 = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/0", this.name(divinationRodItem) + "/divination_rod_0");
        var variant1 = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/1", this.name(divinationRodItem) + "/divination_rod_1");
        var variant2 = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/2", this.name(divinationRodItem) + "/divination_rod_2");
        var variant3 = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/3", this.name(divinationRodItem) + "/divination_rod_3");
        var variant4 = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/4", this.name(divinationRodItem) + "/divination_rod_4");
        var variant5 = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/5", this.name(divinationRodItem) + "/divination_rod_5");
        var variant6 = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/6", this.name(divinationRodItem) + "/divination_rod_6");
        var variant7 = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/7", this.name(divinationRodItem) + "/divination_rod_7");
        var searchingVariant = this.registerItemGenerated("item/" + this.name(divinationRodItem) + "/searching", this.name(divinationRodItem) + "/divination_rod_searching");

        var name = this.name(divinationRodItem);
        this.getBuilder(name)
                .parent(variant0)

                .override()
                .model(variant7)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 0.0f)
                .end()

                .override()
                .model(variant6)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 1.0f)
                .end()

                .override()
                .model(variant5)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 2.0f)
                .end()

                .override()
                .model(variant4)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 3.0f)
                .end()

                .override()
                .model(variant3)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 4.0f)
                .end()

                .override()
                .model(variant2)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 5.0f)
                .end()

                .override()
                .model(variant1)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 6.0f)
                .end()

                .override()
                .model(variant0)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 7.0f)
                .end()

                .override()
                .model(searchingVariant)
                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 8.0f)
                .end();
    }

    protected void registerSulfurs() {
        //alchemical sulfur models

        SulfurRegistry.SULFURS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
            if (sulfur.useAutomaticIconRendering) {
                this.registerItemBuiltinEntity(this.name(sulfur));
            }
        });

        this.registerItemGenerated(this.name(ItemRegistry.GEMS_ABUNDANT_ICON.get()), "sulfurs/gems_abundant");
        this.registerItemGenerated(this.name(ItemRegistry.GEMS_COMMON_ICON.get()), "sulfurs/gems_common");
        this.registerItemGenerated(this.name(ItemRegistry.GEMS_RARE_ICON.get()), "sulfurs/gems_rare");
        this.registerItemGenerated(this.name(ItemRegistry.GEMS_PRECIOUS_ICON.get()), "sulfurs/gems_precious");

        this.registerItemGenerated(this.name(ItemRegistry.METALS_ABUNDANT_ICON.get()), "sulfurs/metals_abundant");
        this.registerItemGenerated(this.name(ItemRegistry.METALS_COMMON_ICON.get()), "sulfurs/metals_common");
        this.registerItemGenerated(this.name(ItemRegistry.METALS_RARE_ICON.get()), "sulfurs/metals_rare");
        this.registerItemGenerated(this.name(ItemRegistry.METALS_PRECIOUS_ICON.get()), "sulfurs/metals_precious");

        this.registerItemGenerated(this.name(ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON.get()), "sulfurs/other_minerals_abundant");
        this.registerItemGenerated(this.name(ItemRegistry.OTHER_MINERALS_COMMON_ICON.get()), "sulfurs/other_minerals_common");
        this.registerItemGenerated(this.name(ItemRegistry.OTHER_MINERALS_RARE_ICON.get()), "sulfurs/other_minerals_rare");
        this.registerItemGenerated(this.name(ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON.get()), "sulfurs/other_minerals_precious");
    }

    protected void registerSalts() {
        this.registerItemGenerated("alchemical_salt"); //parent for alchemical salt

        //alchemical salt models
        SaltRegistry.SALTS.getEntries().stream().map(DeferredHolder::get).forEach(salt -> {
            this.registerAlchemicalSalt(this.name(salt));
        });
    }

    @Override
    protected void registerModels() {
        this.registerItemGenerated(this.name(ItemRegistry.THE_HERMETICA_ICON.get()), "the_hermetica");
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_JAR_ICON.get()), "empty_jar");
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_JAR_IRON_BAND_ICON.get()), "empty_jar_iron_band");
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_JAR_LABELED_ICON.get()), "empty_jar_labeled");
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_CERAMIC_JAR_ICON.get()), "empty_ceramic_jar");
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_CERAMIC_JAR_LABELED_ICON.get()), "empty_ceramic_jar_labeled");
        this.registerItemGenerated(this.name(ItemRegistry.JAR_LABEL_ICON.get()), "jar_label");
        this.registerItemGenerated(this.name(ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON.get()), "jar_label_frame_abundant");
        this.registerItemGenerated(this.name(ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON.get()), "jar_label_frame_common");
        this.registerItemGenerated(this.name(ItemRegistry.JAR_LABEL_FRAME_RARE_ICON.get()), "jar_label_frame_rare");
        this.registerItemGenerated(this.name(ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON.get()), "jar_label_frame_precious");

        this.registerSulfurs();
        this.registerSalts();

        this.registerItemGenerated(this.name(ItemRegistry.MERCURY_SHARD.get()));
        this.registerItemGenerated(this.name(ItemRegistry.MERCURY_CRYSTAL.get()));
        this.registerItemGenerated(this.name(ItemRegistry.SAL_AMMONIAC_CRYSTAL.get()));
        this.registerItemGenerated(this.name(ItemRegistry.PURIFIED_GOLD.get()));

        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD_T1.get());
        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD_T2.get());
        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD_T3.get());
        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD_T4.get());
        this.registerDivinationRod(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get());
        this.registerDivinationRod(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get());
        this.registerDivinationRod(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get());
        this.registerDivinationRod(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get());
        this.registerDivinationRod(ItemRegistry.AMETHYST_DIVINATION_ROD.get());

        this.registerItemGenerated(this.name(ItemRegistry.COPPER_WIRE.get()));
    }
}
