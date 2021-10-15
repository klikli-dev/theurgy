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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraftingHedgeManager extends SimpleJsonResourceReloadListener {
    public static final String FOLDER = TheurgyConstants.Data.GRAFTING_HEDGES_PATH;
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final GraftingHedgeManager instance = new GraftingHedgeManager();

    private Map<ResourceLocation, GraftingHedgeData> graftingHedgeData = Collections.emptyMap();
    private boolean loaded;

    public GraftingHedgeManager() {
        super(GSON, FOLDER);
    }

    public static GraftingHedgeManager get() {
        return instance;
    }

    public void getDatapackSyncPacket() {
        //TODO: get instance of sync packet
    }

    @SubscribeEvent
    public void onDatapackSync(OnDatapackSyncEvent event) {
        //TODO: send sync packet to players
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void onDatapackSyncPacket() {
        //get data from sync packet
    }

    public Optional<GraftingHedgeData> getDataFor(ItemStack itemToGraft) {
        return this.graftingHedgeData.values().stream().filter(data -> data.itemToGraft.test(itemToGraft)).findFirst();
    }

    public Optional<GraftingHedgeData> byKey(ResourceLocation key) {
        return Optional.ofNullable(this.graftingHedgeData.get(key));
    }

    protected void onLoaded() {
        this.loaded = true;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        this.graftingHedgeData = pObject.entrySet().stream()
                .filter(entry -> entry.getValue().isJsonObject())
                .map(entry -> this.loadGraftingHedgeData(entry.getKey(), entry.getValue().getAsJsonObject()))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        data -> data.id,
                        data -> data)
                );

        this.onLoaded();
    }

    private GraftingHedgeData loadGraftingHedgeData(ResourceLocation key, JsonObject value) {
        ICondition condition = this.loadCondition(value);
        if (condition != null && !condition.test()) {
            Theurgy.LOGGER.debug("Skipped loading GraftingHedgeData {} as it did not match the condition.", key);
            return null;
        }
        return GraftingHedgeData.fromJson(key, value);
    }

    private ICondition loadCondition(JsonObject value){
        return value.has("condition") ?
                CraftingHelper.getCondition(GsonHelper.getAsJsonObject(value, "condition")) : null;
    }
}
