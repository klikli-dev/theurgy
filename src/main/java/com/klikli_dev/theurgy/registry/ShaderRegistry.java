// SPDX-FileCopyrightText: 2025 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.render.RenderTypes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderProgram;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;


public class ShaderRegistry {

    public static void onRegisterShaders(RegisterShadersEvent event)  {
        try {
            event.registerShader(new ShaderProgram(event.getResourceProvider(), Theurgy.loc("rendertype_distance_lines"), DefaultVertexFormat.POSITION_COLOR_NORMAL), shaderInstance -> {
                RenderTypes.rendertypeDistanceLines = shaderInstance;
            });
        } catch (IOException e) {
            Theurgy.LOGGER.error("Failed to register shader", e);
        }
    }
}
