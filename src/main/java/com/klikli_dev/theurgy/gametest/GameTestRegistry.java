// SPDX-FileCopyrightText: 2022 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
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

    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> DISABLED_CATALYST_DOES_NOT_PUSH_FLUX =
            TEST_FUNCTIONS.register("mercury_catalyst_disabled_no_push", () -> MercuryCatalystGameTests::disabledCatalystDoesNotPushFlux);

    private static final ResourceKey<Consumer<GameTestHelper>> PLACEMENT_KEY =
            ResourceKey.create(Registries.TEST_FUNCTION, Theurgy.loc("mercury_catalyst_placement"));
    private static final ResourceKey<Consumer<GameTestHelper>> SHARD_GENERATES_FLUX_KEY =
            ResourceKey.create(Registries.TEST_FUNCTION, Theurgy.loc("mercury_catalyst_shard_generates_flux"));
    private static final ResourceKey<Consumer<GameTestHelper>> SHARD_IS_CONSUMED_KEY =
            ResourceKey.create(Registries.TEST_FUNCTION, Theurgy.loc("mercury_catalyst_shard_is_consumed"));
    private static final ResourceKey<Consumer<GameTestHelper>> DISABLED_NO_PUSH_KEY =
            ResourceKey.create(Registries.TEST_FUNCTION, Theurgy.loc("mercury_catalyst_disabled_no_push"));

    public static void onRegisterGameTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Theurgy.loc("mercury_catalyst"));
        var structure = Theurgy.loc("mercury_catalyst_test");

        registerTest(event, "mercury_catalyst_placement", PLACEMENT_KEY, environment, structure, 40, 0);
        registerTest(event, "mercury_catalyst_shard_generates_flux", SHARD_GENERATES_FLUX_KEY, environment, structure, 200, 0);
        registerTest(event, "mercury_catalyst_shard_is_consumed", SHARD_IS_CONSUMED_KEY, environment, structure, 200, 0);
        registerTest(event, "mercury_catalyst_disabled_no_push", DISABLED_NO_PUSH_KEY, environment, structure, 100, 0);
    }

    private static void registerTest(
            RegisterGameTestsEvent event,
            String name,
            ResourceKey<Consumer<GameTestHelper>> functionKey,
            Holder<TestEnvironmentDefinition<?>> environment,
            net.minecraft.resources.Identifier structure,
            int maxTicks,
            int setupTicks
    ) {
        var testData = new TestData<>(environment, structure, maxTicks, setupTicks, true);
        event.registerTest(Theurgy.loc(name), new FunctionGameTestInstance(functionKey, testData));
    }
}
