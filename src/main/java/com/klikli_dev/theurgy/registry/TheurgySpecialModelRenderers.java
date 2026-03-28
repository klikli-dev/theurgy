// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatItemRenderer;
import com.klikli_dev.theurgy.content.item.derivative.render.AlchemicalDerivativeRenderer;
import com.klikli_dev.theurgy.content.item.salt.render.AlchemicalSaltRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

public class TheurgySpecialModelRenderers {

    public static void onRegisterSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "digestion_vat"), DigestionVatItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "alchemical_derivative"), AlchemicalDerivativeRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "alchemical_salt"), AlchemicalSaltRenderer.Unbaked.MAP_CODEC);
    }
}
