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
import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeData;
import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeManager;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class GraftingHedgeBlockEntity extends NetworkedBlockEntity {

    private Optional<GraftingHedgeData> data = Optional.empty();

    public GraftingHedgeBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockEntityRegistry.GRAFTING_HEDGE.get(), pWorldPosition, pBlockState);
    }

    public Optional<GraftingHedgeData> getGraftingHedgeData() {
        return this.data;
    }

    public boolean hasGraftedFruit() {
        return this.data.isPresent();
    }

    public Optional<ItemStack> getFruitToGrow() {
        return this.data.map(d -> d.itemToGrow);
    }

    public void setData(GraftingHedgeData data) {
        this.data = Optional.of(data);
    }

    @Override
    public void loadNetwork(CompoundTag compound) {
        if (compound.contains(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA))
            GraftingHedgeManager.get().byKey(new ResourceLocation(compound.getString(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA)))
                    .ifPresent(data -> this.data = Optional.of(data));
        super.loadNetwork(compound);
    }

    @Override
    public CompoundTag saveNetwork(CompoundTag compound) {
        this.data.ifPresent(data -> compound.putString(TheurgyConstants.Nbt.GRAFTING_HEDGE_DATA, data.id.toString()));
        return super.saveNetwork(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        return super.save(compound);
    }
}
