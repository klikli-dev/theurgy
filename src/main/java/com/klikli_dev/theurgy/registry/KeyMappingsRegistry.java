package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.ModeItem;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetMode;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyMappingsRegistry {
    public static final KeyMapping CHANGE_ITEM_MODE =
            new KeyMapping(TheurgyConstants.I18n.Key.CHANGE_ITEM_MODE, new IKeyConflictContext() {
                @Override
                public boolean isActive() {
                    return Minecraft.getInstance().screen == null && Minecraft.getInstance().player.getMainHandItem().getItem() instanceof ModeItem;
                }

                @Override
                public boolean conflicts(IKeyConflictContext other) {
                    return this == other;
                }
            }, InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN), "key.theurgy.category");

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CHANGE_ITEM_MODE);
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

}
