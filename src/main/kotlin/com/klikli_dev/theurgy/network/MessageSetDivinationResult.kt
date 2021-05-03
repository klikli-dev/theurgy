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
package com.klikli_dev.theurgy.network

import com.klikli_dev.theurgy.api.TheurgyConstants
import com.klikli_dev.theurgy.item.tool.DivinationRodItem
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.network.NetworkEvent

class MessageSetDivinationResult : MessageBase {
    lateinit var result: BlockPos

    constructor(buf: PacketBuffer) {
        decode(buf)
    }

    constructor(result: BlockPos) {
        this.result = result
    }

    override fun onServerReceived(
        minecraftServer: MinecraftServer, player: ServerPlayerEntity,
        context: NetworkEvent.Context
    ) {
        val stack = player.getHeldItem(Hand.MAIN_HAND)
        if (stack.item is DivinationRodItem) {
            stack.orCreateTag.putLong(TheurgyConstants.Nbt.DIVINATION_RESULT, result.toLong())
            player.container.detectAndSendChanges()
        }
    }

    override fun encode(buf: PacketBuffer) {
        buf.writeLong(result.toLong())
    }

    override fun decode(buf: PacketBuffer) {
        result = BlockPos.fromLong(buf.readLong())
    }
}