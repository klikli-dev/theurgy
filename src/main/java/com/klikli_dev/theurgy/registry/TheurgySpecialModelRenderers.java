// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.derivative.render.AlchemicalDerivativeRenderer;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.content.apparatus.distiller.render.DistillerItemRenderer;
import com.klikli_dev.theurgy.content.apparatus.calcinationoven.render.CalcinationOvenItemRenderer;
import com.klikli_dev.theurgy.content.apparatus.salammoniacaccumulator.render.SalAmmoniacAccumulatorItemRenderer;
import com.klikli_dev.theurgy.content.apparatus.salammoniactank.render.SalAmmoniacTankItemRenderer;
import com.klikli_dev.theurgy.content.apparatus.digestionvat.DigestionVatItemRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorMercuryVesselItemRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorSaltVesselItemRenderer;
import com.klikli_dev.theurgy.content.apparatus.incubator.render.IncubatorSulfurVesselItemRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

public class TheurgySpecialModelRenderers {

    public static void onRegisterSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "distiller"), DistillerItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "calcination_oven"), CalcinationOvenItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "digestion_vat"), DigestionVatItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "sal_ammoniac_accumulator"), SalAmmoniacAccumulatorItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "sal_ammoniac_tank"), SalAmmoniacTankItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "incubator_mercury_vessel"), IncubatorMercuryVesselItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "incubator_salt_vessel"), IncubatorSaltVesselItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "incubator_sulfur_vessel"), IncubatorSulfurVesselItemRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(Theurgy.MODID, "alchemical_derivative"), AlchemicalDerivativeRenderer.Unbaked.MAP_CODEC);
    }
}
