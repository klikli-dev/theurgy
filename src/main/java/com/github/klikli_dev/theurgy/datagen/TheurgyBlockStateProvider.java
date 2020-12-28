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
        this.directionalBlock(BlockRegistry.PRIMA_MATERIA_CRYSTAL.get(),
                this.models().getExistingFile(this.modLoc("block/prima_materia_crystal")));
    }
    //endregion Overrides
}
