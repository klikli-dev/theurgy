// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.gui.menu;

import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageClearMenu;

public interface IClearableMenu {

	default void sendClearPacket() {
		Networking.sendToServer(MessageClearMenu.INSTANCE);
	}

	public void clearContents();

}
