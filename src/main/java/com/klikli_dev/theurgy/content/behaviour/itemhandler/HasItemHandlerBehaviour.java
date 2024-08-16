// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.itemhandler;

public interface HasItemHandlerBehaviour<T extends ItemHandlerBehaviour> {
    T itemHandlerBehaviour();
}
