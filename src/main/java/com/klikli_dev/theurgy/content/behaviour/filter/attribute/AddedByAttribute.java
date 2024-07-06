// SPDX-FileCopyrightText: 2024 klikli-dev
// SPDX-FileCopyrightText: 2019 simibubi
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.behaviour.filter.attribute;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AddedByAttribute implements ItemAttribute {

    private final String modId;

    public AddedByAttribute(String modId) {
        this.modId = modId;
    }

    @Override
    public boolean appliesTo(ItemStack stack) {
        return this.modId.equals(stack.getItem()
                .getCreatorModId(stack));
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack stack) {
        String id = stack.getItem()
                .getCreatorModId(stack);
        return id == null ? Collections.emptyList() : List.of(new AddedByAttribute(id));
    }

    @Override
    public String getTranslationKey() {
        return "added_by";
    }

    @Override
    public Object[] getTranslationParameters() {
        Optional<? extends ModContainer> modContainerById = ModList.get()
                .getModContainerById(this.modId);
        String name = modContainerById.map(ModContainer::getModInfo)
                .map(IModInfo::getDisplayName)
                .orElse(StringUtils.capitalize(this.modId));
        return new Object[]{name};
    }

    @Override
    public void writeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
        nbt.putString("id", this.modId);
    }

    @Override
    public ItemAttribute readNBT(HolderLookup.Provider pRegistries,CompoundTag nbt) {
        return new AddedByAttribute(nbt.getString("id"));
    }

}