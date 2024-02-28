// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.accumulation;

import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconConstants;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BookAccumulationRecipePage extends BookRecipePage<AccumulationRecipe> {
    public BookAccumulationRecipePage(BookTextHolder title1, ResourceLocation recipeId1, BookTextHolder title2, ResourceLocation recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
        super(RecipeTypeRegistry.ACCUMULATION.get(), title1, recipeId1, title2, recipeId2, text, anchor, condition);
    }

    public static BookAccumulationRecipePage fromJson(JsonObject json) {
        var common = BookRecipePage.commonFromJson(json);
        var anchor = GsonHelper.getAsString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(json.getAsJsonObject("condition"))
                : new BookNoneCondition();
        return new BookAccumulationRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    public static BookAccumulationRecipePage fromNetwork(FriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        var anchor = buffer.readUtf();
        var condition = BookCondition.fromNetwork(buffer);
        return new BookAccumulationRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    @Override
    public ResourceLocation getType() {
        return TheurgyModonomiconConstants.Page.ACCUMULATION_RECIPE;
    }

    @Override
    public void build(Level level, BookEntry parentEntry, int pageNum) {
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
                    this.recipe1.getResult().getDisplayName())
                    .withStyle(Style.EMPTY
                            .withBold(true)
                            .withColor(this.getParentEntry().getBook().getDefaultTitleColor())
                    ));
        }

        if (this.recipe2 != null && this.title2.isEmpty()) {
            //use recipe title if we don't have a custom one
            this.title2 = new BookTextHolder(((MutableComponent) this.recipe2.getResult().getDisplayName())
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
    protected ItemStack getRecipeOutput(Level level, AccumulationRecipe recipe) {
        return ItemStack.EMPTY;
    }

}
