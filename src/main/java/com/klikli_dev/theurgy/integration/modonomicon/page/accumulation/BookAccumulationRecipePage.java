// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.integration.modonomicon.page.accumulation;

import com.klikli_dev.modonomicon.Modonomicon;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.entries.BookContentEntry;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import com.klikli_dev.modonomicon.data.BookPageType;
import com.klikli_dev.theurgy.content.recipe.AccumulationRecipe;
import com.klikli_dev.theurgy.content.recipe.display.AccumulationRecipeDisplay;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.integration.modonomicon.TheurgyModonomiconPageTypeRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.level.Level;

import java.util.ArrayList;


public class BookAccumulationRecipePage extends BookRecipePage<AccumulationRecipe> {
    public static final Identifier ID = Theurgy.loc("accumulation_recipe");
    public static final MapCodec<BookAccumulationRecipePage> CODEC = codec(BookAccumulationRecipePage::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, BookAccumulationRecipePage> STREAM_CODEC = streamCodec(BookAccumulationRecipePage::new);

    public BookAccumulationRecipePage(JsonDataHolder common) {
        super(common);
    }

    public BookAccumulationRecipePage(NetworkDataHolder common) {
        super(common);
    }

    @Override
    public BookPageType<?> type() {
        return TheurgyModonomiconPageTypeRegistry.ACCUMULATION_RECIPE;
    }

    @Override
    public void build(Level level, BookContentEntry parentEntry, int pageNum) {
        //copy from parents parent as we won't be calling super.
        this.parentEntry = parentEntry;
        this.pageNumber = pageNum;
        this.book = this.parentEntry.getBook();

        //copy from parent and modify so we can use the fluid name as title, instead of the non existent recipe output.

        if (level instanceof ServerLevel serverLevel) {
            this.recipeDisplayEntry1 = this.getRecipeDisplayEntryOrNull(serverLevel, this.recipeKey1);
            this.recipeDisplayEntry2 = this.getRecipeDisplayEntryOrNull(serverLevel, this.recipeKey2);
        }

        if (this.recipeDisplayEntry1 == null && this.recipeDisplayEntry2 != null) {
            this.recipeDisplayEntry1 = this.recipeDisplayEntry2;
            this.recipeDisplayEntry2 = null;
        }

        if (this.title1.isEmpty()) {
            //use recipe title if we don't have a custom one
            if (this.recipeDisplayEntry1.display() instanceof AccumulationRecipeDisplay display) {
                this.title1 = new BookTextHolder((display.resultFluidStack().getHoverName().copy())
                        .withStyle(Style.EMPTY
                                .withBold(true)
                                .withColor(this.getParentEntry().getBook().theme().palette().defaultTitleColor())
                        ));
            }
        }

        if (this.recipeDisplayEntry2 != null && this.title2.isEmpty()) {
            //use recipe title if we don't have a custom one
            if (this.recipeDisplayEntry1.display() instanceof AccumulationRecipeDisplay display) {
                this.title2 = new BookTextHolder((display.resultFluidStack().getHoverName().copy())
                        .withStyle(Style.EMPTY
                                .withBold(true)
                                .withColor(this.getParentEntry().getBook().theme().palette().defaultTitleColor())
                        ));
            }
        }

        if (this.title1.equals(this.title2)) {
            this.title2 = BookTextHolder.EMPTY;
        }
    }

    @Override
    protected ItemStack getRecipeOutput(Level level, RecipeDisplayEntry recipeDisplayEntry) {
        return ItemStack.EMPTY;
    }

    private RecipeDisplayEntry getRecipeDisplayEntryOrNull(ServerLevel serverLevel, ResourceKey<Recipe<?>> key) {
        if (key == null) {
            return null;
        }

        var entries = new ArrayList<RecipeDisplayEntry>();
        serverLevel.recipeAccess().listDisplaysForRecipe(key, entries::add);
        var entry = entries.stream().findFirst().orElse(null);

        if (entry == null) {
            Modonomicon.LOG.warn("Recipe {} not found.", key);
        }

        return entry;
    }

}
