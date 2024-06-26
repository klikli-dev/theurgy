package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

public abstract class Filter implements INBTSerializable<CompoundTag> {
    private ItemStack filterItemStack;

    protected Filter(ItemStack filter) {
        this.filterItemStack = filter;
        this.initFromFilterItemStack(filter);
    }

    public static Filter of(ItemStack filter) {
        if (ItemRegistry.LIST_FILTER.get() == filter.getItem())
            return new ListFilter(filter);

        return empty();
    }

    public static Filter of(HolderLookup.Provider provider, CompoundTag nbt) {
        return of(ItemStack.OPTIONAL_CODEC.decode(provider.createSerializationContext(NbtOps.INSTANCE), nbt).getOrThrow().getFirst());
    }


    public static Filter empty() {
        return EmptyFilter.of(ItemStack.EMPTY);
    }

    public ItemStack item() {
        return this.filterItemStack;
    }

    protected abstract void initFromFilterItemStack(ItemStack filterItemStack);

    public boolean test(Level level, ItemStack stack) {
        return this.test(level, stack, false);
    }

    public abstract boolean test(Level level, ItemStack stack, boolean matchDataComponents);

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        return (CompoundTag) ItemStack.OPTIONAL_CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), this.filterItemStack).getOrThrow();
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        var item = ItemStack.OPTIONAL_CODEC.decode(provider.createSerializationContext(NbtOps.INSTANCE), nbt).getOrThrow().getFirst();
        this.filterItemStack = item;
        this.initFromFilterItemStack(item);
    }
}
