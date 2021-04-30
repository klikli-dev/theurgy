/*
 * MIT License
 *
 * Copyright 2021 klikli-dev, TheOnlyTails
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

package com.klikli_dev.theurgy.datagen

import com.klikli_dev.theurgy.Theurgy
import net.minecraft.data.DataGenerator
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.client.model.generators.ModelFile
import net.minecraftforge.common.data.ExistingFileHelper

class ItemModelsGenerator(generator: DataGenerator, helper: ExistingFileHelper) :
    ItemModelProvider(generator, Theurgy.MOD_ID, helper) {

    override fun registerModels() {
        registerItemGenerated("grimoire")
    }

    private fun registerItemGenerated(name: String) {
        getBuilder(name)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", Theurgy.id("items/$name"))
    }

    private fun registerItemHandheld(name: String) {
        getBuilder(name)
            .parent(ModelFile.UncheckedModelFile("item/handheld"))
            .texture("layer0", Theurgy.id("items/$name"))
    }

    private fun registerBlockItem(name: String) {
        getBuilder(name)
            .parent(ModelFile.UncheckedModelFile("block/$name"))
    }
}
