// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.JadeUI;
import snownee.jade.api.view.*;
import snownee.jade.util.ClientProxy;

import java.util.List;
import java.util.Objects;

public class MercuryFluxEnergyClientProvider<T extends Accessor<?>> extends MercuryFluxEnergyProvider<T> implements IComponentProvider<T> {
    public static final MercuryFluxEnergyClientProvider<BlockAccessor> BLOCK = new MercuryFluxEnergyClientProvider<>();
    private static final Element PROGRESS_OVERLAY = JadeUI.horizontalTiledSprite(RenderPipelines.GUI_TEXTURED, JadeIds.JADE("energy_progress"), 16, 16);

    public static void register(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BLOCK, Block.class);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, T accessor, IPluginConfig config) {
        if (!accessor.showDetails() && config.get(JadeIds.UNIVERSAL_ENERGY_STORAGE_DETAILED)) {
            return;
        }

        List<ClientViewGroup<EnergyView>> groups = ClientProxy.mapToClientGroups(
                accessor,
                ID,
                STREAM_CODEC,
                uid -> Objects.equals(uid, Extension.INSTANCE.getUid()) ? Extension.INSTANCE : null,
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

    public static class Extension implements IClientExtensionProvider<EnergyView.Data, EnergyView> {
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
    }
}
