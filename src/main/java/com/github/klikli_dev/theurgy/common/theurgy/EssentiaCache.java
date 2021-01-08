/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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

package com.github.klikli_dev.theurgy.common.theurgy;

import com.github.klikli_dev.theurgy.common.capability.IEssentiaCapability;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class EssentiaCache implements INBTSerializable<CompoundNBT>, IEssentiaCapability {
    //region Fields
    public Map<Item, Integer> essentia;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    //endregion Fields

    //region Initialization
    public EssentiaCache() {
        this(Integer.MAX_VALUE);

        this.capacity = Integer.MAX_VALUE;
    }

    public EssentiaCache(int capacity) {
        this(capacity, capacity);
    }

    public EssentiaCache(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public EssentiaCache(int capacity, int maxReceive, int maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.essentia = new HashMap<>();
    }
    //endregion Initialization

    //region Getter / Setter
    //endregion Getter / Setter

    //region Overrides
    @Override
    public Map<Item, Integer> getEssentia() {
        return this.essentia;
    }

    @Override
    public int getMaxEssentiaStored() {
        return this.capacity;
    }

    @Override
    public boolean isEmpty() {
        for (int value : this.essentia.values()) {
            if (value > 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public int receiveEssentia(Item essentia, int amount, boolean simulate) {
        int currentAmount = this.getEssentiaStored(essentia);
        int added = Math.min(this.capacity - currentAmount, amount);
        int newAmount = currentAmount + added;
        if (!simulate) {
            this.essentia.put(essentia, newAmount);
            if (newAmount != currentAmount) {
                this.onContentsChanged();
            }
        }
        return added;
    }


    @Override
    public int getEssentiaStored(Item essentia) {
        Integer amount = this.essentia.get(essentia);
        return amount == null ? 0 : amount;
    }

    @Override
    public int extractEssentia(Item essentia, int amount, boolean simulate) {
        int currentAmount = this.getEssentiaStored(essentia);
        int removed = Math.min(currentAmount, amount);
        int newAmount = currentAmount - removed;
        if (!simulate) {
            this.essentia.put(essentia, newAmount);
            if (newAmount != currentAmount) {
                this.onContentsChanged();
            }
        }
        return removed;
    }

    @Override
    public void onContentsChanged() {

    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        ListNBT essentiaList = new ListNBT();
        compound.put("essentia", essentiaList);
        this.essentia.forEach((esssentia, amount) -> {
            CompoundNBT entry = new CompoundNBT();
            entry.putString("essentia", esssentia.getRegistryName().toString());
            entry.putInt("amount", amount);
            essentiaList.add(entry);
        });
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.clear();
        if (nbt.contains("essentia")) {
            ListNBT essentiaList = nbt.getList("essentia", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < essentiaList.size(); i++) {
                CompoundNBT entry = essentiaList.getCompound(i);
                if (entry.contains("essentia") && entry.contains("amount")) {
                    this.essentia.put(ForgeRegistries.ITEMS.getValue(
                            new ResourceLocation(entry.getString("essentia"))).getItem(),
                            entry.getInt("amount"));
                }
            }
        }
    }

    //region Methods

    /**
     * Resets all essentia in the cache to 0.
     */
    public void clear() {
        this.essentia.forEach((item, amount) -> this.essentia.put(item, 0));
    }

    //endregion Methods
}
