// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.result;

import com.klikli_dev.theurgy.registry.RecipeResultRegistry;
import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * A tag result for recipes that use tags as output.
 */
public class ItemRecipeResult extends RecipeResult {

    public static final MapCodec<ItemRecipeResult> INGREDIENT_COMPAT_CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter((ItemRecipeResult t) -> t.template.item()),
            Codec.INT.fieldOf("count").forGetter((ItemRecipeResult t) -> t.template.count()),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter((ItemRecipeResult t) -> t.template.components())
    ).apply(builder, (item, count, components) -> new ItemRecipeResult(new ItemStackTemplate(item, count, components))));

    public static final MapCodec<ItemRecipeResult> ITEM_STACK_COMPAT_CODEC = MapCodec.assumeMapUnsafe(ItemStackTemplate.CODEC.xmap(ItemRecipeResult::new, (ItemRecipeResult t) -> t.template));

    public static final MapCodec<ItemRecipeResult> CODEC = TheurgyExtraCodecs.mapWithAlternative(
            ITEM_STACK_COMPAT_CODEC,
            INGREDIENT_COMPAT_CODEC
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemRecipeResult> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC,
            (ItemRecipeResult t) -> t.template,
            ItemRecipeResult::new
    );

    private final ItemStackTemplate template;

    private ItemStack stack;

    @Nullable
    private ItemStack[] cachedStacks;

    public ItemRecipeResult(ItemStack stack) {
        this(ItemStackTemplate.fromNonEmptyStack(stack));
    }

    public ItemRecipeResult(ItemStackTemplate template) {
        this.template = template;
    }

    @Override
    public ItemStack getStack() {
        if (this.stack == null) {
            this.stack = this.template.create();
        }
        return this.stack;
    }

    @Override
    public ItemStack[] getStacks() {
        if (this.cachedStacks == null) {
            this.cachedStacks = new ItemStack[]{this.getStack()};
        }
        return this.cachedStacks;
    }

    @Override
    public RecipeResultType<?> getType() {
        return RecipeResultRegistry.ITEM.get();
    }

    @Override
    public ItemRecipeResult copyWithCount(int count) {
        return new ItemRecipeResult(this.template.withCount(count));
    }
}
