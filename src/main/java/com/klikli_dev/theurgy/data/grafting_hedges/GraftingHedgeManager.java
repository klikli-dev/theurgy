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
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
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
        return loaded;
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
                .map(entry -> loadGraftingHedgeData(entry.getKey(), entry.getValue().getAsJsonObject()))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        data -> data.id,
                        data -> data)
                );

        this.onLoaded();
    }

    private GraftingHedgeData loadGraftingHedgeData(ResourceLocation key, JsonObject value) {
        GraftingHedgeData data = GraftingHedgeData.fromJson(key, value);
        if (data.condition != null && !data.condition.test()) {
            Theurgy.LOGGER.debug("Skipped loading GraftingHedgeData {} as it did not match the condition.", key);
            return null;
        }
        return data;
    }
}
