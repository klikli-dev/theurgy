package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.block.CalcinationOvenEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Theurgy.MODID);

    public static final RegistryObject<BlockEntityType<CalcinationOvenEntity>> CALCINATION_OVEN_ENTITY =
            BLOCK_ENTITIES.register("calcination_oven_entity", () ->
                    BlockEntityType.Builder.of(CalcinationOvenEntity::new,
                            BlockRegistry.CALCINATION_OVEN.get()).build(null));
}
