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
import com.klikli_dev.theurgy.block.GraftingHedgeBlock;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(DataGenerator gen,
                               ExistingFileHelper exFileHelper) {
        super(gen, Theurgy.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.graftingHedge();
    }

    private void graftingHedge(){
        //The hedge uses a flower-free model before grafted, and when ripe (the latter case uses a tile entity renderer)
        ModelFile.ExistingModelFile graftingHedgeModel = this.models().getExistingFile(this.modLoc("block/grafting_hedge"));
        this.getVariantBuilder(BlockRegistry.GRAFTING_HEDGE.get()).forAllStates(state -> {
            if(!state.getValue(GraftingHedgeBlock.IS_GRAFTED))
                return ConfiguredModel.builder().modelFile(graftingHedgeModel).build();

            int age = state.getValue(GraftingHedgeBlock.AGE);
            return ConfiguredModel.builder().modelFile(age == GraftingHedgeBlock.MAX_AGE ?
                    graftingHedgeModel :
                    this.models().getExistingFile(this.modLoc("block/grafting_hedge_flowered_" + age))
            ).build();
        });
    }
}
