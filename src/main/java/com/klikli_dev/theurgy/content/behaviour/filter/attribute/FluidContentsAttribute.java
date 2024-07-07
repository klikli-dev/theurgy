// SPDX-FileCopyrightText: 2024 klikli-dev
// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter.attribute;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FluidContentsAttribute implements ItemAttribute {
    public static final FluidContentsAttribute EMPTY = new FluidContentsAttribute(null);

    private final Fluid fluid;

    public FluidContentsAttribute(@Nullable Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return this.extractFluids(itemStack).contains(this.fluid);
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack itemStack) {
        return this.extractFluids(itemStack).stream().map(FluidContentsAttribute::new).collect(Collectors.toList());
    }

    @Override
    public String getTranslationKey() {
        return "has_fluid";
    }

    @Override
    public Object[] getTranslationParameters() {
        String parameter = "";
        if (this.fluid != null)
            parameter = this.fluid.getFluidType().getDescription().getString();
        return new Object[]{parameter};
    }

    @Override
    public void writeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        if (this.fluid == null)
            return;
        ResourceLocation id = BuiltInRegistries.FLUID.getKey(this.fluid);

        if (id == null)
            return;
        nbt.putString("id", id.toString());
    }

    @Override
    public ItemAttribute readNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        return nbt.contains("id") ? new FluidContentsAttribute(BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(nbt.getString("id")))) : EMPTY;
    }

    private List<Fluid> extractFluids(ItemStack stack) {
        List<Fluid> fluids = new ArrayList<>();

        var capability =
                stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (capability != null) {
            for (int i = 0; i < capability.getTanks(); i++) {
                fluids.add(capability.getFluidInTank(i).getFluid());
            }
        }

        return fluids;
    }
}
