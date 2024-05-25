// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render;

import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

public class BlockOverlays {
    public static TextureAtlasSprite WHITE;

    public static void onTextureAtlasStitched(TextureAtlasStitchedEvent event){
        TextureAtlas map = event.getAtlas();
        if (!map.location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            return;
        }

        WHITE = map.getSprite(Theurgy.loc("block/overlay/white"));
    }
}
