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
import com.klikli_dev.theurgy.TheurgyAPI;
import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public abstract class LangGenerator extends LanguageProvider {
    public LangGenerator(DataGenerator generator, String locale) {
        super(generator, Theurgy.MODID, locale);
    }

    protected String itemKey(String id){
        return "item." + Theurgy.MODID + "." + id;
    }

    protected void addTooltip(Item item, String tooltip) {
        this.add(item.getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_SUFFIX, tooltip);
    }

    protected void addTooltip(Item item, String tooltip, String extendedTooltip) {
        this.addTooltip(item, tooltip);
        this.add(item.getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_EXTENDED_SUFFIX, extendedTooltip);
    }

    protected void addTooltip(Item item, String tooltip, String extendedTooltip, String usageTooltip) {
        this.addTooltip(item, tooltip, extendedTooltip);
        this.add(item.getDescriptionId() + TheurgyConstants.I18n.TOOLTIP_USAGE_SUFFIX, usageTooltip);
    }

    public static final class English extends LangGenerator {

        public English(DataGenerator generator) {
            super(generator, "en_us");
        }

        protected void addTranslations() {
            this.addMisc();
            this.addTooltips();
            this.addPatchouli();
        }

        private void addMisc(){
            this.add("itemGroup.theurgy", "Theurgy");
            this.add(TheurgyConstants.I18n.TOOLTIP_SHOW_EXTENDED,
                    ChatFormatting.GOLD + "[" +
                            ChatFormatting.LIGHT_PURPLE + "shift" +
                            ChatFormatting.GRAY + "read more" +
                            ChatFormatting.GOLD + "]");
            this.add(TheurgyConstants.I18n.TOOLTIP_SHOW_USAGE,
                    ChatFormatting.GOLD + "[" +
                            ChatFormatting.LIGHT_PURPLE + "shift-n" +
                            ChatFormatting.GRAY + "show usage" +
                            ChatFormatting.GOLD + "]");
        }

        private void addTooltips(){

        }

        private void addPatchouli() {
            //TODO: add Patchouli texts
        }
    }
}
