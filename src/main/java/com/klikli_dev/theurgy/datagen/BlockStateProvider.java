package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Theurgy.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerCalcinationOven();

    }

    protected void registerCalcinationOven() {
        var calcinationOven = this.models().getExistingFile(this.modLoc("calcination_oven"));
        var calcinationOvenLit = this.models().withExistingParent("calcination_oven_lit", this.modLoc("block/calcination_oven"))
                .texture("texture", this.modLoc("block/calcination_oven_lit"));

        //build blockstate
        this.getVariantBuilder(BlockRegistry.CALCINATION_OVEN.get()) // Get variant builder
                .partialState()
                .with(BlockStateProperties.LIT, false)
                .modelForState()//start setting models
                .modelFile(calcinationOven)
                .addModel()//finish setting models
                .partialState()
                .with(BlockStateProperties.LIT, true)
                .modelForState()//start setting models
                .modelFile(calcinationOvenLit)
                .addModel();

        //add item model
        this.itemModels().withExistingParent("calcination_oven", this.modLoc("block/calcination_oven"))
                .transforms()
                //take defaults from net.minecraft:client:extra/assets/minecraft/models/block/block.json
                //then slightly move down and reduce scale from 0.625 to 0.5
                .transform(ItemTransforms.TransformType.GUI)
                .rotation(30, 225, 0)
                .translation(0, -2.0f, 0)
                .scale(0.5f)
                .end();
    }
}
