// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.item.niter.AlchemicalNiterItem;
import com.klikli_dev.theurgy.content.item.renderer.DivinationDistanceProperty;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.registry.*;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Optional;

public class TheurgyItemModelSubProvider {

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
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel( ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(Theurgy.loc("item/" + texture)), itemModels.modelOutput)));

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

        //Main item model with range select
        ItemModel.Unbaked model = ItemModelUtils.rangeSelect(
                new DivinationDistanceProperty(),
                ItemModelUtils.plainModel(variant7), // Fallback
                new RangeSelectItemModel.Entry(0.0f, ItemModelUtils.plainModel(variant7)),
                new RangeSelectItemModel.Entry(1.0f, ItemModelUtils.plainModel(variant6)),
                new RangeSelectItemModel.Entry(2.0f, ItemModelUtils.plainModel(variant5)),
                new RangeSelectItemModel.Entry(3.0f, ItemModelUtils.plainModel(variant4)),
                new RangeSelectItemModel.Entry(4.0f, ItemModelUtils.plainModel(variant3)),
                new RangeSelectItemModel.Entry(5.0f, ItemModelUtils.plainModel(variant2)),
                new RangeSelectItemModel.Entry(6.0f, ItemModelUtils.plainModel(variant1)),
                new RangeSelectItemModel.Entry(7.0f, ItemModelUtils.plainModel(variant0)),
                new RangeSelectItemModel.Entry(8.0f, ItemModelUtils.plainModel(searchingVariant))
        );

        itemModels.itemModelOutput.accept(divinationRodItem, model);
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

    protected void registerSalAmmoniacBucket(ItemModelGenerators itemModels){
        itemModels.itemModelOutput.accept(
                ItemRegistry.SAL_AMMONIAC_BUCKET.get(),
                new DynamicFluidContainerModel.Unbaked(
                        // The textures used to construct the container
                        // These are in reference to the block atlas, so they are relative to the `textures` directory
                        new DynamicFluidContainerModel.Textures(
                                // Sets the model particle sprite
                                // If not set, uses the first texture that is not null:
                                // - Fluid still texture
                                // - Container base texture
                                // - Container cover texture, if not used as a mask
                                // Points to 'assets/minecraft/textures/item/bucket.png'
                                Optional.of(ResourceLocation.withDefaultNamespace("item/bucket")),
                                // Sets the texture to use on the first layer, generally the container of the fluid
                                // If not set, the layer will not be added
                                // Points to 'assets/minecraft/textures/item/bucket.png'
                                Optional.of(ResourceLocation.withDefaultNamespace("item/bucket")),
                                // Sets the texture to use as the mask for the still fluid texture
                                // Areas where the fluid is seen should be pure white
                                // If not set or the fluid is empty, then the layer is not rendered
                                // Points to 'assets/neoforge/textures/item/mask/bucket_fluid.png'
                                Optional.of(ResourceLocation.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid")),
                                // Sets the texture to use as either
                                // - The overlay texture when 'cover_is_mask' is false
                                // - The mask to apply to the base texture (should be pure white to see) when 'cover_is_mask' is true
                                // If not set or no base texture is set when 'cover_is_mask' is true, then the layer is not rendered
                                // Points to 'assets/neoforge/textures/item/mask/bucket_fluid_cover.png'
                                Optional.of(ResourceLocation.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid_cover"))
                        ),
                        FluidRegistry.SAL_AMMONIAC.get(),
                        // When true, rotates the model 180 degrees
                        // Defaults to false
                        false,
                        // When true, uses the cover texture as a mask for the base texture
                        // Defaults to true
                        true,
                        // When true, sets the lightmap of the fluid texture layer to its max value
                        // Defaults to true
                        true
                )
        );
    }

    public void registerModels(ItemModelGenerators itemModels) {
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

        this.registerSalAmmoniacBucket(itemModels);
    }
}

