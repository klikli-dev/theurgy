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

package com.github.klikli_dev.theurgy.common.entity;

import com.github.klikli_dev.theurgy.registry.EntityRegistry;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EndInfusedCrystalSpearEntity extends SpearEntity {

    //region Initialization
    public EndInfusedCrystalSpearEntity(
            EntityType<? extends EndInfusedCrystalSpearEntity> type, World worldIn) {
        super(type, worldIn, new ItemStack(ItemRegistry.END_INFUSED_CRYSTAL_SPEAR.get()));
    }

    public EndInfusedCrystalSpearEntity(World worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        super(EntityRegistry.END_INFUSED_CRYSTAL_SPEAR_TYPE.get(), worldIn, thrower, thrownStackIn);
    }

    public EndInfusedCrystalSpearEntity(World worldIn, double x, double y, double z) {
        super(EntityRegistry.END_INFUSED_CRYSTAL_SPEAR_TYPE.get(), worldIn, x, y, z,
                new ItemStack(ItemRegistry.END_INFUSED_CRYSTAL_SPEAR.get()));
    }
    //endregion Initialization
}
