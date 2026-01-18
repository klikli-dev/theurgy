// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.accumulation;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.entries.BookContentEntry;
import com.klikli_dev.modonomicon.book.page.BookCraftingRecipePage;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.level.Level;


public class BookAccumulationRecipePage extends BookRecipePage<AccumulationRecipe> {

    public BookAccumulationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookAccumulationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    public static BookAccumulationRecipePage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
        var common = BookRecipePage.commonFromJson(entryId, json, provider);
        return new BookAccumulationRecipePage(common);
    }

    public static BookAccumulationRecipePage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        return new BookAccumulationRecipePage(common);
    }

    @Override
    public ResourceLocation getType() {
        return TheurgyModonomiconConstants.Page.ACCUMULATION_RECIPE;
    }

    @Override
    public void build(Level level, BookContentEntry parentEntry, int pageNum) {
        //copy from parents parent as we won't be calling super.
        this.parentEntry = parentEntry;
        this.pageNumber = pageNum;
        this.book = this.parentEntry.getBook();

        //copy from parent so we can use the fluid name as title, instead of the non existent recipe output.

        this.recipe1 = this.loadRecipe(level, parentEntry, this.recipeId1);
        this.recipe2 = this.loadRecipe(level, parentEntry, this.recipeId2);

        if (this.recipe1 == null && this.recipe2 != null) {
            this.recipe1 = this.recipe2;
            this.recipe2 = null;
        }

        if (this.title1.isEmpty()) {
            //use recipe title if we don't have a custom one
            this.title1 = new BookTextHolder(((MutableComponent)
                    this.recipe1.value().getResult().getHoverName()) //getResult is a FluidStack
                    .withStyle(Style.EMPTY
                            .withBold(true)
                            .withColor(this.getParentEntry().getBook().getDefaultTitleColor())
                    ));
        }

        if (this.recipe2 != null && this.title2.isEmpty()) {
            //use recipe title if we don't have a custom one
            this.title2 = new BookTextHolder(((MutableComponent) this.recipe2.value().getResult().getHoverName()) //getResult is a FluidStack
                    .withStyle(Style.EMPTY
                            .withBold(true)
                            .withColor(this.getParentEntry().getBook().getDefaultTitleColor())
                    ));
        }

        if (this.title1.equals(this.title2)) {
            this.title2 = BookTextHolder.EMPTY;
        }

        //copy from parent so we can use the fluid name as title, instead of the non existent recipe output.


        //if we are on the server we have to load the recipe display info.
        //on the client we already get it in the constructor, sent from the server.
        if (level instanceof ServerLevel serverLevel) {
            this.recipeDisplayEntry1 = this.getRecipeDisplayEntry(serverLevel, this.recipeKey1);
            this.recipeDisplayEntry2 = this.getRecipeDisplayEntry(serverLevel, this.recipeKey2);
        }

        if (this.recipeDisplayEntry1 == null && this.recipeDisplayEntry2 != null) {
            this.recipeDisplayEntry1 = this.recipeDisplayEntry2;
            this.recipeDisplayEntry2 = null;
        }

        if (this.title1.isEmpty()) {
            //use recipe title if we don't have a custom one
            this.title1 = new BookTextHolder(
                    recipeDisplayEntry1.display()
                    ((MutableComponent) this.getRecipeOutput(level, this.recipeDisplayEntry1).getHoverName())
                    .withStyle(Style.EMPTY
                            .withBold(true)
                            .withColor(this.getParentEntry().getBook().getDefaultTitleColor())
                    ));
        }

        if (this.recipeDisplayEntry2 != null && this.title2.isEmpty()) {
            //use recipe title if we don't have a custom one
            this.title2 = new BookTextHolder(((MutableComponent) this.getRecipeOutput(level, this.recipeDisplayEntry2).getHoverName())
                    .withStyle(Style.EMPTY
                            .withBold(true)
                            .withColor(this.getParentEntry().getBook().getDefaultTitleColor())
                    ));
        }

        if (this.title1.equals(this.title2)) {
            this.title2 = BookTextHolder.EMPTY;
        }
    }

    @Override
    protected ItemStack getRecipeOutput(Level level, RecipeDisplayEntry recipeDisplayEntry) {
        return ItemStack.EMPTY;
    }

}
