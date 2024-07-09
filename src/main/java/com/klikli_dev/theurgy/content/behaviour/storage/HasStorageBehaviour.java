// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.storage;

public interface HasStorageBehaviour<T extends StorageBehaviour<?>> {
    StorageBehaviour<T> storageBehaviour();
}
