// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter;

import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public abstract class Filter implements INBTSerializable<CompoundTag> {
    protected ItemStack filterItemStack;

    protected Filter(HolderLookup.Provider provider, ItemStack filter) {
        this.filterItemStack = filter;
        this.initFromFilterItemStack(provider, filter);
    }

    public static Filter of(HolderLookup.Provider provider, ItemStack filter) {
        if (ItemRegistry.LIST_FILTER.get() == filter.getItem())
            return new ListFilter(provider, filter);

        if (ItemRegistry.ATTRIBUTE_FILTER.get() == filter.getItem())
            return new AttributeFilter(provider, filter);

        return empty();
    }

    public static Filter of(HolderLookup.Provider provider, CompoundTag nbt) {
        return of(provider, ItemStack.OPTIONAL_CODEC.decode(provider.createSerializationContext(NbtOps.INSTANCE), nbt).getOrThrow().getFirst());
    }

    public static Filter empty() {
        return new EmptyFilter();
    }

    public ItemStack item() {
        return this.filterItemStack;
    }

    protected abstract void initFromFilterItemStack(HolderLookup.Provider provider, ItemStack filterItemStack);

    public boolean test(Level level, ItemStack stack) {
        return this.test(level, stack, false);
    }

    public abstract boolean test(Level level, ItemStack stack, boolean matchDataComponents);

    public abstract boolean isEmpty();

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        return (CompoundTag) ItemStack.OPTIONAL_CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this.filterItemStack).getOrThrow();
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        var item = ItemStack.OPTIONAL_CODEC.decode(provider.createSerializationContext(NbtOps.INSTANCE), nbt).getOrThrow().getFirst();
        this.filterItemStack = item;
        this.initFromFilterItemStack(provider, item);
    }
}
