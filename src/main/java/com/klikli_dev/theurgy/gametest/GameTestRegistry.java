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

    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        registerMercuryCatalystTests(event);
        registerPyromanticBrazierTests(event);
    }

    private static void registerMercuryCatalystTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("mercury_catalyst"));
        var structure = Theurgy.loc("mercury_catalyst_test");

        registerTest(event, PLACEMENT_AND_DEFAULT_STATE, environment, structure, 40, 0);
        registerTest(event, MERCURY_SHARD_GENERATES_FLUX, environment, structure, 200, 0);
        registerTest(event, MERCURY_SHARD_IS_CONSUMED, environment, structure, 200, 0);
        registerTest(event, DISABLED_CATALYST_STILL_GENERATES_FLUX, environment, structure, 100, 0);
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
