package com.klikli_dev.theurgy.integration.jei.recipes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class CalcinationCategory implements IRecipeCategory<CalcinationRecipe> {
    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;
    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public CalcinationCategory(IGuiHelper guiHelper) {
        this.staticFlame = guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 82, 114, 14, 14);
        this.animatedFlame = guiHelper.createAnimatedDrawable(this.staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        //TODO: update background to remove fuel slot
        this.background = guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 0, 114, 82, 54);
        this.regularCookTime = 200;
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.CALCINATION_OVEN.get()));
        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.CALCINATION_CATEGORY);
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }


    protected IDrawableAnimated getArrow(CalcinationRecipe recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = this.regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(CalcinationRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        this.animatedFlame.draw(poseStack, 1, 20);

        IDrawableAnimated arrow = this.getArrow(recipe);
        arrow.draw(poseStack, 24, 18);

        this.drawCookTime(recipe, poseStack, 45);
    }

    protected void drawCookTime(CalcinationRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CalcinationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(OUTPUT, 61, 19)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public RecipeType<CalcinationRecipe> getRecipeType() {
        return JeiRecipeTypes.CALCINATION;
    }
}
