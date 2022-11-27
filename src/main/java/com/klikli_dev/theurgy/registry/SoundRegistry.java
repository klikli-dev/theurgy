/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Theurgy.MODID);

    public static final RegistryObject<SoundEvent> TUNING_FORK = SOUNDS.register("tuning_fork", () -> loadSoundEvent("tuning_fork"));


    /**
     * Creates the sound event object for the given sound event name, as specified in sounds.json Automatically appends
     * MODID.
     *
     * @param name the sound event name without domain.
     * @return the sound event.
     */
    private static SoundEvent loadSoundEvent(String name) {
        ResourceLocation location = new ResourceLocation(Theurgy.MODID, name);
        return new SoundEvent(location);
    }
}
