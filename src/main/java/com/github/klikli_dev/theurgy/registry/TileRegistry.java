package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.tile.CrucibleTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(
            ForgeRegistries.TILE_ENTITIES, Theurgy.MODID);

    public static final RegistryObject<TileEntityType<CrucibleTileEntity>> CRUCIBLE = TILES.register(
            "crucible", () -> TileEntityType.Builder.create(CrucibleTileEntity::new,
                    BlockRegistry.CRUCIBLE.get()).build(null));
}
