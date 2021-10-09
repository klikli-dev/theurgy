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

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public abstract class LangGenerator extends LanguageProvider {
    public LangGenerator(DataGenerator generator, String locale) {
        super(generator, Theurgy.MODID, locale);
    }

    protected String itemKey(String id) {
        return "item." + Theurgy.MODID + "." + id;
    }

    protected void advancementTitle(String name, String s) {
        this.add(AdvancementsGenerator.title(name).getKey(), s);
    }

    protected void advancementDescr(String name, String s) {
        this.add(AdvancementsGenerator.descr(name).getKey(), s);
    }

    protected void addTooltip(Supplier<? extends Item> key, String tooltip) {
        this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_SUFFIX, tooltip);
    }

    protected void addTooltip(Supplier<? extends Item> key, String tooltip, String extendedTooltip) {
        this.addTooltip(key, tooltip);
        this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_EXTENDED_SUFFIX, extendedTooltip);
    }

    protected void addTooltip(Supplier<? extends Item> key, String tooltip, String extendedTooltip, String usageTooltip) {
        this.addTooltip(key, tooltip, extendedTooltip);
        this.add(key.get().getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_USAGE_SUFFIX, usageTooltip);
    }

    public static final class English extends LangGenerator {

        public English(DataGenerator generator) {
            super(generator, "en_us");
        }

        protected void addTranslations() {
            this.addMisc();
            this.addItems();
            this.addTooltips();
            this.addAdvancements();
            this.addPatchouli();
        }

        private void addMisc() {
            this.add("itemGroup.theurgy", "Theurgy");
            this.add(TheurgyConstants.I18n.TOOLTIP_SHOW_EXTENDED, ChatFormatting.GOLD + "[" +
                            ChatFormatting.LIGHT_PURPLE + "shift" +
                            ChatFormatting.GRAY + "read more" +
                            ChatFormatting.GOLD + "]");
            this.add(TheurgyConstants.I18n.TOOLTIP_SHOW_USAGE, ChatFormatting.GOLD + "[" +
                            ChatFormatting.LIGHT_PURPLE + "shift-n" +
                            ChatFormatting.GRAY + "show usage" +
                            ChatFormatting.GOLD + "]");
        }

        private void addItems() {
            this.addItem(ItemRegistry.THEURGY, "Theurgy");
            this.addItem(ItemRegistry.TEST, "Test Item");
        }

        private void addTooltips() {
            this.addTooltip(ItemRegistry.THEURGY, "Dummy item for easy access to the Theurgy icon.");
            this.addTooltip(ItemRegistry.TEST,
                    "A test item to test tooltips.",
                    "It also has an extended tooltip.",
                    "And even usage info.");
        }

        private void addAdvancements() {
            this.advancementTitle("root", "Theurgy");
            this.advancementDescr("root", "The journey towards uncovering the mysteries of the universe lies ahead.");
        }

        private void addPatchouli() {
            //TODO: add Patchouli texts
        }
    }
}
