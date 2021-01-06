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
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 *
 */
public class EssentiaCache implements INBTSerializable<CompoundNBT>, IEssentiaCapability {
    //region Fields
    public Map<Item, Integer> essentia;
    private int capacity;
    //endregion Fields

    //region Getter / Setter

    public EssentiaCache(){
        this.essentia = new HashMap<>();
        this.capacity = Integer.MAX_VALUE;
    }

    public EssentiaCache(int capacity){
        this();
        this.capacity = capacity;
    }

    /**
     * @return true if there is no essentia at all in this cache.
     */
    @Override
    public boolean isEmpty() {
        return this.getEssentiaSum() <= 0;
    }

    /**
     * Sums amounts of all essentia types in the cache.
     *
     * @return sum of all essentia.
     */
    public int getEssentiaSum() {
        return this.essentia.values().stream().reduce(0, Integer::sum);
    }

    /**
     * Gets the capacity for each essentia type in this cache.
     * @return the capacity.
     */
    @Override
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Sets the capacity for each essentia type in this cache.
     * @param capacity the capacity.
     */
    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public Map<Item, Integer> getEssentia() {
        return this.essentia;
    }

    @Override
    public Set<Item> getEssentiaTypes() {
        return this.essentia.keySet();
    }
    //endregion Getter / Setter

    //region Overrides
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("capacity", this.capacity);
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
        this.capacity = nbt.getInt("capacity");
        if (nbt.contains("essentia")) {
            ListNBT essentiaList = nbt.getList("essentia", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < essentiaList.size(); i++) {
                CompoundNBT entry = essentiaList.getCompound(i);
                if (entry.contains("essentia") && entry.contains("amount")) {
                    this.set(ForgeRegistries.ITEMS.getValue(
                            new ResourceLocation(entry.getString("essentia"))).getItem(),
                            entry.getInt("amount"));
                }
            }
        }
    }

    @Override
    public boolean hasCapacity(Item essentia) {
        return this.capacity >  this.get(essentia);
    }

    //region Methods

    /**
     * Add the given amount of essentia.
     *
     * @param essentia the essentia type to add.
     * @param amount   the amount to add.
     * @param simulate true to only simulate the change.
     * @return the amount added.
     */
    @Override
    public int add(Item essentia, int amount, boolean simulate) {
        int currentAmount = this.get(essentia);
        int added = Math.min(this.capacity - currentAmount, amount);
        int newAmount = currentAmount + added;
        if(!simulate){
            this.essentia.put(essentia, newAmount);
            if(newAmount != currentAmount){
                this.onContentsChanged();
            }
        }
        return added;
    }

    /**
     * Resets all essentia in the cache to 0.
     */
    @Override
    public void clear() {
        this.essentia.forEach((item, amount) -> this.essentia.put(item, 0));
    }

    /**
     * Get the current amount of the given essentia type.
     *
     * @param essentia the essentia type to look up.
     * @return the current amount.
     */
    @Override
    public int get(Item essentia) {
        Integer amount = this.essentia.get(essentia);
        return amount == null ? 0 : amount;
    }

    /**
     * Sets the amount of the given essentia.
     * @param essentia
     * @param value
     */
    @Override
    public void set(Item essentia, int value) {
        this.essentia.put(essentia, value);
    }

    /**
     * Gets the minimum amount that is availalbe for all of the given essentia types.
     *
     * @param essentia the essentia to check.
     * @return the minimum amount of essentia available.
     */
    @Override
    public int min(Item... essentia) {
        return Arrays.stream(essentia).mapToInt(e -> this.essentia.getOrDefault(e, 0)).min().orElse(0);
    }

    /**
     * Remove the given amount of Essentia.
     *
     * @param essentia the essentia type to remove.
     * @param amount   the amount to remove.
     * @param simulate true to only simulate the change.
     * @return the amount removed.
     */
    @Override
    public int remove(Item essentia, int amount, boolean simulate) {
        int currentAmount = this.get(essentia);
        int removed = Math.min(currentAmount, amount);
        int newAmount = currentAmount - removed;
        if(!simulate){
            this.essentia.put(essentia, newAmount);
            if(newAmount != currentAmount){
                this.onContentsChanged();
            }
        }
        return removed;
    }

    @Override
    public void onContentsChanged() {

    }
    //endregion Methods
}
