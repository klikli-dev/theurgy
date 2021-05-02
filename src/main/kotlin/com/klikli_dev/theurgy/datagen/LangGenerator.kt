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
import com.klikli_dev.theurgy.util.TextUtil.trimPatchouli
import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.LanguageProvider

abstract class LangGenerator(generator: DataGenerator, locale: String) :
    LanguageProvider(generator, Theurgy.MOD_ID, locale) {

    class English(generator: DataGenerator) : LangGenerator(generator, "en_us") {
        override fun addTranslations() {
            //Item Groups
            add("itemGroup.${Theurgy.MOD_ID}", "Theurgy")

            //Items
            add("item.${Theurgy.MOD_ID}.grimoire", "The Hermetica")
            add("item.${Theurgy.MOD_ID}.divination_rod_t1", "Crude Divination Rod")
            add("item.${Theurgy.MOD_ID}.divination_rod_t1.message.linked_block", "Attuned to %s.")
            add("item.${Theurgy.MOD_ID}.divination_rod_t1.message.unlinked_block", "The rod is no longer attuned to any block.")
            add("item.${Theurgy.MOD_ID}.divination_rod_t1.message.no_linked_block", "Your rod is currently not attuned to any block.")
            add("item.${Theurgy.MOD_ID}.divination_rod_t1.message.not_an_ore", "The rod can only be attuned to ores.")
            add("item.${Theurgy.MOD_ID}.divination_rod_t1.message.tier_too_low", "You need a higher tier rod to attune to this type of ore.")

            //Blocks

            //Essentia

            //Overlay Tooltips
            add("tooltip.${Theurgy.MOD_ID}.shift_for_more_information", "\n§oPress §lShift§r §ofor more information.§r")
            add("item.patchouli.guide_book.tooltip", "Craft or use command (available to all players) to obtain.")
            add("item.patchouli.guide_book.tooltip.shift", "Use §o/theurgy guide§r to obtain.")

            add(
                "item.${Theurgy.MOD_ID}.divination_rod_t1.tooltip",
                "Can be used to find ores and other objects it can be attuned to."
            )
            add("item.${Theurgy.MOD_ID}.divination_rod_t1.tooltip.attuned", "Attuned to: %s")
            add("item.${Theurgy.MOD_ID}.divination_rod_t1.tooltip.not_attuned", "Not attuned to any block.")
            add(
                "item.${Theurgy.MOD_ID}.divination_rod_t1.tooltip.shift",
                """
                    Craft with fitting items to attune to an Essentia (see JEI for recipes).
                    Shift + Right Click an object to attune the divination rod to it.
                    Shift + Right Click into the air to remove the attunement.
                    Right Click + Hold (takes a few seconds!) to search for the attuned object.
                    Right Click without holding to show the direction of the last found object.
                """.trimIndent()
            )

            //Command replies
            add(
                "message.${Theurgy.MOD_ID}.command.help.reply",
                "Stuck or need help? §6§lClick this message to visit our Discord!§r"
            )

            //Jei

            //Subtitles
            add("${Theurgy.MOD_ID}.subtitle.tuning_fork", "Tuning Fork Sound playing")

            this.generateBook()
        }

        private fun generateBook(){
            //Patchouli
            add(
                "${Theurgy.MOD_ID}.book.landing",
                """
                    The definite guide for the aspiring Theurgist. §(br2)
                    For help or to give feedback please join us in Discord §(l:https://invite.gg/klikli)https://invite.gg/klikli§().
                """.trimPatchouli()
            )
            add("${Theurgy.MOD_ID}.book.subtitle", "A Theurgist's Guide.")

            add(
                "${Theurgy.MOD_ID}.category.getting_started.description",
                "In this chapter the reader learns how to get started in Theurgy."
            )

            add("${Theurgy.MOD_ID}.page.introduction.name", "Introduction")
            add(
                "${Theurgy.MOD_ID}.page.introduction.0.text",
                """
                    Dearest reader,§(br2)
                    The volume before you shall be your trusted guide through the mysteries of Theurgy.
                    Before we start our journey, let me introduce the most important terms to you.
                """.trimPatchouli()
            )
            add("${Theurgy.MOD_ID}.page.introduction.1.title", "Theurgy")
            add(
                "${Theurgy.MOD_ID}.page.introduction.1.text",
                """
                    Called "miracle-working" by the uneducated. Referred to in educated circles as 
                    Hermeticism, Thaumaturgy or Alchemy, it is the science of the invisible 
                    forces that govern our universe.
                """.trimPatchouli()
            )
            add("${Theurgy.MOD_ID}.page.introduction.2.title", "Theurgist")
            add(
                "${Theurgy.MOD_ID}.page.introduction.2.text",
                """
                    Known also as Hermeticist, Thaumaturge, Alchemist. Often seen as a performer of miracles,
                    but simply a person learned in the science of Theurgy.
                """.trimPatchouli()
            )
            add("${Theurgy.MOD_ID}.page.introduction.3.title", "Essentia")
            add(
                "${Theurgy.MOD_ID}.page.introduction.3.text",
                """
                    The indivisible elements §(o)everything§() is made of. 
                    Control over essentia equals (almost) total control over matter.
                """.trimPatchouli()
            )
            add("${Theurgy.MOD_ID}.page.introduction.4.title", "Aether")
            add(
                "${Theurgy.MOD_ID}.page.introduction.4.text",
                """
                    The invisible force that drives and controls the universe. It permeates every object and being. 
                    An obvious choice as energy source, if one knows how to access it.
                """.trimPatchouli()
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
