/*
 * MIT License
 *
 * Copyright 2020 klikli-dev, Florian "Sangar" Nücke
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
package com.klikli_dev.theurgy.client.divination

import com.klikli_dev.theurgy.config.TheurgyClientConfig
import com.klikli_dev.theurgy.config.TheurgyServerConfig
import com.klikli_dev.theurgy.util.Math3DUtil.getCenter
import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import java.util.function.Consumer

/**
 * Based on https://github.com/MightyPirates/Scannable
 */
object ScanManager {
    private var results: MutableList<BlockPos> = ArrayList()
    private var scanner: Scanner? = null
    private var scanningTicks = -1

    fun beginScan(player: PlayerEntity, target: Block) {
        cancelScan()
        scanner = Scanner(target)
        scanner!!.initialize(player, player.positionVec,
            TheurgyClientConfig.divinationSettings.divinationRadius.get().toFloat(),
            TheurgyServerConfig.divinationSettings.divinationTicks.get())
    }

    fun updateScan(player: PlayerEntity?, forceFinish: Boolean) {
        val remainingTicks = TheurgyServerConfig.divinationSettings.divinationTicks.get() - scanningTicks
        if (remainingTicks <= 0 || scanner == null) {
            return
        }

        //if we are not forcing we simply tick once
        if (!forceFinish) {
            scanner!!.scan { result: BlockPos -> results.add(result) }
            scanningTicks++
            return
        }

        //when forcing we scan through all remaining tikcs at once
        for (i in 0 until remainingTicks) {
            scanner!!.scan { result: BlockPos -> results.add(result) }
            scanningTicks++
        }
    }

    fun finishScan(player: PlayerEntity): BlockPos? {
        updateScan(player, true)
        val scanCenter: Vector3d = player.positionVec
        results
            .sortWith(Comparator.comparing { result: BlockPos ->
                scanCenter.squareDistanceTo(
                    result.getCenter()
                )
            })
        val result: BlockPos? = if (results.isNotEmpty()) results[0] else null
        cancelScan()
        return result
    }

    fun cancelScan() {
        scanner = null
        results.clear()
        scanningTicks = -1
    }
}