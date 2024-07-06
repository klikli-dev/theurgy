// SPDX-FileCopyrightText: 2024 klikli-dev
// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter.attribute;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantAttribute implements ItemAttribute {
    public static final EnchantAttribute EMPTY = new EnchantAttribute(null);

    private final Holder<Enchantment> enchantment;

    public EnchantAttribute(@Nullable Holder<Enchantment> enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public boolean appliesTo(ItemStack stack, Level level) {
        return stack.getAllEnchantments(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)).keySet().contains(this.enchantment);
    }

    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return itemStack.getTagEnchantments().keySet().contains(this.enchantment);
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack itemStack) {
        return itemStack.getTagEnchantments().keySet().stream().map(EnchantAttribute::new).collect(Collectors.toList());
    }

    @Override
    public String getTranslationKey() {
        return "has_enchant";
    }

    @Override
    public Object[] getTranslationParameters() {
        if (this.enchantment != null)
            return new Object[]{this.enchantment.value().description()};
        return new Object[]{};
    }

    @Override
    public void writeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        if (this.enchantment == null)
            return;
        ResourceLocation id = this.enchantment.unwrapKey().get().location();
        if (id == null)
            return;
        nbt.putString("id", id.toString());
    }

    @Override
    public ItemAttribute readNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        return nbt.contains("id") ? new EnchantAttribute(
                pRegistries.lookupOrThrow(Registries.ENCHANTMENT).get(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.tryParse(nbt.getString("id")))).get()) : EMPTY;
    }
}
