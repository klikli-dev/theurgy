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

package com.klikli_dev.theurgy.crafting

import com.google.gson.JsonObject
import com.klikli_dev.theurgy.registry.RecipeRegistry
import net.minecraft.inventory.CraftingInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapelessRecipe
import net.minecraft.item.crafting.ShapelessRecipe.Serializer
import net.minecraft.network.PacketBuffer
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistryEntry

class KeepDamageShapelessRecipe(
    idIn: ResourceLocation, groupIn: String, recipeOutputIn: ItemStack,
    recipeItemsIn: NonNullList<Ingredient>
) : ShapelessRecipe(idIn, groupIn, recipeOutputIn, recipeItemsIn) {

    override fun getCraftingResult(inv: CraftingInventory): ItemStack {
        //get original crafting result
        val result = super.getCraftingResult(inv);

        //now find the highest damage in the input
        var maxDamage = 0;
        for (i in 0 .. inv.sizeInventory){
            val stack = inv.getStackInSlot(i);
            if(!stack.isEmpty && stack.isDamageable && stack.damage > maxDamage){
                maxDamage = stack.damage
            }
        }

        result.damage = maxDamage;

        return result;
    }

    override fun getSerializer(): IRecipeSerializer<*> {
        return Serializer
    }

    override fun getType(): IRecipeType<*> {
        return RecipeRegistry.keepDamageShapelessRecipeType.get()
    }

    object Serializer : ForgeRegistryEntry<IRecipeSerializer<*>?>(), IRecipeSerializer<KeepDamageShapelessRecipe> {
        private val serializer = Serializer()

        override fun read(
            recipeId: ResourceLocation,
            json: JsonObject
        ): KeepDamageShapelessRecipe {
            val recipe = serializer.read(recipeId, json)
            return KeepDamageShapelessRecipe(recipe.id, recipe.group, recipe.recipeOutput, recipe.ingredients)
        }

        override fun read(
            recipeId: ResourceLocation,
            buffer: PacketBuffer
        ): KeepDamageShapelessRecipe {
            val recipe = serializer.read(recipeId, buffer)
            return KeepDamageShapelessRecipe(recipe!!.id, recipe.group, recipe.recipeOutput, recipe.ingredients)
        }

        override fun write(
            buffer: PacketBuffer,
            recipe: KeepDamageShapelessRecipe
        ) {
            serializer.write(buffer, recipe)
        }
    }
}