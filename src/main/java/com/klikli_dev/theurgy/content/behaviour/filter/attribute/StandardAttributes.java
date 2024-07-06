// SPDX-FileCopyrightText: 2024 klikli-dev
// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter.attribute;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public enum StandardAttributes implements ItemAttribute {

    DUMMY(s -> false),
    PLACEABLE(s -> s.getItem() instanceof BlockItem),
    CONSUMABLE(s -> s.getFoodProperties(null) != null),
    FLUID_CONTAINER(s -> s.getCapability(Capabilities.FluidHandler.ITEM) != null),
    ENCHANTED(ItemStack::isEnchanted),
    MAX_ENCHANTED(StandardAttributes::maxEnchanted),
    RENAMED(s -> s.has(DataComponents.CUSTOM_NAME)),
    DAMAGED(ItemStack::isDamaged),
    BADLY_DAMAGED(s -> s.isDamaged() && (float) s.getDamageValue() / s.getMaxDamage() > 3 / 4f),
    NOT_STACKABLE(((Predicate<ItemStack>) ItemStack::isStackable).negate()),
    EQUIPABLE(s -> Equipable.get(s) != null),
    FURNACE_FUEL(AbstractFurnaceBlockEntity::isFuel),
    SMELTABLE((s, w) -> testRecipe(s, w, RecipeType.SMELTING)),
    SMOKABLE((s, w) -> testRecipe(s, w, RecipeType.SMOKING)),
    BLASTABLE((s, w) -> testRecipe(s, w, RecipeType.BLASTING)),
    COMPOSTABLE(s -> ComposterBlock.COMPOSTABLES.containsKey(s.getItem()));

    private Predicate<ItemStack> test;
    private BiPredicate<ItemStack, Level> testWithWorld;

    StandardAttributes(Predicate<ItemStack> test) {
        this.test = test;
    }

    StandardAttributes(BiPredicate<ItemStack, Level> test) {
        this.testWithWorld = test;
    }

    private static boolean testRecipe(ItemStack s, Level level, RecipeType<? extends Recipe<SingleRecipeInput>> type) {
        var input = new SingleRecipeInput(s);
        return level.getRecipeManager()
                .getRecipeFor(type, input, level)
                .isPresent();
    }

    private static boolean maxEnchanted(ItemStack s, Level level) {
        return s.getAllEnchantments(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)).entrySet()
                .stream()
                .anyMatch(e -> e.getKey().unwrap().right().get().getMaxLevel() <= e.getIntValue());
    }

    @Override
    public boolean appliesTo(ItemStack stack, Level world) {
        if (this.testWithWorld != null)
            return this.testWithWorld.test(stack, world);
        return this.appliesTo(stack);
    }

    @Override
    public boolean appliesTo(ItemStack stack) {
        return this.test.test(stack);
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack stack, Level world) {
        List<ItemAttribute> attributes = new ArrayList<>();
        for (var trait : values())
            if (trait.appliesTo(stack, world))
                attributes.add(trait);
        return attributes;
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack stack) {
        return null;
    }

    @Override
    public String getTranslationKey() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getNBTKey() {
        return "standard_trait";
    }

    @Override
    public void writeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        nbt.putBoolean(this.name(), true);
    }

    @Override
    public ItemAttribute readNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        for (var trait : values())
            if (nbt.contains(trait.name()))
                return trait;
        return null;
    }
}
