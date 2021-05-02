/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.registry

import com.klikli_dev.theurgy.Theurgy
import com.klikli_dev.theurgy.crafting.KeepDamageShapelessRecipe
import net.minecraft.item.crafting.ICraftingRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraftforge.common.util.NonNullLazy
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate

object RecipeRegistry {
    val recipes = KDeferredRegister(ForgeRegistries.RECIPE_SERIALIZERS, Theurgy.MOD_ID)

    val keepDamageShapelessRecipeType: NonNullLazy<IRecipeType<ICraftingRecipe>> =
        NonNullLazy.of {
            IRecipeType.CRAFTING //this should work in any place vanilla crafting works
        }

    val keepDamageShapelessRecipe: ObjectHolderDelegate<KeepDamageShapelessRecipe.Serializer> =
        recipes.registerObject(
            "keep_damage_shapeless"
        ) { KeepDamageShapelessRecipe.Serializer }
}