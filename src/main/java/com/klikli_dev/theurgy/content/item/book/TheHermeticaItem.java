// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.book;

import com.klikli_dev.modonomicon.item.ModonomiconCustomItemBase;
import com.klikli_dev.theurgy.Theurgy;
import net.minecraft.resources.Identifier;

public class TheHermeticaItem extends ModonomiconCustomItemBase {
    public static final Identifier THE_HERMETICA = Theurgy.loc("the_hermetica");

    public TheHermeticaItem(Properties properties) {
        super(THE_HERMETICA, properties);
    }
}
