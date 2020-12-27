package com.github.klikli_dev.theurgy.common.theurgy;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;
//region Overrides
//endregion Overrides
//endregion Overrides
//region Methods
//endregion Methods
//endregion Methods

public class EssentiaCache implements INBTSerializable<CompoundNBT> {
    //region Fields
    public Map<Item, Integer> essentia = new HashMap<>();
    //endregion Fields

    //region Getter / Setter

    public boolean isEmpty() {
        return this.essentia.isEmpty();
    }

    /**
     * Sums amounts of all essentia types in the cache.
     *
     * @return sum of all essentia.
     */
    public int getEssentiaSum() {
        return this.essentia.values().stream().reduce(0, Integer::sum);
    }
    //endregion Getter / Setter

    //region Overrides
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
        this.essentia.clear();
        if (nbt.contains("essentia")) {
            ListNBT essentiaList = nbt.getList("essentia", 10);
            for (int i = 0; i < essentiaList.size(); i++) {
                CompoundNBT entry = essentiaList.getCompound(i);
                if (entry.contains("essentia") && entry.contains("amount")) {
                    this.add(ForgeRegistries.ITEMS.getValue(
                            new ResourceLocation(entry.getString("essentia"))).getItem(),
                            entry.getInt("amount"));
                }
            }
        }
    }

    //region Methods

    /**
     * Add the given amount of essentia.
     *
     * @param essentia the essentia type to add.
     * @param amount   the amount to add.
     */
    public void add(Item essentia, int amount) {
        if (this.essentia.containsKey(essentia)) {
            int currentAmount = this.essentia.get(essentia);
            amount += currentAmount;
        }
        this.essentia.put(essentia, amount);
    }

    public void clear(){
        this.essentia.clear();
    }

    /**
     * Get the current amount of the given essentia type.
     *
     * @param essentia the essentia type to look up.
     * @return the current amount.
     */
    public int get(Item essentia) {
        Integer amount = this.essentia.get(essentia);
        return amount == null ? 0 : amount;
    }

    /**
     * Remove the given amount of essentia, independent of remaining amount.
     * To *take* essentia, use {@link #take(Item, int)}
     *
     * @param essentia the essentia type to remove.
     * @param amount   the amount to remove.
     */
    public void remove(Item essentia, int amount) {
        int remaining = this.get(essentia) - amount;
        if (remaining <= 0) {
            this.essentia.remove(essentia);
        }
        else {
            this.essentia.put(essentia, remaining);
        }
    }

    /**
     * Remove the given amount from each essentia type, independent of remaining amount.
     * @param amount
     */
    public void removeAll(int amount){
        //copy entry set to safely iterate
        List<Item> essentia = new ArrayList<>(this.essentia.keySet());
        for (Item item : essentia) {
            this.remove(item, amount);
        }
    }

    /**
     * Remove the entire amount of the given type of essentia.
     *
     * @param essentia the essentia type to remove.
     */
    public void remove(Item essentia) {
        this.essentia.remove(essentia);
    }

    /**
     * Take the given amount of essentia from the essentia cache.
     *
     * @param essentia the essentia type to take.
     * @param amount   the amount to take.
     * @return False if not enough essentia is in the cache.
     */
    public boolean take(Item essentia, int amount) {
        int currentAmount = this.get(essentia);
        if (currentAmount >= amount) {
            int remaining = currentAmount - amount;
            this.essentia.put(essentia, remaining);
            return true;
        }
        return false;
    }
    //endregion Methods
}
