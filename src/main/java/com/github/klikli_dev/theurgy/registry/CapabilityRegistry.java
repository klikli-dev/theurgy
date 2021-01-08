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

package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.capability.*;
import com.github.klikli_dev.theurgy.common.capability.DefaultEssentiaCapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CapabilityRegistry {
    //region Fields
    public static final ResourceLocation ESSENTIA_ID = new ResourceLocation(Theurgy.MODID, "essentia");
    public static final ResourceLocation AETHER_ID = new ResourceLocation(Theurgy.MODID, "aether");

    @CapabilityInject(IEssentiaCapability.class)
    public static Capability<IEssentiaCapability> ESSENTIA;


    @CapabilityInject(IAetherCapability.class)
    public static Capability<IAetherCapability> AETHER;
    //endregion Fields

    //region Static Methods
    public static void commonSetup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE
                .register(IEssentiaCapability.class, new EssentiaCapabilityStorage(), DefaultEssentiaCapability::new);
        CapabilityManager.INSTANCE
                .register(IAetherCapability.class, new AetherCapabilityStorage(),
                        () -> new DefaultAetherCapability(1000));
    }
    //endregion Static Methods
}
