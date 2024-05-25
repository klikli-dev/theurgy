// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mode;

import net.neoforged.neoforge.client.event.RenderHighlightEvent;

public class ItemModeRenderHandler<T extends ItemMode> {

    protected final T mode;
    public ItemModeRenderHandler(T mode){
        this.mode = mode;
    }


    public void renderBlockHighlight(RenderHighlightEvent.Block event) {

    }
}
