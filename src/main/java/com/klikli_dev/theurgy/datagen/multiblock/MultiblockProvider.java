/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.multiblock;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MultiblockProvider implements DataProvider {

    protected final PackOutput.PathProvider pathProvider;
    protected String modid;

    protected BiConsumer<ResourceLocation, JsonObject> multiblockConsumer;

    public MultiblockProvider(PackOutput packOutput, String modid) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "modonomicon/multiblocks");
        this.modid = modid;
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(this.modid, name);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        this.multiblockConsumer = (id, recipe) -> {
            if (!set.add(id)) {
                throw new IllegalStateException("Duplicate multiblock " + id);
            } else {
                futures.add(DataProvider.saveStable(pOutput, recipe, this.pathProvider.json(id)));
            }
        };
        this.buildMultiblocks(this.multiblockConsumer);
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Multiblocks";
    }

    public void buildMultiblocks(BiConsumer<ResourceLocation, JsonObject> multiblockConsumer) {
        this.multiblockConsumer.accept(this.modLoc("placement/calcination_oven"),
                new DenseMultiblockBuilder()
                        .layer("0")
                        .layer("b")
                        .block('b', BlockRegistry.PYROMANTIC_BRAZIER)
                        .block('0', BlockRegistry.CALCINATION_OVEN)
                        .build()
        );
    }

    private class DenseMultiblockBuilder {
        JsonObject multiblock;

        public DenseMultiblockBuilder() {
            this.multiblock = new JsonObject();
            this.multiblock.addProperty("type", "modonomicon:dense");
            this.multiblock.add("pattern", new JsonArray());
            this.multiblock.add("mapping", new JsonObject());
        }

        private List<String> createPattern(String... rows) {
            List<String> pattern = new ArrayList<>();
            for (String row : rows) {
                pattern.add(row.replace(" ", "_"));
            }
            return pattern;
        }

        /**
         * Creates a new layer from a list of strings representing rows of blocks.
         * Does NOT replace spaces with underscores, so likely you'll want the layer(String...) method instead.
         */
        public DenseMultiblockBuilder layer(List<String> rows) {
            var pattern = this.multiblock.getAsJsonArray("pattern");
            var layer = new JsonArray();
            for (String row : rows)
                layer.add(row);
            pattern.add(layer);
            return this;
        }

        /**
         * Creates a new layer from a string array, each row representing a row of blocks in the layer.
         * Replaces spaces (which would be mapped to AIR) with underscores (which will be mapped to ANY).
         */
        public DenseMultiblockBuilder layer(String... rows) {
            return this.layer(this.createPattern(rows));
        }

        /**
         * Creates a new mapping from a character to a JSON representation of a modonomicon state matcher representing a block.
         */
        private DenseMultiblockBuilder map(String key, JsonObject stateMatcher) {
            var mapping = this.multiblock.getAsJsonObject("mapping");
            mapping.add(key, stateMatcher);
            return this;
        }

        public DenseMultiblockBuilder block(char c, Supplier<? extends Block> b) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "modonomicon:block");
            json.addProperty("block", ForgeRegistries.BLOCKS.getKey(b.get()).toString());

            return this.map(String.valueOf(c), json);
        }

        public DenseMultiblockBuilder blockDisplay(char c, Supplier<? extends Block> b, Supplier<? extends Block> display) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "modonomicon:block");
            json.addProperty("block", ForgeRegistries.BLOCKS.getKey(b.get()).toString());
            json.addProperty("display", ForgeRegistries.BLOCKS.getKey(display.get()).toString());

            return this.map(String.valueOf(c), json);
        }

        public DenseMultiblockBuilder display(char c, Supplier<? extends Block> display) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "modonomicon:display");
            json.addProperty("display", ForgeRegistries.BLOCKS.getKey(display.get()).toString());

            return this.map(String.valueOf(c), json);
        }

        public DenseMultiblockBuilder tag(char c, TagKey<Block> tag, Supplier<? extends Block> display) {
            JsonObject json = new JsonObject();
            json.addProperty("type", "modonomicon:tag");
            json.addProperty("tag", "#" + tag.location());
            json.addProperty("display", ForgeRegistries.BLOCKS.getKey(display.get()).toString());

            return this.map(String.valueOf(c), json);
        }

        public JsonObject build() {
            return this.build(true);
        }

        public JsonObject build(boolean displayGroundLayer) {
            return this.build(displayGroundLayer, 1);
        }

        public JsonObject build(boolean displayGroundLayer, int groundLayerPadding) {
            if (displayGroundLayer) {
                //add mapping for the ground layer
                this.display('*', () -> Blocks.BASALT).display('+', () -> Blocks.STONE);

                var pattern = this.multiblock.getAsJsonArray("pattern");

                //build ground layer
                var width = pattern.get(0).getAsJsonArray().get(0).getAsString().length();
                var length = pattern.get(0).getAsJsonArray().size();
                var groundLayer = new JsonArray();
                for (int i = 0; i < length + groundLayerPadding * 2; i++) {
                    var row = new StringBuilder();
                    for (int j = 0; j < width + groundLayerPadding * 2; j++) {
                        //create a checkerboard, alternatively adding "*" and "+" to the row
                        if ((i + j) % 2 == 0)
                            row.append("*");
                        else
                            row.append("+");
                    }
                    groundLayer.add(row.toString());
                }

                //loop through all layers in the pattern, and add groundLayerPadding number of rows at the beginning and the end, and for existing rows, add groundLayerPadding number of "_" to the beginning and the end.
                for (int i = 0; i < pattern.size(); i++) {
                    var layer = pattern.get(i).getAsJsonArray();
                    //add "_" = any matcher to the beginning and the end of each row to account for the padding
                    for (int j = 0; j < layer.size(); j++) {
                        var row = layer.get(j).getAsString();
                        var newRow = new StringBuilder();
                        for (int k = 0; k < groundLayerPadding; k++) {
                            newRow.append("_");
                        }
                        newRow.append(row);
                        for (int k = 0; k < groundLayerPadding; k++) {
                            newRow.append("_");
                        }
                        layer.set(j, new JsonPrimitive(newRow.toString()));
                    }

                    //add empty rows / "_" any match rows to the beginning and the end of the layer to account for the padding
                    var emptyRow = "_".repeat( length + groundLayerPadding * 2);
                    var updatedLayer = new JsonArray();
                    for (int j = 0; j < groundLayerPadding; j++) {
                        updatedLayer.add(new JsonPrimitive(emptyRow));
                    }
                    updatedLayer.addAll(layer);
                    for (int j = 0; j < groundLayerPadding; j++) {
                        updatedLayer.add(new JsonPrimitive(emptyRow));
                    }
                    pattern.set(i, updatedLayer);
                }

                //finally add ground layer to pattern
                pattern.add(groundLayer);
            }

            return this.multiblock;
        }
    }
}
