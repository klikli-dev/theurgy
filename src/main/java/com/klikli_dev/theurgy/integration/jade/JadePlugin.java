// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jade;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.EnergyView;
import net.minecraft.nbt.CompoundTag;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    @SuppressWarnings("unchecked")
    public void register(IWailaCommonRegistration registration) {
        registration.registerEnergyStorage((IServerExtensionProvider) (Object) MercuryFluxEnergyProvider.get(), BlockEntity.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(MercuryFluxEnergyProvider.Client.INSTANCE, Block.class);
        registration.registerEnergyStorageClient((IClientExtensionProvider) (Object) MercuryFluxEnergyProvider.get());
    }
}
