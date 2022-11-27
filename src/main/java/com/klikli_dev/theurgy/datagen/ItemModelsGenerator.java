/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelsGenerator extends ItemModelProvider {
    public ItemModelsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Theurgy.MODID, existingFileHelper);
    }

    protected String name(Item item) {
        return Registry.ITEM.getKey(item).getPath();
    }

    private void registerItemGenerated(String name) {
        this.registerItemGenerated(name, name);
    }

    private ItemModelBuilder registerItemGenerated(String name, String texture) {
        return this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", Theurgy.loc("item/" + texture));
    }

    private void registerItemHandheld(String name) {
        this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", Theurgy.loc("item/" + name));
    }

    private void registerItemBuiltinEntity(String name) {
        this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    private void registerDivinationRod(Item divinationRodItem){
        var variant0 = this.registerItemGenerated(this.name(divinationRodItem) + "_0", "divination_rod/" + this.name(divinationRodItem) + "_0");
        var variant1 =this.registerItemGenerated(this.name(divinationRodItem) + "_1", "divination_rod/" + this.name(divinationRodItem) + "_1");
        var variant2 =this.registerItemGenerated(this.name(divinationRodItem) + "_2", "divination_rod/" + this.name(divinationRodItem) + "_2");
        var variant3 =this.registerItemGenerated(this.name(divinationRodItem) + "_3", "divination_rod/" + this.name(divinationRodItem) + "_3");
        var variant4 =this.registerItemGenerated(this.name(divinationRodItem) + "_4", "divination_rod/" + this.name(divinationRodItem) + "_4");
        var variant5 =this.registerItemGenerated(this.name(divinationRodItem) + "_5", "divination_rod/" + this.name(divinationRodItem) + "_5");
        var variant6 =this.registerItemGenerated(this.name(divinationRodItem) + "_6", "divination_rod/" + this.name(divinationRodItem) + "_6");
        var variant7 = this.registerItemGenerated(this.name(divinationRodItem) + "_7", "divination_rod/" + this.name(divinationRodItem) + "_7");
        var searchingVariant = this.registerItemGenerated(this.name(divinationRodItem) + "_searching", "divination_rod/" + this.name(divinationRodItem) + "_searching");

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

    @Override
    protected void registerModels() {
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_JAR.get()));
        this.registerItemGenerated(this.name(ItemRegistry.JAR_LABEL.get()));
        this.registerItemBuiltinEntity(this.name(ItemRegistry.ALCHEMICAL_SULFUR.get()));
        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD.get());
    }
}
