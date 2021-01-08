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

package com.github.klikli_dev.theurgy.common.tile;

import com.github.klikli_dev.theurgy.common.capability.IEssentiaCapability;
import com.github.klikli_dev.theurgy.common.capability.CreativeEssentiaCapability;
import com.github.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreativeEssentiaSourceTileEntity extends NetworkedTileEntity {

    //region Fields
    public final LazyOptional<IEssentiaCapability> essentiaCapabilityLazyOptional;
    public IEssentiaCapability essentiaCapability;
    //endregion Fields

    //region Initialization
    public CreativeEssentiaSourceTileEntity() {
        super(TileRegistry.CREATIVE_ESSENTIA_SOURCE.get());
        this.essentiaCapability = new CreativeEssentiaCapability();
        this.essentiaCapabilityLazyOptional = LazyOptional.of(() -> this.essentiaCapability);
    }
    //endregion Initialization

    //region Overrides
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction direction) {
        if (cap == CapabilityRegistry.ESSENTIA) {
            return this.essentiaCapabilityLazyOptional.cast();
        }
        return super.getCapability(cap, direction);
    }
    //endregion Overrides
}
