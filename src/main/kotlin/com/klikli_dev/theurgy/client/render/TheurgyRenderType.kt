/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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
package com.klikli_dev.theurgy.client.render

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat
import java.util.*

@Suppress("INACCESSIBLE_TYPE")
class TheurgyRenderType
    (
    name: String, vertexFormat: VertexFormat, drawMode: Int, bufferSize: Int,
    useDelegate: Boolean, needsSorting: Boolean, setupTaskIn: Runnable,
    clearTaskIn: Runnable
) : RenderType(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, setupTaskIn, clearTaskIn) {
    companion object {
        //region Fields
        val BLOCK_SELECTION_LINE_STATE = LineState(OptionalDouble.of(4.0))
        val BLOCK_SELECTION: RenderType = makeType(
            "overlay_lines", DefaultVertexFormats.POSITION_COLOR, 1,
            256, State.getBuilder().line(BLOCK_SELECTION_LINE_STATE).layer(POLYGON_OFFSET_LAYERING)
                .transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).depthTest(DEPTH_ALWAYS)
                .cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).writeMask(COLOR_WRITE).build(false)
        )
    }
}