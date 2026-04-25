// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.gametest;

import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class DivinationRodGameTests {

    private static final int GRID_SIZE = 3;

    public static void craftNormalT1Rod(GameTestHelper helper) {
        var input = CraftingInput.of(GRID_SIZE, GRID_SIZE, t1Ingredients());
        var recipe = requireRecipe(helper, input, "Expected the normal t1 divination rod recipe to resolve");

        var output = recipe.value().assemble(input);

        helper.assertTrue(ItemStack.isSameItemSameComponents(output, new ItemStack(ItemRegistry.DIVINATION_ROD_T1.get())), "Crafted normal t1 divination rod should match the expected item");
        helper.assertTrue(output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_ALLOW_ATTUNING, false), "Normal t1 divination rod should keep its default attunement setting");
        helper.assertTrue(output.has(DataComponentRegistry.DIVINATION_SETTINGS_ALLOWED_BLOCKS_TAG), "Normal t1 divination rod should keep its allowed-block tag component");
        helper.assertTrue(output.has(DataComponentRegistry.DIVINATION_SETTINGS_DISALLOWED_BLOCKS_TAG), "Normal t1 divination rod should keep its disallowed-block tag component");
        helper.assertTrue(output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_RANGE, -1) == 96, "Normal t1 divination rod should keep its default range");
        helper.assertTrue(output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DURATION, -1) == 40, "Normal t1 divination rod should keep its default duration");
        helper.assertTrue(output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_MAX_DAMAGE, -1) == 8, "Normal t1 divination rod should keep its default durability");
        helper.succeed();
    }

    public static void craftSulfurAttunedAbundantRodWithDirt(GameTestHelper helper) {
        var ingredients = sulfurIngredients();
        var input = CraftingInput.of(GRID_SIZE, GRID_SIZE, ingredients);
        var recipe = requireRecipe(helper, input, "Expected the sulfur attuned abundant divination rod recipe to resolve with dirt sulfur");

        var output = recipe.value().assemble(input);

        helper.assertTrue(ItemStack.isSameItem(output, new ItemStack(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get())), "Crafted sulfur attuned abundant rod should match the expected item");
        helper.assertTrue(output.has(DataComponentRegistry.DIVINATION_LINKED_BLOCK), "Dirt sulfur should add a linked-block component");
        helper.assertTrue(output.get(DataComponentRegistry.DIVINATION_LINKED_BLOCK).value() == Blocks.DIRT, "Dirt sulfur should attune the rod to dirt");
        helper.assertTrue(!output.has(DataComponentRegistry.DIVINATION_LINKED_TAG), "Dirt sulfur should not need a linked-tag component");
        helper.assertTrue(!output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_ALLOW_ATTUNING, true), "Sulfur attuned abundant rod should keep attuning disabled");
        helper.succeed();
    }

    public static void craftSulfurAttunedAbundantRodWithCopper(GameTestHelper helper) {
        var input = CraftingInput.of(GRID_SIZE, GRID_SIZE, copperSulfurIngredients());
        var recipe = requireRecipe(helper, input, "Expected the sulfur attuned abundant divination rod recipe to resolve with copper sulfur");

        var output = recipe.value().assemble(input);

        helper.assertTrue(ItemStack.isSameItem(output, new ItemStack(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get())), "Crafted sulfur attuned abundant rod should match the expected item");
        helper.assertTrue(!output.has(DataComponentRegistry.DIVINATION_LINKED_BLOCK), "Copper sulfur should attune via linked tag, not linked block");
        helper.assertTrue(output.has(DataComponentRegistry.DIVINATION_LINKED_TAG), "Copper sulfur should add a linked-tag component");
        helper.assertTrue(output.get(DataComponentRegistry.DIVINATION_LINKED_TAG).location().toString().equals("c:ores/copper"), "Copper sulfur should attune to #c:ores/copper");
        helper.assertTrue(!output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_ALLOW_ATTUNING, true), "Sulfur attuned abundant rod should keep attuning disabled");
        helper.succeed();
    }

    public static void craftDivergentRecipeSurvivesComponents(GameTestHelper helper) {
        var ingredients = divergentIngredients();
        var input = CraftingInput.of(GRID_SIZE, GRID_SIZE, ingredients);
        var recipe = requireRecipe(helper, input, "Expected the divergent divination rod recipe to resolve");

        var output = recipe.value().assemble(input);

        helper.assertTrue(output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_ALLOW_ATTUNING, true) == false, "Divergent recipe should preserve allow-attuning=false");
        helper.assertTrue(output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DURATION, -1) == 13, "Divergent recipe should preserve custom duration");
        helper.assertTrue(output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_MAX_DAMAGE, -1) == 5, "Divergent recipe should preserve custom durability");
        helper.assertTrue(output.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_RANGE, -1) == 17, "Divergent recipe should preserve custom range");
        helper.assertTrue(output.has(DataComponentRegistry.DIVINATION_SETTINGS_ALLOWED_BLOCKS_TAG), "Divergent recipe should preserve allowed-block tag");
        helper.assertTrue(output.has(DataComponentRegistry.DIVINATION_SETTINGS_DISALLOWED_BLOCKS_TAG), "Divergent recipe should preserve disallowed-block tag");
        helper.succeed();
    }

    private static List<ItemStack> t1Ingredients() {
        return List.of(
                ItemStack.EMPTY, new ItemStack(Items.GLASS), new ItemStack(Items.STICK),
                ItemStack.EMPTY, new ItemStack(Items.STICK), new ItemStack(Items.GLASS),
                new ItemStack(Items.STICK), ItemStack.EMPTY, ItemStack.EMPTY
        );
    }

    private static List<ItemStack> sulfurIngredients() {
        return List.of(
                ItemStack.EMPTY, new ItemStack(Items.GLASS), new ItemStack(SulfurRegistry.DIRT.get()),
                ItemStack.EMPTY, new ItemStack(Items.STICK), new ItemStack(Items.GLASS),
                new ItemStack(Items.STICK), ItemStack.EMPTY, ItemStack.EMPTY
        );
    }

    private static List<ItemStack> divergentIngredients() {
        return List.of(
                new ItemStack(Items.DIRT), new ItemStack(Items.GLASS), new ItemStack(Items.DIRT),
                ItemStack.EMPTY, new ItemStack(Items.STICK), ItemStack.EMPTY,
                new ItemStack(Items.DIRT), new ItemStack(Items.GLASS), new ItemStack(Items.DIRT)
        );
    }

    private static List<ItemStack> copperSulfurIngredients() {
        return List.of(
                ItemStack.EMPTY, new ItemStack(Items.GLASS), new ItemStack(SulfurRegistry.COPPER.get()),
                ItemStack.EMPTY, new ItemStack(Items.STICK), new ItemStack(Items.GLASS),
                new ItemStack(Items.STICK), ItemStack.EMPTY, ItemStack.EMPTY
        );
    }

    private static RecipeHolder<CraftingRecipe> requireRecipe(GameTestHelper helper, CraftingInput input, String failureMessage) {
        var recipe = helper.getLevel().getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());
        if (recipe.isPresent()) {
            return recipe.get();
        }

        var matchingRecipes = helper.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream()
                .filter(entry -> entry.value().matches(input, helper.getLevel()))
                .map(RecipeHolder::id)
                .map(ResourceKey::identifier)
                .map(Object::toString)
                .toList();

        helper.fail(failureMessage + "; matching recipes: " + matchingRecipes);
        throw new IllegalStateException("Unreachable after helper.fail");
    }
}
