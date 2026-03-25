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

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> PLACEMENT_AND_DEFAULT_STATE =
            TEST_FUNCTIONS.register("mercury_catalyst_placement", () -> MercuryCatalystGameTests::placementAndDefaultState);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MERCURY_SHARD_GENERATES_FLUX =
            TEST_FUNCTIONS.register("mercury_catalyst_shard_generates_flux", () -> MercuryCatalystGameTests::mercuryShardGeneratesFlux);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> MERCURY_SHARD_IS_CONSUMED =
            TEST_FUNCTIONS.register("mercury_catalyst_shard_is_consumed", () -> MercuryCatalystGameTests::mercuryShardIsConsumed);

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISABLED_CATALYST_STILL_GENERATES_FLUX =
            TEST_FUNCTIONS.register("mercury_catalyst_disabled_still_generates_flux", () -> MercuryCatalystGameTests::disabledCatalystStillGeneratesFlux);

    // Liquefaction Cauldron tests
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

    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("mercury_catalyst"));
        var structure = Theurgy.loc("mercury_catalyst_test");

        registerTest(event, PLACEMENT_AND_DEFAULT_STATE, environment, structure, 40, 0);
        registerTest(event, MERCURY_SHARD_GENERATES_FLUX, environment, structure, 200, 0);
        registerTest(event, MERCURY_SHARD_IS_CONSUMED, environment, structure, 200, 0);
        registerTest(event, DISABLED_CATALYST_STILL_GENERATES_FLUX, environment, structure, 100, 0);

        // Liquefaction Cauldron tests
        var lcEnvironment = event.registerEnvironment(Theurgy.loc("liquefaction_cauldron"));
        var lcStructure = Theurgy.loc("liquefaction_cauldron_test");

        registerTest(event, LC_PLACEMENT_CREATES_TWO_BLOCK_STRUCTURE, lcEnvironment, lcStructure, 40, 0);
        registerTest(event, LC_BLOCK_ENTITY_ONLY_ON_LOWER_HALF, lcEnvironment, lcStructure, 40, 0);
        registerTest(event, LC_BREAKING_LOWER_DESTROYS_UPPER, lcEnvironment, lcStructure, 40, 0);
        registerTest(event, LC_BREAKING_UPPER_DESTROYS_LOWER, lcEnvironment, lcStructure, 40, 0);
        registerTest(event, LC_INSERT_INPUT_ITEM, lcEnvironment, lcStructure, 40, 0);
        registerTest(event, LC_INSERT_SOLVENT_FLUID, lcEnvironment, lcStructure, 40, 0);
        registerTest(event, LC_EXTRACT_OUTPUT_ITEM, lcEnvironment, lcStructure, 40, 0);
        registerTest(event, LC_PROCESSING_STARTS, lcEnvironment, lcStructure, 200, 0);
        registerTest(event, LC_LIT_STATE_TRUE_DURING_PROCESSING, lcEnvironment, lcStructure, 200, 0);
        registerTest(event, LC_INPUT_CONSUMED_OUTPUT_PRODUCED, lcEnvironment, lcStructure, 300, 0);
        registerTest(event, LC_PROCESSING_STOPS_WHEN_HEAT_REMOVED, lcEnvironment, lcStructure, 300, 0);
        registerTest(event, LC_PROCESSING_REQUIRES_INPUT, lcEnvironment, lcStructure, 100, 0);
        registerTest(event, LC_PROCESSING_REQUIRES_SOLVENT, lcEnvironment, lcStructure, 100, 0);
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
