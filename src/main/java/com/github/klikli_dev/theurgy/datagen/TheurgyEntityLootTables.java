package com.github.klikli_dev.theurgy.datagen;

import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;

public class TheurgyEntityLootTables extends EntityLootTables {
    //region Fields
    TheurgyLootTableProvider lootTableProvider;
    //endregion Fields

    //region Initialization
    public TheurgyEntityLootTables(TheurgyLootTableProvider lootTableProvider) {
        this.lootTableProvider = lootTableProvider;
    }
    //endregion Initialization

    //region Overrides
    @Override
    protected void addTables() {

    }

    @Override
    protected void registerLootTable(EntityType<?> type, LootTable.Builder table) {
        this.lootTableProvider.entityLootTable.put(type, table);
    }
    //endregion Overrides
}
