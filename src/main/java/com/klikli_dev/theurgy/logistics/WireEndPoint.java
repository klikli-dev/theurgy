package com.klikli_dev.theurgy.logistics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record WireEndPoint(BlockPos pos, ResourceKey<Level> level) {

    public static WireEndPoint load(CompoundTag tag) {
        return new WireEndPoint(BlockPos.of(tag.getLong("pos")), ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("level"))));
    }

    public static WireEndPoint load(ItemStack stack) {
        if (!stack.hasTag() || !stack.getOrCreateTag().contains("wirePoint"))
            return null;
        return load(stack.getOrCreateTag().getCompound("wirePoint"));
    }

    public void save(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("wirePoint", this.save(new CompoundTag()));
    }

    public CompoundTag save(CompoundTag tag) {
        tag.putLong("pos", this.pos.asLong());
        tag.putString("level", this.level.location().toString());
        return tag;
    }
}
