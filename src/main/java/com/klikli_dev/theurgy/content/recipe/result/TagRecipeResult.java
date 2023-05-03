/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe.result;

import com.klikli_dev.theurgy.util.TagUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * A tag result for recipes that use tags as output.
 */
public class TagRecipeResult extends RecipeResult {

    public static final Codec<TagRecipeResult> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(
                TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter((getter) -> {
                    return getter.tag;
                }), Codec.INT.fieldOf("count").forGetter((getter) -> {
                    return getter.count;
                }), CompoundTag.CODEC.optionalFieldOf("nbt").forGetter((getter) -> {
                    return Optional.ofNullable(getter.nbt);
                })).apply(builder, TagRecipeResult::new);
    });
    public static byte TYPE = 1;
    private final TagKey<Item> tag;
    private final int count;
    @Nullable
    private final CompoundTag nbt;

    @Nullable
    protected ItemStack cachedOutputStack;

    @Nullable
    private ItemStack[] cachedStacks;

    public TagRecipeResult(TagKey<Item> tag, int count) {
        this(tag, count, (CompoundTag) null);
    }

    public TagRecipeResult(TagKey<Item> tag, int count, Optional<CompoundTag> nbt) {
        this(tag, count, nbt.orElse(null));
    }

    public TagRecipeResult(TagKey<Item> tag, int count, @Nullable CompoundTag nbt) {
        this.tag = tag;
        this.count = count;
        this.nbt = nbt;
    }

    public static TagRecipeResult fromNetwork(FriendlyByteBuf pBuffer) {
        var tag = TagKey.create(Registries.ITEM, pBuffer.readResourceLocation());
        var count = pBuffer.readVarInt();
        var nbt = pBuffer.readBoolean() ? pBuffer.readNbt() : null;
        return new TagRecipeResult(tag, count, nbt);
    }

    public TagKey<Item> getTag() {
        return this.tag;
    }

    public int getCount() {
        return this.count;
    }

    @Nullable
    public CompoundTag getNbt() {
        return this.nbt;
    }

    public boolean hasNbt() {
        return this.nbt != null;
    }

    @Override
    public ItemStack getStack() {
        if (this.cachedOutputStack == null) {
            var item = TagUtil.getItemStackForTag(this.tag).copy();
            item.setCount(this.count);
            item.setTag(this.nbt);

            if (item.isEmpty()) {
                item = new ItemStack(Blocks.BARRIER).setHoverName(Component.literal("Empty Tag: " + this.tag.location()));
            }

            this.cachedOutputStack = item;
        }
        return this.cachedOutputStack;
    }

    @Override
    public ItemStack[] getStacks() {
        if (this.cachedStacks == null) {
            //get all items in tag
            this.cachedStacks = BuiltInRegistries.ITEM.getTag(this.tag).map(HolderSet.ListBacked::stream).orElse(Stream.empty()).map(ItemStack::new).toArray(ItemStack[]::new);
        }
        return this.cachedStacks;
    }

    @Override
    public byte getType() {
        return TYPE;
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer) {
        super.toNetwork(pBuffer); //write type

        pBuffer.writeResourceLocation(this.tag.location());
        pBuffer.writeVarInt(this.count);
        pBuffer.writeBoolean(this.nbt != null);
        if (this.nbt != null) {
            pBuffer.writeNbt(this.nbt);
        }
    }
}
