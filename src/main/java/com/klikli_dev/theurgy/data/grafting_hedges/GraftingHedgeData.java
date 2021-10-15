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

package com.klikli_dev.theurgy.data.grafting_hedges;

import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.util.SerializerUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;

public class GraftingHedgeData {
    public ResourceLocation id;
    public Ingredient itemToGraft;
    public ItemStack itemToGrow;

    public GraftingHedgeData(ResourceLocation id, Ingredient itemToGraft, ItemStack itemToGrow) {
        this.id = id;
        this.itemToGraft = itemToGraft;
        this.itemToGrow = itemToGrow;
    }

    public static GraftingHedgeData fromJson(ResourceLocation id, JsonObject json) {
        Ingredient itemToGraft = Ingredient.fromJson(
                GsonHelper.isArrayNode(json, "item_to_graft") ?
                        GsonHelper.getAsJsonArray(json, "item_to_graft") :
                        GsonHelper.getAsJsonObject(json, "item_to_graft"));
        ItemStack itemToGrow = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "item_to_grow"), true);
        return new GraftingHedgeData(id, itemToGraft, itemToGrow);
    }

    public static GraftingHedgeData fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient itemToGraft = Ingredient.fromNetwork(buffer);
        ItemStack itemToGrow = buffer.readItem();
        return new GraftingHedgeData(id, itemToGraft, itemToGrow);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("item_to_graft", this.itemToGraft.toJson());
        json.add("item_to_grow", SerializerUtil.serialize(this.itemToGrow));
        return json;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        this.itemToGraft.toNetwork(buffer);
        buffer.writeItem(this.itemToGrow);
        //no need to sync condition, because if it fails the data is never added to the data manager
    }
}
