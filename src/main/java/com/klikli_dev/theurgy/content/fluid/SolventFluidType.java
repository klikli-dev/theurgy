// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.fluid;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.fluids.FluidType;

public class SolventFluidType extends FluidType {
    public final Identifier still;
    public final Identifier flowing;
    public final Identifier overlay;
    public final int tint;

    /**
     * Default constructor.
     *
     * @param properties the general properties of the fluid type
     */
    public SolventFluidType(Properties properties, Identifier still, Identifier flowing, Identifier overlay, int tint) {
        super(properties);
        this.still = still;
        this.flowing = flowing;
        this.overlay = overlay;
        this.tint = tint;
    }
}
