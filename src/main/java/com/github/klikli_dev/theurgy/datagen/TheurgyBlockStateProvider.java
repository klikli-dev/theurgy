/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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

package com.github.klikli_dev.theurgy.datagen;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TheurgyBlockStateProvider extends BlockStateProvider {
    //region Initialization
    public TheurgyBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Theurgy.MODID, existingFileHelper);
    }
    //endregion Initialization


    //region Overrides
    @Override
    protected void registerStatesAndModels() {
        this.directionalBlock(BlockRegistry.PURE_CRYSTAL.get(),
                this.models().getExistingFile(this.modLoc("block/pure_crystal")));
        this.directionalBlock(BlockRegistry.PRIMA_MATERIA_CRYSTAL.get(),
                this.models().getExistingFile(this.modLoc("block/prima_materia_crystal")));
        this.directionalBlock(BlockRegistry.AER_CRYSTAL.get(),
                this.models().getExistingFile(this.modLoc("block/aer_crystal")));
        this.directionalBlock(BlockRegistry.AQUA_CRYSTAL.get(),
                this.models().getExistingFile(this.modLoc("block/aqua_crystal")));
        this.directionalBlock(BlockRegistry.IGNIS_CRYSTAL.get(),
                this.models().getExistingFile(this.modLoc("block/ignis_crystal")));
        this.directionalBlock(BlockRegistry.TERRA_CRYSTAL.get(),
                this.models().getExistingFile(this.modLoc("block/terra_crystal")));
    }
    //endregion Overrides
}
