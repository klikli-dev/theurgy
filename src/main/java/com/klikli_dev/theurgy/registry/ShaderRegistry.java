// SPDX-FileCopyrightText: 2025 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderProgram;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;


import net.minecraft.client.renderer.ShaderDefines;

public class ShaderRegistry {

    public static void onRegisterShaders(RegisterShadersEvent event)  {
        //try {
            var shader = new ShaderProgram(Theurgy.loc("rendertype_distance_lines"), DefaultVertexFormat.POSITION_COLOR_NORMAL, ShaderDefines.EMPTY);
            event.registerShader(shader);
            //RenderTypes.rendertypeDistanceLines = shader;
        //} catch (IOException e) {
        //    Theurgy.LOGGER.error("Failed to register shader", e);
        //}
    }
}
