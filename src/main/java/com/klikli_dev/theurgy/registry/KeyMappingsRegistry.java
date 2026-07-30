// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetMode;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyMappingsRegistry {
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Theurgy.loc("keybindings"));

    public static final KeyMapping CHANGE_ITEM_MODE =
            new KeyMapping(TheurgyConstants.I18n.Key.CHANGE_ITEM_MODE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
    public static final KeyMapping SHOW_HELD_STACK_FIT_OUTLINE =
            new KeyMapping(TheurgyConstants.I18n.Key.SHOW_HELD_STACK_FIT_OUTLINE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.registerCategory(CATEGORY);
        event.register(CHANGE_ITEM_MODE);
        event.register(SHOW_HELD_STACK_FIT_OUTLINE);
    }

    public static void onKeyInput(InputEvent.Key event) {
        onInput(event);
    }

    public static void onMouseInput(InputEvent.MouseButton.Key event) {
        onInput(event);
    }

    private static void onInput(InputEvent event) {

        if (CHANGE_ITEM_MODE.consumeClick()) {
            Networking.sendToServer(new MessageSetMode(1));
        }
    }

    public static boolean isHeldStackFitOutlineKeyActive() {
        // Intentionally treat an unbound key as always active so the config alone can enable rendering.
        return SHOW_HELD_STACK_FIT_OUTLINE.isUnbound() || SHOW_HELD_STACK_FIT_OUTLINE.isDown();
    }

}
