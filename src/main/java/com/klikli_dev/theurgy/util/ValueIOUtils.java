// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.function.Consumer;

public final class ValueIOUtils {
    private ValueIOUtils() {
    }

    public static CompoundTag serialize(HolderLookup.Provider registries, ValueIOSerializable serializable) {
        return serialize(registries, serializable::serialize);
    }

    public static CompoundTag serialize(HolderLookup.Provider registries, Consumer<ValueOutput> writer) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        writer.accept(output);
        return output.buildResult();
    }

    public static void deserialize(HolderLookup.Provider registries, ValueIOSerializable serializable, CompoundTag tag) {
        deserialize(registries, tag, serializable::deserialize);
    }

    public static void deserialize(HolderLookup.Provider registries, CompoundTag tag, Consumer<ValueInput> reader) {
        reader.accept(TagValueInput.create(ProblemReporter.DISCARDING, registries, tag));
    }
}
