// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class GameTestRegistry {

    public static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS =
            DeferredRegister.create(BuiltInRegistries.TEST_FUNCTION, Theurgy.MODID);

    // --- Mercury Catalyst ---

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> PLACEMENT_AND_DEFAULT_STATE =
            TEST_FUNCTIONS.register("mercury_catalyst_placement", () -> MercuryCatalystGameTests::placementAndDefaultState);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MERCURY_SHARD_GENERATES_FLUX =
            TEST_FUNCTIONS.register("mercury_catalyst_shard_generates_flux", () -> MercuryCatalystGameTests::mercuryShardGeneratesFlux);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MERCURY_SHARD_IS_CONSUMED =
            TEST_FUNCTIONS.register("mercury_catalyst_shard_is_consumed", () -> MercuryCatalystGameTests::mercuryShardIsConsumed);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISABLED_CATALYST_STILL_GENERATES_FLUX =
            TEST_FUNCTIONS.register("mercury_catalyst_disabled_still_generates_flux", () -> MercuryCatalystGameTests::disabledCatalystStillGeneratesFlux);

    // --- Liquefaction Cauldron ---

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_PLACEMENT_CREATES_TWO_BLOCK_STRUCTURE =
            TEST_FUNCTIONS.register("lc_placement_creates_two_block_structure", () -> LiquefactionCauldronGameTests::placementCreatesTwoBlockStructure);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_BLOCK_ENTITY_ONLY_ON_LOWER_HALF =
            TEST_FUNCTIONS.register("lc_block_entity_only_on_lower_half", () -> LiquefactionCauldronGameTests::blockEntityOnlyOnLowerHalf);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_BREAKING_LOWER_DESTROYS_UPPER =
            TEST_FUNCTIONS.register("lc_breaking_lower_destroys_upper", () -> LiquefactionCauldronGameTests::breakingLowerDestroysUpper);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_BREAKING_UPPER_DESTROYS_LOWER =
            TEST_FUNCTIONS.register("lc_breaking_upper_destroys_lower", () -> LiquefactionCauldronGameTests::breakingUpperDestroysLower);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_INSERT_INPUT_ITEM =
            TEST_FUNCTIONS.register("lc_insert_input_item", () -> LiquefactionCauldronGameTests::insertInputItem);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_INSERT_SOLVENT_FLUID =
            TEST_FUNCTIONS.register("lc_insert_solvent_fluid", () -> LiquefactionCauldronGameTests::insertSolventFluid);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_EXTRACT_OUTPUT_ITEM =
            TEST_FUNCTIONS.register("lc_extract_output_item", () -> LiquefactionCauldronGameTests::extractOutputItem);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_PROCESSING_STARTS =
            TEST_FUNCTIONS.register("lc_processing_starts", () -> LiquefactionCauldronGameTests::processingStartsWithHeatInputAndSolvent);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_LIT_STATE_TRUE_DURING_PROCESSING =
            TEST_FUNCTIONS.register("lc_lit_state_true_during_processing", () -> LiquefactionCauldronGameTests::litStateTrueDuringProcessing);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_INPUT_CONSUMED_OUTPUT_PRODUCED =
            TEST_FUNCTIONS.register("lc_input_consumed_output_produced", () -> LiquefactionCauldronGameTests::inputConsumedSolventDrainedOutputProduced);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_PROCESSING_STOPS_WHEN_HEAT_REMOVED =
            TEST_FUNCTIONS.register("lc_processing_stops_when_heat_removed", () -> LiquefactionCauldronGameTests::processingStopsWhenHeatRemoved);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_PROCESSING_REQUIRES_INPUT =
            TEST_FUNCTIONS.register("lc_processing_requires_input", () -> LiquefactionCauldronGameTests::processingRequiresInput);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_PROCESSING_REQUIRES_SOLVENT =
            TEST_FUNCTIONS.register("lc_processing_requires_solvent", () -> LiquefactionCauldronGameTests::processingRequiresSolvent);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_DROPS_ITEMS_WHEN_BROKEN =
            TEST_FUNCTIONS.register("lc_drops_items_when_broken", () -> LiquefactionCauldronGameTests::dropsItemsWhenBroken);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LC_EXTRACT_SOLVENT_FLUID =
            TEST_FUNCTIONS.register("lc_extract_solvent_fluid", () -> LiquefactionCauldronGameTests::extractSolventFluid);

    // --- Calcination Oven ---

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_PLACEMENT =
            TEST_FUNCTIONS.register("calcination_oven_placement", () -> CalcinationOvenGameTests::placementCreatesTwoBlockStructure);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_BE_ONLY_LOWER =
            TEST_FUNCTIONS.register("calcination_oven_be_only_lower", () -> CalcinationOvenGameTests::blockEntityOnlyOnLowerHalf);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_BREAK_LOWER_DESTROYS_UPPER =
            TEST_FUNCTIONS.register("calcination_oven_break_lower_destroys_upper", () -> CalcinationOvenGameTests::breakingLowerDestroysUpper);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_BREAK_UPPER_DESTROYS_LOWER =
            TEST_FUNCTIONS.register("calcination_oven_break_upper_destroys_lower", () -> CalcinationOvenGameTests::breakingUpperDestroysLower);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_INSERT_INPUT =
            TEST_FUNCTIONS.register("calcination_oven_insert_input", () -> CalcinationOvenGameTests::insertInputItem);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_EXTRACT_OUTPUT =
            TEST_FUNCTIONS.register("calcination_oven_extract_output", () -> CalcinationOvenGameTests::extractOutputItem);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_PROCESSING_STARTS =
            TEST_FUNCTIONS.register("calcination_oven_processing_starts", () -> CalcinationOvenGameTests::processingStartsWithHeatAndInput);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_LIT_DURING_PROCESSING =
            TEST_FUNCTIONS.register("calcination_oven_lit_during_processing", () -> CalcinationOvenGameTests::litStateTrueDuringProcessing);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_INPUT_CONSUMED_OUTPUT_PRODUCED =
            TEST_FUNCTIONS.register("calcination_oven_input_consumed_output_produced", () -> CalcinationOvenGameTests::inputConsumedAndOutputProduced);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_STOPS_WITHOUT_HEAT =
            TEST_FUNCTIONS.register("calcination_oven_stops_without_heat", () -> CalcinationOvenGameTests::processingStopsWhenHeatRemoved);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_STOPS_WITHOUT_INPUT =
            TEST_FUNCTIONS.register("calcination_oven_stops_without_input", () -> CalcinationOvenGameTests::processingStopsWhenInputEmpty);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> OVEN_DROPS_ITEMS_WHEN_BROKEN =
            TEST_FUNCTIONS.register("calcination_oven_drops_items_when_broken", () -> CalcinationOvenGameTests::dropsItemsWhenBroken);

    // --- Pyromantic Brazier ---

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_PLACEMENT =
            TEST_FUNCTIONS.register("pyromantic_brazier_placement", () -> PyromanticBrazierGameTests::placementAndDefaultState);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_LIT_STATE_UPDATES =
            TEST_FUNCTIONS.register("pyromantic_brazier_lit_state_updates", () -> PyromanticBrazierGameTests::litStateUpdatesWhenFuelInserted);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_INSERT_FUEL =
            TEST_FUNCTIONS.register("pyromantic_brazier_insert_fuel", () -> PyromanticBrazierGameTests::insertFuelItem);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_REJECTS_NON_BURNABLE =
            TEST_FUNCTIONS.register("pyromantic_brazier_rejects_non_burnable", () -> PyromanticBrazierGameTests::rejectsNonBurnableItems);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_FUEL_CONSUMED =
            TEST_FUNCTIONS.register("pyromantic_brazier_fuel_consumed", () -> PyromanticBrazierGameTests::fuelIsConsumedOverTime);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_HEAT_CALCINATION_OVEN =
            TEST_FUNCTIONS.register("pyromantic_brazier_heat_calcination_oven", () -> PyromanticBrazierGameTests::providesHeatToCalcinationOven);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_HEAT_LIQUEFACTION_CAULDRON =
            TEST_FUNCTIONS.register("pyromantic_brazier_heat_liquefaction_cauldron", () -> PyromanticBrazierGameTests::providesHeatToLiquefactionCauldron);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_HEAT_DISTILLER =
            TEST_FUNCTIONS.register("pyromantic_brazier_heat_distiller", () -> PyromanticBrazierGameTests::providesHeatToDistiller);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_STOPS_HEAT_NO_FUEL =
            TEST_FUNCTIONS.register("pyromantic_brazier_stops_heat_no_fuel", () -> PyromanticBrazierGameTests::stopsProvidingHeatWhenFuelRunsOut);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_REMOVE_FUEL =
            TEST_FUNCTIONS.register("pyromantic_brazier_remove_fuel", () -> PyromanticBrazierGameTests::removeFuelViaEmptyHand);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BRAZIER_DROPS_ITEMS_WHEN_BROKEN =
            TEST_FUNCTIONS.register("pyromantic_brazier_drops_items_when_broken", () -> PyromanticBrazierGameTests::dropsItemsWhenBroken);

    // --- Distiller ---

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_PLACEMENT =
            TEST_FUNCTIONS.register("distiller_placement", () -> DistillerGameTests::placementCreatesTwoBlockStructure);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_BE_ONLY_LOWER =
            TEST_FUNCTIONS.register("distiller_be_only_lower", () -> DistillerGameTests::blockEntityOnlyOnLowerHalf);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_BREAK_LOWER_DESTROYS_UPPER =
            TEST_FUNCTIONS.register("distiller_break_lower_destroys_upper", () -> DistillerGameTests::breakingLowerDestroysUpper);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_BREAK_UPPER_DESTROYS_LOWER =
            TEST_FUNCTIONS.register("distiller_break_upper_destroys_lower", () -> DistillerGameTests::breakingUpperDestroysLower);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_INSERT_INPUT =
            TEST_FUNCTIONS.register("distiller_insert_input", () -> DistillerGameTests::insertInputItem);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_EXTRACT_OUTPUT =
            TEST_FUNCTIONS.register("distiller_extract_output", () -> DistillerGameTests::extractOutputItem);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_PROCESSING_STARTS =
            TEST_FUNCTIONS.register("distiller_processing_starts", () -> DistillerGameTests::processingStartsWithHeatAndInput);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_LIT_DURING_PROCESSING =
            TEST_FUNCTIONS.register("distiller_lit_during_processing", () -> DistillerGameTests::litStateTrueDuringProcessing);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_INPUT_CONSUMED_OUTPUT_PRODUCED =
            TEST_FUNCTIONS.register("distiller_input_consumed_output_produced", () -> DistillerGameTests::inputConsumedAndOutputProduced);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_STOPS_WITHOUT_HEAT =
            TEST_FUNCTIONS.register("distiller_stops_without_heat", () -> DistillerGameTests::processingStopsWhenHeatRemoved);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISTILLER_DROPS_ITEMS_WHEN_BROKEN =
            TEST_FUNCTIONS.register("distiller_drops_items_when_broken", () -> DistillerGameTests::dropsItemsWhenBroken);

    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        registerMercuryCatalystTests(event);
        registerCalcinationOvenTests(event);
        registerPyromanticBrazierTests(event);
        registerLiquefactionCauldronTests(event);
        registerDistillerTests(event);
    }

    private static void registerMercuryCatalystTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("mercury_catalyst"));
        var structure = Theurgy.loc("mercury_catalyst_test");

        registerTest(event, PLACEMENT_AND_DEFAULT_STATE, environment, structure, 40, 0);
        registerTest(event, MERCURY_SHARD_GENERATES_FLUX, environment, structure, 200, 0);
        registerTest(event, MERCURY_SHARD_IS_CONSUMED, environment, structure, 200, 0);
        registerTest(event, DISABLED_CATALYST_STILL_GENERATES_FLUX, environment, structure, 100, 0);
    }

    private static void registerLiquefactionCauldronTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("liquefaction_cauldron"));
        var structure = Theurgy.loc("liquefaction_cauldron_test");

        registerTest(event, LC_PLACEMENT_CREATES_TWO_BLOCK_STRUCTURE, environment, structure, 40, 0);
        registerTest(event, LC_BLOCK_ENTITY_ONLY_ON_LOWER_HALF, environment, structure, 40, 0);
        registerTest(event, LC_BREAKING_LOWER_DESTROYS_UPPER, environment, structure, 40, 0);
        registerTest(event, LC_BREAKING_UPPER_DESTROYS_LOWER, environment, structure, 40, 0);
        registerTest(event, LC_INSERT_INPUT_ITEM, environment, structure, 40, 0);
        registerTest(event, LC_INSERT_SOLVENT_FLUID, environment, structure, 40, 0);
        registerTest(event, LC_EXTRACT_OUTPUT_ITEM, environment, structure, 40, 0);
        registerTest(event, LC_PROCESSING_STARTS, environment, structure, 200, 0);
        registerTest(event, LC_LIT_STATE_TRUE_DURING_PROCESSING, environment, structure, 200, 0);
        registerTest(event, LC_INPUT_CONSUMED_OUTPUT_PRODUCED, environment, structure, 300, 0);
        registerTest(event, LC_PROCESSING_STOPS_WHEN_HEAT_REMOVED, environment, structure, 300, 0);
        registerTest(event, LC_PROCESSING_REQUIRES_INPUT, environment, structure, 100, 0);
        registerTest(event, LC_PROCESSING_REQUIRES_SOLVENT, environment, structure, 100, 0);
        registerTest(event, LC_DROPS_ITEMS_WHEN_BROKEN, environment, structure, 40, 0);
        registerTest(event, LC_EXTRACT_SOLVENT_FLUID, environment, structure, 40, 0);
    }

    private static void registerCalcinationOvenTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("calcination_oven"));
        var structure = Theurgy.loc("calcination_oven_test");

        registerTest(event, OVEN_PLACEMENT, environment, structure, 40, 0);
        registerTest(event, OVEN_BE_ONLY_LOWER, environment, structure, 40, 0);
        registerTest(event, OVEN_BREAK_LOWER_DESTROYS_UPPER, environment, structure, 40, 0);
        registerTest(event, OVEN_BREAK_UPPER_DESTROYS_LOWER, environment, structure, 40, 0);
        registerTest(event, OVEN_INSERT_INPUT, environment, structure, 100, 0);
        registerTest(event, OVEN_EXTRACT_OUTPUT, environment, structure, 100, 0);
        registerTest(event, OVEN_PROCESSING_STARTS, environment, structure, 200, 0);
        registerTest(event, OVEN_LIT_DURING_PROCESSING, environment, structure, 200, 0);
        registerTest(event, OVEN_INPUT_CONSUMED_OUTPUT_PRODUCED, environment, structure, 300, 0);
        registerTest(event, OVEN_STOPS_WITHOUT_HEAT, environment, structure, 400, 0);
        registerTest(event, OVEN_STOPS_WITHOUT_INPUT, environment, structure, 300, 0);
        registerTest(event, OVEN_DROPS_ITEMS_WHEN_BROKEN, environment, structure, 40, 0);
    }

    private static void registerPyromanticBrazierTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("pyromantic_brazier"));
        var structure = Theurgy.loc("pyromantic_brazier_test");

        registerTest(event, BRAZIER_PLACEMENT, environment, structure, 40, 0);
        registerTest(event, BRAZIER_LIT_STATE_UPDATES, environment, structure, 100, 0);
        registerTest(event, BRAZIER_INSERT_FUEL, environment, structure, 40, 0);
        registerTest(event, BRAZIER_REJECTS_NON_BURNABLE, environment, structure, 40, 0);
        registerTest(event, BRAZIER_FUEL_CONSUMED, environment, structure, 200, 0);
        registerTest(event, BRAZIER_HEAT_CALCINATION_OVEN, environment, structure, 100, 0);
        registerTest(event, BRAZIER_HEAT_LIQUEFACTION_CAULDRON, environment, structure, 100, 0);
        registerTest(event, BRAZIER_HEAT_DISTILLER, environment, structure, 100, 0);
        registerTest(event, BRAZIER_STOPS_HEAT_NO_FUEL, environment, structure, 300, 0);
        registerTest(event, BRAZIER_REMOVE_FUEL, environment, structure, 40, 0);
        registerTest(event, BRAZIER_DROPS_ITEMS_WHEN_BROKEN, environment, structure, 40, 0);
    }

    private static void registerDistillerTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("distiller"));
        var structure = Theurgy.loc("distiller_test");

        registerTest(event, DISTILLER_PLACEMENT, environment, structure, 40, 0);
        registerTest(event, DISTILLER_BE_ONLY_LOWER, environment, structure, 40, 0);
        registerTest(event, DISTILLER_BREAK_LOWER_DESTROYS_UPPER, environment, structure, 40, 0);
        registerTest(event, DISTILLER_BREAK_UPPER_DESTROYS_LOWER, environment, structure, 40, 0);
        registerTest(event, DISTILLER_INSERT_INPUT, environment, structure, 100, 0);
        registerTest(event, DISTILLER_EXTRACT_OUTPUT, environment, structure, 100, 0);
        registerTest(event, DISTILLER_PROCESSING_STARTS, environment, structure, 200, 0);
        registerTest(event, DISTILLER_LIT_DURING_PROCESSING, environment, structure, 200, 0);
        registerTest(event, DISTILLER_INPUT_CONSUMED_OUTPUT_PRODUCED, environment, structure, 300, 0);
        registerTest(event, DISTILLER_STOPS_WITHOUT_HEAT, environment, structure, 400, 0);
        registerTest(event, DISTILLER_DROPS_ITEMS_WHEN_BROKEN, environment, structure, 40, 0);
    }

    private static void registerTest(
            RegisterGameTestsEvent event,
            DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> test,
            Holder<TestEnvironmentDefinition<?>> environment,
            net.minecraft.resources.Identifier structure,
            int maxTicks,
            int setupTicks
    ) {
        var testData = new TestData<>(environment, structure, maxTicks, setupTicks, true);
        event.registerTest(test.getId(), new FunctionGameTestInstance(test.getKey(), testData));
    }
}
