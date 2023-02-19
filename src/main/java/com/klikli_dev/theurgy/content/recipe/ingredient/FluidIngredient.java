/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.recipe.ingredient;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FluidIngredient extends Ingredient {

    public static final FluidIngredient EMPTY = new FluidIngredient(Stream.empty());
    private final Value[] values;

    @Nullable
    private FluidStack[] fluidStacks;

    protected FluidIngredient(Stream<? extends Value> pValues) {
        super(Stream.empty());

        this.values = pValues.toArray(Value[]::new);
    }

    public static FluidIngredient ofFluid() {
        return EMPTY;
    }

    public static FluidIngredient ofFluid(int amount, Fluid... pItems) {
        return ofFluid(Arrays.stream(pItems).map(f -> new FluidStack(f, amount)));
    }

    public static FluidIngredient ofFluid(FluidStack... pStacks) {
        return ofFluid(Arrays.stream(pStacks));
    }

    public static FluidIngredient ofFluid(Stream<FluidStack> pStacks) {
        return fromFluidValues(pStacks.filter((p_43944_) -> {
            return !p_43944_.isEmpty();
        }).map(FluidValue::new));
    }

    public static FluidIngredient ofFluid(TagKey<Fluid> pTag, int amount) {
        return fromFluidValues(Stream.of(new TagValue(pTag, amount)));
    }

    public static FluidIngredient fromFluidValues(Stream<? extends Value> pStream) {
        var ingredient = new FluidIngredient(pStream);
        return ingredient.isEmpty() ? EMPTY : ingredient;
    }

    public static FluidIngredient fromJson(@Nullable JsonElement pJson) {
        if (pJson != null && !pJson.isJsonNull()) {
            if (pJson.isJsonObject()) {
                return fromFluidValues(Stream.of(fluidValueFromJson(pJson.getAsJsonObject())));
            } else if (pJson.isJsonArray()) {
                JsonArray jsonarray = pJson.getAsJsonArray();
                if (jsonarray.size() == 0) {
                    throw new JsonSyntaxException("Fluid array cannot be empty, at least one item must be defined");
                } else {
                    return fromFluidValues(StreamSupport.stream(jsonarray.spliterator(), false).map((p_151264_) -> {
                        return fluidValueFromJson(GsonHelper.convertToJsonObject(p_151264_, "" +
                                "fluid"));
                    }));
                }
            } else {
                throw new JsonSyntaxException("Expected fluid to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    public static FluidIngredient fromNetwork(FriendlyByteBuf pBuffer) {
        var size = pBuffer.readVarInt();
        if (size == -1)
            throw new UnsupportedOperationException("FluidIngredients should never be serialized with size -1!");
        return fromFluidValues(Stream.generate(() -> new FluidValue(pBuffer.readFluidStack())).limit(size));
    }

    public static FluidIngredient.Value fluidValueFromJson(JsonObject pJson) {
        if (!pJson.has("amount")) {
            throw new JsonParseException("A fluid ingredient entry must have an amount");
        }
        var amount = GsonHelper.getAsInt(pJson, "amount");

        if (pJson.has("fluid") && pJson.has("tag")) {
            throw new JsonParseException("A fluid ingredient entry is either a tag or a fluid, not both");
        } else if (pJson.has("fluid")) {
            var fluid = fluidFromJson(pJson);
            return new FluidValue(new FluidStack(fluid, amount));
        } else if (pJson.has("tag")) {
            ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(pJson, "tag"));
            TagKey<Fluid> tagkey = TagKey.create(Registries.FLUID, resourcelocation);
            return new TagValue(tagkey, amount);
        } else {
            throw new JsonParseException("A fluid ingredient entry needs either a tag or a fluid");
        }
    }

    public static Fluid fluidFromJson(JsonObject pItemObject) {
        String s = GsonHelper.getAsString(pItemObject, "fluid");
        var fluid = BuiltInRegistries.FLUID.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown fluid '" + s + "'");
        });
        if (fluid == Fluids.EMPTY) {
            throw new JsonSyntaxException("Invalid fluid: " + s);
        } else {
            return fluid;
        }
    }

    @Override
    protected void invalidate() {
        this.invalidate();
        this.fluidStacks = null;
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

    @Override
    public JsonElement toJson() {
        if (this.values.length == 1) {
            return this.values[0].serialize();
        } else {
            JsonArray jsonarray = new JsonArray();

            for (Value value : this.values) {
                jsonarray.add(value.serialize());
            }

            return jsonarray;
        }
    }

    public boolean test(FluidStack fluidStack) {
        if (fluidStack == null) {
            return false;
        } else if (this.isEmpty()) {
            return fluidStack.isEmpty();
        } else {
            for (var fluid : this.getFluids()) {
                //Fluid ingredients need to test for amounts too, hence the containsFluid
                if (fluidStack.containsFluid(fluid)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    public interface Value {
        Collection<FluidStack> getFluids();

        JsonObject serialize();
    }

    public static class FluidValue implements Value {
        private final FluidStack fluid;

        public FluidValue(FluidStack fluid) {
            this.fluid = fluid;
        }

        @Override
        public Collection<FluidStack> getFluids() {
            return ImmutableList.of(this.fluid);
        }

        @Override
        public JsonObject serialize() {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("fluid", BuiltInRegistries.FLUID.getKey(this.fluid.getFluid()).toString());
            jsonobject.addProperty("amount", this.fluid.getAmount());
            return jsonobject;
        }
    }

    public static class TagValue implements Value {
        private final TagKey<Fluid> tag;

        private final int amount;

        public TagValue(TagKey<Fluid> pTag, int amount) {
            this.tag = pTag;
            this.amount = amount;
        }

        @Override
        public Collection<FluidStack> getFluids() {
            var list = new ArrayList<FluidStack>();

            for (Holder<Fluid> holder : BuiltInRegistries.FLUID.getTagOrEmpty(this.tag)) {
                list.add(new FluidStack(holder.get(), this.amount));
            }

            //Note: Item stacks here add a barrier with custom hover name, we just leave the list empty, should be OK.
            return list;
        }

        @Override
        public JsonObject serialize() {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", this.tag.location().toString());
            jsonobject.addProperty("amount", this.amount);
            return jsonobject;
        }
    }

    public static class Serializer implements IIngredientSerializer<FluidIngredient> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public FluidIngredient parse(FriendlyByteBuf buffer) {
            return FluidIngredient.fromFluidValues(Stream.generate(() -> new FluidValue(buffer.readFluidStack())).limit(buffer.readVarInt()));
        }

        @Override
        public FluidIngredient parse(JsonObject json) {
            return FluidIngredient.fromFluidValues(Stream.of(FluidIngredient.fluidValueFromJson(json)));
        }

        @Override
        public void write(FriendlyByteBuf buffer, FluidIngredient ingredient) {
            FluidStack[] fluids = ingredient.getFluids();
            buffer.writeVarInt(fluids.length);

            for (FluidStack stack : fluids)
                buffer.writeFluidStack(stack);
        }
    }
}
