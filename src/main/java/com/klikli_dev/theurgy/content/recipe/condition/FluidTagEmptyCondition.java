// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.recipe.condition;

import com.klikli_dev.theurgy.integration.kubejs.KubeJsIntegration;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public record FluidTagEmptyCondition(TagKey<Fluid> tag) implements ICondition {

    public static final MapCodec<FluidTagEmptyCondition> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            Identifier.CODEC.xmap(loc -> TagKey.create(Registries.FLUID, loc), TagKey::location).fieldOf("tag").forGetter(FluidTagEmptyCondition::tag))
                    .apply(builder, FluidTagEmptyCondition::new));

    public FluidTagEmptyCondition(String location) {
        this(Identifier.parse(location));
    }

    public FluidTagEmptyCondition(String namespace, String path) {
        this(Identifier.fromNamespaceAndPath(namespace, path));
    }

    public FluidTagEmptyCondition(Identifier tag) {
        this(TagKey.create(Registries.FLUID, tag));
    }

    @Override
    public boolean test(ICondition.IContext context) {
        if (KubeJsIntegration.get().isLoaded())
            return KubeJsIntegration.get().isEmpty(this.tag);

        return false; // TODO: Fix getTag
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "fluid_tag_empty(\"" + this.tag.location() + "\")";
    }
}
