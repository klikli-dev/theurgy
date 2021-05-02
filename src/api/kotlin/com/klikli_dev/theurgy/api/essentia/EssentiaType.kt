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

package com.klikli_dev.theurgy.api.essentia

import com.klikli_dev.theurgy.api.TheurgyAPI
import com.klikli_dev.theurgy.api.TheurgyConstants
import net.minecraft.state.EnumProperty
import net.minecraft.util.ColorHelper
import net.minecraft.util.IStringSerializable

enum class EssentiaType(r: Int, g: Int, b: Int, name: String) : IStringSerializable, IEssentiaTypeProvider {

    NONE(128, 128, 128, "none"),
    AER(238, 255, 219, "aer"),
    AQUA(43, 173, 255, "aqua"),
    IGNIS(247, 107, 27, "ignis"),
    TERRA(76, 133, 102, "terra"),
    ORDO(255, 255, 255, "ordo"),
    PERDITIO(0, 0, 0, "perditio");

    val essentiaBlockProperty: EnumProperty<EssentiaType> =
        EnumProperty.create(TheurgyConstants.ESSENTIA_TYPE, EssentiaType::class.java)

    private val r = r / 255f
    private val g = g / 255f
    private val b = b / 255f
    private val color = ColorHelper.PackedColor.packColor(1, r, g, b)
    private val essentiaName: String = name

    fun getRed() = r
    fun getGreen() = g
    fun getBlue() = b
    fun getColor() = color

    override fun getString(): String {
        return this.essentiaName
    }

    override fun getEssentiaType(): EssentiaType {
        return this
    }

    fun getTranslationKey(): String {
        return "essentia.${TheurgyAPI.THEURGY_ID}.${this.string}"
    }

    companion object {
        fun byName(name: String): EssentiaType {
            for (elementType in values()) {
                if (elementType.name == name) {
                    return elementType
                }
            }
            return NONE
        }
    }
}

