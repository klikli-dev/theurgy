/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorBlock;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlock;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class TheurgyBlockStateProvider extends BlockStateProvider {
    public TheurgyBlockStateProvider(PackOutput packOutput, ExistingFileHelper exFileHelper) {
        super(packOutput, Theurgy.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerCalcinationOven();
        this.registerPyromanticBrazier();
        this.registerLiquefactionCauldron();
        this.registerDistiller();
        this.registerIncubator();
        this.registerIncubatorVessels();
        this.registerSalAmmoniacAccumulator();
        this.registerSalAmmoniacTank();

        this.simpleBlockWithItem(BlockRegistry.SAL_AMMONIAC_ORE.get(), this.cubeAll(BlockRegistry.SAL_AMMONIAC_ORE.get()));
        this.simpleBlockWithItem(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get(), this.cubeAll(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get()));
    }

    protected void registerSalAmmoniacAccumulator() {
        var model = this.models().withExistingParent("sal_ammoniac_accumulator", this.modLoc("block/sal_ammoniac_accumulator_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/sal_ammoniac_accumulator"))
                .texture("particle", this.mcLoc("block/copper_block"));

        //build blockstate
        this.simpleBlock(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), model);

        //add item model
        this.itemModels().withExistingParent("sal_ammoniac_accumulator", this.modLoc("block/sal_ammoniac_accumulator"));
    }

    protected void registerSalAmmoniacTank() {

        //tank is rendered by geckolib, so we just give a model with a particle texture
        //we then use it for both the lit and unlit blockstate
        var model = this.models().getBuilder("sal_ammoniac_tank").texture("particle", "minecraft:block/copper_block");

        //build blockstate
        this.simpleBlock(BlockRegistry.SAL_AMMONIAC_TANK.get(), model);

        //distiller needs an item model that allows geckolib to render
        this.itemModels().getBuilder("sal_ammoniac_tank").parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    protected void registerDistiller() {

        //distiller is rendered by geckolib, so we just give a model with a particle texture
        //we then use it for both the lit and unlit blockstate
        var model = this.models().getBuilder("distiller").texture("particle", "minecraft:block/copper_block");

        //build blockstate
        this.getVariantBuilder(BlockRegistry.DISTILLER.get()).forAllStates(s -> ConfiguredModel.builder()
                .modelFile(model)
                .build());

        //distiller needs an item model that allows geckolib to render
        this.itemModels().getBuilder("distiller").parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    protected void registerIncubator() {
        var lowerHalfModel = this.models().withExistingParent("incubator_lower", this.modLoc("block/incubator_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/incubator_gold"))
                .texture("particle", this.mcLoc("block/copper_block"));

        //we use an empty upper half model that just shows the particle texture
        var upperHalfModel = this.models().getBuilder("incubator_upper")
                .ao(false)
                .texture("particle", "minecraft:block/copper_block");

        var pipeModel = this.models().withExistingParent("incubator_pipe", this.modLoc("block/incubator_pipe_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/incubator_pipe"))
                .texture("particle", this.mcLoc("block/copper_block"));

        //build blockstate
        MultiPartBlockStateBuilder incubatorBuilder = this.getMultipartBuilder(BlockRegistry.INCUBATOR.get());

        //start with the main model, which only shows on the lower half
        incubatorBuilder.part().modelFile(lowerHalfModel)
                .addModel()
                .condition(IncubatorBlock.HALF, DoubleBlockHalf.LOWER)
                .end();

        //now do the upper half model, which only shows on the upper half and is actually empty
        incubatorBuilder.part().modelFile(upperHalfModel)
                .addModel()
                .condition(IncubatorBlock.HALF, DoubleBlockHalf.UPPER)
                .end();

        //finally do the pipe models, copied and adjusted from fourWayMultipart to only show on the lower half
        PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> {
            Direction dir = e.getKey();
            if (dir.getAxis().isHorizontal()) {
                incubatorBuilder.part().modelFile(pipeModel).rotationY((((int) dir.toYRot()) + 180) % 360).uvLock(true).addModel()
                        .condition(e.getValue(), true)
                        .condition(IncubatorBlock.HALF, DoubleBlockHalf.LOWER);
            }
        });

        //add item model
        this.itemModels().withExistingParent("incubator", lowerHalfModel.getLocation())
                .transforms()
                //take defaults from net.minecraft:client:extra/assets/minecraft/models/block/block.json
                //then slightly move down and reduce scale from 0.625 to 0.5
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 225, 0)
                .translation(0, -2.0f, 0)
                .scale(0.35f)
                .end();
    }

    protected void registerIncubatorVessels() {

        //vessels are rendered by geckolib, so we just give a model with a particle texture
        var incubatorVessel = this.models().getBuilder("incubator_vessel").texture("particle", "minecraft:block/copper_block");

        //build blockstate
        this.simpleBlock(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get(), incubatorVessel);
        this.simpleBlock(BlockRegistry.INCUBATOR_SALT_VESSEL.get(), incubatorVessel);
        this.simpleBlock(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get(), incubatorVessel);

        //vessels need item models that allow geckolib to render
        this.itemModels().getBuilder("incubator_mercury_vessel").parent(new ModelFile.UncheckedModelFile("builtin/entity"));
        this.itemModels().getBuilder("incubator_salt_vessel").parent(new ModelFile.UncheckedModelFile("builtin/entity"));
        this.itemModels().getBuilder("incubator_sulfur_vessel").parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    protected void registerLiquefactionCauldron() {
        var lowerHalfModel = this.models().withExistingParent("liquefaction_cauldron_lower", this.modLoc("block/liquefaction_cauldron_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/liquefaction_cauldron"))
                .texture("particle", this.mcLoc("block/copper_block"));


        //we use an empty upper half model that just shows the particle texture
        var upperHalfModel = this.models().getBuilder("liquefaction_cauldron_upper").texture("particle", "minecraft:block/copper_block");

        //build blockstate
        this.getVariantBuilder(BlockRegistry.LIQUEFACTION_CAULDRON.get()).forAllStates(s -> {
                    var model = s.getValue(LiquefactionCauldronBlock.HALF) == DoubleBlockHalf.LOWER ? lowerHalfModel : upperHalfModel;
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .build();
                }
        );

        //add item model
        this.itemModels().withExistingParent("liquefaction_cauldron", lowerHalfModel.getLocation())
                .transforms()
                //take defaults from net.minecraft:client:extra/assets/minecraft/models/block/block.json
                //then slightly move down and reduce scale from 0.625 to 0.5
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 225, 0)
                .translation(0, -2.0f, 0)
                .scale(0.5f)
                .end();
    }

    protected void registerCalcinationOven() {
        //calcination oven is rendered by geckolib, so we just give an empty model with a particle texture
        //we then use it for both the lit and unlit blockstate
        var model = this.models().getBuilder("calcination_oven").texture("particle", "minecraft:block/copper_block");

        //build blockstate
        this.getVariantBuilder(BlockRegistry.CALCINATION_OVEN.get()).forAllStates(s -> ConfiguredModel.builder()
                .modelFile(model)
                .build());

        //calcination oven needs an item model that allows geckolib to render
        this.itemModels().getBuilder("calcination_oven").parent(new ModelFile.UncheckedModelFile("builtin/entity"));
    }

    protected void registerPyromanticBrazier() {
        var model = this.models().withExistingParent("pyromantic_brazier", this.modLoc("block/pyromantic_brazier_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/pyromantic_brazier"))
                .texture("particle", this.mcLoc("block/copper_block"));

        var modelLit = this.models().withExistingParent("pyromantic_brazier_lit", this.modLoc("block/pyromantic_brazier_lit_template"))
                .renderType("cutout_mipped") //also blockbench doesnt set render type
                .texture("texture", this.modLoc("block/pyromantic_brazier_lit"))
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
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 225, 0)
//                .translation(0, -2.0f, 0)
                .scale(0.5f)
                .end()
        ;
    }
}
