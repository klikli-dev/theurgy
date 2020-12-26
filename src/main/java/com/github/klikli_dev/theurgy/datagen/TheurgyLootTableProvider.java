package com.github.klikli_dev.theurgy.datagen;

import com.github.klikli_dev.theurgy.Theurgy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TheurgyLootTableProvider extends LootTableProvider {
    //region Fields
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    protected final Map<Block, LootTable.Builder> blockLootTable = new HashMap<>();
    protected final Map<EntityType<?>, LootTable.Builder> entityLootTable = new HashMap<>();

    protected final TheurgyBlockLootTables blockLoot;
    protected final TheurgyEntityLootTables entityLoot;

    protected final DataGenerator generator;
    //endregion Fields

    //region Initialization
    public TheurgyLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
        this.blockLoot = new TheurgyBlockLootTables(this);
        this.entityLoot = new TheurgyEntityLootTables(this);
    }
    //endregion Initialization

    //region Overrides
    @Override
    // Entry point
    public void act(DirectoryCache cache) {
        this.addTables();
        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : this.blockLootTable.entrySet()) {
            tables.put(entry.getKey().getLootTable(),
                    entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
        }
        for (Map.Entry<EntityType<?>, LootTable.Builder> entry : this.entityLootTable.entrySet()) {
            tables.put(entry.getKey().getLootTable(),
                    entry.getValue().setParameterSet(LootParameterSets.ENTITY).build());
        }
        this.writeTables(cache, tables);
    }

    @Override
    public String getName() {
        return "Theurgy LootTables";
    }
    //endregion Overrides

    //region Methods
    protected void addTables() {
        this.blockLoot.addTables();
        this.entityLoot.addTables();
    }

    private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
            } catch (IOException e) {
                Theurgy.LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }
    //endregion Methods
}
