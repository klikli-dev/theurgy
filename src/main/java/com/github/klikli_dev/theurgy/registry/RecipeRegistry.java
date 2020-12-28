package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.EssentiaRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistry {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, Theurgy.MODID);

    public static final NonNullLazy<IRecipeType<EssentiaRecipe>> ESSENTIA_TYPE =
            NonNullLazy.of(() -> IRecipeType.register("theurgy:essentia"));

    public static final NonNullLazy<IRecipeType<CrucibleRecipe>> CRUCIBLE_TYPE =
            NonNullLazy.of(() -> IRecipeType.register("theurgy:crucible"));

    public static final RegistryObject<IRecipeSerializer<EssentiaRecipe>> ESSENTIA = RECIPES.register("essentia",
            () -> EssentiaRecipe.SERIALIZER);

    public static final RegistryObject<IRecipeSerializer<CrucibleRecipe>> CRUCIBLE = RECIPES.register("crucible",
            () -> CrucibleRecipe.SERIALIZER);
}
