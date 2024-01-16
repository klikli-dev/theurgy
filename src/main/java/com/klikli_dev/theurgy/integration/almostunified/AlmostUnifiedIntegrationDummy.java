package com.klikli_dev.theurgy.integration.almostunified;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class AlmostUnifiedIntegrationDummy implements AlmostUnifiedIntegration {
    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public @Nullable Item getPreferredItemForTag(TagKey<Item> tag) {
        return null;
    }
}
