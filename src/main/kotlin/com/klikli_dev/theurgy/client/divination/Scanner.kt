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

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.World

/**
 * Based on https://github.com/MightyPirates/Scannable
 */
class Scanner(private var target: Block) {
    private var player: PlayerEntity? = null
    private var center: Vector3d? = null
    private var radius = 0f

    //radius squared for faster comparison of distance
    private var radiusSquared = 0f

    //the max extent of the scan
    private var min: BlockPos? = null
    private var max: BlockPos? = null

    //the current scanned block
    private var x = 0
    private var y = 0
    private var z = 0
    private var blocksPerTick = 0

    fun initialize(player: PlayerEntity, center: Vector3d, radius: Float, totalTicks: Int) {
        this.player = player
        this.center = center
        this.radius = radius
        radiusSquared = this.radius * this.radius
        min = BlockPos(center).add(-this.radius.toDouble(), -this.radius.toDouble(), -this.radius.toDouble())
        max = BlockPos(center).add(this.radius.toDouble(), this.radius.toDouble(), this.radius.toDouble())
        x = min!!.x
        y = min!!.y - 1 //first move next increments this to min.getY();
        z = min!!.z
        val size = max!!.subtract(min!!)
        val blockCount = (size.x + 1) * (size.y + 1) * (size.z + 1)
        blocksPerTick = MathHelper.ceil(blockCount / totalTicks.toFloat())
    }

    fun reset() {
        player = null
        center = null
        radius = 0f
        radiusSquared = 0f
        min = null
        max = null
    }

    fun scan(resultConsumer: (BlockPos) -> Unit) {
        val world = player!!.world
        for (i in 0 until blocksPerTick) {
            //move to next block
            if (!nextBlock(world)) {
                return
            }

            //check if block is within radius
            if (center!!.squareDistanceTo(x + 0.5, y + 0.5, z + 0.5) > radiusSquared) {
                continue
            }
            val pos = BlockPos(x, y, z)
            val state = world.getBlockState(pos)

            //if this is the block we search for, consume it.
            if (isValidBlock(state)) {
                resultConsumer.invoke(pos)
            }
        }
    }

    fun nextBlock(world: World): Boolean {
        y++
        if (y > max!!.y || y >= world.height) {
            y = min!!.y
            x++
            if (x > max!!.x) {
                x = min!!.x
                z++
                if (z > max!!.z) {
                    blocksPerTick = 0
                    return false
                }
            }
        }
        return true
    }

    fun isValidBlock(state: BlockState): Boolean {
        return state.block === target
    }
}