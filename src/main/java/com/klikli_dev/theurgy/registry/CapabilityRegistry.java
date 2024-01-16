// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.capability.HeatProvider;
import com.klikli_dev.theurgy.content.capability.HeatReceiver;
import com.klikli_dev.theurgy.content.capability.MercuryFluxStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapabilityRegistry {

    public static final BlockCapability<MercuryFluxStorage, @Nullable Direction> MERCURY_FLUX_HANDLER = BlockCapability.createSided(
            Theurgy.loc("mercury_flux_handler"),
            MercuryFluxStorage.class);
    public static BlockCapability<HeatProvider, @Nullable Direction> HEAT_PROVIDER = BlockCapability.createSided(
            Theurgy.loc("heat_provider"),
            HeatProvider.class);
    public static BlockCapability<HeatReceiver, @Nullable Direction> HEAT_RECEIVER = BlockCapability.createSided(
            Theurgy.loc("heat_receiver"),
            HeatReceiver.class);

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        registerCalcinationOven(event);
        registerCaloricFluxEmitter(event);
        registerDigestionVat(event);
        registerDistiller(event);
        registerFermentationVat(event);
        registerIncubator(event);
        registerLiquefactionCauldron(event);
        registerMercuryCatalyst(event);
        registerPyromanticBrazier(event);
        registerReformationArray(event);
        registerSalAmmoniacAccumulator(event);
        registerSalAmmoniacTank(event);
    }

    public static void registerCalcinationOven(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.CALCINATION_OVEN.get(),
                (blockEntity, side) ->
                        switch (side) {
                            case UP -> blockEntity.storageBehaviour.inputInventory;
                            case DOWN -> blockEntity.storageBehaviour.outputInventoryExtractOnlyWrapper;
                            default -> blockEntity.storageBehaviour.inventory;
                        }
        );
        event.registerBlockEntity(
                HEAT_RECEIVER,
                BlockEntityRegistry.CALCINATION_OVEN.get(),
                (blockEntity, side) -> {
                    return blockEntity.heatReceiver;
                });
    }

    public static void registerCaloricFluxEmitter(@NotNull RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.CALORIC_FLUX_EMITTER.get(),
                (blockEntity, side) -> {
                    return blockEntity.mercuryFluxStorage;
                });
    }

    public static void registerDigestionVat(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.DIGESTION_VAT.get(),
                (blockEntity, side) -> {
                    var isOpen = blockEntity.getBlockState().getValue(BlockStateProperties.OPEN);
                    return switch (side) {
                        case UP ->
                                isOpen ? blockEntity.storageBehaviour.inputInventory : blockEntity.storageBehaviour.inputInventoryReadOnlyWrapper;
                        case DOWN ->
                                isOpen ? blockEntity.storageBehaviour.outputInventoryExtractOnlyWrapper : blockEntity.storageBehaviour.outputInventoryReadOnlyWrapper;
                        default ->
                                isOpen ? blockEntity.storageBehaviour.inventory : blockEntity.storageBehaviour.inventoryReadOnlyWrapper;
                    };
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntityRegistry.DIGESTION_VAT.get(),
                (blockEntity, side) -> {
                    var isOpen = blockEntity.getBlockState().getValue(BlockStateProperties.OPEN);
                    return isOpen ? blockEntity.storageBehaviour.fluidTank : blockEntity.storageBehaviour.fluidTankReadOnlyWrapper;
                }
        );
    }

    public static void registerDistiller(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.DISTILLER.get(),
                (blockEntity, side) ->
                        switch (side) {
                            case UP -> blockEntity.storageBehaviour.inputInventory;
                            case DOWN -> blockEntity.storageBehaviour.outputInventoryExtractOnlyWrapper;
                            default -> blockEntity.storageBehaviour.inventory;
                        }
        );
        event.registerBlockEntity(
                HEAT_RECEIVER,
                BlockEntityRegistry.DISTILLER.get(),
                (blockEntity, side) -> {
                    return blockEntity.heatReceiver;
                });
    }

    public static void registerFermentationVat(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.FERMENTATION_VAT.get(),
                (blockEntity, side) -> {
                    var isOpen = blockEntity.getBlockState().getValue(BlockStateProperties.OPEN);
                    return switch (side) {
                        case UP ->
                                isOpen ? blockEntity.storageBehaviour.inputInventory : blockEntity.storageBehaviour.inputInventoryReadOnlyWrapper;
                        case DOWN ->
                                isOpen ? blockEntity.storageBehaviour.outputInventoryExtractOnlyWrapper : blockEntity.storageBehaviour.outputInventoryReadOnlyWrapper;
                        default ->
                                isOpen ? blockEntity.storageBehaviour.inventory : blockEntity.storageBehaviour.inventoryReadOnlyWrapper;
                    };
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntityRegistry.FERMENTATION_VAT.get(),
                (blockEntity, side) -> {
                    var isOpen = blockEntity.getBlockState().getValue(BlockStateProperties.OPEN);
                    return isOpen ? blockEntity.storageBehaviour.fluidTank : blockEntity.storageBehaviour.fluidTankReadOnlyWrapper;
                }
        );
    }

    public static void registerIncubator(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.INCUBATOR.get(),
                (blockEntity, side) -> {
                    return blockEntity.outputInventoryTakeOnlyWrapper;
                });

        event.registerBlockEntity(
                HEAT_RECEIVER,
                BlockEntityRegistry.INCUBATOR.get(),
                (blockEntity, side) -> {
                    return blockEntity.heatReceiver;
                });

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.INCUBATOR_SULFUR_VESSEL.get(),
                (blockEntity, side) -> {
                    return blockEntity.inputInventory;
                });

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.INCUBATOR_SALT_VESSEL.get(),
                (blockEntity, side) -> {
                    return blockEntity.inputInventory;
                });

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.INCUBATOR_MERCURY_VESSEL.get(),
                (blockEntity, side) -> {
                    return blockEntity.inputInventory;
                });
    }

    public static void registerLiquefactionCauldron(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                HEAT_RECEIVER,
                BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(),
                (blockEntity, side) -> {
                    return blockEntity.heatReceiver;
                });

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(),
                (blockEntity, side) ->
                        switch (side) {
                            case UP -> blockEntity.storageBehaviour.inputInventory;
                            case DOWN -> blockEntity.storageBehaviour.outputInventory;
                            default -> blockEntity.storageBehaviour.inventory;
                        }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(),
                (blockEntity, side) -> blockEntity.storageBehaviour.solventTank
        );
    }

    public static void registerMercuryCatalyst(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.MERCURY_CATALYST.get(),
                (blockEntity, side) -> {
                    return blockEntity.mercuryFluxStorage;
                });

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.MERCURY_CATALYST.get(),
                (blockEntity, side) -> {
                    return blockEntity.inventory;
                });
    }

    public static void registerPyromanticBrazier(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.PYROMANTIC_BRAZIER.get(),
                (blockEntity, side) -> {
                    return blockEntity.inventory;
                });
        event.registerBlockEntity(
                HEAT_PROVIDER,
                BlockEntityRegistry.PYROMANTIC_BRAZIER.get(),
                (blockEntity, side) -> {
                    return blockEntity.heatProvider;
                });
    }

    public static void registerReformationArray(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                MERCURY_FLUX_HANDLER,
                BlockEntityRegistry.SULFURIC_FLUX_EMITTER.get(),
                (blockEntity, side) -> {
                    return blockEntity.mercuryFluxStorage;
                });

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.REFORMATION_RESULT_PEDESTAL.get(),
                (blockEntity, side) -> {
                    return blockEntity.outputInventoryTakeOnlyWrapper;
                });

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.REFORMATION_TARGET_PEDESTAL.get(),
                (blockEntity, side) -> {
                    return blockEntity.inputInventory;
                });

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.REFORMATION_SOURCE_PEDESTAL.get(),
                (blockEntity, side) -> {
                    return blockEntity.inputInventory;
                });
    }

    public static void registerSalAmmoniacAccumulator(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get(),
                (blockEntity, side) -> {
                    return blockEntity.inventory;
                });

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntityRegistry.SAL_AMMONIAC_ACCUMULATOR.get(),
                (blockEntity, side) -> {
                    return blockEntity.waterTank;
                });
    }

    public static void registerSalAmmoniacTank(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                BlockEntityRegistry.SAL_AMMONIAC_TANK.get(),
                (blockEntity, side) -> {
                    return blockEntity.tank;
                });
    }

}
