/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.klikli_dev.theurgy.client.particle

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.particle.IParticleRenderType
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats

@SuppressWarnings("deprecation")
object TheurgyParticleRenderTypes {
    val particleSheetAdditive: IParticleRenderType = object : IParticleRenderType {
        override fun beginRender(bufferBuilder: BufferBuilder, textureManager: TextureManager) {
            RenderSystem.depthMask(true)
            textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE)
            RenderSystem.disableCull()
            RenderSystem.depthMask(false)
            RenderSystem.enableBlend()
            RenderSystem.blendFunc(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE
            )
            RenderSystem.alphaFunc(516, 0.003921569f)
            bufferBuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP)
        }

        override fun finishRender(tesselator: Tessellator) {
            tesselator.draw()
        }

        override fun toString(): String {
            return "PARTICLE_SHEET_ADDITIVE"
        }
    }

    val particleSheetTranslucentNoDepth: IParticleRenderType =
        object : IParticleRenderType {
            override fun beginRender(bufferBuilder: BufferBuilder, textureManager: TextureManager) {
                RenderSystem.depthMask(false)
                textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE)
                RenderSystem.enableBlend()
                RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                )
                RenderSystem.alphaFunc(516, 0.003921569f)
                bufferBuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP)
            }

            override fun finishRender(tesselator: Tessellator) {
                tesselator.draw()
                RenderSystem.depthMask(true)
            }

            override fun toString(): String {
                return "PARTICLE_SHEET_TRANSLUCENT_NO_DEPTH"
            }
        }
}