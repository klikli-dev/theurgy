package com.klikli_dev.theurgy.integration.jei.recipes;

//TODO: Enable
//public class DistillationCategory implements IRecipeCategory<DistillationRecipe> {
//    private final IDrawableAnimated animatedFire;
//    private final IDrawable background;
//    private final IDrawable icon;
//    private final Component localizedName;
//    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;
//
//    public DistillationCategory(IGuiHelper guiHelper) {
//        this.background = guiHelper.createBlankDrawable(82, 43);
//
//        this.animatedFire = JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_FIRE_FULL, 300, IDrawableAnimated.StartDirection.TOP, true);
//
//        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.DISTILLER.get()));
//        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.DISTILLATION_CATEGORY);
//
//        //We need different animations for different cook times, hence the cache
//        this.cachedAnimatedArrow = CacheBuilder.newBuilder()
//                .maximumSize(25)
//                .build(new CacheLoader<>() {
//                    @Override
//                    public IDrawableAnimated load(Integer cookTime) {
//                        return JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_ARROW_RIGHT_FULL, cookTime, IDrawableAnimated.StartDirection.LEFT, false);
//                    }
//                });
//    }
//
//    protected IDrawableAnimated getAnimatedArrow(DistillationRecipe recipe) {
//        int cookTime = recipe.getDistillationTime();
//        if (cookTime <= 0) {
//            cookTime = DistillationRecipe.DEFAULT_DISTILLATION_TIME;
//        }
//        return this.cachedAnimatedArrow.getUnchecked(cookTime);
//    }
//
//    @Override
//    public IDrawable getBackground() {
//        return this.background;
//    }
//
//    @Override
//    public IDrawable getIcon() {
//        return this.icon;
//    }
//
//    @Override
//    public void draw(DistillationRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
//        GuiTextures.JEI_FIRE_EMPTY.render(poseStack, 1, 20);
//        this.animatedFire.draw(poseStack, 1, 20);
//
//        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(poseStack, 24, 8);
//        this.getAnimatedArrow(recipe).draw(poseStack, 24, 8);
//
//        this.drawCookTime(recipe, poseStack, 34);
//    }
//
//    protected void drawCookTime(DistillationRecipe recipe, PoseStack poseStack, int y) {
//        int cookTime = recipe.getDistillationTime();
//        if (cookTime > 0) {
//            int cookTimeSeconds = cookTime / 20;
//            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
//            Minecraft minecraft = Minecraft.getInstance();
//            Font fontRenderer = minecraft.font;
//            int stringWidth = fontRenderer.width(timeString);
//            fontRenderer.draw(poseStack, timeString, this.background.getWidth() - stringWidth, y, 0xFF808080);
//        }
//    }
//
//    @Override
//    public Component getTitle() {
//        return this.localizedName;
//    }
//
//    @Override
//    public void setRecipe(IRecipeLayoutBuilder builder, DistillationRecipe recipe, IFocusGroup focuses) {
//        builder.addSlot(INPUT, 1, 1)
//                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
//                .addIngredients(VanillaTypes.ITEM_STACK, Arrays.stream(recipe.getIngredient().getItems()).map(i -> i.copyWithCount(recipe.getIngredientCount())).toList());
//
//        builder.addSlot(OUTPUT, 61, 9)
//                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
//                .addItemStack(recipe.getResultItem());
//    }
//
//    @Override
//    public RecipeType<DistillationRecipe> getRecipeType() {
//        return JeiRecipeTypes.DISTILLATION;
//    }
//}
