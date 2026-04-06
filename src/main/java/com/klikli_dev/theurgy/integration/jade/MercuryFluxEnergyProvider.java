// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jade;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.JadeIds;
import snownee.jade.api.StreamServerDataProvider;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.EnergyView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ProgressView;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.util.ClientProxy;
import snownee.jade.util.CommonProxy;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

public class MercuryFluxEnergyProvider<T extends Accessor<?>> implements StreamServerDataProvider<T, Entry<Identifier, List<ViewGroup<EnergyView.Data>>>> {
    public static final Identifier ID = Theurgy.loc("mercury_flux");
    private static final StreamCodec<RegistryFriendlyByteBuf, Entry<Identifier, List<ViewGroup<EnergyView.Data>>>> STREAM_CODEC = ViewGroup.listCodec(EnergyView.Data.STREAM_CODEC)
            .cast();
    public static final MercuryFluxEnergyProvider<BlockAccessor> BLOCK = new MercuryFluxEnergyProvider<>();

    public static @Nullable List<ViewGroup<EnergyView.Data>> wrapMercuryFluxStorage(Accessor<?> accessor) {
        if (!(accessor instanceof BlockAccessor)) {
            return null;
        }

        var storage = CommonProxy.getDefaultStorage(accessor, CapabilityRegistry.MERCURY_FLUX_HANDLER, null);
        if (storage == null || storage.getMaxEnergyStored() <= 0) {
            return null;
        }

        ViewGroup<EnergyView.Data> group = new ViewGroup<>(List.of(new EnergyView.Data(storage.getEnergyStored(), storage.getMaxEnergyStored())));
        group.getExtraData().putString("Unit", "MF");
        return List.of(group);
    }

    public static boolean hasDefaultMercuryFluxStorage(Accessor<?> accessor) {
        return accessor instanceof BlockAccessor && CommonProxy.hasDefaultStorage(accessor, CapabilityRegistry.MERCURY_FLUX_HANDLER, null);
    }

    @Override
    public @Nullable Entry<Identifier, List<ViewGroup<EnergyView.Data>>> streamData(T accessor) {
        return CommonProxy.getServerExtensionData(accessor, WailaCommonRegistration.instance().energyStorageProviders);
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, Entry<Identifier, List<ViewGroup<EnergyView.Data>>>> streamCodec() {
        return STREAM_CODEC;
    }

    @Override
    public Identifier getUid() {
        return ID;
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.BODY + 1000;
    }

    @Override
    public boolean shouldRequestData(T accessor) {
        return !accessor.showDetails() && IWailaConfig.get().plugin().get(JadeIds.UNIVERSAL_ENERGY_STORAGE_DETAILED)
                ? false
                : WailaCommonRegistration.instance().energyStorageProviders.hitsAny(accessor, IServerExtensionProvider::shouldRequestData);
    }

    public static class Client<T extends Accessor<?>> extends MercuryFluxEnergyProvider<T> implements IComponentProvider<T> {
        public static final Client<BlockAccessor> BLOCK = new Client<>();
        private static final Element PROGRESS_OVERLAY = JadeUI.horizontalTiledSprite(RenderPipelines.GUI_TEXTURED, JadeIds.JADE("energy_progress"), 16, 16);

        @Override
        public void appendTooltip(ITooltip tooltip, T accessor, IPluginConfig config) {
            if (!accessor.showDetails() && config.get(JadeIds.UNIVERSAL_ENERGY_STORAGE_DETAILED)) {
                return;
            }

            List<ClientViewGroup<EnergyView>> groups = ClientProxy.mapToClientGroups(
                    accessor,
                    ID,
                    STREAM_CODEC,
                    WailaClientRegistration.instance().energyStorageProviders::get,
                    tooltip
            );
            if (groups == null || groups.isEmpty()) {
                return;
            }

            boolean renderGroup = groups.size() > 1 || groups.getFirst().shouldRenderGroup();
            ClientViewGroup.tooltip(tooltip, groups, renderGroup, (theTooltip, group) -> {
                if (renderGroup) {
                    group.renderHeader(theTooltip);
                }

                for (EnergyView view : group.views) {
                    IWailaConfig.HandlerDisplayStyle style = config.getEnum(JadeIds.UNIVERSAL_ENERGY_STORAGE_STYLE);
                    Component text;
                    if (view.overrideText != null) {
                        text = view.overrideText;
                    } else {
                        String current = view.current;
                        if (style == IWailaConfig.HandlerDisplayStyle.PROGRESS_BAR) {
                            current = ChatFormatting.WHITE + current;
                        }
                        text = Component.translatable("jade.fe", current, view.max);
                    }

                    switch (style) {
                        case PLAIN_TEXT -> theTooltip.add(Component.translatable("jade.energy.text", text));
                        case ICON -> {
                            theTooltip.add(JadeUI.sprite(JadeIds.JADE("energy"), 10, 10).size(10, 9).offset(0, -1));
                            theTooltip.append(text);
                        }
                        case PROGRESS_BAR -> {
                            ProgressView progressView = new ProgressView(
                                    ProgressView.Part.of(view.ratio, PROGRESS_OVERLAY),
                                    text,
                                    JadeUI.progressStyle(),
                                    BoxStyle.nestedBox());
                            theTooltip.add(JadeUI.progress(progressView));
                        }
                    }
                }
            });
        }
    }

    public static class Extension implements IServerExtensionProvider<EnergyView.Data>, IClientExtensionProvider<EnergyView.Data, EnergyView> {
        public static final Extension INSTANCE = new Extension();

        @Override
        public Identifier getUid() {
            return ID;
        }

        @Override
        public List<ClientViewGroup<EnergyView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<EnergyView.Data>> groups) {
            return groups.stream().map($ -> {
                String unit = $.getExtraData().getStringOr("Unit", "MF");
                return new ClientViewGroup<>($.views.stream().map(data -> EnergyView.read(data, unit)).filter(Objects::nonNull).toList());
            }).toList();
        }

        @Override
        public @Nullable List<ViewGroup<EnergyView.Data>> getGroups(Accessor<?> accessor) {
            return wrapMercuryFluxStorage(accessor);
        }

        @Override
        public boolean shouldRequestData(Accessor<?> accessor) {
            return hasDefaultMercuryFluxStorage(accessor);
        }

        @Override
        public int getDefaultPriority() {
            return 9999;
        }
    }
}
