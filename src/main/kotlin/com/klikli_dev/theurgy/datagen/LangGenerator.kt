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

package com.klikli_dev.theurgy.datagen

import com.klikli_dev.theurgy.Theurgy
import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.LanguageProvider

abstract class LangGenerator(generator: DataGenerator, locale: String) :
    LanguageProvider(generator, Theurgy.MOD_ID, locale) {

    class English(generator: DataGenerator) : LangGenerator(generator, "en_us") {
        override fun addTranslations() {
            //Item Groups
            add("itemGroup.${Theurgy.MOD_ID}", "Theurgy");

            //Items
            add("item.${Theurgy.MOD_ID}.grimoire", "The Hermetica");

            //Blocks

            //Essentia

            //Overlay Tooltips

            //Jei

            //Patchouli


            add(
                "${Theurgy.MOD_ID}.book.landing",
                "The definite guide for the aspiring Theurgist. $(br2)For help or to give feedback please join " +
                        "us in Discord $(l:https://invite.gg/klikli)https://invite.gg/klikli$()."
            )
            add("${Theurgy.MOD_ID}.book.subtitle", "A Theurgist's Guide.")

            add(
                "${Theurgy.MOD_ID}.category.getting_started.description",
                "In this chapter the reader learns how to get started in Theurgy."
            )

            add("${Theurgy.MOD_ID}.page.introduction.name", "Introduction")
            add(
                "${Theurgy.MOD_ID}.page.introduction.0.text",
                "Dearest reader,$(br2)The volume before you shall be your trusted guide through the mysteries of Theurgy. " +
                        "Before we start our journey, let me introduce the most important terms to you."
            )
            add("${Theurgy.MOD_ID}.page.introduction.1.title", "Theurgy")
            add(
                "${Theurgy.MOD_ID}.page.introduction.1.text",
                "Called \"miracle-working\" by the uneducated. Referred to in educated circles as Hermeticism, Thaumaturgy or Alchemy, " +
                        "it is the science of the invisible forces that govern our universe."
            )
            add("${Theurgy.MOD_ID}.page.introduction.2.title", "Theurgist")
            add(
                "${Theurgy.MOD_ID}.page.introduction.2.text",
                "Known also as Hermeticist, Thaumaturge, Alchemist. Often seen as a performer of miracles, " +
                        "but simply a person learned in the science of Theurgy."
            )
            add("${Theurgy.MOD_ID}.page.introduction.3.title", "Essentia")
            add(
                "${Theurgy.MOD_ID}.page.introduction.3.text",
                "The indivisible elements $(o)everything$() is made of. Control over essentia equals (almost) total control over matter."
            )
            add("${Theurgy.MOD_ID}.page.introduction.4.title", "Aether")
            add(
                "${Theurgy.MOD_ID}.page.introduction.4.text",
                "The invisible force that drives and controls the universe. It permeates every object and being. " +
                        "An obvious choice as energy source, if one knows how to access it."
            )
            add("${Theurgy.MOD_ID}.page.introduction.5.title", "Resonance")
            add(
                "${Theurgy.MOD_ID}.page.introduction.5.text",
                "The concept, that every object oscillates at a certain frequency. Once the frequency is known, finding alike objects is trivial."
            )

            add("${Theurgy.MOD_ID}.page.divination_rods.name", "Divination Rods")
            add("${Theurgy.MOD_ID}.page.divination_rods.0.title", "")
            add("${Theurgy.MOD_ID}.page.divination_rods.0.text", "")

//            add("${Theurgy.MOD_ID}.page.introduction.x.title", "")
//            add("${Theurgy.MOD_ID}.page.introduction.x.text", "")
//
//            add("${Theurgy.MOD_ID}.page.x.name", "")
        }
    }
}
