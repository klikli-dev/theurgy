/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class SolventFluidType extends FluidType {
    private final ResourceLocation still;
    private final ResourceLocation flowing;
    private final ResourceLocation overlay;
    private final int tint;

    /**
     * Default constructor.
     *
     * @param properties the general properties of the fluid type
     */
    public SolventFluidType(Properties properties, ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay, int tint) {
        super(properties);
        this.still = still;
        this.flowing = flowing;
        this.overlay = overlay;
        this.tint = tint;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return SolventFluidType.this.still;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return SolventFluidType.this.flowing;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return SolventFluidType.this.overlay;
            }

            @Override
            public int getTintColor() {
                return SolventFluidType.this.tint;
            }
        });
    }
}
