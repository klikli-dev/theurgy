/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.models;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class TheurgyItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public TheurgyItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Theurgy.MODID, existingFileHelper);
    }

    protected String name(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
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

    private void registerDivinationRod(Item divinationRodItem){
        var variant0 = this.registerItemGenerated(this.name(divinationRodItem) + "_0", this.name(divinationRodItem)+ "/divination_rod_0");
        var variant1 =this.registerItemGenerated(this.name(divinationRodItem) + "_1", this.name(divinationRodItem)+ "/divination_rod_1");
        var variant2 =this.registerItemGenerated(this.name(divinationRodItem) + "_2", this.name(divinationRodItem)+ "/divination_rod_2");
        var variant3 =this.registerItemGenerated(this.name(divinationRodItem) + "_3", this.name(divinationRodItem)+ "/divination_rod_3");
        var variant4 =this.registerItemGenerated(this.name(divinationRodItem) + "_4", this.name(divinationRodItem)+ "/divination_rod_4");
        var variant5 =this.registerItemGenerated(this.name(divinationRodItem) + "_5", this.name(divinationRodItem)+ "/divination_rod_5");
        var variant6 =this.registerItemGenerated(this.name(divinationRodItem) + "_6", this.name(divinationRodItem)+ "/divination_rod_6");
        var variant7 = this.registerItemGenerated(this.name(divinationRodItem) + "_7", this.name(divinationRodItem)+ "/divination_rod_7");
        var searchingVariant = this.registerItemGenerated(this.name(divinationRodItem) + "_searching", this.name(divinationRodItem)+ "/divination_rod_searching");

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
        this.registerItemGenerated(this.name(ItemRegistry.THE_HERMETICA_ICON.get()), "the_hermetica");
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_JAR.get()));
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_JAR_LABELED.get()));
        this.registerItemGenerated(this.name(ItemRegistry.JAR_LABEL.get()));
        this.registerItemBuiltinEntity(this.name(ItemRegistry.ALCHEMICAL_SULFUR.get()));
        this.registerItemGenerated("alchemical_salt"); //parent for alchemical salt
        this.registerAlchemicalSalt(this.name(ItemRegistry.ALCHEMICAL_SALT_ORE.get()));

        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD_T1.get());
        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD_T2.get());
        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD_T3.get());
        this.registerDivinationRod(ItemRegistry.DIVINATION_ROD_T4.get());

    }
}
