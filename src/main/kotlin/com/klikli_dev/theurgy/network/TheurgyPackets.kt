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

import com.klikli_dev.theurgy.Theurgy.id
import net.minecraft.entity.Entity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.RegistryKey
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.PacketDistributor

object TheurgyPackets {
    private const val PROTOCOL_VERSION = "1"

    val instance = NetworkRegistry.newSimpleChannel(
        id("main"),
        { PROTOCOL_VERSION },
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    )

    private var id = 0

    private fun nextID(): Int {
        return id++
    }

    @Suppress("INACCESSIBLE_TYPE")
    fun registerMessages() {
        instance.registerMessage(
            nextID(),
            MessageSetDivinationResult::class.java,
            MessageSetDivinationResult::encode,
            ::MessageSetDivinationResult,
            MessageSetDivinationResult::handle
        )
    }

    fun <MSG> sendTo(player: ServerPlayerEntity?, message: MSG) {
        instance.send(PacketDistributor.PLAYER.with { player }, message)
    }

    fun <MSG> sendToTracking(entity: Entity?, message: MSG) {
        instance.send(PacketDistributor.TRACKING_ENTITY.with { entity }, message)
    }

    fun <MSG> sendToDimension(dimensionKey: RegistryKey<World?>?, message: MSG) {
        instance.send(PacketDistributor.DIMENSION.with { dimensionKey }, message)
    }

    fun <MSG> sendToServer(message: MSG) {
        instance.sendToServer(message)
    }
}