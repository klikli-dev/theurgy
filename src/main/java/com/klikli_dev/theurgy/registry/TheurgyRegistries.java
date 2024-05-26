package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.recipe.result.RecipeResultType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class TheurgyRegistries {
    public static final Registry<RecipeResultType<?>> RECIPE_RESULT_TYPES = new RegistryBuilder<>(Keys.RECIPE_RESULT_TYPES).sync(true).create();


    public static void onRegisterRegistries(NewRegistryEvent event) {
        event.register(RECIPE_RESULT_TYPES);
    }

    public static final class Keys {
        public static final ResourceKey<Registry<RecipeResultType<?>>> RECIPE_RESULT_TYPES = ResourceKey.createRegistryKey(Theurgy.loc("recipe_result_type"));
    }
}
