// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.ingredient;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.registry.IngredientTypeRegistry;
import com.klikli_dev.theurgy.util.TheurgyExtraCodecs;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluidIngredient extends Ingredient {

    public static final Codec<FluidIngredient> CODEC = codec(true);
    public static final Codec<FluidIngredient> CODEC_NONEMPTY = codec(false);

    public static final FluidIngredient EMPTY = new FluidIngredient(Stream.empty());
    private final Value[] values;
    @Nullable
    private FluidStack[] fluidStacks;

    protected FluidIngredient(Stream<? extends Value> pValues) {
        super(Stream.empty(), IngredientTypeRegistry.FLUID_INGREDIENT);

        this.values = pValues.toArray(Value[]::new);
    }

    protected FluidIngredient(Value[] values) {
        super(Stream.empty(), IngredientTypeRegistry.FLUID_INGREDIENT);
        this.values = values;
    }

    private static Codec<FluidIngredient> codec(boolean pAllowEmpty) {
        Codec<FluidIngredient.Value[]> codec = Codec.list(FluidIngredient.Value.CODEC)
                .comapFlatMap(
                        from -> !pAllowEmpty && from.size() < 1
                                ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined")
                                : DataResult.success(from.toArray(new FluidIngredient.Value[0])),
                        List::of
                );
        return ExtraCodecs.either(codec, FluidIngredient.Value.CODEC)
                .flatComapMap(
                        to -> to.map(FluidIngredient::new, value -> new FluidIngredient(new FluidIngredient.Value[]{value})),
                        from -> {
                            if (from.values.length == 1) {
                                return DataResult.success(Either.right(from.values[0]));
                            } else {
                                return from.values.length == 0 && !pAllowEmpty
                                        ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined")
                                        : DataResult.success(Either.left(from.values));
                            }
                        }
                );
    }

    public static FluidIngredient ofFluid() {
        return EMPTY;
    }

    public static FluidIngredient ofFluid(Fluid... pFluids) {
        return ofFluid(Arrays.stream(pFluids).map(f -> new FluidStack(f, 1)));
    }

    public static FluidIngredient ofFluid(FluidStack... pStacks) {
        return ofFluid(Arrays.stream(pStacks));
    }

    public static FluidIngredient ofFluid(Stream<FluidStack> pStacks) {
        return fromFluidValues(pStacks.filter((p_43944_) -> {
            return !p_43944_.isEmpty();
        }).map(FluidValue::new));
    }

    public static FluidIngredient ofFluid(TagKey<Fluid> pTag) {
        return fromFluidValues(Stream.of(new FluidTagValue(pTag)));
    }

    public static FluidIngredient fromFluidValues(Stream<? extends Value> pStream) {
        var ingredient = new FluidIngredient(pStream);
        return ingredient.isEmpty() ? EMPTY : ingredient;
    }


    public static FluidIngredient fromNetwork(FriendlyByteBuf pBuffer) {
        var size = pBuffer.readVarInt();
        if (size == -1) //indicates we are synchronizing with Codec, instead of with contents
            return pBuffer.readWithCodecTrusted(net.minecraft.nbt.NbtOps.INSTANCE, CODEC);
        return fromFluidValues(Stream.generate(() -> new FluidValue(pBuffer.readFluidStack())).limit(size));
    }

    @Override
    public boolean isEmpty() {
        return this.values.length == 0;
    }

    @Override
    public IntList getStackingIds() {
        return super.getStackingIds();
    }

    @Override
    public ItemStack[] getItems() {
        return super.getItems(); //will be empty
    }

    public FluidStack[] getFluids() {
        if (this.fluidStacks == null) {
            this.fluidStacks = Arrays.stream(this.values).flatMap((value) -> value.getFluids().stream()).distinct().toArray(FluidStack[]::new);
        }

        return this.fluidStacks;
    }

    @Override
    public boolean test(@Nullable ItemStack pStack) {
        return false;
    }

    public boolean test(FluidStack fluidStack) {
        if (fluidStack == null) {
            return false;
        } else if (this.isEmpty()) {
            return fluidStack.isEmpty();
        } else {
            for (var fluid : this.getFluids()) {
                //does not test for amount (just like ingredient)
                if (fluidStack.isFluidEqual(fluid)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer) {
        if (this.synchronizeWithContents()) {
            pBuffer.writeCollection(Arrays.asList(this.getFluids()), FriendlyByteBuf::writeFluidStack);
        } else {
            pBuffer.writeVarInt(-1);
            pBuffer.writeWithCodec(net.minecraft.nbt.NbtOps.INSTANCE, CODEC, this);
        }
    }

    @Override
    public boolean equals(Object pOther) {
        return pOther instanceof FluidIngredient ingredient && Arrays.equals(this.values, ingredient.values);
    }

    public interface Value {
        Codec<FluidIngredient.Value> CODEC = ExtraCodecs.xor(FluidIngredient.FluidValue.CODEC, FluidTagValue.CODEC)
                .xmap(first -> first.map(l -> l, r -> r), second -> {
                    if (second instanceof FluidTagValue tagvalue) {
                        return Either.right(tagvalue);
                    } else if (second instanceof FluidIngredient.FluidValue fluidValue) {
                        return Either.left(fluidValue);
                    } else {
                        throw new UnsupportedOperationException("This is neither an fluid value nor a tag value.");
                    }
                });

        Collection<FluidStack> getFluids();
    }

    public static class FluidValue implements Value {

        public static final Codec<FluidIngredient.FluidValue> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        TheurgyExtraCodecs.SINGLE_FLUID_CODEC.fieldOf("fluid").forGetter(value -> value.fluid)
                ).apply(instance, FluidIngredient.FluidValue::new)
        );
        private final FluidStack fluid;

        public FluidValue(FluidStack fluid) {
            this.fluid = fluid;
        }

        @Override
        public Collection<FluidStack> getFluids() {
            return ImmutableList.of(this.fluid);
        }

        @Override
        public boolean equals(Object pOther) {
            if (!(pOther instanceof FluidValue fluidValue)) {
                return false;
            } else {
                return this.fluid.isFluidStackIdentical(fluidValue.fluid);
            }
        }
    }

    public static class FluidTagValue implements Value {

        static final Codec<FluidTagValue> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        TagKey.codec(Registries.FLUID).fieldOf("tag").forGetter(p_301154_ -> p_301154_.tag)
                ).apply(instance, FluidTagValue::new)
        );
        private final TagKey<Fluid> tag;

        public FluidTagValue(TagKey<Fluid> pTag) {
            this.tag = pTag;
        }

        @Override
        public Collection<FluidStack> getFluids() {
            return BuiltInRegistries.FLUID.getTag(this.tag)
                    .map(tag -> tag.stream().map(h -> new FluidStack(h, 1)).collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
        }

        @Override
        public boolean equals(Object pOther) {
            return pOther instanceof FluidTagValue tagValue && tagValue.tag.location().equals(this.tag.location());
        }
    }
}
