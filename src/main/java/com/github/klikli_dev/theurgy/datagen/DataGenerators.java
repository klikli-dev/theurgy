package com.github.klikli_dev.theurgy.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    //region Static Methods
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new TheurgyLootTableProvider(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new TheurgyBlockStateProvider(generator, event.getExistingFileHelper()));
        }
    }
    //endregion Static Methods
}