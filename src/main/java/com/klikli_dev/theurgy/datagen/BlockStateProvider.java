package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Theurgy.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerCalcinationOven();
        this.registerPyromanticBrazier();
    }

    protected void registerCalcinationOven() {
        var model = this.models().withExistingParent("calcination_oven", this.modLoc("block/calcination_oven_template"))
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/calcination_oven"))
                .texture("particle", this.mcLoc("block/copper_block"));


        var modelLit = this.models().withExistingParent("calcination_oven_lit", this.modLoc("block/calcination_oven"))
                //we're based on the normal model so we only have to override the texture that changes - the main texture to lit
                .texture("texture", this.modLoc("block/calcination_oven_lit"));

        //build blockstate
        this.getVariantBuilder(BlockRegistry.CALCINATION_OVEN.get()) // Get variant builder
                .partialState()
                .with(BlockStateProperties.LIT, false)
                .modelForState()//start setting models
                .modelFile(model)
                .addModel()//finish setting models
                .partialState()
                .with(BlockStateProperties.LIT, true)
                .modelForState()//start setting models
                .modelFile(modelLit)
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

    protected void registerPyromanticBrazier(){
        var model = this.models().withExistingParent("pyromantic_brazier", this.modLoc("block/pyromantic_brazier_template"))
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/liquefaction_cauldron"))
                .texture("particle", this.mcLoc("block/copper_block"));

        var modelLit = this.models().withExistingParent("pyromantic_brazier_lit", this.modLoc("block/pyromantic_brazier_lit_template"))
                .renderType("cutout_mipped") //also blockbench doesnt set render type
                .texture("texture", this.modLoc("block/liquefaction_cauldron"))
                .texture("particle", this.mcLoc("block/copper_block"))
                .texture("fire", this.mcLoc("block/campfire_fire"));

        //build blockstate
        this.getVariantBuilder(BlockRegistry.PYROMANTIC_BRAZIER.get()) // Get variant builder
                .partialState()
                .with(BlockStateProperties.LIT, false)
                .modelForState()//start setting models
                .modelFile(model)
                .addModel()//finish setting models
                .partialState()
                .with(BlockStateProperties.LIT, true)
                .modelForState()//start setting models
                .modelFile(modelLit)
                .addModel();

        //add item model
        this.itemModels().withExistingParent("pyromantic_brazier", this.modLoc("block/pyromantic_brazier"))
                .transforms()
                //take defaults from net.minecraft:client:extra/assets/minecraft/models/block/block.json
                //then slightly move down and reduce scale from 0.625 to 0.5
                .transform(ItemTransforms.TransformType.GUI)
                .rotation(30, 225, 0)
//                .translation(0, -2.0f, 0)
                .scale(0.5f)
                .end()
        ;
    }
}
