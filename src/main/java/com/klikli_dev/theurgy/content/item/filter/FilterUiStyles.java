// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.filter;

import com.klikli_dev.codedefinedgui.api.style.GuiStyleKey;
import com.klikli_dev.theurgy.Theurgy;

public final class FilterUiStyles {
    public static final GuiStyleKey THEURGY_LIST = GuiStyleKey.of(Theurgy.loc("filter/list"));
    public static final GuiStyleKey THEURGY_ATTRIBUTE = GuiStyleKey.of(Theurgy.loc("filter/attribute"));

    private FilterUiStyles() {
    }
}
