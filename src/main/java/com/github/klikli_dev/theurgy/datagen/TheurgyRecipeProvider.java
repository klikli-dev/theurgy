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

package com.github.klikli_dev.theurgy.datagen;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.datagen.recipe.CrucibleRecipeBuilder;
import com.github.klikli_dev.theurgy.datagen.recipe.EssentiaRecipeBuilder;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import com.github.klikli_dev.theurgy.registry.TagRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TheurgyRecipeProvider extends RecipeProvider {

    //region Fields
    public static final ITag.INamedTag<Item> COPPER_INGOT = TagRegistry.makeForgeItemTag("ingots/copper");
    public static final ITag.INamedTag<Item> SILVER_INGOT = TagRegistry.makeForgeItemTag("ingots/silver");
    //endregion Fields

    //region Initialization
    public TheurgyRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }
    //endregion Initialization

    //region Overrides
    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        Item aer = ItemRegistry.AER_ESSENTIA.get();
        Item aqua = ItemRegistry.AQUA_ESSENTIA.get();
        Item ignis = ItemRegistry.IGNIS_ESSENTIA.get();
        Item terra = ItemRegistry.TERRA_ESSENTIA.get();

        EssentiaRecipeBuilder copperEssentia = EssentiaRecipeBuilder.create()
                                                       .setRecipeName("copper_ingot")
                                                       .ingredient(COPPER_INGOT)
                                                       .essentia(ignis, 100)
                                                       .essentia(terra, 100);

        this.registerTagExistsCondition(copperEssentia, COPPER_INGOT, consumer);

        EssentiaRecipeBuilder ironEssentia = EssentiaRecipeBuilder.create()
                                                     .setRecipeName("iron_ingot")
                                                     .ingredient(Tags.Items.INGOTS_IRON)
                                                     .essentia(ignis, 100)
                                                     .essentia(terra, 100)
                                                     .build(consumer);
        EssentiaRecipeBuilder goldEssentia = EssentiaRecipeBuilder.create()
                                                     .setRecipeName("gold_ingot")
                                                     .ingredient(Tags.Items.INGOTS_GOLD)
                                                     .essentia(aer, 50)
                                                     .essentia(ignis, 200)
                                                     .essentia(terra, 200)
                                                     .build(consumer);


        CrucibleRecipeBuilder.transmutation(Tags.Items.INGOTS_GOLD, 1)
                .setRecipeName("gold_ingot")
                .ingredient(Tags.Items.INGOTS_IRON)
                .essentia(this.essentiaDifference(ironEssentia.essentia, goldEssentia.essentia))
                .build(consumer);
    }
    //endregion Overrides

    //region Methods
    protected void registerTagExistsCondition(EssentiaRecipeBuilder builder, ITag.INamedTag<Item> tag,
                                              Consumer<IFinishedRecipe> consumer) {
        ConditionalRecipe.builder()
                .addCondition(new NotCondition(new TagEmptyCondition(tag.getName())))
                .addRecipe(builder::build)
                .build(consumer, Theurgy.MODID, "essentia/" + builder.recipeName);
    }

    /**
     * Gets the remaining required essentia for an item
     *
     * @param inputEssentia the essentia contained in the input item.
     * @param inputEssentia the essentia contained in the desired output item.
     */
    protected List<ItemStack> essentiaDifference(List<ItemStack> inputEssentia, List<ItemStack> outputEssentia) {
        return essentiaDifference(inputEssentia, outputEssentia, 1.0f);
    }

    /**
     * Gets the remaining required essentia for an item
     *
     * @param inputEssentia            the essentia contained in the input item.
     * @param inputEssentia            the essentia contained in the desired output item.
     * @param outputEssentiaMultiplier a multiplier for the output essentia, usually to simulate loss / additional cost
     */
    protected List<ItemStack> essentiaDifference(List<ItemStack> inputEssentia, List<ItemStack> outputEssentia,
                                                 float outputEssentiaMultiplier) {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack essentia : outputEssentia) {
            Optional<ItemStack> other = inputEssentia.stream()
                                                .filter(stack -> stack.getItem() == essentia.getItem())
                                                .findFirst();
            if (other.isPresent()) {
                //if input has matching essentia, subtract it from requirement
                ItemStack output = essentia.copy();
                int difference = Math.max(0, essentia.getCount() - other.get().getCount());
                output.setCount((int) (difference * outputEssentiaMultiplier));
                result.add(output);
            }
            else {
                //if no matching input essentia, just return the output essentia * outputEssentiaMultiplier
                ItemStack output = essentia.copy();
                output.setCount((int) (essentia.getCount() * outputEssentiaMultiplier));
                result.add(output);
            }
        }
        return result;
    }
    //endregion Methods
}
