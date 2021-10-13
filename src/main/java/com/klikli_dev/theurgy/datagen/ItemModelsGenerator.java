/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
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
        return item.getRegistryName().getPath();
    }

    @Override
    protected void registerModels() {
        this.registerItemGenerated(this.name(ItemRegistry.THEURGY.get()), "grimoire");
        this.registerBlockItem(this.name(ItemRegistry.GRAFTING_HEDGE.get()));
        this.registerItemGenerated(this.name(ItemRegistry.UNRIPE_GRAFTING_FRUIT.get()));
    }

    private void registerItemGenerated(String name) {
        this.registerItemGenerated(name, name);
    }

    private void registerItemGenerated(String name, String texture) {
        this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", Theurgy.id("item/" + texture));
    }

    private void registerItemHandheld(String name) {
        this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", Theurgy.id("item/" + name));
    }

    private void registerBlockItem(String name) {
        this.getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(Theurgy.id("block/" + name)));
    }
}
