package com.github.klikli_dev.theurgy.common.crafting.recipe;

import com.github.klikli_dev.theurgy.common.theurgy.EssentiaCache;
import net.minecraft.item.ItemStack;

public class CrucibleItemStackFakeInventory extends ItemStackFakeInventory{
    public EssentiaCache essentiaCache;

    public CrucibleItemStackFakeInventory(ItemStack input, EssentiaCache essentiaCache) {
        super(input);
        this.essentiaCache = essentiaCache;
    }


}
