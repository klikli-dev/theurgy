// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SoundRegistry {

    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Theurgy.MODID);

    public static final Supplier<SoundEvent> TUNING_FORK = SOUNDS.register("tuning_fork", () -> loadSoundEvent("tuning_fork"));


    /**
     * Creates the sound event object for the given sound event name, as specified in sounds.json Automatically appends
     * MODID.
     *
     * @param name the sound event name without domain.
     * @return the sound event.
     */
    private static SoundEvent loadSoundEvent(String name) {
        ResourceLocation location = new ResourceLocation(Theurgy.MODID, name);
        return SoundEvent.createVariableRangeEvent(location);
    }
}
