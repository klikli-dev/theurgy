package com.klikli_dev.theurgy.datagen

import com.klikli_dev.theurgy.Theurgy
import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.LanguageProvider

abstract class LangGenerator(generator: DataGenerator, locale: String) :
    LanguageProvider(generator, Theurgy.MOD_ID, locale) {

    class English(generator: DataGenerator) : LangGenerator(generator, "en_us") {
        override fun addTranslations() {
            //Item Groups
            add("itemGroup.theurgy", "Theurgy");

            //Items
            add("item.theurgy.grimoire", "The Hermetica");

            //Blocks

            //Essentia

            //Overlay Tooltips

            //Jei

            //Patchouli


            add(
                "theurgy.book.landing",
                "The definite guide for the aspiring Theurgist. $(br2)For help or to give feedback please join " +
                        "us in Discord $(l:https://invite.gg/klikli)https://invite.gg/klikli$()."
            )
            add("theurgy.book.subtitle", "A Theurgist's Guide.")

            add(
                "theurgy.category.getting_started.description",
                "In this chapter the reader learns how to get started in Theurgy."
            )

            add("theurgy.page.introduction.name", "Introduction")
            add(
                "theurgy.page.introduction.0.text",
                "Dearest reader,$(br2)The volume before you shall be your trusted guide through the mysteries of Theurgy. " +
                        "Before we start our journey, let me introduce the most important terms to you."
            )
            add("theurgy.page.introduction.1.title", "Theurgy")
            add(
                "theurgy.page.introduction.1.text",
                "Called \"miracle-working\" by the uneducated. Referred to in educated circles as Hermeticism, Thaumaturgy or Alchemy, " +
                        "it is the science of the invisible forces that govern our universe."
            )
            add("theurgy.page.introduction.2.title", "Theurgist")
            add(
                "theurgy.page.introduction.2.text",
                "Known also as Hermeticist, Thaumaturge, Alchemist. Often seen as a performer of miracles, " +
                        "but simply a person learned in the science of Theurgy."
            )
            add("theurgy.page.introduction.3.title", "Essentia")
            add(
                "theurgy.page.introduction.3.text",
                "The indivisible elements $(o)everything$() is made of. Control over essentia equals (almost) total control over matter."
            )
            add("theurgy.page.introduction.4.title", "Aether")
            add(
                "theurgy.page.introduction.4.text",
                "The invisible force that drives and controls the universe. It permeates every object and being. " +
                        "An obvious choice as energy source, if one knows how to access it."
            )
            add("theurgy.page.introduction.5.title", "Resonance")
            add(
                "theurgy.page.introduction.5.text",
                "The concept, that every object oscillates at a certain frequency. Once the frequency is known, finding alike objects is trivial."
            )

            add("theurgy.page.divination_rods.name", "Divination Rods")
            add("theurgy.page.divination_rods.0.title", "")
            add("theurgy.page.divination_rods.0.text", "")

//            add("theurgy.page.introduction.x.title", "")
//            add("theurgy.page.introduction.x.text", "")
//
//            add("theurgy.page.x.name", "")
        }
    }
}
