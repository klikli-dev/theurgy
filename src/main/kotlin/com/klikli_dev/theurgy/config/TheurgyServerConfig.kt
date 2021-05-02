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
package com.klikli_dev.theurgy.config

import com.klikli_dev.theurgy.config.value.CachedInt
import com.klikli_dev.theurgy.config.value.CachedInt.Companion.cache
import net.minecraftforge.common.ForgeConfigSpec

object TheurgyServerConfig : ConfigBase() {
    val divinationSettings: DivinationSettings
    val spec: ForgeConfigSpec

    class DivinationSettings(parent: IConfigCache?, builder: ForgeConfigSpec.Builder) :
        ConfigCategoryBase(parent!!, builder) {
        val divinationTicks: CachedInt

        init {
            builder.comment("Divination Settings").push("divination")
            divinationTicks = cache(
                this,
                builder.comment(
                    """
                        The duration in ticks for a divination scan to complete. 
                        Usually should not be changed, lower values can lead to performance impact on clients.
                    """.trimIndent()
                ).define("divinationTicks", 40)
            )
            builder.pop()
        }
    }

    init {
        val builder = ForgeConfigSpec.Builder()
        divinationSettings = DivinationSettings(this, builder)
        spec = builder.build()
    }
}