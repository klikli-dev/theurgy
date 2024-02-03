// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorBlock;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlock;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


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
        this.registerMercuryCatalyst();
        this.registerCaloricFluxEmitter();
        this.registerSulfuricFluxEmitter();

        this.registerReformationSourcePedestal();
        this.registerReformationTargetPedestal();
        this.registerReformationResultPedestal();

        this.registerFermentationVat();
        this.registerDigestionVat();
        this.registerLogisticsItemInserter();

        this.simpleBlockWithItem(BlockRegistry.SAL_AMMONIAC_ORE.get(), this.cubeAll(BlockRegistry.SAL_AMMONIAC_ORE.get()));
        this.simpleBlockWithItem(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get(), this.cubeAll(BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get()));


        this.simpleBlockWithItem(BlockRegistry.LOGISTICS_NODE.get(), this.cubeAll(BlockRegistry.LOGISTICS_NODE.get()));
    }

    protected void registerLogisticsItemInserter() {
        var model = this.models().withExistingParent("logistics_item_inserter", this.modLoc("block/logistics_connector_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/logistics_connector"))
                .texture("particle", this.mcLoc("block/copper_block"));

        //build blockstate
        this.directionalBlock(BlockRegistry.LOGISTICS_ITEM_INSERTER.get(), model);
        this.simpleBlockItem(BlockRegistry.LOGISTICS_ITEM_INSERTER.get(), model);
    }

    protected void registerLogisticsItemExtractor() {
        var model = this.models().withExistingParent("logistics_item_extractor", this.modLoc("block/logistics_connector_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/logistics_connector"))
                .texture("particle", this.mcLoc("block/copper_block"));

        //build blockstate
        this.directionalBlock(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), model);
        this.simpleBlockItem(BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), model);
    }

    protected void registerFermentationVat() {
        //for now we use a barrel, later we add the appropriate improved textures
        var model = this.models().withExistingParent("fermentation_vat", this.mcLoc("block/cube_bottom_top"))
                .texture("bottom", this.modLoc("block/fermentation_vat_bottom"))
                .texture("side", this.modLoc("block/fermentation_vat_side"))
                .texture("top", this.modLoc("block/fermentation_vat_top"))
                .texture("particle", this.modLoc("block/fermentation_vat_top"));

        var modelOpen = this.models().withExistingParent("fermentation_vat_open", this.mcLoc("block/cube_bottom_top"))
                .texture("bottom", this.modLoc("block/fermentation_vat_bottom"))
                .texture("side", this.modLoc("block/fermentation_vat_side"))
                .texture("top", this.modLoc("block/fermentation_vat_top_open"))
                .texture("particle", this.modLoc("block/fermentation_vat_top"));


        //build blockstate
        this.directionalBlock(BlockRegistry.FERMENTATION_VAT.get(), (state) -> state.getValue(BlockStateProperties.OPEN) ? modelOpen : model);
        this.simpleBlockItem(BlockRegistry.FERMENTATION_VAT.get(), model);
    }

    protected void registerDigestionVat() {
        //rendered by block entity renderer, so just a particle texture but no actual model
        var model = this.models().getBuilder("digestion_vat").texture("particle", "minecraft:block/blue_terracotta");

        this.simpleBlock(BlockRegistry.DIGESTION_VAT.get(), model);

        //needs an item model that allows a BEWLR to render it
        this.itemModels().getBuilder("digestion_vat")
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(BlockModel.GuiLight.FRONT)
                .transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 45, 0)
                .translation(0, -1, 0)
                .scale(0.55f)
                .end()
                .transform(ItemDisplayContext.GROUND)
                .rotation(0, 0, 0)
                .translation(0, 1, 0)
                .scale(0.25f)
                .end()
                .transform(ItemDisplayContext.FIXED)
                .rotation(0, 180, 0)
                .translation(0, 0, 0)
                .scale(0.5f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(0, 90, 0)
                .translation(0, 2, 0.5f)
                .scale(0.375f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 90, 0)
                .translation(0, 0, 0)
                .scale(0.375f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(0, 270, 0)
                .translation(0, 2, 0.5f)
                .scale(0.375f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 270, 0)
                .translation(0, 0, 0)
                .scale(0.375f)
                .end()
                .transform(ItemDisplayContext.HEAD)
                .rotation(0, 180, 0)
                .translation(0, 16, 0)
                .scale(1.5f)
                .end()
        ;


    }

    protected void registerReformationSourcePedestal() {
        var model = this.models().withExistingParent("reformation_source_pedestal", this.modLoc("block/reformation_source_pedestal_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/reformation_source_pedestal"))
                .texture("particle", this.mcLoc("block/copper_block"));

        //build blockstate
        this.simpleBlockWithItem(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get(), model);
    }

    protected void registerReformationTargetPedestal() {
        var model = this.models().withExistingParent("reformation_target_pedestal", this.modLoc("block/reformation_target_pedestal_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/reformation_target_pedestal"))
                .texture("particle", this.mcLoc("block/copper_block"));

        //build blockstate
        this.simpleBlockWithItem(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get(), model);
    }

    protected void registerReformationResultPedestal() {
        var model = this.models().withExistingParent("reformation_result_pedestal", this.modLoc("block/reformation_result_pedestal_template"))
                .ao(false)
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/reformation_result_pedestal"))
                .texture("particle", this.mcLoc("block/copper_block"));

        //build blockstate
        this.simpleBlockWithItem(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get(), model);
    }

    protected void registerCaloricFluxEmitter() {
        var model = this.models().withExistingParent("caloric_flux_emitter", this.modLoc("block/caloric_flux_emitter_template"))
                .ao(false)
                .renderType(new ResourceLocation("minecraft", "translucent"))
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("emitter", this.modLoc("block/caloric_flux_emitter"))
                .texture("socket", this.modLoc("block/emitter_socket"))
                .texture("particle", this.modLoc("block/emitter_socket"));

        //build blockstate
        this.directionalBlock(BlockRegistry.CALORIC_FLUX_EMITTER.get(), model);
        this.simpleBlockItem(BlockRegistry.CALORIC_FLUX_EMITTER.get(), model);
    }

    protected void registerSulfuricFluxEmitter() {
        //we re-use the caloric flux emitter model
        var model = this.models().withExistingParent("sulfuric_flux_emitter", this.modLoc("block/sulfuric_flux_emitter_template"))
                .ao(false)
                .renderType(new ResourceLocation("minecraft", "translucent"))
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("emitter", this.modLoc("block/sulfuric_flux_emitter"))
                .texture("socket", this.modLoc("block/emitter_socket"))
                .texture("particle", this.modLoc("block/emitter_socket"));

        //build blockstate
        this.directionalBlock(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), model);
        this.simpleBlockItem(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), model);
    }

    protected void registerSalAmmoniacAccumulator() {
        //accumulator is rendered by geckolib, so we just give a model with a particle texture
        //we then use it for both the lit and unlit blockstate
        var model = this.models().getBuilder("sal_ammoniac_accumulator").texture("particle", "minecraft:block/copper_block");

        //build blockstate
        this.simpleBlock(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), model);

        //needs an item model that allows geckolib to render
        this.itemModels().getBuilder("sal_ammoniac_accumulator").parent(new ModelFile.UncheckedModelFile("builtin/entity"));
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

    protected void registerMercuryCatalyst() {
        var model = this.models().withExistingParent("mercury_catalyst", this.modLoc("block/mercury_catalyst_template"))
                .ao(false)
                .renderType(new ResourceLocation("minecraft", "translucent"))
                //blockbench spits out garbage textures by losing the folder name so we fix them here
                .texture("texture", this.modLoc("block/mercury_catalyst"))
                .texture("particle", this.mcLoc("block/iron_block"));

        //build blockstate
        this.simpleBlockWithItem(BlockRegistry.MERCURY_CATALYST.get(), model);
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
                .end()
                .transform(ItemDisplayContext.GROUND)
                .rotation(0, 0, 0)
                .translation(0, 3, 0)
                .scale(0.25f)
                .end()
                .transform(ItemDisplayContext.FIXED)
                .rotation(0, 0, 0)
                .translation(0, 3, 0)
                .scale(0.5f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(75, 45, 0)
                .translation(0, 2.5f, 0)
                .scale(0.375f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 45, 0)
                .translation(0, 0, 0)
                .scale(0.4f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(75, 225, 0)
                .translation(0, 2.5f, 0)
                .scale(0.375f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 225, 0)
                .translation(0, 0, 0)
                .scale(0.4f)
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
