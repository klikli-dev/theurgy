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
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ModelsResourceUtil;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
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

        this.directionalBlock(BlockRegistry.ESSENTIA_RECEIVER.get(),
                this.getExistingModel(BlockRegistry.ESSENTIA_RECEIVER.get()));
        this.generateEmitterState(BlockRegistry.ESSENTIA_EMITTER.get());

        this.directionalBlock(BlockRegistry.AETHER_RECEIVER.get(),
                this.getExistingModel(BlockRegistry.AETHER_RECEIVER.get()));
        this.generateEmitterState(BlockRegistry.AETHER_EMITTER.get());
    }
    //endregion Overrides

    //region Methods
    protected ModelFile.ExistingModelFile getExistingModel(Block block){
        return this.models().getExistingFile(ModelsResourceUtil.func_240221_a_(block));
    }

    protected void generateEmitterState(Block block) {
        ModelFile.ExistingModelFile enabledModel =
                this.models().getExistingFile(ModelsResourceUtil.func_240221_a_(block));
        ModelFile.ExistingModelFile disabledModel =
                this.models().getExistingFile(ModelsResourceUtil.func_240222_a_(block, "_disabled"));

        //copied from directional block, except model file depends on blockstate property "enabled",
        //and angle offset (((int) dir.getHorizontalAngle()) + 180) is copied from constant
        this.getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.get(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                                   .modelFile(state.get(BlockStateProperties.ENABLED) ? enabledModel : disabledModel)
                                   .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                                   .rotationY(dir.getAxis().isVertical() ? 0 :
                                                      (((int) dir.getHorizontalAngle()) + 180) % 360)
                                   .build();
                });

    }
    //endregion Methods
}
