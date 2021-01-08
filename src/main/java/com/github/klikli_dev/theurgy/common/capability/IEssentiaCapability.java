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

package com.github.klikli_dev.theurgy.common.capability;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface IEssentiaCapability extends INBTSerializable<CompoundNBT> {
    //region Getter / Setter

    Map<Item, Integer> getEssentia();

    int getMaxEssentiaStored();

    /**
     * @return true if there is no essentia at all in this cache.
     */
    boolean isEmpty();
    //endregion Getter / Setter

    //region Overrides
    CompoundNBT serializeNBT();

    void deserializeNBT(CompoundNBT nbt);
    //endregion Overrides

    //region Methods
    /**
     * Receive the given amount of essentia.
     *
     * @param essentia the essentia type to receive.
     * @param amount   the amount to receive.
     * @param simulate true to only simulate the change.
     * @return the amount received.
     */
    int receiveEssentia(Item essentia, int amount, boolean simulate);

    /**
     * Get the current amount of the given essentia type.
     *
     * @param essentia the essentia type to look up.
     * @return the current amount.
     */
    int getEssentiaStored(Item essentia);

    /**
     * Remove the given amount of Essentia.
     *
     * @param essentia the essentia type to remove.
     * @param amount   the amount to remove.
     * @param simulate true to only simulate the change.
     * @return the amount removed.
     */
    int extractEssentia(Item essentia, int amount, boolean simulate);

    void onContentsChanged();

    boolean canExtract();

    boolean canReceive();
    //endregion Methods
}
