/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
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

    private void registerItemGenerated(String name, String texture) {
        this.getBuilder(name)
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

    @Override
    protected void registerModels() {
        this.registerItemGenerated(this.name(ItemRegistry.EMPTY_JAR.get()));
        this.registerItemGenerated(this.name(ItemRegistry.JAR_LABEL.get()));
        this.registerItemBuiltinEntity(this.name(ItemRegistry.ALCHEMICAL_SULFUR.get()));
        //this.registerItemGenerated(this.name(ItemRegistry.SULFUR_INGOT.get()));
    }
}
