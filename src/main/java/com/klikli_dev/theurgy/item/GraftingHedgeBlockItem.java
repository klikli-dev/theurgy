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

package com.klikli_dev.theurgy.item;

import com.klikli_dev.theurgy.api.TheurgyConstants;
import com.klikli_dev.theurgy.api.tooltips.IAdditionalTooltipDataProvider;
import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeManager;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class GraftingHedgeBlockItem extends BlockItem implements IAdditionalTooltipDataProvider {
    public GraftingHedgeBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public TranslatableComponent[] getAdditionalTooltipData(ItemStack pStack) {

        //allow access to name of item to grow, if grafted
        CompoundTag blockEntityTag = pStack.getTagElement("BlockEntityTag");
        if (blockEntityTag != null && blockEntityTag.contains(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA)) {
            var loc = new ResourceLocation(blockEntityTag.getString(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA));
            var data = GraftingHedgeManager.get().byKey(loc);
            if(data.isPresent()){
                return new TranslatableComponent[]{new TranslatableComponent(data.get().itemToGrow.getDescriptionId())};
            }
        }

        return new TranslatableComponent[0];
    }

    @Override
    public Component getName(ItemStack pStack) {

        //allow access to name of item to grow, if grafted
        CompoundTag blockEntityTag = pStack.getTagElement("BlockEntityTag");
        if (blockEntityTag != null && blockEntityTag.contains(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA)) {
            var loc = new ResourceLocation(blockEntityTag.getString(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA));
            var data = GraftingHedgeManager.get().byKey(loc);
            if(data.isPresent()){
                return new TranslatableComponent(this.getDescriptionId(pStack),
                        new TranslatableComponent(data.get().itemToGrow.getDescriptionId()));
            }
        }
        return new TranslatableComponent(this.getDescriptionId(pStack));
    }

    @Override
    public String getDescriptionId(ItemStack pStack) {
        //if the hedge is already grafted, return with suffix
        CompoundTag blockEntityTag = pStack.getTagElement("BlockEntityTag");
        if (blockEntityTag != null && blockEntityTag.contains(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA)) {
             return super.getDescriptionId(pStack) + TheurgyConstants.I18n.GRAFTED_SUFFIX;
        }
        return super.getDescriptionId(pStack);
    }

    @Override
    public void fillItemCategory(CreativeModeTab pGroup, NonNullList<ItemStack> pItems) {
        //add the original block
        super.fillItemCategory(pGroup, pItems);

        if (this.allowdedIn(pGroup)) {
            //add grafted versions
            if(GraftingHedgeManager.get().isLoaded()){
                var data = GraftingHedgeManager.get().getGraftingHedgeData();
                for(var entry : data.entrySet()){
                    ItemStack stack = new ItemStack(this);
                    stack.getOrCreateTagElement("BlockEntityTag")
                            .putString(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA, entry.getValue().id.toString());
                    pItems.add(stack);
                }
            }
        }
    }
}
