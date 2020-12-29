package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.block.CrucibleBlock;
import com.github.klikli_dev.theurgy.common.block.crystal.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.AquaAffinityEnchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BlockRegistry {
    //region Fields
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Theurgy.MODID);
    public static final Map<ResourceLocation, BlockDataGenSettings> BLOCK_DATA_GEN_SETTINGS = new HashMap<>();

    //Machines
    public static final RegistryObject<CrucibleBlock> CRUCIBLE =
            register("crucible", () -> new CrucibleBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(2.0F).notSolid()));

    //Resources
    public static final ICrystalSpreadCondition PURE_CRYSTAL_CONDITION = new PureCrystalCondition();
    public static final ICrystalSpreadCondition PRIMA_MATERIA_CRYSTAL_CONDITION = new PrimaMateriaCrystalCondition();
    public static final ICrystalSpreadCondition AER_CRYSTAL_CONDITION = new AerCrystalCondition();
    public static final ICrystalSpreadCondition AQUA_CRYSTAL_CONDITION = new AquaCrystalCondition();
    public static final ICrystalSpreadCondition IGNIS_CRYSTAL_CONDITION = new IgnisCrystalCondition();
    public static final ICrystalSpreadCondition TERRA_CRYSTAL_CONDITION = new TerraCrystalCondition();

    public static final ICrystalSpreadHandler PURE_CRYSTAL_SPREADER = new PureCrystalSpreader(
            PURE_CRYSTAL_CONDITION,
            PRIMA_MATERIA_CRYSTAL_CONDITION,
            AER_CRYSTAL_CONDITION,
            AQUA_CRYSTAL_CONDITION,
            IGNIS_CRYSTAL_CONDITION,
            TERRA_CRYSTAL_CONDITION
    );
    public static final RegistryObject<CrystalBlock> PURE_CRYSTAL = register("pure_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , PURE_CRYSTAL_SPREADER) {
    });

    public static final RegistryObject<CrystalBlock> PRIMA_MATERIA_CRYSTAL = register("prima_materia_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly(),
            new PrimaMateriaCrystalSpreader(PRIMA_MATERIA_CRYSTAL_CONDITION, BlockRegistry.PRIMA_MATERIA_CRYSTAL::get)) {
    });

    public static final RegistryObject<CrystalBlock> AER_CRYSTAL = register("aer_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , new EssentiaCrystalSpreader(AER_CRYSTAL_CONDITION, 5, BlockRegistry.AER_CRYSTAL::get)) {
    });
    public static final RegistryObject<CrystalBlock> AQUA_CRYSTAL = register("aqua_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , new EssentiaCrystalSpreader(AQUA_CRYSTAL_CONDITION, 5, BlockRegistry.AQUA_CRYSTAL::get)) {
    });
    public static final RegistryObject<CrystalBlock> IGNIS_CRYSTAL = register("ignis_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , new EssentiaCrystalSpreader(IGNIS_CRYSTAL_CONDITION, 5, BlockRegistry.IGNIS_CRYSTAL::get)) {
    });
    public static final RegistryObject<CrystalBlock> TERRA_CRYSTAL = register("terra_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , new EssentiaCrystalSpreader(TERRA_CRYSTAL_CONDITION,5, BlockRegistry.TERRA_CRYSTAL::get)) {
    });
    //endregion Fields

    //region Static Methods
    public static <I extends Block> RegistryObject<I> register(final String name, final Supplier<? extends I> sup) {
        return register(name, sup, true);
    }

    public static <I extends Block> RegistryObject<I> register(final String name, final Supplier<? extends I> sup,
                                                               boolean generateDefaultBlockItem) {
        return register(name, sup, generateDefaultBlockItem, LootTableType.DROP_SELF);
    }

    public static <I extends Block> RegistryObject<I> register(final String name, final Supplier<? extends I> sup,
                                                               boolean generateDefaultBlockItem,
                                                               LootTableType lootTableType) {
        RegistryObject<I> object = BLOCKS.register(name, sup);
        BLOCK_DATA_GEN_SETTINGS.put(object.getId(), new BlockDataGenSettings(generateDefaultBlockItem, lootTableType));
        return object;
    }
    //endregion Static Methods

    public enum LootTableType {
        EMPTY,
        DROP_SELF
    }

    public static class BlockDataGenSettings {
        //region Fields
        public boolean generateDefaultBlockItem;
        public LootTableType lootTableType;
        //endregion Fields

        //region Initialization
        public BlockDataGenSettings(boolean generateDefaultBlockItem,
                                    LootTableType lootTableType) {
            this.generateDefaultBlockItem = generateDefaultBlockItem;
            this.lootTableType = lootTableType;
        }
        //endregion Initialization
    }
}
