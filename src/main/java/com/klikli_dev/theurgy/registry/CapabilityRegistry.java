// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.CalcinationOvenBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.distiller.DistillerBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.incubator.IncubatorBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.liquefactioncauldron.LiquefactionCauldronBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.logisticsmercuryfluxconnector.LogisticsMercuryFluxConnectorBlockEntity;
import com.klikli_dev.theurgy.content.capability.HeatProvider;
import com.klikli_dev.theurgy.content.capability.HeatReceiver;
import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class CapabilityRegistry {

    public static final BlockCapability<ResourceHandler<ItemResource>, @Nullable Direction> ITEM_HANDLER = Capabilities.Item.BLOCK;
    public static final BlockCapability<ResourceHandler<FluidResource>, @Nullable Direction> FLUID_HANDLER = Capabilities.Fluid.BLOCK;

    public static final BlockCapability<MercuryFluxStorage, @Nullable Direction> MERCURY_FLUX_HANDLER = BlockCapability.createSided(
            Theurgy.loc("mercury_flux_handler"),
            MercuryFluxStorage.class);
    public static BlockCapability<HeatProvider, @Nullable Direction> HEAT_PROVIDER = BlockCapability.createSided(
            Theurgy.loc("heat_provider"),
            HeatProvider.class);
    public static BlockCapability<HeatReceiver, @Nullable Direction> HEAT_RECEIVER = BlockCapability.createSided(
            Theurgy.loc("heat_receiver"),
            HeatReceiver.class);

    private static <E extends BlockEntity, T, C> T doubleBlockCapability(Level level, BlockPos pos, @Nullable BlockEntity blockEntity, C context, Class<E> clazz, BiFunction<E, C, T> propertyGetter) {
        if (clazz.isInstance(blockEntity)) {
            return propertyGetter.apply(clazz.cast(blockEntity), context);
        } else if (level.getBlockState(pos).getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            BlockPos below = pos.below();
            BlockEntity blockEntityBelow = level.getBlockEntity(below);
            if (clazz.isInstance(blockEntityBelow)) {
                return propertyGetter.apply(clazz.cast(blockEntityBelow), context);
            }
        }
        return null;
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        registerCalcinationOven(event);
        registerCaloricFluxEmitter(event);
        registerDigestionVat(event);
        registerDistiller(event);
        registerFermentationVat(event);
        registerIncubator(event);
        registerLiquefactionCauldron(event);
        registerLogisticsCapabilityProxy(event);
        registerLogisticsMercuryFluxConnector(event);
        registerMercuryCatalyst(event);
        registerMercuryCapacitor(event);
        registerPyromanticBrazier(event);
        registerReformationArray(event);
        registerSalAmmoniacAccumulator(event);
        registerSalAmmoniacTank(event);
    }

    public static void registerCalcinationOven(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                ITEM_HANDLER,
                (level, pos, state, be, context) ->
                        doubleBlockCapability(level, pos, be, context, CalcinationOvenBlockEntity.class,
                                (blockEntity, side) -> {
                                    if (side == Direction.UP) {
                                        return blockEntity.storageBehaviour.inputInventory;
                                    } else if (side == Direction.DOWN) {
                                        return blockEntity.storageBehaviour.outputInventoryExtractOnlyWrapper;
                                    } else {
                                        return blockEntity.storageBehaviour.inventory;
                                    }
                                }
                        ),
                BlockRegistry.CALCINATION_OVEN.get()
        );

        event.registerBlockEntity(
                HEAT_RECEIVER,
                BlockEntityRegistry.CALCINATION_OVEN.get(),
                (blockEntity, side) -> blockEntity.heatReceiver);
    }

    public static void registerCaloricFluxEmitter(@NotNull RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.CALORIC_FLUX_EMITTER.get(),
                (blockEntity, side) -> blockEntity.mercuryFluxStorage);
    }

    public static void registerDigestionVat(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.DIGESTION_VAT.get(),
                (blockEntity, side) -> {
                    if (side == Direction.UP) {
                        return blockEntity.storageBehaviour.inputInventoryOpenCloseAwareWrapper;
                    } else if (side == Direction.DOWN) {
                        return blockEntity.storageBehaviour.outputInventoryOpenCloseAwareWrapper;
                    } else {
                        return blockEntity.storageBehaviour.inventoryOpenCloseAwareWrapper;
                    }
                }
        );

        event.registerBlockEntity(
                FLUID_HANDLER,
                BlockEntityRegistry.DIGESTION_VAT.get(),
                (blockEntity, side) -> blockEntity.storageBehaviour.fluidTankCloseAwareWrapper
        );
    }

    public static void registerDistiller(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                ITEM_HANDLER,
                (level, pos, state, be, context) ->
                        doubleBlockCapability(level, pos, be, context, DistillerBlockEntity.class,
                                (blockEntity, side) -> {
                                    if (side == Direction.UP) {
                                        return blockEntity.storageBehaviour.inputInventory;
                                    } else if (side == Direction.DOWN) {
                                        return blockEntity.storageBehaviour.outputInventoryExtractOnlyWrapper;
                                    } else {
                                        return blockEntity.storageBehaviour.inventory;
                                    }
                                }
                        ),
                BlockRegistry.DISTILLER.get()
        );

        event.registerBlockEntity(
                HEAT_RECEIVER,
                BlockEntityRegistry.DISTILLER.get(),
                (blockEntity, side) -> blockEntity.heatReceiver);
    }

    public static void registerFermentationVat(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.FERMENTATION_VAT.get(),
                (blockEntity, side) -> {
                    if (side == Direction.UP) {
                        return blockEntity.storageBehaviour.inputInventoryOpenCloseAwareWrapper;
                    } else if (side == Direction.DOWN) {
                        return blockEntity.storageBehaviour.outputInventoryOpenCloseAwareWrapper;
                    } else {
                        return blockEntity.storageBehaviour.inventoryOpenCloseAwareWrapper;
                    }
                }
        );

        event.registerBlockEntity(
                FLUID_HANDLER,
                BlockEntityRegistry.FERMENTATION_VAT.get(),
                (blockEntity, side) -> blockEntity.storageBehaviour.fluidTankCloseAwareWrapper
        );
    }

    public static void registerIncubator(RegisterCapabilitiesEvent event) {
        event.registerBlock(
                ITEM_HANDLER,
                (level, pos, state, be, context) ->
                        doubleBlockCapability(level, pos, be, context, IncubatorBlockEntity.class,
                                (blockEntity, side) -> blockEntity.outputInventoryTakeOnlyWrapper
                        ),
                BlockRegistry.INCUBATOR.get()
        );

        event.registerBlockEntity(
                HEAT_RECEIVER,
                BlockEntityRegistry.INCUBATOR.get(),
                (blockEntity, side) -> blockEntity.heatReceiver);

        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.INCUBATOR_SULFUR_VESSEL.get(),
                (blockEntity, side) -> blockEntity.inputInventory);

        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.INCUBATOR_SALT_VESSEL.get(),
                (blockEntity, side) -> blockEntity.inputInventory);

        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.INCUBATOR_MERCURY_VESSEL.get(),
                (blockEntity, side) -> blockEntity.inputInventory);
    }

    public static void registerLiquefactionCauldron(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                HEAT_RECEIVER,
                BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(),
                (blockEntity, side) -> blockEntity.heatReceiver);

        event.registerBlock(
                ITEM_HANDLER,
                (level, pos, state, be, context) ->
                        doubleBlockCapability(level, pos, be, context, LiquefactionCauldronBlockEntity.class, (blockEntity, side) -> {
                                    if (side == Direction.UP) {
                                        return blockEntity.storageBehaviour.inputInventory;
                                    } else if (side == Direction.DOWN) {
                                        return blockEntity.storageBehaviour.outputInventory;
                                    } else {
                                        return blockEntity.storageBehaviour.inventory;
                                    }
                                }
                        ),
                BlockRegistry.LIQUEFACTION_CAULDRON.get()
        );

        event.registerBlock(
                FLUID_HANDLER,
                (level, pos, state, be, context) ->
                        doubleBlockCapability(level, pos, be, context, LiquefactionCauldronBlockEntity.class, (blockEntity, c) -> blockEntity.storageBehaviour.solventTank),
                BlockRegistry.LIQUEFACTION_CAULDRON.get()
        );
    }

    public static void registerLogisticsCapabilityProxy(RegisterCapabilitiesEvent event) {
        BlockCapability.getAllProxyable().forEach(capability -> registerLogisticsCapabilityProxy(event, capability));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void registerLogisticsCapabilityProxy(RegisterCapabilitiesEvent event, BlockCapability capability) {
        event.registerBlockEntity(
                capability,
                BlockEntityRegistry.LOGISTICS_CAPABILITY_PROXY.get(),
                (blockEntity, side) -> blockEntity.resolveProbeCapability(capability)
        );
    }

    public static void registerLogisticsMercuryFluxConnector(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.LOGISTICS_MERCURY_FLUX_CONNECTOR.get(),
                // Expose the connector's own buffer so source blocks can push into it.
                // The connector then forwards buffer contents through the logistics network.
                (blockEntity, side) -> blockEntity.leafNode().buffer());
    }


    public static void registerMercuryCatalyst(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.MERCURY_CATALYST.get(),
                (blockEntity, side) -> blockEntity.mercuryFluxStorage);

        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.MERCURY_CATALYST.get(),
                (blockEntity, side) -> blockEntity.inventory);
    }

    public static void registerMercuryCapacitor(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.MERCURY_CAPACITOR.get(),
                (blockEntity, side) -> blockEntity.getMercuryFluxStorage(side));
    }

    public static void registerPyromanticBrazier(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.PYROMANTIC_BRAZIER.get(),
                (blockEntity, side) -> blockEntity.inventory);
        event.registerBlockEntity(
                HEAT_PROVIDER,
                BlockEntityRegistry.PYROMANTIC_BRAZIER.get(),
                (blockEntity, side) -> blockEntity.heatProvider);
    }

    public static void registerReformationArray(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.SULFURIC_FLUX_EMITTER.get(),
                (blockEntity, side) -> blockEntity.mercuryFluxStorage);

        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.MERCURY_FLUX_EMITTER.get(),
                (blockEntity, side) -> {
                    // Only expose capability on the side the emitter is attached to (opposite to FACING)
                    var blockState = blockEntity.getBlockState();
                    var facing = blockState.getValue(BlockStateProperties.FACING);
                    var attachedSide = facing.getOpposite();
                    if (side == null || side == attachedSide) {
                        return blockEntity.mercuryFluxStorage;
                    }
                    return null;
                });

        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.REFORMATION_RESULT_PEDESTAL.get(),
                (blockEntity, side) -> blockEntity.outputInventoryTakeOnlyWrapper);

        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.REFORMATION_TARGET_PEDESTAL.get(),
                (blockEntity, side) -> blockEntity.inputInventory);

        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.REFORMATION_SOURCE_PEDESTAL.get(),
                (blockEntity, side) -> blockEntity.inputInventory);
    }

    public static void registerSalAmmoniacAccumulator(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                ITEM_HANDLER,
                BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get(),
                (blockEntity, side) -> blockEntity.inventory);

        event.registerBlockEntity(
                FLUID_HANDLER,
                BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get(),
                (blockEntity, side) -> blockEntity.waterTank);
    }

    public static void registerSalAmmoniacTank(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                FLUID_HANDLER,
                BlockEntityRegistry.SAL_AMMONIAC_TANK.get(),
                (blockEntity, side) -> blockEntity.tank);
    }

}
