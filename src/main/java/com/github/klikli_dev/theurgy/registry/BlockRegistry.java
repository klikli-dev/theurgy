/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
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

package com.github.klikli_dev.theurgy.registry;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.common.block.*;
import com.github.klikli_dev.theurgy.common.block.crystal.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
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

    //Constructs
    public static final RegistryObject<CrucibleBlock> CRUCIBLE =
            register("crucible", () -> new CrucibleBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(2.0F).notSolid()));

    public static final RegistryObject<EssentiaEmitterBlock> ESSENTIA_EMITTER =
            register("essentia_emitter", () -> new EssentiaEmitterBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(1.0F).notSolid()));

    public static final RegistryObject<EssentiaReceiverBlock> ESSENTIA_RECEIVER =
            register("essentia_receiver", () -> new EssentiaReceiverBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(1.0F).notSolid()));

    public static final RegistryObject<CreativeEssentiaSourceBlock> CREATIVE_ESSENTIA_SOURCE =
            register("creative_essentia_source", () -> new CreativeEssentiaSourceBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(1.0F).notSolid()));

    public static final RegistryObject<AetherEmitterBlock> AETHER_EMITTER =
            register("aether_emitter", () -> new AetherEmitterBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(1.0F).notSolid()));

    public static final RegistryObject<AetherReceiverBlock> AETHER_RECEIVER =
            register("aether_receiver", () -> new AetherReceiverBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(1.0F).notSolid()));

    public static final RegistryObject<CreativeAetherSourceBlock> CREATIVE_AETHER_SOURCE =
            register("creative_aether_source", () -> new CreativeAetherSourceBlock(
                    Block.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setRequiresTool().hardnessAndResistance(1.0F).notSolid()));


    //Resources
    public static final ICrystalSpreadCondition PURE_CRYSTAL_CONDITION = new PureCrystalCondition();
    public static final ICrystalSpreadCondition PRIMA_MATERIA_CRYSTAL_CONDITION = new PrimaMateriaCrystalCondition();
    public static final ICrystalSpreadCondition AER_CRYSTAL_CONDITION = new AerCrystalCondition();
    public static final ICrystalSpreadCondition AQUA_CRYSTAL_CONDITION = new AquaCrystalCondition();
    public static final ICrystalSpreadCondition IGNIS_CRYSTAL_CONDITION = new IgnisCrystalCondition();
    public static final ICrystalSpreadCondition TERRA_CRYSTAL_CONDITION = new TerraCrystalCondition();

    public static final ICrystalSpreadHandler PURE_CRYSTAL_SPREADER = new PureCrystalSpreader(
            PURE_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.pureCrystalChanceToSpread::get,
            PRIMA_MATERIA_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.primaMateriaCrystalChanceToSpread::get,
            AER_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.aerCrystalChanceToSpread::get,
            AQUA_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.aquaCrystalChanceToSpread::get,
            IGNIS_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.ignisCrystalChanceToSpread::get,
            TERRA_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.terraCrystalChanceToSpread::get
    );

    public static final RegistryObject<CrystalBlock> PURE_CRYSTAL = register("pure_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , PURE_CRYSTAL_SPREADER));

    public static final RegistryObject<CrystalBlock> PRIMA_MATERIA_CRYSTAL =
            register("prima_materia_crystal", () -> new CrystalBlock(
                    Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                            .hardnessAndResistance(0.3f, 0.3f)
                            .setLightLevel((state) -> 8).tickRandomly(),
                    new PrimaMateriaCrystalSpreader(PRIMA_MATERIA_CRYSTAL_CONDITION,
                            Theurgy.CONFIG.crystalSettings.primaMateriaSpreadEssentia::get,
                            BlockRegistry.PRIMA_MATERIA_CRYSTAL::get)));

    public static final RegistryObject<CrystalBlock> AER_CRYSTAL = register("aer_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , new EssentiaCrystalSpreader(AER_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.aerCrystalChanceToSpread::get,
            BlockRegistry.AER_CRYSTAL::get)));
    public static final RegistryObject<CrystalBlock> AQUA_CRYSTAL = register("aqua_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , new EssentiaCrystalSpreader(AQUA_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.aquaCrystalChanceToSpread::get,
            BlockRegistry.AQUA_CRYSTAL::get)));
    public static final RegistryObject<CrystalBlock> IGNIS_CRYSTAL = register("ignis_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , new EssentiaCrystalSpreader(IGNIS_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.ignisCrystalChanceToSpread::get,
            BlockRegistry.IGNIS_CRYSTAL::get)));
    public static final RegistryObject<CrystalBlock> TERRA_CRYSTAL = register("terra_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8).tickRandomly()
            , new EssentiaCrystalSpreader(TERRA_CRYSTAL_CONDITION,
            Theurgy.CONFIG.crystalSettings.terraCrystalChanceToSpread::get,
            BlockRegistry.TERRA_CRYSTAL::get)));

    //not spreading crystals
    public static final RegistryObject<CrystalBlock> EMPTY_END_CRYSTAL = register("empty_end_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8)
            , new NoSpreadCrystalSpreader()));
    public static final RegistryObject<CrystalBlock> END_INFUSED_CRYSTAL = register("end_infused_crystal", () -> new CrystalBlock(
            Block.Properties.create(Material.GLASS).sound(SoundType.GLASS)
                    .hardnessAndResistance(0.3f, 0.3f)
                    .setLightLevel((state) -> 8)
            , new NoSpreadCrystalSpreader()));
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
