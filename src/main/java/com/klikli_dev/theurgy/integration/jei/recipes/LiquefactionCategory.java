package com.klikli_dev.theurgy.integration.jei.recipes;

//TODO: Enable
//public class LiquefactionCategory implements IRecipeCategory<LiquefactionRecipe> {
//    private final IDrawableAnimated animatedFire;
//    private final IDrawable background;
//    private final IDrawable icon;
//    private final Component localizedName;
//    private final LoadingCache<Integer, IDrawableAnimated> cachedAnimatedArrow;
//
//    public LiquefactionCategory(IGuiHelper guiHelper) {
//        this.background = guiHelper.createBlankDrawable(102, 43);
//
//        this.animatedFire = JeiDrawables.asAnimatedDrawable(guiHelper, GuiTextures.JEI_FIRE_FULL, 300, IDrawableAnimated.StartDirection.TOP, true);
//
//        this.icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.LIQUEFACTION_CAULDRON.get()));
//        this.localizedName = Component.translatable(TheurgyConstants.I18n.JEI.LIQUEFACTION_CATEGORY);
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
//
//    protected IDrawableAnimated getAnimatedArrow(LiquefactionRecipe recipe) {
//        int cookTime = recipe.getLiquefactionTime();
//        if (cookTime <= 0) {
//            cookTime = LiquefactionRecipe.DEFAULT_LIQUEFACTION_TIME;
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
//    public void draw(LiquefactionRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
//        GuiTextures.JEI_FIRE_EMPTY.render(poseStack, 12, 20);
//        this.animatedFire.draw(poseStack, 12, 20);
//
//        GuiTextures.JEI_ARROW_RIGHT_EMPTY.render(poseStack, 45, 8);
//        this.getAnimatedArrow(recipe).draw(poseStack, 45, 8);
//
//        this.drawCookTime(recipe, poseStack, 34);
//    }
//
//    protected void drawCookTime(LiquefactionRecipe recipe, PoseStack poseStack, int y) {
//        int cookTime = recipe.getLiquefactionTime();
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
//    public void setRecipe(IRecipeLayoutBuilder builder, LiquefactionRecipe recipe, IFocusGroup focuses) {
//        builder.addSlot(INPUT, 1, 1)
//                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
//                .addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getSolvent().getFluids()))
//                .addTooltipCallback(addFluidTooltip(recipe.getSolvent().getAmount()));
//
//        builder.addSlot(INPUT, 19, 1)
//                .setBackground(JeiDrawables.INPUT_SLOT, -1, -1)
//                .addIngredients(recipe.getIngredients().get(0));
//        builder.addSlot(OUTPUT, 81, 9)
//                .setBackground(JeiDrawables.OUTPUT_SLOT, -5, -5)
//                .addItemStack(recipe.getResultItem());
//    }
//
//    @Override
//    public RecipeType<LiquefactionRecipe> getRecipeType() {
//        return JeiRecipeTypes.LIQUEFACTION;
//    }
//
//    public static IRecipeSlotTooltipCallback addFluidTooltip(int overrideAmount) {
//        return (view, tooltip) -> {
//            var displayed = view.getDisplayedIngredient(ForgeTypes.FLUID_STACK);
//            if (displayed.isEmpty())
//                return;
//
//            var fluidStack = displayed.get();
//
//            var amount = overrideAmount == -1 ? fluidStack.getAmount() : overrideAmount;
//            var text = Component.translatable(TheurgyConstants.I18n.Misc.UNIT_MILLIBUCKETS, amount).withStyle(ChatFormatting.GOLD);
//            if (tooltip.isEmpty())
//                tooltip.add(0, text);
//            else {
//                List<Component> siblings = tooltip.get(0).getSiblings();
//                siblings.add(Component.literal(" "));
//                siblings.add(text);
//            }
//        };
//    }
//
//}
