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

object TheurgyClientConfig : ConfigBase() {
    val divinationSettings: DivinationSettings
    val spec: ForgeConfigSpec

    class DivinationSettings(parent: IConfigCache?, builder: ForgeConfigSpec.Builder) :
        ConfigCategoryBase(parent!!, builder) {
        val divinationRadius: CachedInt

        init {
            builder.comment("Divination Settings").push("divination")
            divinationRadius = cache(
                this,
                builder.comment(
                    "The divination radius in blocks. A higher radius can lead to performance impact on clients."
                ).define("divinationRadius", 96)
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