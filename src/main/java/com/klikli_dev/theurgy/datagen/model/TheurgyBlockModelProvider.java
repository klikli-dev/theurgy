// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.fermentationvat.FermentationVatBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorBlock;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlock;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.client.data.models.blockstates.Condition;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TheurgyBlockModelProvider extends ModelProvider {

    public static final TextureSlot TEXTURE = TextureSlot.create("texture");
    public static final TextureSlot EMITTER = TextureSlot.create("emitter");
    public static final TextureSlot SOCKET = TextureSlot.create("socket");
    public static final TextureSlot CONNECTOR = TextureSlot.create("connector");
    public static final TextureSlot BASE = TextureSlot.create("base");
    public static final TextureSlot FIRE = TextureSlot.create("fire");

    public TheurgyBlockModelProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.registerCalcinationOven(blockModels, itemModels);
        this.registerPyromanticBrazier(blockModels, itemModels);
        this.registerLiquefactionCauldron(blockModels, itemModels);
        this.registerDistiller(blockModels, itemModels);
        this.registerIncubator(blockModels, itemModels);
        this.registerIncubatorVessels(blockModels, itemModels);
        this.registerSalAmmoniacAccumulator(blockModels, itemModels);
        this.registerSalAmmoniacTank(blockModels, itemModels);
        this.registerMercuryCatalyst(blockModels, itemModels);
        this.registerCaloricFluxEmitter(blockModels, itemModels);
        this.registerSulfuricFluxEmitter(blockModels, itemModels);

        this.registerReformationSourcePedestal(blockModels, itemModels);
        this.registerReformationTargetPedestal(blockModels, itemModels);
        this.registerReformationResultPedestal(blockModels, itemModels);

        this.registerFermentationVat(blockModels, itemModels);
        this.registerDigestionVat(blockModels, itemModels);

        ResourceLocation logisticsFilter = this.registerLogisticsFilterModel(blockModels);
        this.registerLogisticsItemInserter(blockModels, itemModels, logisticsFilter);
        this.registerLogisticsItemExtractor(blockModels, itemModels, logisticsFilter);
        this.registerLogisticsFluidInserter(blockModels, itemModels, logisticsFilter);
        this.registerLogisticsFluidExtractor(blockModels, itemModels, logisticsFilter);
        this.registerLogisticsNode(blockModels, itemModels);

        this.simpleBlockWithItem(blockModels, BlockRegistry.SAL_AMMONIAC_ORE.get());
        this.simpleBlockWithItem(blockModels, BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get());
    }

    private ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(Theurgy.MODID, path);
    }

    private ResourceLocation mcLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    private void simpleBlockWithItem(BlockModelGenerators blockModels, Block block) {
        blockModels.createTrivialCube(block);
    }

    private void simpleBlockWithItem(BlockModelGenerators blockModels, Block block, ResourceLocation modelLocation) {
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, modelLocation));
    }

    private void registerCalcinationOven(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var model = new ModelTemplate(Optional.empty(), Optional.empty(), TextureSlot.PARTICLE);
        var texture = new TextureMapping().put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        var modelLoc = model.create(BlockRegistry.CALCINATION_OVEN.get(), texture, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockRegistry.CALCINATION_OVEN.get(), modelLoc));

        ModelTemplate itemTemplate = new ModelTemplate(Optional.of(mcLoc("builtin/entity")), Optional.empty());
        itemTemplate.create(BlockRegistry.CALCINATION_OVEN.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }

    private void registerDistiller(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var model = new ModelTemplate(Optional.empty(), Optional.empty(), TextureSlot.PARTICLE);
        var texture = new TextureMapping().put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        var modelLoc = model.create(BlockRegistry.DISTILLER.get(), texture, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockRegistry.DISTILLER.get(), modelLoc));

        ModelTemplate itemTemplate = new ModelTemplate(Optional.of(mcLoc("builtin/entity")), Optional.empty());
        itemTemplate.create(BlockRegistry.DISTILLER.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }

    private void registerSalAmmoniacAccumulator(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var model = new ModelTemplate(Optional.empty(), Optional.empty(), TextureSlot.PARTICLE);
        var texture = new TextureMapping().put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        var modelLoc = model.create(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), texture, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), modelLoc));

        ModelTemplate itemTemplate = new ModelTemplate(Optional.of(mcLoc("builtin/entity")), Optional.empty());
        itemTemplate.create(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }

    private void registerSalAmmoniacTank(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var model = new ModelTemplate(Optional.empty(), Optional.empty(), TextureSlot.PARTICLE);
        var texture = new TextureMapping().put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        var modelLoc = model.create(BlockRegistry.SAL_AMMONIAC_TANK.get(), texture, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockRegistry.SAL_AMMONIAC_TANK.get(), modelLoc));

        ModelTemplate itemTemplate = new ModelTemplate(Optional.of(mcLoc("builtin/entity")), Optional.empty());
        itemTemplate.create(BlockRegistry.SAL_AMMONIAC_TANK.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }

    private void registerIncubatorVessels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var model = new ModelTemplate(Optional.empty(), Optional.empty(), TextureSlot.PARTICLE);
        var texture = new TextureMapping().put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        var modelLoc = model.create(loc("block/incubator_vessel"), texture, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get(), modelLoc));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockRegistry.INCUBATOR_SALT_VESSEL.get(), modelLoc));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get(), modelLoc));

        ModelTemplate itemTemplate = new ModelTemplate(Optional.of(mcLoc("builtin/entity")), Optional.empty());
        itemTemplate.create(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get().asItem(), new TextureMapping(), itemModels.modelOutput);
        itemTemplate.create(BlockRegistry.INCUBATOR_SALT_VESSEL.get().asItem(), new TextureMapping(), itemModels.modelOutput);
        itemTemplate.create(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }

    private void registerDigestionVat(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var model = new ModelTemplate(Optional.empty(), Optional.empty(), TextureSlot.PARTICLE);
        var texture = new TextureMapping().put(TextureSlot.PARTICLE, mcLoc("block/blue_terracotta"));
        var modelLoc = model.create(BlockRegistry.DIGESTION_VAT.get(), texture, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(BlockRegistry.DIGESTION_VAT.get(), modelLoc));

        ModelTemplate itemTemplate = new ModelTemplate(Optional.of(mcLoc("builtin/entity")), Optional.empty());
        itemTemplate.create(BlockRegistry.DIGESTION_VAT.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }

    private void registerPyromanticBrazier(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var unlit = new ModelTemplate(Optional.of(loc("block/pyromantic_brazier_template")), Optional.empty(), BASE, EMITTER)
                .create(BlockRegistry.PYROMANTIC_BRAZIER.get(), new TextureMapping()
                        .put(BASE, loc("block/pyromantic_brazier_base"))
                        .put(EMITTER, loc("block/pyromantic_brazier_bowl")), blockModels.modelOutput);
        
        var lit = new ModelTemplate(Optional.of(loc("block/pyromantic_brazier_template")), Optional.empty(), BASE, EMITTER)
                .createWithSuffix(BlockRegistry.PYROMANTIC_BRAZIER.get(), "_lit", new TextureMapping()
                        .put(BASE, loc("block/pyromantic_brazier_base"))
                        .put(EMITTER, loc("block/pyromantic_brazier_fire")), blockModels.modelOutput);

        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(BlockRegistry.PYROMANTIC_BRAZIER.get()).with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT, lit, unlit)));
    }

    private void registerLiquefactionCauldron(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModelTemplate lowerTemplate = new ModelTemplate(Optional.of(loc("block/liquefaction_cauldron_template")), Optional.of("_lower"), TEXTURE, TextureSlot.PARTICLE);
        TextureMapping lowerMap = new TextureMapping()
                .put(TEXTURE, loc("block/liquefaction_cauldron"))
                .put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        ResourceLocation lowerModel = lowerTemplate.create(BlockRegistry.LIQUEFACTION_CAULDRON.get(), lowerMap, blockModels.modelOutput);

        ModelTemplate upperTemplate = new ModelTemplate(Optional.empty(), Optional.of("_upper"), TextureSlot.PARTICLE);
        TextureMapping upperMap = new TextureMapping()
                .put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        ResourceLocation upperModel = upperTemplate.create(BlockRegistry.LIQUEFACTION_CAULDRON.get(), upperMap, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(BlockRegistry.LIQUEFACTION_CAULDRON.get()).with(
            PropertyDispatch.property(LiquefactionCauldronBlock.HALF)
                .select(DoubleBlockHalf.LOWER, Variant.variant().with(VariantProperties.MODEL, lowerModel))
                .select(DoubleBlockHalf.UPPER, Variant.variant().with(VariantProperties.MODEL, upperModel))));
    }

    private void registerReformationSourcePedestal(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModelTemplate template = new ModelTemplate(Optional.of(loc("block/reformation_source_pedestal_template")), Optional.empty(), TEXTURE, TextureSlot.PARTICLE);
        TextureMapping map = new TextureMapping()
                .put(TEXTURE, loc("block/reformation_source_pedestal"))
                .put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        ResourceLocation model = template.create(BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get(), map, blockModels.modelOutput);

        simpleBlockWithItem(blockModels, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get(), model);
    }

    private void registerReformationTargetPedestal(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModelTemplate template = new ModelTemplate(Optional.of(loc("block/reformation_target_pedestal_template")), Optional.empty(), TEXTURE, TextureSlot.PARTICLE);
        TextureMapping map = new TextureMapping()
                .put(TEXTURE, loc("block/reformation_target_pedestal"))
                .put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        ResourceLocation model = template.create(BlockRegistry.REFORMATION_TARGET_PEDESTAL.get(), map, blockModels.modelOutput);

        simpleBlockWithItem(blockModels, BlockRegistry.REFORMATION_TARGET_PEDESTAL.get(), model);
    }

    private void registerReformationResultPedestal(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModelTemplate template = new ModelTemplate(Optional.of(loc("block/reformation_result_pedestal_template")), Optional.empty(), TEXTURE, TextureSlot.PARTICLE);
        TextureMapping map = new TextureMapping()
                .put(TEXTURE, loc("block/reformation_result_pedestal"))
                .put(TextureSlot.PARTICLE, mcLoc("block/copper_block"));
        ResourceLocation model = template.create(BlockRegistry.REFORMATION_RESULT_PEDESTAL.get(), map, blockModels.modelOutput);

        simpleBlockWithItem(blockModels, BlockRegistry.REFORMATION_RESULT_PEDESTAL.get(), model);
    }

    private void registerMercuryCatalyst(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModelTemplate template = new ModelTemplate(Optional.of(loc("block/mercury_catalyst_template")), Optional.empty(), TEXTURE, TextureSlot.PARTICLE);
        TextureMapping map = new TextureMapping()
                .put(TEXTURE, loc("block/mercury_catalyst"))
                .put(TextureSlot.PARTICLE, mcLoc("block/iron_block"));
        ResourceLocation model = template.create(BlockRegistry.MERCURY_CATALYST.get(), map, blockModels.modelOutput);

        simpleBlockWithItem(blockModels, BlockRegistry.MERCURY_CATALYST.get(), model);
    }

    private void registerCaloricFluxEmitter(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModelTemplate template = new ModelTemplate(Optional.of(loc("block/caloric_flux_emitter_template")), Optional.empty(), EMITTER, SOCKET, TextureSlot.PARTICLE);
        TextureMapping map = new TextureMapping()
                .put(EMITTER, loc("block/caloric_flux_emitter"))
                .put(SOCKET, loc("block/emitter_socket"))
                .put(TextureSlot.PARTICLE, loc("block/emitter_socket"));

        ResourceLocation model = template.create(BlockRegistry.CALORIC_FLUX_EMITTER.get(), map, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(BlockRegistry.CALORIC_FLUX_EMITTER.get())
                .with(createDirectionalDispatch(model))
                .with(PropertyDispatch.property(com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlock.ENABLED)
                        .select(true, Variant.variant())
                        .select(false, Variant.variant())));
    }

    private void registerSulfuricFluxEmitter(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModelTemplate template = new ModelTemplate(Optional.of(loc("block/sulfuric_flux_emitter_template")), Optional.empty(), EMITTER, SOCKET, TextureSlot.PARTICLE);
        TextureMapping map = new TextureMapping()
                .put(EMITTER, loc("block/sulfuric_flux_emitter"))
                .put(SOCKET, loc("block/emitter_socket"))
                .put(TextureSlot.PARTICLE, loc("block/emitter_socket"));

        ResourceLocation model = template.create(BlockRegistry.SULFURIC_FLUX_EMITTER.get(), map, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(BlockRegistry.SULFURIC_FLUX_EMITTER.get())
                .with(createDirectionalDispatch(model)));
    }

    private PropertyDispatch createDirectionalDispatch(ResourceLocation model) {
        return PropertyDispatch.property(BlockStateProperties.FACING)
                .select(Direction.DOWN, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .select(Direction.UP, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                .select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, model))
                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, model).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
    }

    private void registerIncubator(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var lowerHalfModel = new ModelTemplate(Optional.of(loc("block/incubator_template")), Optional.empty(), TEXTURE, TextureSlot.PARTICLE)
                .create(BlockRegistry.INCUBATOR.get(), new TextureMapping()
                        .put(TEXTURE, loc("block/incubator_gold"))
                        .put(TextureSlot.PARTICLE, mcLoc("block/copper_block")), blockModels.modelOutput);

        var upperHalfModel = new ModelTemplate(Optional.empty(), Optional.of("_upper"), TextureSlot.PARTICLE)
                .create(BlockRegistry.INCUBATOR.get(), new TextureMapping()
                        .put(TextureSlot.PARTICLE, mcLoc("block/copper_block")), blockModels.modelOutput);

        var pipeModel = new ModelTemplate(Optional.of(loc("block/incubator_pipe_template")), Optional.of("_pipe"), TEXTURE, TextureSlot.PARTICLE)
                .create(BlockRegistry.INCUBATOR.get(), new TextureMapping()
                        .put(TEXTURE, loc("block/incubator_pipe"))
                        .put(TextureSlot.PARTICLE, mcLoc("block/copper_block")), blockModels.modelOutput);

        MultiPartGenerator generator = MultiPartGenerator.multiPart(BlockRegistry.INCUBATOR.get())
                .with(Condition.condition().term(IncubatorBlock.HALF, DoubleBlockHalf.LOWER), Variant.variant().with(VariantProperties.MODEL, lowerHalfModel))
                .with(Condition.condition().term(IncubatorBlock.HALF, DoubleBlockHalf.UPPER), Variant.variant().with(VariantProperties.MODEL, upperHalfModel));

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            generator.with(Condition.and(
                            Condition.condition().term(PipeBlock.PROPERTY_BY_DIRECTION.get(dir), true),
                            Condition.condition().term(IncubatorBlock.HALF, DoubleBlockHalf.LOWER)
                    ),
                    Variant.variant().with(VariantProperties.MODEL, pipeModel)
                            .with(VariantProperties.Y_ROT, getRotation((int) dir.toYRot() + 180))
                            .with(VariantProperties.UV_LOCK, true)
            );
        }

        blockModels.blockStateOutput.accept(generator);
        
        new ModelTemplate(Optional.of(lowerHalfModel), Optional.empty()).create(BlockRegistry.INCUBATOR.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }

    private void registerFermentationVat(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        TextureMapping closedMap = new TextureMapping()
                .put(TextureSlot.UP, loc("block/fermentation_vat_top"))
                .put(TextureSlot.DOWN, loc("block/fermentation_vat_bottom"))
                .put(TextureSlot.SOUTH, loc("block/fermentation_vat_side_front"))
                .put(TextureSlot.NORTH, loc("block/fermentation_vat_side"))
                .put(TextureSlot.WEST, loc("block/fermentation_vat_side"))
                .put(TextureSlot.EAST, loc("block/fermentation_vat_side"))
                .put(TextureSlot.PARTICLE, loc("block/fermentation_vat_side"));
        ResourceLocation modelClosed = ModelTemplates.CUBE.create(BlockRegistry.FERMENTATION_VAT.get(), closedMap, blockModels.modelOutput);

        ModelTemplate templateOpen = new ModelTemplate(Optional.of(modelClosed), Optional.empty(), TextureSlot.UP);
        TextureMapping mapOpen = new TextureMapping().put(TextureSlot.UP, loc("block/fermentation_vat_top_open"));
        ResourceLocation modelOpen = templateOpen.createWithSuffix(BlockRegistry.FERMENTATION_VAT.get(), "_open", mapOpen, blockModels.modelOutput);

        ModelTemplate templateClosedActive = new ModelTemplate(Optional.of(modelClosed), Optional.empty(), TextureSlot.SOUTH);
        TextureMapping mapActive = new TextureMapping().put(TextureSlot.SOUTH, loc("block/fermentation_vat_side_front_active"));
        ResourceLocation modelClosedActive = templateClosedActive.createWithSuffix(BlockRegistry.FERMENTATION_VAT.get(), "_active", mapActive, blockModels.modelOutput);

        ModelTemplate templateOpenActive = new ModelTemplate(Optional.of(modelOpen), Optional.empty(), TextureSlot.SOUTH);
        ResourceLocation modelOpenActive = templateOpenActive.createWithSuffix(BlockRegistry.FERMENTATION_VAT.get(), "_open_active", mapActive, blockModels.modelOutput);

        MultiPartGenerator generator = MultiPartGenerator.multiPart(BlockRegistry.FERMENTATION_VAT.get());
        
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            for (boolean open : List.of(false, true)) {
                for (boolean output : List.of(false, true)) {
                    ResourceLocation model = open ? (output ? modelOpenActive : modelOpen) : (output ? modelClosedActive : modelClosed);
                    
                    generator.with(Condition.condition()
                            .term(BlockStateProperties.HORIZONTAL_FACING, dir)
                            .term(BlockStateProperties.OPEN, open)
                            .term(FermentationVatBlock.HAS_OUTPUT, output),
                            Variant.variant()
                                    .with(VariantProperties.MODEL, model)
                                    .with(VariantProperties.Y_ROT, getRotation((int) dir.toYRot())));
                }
            }
        }
        
        blockModels.blockStateOutput.accept(generator);

        new ModelTemplate(Optional.of(modelClosed), Optional.empty()).create(BlockRegistry.FERMENTATION_VAT.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }

    private ResourceLocation registerLogisticsFilterModel(BlockModelGenerators blockModels) {
        ModelTemplate template = new ModelTemplate(Optional.of(loc("block/logistics_connector_filter_template")), Optional.of("_filter"), TEXTURE, TextureSlot.PARTICLE);
        TextureMapping map = new TextureMapping()
            .put(TEXTURE, loc("block/logistics_connector_filter"))
            .put(TextureSlot.PARTICLE, mcLoc("block/iron_block"));
        return template.create(loc("block/logistics_connector_filter"), map, blockModels.modelOutput);
    }

    private void registerLogisticsItemInserter(BlockModelGenerators blockModels, ItemModelGenerators itemModels, ResourceLocation filter) {
        registerLogisticsBlock(blockModels, itemModels, BlockRegistry.LOGISTICS_ITEM_INSERTER.get(), "block/logistics_item_inserter", filter);
    }

    private void registerLogisticsItemExtractor(BlockModelGenerators blockModels, ItemModelGenerators itemModels, ResourceLocation filter) {
        registerLogisticsBlock(blockModels, itemModels, BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), "block/logistics_item_extractor", filter);
    }

    private void registerLogisticsFluidInserter(BlockModelGenerators blockModels, ItemModelGenerators itemModels, ResourceLocation filter) {
        registerLogisticsBlock(blockModels, itemModels, BlockRegistry.LOGISTICS_FLUID_INSERTER.get(), "block/logistics_fluid_inserter", filter);
    }

    private void registerLogisticsFluidExtractor(BlockModelGenerators blockModels, ItemModelGenerators itemModels, ResourceLocation filter) {
        registerLogisticsBlock(blockModels, itemModels, BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get(), "block/logistics_fluid_extractor", filter);
    }

    private void registerLogisticsBlock(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block, String texturePath, ResourceLocation filter) {
        var model = new ModelTemplate(Optional.of(loc("block/logistics_connector_template")), Optional.empty(), TEXTURE, TextureSlot.PARTICLE)
                .create(block, new TextureMapping()
                        .put(TEXTURE, loc(texturePath))
                        .put(TextureSlot.PARTICLE, mcLoc("block/copper_block")), blockModels.modelOutput);

        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);

        for (Direction dir : Direction.values()) {
            int xRot = dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0;
            int yRot = dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360;

            generator.with(Condition.condition().term(BlockStateProperties.FACING, dir),
                    Variant.variant().with(VariantProperties.MODEL, model)
                            .with(VariantProperties.X_ROT, getRotation(xRot))
                            .with(VariantProperties.Y_ROT, getRotation(yRot))
            );

            generator.with(Condition.and(
                            Condition.condition().term(BlockStateProperties.FACING, dir),
                            Condition.condition().term(LogisticsItemConnectorBlock.HAS_FILTER, true)
                    ),
                    Variant.variant().with(VariantProperties.MODEL, filter)
                            .with(VariantProperties.X_ROT, getRotation(xRot))
                            .with(VariantProperties.Y_ROT, getRotation(yRot))
            );
        }

        blockModels.blockStateOutput.accept(generator);
        
        new ModelTemplate(Optional.of(model), Optional.empty()).create(block.asItem(), new TextureMapping(), itemModels.modelOutput);
    }
    
    private VariantProperties.Rotation getRotation(int degrees) {
        return switch (degrees) {
            case 90 -> VariantProperties.Rotation.R90;
            case 180 -> VariantProperties.Rotation.R180;
            case 270 -> VariantProperties.Rotation.R270;
            default -> VariantProperties.Rotation.R0;
        };
    }

    private void registerLogisticsNode(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var model = new ModelTemplate(Optional.of(loc("block/logistics_node_template")), Optional.empty(), CONNECTOR, BASE, TextureSlot.PARTICLE)
                .create(BlockRegistry.LOGISTICS_CONNECTION_NODE.get(), new TextureMapping()
                        .put(CONNECTOR, loc("block/logistics_node_connector"))
                        .put(BASE, loc("block/logistics_node_base"))
                        .put(TextureSlot.PARTICLE, mcLoc("block/terracotta")), blockModels.modelOutput);
        
        // Directional block
        // We can reuse the rotation logic from logistics block but without filter
        MultiPartGenerator generator = MultiPartGenerator.multiPart(BlockRegistry.LOGISTICS_CONNECTION_NODE.get());
        for (Direction dir : Direction.values()) {
            // Check logic in TheurgyBlockStateProvider:
            // .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
            // .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
            // angleOffset was 180? Wait, let's check old code below
            
            // "rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)" 
            // In registerLogisticsFluidInserter: angleOffset = 180.
            
            int xRot = dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0;
            int yRot = dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360;

             generator.with(Condition.condition().term(BlockStateProperties.FACING, dir),
                    Variant.variant().with(VariantProperties.MODEL, model)
                            .with(VariantProperties.X_ROT, getRotation(xRot))
                            .with(VariantProperties.Y_ROT, getRotation(yRot))
            );
        }
        blockModels.blockStateOutput.accept(generator);

        new ModelTemplate(Optional.of(model), Optional.empty()).create(BlockRegistry.LOGISTICS_CONNECTION_NODE.get().asItem(), new TextureMapping(), itemModels.modelOutput);
    }
}
