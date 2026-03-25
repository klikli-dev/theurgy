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

    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("mercury_catalyst"));
        var structure = Theurgy.loc("mercury_catalyst_test");

        registerTest(event, PLACEMENT_AND_DEFAULT_STATE, environment, structure, 40, 0);
        registerTest(event, MERCURY_SHARD_GENERATES_FLUX, environment, structure, 200, 0);
        registerTest(event, MERCURY_SHARD_IS_CONSUMED, environment, structure, 200, 0);
        registerTest(event, DISABLED_CATALYST_STILL_GENERATES_FLUX, environment, structure, 100, 0);
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
