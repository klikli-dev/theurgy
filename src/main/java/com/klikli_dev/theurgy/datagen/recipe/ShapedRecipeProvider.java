// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.divinationrod.DivinationRodItem;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.ItemTagRegistry;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ShapedRecipeProvider extends JsonRecipeProvider {

    public ShapedRecipeProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID, "crafting/shaped");
    }

    @Override
    public void buildRecipes(BiConsumer<ResourceLocation, JsonObject> recipeConsumer) {
        //Divination Rods
        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.DIVINATION_ROD_T1.get(), 1, this.makeDivinationRodSettings(ItemRegistry.DIVINATION_ROD_T1.get()).build())
                .pattern(" GR")
                .pattern(" RG")
                .pattern("R  ")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('R', Tags.Items.RODS_WOODEN)
        );

        var amethystDivinationRodSettings = this.makeDivinationRodSettings(ItemRegistry.AMETHYST_DIVINATION_ROD.get());
        //noinspection deprecation
        amethystDivinationRodSettings.set(DataComponentRegistry.DIVINATION_LINKED_BLOCK.get(), Blocks.BUDDING_AMETHYST.builtInRegistryHolder());

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.AMETHYST_DIVINATION_ROD.get(), 1, amethystDivinationRodSettings.build())
                .pattern(" GP")
                .pattern(" RG")
                .pattern("R  ")
                .define('P', Tags.Items.DYES_PURPLE)
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('R', Tags.Items.RODS_WOODEN)
        );

        this.makeRecipe(new ShapedRecipeBuilder("theurgy:divination_rod",
                ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get(), 1, this.makeDivinationRodSettings(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get()).build())
                .pattern(" GS")
                .pattern(" RG")
                .pattern("R  ")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('R', Tags.Items.RODS_WOODEN)
                .define('S', ItemTagRegistry.ALCHEMICAL_SULFURS_ABUNDANT)
        );
        this.makeRecipe(new ShapedRecipeBuilder("theurgy:divination_rod",
                ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get(), 1, this.makeDivinationRodSettings(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get()).build())
                .pattern(" GS")
                .pattern(" RG")
                .pattern("R  ")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('R', Tags.Items.RODS_WOODEN)
                .define('S', ItemTagRegistry.ALCHEMICAL_SULFURS_COMMON)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.DIVINATION_ROD_T2.get(), 1, this.makeDivinationRodSettings(ItemRegistry.DIVINATION_ROD_T2.get()).build())
                .pattern(" GM")
                .pattern(" AG")
                .pattern("R  ")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('R', Tags.Items.RODS_WOODEN)
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('M', Tags.Items.NUGGETS_GOLD)
        );
        this.makeRecipe(new ShapedRecipeBuilder(
                "theurgy:divination_rod", ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get(), 1, this.makeDivinationRodSettings(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get()).build())
                .pattern(" GS")
                .pattern(" AG")
                .pattern("R  ")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('R', Tags.Items.RODS_WOODEN)
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('S', ItemTagRegistry.ALCHEMICAL_SULFURS_RARE)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.DIVINATION_ROD_T3.get(), 1, this.makeDivinationRodSettings(ItemRegistry.DIVINATION_ROD_T3.get()).build())
                .pattern(" GD")
                .pattern(" QG")
                .pattern("A  ")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('A', Tags.Items.GEMS_AMETHYST)
        );
        this.makeRecipe(new ShapedRecipeBuilder("theurgy:divination_rod",
                ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get(), 1, this.makeDivinationRodSettings(ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get()).build())
                .pattern(" GS")
                .pattern(" DG")
                .pattern("A  ")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('A', Tags.Items.GEMS_AMETHYST)
                .define('S', ItemTagRegistry.ALCHEMICAL_SULFURS_PRECIOUS)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.DIVINATION_ROD_T4.get(), 1, this.makeDivinationRodSettings(ItemRegistry.DIVINATION_ROD_T4.get()).build())
                .pattern(" GM")
                .pattern(" RG")
                .pattern("A  ")
                .define('G', Tags.Items.GLASS_BLOCKS)
                .define('R', Tags.Items.RODS_BLAZE)
                .define('M', Tags.Items.ORES_NETHERITE_SCRAP)
                .define('A', Tags.Items.GEMS_AMETHYST)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.PYROMANTIC_BRAZIER.get(), 1)
                .pattern("CCC")
                .pattern("CSC")
                .pattern("SSS")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('S', Tags.Items.STONES)
        );


        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.CALCINATION_OVEN.get(), 1)
                .pattern(" I ")
                .pattern("ICI")
                .pattern(" I ")
                .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
                .define('I', Tags.Items.INGOTS_IRON)
        );


        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.DISTILLER.get(), 1)
                .pattern(" I ")
                .pattern("ICI")
                .pattern("SSS")
                .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.STONES)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.LIQUEFACTION_CAULDRON.get(), 1)
                .pattern("CCC")
                .pattern("CBC")
                .pattern("SSS")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('B', Items.CAULDRON)
                .define('S', Tags.Items.STONES)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.SAL_AMMONIAC_ACCUMULATOR.get(), 1)
                .pattern("SSS")
                .pattern("III")
                .pattern("R R")
                .define('R', Tags.Items.RODS_WOODEN)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.STONES)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.SAL_AMMONIAC_TANK.get(), 1)
                .pattern("ICI")
                .pattern("ICI")
                .pattern("RCR")
                .define('R', Tags.Items.RODS_WOODEN)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('I', Tags.Items.INGOTS_IRON)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.INCUBATOR.get(), 1)
                .pattern("PSP")
                .pattern("GGG")
                .pattern("SCS")
                .define('P', ItemTags.PLANKS)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('S', Tags.Items.STONES)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.INCUBATOR_MERCURY_VESSEL.get(), 1)
                .pattern("cMc")
                .pattern("c c")
                .pattern("SSS")
                .define('c', Tags.Items.INGOTS_COPPER)
                .define('M', ItemTagRegistry.ALCHEMICAL_MERCURIES)
                .define('S', Tags.Items.STONES)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.INCUBATOR_SALT_VESSEL.get(), 1)
                .pattern("csc")
                .pattern("c c")
                .pattern("SSS")
                .define('c', Tags.Items.INGOTS_COPPER)
                .define('s', ItemTagRegistry.ALCHEMICAL_SALTS)
                .define('S', Tags.Items.STONES)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.INCUBATOR_SULFUR_VESSEL.get(), 1)
                .pattern("csc")
                .pattern("c c")
                .pattern("SSS")
                .define('c', Tags.Items.INGOTS_COPPER)
                .define('s', ItemTagRegistry.ALCHEMICAL_SULFURS)
                .define('S', Tags.Items.STONES)
        );


        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.MERCURY_CATALYST.get(), 1)
                .pattern("imi")
                .pattern("gQg")
                .pattern("igi")
                .define('Q', Items.QUARTZ_BLOCK)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('m', ItemTagRegistry.ALCHEMICAL_MERCURIES)
        );

        this.makeRecipe(this.name(ItemRegistry.CALORIC_FLUX_EMITTER.get()) + "_from_campfire", new ShapedRecipeBuilder(
                ItemRegistry.CALORIC_FLUX_EMITTER.get(), 1)
                .pattern(" h ")
                .pattern("gmg")
                .pattern("sss")
                .define('h', Items.CAMPFIRE)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('s', Tags.Items.STONES)
                .define('m', ItemTagRegistry.ALCHEMICAL_MERCURIES)
        );

        this.makeRecipe(this.name(ItemRegistry.CALORIC_FLUX_EMITTER.get()) + "_from_lava_bucket", new ShapedRecipeBuilder(
                ItemRegistry.CALORIC_FLUX_EMITTER.get(), 1)
                .pattern(" h ")
                .pattern("gmg")
                .pattern("sss")
                .define('h', Items.LAVA_BUCKET)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('s', Tags.Items.STONES)
                .define('m', ItemTagRegistry.ALCHEMICAL_MERCURIES)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.SULFURIC_FLUX_EMITTER.get(), 1)
                .pattern(" a ")
                .pattern("gSg")
                .pattern("sss")
                .define('a', ItemTagRegistry.GEMS_SAL_AMMONIAC)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('s', Tags.Items.STONES)
                .define('S', ItemTagRegistry.ALCHEMICAL_SULFURS)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.REFORMATION_TARGET_PEDESTAL.get(), 1)
                .pattern("cSc")
                .pattern("cdc")
                .pattern("sss")
                .define('S', ItemTagRegistry.ALCHEMICAL_SULFURS)
                .define('c', Tags.Items.INGOTS_COPPER)
                .define('s', Items.BLACKSTONE)
                .define('d', Tags.Items.GEMS_DIAMOND)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.REFORMATION_SOURCE_PEDESTAL.get(), 1)
                .pattern("sSs")
                .pattern("iii")
                .pattern("sss")
                .define('S', ItemTagRegistry.ALCHEMICAL_SULFURS)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('s', Items.BLACKSTONE)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.REFORMATION_RESULT_PEDESTAL.get(), 1)
                .pattern("ggg")
                .pattern("gSg")
                .pattern("sss")
                .define('S', ItemTagRegistry.ALCHEMICAL_SULFURS)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('s', Items.BLACKSTONE)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.FERMENTATION_VAT.get(), 1)
                .pattern("csc")
                .pattern("cbc")
                .pattern("cCc")
                .define('s', ItemTagRegistry.ALCHEMICAL_SULFURS)
                .define('b', Tags.Items.BARRELS_WOODEN)
                .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
                .define('c', Tags.Items.INGOTS_COPPER)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.DIGESTION_VAT.get(), 1)
                .pattern(" s ")
                .pattern("gpg")
                .pattern("SSS")
                .define('s', ItemTagRegistry.GEMS_SAL_AMMONIAC)
                .define('p', Items.DECORATED_POT)
                .define('g', Tags.Items.INGOTS_GOLD)
                .define('S', Tags.Items.SANDSTONE_BLOCKS)
        );


        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.COPPER_WIRE.get(), 10)
                .pattern("cmc")
                .define('m', ItemRegistry.MERCURY_SHARD)
                .define('c', Tags.Items.INGOTS_COPPER)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.MERCURIAL_WAND.get(), 1)
                .pattern(" sm")
                .pattern(" cs")
                .pattern("s  ")
                .define('m', ItemRegistry.MERCURY_SHARD)
                .define('s', Tags.Items.RODS_WOODEN)
                .define('c', Tags.Items.INGOTS_COPPER)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.LOGISTICS_ITEM_INSERTER.get(), 1)
                .pattern("m")
                .pattern("c")
                .define('m', ItemRegistry.MERCURY_SHARD)
                .define('c', Tags.Items.INGOTS_COPPER)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.LOGISTICS_ITEM_EXTRACTOR.get(), 1)
                .pattern("c")
                .pattern("m")
                .define('m', ItemRegistry.MERCURY_SHARD)
                .define('c', Tags.Items.INGOTS_COPPER)
        );

        this.makeRecipe(new ShapedRecipeBuilder(
                ItemRegistry.LOGISTICS_CONNECTION_NODE.get(), 3)
                .pattern(" m ")
                .pattern(" i ")
                .pattern("bbb")
                .define('m', ItemRegistry.MERCURY_SHARD)
                .define('i', Tags.Items.INGOTS_IRON)
                .define('b', Tags.Items.BRICKS_NORMAL)
        );
    }

    public DataComponentPatch.Builder makeDivinationRodSettings(DivinationRodItem rodItem) {
        return this.makeDivinationRodSettings(rodItem.defaultTier, rodItem.defaultAllowedBlocksTag, rodItem.defaultDisallowedBlocksTag, rodItem.defaultRange, rodItem.defaultDuration, rodItem.defaultDurability);
    }

    public DataComponentPatch.Builder makeDivinationRodSettings(Tiers defaultTier, TagKey<Block> defaultAllowedBlocksTag, TagKey<Block> defaultDisallowedBlocksTag, int defaultRange, int defaultDuration, int defaultDurability) {
        return DataComponentPatch.builder()
                .set(DataComponentRegistry.DIVINATION_SETTINGS_TIER.get(), defaultTier)
                .set(DataComponentRegistry.DIVINATION_SETTINGS_ALLOWED_BLOCKS_TAG.get(), defaultAllowedBlocksTag)
                .set(DataComponentRegistry.DIVINATION_SETTINGS_DISALLOWED_BLOCKS_TAG.get(), defaultDisallowedBlocksTag)
                .set(DataComponentRegistry.DIVINATION_SETTINGS_RANGE.get(), defaultRange)
                .set(DataComponentRegistry.DIVINATION_SETTINGS_DURATION.get(), defaultDuration)
                .set(DataComponentRegistry.DIVINATION_SETTINGS_MAX_DAMAGE.get(), defaultDurability);
    }

    protected void makeRecipe(ShapedRecipeBuilder recipe) {
        this.makeRecipe(this.name(recipe.result()), recipe);
    }

    protected void makeRecipe(ItemLike result, ShapedRecipeBuilder recipe) {
        this.makeRecipe(this.name(result), recipe);
    }

    protected void makeRecipe(String name, ShapedRecipeBuilder recipe) {
        this.recipeConsumer.accept(this.modLoc(name), recipe.build());
    }

    @Override
    public @NotNull String getName() {
        return "Shaped Crafting Recipes";
    }

    protected static class ShapedRecipeBuilder {

        private final JsonObject recipe;
        private final ItemStack result;

        public ShapedRecipeBuilder(ItemLike result) {
            //noinspection deprecation
            this(result.asItem().builtInRegistryHolder());
        }

        public ShapedRecipeBuilder(ItemLike result, int count) {
            //noinspection deprecation
            this(result.asItem().builtInRegistryHolder(), count);
        }

        public ShapedRecipeBuilder(ItemLike result, int count, DataComponentPatch patch) {
            //noinspection deprecation
            this(result.asItem().builtInRegistryHolder(), count, patch);
        }

        public ShapedRecipeBuilder(String recipeType, ItemLike result, int count, DataComponentPatch patch) {
            //noinspection deprecation
            this(recipeType, new ItemStack(result.asItem().builtInRegistryHolder(), count, patch));
        }

        public ShapedRecipeBuilder(Holder<Item> result) {
            this(result, 1);
        }

        public ShapedRecipeBuilder(Holder<Item> result, int count) {
            this(result, count, DataComponentPatch.EMPTY);
        }

        public ShapedRecipeBuilder(Holder<Item> result, int count, DataComponentPatch patch) {
            //noinspection DataFlowIssue
            this(BuiltInRegistries.RECIPE_SERIALIZER.getKey(RecipeSerializer.SHAPED_RECIPE).toString(), result, count, patch);
        }

        public ShapedRecipeBuilder(String recipeType, Holder<Item> result, int count, DataComponentPatch patch) {
            this(recipeType, new ItemStack(result, count, patch));
        }

        public ShapedRecipeBuilder(String recipeType, ItemStack result) {
            this.result = result;
            this.recipe = new JsonObject();
            this.recipe.addProperty("type", recipeType);
            this.recipe.add("result", ItemStack.STRICT_CODEC.encodeStart(JsonOps.INSTANCE, result).getOrThrow());
            this.recipe.add("key", new JsonObject());
            this.recipe.add("pattern", new JsonArray());
        }

        private JsonObject ingredient(TagKey<Item> tag) {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", tag.location().toString());
            return jsonobject;
        }

        public ShapedRecipeBuilder define(char key, TagKey<Item> tag) {
            return this.define(key, this.ingredient(tag));
        }

        private JsonObject ingredient(ItemLike item) {
            JsonObject jsonobject = new JsonObject();
            //noinspection deprecation,OptionalGetWithoutIsPresent
            jsonobject.addProperty("item", item.asItem().builtInRegistryHolder().unwrapKey().get().location().toString());
            return jsonobject;
        }

        public ShapedRecipeBuilder define(char key, ItemLike item) {
            return this.define(key, this.ingredient(item));
        }

        public ShapedRecipeBuilder define(char key, JsonObject ingredient) {
            var keyString = String.valueOf(key);
            var keys = this.recipe.getAsJsonObject("key");
            if (keys.has(keyString))
                throw new IllegalArgumentException("Key " + keyString + " already defined");

            keys.add(keyString, ingredient);
            return this;
        }

        public ShapedRecipeBuilder pattern(String pattern) {
            this.recipe.getAsJsonArray("pattern").add(pattern);
            return this;
        }

        public JsonObject build() {
            if (!this.recipe.has("category"))
                this.recipe.addProperty("category", CraftingBookCategory.MISC.getSerializedName());
            return this.recipe;
        }

        public ItemStack result() {
            return this.result;
        }
    }
}
