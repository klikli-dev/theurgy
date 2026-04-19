// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.model;

import com.geckolib.renderer.internal.GeckolibItemSpecialRenderer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatItemRenderer;
import com.klikli_dev.theurgy.content.apparatus.fermentationvat.FermentationVatBlock;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorBlock;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlock;
import com.klikli_dev.theurgy.content.apparatus.mercurycatalyst.MercuryCatalystBlock;
import com.klikli_dev.theurgy.content.apparatus.mercurycapacitor.MercuryCapacitorBlock;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.mojang.math.Quadrant;
import com.mojang.math.Transformation;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class TheurgyBlockModelSubProvider {

    public void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.registerCalcinationOven(blockModels, itemModels);
        this.registerPyromanticBrazier(blockModels, itemModels);
        this.registerLiquefactionCauldron(blockModels, itemModels);
        this.registerDistiller(blockModels, itemModels);
        this.registerIncubator(blockModels, itemModels);
        this.registerIncubatorVessels(blockModels, itemModels);
        this.registerSalAmmoniacAccumulator(blockModels, itemModels);
        this.registerSalAmmoniacTank(blockModels, itemModels);
        this.registerLogisticsNexus(blockModels, itemModels);
        this.registerMercuryCatalyst(blockModels, itemModels);
        this.registerMercuryCapacitor(blockModels, itemModels);
        this.registerCaloricFluxEmitter(blockModels, itemModels);
        this.registerSulfuricFluxEmitter(blockModels, itemModels);
        this.registerMercuryFluxEmitter(blockModels, itemModels);
        this.registerSimpleTemplateBlock(blockModels, itemModels, BlockRegistry.REFORMATION_SOURCE_PEDESTAL.get(), "block/reformation_source_pedestal_template", Map.of(
                "texture", Theurgy.loc("block/reformation_source_pedestal"),
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));
        this.registerSimpleTemplateBlock(blockModels, itemModels, BlockRegistry.REFORMATION_TARGET_PEDESTAL.get(), "block/reformation_target_pedestal_template", Map.of(
                "texture", Theurgy.loc("block/reformation_target_pedestal"),
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));
        this.registerSimpleTemplateBlock(blockModels, itemModels, BlockRegistry.REFORMATION_RESULT_PEDESTAL.get(), "block/reformation_result_pedestal_template", Map.of(
                "texture", Theurgy.loc("block/reformation_result_pedestal"),
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));
        this.registerFermentationVat(blockModels, itemModels);
        this.registerDigestionVat(blockModels, itemModels);
        this.registerLogisticsConnectorFilter(blockModels);
        this.registerLogisticsConnector(blockModels, itemModels, BlockRegistry.LOGISTICS_ITEM_INSERTER.get(), Theurgy.loc("block/logistics_item_inserter"));
        this.registerLogisticsConnector(blockModels, itemModels, BlockRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), Theurgy.loc("block/logistics_item_extractor"));
        this.registerLogisticsConnector(blockModels, itemModels, BlockRegistry.LOGISTICS_FLUID_INSERTER.get(), Theurgy.loc("block/logistics_fluid_inserter"));
        this.registerLogisticsConnector(blockModels, itemModels, BlockRegistry.LOGISTICS_FLUID_EXTRACTOR.get(), Theurgy.loc("block/logistics_fluid_extractor"));
        this.registerLogisticsConnector(blockModels, itemModels, BlockRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get(), Theurgy.loc("block/logistics_mercury_flux_connector"));
        this.registerLogisticsProbe(blockModels, itemModels, BlockRegistry.LOGISTICS_CAPABILITY_PROBE.get(), Theurgy.loc("block/logistics_capability_probe"));
        this.registerLogisticsCapabilityProxy(blockModels, itemModels, BlockRegistry.LOGISTICS_CAPABILITY_PROXY.get(), Theurgy.loc("block/logistics_proxy"));
        this.registerLogisticsNode(blockModels, itemModels);
        this.registerCubeAll(blockModels, itemModels, BlockRegistry.SAL_AMMONIAC_ORE.get());
        this.registerCubeAll(blockModels, itemModels, BlockRegistry.DEEPSLATE_SAL_AMMONIAC_ORE.get());
        this.registerSalAmmoniacFluid(blockModels);
    }

    private void registerCalcinationOven(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.CALCINATION_OVEN.get()), Identifier.withDefaultNamespace("block/copper_block"));
        this.registerSingleStateBlock(blockModels, BlockRegistry.CALCINATION_OVEN.get(), this.blockModel(BlockRegistry.CALCINATION_OVEN.get()));
        this.registerGeckolibItem(itemModels, BlockRegistry.CALCINATION_OVEN.get(), Identifier.withDefaultNamespace("block/copper_block"));
    }

    private void registerPyromanticBrazier(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(BlockRegistry.PYROMANTIC_BRAZIER.get()), Theurgy.loc("block/pyromantic_brazier_template"), Map.of(
                "texture", Theurgy.loc("block/pyromantic_brazier"),
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));
        this.emitParentModel(blockModels.modelOutput, Theurgy.loc("block/pyromantic_brazier_lit"), Theurgy.loc("block/pyromantic_brazier_lit_template"), Map.of(
                "texture", Theurgy.loc("block/pyromantic_brazier_lit"),
                "particle", Identifier.withDefaultNamespace("block/copper_block"),
                "fire", Identifier.withDefaultNamespace("block/campfire_fire")
        ));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(BlockRegistry.PYROMANTIC_BRAZIER.get());
        generator = generator.with(
                BlockModelGenerators.condition().term(BlockStateProperties.LIT, false),
                BlockModelGenerators.variant(new Variant(this.blockModel(BlockRegistry.PYROMANTIC_BRAZIER.get())))
        );
        generator = generator.with(
                BlockModelGenerators.condition().term(BlockStateProperties.LIT, true),
                BlockModelGenerators.variant(new Variant(Theurgy.loc("block/pyromantic_brazier_lit")))
        );
        blockModels.blockStateOutput.accept(generator);

        this.registerScaledParentedItemModel(itemModels, BlockRegistry.PYROMANTIC_BRAZIER.get(), this.blockModel(BlockRegistry.PYROMANTIC_BRAZIER.get()),
                new Transformation(
                        new Vector3f(1/16f * 1.6f, 1/16f * 1.6f, 1/16f * 1.6f), // compensate for scale shifting model towards origin
                        null,
                        new Vector3f(0.8f, 0.8f, 0.8f), // scale down to 80%
                        null
                ));
    }

    private void registerLiquefactionCauldron(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParentModel(blockModels.modelOutput, Theurgy.loc("block/liquefaction_cauldron_lower"), Theurgy.loc("block/liquefaction_cauldron_template"), Map.of(
                "texture", Theurgy.loc("block/liquefaction_cauldron"),
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));
        this.emitParticleModel(blockModels.modelOutput, Theurgy.loc("block/liquefaction_cauldron_upper"), Identifier.withDefaultNamespace("block/copper_block"));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(BlockRegistry.LIQUEFACTION_CAULDRON.get());
        generator = generator.with(
                BlockModelGenerators.condition().term(LiquefactionCauldronBlock.HALF, DoubleBlockHalf.LOWER),
                BlockModelGenerators.variant(new Variant(Theurgy.loc("block/liquefaction_cauldron_lower")))
        );
        generator = generator.with(
                BlockModelGenerators.condition().term(LiquefactionCauldronBlock.HALF, DoubleBlockHalf.UPPER),
                BlockModelGenerators.variant(new Variant(Theurgy.loc("block/liquefaction_cauldron_upper")))
        );
        blockModels.blockStateOutput.accept(generator);

        this.registerLiquefactionCauldronItemModel(itemModels, BlockRegistry.LIQUEFACTION_CAULDRON.get());
    }

    private void registerLiquefactionCauldronItemModel(ItemModelGenerators itemModels, Block block) {
        this.registerScaledParentedItemModel(itemModels, block, Theurgy.loc("block/liquefaction_cauldron_lower"),
                new Transformation(
                        new Vector3f(1/16f * 6, 1/16f, 0), // nudge left and up to center the model
                        null,
                        new Vector3f(0.625f, 0.625f, 0.625f), // scale down to 62.5%
                        null
                ));
    }

    private void registerDistiller(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.DISTILLER.get()), Identifier.withDefaultNamespace("block/copper_block"));
        this.registerSingleStateBlock(blockModels, BlockRegistry.DISTILLER.get(), this.blockModel(BlockRegistry.DISTILLER.get()));
        this.registerGeckolibItem(itemModels, BlockRegistry.DISTILLER.get(), Identifier.withDefaultNamespace("block/copper_block"));
    }

    private void registerIncubator(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(BlockRegistry.INCUBATOR.get()), Theurgy.loc("block/incubator_template"), Map.of(
                "texture", Theurgy.loc("block/incubator_gold"),
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));
        this.emitParticleModel(blockModels.modelOutput, Theurgy.loc("block/incubator_upper"), Identifier.withDefaultNamespace("block/copper_block"));
        this.emitParentModel(blockModels.modelOutput, Theurgy.loc("block/incubator_pipe"), Theurgy.loc("block/incubator_pipe_template"), Map.of(
                "texture", Theurgy.loc("block/incubator_pipe"),
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(BlockRegistry.INCUBATOR.get());
        generator = generator.with(
                BlockModelGenerators.condition().term(IncubatorBlock.HALF, DoubleBlockHalf.LOWER),
                BlockModelGenerators.variant(new Variant(this.blockModel(BlockRegistry.INCUBATOR.get())))
        );
        generator = generator.with(
                BlockModelGenerators.condition().term(IncubatorBlock.HALF, DoubleBlockHalf.UPPER),
                BlockModelGenerators.variant(new Variant(Theurgy.loc("block/incubator_upper")))
        );

        generator = this.addIncubatorPipe(generator, Direction.NORTH, 0);
        generator = this.addIncubatorPipe(generator, Direction.EAST, 0);
        generator = this.addIncubatorPipe(generator, Direction.SOUTH, 180);
        generator = this.addIncubatorPipe(generator, Direction.WEST, 270);
        blockModels.blockStateOutput.accept(generator);

        this.registerScaledParentedItemModel(itemModels, BlockRegistry.INCUBATOR.get(), this.blockModel(BlockRegistry.INCUBATOR.get()),
                new Transformation(
                        new Vector3f(1/16f * 3f, 1/16f * -3f, 1/16f * 3f), // compensate for scale shifting the tall model towards origin
                        null,
                        new Vector3f(0.625f, 0.625f, 0.625f), // scale down to 62.5% (model extends to Y=30, nearly 2 blocks tall)
                        null
                ));
    }

    private MultiPartGenerator addIncubatorPipe(MultiPartGenerator generator, Direction direction, int yRotation) {
        return generator.with(
                BlockModelGenerators.condition()
                        .term(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true)
                        .term(IncubatorBlock.HALF, DoubleBlockHalf.LOWER),
                BlockModelGenerators.variant(this.variant(Theurgy.loc("block/incubator_pipe"), 0, yRotation, true))
        );
    }

    private void registerIncubatorVessels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        Identifier particleTexture = Identifier.withDefaultNamespace("block/copper_block");
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get()), particleTexture);
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.INCUBATOR_SALT_VESSEL.get()), particleTexture);
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get()), particleTexture);
        this.registerSingleStateBlock(blockModels, BlockRegistry.INCUBATOR_MERCURY_VESSEL.get(), this.blockModel(BlockRegistry.INCUBATOR_MERCURY_VESSEL.get()));
        this.registerSingleStateBlock(blockModels, BlockRegistry.INCUBATOR_SALT_VESSEL.get(), this.blockModel(BlockRegistry.INCUBATOR_SALT_VESSEL.get()));
        this.registerSingleStateBlock(blockModels, BlockRegistry.INCUBATOR_SULFUR_VESSEL.get(), this.blockModel(BlockRegistry.INCUBATOR_SULFUR_VESSEL.get()));
        this.registerGeckolibItem(itemModels, BlockRegistry.INCUBATOR_MERCURY_VESSEL.get(), particleTexture);
        this.registerGeckolibItem(itemModels, BlockRegistry.INCUBATOR_SALT_VESSEL.get(), particleTexture);
        this.registerGeckolibItem(itemModels, BlockRegistry.INCUBATOR_SULFUR_VESSEL.get(), particleTexture);
    }

    private void registerSalAmmoniacAccumulator(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()), Identifier.withDefaultNamespace("block/copper_block"));
        this.registerSingleStateBlock(blockModels, BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), this.blockModel(BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get()));
        this.registerGeckolibItem(itemModels, BlockRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), Identifier.withDefaultNamespace("block/copper_block"));
    }

    private void registerSalAmmoniacTank(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.SAL_AMMONIAC_TANK.get()), Identifier.withDefaultNamespace("block/copper_block"));
        this.registerSingleStateBlock(blockModels, BlockRegistry.SAL_AMMONIAC_TANK.get(), this.blockModel(BlockRegistry.SAL_AMMONIAC_TANK.get()));
        this.registerGeckolibItem(itemModels, BlockRegistry.SAL_AMMONIAC_TANK.get(), Identifier.withDefaultNamespace("block/copper_block"));
    }

    private void registerLogisticsNexus(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.LOGISTICS_NEXUS.get()), Identifier.withDefaultNamespace("block/copper_block"));
        this.registerSingleStateBlock(blockModels, BlockRegistry.LOGISTICS_NEXUS.get(), this.blockModel(BlockRegistry.LOGISTICS_NEXUS.get()));
        this.registerGeckolibItem(itemModels, BlockRegistry.LOGISTICS_NEXUS.get(), Identifier.withDefaultNamespace("block/copper_block"));
    }

    private void registerMercuryCatalyst(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var block = BlockRegistry.MERCURY_CATALYST.get();
        this.emitParentModel(blockModels.modelOutput, this.blockModel(block), Theurgy.loc("block/mercury_catalyst_template"), Map.of(
                "texture", Theurgy.loc("block/mercury_catalyst"),
                "particle", Identifier.withDefaultNamespace("block/iron_block")
        ));
        this.registerSingleStateBlock(blockModels, block, this.blockModel(block));
        this.emitParentModel(itemModels.modelOutput, this.itemModel(block), this.blockModel(block), Map.of());
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(this.itemModel(block), MercuryCatalystBlock.ItemTintSource.INSTANCE));
    }

    private void registerMercuryCapacitor(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var block = BlockRegistry.MERCURY_CAPACITOR.get();
        this.emitParentModel(blockModels.modelOutput, this.blockModel(block), Theurgy.loc("block/mercury_capacitor_template"), Map.of(
                "texture", Theurgy.loc("block/mercury_capacitor"),
                "particle", Identifier.withDefaultNamespace("block/iron_block")
        ));
        this.registerSingleStateBlock(blockModels, block, this.blockModel(block));
        this.emitParentModel(itemModels.modelOutput, this.itemModel(block), this.blockModel(block), Map.of());
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(this.itemModel(block), MercuryCapacitorBlock.ItemTintSource.INSTANCE));
    }

    private void registerCaloricFluxEmitter(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.registerEmitter(blockModels, itemModels, BlockRegistry.CALORIC_FLUX_EMITTER.get(), "block/caloric_flux_emitter_template", Theurgy.loc("block/caloric_flux_emitter"));
    }

    private void registerSulfuricFluxEmitter(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.registerEmitter(blockModels, itemModels, BlockRegistry.SULFURIC_FLUX_EMITTER.get(), "block/sulfuric_flux_emitter_template", Theurgy.loc("block/sulfuric_flux_emitter"));
    }

    private void registerMercuryFluxEmitter(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.registerEmitter(blockModels, itemModels, BlockRegistry.MERCURY_FLUX_EMITTER.get(), "block/mercury_flux_emitter_template", Theurgy.loc("block/mercury_flux_emitter"));
    }

    private void registerEmitter(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block, String parentPath, Identifier emitterTexture) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(block), Theurgy.loc(parentPath), Map.of(
                "emitter", emitterTexture,
                "socket", Theurgy.loc("block/emitter_socket"),
                "particle", Theurgy.loc("block/emitter_socket")
        ));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
        for (Direction direction : Direction.values()) {
            Rotation rotation = this.connectorRotation(direction);
            generator = this.addFacingVariant(generator, BlockStateProperties.FACING, direction, this.blockModel(block), rotation.xDegrees, rotation.yDegrees, false);
        }
        blockModels.blockStateOutput.accept(generator);

        this.registerParentedItemModel(itemModels, block, this.blockModel(block));
    }

    private void registerSimpleTemplateBlock(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block, String parentPath, Map<String, Identifier> textures) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(block), Theurgy.loc(parentPath), textures);
        this.registerSingleStateBlock(blockModels, block, this.blockModel(block));
        this.registerParentedItemModel(itemModels, block, this.blockModel(block));
    }

    private void registerFermentationVat(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(BlockRegistry.FERMENTATION_VAT.get()), Identifier.withDefaultNamespace("block/cube"), Map.of(
                "up", Theurgy.loc("block/fermentation_vat_top"),
                "down", Theurgy.loc("block/fermentation_vat_bottom"),
                "south", Theurgy.loc("block/fermentation_vat_side_front"),
                "north", Theurgy.loc("block/fermentation_vat_side"),
                "west", Theurgy.loc("block/fermentation_vat_side"),
                "east", Theurgy.loc("block/fermentation_vat_side"),
                "particle", Theurgy.loc("block/fermentation_vat_side")
        ));
        this.emitParentModel(blockModels.modelOutput, Theurgy.loc("block/fermentation_vat_open"), this.blockModel(BlockRegistry.FERMENTATION_VAT.get()), Map.of(
                "up", Theurgy.loc("block/fermentation_vat_top_open")
        ));
        this.emitParentModel(blockModels.modelOutput, Theurgy.loc("block/fermentation_vat_active"), this.blockModel(BlockRegistry.FERMENTATION_VAT.get()), Map.of(
                "south", Theurgy.loc("block/fermentation_vat_side_front_active")
        ));
        this.emitParentModel(blockModels.modelOutput, Theurgy.loc("block/fermentation_vat_open_active"), Theurgy.loc("block/fermentation_vat_open"), Map.of(
                "south", Theurgy.loc("block/fermentation_vat_side_front_active")
        ));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(BlockRegistry.FERMENTATION_VAT.get());
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int yRotation = switch (direction) {
                case NORTH -> 180;
                case EAST -> 270;
                case SOUTH -> 0;
                case WEST -> 90;
                default -> throw new IllegalStateException("Unexpected horizontal direction: " + direction);
            };

            generator = this.addFermentationState(generator, direction, false, false, this.blockModel(BlockRegistry.FERMENTATION_VAT.get()), yRotation);
            generator = this.addFermentationState(generator, direction, true, false, Theurgy.loc("block/fermentation_vat_active"), yRotation);
            generator = this.addFermentationState(generator, direction, false, true, Theurgy.loc("block/fermentation_vat_open"), yRotation);
            generator = this.addFermentationState(generator, direction, true, true, Theurgy.loc("block/fermentation_vat_open_active"), yRotation);
        }
        blockModels.blockStateOutput.accept(generator);

        this.registerParentedItemModel(itemModels, BlockRegistry.FERMENTATION_VAT.get(), this.blockModel(BlockRegistry.FERMENTATION_VAT.get()));
    }

    private MultiPartGenerator addFermentationState(MultiPartGenerator generator, Direction direction, boolean hasOutput, boolean open, Identifier model, int yRotation) {
        return generator.with(
                BlockModelGenerators.condition()
                        .term(BlockStateProperties.HORIZONTAL_FACING, direction)
                        .term(FermentationVatBlock.HAS_OUTPUT, hasOutput)
                        .term(BlockStateProperties.OPEN, open),
                BlockModelGenerators.variant(this.variant(model, 0, yRotation, false))
        );
    }

    private void registerDigestionVat(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(BlockRegistry.DIGESTION_VAT.get()), Identifier.withDefaultNamespace("block/blue_terracotta"));
        this.registerSingleStateBlock(blockModels, BlockRegistry.DIGESTION_VAT.get(), this.blockModel(BlockRegistry.DIGESTION_VAT.get()));

        this.registerBuiltinEntityItem(itemModels, BlockRegistry.DIGESTION_VAT.get(), Identifier.withDefaultNamespace("block/blue_terracotta"));
        itemModels.itemModelOutput.accept(BlockRegistry.DIGESTION_VAT.get().asItem(), ItemModelUtils.specialModel(this.itemModel(BlockRegistry.DIGESTION_VAT.get()), new DigestionVatItemRenderer.Unbaked()));
    }

    private void registerLogisticsConnectorFilter(BlockModelGenerators blockModels) {
        this.emitParentModel(blockModels.modelOutput, Theurgy.loc("block/logistics_connector_filter"), Theurgy.loc("block/logistics_connector_filter_template"), Map.of(
                "texture", Theurgy.loc("block/logistics_connector_filter"),
                "particle", Identifier.withDefaultNamespace("block/iron_block")
        ));
    }

    private void registerLogisticsConnector(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block, Identifier texture) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(block), Theurgy.loc("block/logistics_connector_template"), Map.of(
                "texture", texture,
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
        for (Direction direction : Direction.values()) {
            Rotation rotation = this.connectorRotation(direction);
            generator = this.addFacingVariant(generator, BlockStateProperties.FACING, direction, this.blockModel(block), rotation.xDegrees, rotation.yDegrees, false);
            generator = generator.with(
                    BlockModelGenerators.condition()
                            .term(BlockStateProperties.FACING, direction)
                            .term(LogisticsItemConnectorBlock.HAS_FILTER, true),
                    BlockModelGenerators.variant(this.variant(Theurgy.loc("block/logistics_connector_filter"), rotation.xDegrees, rotation.yDegrees, false))
            );
        }
        blockModels.blockStateOutput.accept(generator);

        this.registerParentedItemModel(itemModels, block, this.blockModel(block));
    }

    private void registerLogisticsProbe(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block, Identifier texture) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(block), Theurgy.loc("block/logistics_connector_template"), Map.of(
                "texture", texture,
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
        for (Direction direction : Direction.values()) {
            Rotation rotation = this.connectorRotation(direction);
            generator = this.addFacingVariant(generator, BlockStateProperties.FACING, direction, this.blockModel(block), rotation.xDegrees, rotation.yDegrees, false);
        }
        blockModels.blockStateOutput.accept(generator);

        this.registerParentedItemModel(itemModels, block, this.blockModel(block));
    }

    private void registerLogisticsCapabilityProxy(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block, Identifier texture) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(block), Theurgy.loc("block/logistics_proxy_template"), Map.of(
                "texture", texture,
                "particle", Identifier.withDefaultNamespace("block/copper_block")
        ));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(block);
        for (Direction direction : Direction.values()) {
            Rotation rotation = this.connectorRotation(direction);
            generator = this.addFacingVariant(generator, BlockStateProperties.FACING, direction, this.blockModel(block), rotation.xDegrees, rotation.yDegrees, false);
        }
        blockModels.blockStateOutput.accept(generator);

        this.registerParentedItemModel(itemModels, block, this.blockModel(block));
    }

    private void registerLogisticsNode(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(BlockRegistry.LOGISTICS_CONNECTION_NODE.get()), Theurgy.loc("block/logistics_node_template"), Map.of(
                "connector", Theurgy.loc("block/logistics_node_connector"),
                "base", Theurgy.loc("block/logistics_node_base"),
                "particle", Identifier.withDefaultNamespace("block/terracotta")
        ));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(BlockRegistry.LOGISTICS_CONNECTION_NODE.get());
        for (Direction direction : Direction.values()) {
            Rotation rotation = this.connectorRotation(direction);
            generator = this.addFacingVariant(generator, BlockStateProperties.FACING, direction, this.blockModel(BlockRegistry.LOGISTICS_CONNECTION_NODE.get()), rotation.xDegrees, rotation.yDegrees, false);
        }
        blockModels.blockStateOutput.accept(generator);

        this.registerParentedItemModel(itemModels, BlockRegistry.LOGISTICS_CONNECTION_NODE.get(), this.blockModel(BlockRegistry.LOGISTICS_CONNECTION_NODE.get()));
    }

    private Rotation connectorRotation(Direction direction) {
        return switch (direction) {
            case DOWN -> new Rotation(180, 0);
            case UP -> new Rotation(0, 0);
            case NORTH -> new Rotation(90, 0);
            case SOUTH -> new Rotation(90, 180);
            case WEST -> new Rotation(90, 270);
            case EAST -> new Rotation(90, 90);
        };
    }

    private MultiPartGenerator addFacingVariant(MultiPartGenerator generator, Property<Direction> property, Direction facing, Identifier model, int xRotation, int yRotation, boolean uvLock) {
        return generator.with(
                BlockModelGenerators.condition().term(property, facing),
                BlockModelGenerators.variant(this.variant(model, xRotation, yRotation, uvLock))
        );
    }

    private void registerCubeAll(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block) {
        this.emitParentModel(blockModels.modelOutput, this.blockModel(block), Identifier.withDefaultNamespace("block/cube_all"), Map.of(
                "all", Theurgy.loc("block/" + this.name(block))
        ));
        this.registerSingleStateBlock(blockModels, block, this.blockModel(block));
        this.registerParentedItemModel(itemModels, block, this.blockModel(block));
    }

    private void registerSingleStateBlock(BlockModelGenerators blockModels, Block block, Identifier model) {
        blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block).with(BlockModelGenerators.variant(new Variant(model))));
    }

    private void registerSalAmmoniacFluid(BlockModelGenerators blockModels) {
        Block block = BlockRegistry.SAL_AMMONIAC_FLUID.get();
        this.emitParticleModel(blockModels.modelOutput, this.blockModel(block), Theurgy.loc("block/sal_ammoniac/still"));
        this.registerSingleStateBlock(blockModels, block, this.blockModel(block));
    }

    private void registerParentedItemModel(ItemModelGenerators itemModels, Block block, Identifier parentModel) {
        this.emitParentModel(itemModels.modelOutput, this.itemModel(block), parentModel, Map.of());
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(this.itemModel(block)));
    }

    private void registerScaledParentedItemModel(ItemModelGenerators itemModels, Block block, Identifier parentModel, Transformation transformation) {
        Identifier modelId = this.itemModel(block);
        this.emitParentModel(itemModels.modelOutput, modelId, parentModel, Map.of());
        itemModels.itemModelOutput.accept(
                block.asItem(),
                new CuboidItemModelWrapper.Unbaked(modelId, Optional.of(transformation), List.of())
        );
    }

    private void registerGeckolibItem(ItemModelGenerators itemModels, Block block, Identifier particleTexture) {
        this.emitBuiltinEntityModel(itemModels.modelOutput, this.itemModel(block), particleTexture);
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.specialModel(this.itemModel(block), new GeckolibItemSpecialRenderer.Unbaked()));
    }

    private void registerBuiltinEntityItem(ItemModelGenerators itemModels, Block block, Identifier particleTexture) {
        this.emitBuiltinEntityModel(itemModels.modelOutput, this.itemModel(block), particleTexture);
    }

    private Variant variant(Identifier model, int xRotation, int yRotation, boolean uvLock) {
        Variant variant = new Variant(model);
        if (xRotation != 0) {
            variant = variant.with(VariantMutator.X_ROT.withValue(this.quadrant(xRotation)));
        }
        if (yRotation != 0) {
            variant = variant.with(VariantMutator.Y_ROT.withValue(this.quadrant(yRotation)));
        }
        if (uvLock) {
            variant = variant.with(VariantMutator.UV_LOCK.withValue(true));
        }
        return variant;
    }

    private Quadrant quadrant(int degrees) {
        return switch (Math.floorMod(degrees, 360)) {
            case 0 -> Quadrant.R0;
            case 90 -> Quadrant.R90;
            case 180 -> Quadrant.R180;
            case 270 -> Quadrant.R270;
            default -> throw new IllegalArgumentException("Unsupported rotation: " + degrees);
        };
    }

    private void emitParticleModel(BiConsumer<Identifier, ModelInstance> output, Identifier modelLocation, Identifier particleTexture) {
        this.emitParentModel(output, modelLocation, null, Map.of("particle", particleTexture));
    }

    private void emitParentModel(BiConsumer<Identifier, ModelInstance> output, Identifier modelLocation, Identifier parent, Map<String, Identifier> textures) {
        output.accept(modelLocation, () -> {
            JsonObject json = new JsonObject();
            if (parent != null) {
                json.addProperty("parent", parent.toString());
            }
            if (!textures.isEmpty()) {
                JsonObject textureJson = new JsonObject();
                textures.forEach((key, value) -> textureJson.addProperty(key, value.toString()));
                json.add("textures", textureJson);
            }
            return json;
        });
    }

    /**
     * Emits a special item model JSON with display transforms for special-rendered items
     * (GeckoLib, custom SpecialModelRenderer). Provides the correct isometric appearance
     * in inventory/GUI and proper transforms for all other display contexts.
     */
    private void emitBuiltinEntityModel(BiConsumer<Identifier, ModelInstance> output, Identifier modelLocation, Identifier particleTexture) {
        output.accept(modelLocation, () -> {
            JsonObject json = new JsonObject();

            JsonObject textures = new JsonObject();
            textures.addProperty("particle", particleTexture.toString());
            json.add("textures", textures);

            JsonObject display = new JsonObject();
            display.add("gui", displayTransform(30, 45, 0, 0, 0, 0, 0.625f));
            display.add("ground", displayTransform(0, 0, 0, 0, 3, 0, 0.25f));
            display.add("head", displayTransform(0, 180, 0, 0, 0, 0, 1.0f));
            display.add("fixed", displayTransform(0, 180, 0, 0, 0, 0, 0.5f));
            display.add("thirdperson_righthand", displayTransform(75, 315, 0, 0, 2.5f, 0, 0.375f));
            display.add("firstperson_righthand", displayTransform(0, 315, 0, 0, 0, 0, 0.4f));
            json.add("display", display);

            return json;
        });
    }

    private static JsonObject displayTransform(float rotX, float rotY, float rotZ, float transX, float transY, float transZ, float scale) {
        JsonObject obj = new JsonObject();
        obj.add("rotation", jsonArray(rotX, rotY, rotZ));
        obj.add("translation", jsonArray(transX, transY, transZ));
        obj.add("scale", jsonArray(scale, scale, scale));
        return obj;
    }

    private static JsonArray jsonArray(float x, float y, float z) {
        JsonArray arr = new JsonArray();
        arr.add(x);
        arr.add(y);
        arr.add(z);
        return arr;
    }

    private Identifier blockModel(Block block) {
        return Theurgy.loc("block/" + this.name(block));
    }

    private Identifier itemModel(Block block) {
        return Theurgy.loc("item/" + this.name(block));
    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    private record Rotation(int xDegrees, int yDegrees) {
    }
}

