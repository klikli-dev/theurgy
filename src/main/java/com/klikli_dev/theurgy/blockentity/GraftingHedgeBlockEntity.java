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

package com.klikli_dev.theurgy.blockentity;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class GraftingHedgeBlockEntity extends NetworkedBlockEntity {

    private ItemStack fruitToGrow = ItemStack.EMPTY;

    public GraftingHedgeBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockEntityRegistry.GRAFTING_HEDGE.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        return super.save(compound);
    }

    @Override
    public void loadNetwork(CompoundTag compound) {
        if(compound.contains(TheurgyConstants.Nbt.FRUIT_TO_GROW))
            this.fruitToGrow = ItemStack.of(compound.getCompound(TheurgyConstants.Nbt.FRUIT_TO_GROW));
        super.loadNetwork(compound);
    }

    @Override
    public CompoundTag saveNetwork(CompoundTag compound) {
        if(!this.fruitToGrow.isEmpty())
            compound.put(TheurgyConstants.Nbt.FRUIT_TO_GROW, this.fruitToGrow.save(new CompoundTag()));
        return super.saveNetwork(compound);
    }

    public ItemStack getFruitToGrow() {
        return this.fruitToGrow;
    }

    public void setFruitToGrow(ItemStack fruitToGrow) {
        this.fruitToGrow = fruitToGrow;
        this.setNetworkChanged();
    }
}
