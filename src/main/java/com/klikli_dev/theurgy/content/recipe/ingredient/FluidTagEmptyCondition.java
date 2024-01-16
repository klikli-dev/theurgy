// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.ingredient;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.conditions.ICondition;
import net.neoforged.neoforge.common.crafting.conditions.IConditionSerializer;

public class FluidTagEmptyCondition implements ICondition {
    private static final ResourceLocation NAME = Theurgy.loc("fluid_tag_empty");
    private final TagKey<Fluid> tag;

    public FluidTagEmptyCondition(String location) {
        this(new ResourceLocation(location));
    }

    public FluidTagEmptyCondition(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    public FluidTagEmptyCondition(ResourceLocation tag) {
        this.tag = TagKey.create(Registries.FLUID, tag);
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        return context.getTag(this.tag).isEmpty();
    }

    @Override
    public String toString() {
        return "fluid_tag_empty(\"" + this.tag.location() + "\")";
    }

    public static class Serializer implements IConditionSerializer<FluidTagEmptyCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, FluidTagEmptyCondition value) {
            json.addProperty("tag", value.tag.location().toString());
        }

        @Override
        public FluidTagEmptyCondition read(JsonObject json) {
            return new FluidTagEmptyCondition(new ResourceLocation(GsonHelper.getAsString(json, "tag")));
        }

        @Override
        public ResourceLocation getID() {
            return FluidTagEmptyCondition.NAME;
        }
    }
}
