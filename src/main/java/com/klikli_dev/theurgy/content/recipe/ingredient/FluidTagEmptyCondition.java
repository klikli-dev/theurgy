// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;

public record FluidTagEmptyCondition(TagKey<Fluid> tag) implements ICondition {

    public static final Codec<FluidTagEmptyCondition> CODEC = RecordCodecBuilder.create(
            builder -> builder
                    .group(
                            ResourceLocation.CODEC.xmap(loc -> TagKey.create(Registries.FLUID, loc), TagKey::location).fieldOf("tag").forGetter(FluidTagEmptyCondition::tag))
                    .apply(builder, FluidTagEmptyCondition::new));

    public FluidTagEmptyCondition(String location) {
        this(new ResourceLocation(location));
    }

    public FluidTagEmptyCondition(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    public FluidTagEmptyCondition(ResourceLocation tag) {
        this(TagKey.create(Registries.FLUID, tag));
    }

    @Override
    public boolean test(ICondition.IContext context) {
        return context.getTag(this.tag).isEmpty();
    }

    @Override
    public Codec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "fluid_tag_empty(\"" + this.tag.location() + "\")";
    }
}
