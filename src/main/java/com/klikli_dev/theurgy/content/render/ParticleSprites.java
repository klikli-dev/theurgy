// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.magicparticleslib.MagicParticlesLib;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

public class ParticleSprites {
    private static final Identifier MPL_GLOW_SPRITE = Identifier.fromNamespaceAndPath(MagicParticlesLib.MODID, "particle_glow");
    public static TextureAtlasSprite GLOW;

    public static void onTextureAtlasStitched(TextureAtlasStitchedEvent event) {
        TextureAtlas map = event.getAtlas();
        if (!map.location().equals(TextureAtlas.LOCATION_PARTICLES)) {
            return;
        }

        GLOW = map.getSprite(MPL_GLOW_SPRITE);
    }
}
