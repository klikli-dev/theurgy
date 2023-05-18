package com.klikli_dev.theurgy.registry;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.block.CalcinationOven;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Theurgy.MODID);

    public static final RegistryObject<Block> CALCINATION_OVEN =
            BLOCKS.register("calcination_oven", () ->
                    new CalcinationOven(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()));
}
