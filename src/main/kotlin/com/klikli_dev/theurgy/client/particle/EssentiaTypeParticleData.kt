/*
 * MIT License
 *
 * Copyright 2021 klikli-dev, sirttas
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

import com.klikli_dev.theurgy.api.essentia.EssentiaType
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.serialization.Codec
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType

/**
 * https://github.com/Sirttas/ElementalCraft
 */
class EssentiaTypeParticleData(
    private val type: ParticleType<EssentiaTypeParticleData>,
    val essentiaType: EssentiaType
) : IParticleData {
    override fun getType(): ParticleType<EssentiaTypeParticleData> {
        return type
    }

    override fun write(buffer: PacketBuffer) {
        buffer.writeString(essentiaType.string)
    }

    override fun getParameters(): String {
        return getType().registryName.toString() + ":" + essentiaType.string
    }

    companion object {
        //fully qualified to avoid unsuppressible deprecation warning in import
        @Suppress("DEPRECATION")
        val deserializer: net.minecraft.particles.IParticleData.IDeserializer<EssentiaTypeParticleData> =
            object : net.minecraft.particles.IParticleData.IDeserializer<EssentiaTypeParticleData> {
                @Throws(CommandSyntaxException::class)

                override fun deserialize(
                    type: ParticleType<EssentiaTypeParticleData>,
                    reader: StringReader
                ): EssentiaTypeParticleData {
                    reader.expect(' ')
                    return EssentiaTypeParticleData(type, EssentiaType.byName(reader.readString()))
                }

                override fun read(
                    type: ParticleType<EssentiaTypeParticleData>,
                    buffer: PacketBuffer
                ): EssentiaTypeParticleData {
                    return EssentiaTypeParticleData(type, EssentiaType.byName(buffer.readString()))
                }
            }

        fun codecFor(particleType: ParticleType<EssentiaTypeParticleData>): Codec<EssentiaTypeParticleData> {
            return EssentiaType.codec.xmap({ e ->
                EssentiaTypeParticleData(particleType, e)
            }) { obj: EssentiaTypeParticleData -> obj.essentiaType }
        }

        fun createParticleType(alwaysShow: Boolean): ParticleType<EssentiaTypeParticleData> {
            return object : ParticleType<EssentiaTypeParticleData>(alwaysShow, deserializer) {
                override fun func_230522_e_(): Codec<EssentiaTypeParticleData> {
                    return codecFor(this)
                }
            }
        }
    }
}