/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.common.item.tool;

import com.github.klikli_dev.theurgy.common.handlers.ClientRenderEventHandler;
import com.github.klikli_dev.theurgy.common.network.MessageEssentiaChunkData;
import com.github.klikli_dev.theurgy.common.network.Packets;
import com.github.klikli_dev.theurgy.common.theurgy.essentia_chunks.EssentiaChunkHandler;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class EssentiaGaugeItem extends Item {
    //region Fields
    public static final float NOT_FOUND = 0.0f;
    public static final float SEARCHING = 1.0f;
    public static final String PROPERTY_TAG_NAME = "is_searching";
    public static final int USE_DURATION_TICKS = 400; //200 Ticks = 20 Sec
    //endregion Fields

    //region Initialization
    public EssentiaGaugeItem(Item.Properties properties) {
        super(properties);
    }
    //endregion Initialization

    //region Overrides

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!player.isSneaking()) {
            stack.getOrCreateTag().putFloat(PROPERTY_TAG_NAME, SEARCHING);
            player.setActiveHand(hand);

            if (!world.isRemote) {
                Packets.sendTo((ServerPlayerEntity) player, new MessageEssentiaChunkData(
                        EssentiaChunkHandler.getEssentiaCache(
                                world.getDimensionKey(),
                                new ChunkPos(player.getPosition())).essentia
                ));
            }
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
        if (!(entityLiving instanceof PlayerEntity))
            return stack;

        stack.getOrCreateTag().putFloat(PROPERTY_TAG_NAME, NOT_FOUND);
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION_TICKS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        //player interrupted, so we can reset to not found.
        stack.getOrCreateTag().putFloat(EssentiaGaugeItem.PROPERTY_TAG_NAME, NOT_FOUND);
        if(world.isRemote){
            //Stop rendering when item use is stopped early
            ClientRenderEventHandler.displayChunkEssentia = false;
        }
        super.onPlayerStoppedUsing(stack, world, entityLiving, timeLeft);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".tooltip"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
    //endregion Overrides
}
