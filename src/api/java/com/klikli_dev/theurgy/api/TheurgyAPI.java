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

package com.klikli_dev.theurgy.api;

import com.klikli_dev.theurgy.api.stub.TheurgyAPIStub;
import net.minecraftforge.common.util.Lazy;
import org.apache.logging.log4j.LogManager;

public class TheurgyAPI {
    public static final String ID = "theurgy";
    public static final String Name = "Theurgy";

    private static final Lazy<ITheurgyAPI> lazyInstance = Lazy.concurrentOf(() -> {
        try {
            return (ITheurgyAPI) Class.forName("com.klikli_dev.theurgy.apiimpl.TheurgyAPIImpl").getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find PatchouliAPIImpl, using a dummy");
            return TheurgyAPIStub.get();
        }
    });

    public static ITheurgyAPI get() {
        return lazyInstance.get();
    }

}
