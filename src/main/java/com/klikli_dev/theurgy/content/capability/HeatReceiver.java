// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.capability;

public interface HeatReceiver {
    void setHotUntil(long gameTime);

    long getIsHotUntil();

    boolean readyToReceive();
}
