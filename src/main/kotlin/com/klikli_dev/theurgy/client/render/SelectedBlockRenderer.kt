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

import com.klikli_dev.theurgy.util.RenderUtil
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3d
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.awt.Color

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value= [Dist.CLIENT])
object SelectedBlockRenderer {
    private var selectedBlocks: MutableSet<SelectionInfo> = HashSet()

    /**
     * Highlights the given block position until the given system time (not game time)
     *
     * @param pos        the position to highlight.
     * @param expireTime the time when it expires. Absolute system time, not interval!
     * @param color      the color to render the block in.
     */
    fun selectBlock(pos: BlockPos, expireTime: Long, color: Color = Color(1.0f, 1.0f, 1.0f, 0.8f)) {
        val info = SelectionInfo(pos, expireTime, color)
        if (selectedBlocks.contains(info)) {
            selectedBlocks.remove(info)
        }
        selectedBlocks.add(info)
    }

    /**
     * Unselects the given block position.
     *
     * @param pos the position.
     */
    fun unselectBlock(pos: BlockPos) {
        selectedBlocks.removeIf { info: SelectionInfo -> info.selectedBlock == pos }
    }

    @SubscribeEvent
    fun renderWorldLastEvent(event: RenderWorldLastEvent) {
        renderSelectedBlocks(event)
    }

    private fun renderSelectedBlocks(event: RenderWorldLastEvent) {
        if (selectedBlocks.isNotEmpty()) {
            val time = System.currentTimeMillis()
            val matrixStack: MatrixStack = event.matrixStack
            val buffer: IRenderTypeBuffer.Impl = Minecraft.getInstance().renderTypeBuffers.bufferSource
            val builder: IVertexBuilder = buffer.getBuffer(TheurgyRenderType.BLOCK_SELECTION)
            matrixStack.push()
            val projectedView: Vector3d = Minecraft.getInstance().gameRenderer.activeRenderInfo.projectedView
            matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z)
            val transform: Matrix4f = matrixStack.last.matrix
            val it = selectedBlocks.iterator()
            while (it.hasNext()) {
                val info = it.next()
                if (time > info.selectionExpireTime || info.selectedBlock == null) {
                    //remove expired or invalid selections
                    it.remove()
                    return
                } else {
                    RenderUtil
                        .buildBlockOutline(
                            builder, transform, info.selectedBlock!!.x.toFloat(), info.selectedBlock!!.y.toFloat(),
                            info.selectedBlock!!.z.toFloat(), info.color.red / 255.0f,
                            info.color.green / 255.0f, info.color.blue / 255.0f,
                            info.color.alpha / 255.0f
                        )
                }
            }
            matrixStack.pop()
            RenderSystem.enableTexture()
            RenderSystem.disableDepthTest()
            buffer.finish(TheurgyRenderType.BLOCK_SELECTION)
            RenderSystem.enableDepthTest()
        }
    }

    class SelectionInfo(var selectedBlock: BlockPos?, var selectionExpireTime: Long, var color: Color) {

        override fun hashCode(): Int {
            return selectedBlock.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other === this) return true
            val otherSelectionInfo = other as SelectionInfo? ?: return false
            return otherSelectionInfo.selectedBlock == selectedBlock
        }

    }
}