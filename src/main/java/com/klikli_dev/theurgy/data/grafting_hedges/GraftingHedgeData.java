package com.klikli_dev.theurgy.data.grafting_hedges;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

import javax.annotation.Nullable;

public class GraftingHedgeData {
    public ResourceLocation id;
    public Ingredient itemToGraft;
    public ItemStack itemToGrow;
    @Nullable
    public ICondition condition;

    public GraftingHedgeData(ResourceLocation id, Ingredient itemToGraft, ItemStack itemToGrow, ICondition condition) {
        this.id = id;
        this.itemToGraft = itemToGraft;
        this.itemToGrow = itemToGrow;
        this.condition = condition;
    }

    public static GraftingHedgeData fromJson(ResourceLocation id, JsonObject json) {
        Ingredient itemToGraft = Ingredient.fromJson(
                GsonHelper.isArrayNode(json, "item_to_graft") ?
                        GsonHelper.getAsJsonArray(json, "item_to_graft") :
                        GsonHelper.getAsJsonObject(json, "item_to_graft"));
        ItemStack itemToGrow = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "item_to_grow"), true);
        ICondition condition = json.has("condition") ? CraftingHelper.getCondition(GsonHelper.getAsJsonObject(json, "condition")) : null;
        return new GraftingHedgeData(id, itemToGraft, itemToGrow, condition);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("item_to_graft", this.itemToGraft.toJson());
        //codec is ok here as capabilities are highly unlikely on a grown fruit
        json.add("item_to_grow", ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, this.itemToGrow).get().orThrow());
        if (this.condition != null) {
            json.add("condition", CraftingHelper.serialize(this.condition));
        }
        return json;
    }

    public static GraftingHedgeData fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient itemToGraft = Ingredient.fromNetwork(buffer);
        ItemStack itemToGrow = buffer.readItem();
        return new GraftingHedgeData(id, itemToGraft, itemToGrow, null);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        this.itemToGraft.toNetwork(buffer);
        buffer.writeItem(this.itemToGrow);
        //no need to sync condition, because if it fails the data is never added to the data manager
    }
}
