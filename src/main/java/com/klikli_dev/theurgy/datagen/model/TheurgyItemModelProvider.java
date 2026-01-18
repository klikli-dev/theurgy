// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.item.niter.AlchemicalNiterItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.registry.NiterRegistry;
import com.klikli_dev.theurgy.registry.SaltRegistry;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

public class TheurgyItemModelProvider extends ModelProvider {
    public TheurgyItemModelProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID);
    }

    protected String name(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    private void registerAlchemicalSalt(ItemModelGenerators itemModels, Item item) {
        //Alchemical salt has a custom model but here we just register it as a generated item with a specific texture?
        //Original: parent(modLoc("item/alchemical_salt"))
        //In 1.21.4 we can create a model with that parent.
        //But assuming "item/alchemical_salt" is a manually created model file or generated elsewhere.
        //If it's generated here:
        //this.registerItemGenerated("alchemical_salt");
        
        //If we want to use "item/alchemical_salt" as parent for the item:
        //We can use a custom template or just point to it.
        //However, in vanilla ItemModelGenerators, we typically generate a flat item model.
        //If alchemical salts just use the texture:
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    private void registerItemGenerated(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    private void registerItemGenerated(ItemModelGenerators itemModels, Item item, String texture) {
        //Generate model with custom texture
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(Theurgy.loc("item/" + texture)), itemModels.modelOutput);
    }

    private void registerItemHandheld(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
    }
    
    //Helper to create a model for a variant but not assign it to the item directly (used for divination rod)
    private ResourceLocation createItemHandheldVariant(ItemModelGenerators itemModels, String modelName, String texture) {
        return ModelTemplates.FLAT_HANDHELD_ITEM.create(Theurgy.loc("item/" + modelName), TextureMapping.layer0(Theurgy.loc("item/" + texture)), itemModels.modelOutput);
    }

    private void registerDivinationRod(ItemModelGenerators itemModels, Item divinationRodItem) {
        String name = this.name(divinationRodItem);
        //Generate variant models
        ResourceLocation variant0 = this.createItemHandheldVariant(itemModels, name + "/0", name + "/divination_rod_0");
        ResourceLocation variant1 = this.createItemHandheldVariant(itemModels, name + "/1", name + "/divination_rod_1");
        ResourceLocation variant2 = this.createItemHandheldVariant(itemModels, name + "/2", name + "/divination_rod_2");
        ResourceLocation variant3 = this.createItemHandheldVariant(itemModels, name + "/3", name + "/divination_rod_3");
        ResourceLocation variant4 = this.createItemHandheldVariant(itemModels, name + "/4", name + "/divination_rod_4");
        ResourceLocation variant5 = this.createItemHandheldVariant(itemModels, name + "/5", name + "/divination_rod_5");
        ResourceLocation variant6 = this.createItemHandheldVariant(itemModels, name + "/6", name + "/divination_rod_6");
        ResourceLocation variant7 = this.createItemHandheldVariant(itemModels, name + "/7", name + "/divination_rod_7");
        ResourceLocation searchingVariant = this.createItemHandheldVariant(itemModels, name + "/searching", name + "/divination_rod_searching");

        //Main item model - just use variant0 for now until we implement the conditional logic
        //TODO: Implement RangeSelect equivalent for custom property DIVINATION_DISTANCE
        /*
        ItemModel.Unbaked model = ItemModelUtils.conditional(
                //Predicate for property?
                //ItemModelUtils.rangeSelect(...)
                ItemModelUtils.plainModel(variant0)
        );
        itemModels.itemModelOutput.accept(divinationRodItem, model);
        */
        //Fallback:
//        itemModels.itemModelOutput.accept(divinationRodItem, ItemModelUtils.plainModel(variant0));

        //<= 1.21.3 way:
        //    var name = this.name(divinationRodItem);
        //        this.getBuilder(name)
        //                .parent(variant0)
        //
        //                .override()
        //                .model(variant7)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 0.0f)
        //                .end()
        //
        //                .override()
        //                .model(variant6)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 1.0f)
        //                .end()
        //
        //                .override()
        //                .model(variant5)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 2.0f)
        //                .end()
        //
        //                .override()
        //                .model(variant4)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 3.0f)
        //                .end()
        //
        //                .override()
        //                .model(variant3)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 4.0f)
        //                .end()
        //
        //                .override()
        //                .model(variant2)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 5.0f)
        //                .end()
        //
        //                .override()
        //                .model(variant1)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 6.0f)
        //                .end()
        //
        //                .override()
        //                .model(variant0)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 7.0f)
        //                .end()
        //
        //                .override()
        //                .model(searchingVariant)
        //                .predicate(TheurgyConstants.ItemProperty.DIVINATION_DISTANCE, 8.0f)
        //                .end();
    }

    protected void registerSulfurs(ItemModelGenerators itemModels) {
        SulfurRegistry.SULFURS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalSulfurItem.class::cast).forEach(sulfur -> {
            if (sulfur.useAutomaticIconRendering) {
                //this.registerItemBuiltinEntity(itemModels, sulfur);
                //If it uses automatic rendering, it probably needs a model that supports it, or it is handled by the renderer.
                //If it was "builtin/entity" before, usage:
                itemModels.generateFlatItem(sulfur, ModelTemplates.FLAT_ITEM);
            }
        });
    }

    protected void registerNiters(ItemModelGenerators itemModels) {
        NiterRegistry.NITERS.getEntries().stream().map(DeferredHolder::get).map(AlchemicalNiterItem.class::cast).forEach(niter -> {
            if (niter.useAutomaticIconRendering) {
                 //this.registerItemBuiltinEntity(itemModels, niter);
                 itemModels.generateFlatItem(niter, ModelTemplates.FLAT_ITEM);
            }
        });

        this.registerItemGenerated(itemModels, ItemRegistry.GEMS_ABUNDANT_ICON.get(), "niters/gems_abundant");
        this.registerItemGenerated(itemModels, ItemRegistry.GEMS_COMMON_ICON.get(), "niters/gems_common");
        this.registerItemGenerated(itemModels, ItemRegistry.GEMS_RARE_ICON.get(), "niters/gems_rare");
        this.registerItemGenerated(itemModels, ItemRegistry.GEMS_PRECIOUS_ICON.get(), "niters/gems_precious");

        this.registerItemGenerated(itemModels, ItemRegistry.METALS_ABUNDANT_ICON.get(), "niters/metals_abundant");
        this.registerItemGenerated(itemModels, ItemRegistry.METALS_COMMON_ICON.get(), "niters/metals_common");
        this.registerItemGenerated(itemModels, ItemRegistry.METALS_RARE_ICON.get(), "niters/metals_rare");
        this.registerItemGenerated(itemModels, ItemRegistry.METALS_PRECIOUS_ICON.get(), "niters/metals_precious");

        this.registerItemGenerated(itemModels, ItemRegistry.OTHER_MINERALS_ABUNDANT_ICON.get(), "niters/other_minerals_abundant");
        this.registerItemGenerated(itemModels, ItemRegistry.OTHER_MINERALS_COMMON_ICON.get(), "niters/other_minerals_common");
        this.registerItemGenerated(itemModels, ItemRegistry.OTHER_MINERALS_RARE_ICON.get(), "niters/other_minerals_rare");
        this.registerItemGenerated(itemModels, ItemRegistry.OTHER_MINERALS_PRECIOUS_ICON.get(), "niters/other_minerals_precious");
    }

    protected void registerSalts(ItemModelGenerators itemModels) {
        //this.registerItemGenerated("alchemical_salt"); // Abstract parent not needed if we generate directly

        SaltRegistry.SALTS.getEntries().stream().map(DeferredHolder::get).forEach(salt -> {
            this.registerAlchemicalSalt(itemModels, salt);
        });
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.registerItemGenerated(itemModels, ItemRegistry.THE_HERMETICA_ICON.get(), "the_hermetica");
        this.registerItemGenerated(itemModels, ItemRegistry.EMPTY_JAR_ICON.get(), "empty_jar");
        this.registerItemGenerated(itemModels, ItemRegistry.EMPTY_JAR_IRON_BAND_ICON.get(), "empty_jar_iron_band");
        this.registerItemGenerated(itemModels, ItemRegistry.EMPTY_JAR_LABELED_ICON.get(), "empty_jar_labeled");
        this.registerItemGenerated(itemModels, ItemRegistry.EMPTY_CERAMIC_JAR_ICON.get(), "empty_ceramic_jar");
        this.registerItemGenerated(itemModels, ItemRegistry.EMPTY_CERAMIC_JAR_LABELED_ICON.get(), "empty_ceramic_jar_labeled");
        this.registerItemGenerated(itemModels, ItemRegistry.JAR_LABEL_ICON.get(), "jar_label");
        this.registerItemGenerated(itemModels, ItemRegistry.JAR_LABEL_FRAME_ABUNDANT_ICON.get(), "jar_label_frame_abundant");
        this.registerItemGenerated(itemModels, ItemRegistry.JAR_LABEL_FRAME_COMMON_ICON.get(), "jar_label_frame_common");
        this.registerItemGenerated(itemModels, ItemRegistry.JAR_LABEL_FRAME_RARE_ICON.get(), "jar_label_frame_rare");
        this.registerItemGenerated(itemModels, ItemRegistry.JAR_LABEL_FRAME_PRECIOUS_ICON.get(), "jar_label_frame_precious");

        this.registerSulfurs(itemModels);
        this.registerNiters(itemModels);
        this.registerSalts(itemModels);

        this.registerItemGenerated(itemModels, ItemRegistry.MERCURY_SHARD.get());
        this.registerItemGenerated(itemModels, ItemRegistry.MERCURY_CRYSTAL.get());
        this.registerItemGenerated(itemModels, ItemRegistry.SAL_AMMONIAC_CRYSTAL.get());
        this.registerItemGenerated(itemModels, ItemRegistry.PURIFIED_GOLD.get());
        this.registerItemGenerated(itemModels, ItemRegistry.FERMENTATION_STARTER.get());
        this.registerItemGenerated(itemModels, ItemRegistry.CRYSTALLIZED_WATER.get());
        this.registerItemGenerated(itemModels, ItemRegistry.CRYSTALLIZED_LAVA.get());

        this.registerDivinationRod(itemModels, ItemRegistry.DIVINATION_ROD_T1.get());
        this.registerDivinationRod(itemModels, ItemRegistry.DIVINATION_ROD_T2.get());
        this.registerDivinationRod(itemModels, ItemRegistry.DIVINATION_ROD_T3.get());
        this.registerDivinationRod(itemModels, ItemRegistry.DIVINATION_ROD_T4.get());
        this.registerDivinationRod(itemModels, ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_ABUNDANT.get());
        this.registerDivinationRod(itemModels, ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_COMMON.get());
        this.registerDivinationRod(itemModels, ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_RARE.get());
        this.registerDivinationRod(itemModels, ItemRegistry.SULFUR_ATTUNED_DIVINATION_ROD_PRECIOUS.get());
        this.registerDivinationRod(itemModels, ItemRegistry.AMETHYST_DIVINATION_ROD.get());

        this.registerItemGenerated(itemModels, ItemRegistry.COPPER_WIRE.get());
        this.registerItemHandheld(itemModels, ItemRegistry.MERCURIAL_WAND.get());
        this.registerItemGenerated(itemModels, ItemRegistry.LIST_FILTER.get());
        this.registerItemGenerated(itemModels, ItemRegistry.ATTRIBUTE_FILTER.get());
    }
}
