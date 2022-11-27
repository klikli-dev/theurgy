/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.item;

import com.google.common.collect.ImmutableList;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.client.render.SulfurBEWLR;
import com.klikli_dev.theurgy.entity.FollowProjectile;
import com.klikli_dev.theurgy.registry.EntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Consumer;

public class AlchemicalSulfurItem extends Item {
    public AlchemicalSulfurItem(Properties pProperties) {
        super(pProperties);
    }

    public static ItemStack getSourceStack(ItemStack sulfurStack) {
        if (sulfurStack.hasTag() && sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_ID)) {
            var itemId = new ResourceLocation(sulfurStack.getTag().getString(TheurgyConstants.Nbt.SULFUR_SOURCE_ID));
            var sourceStack = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId));

            if (sulfurStack.getTag().contains(TheurgyConstants.Nbt.SULFUR_SOURCE_NBT))
                sourceStack.setTag(sulfurStack.getTag().getCompound(TheurgyConstants.Nbt.SULFUR_SOURCE_NBT));
            return sourceStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return SulfurBEWLR.get();
            }
        });
    }

    @Override
    public Component getName(ItemStack pStack) {
        var source = getSourceStack(pStack);

        if (!source.isEmpty() && source.getHoverName() instanceof MutableComponent hoverName)
            return Component.translatable(this.getDescriptionId(), ComponentUtils.wrapInSquareBrackets(
                    hoverName.withStyle(Style.EMPTY
                    .withColor(ChatFormatting.GREEN)
                    .withItalic(true)
            )));

        return super.getName(pStack);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            DistHelper.fillItemCategory(this, tab, items);
        }
    }

    public static List<MutableComponent> getTooltipData(ItemStack sulfurStack) {
        var source = getSourceStack(sulfurStack);

        if (!source.isEmpty() && source.getHoverName() instanceof MutableComponent hoverName)
            return ImmutableList.of(hoverName.withStyle(Style.EMPTY
                            .withColor(ChatFormatting.GREEN)
                            .withItalic(true)
                    ));

        return ImmutableList.of();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if(pLevel.isClientSide && pLevel instanceof ClientLevel clientLevel){
            var from =  new Vec3(pPlayer.getX(), pPlayer.getEyeY() - (double)0.1F, pPlayer.getZ());
            var look = pPlayer.getLookAngle().normalize();
            var to = from.add(look.scale(10));


            if (pLevel.isLoaded(new BlockPos(to)) && pLevel.isLoaded(new BlockPos(from))) {
                FollowProjectile aoeProjectile = new FollowProjectile(pLevel, from, to);
                clientLevel.putNonPlayerEntity(aoeProjectile.getId(), aoeProjectile);
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static class DistHelper {
        public static void fillItemCategory(AlchemicalSulfurItem item, CreativeModeTab tab, NonNullList<ItemStack> items) {
            var level = Minecraft.getInstance().level;
            if (level != null) {
                var recipeManager = level.getRecipeManager();
                recipeManager.getRecipes().forEach((recipe) -> {
                    if (recipe.getResultItem().getItem() == item) {
                        items.add(recipe.getResultItem().copy());
                    }
                });
            }
        }
    }
}
