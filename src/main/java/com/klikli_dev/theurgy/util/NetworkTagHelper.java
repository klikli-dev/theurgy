// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.function.Consumer;

public final class NetworkTagHelper {
    private NetworkTagHelper() {
    }

    public static CompoundTag write(HolderLookup.Provider registries, Consumer<ValueOutput> writer) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        writer.accept(output);
        return output.buildResult();
    }
}
