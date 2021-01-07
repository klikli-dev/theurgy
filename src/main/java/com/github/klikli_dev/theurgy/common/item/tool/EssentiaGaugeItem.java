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

import com.github.klikli_dev.theurgy.client.particle.GlowingBallParticleData;
import com.github.klikli_dev.theurgy.client.particle.SparkleParticle;
import com.github.klikli_dev.theurgy.client.particle.SparkleParticleData;
import com.github.klikli_dev.theurgy.common.handlers.ClientRenderEventHandler;
import com.github.klikli_dev.theurgy.common.network.MessageEssentiaChunkData;
import com.github.klikli_dev.theurgy.common.network.Packets;
import com.github.klikli_dev.theurgy.common.theurgy.essentia_chunks.EssentiaChunkHandler;
import com.github.klikli_dev.theurgy.common.tile.IEssentiaEmitter;
import com.github.klikli_dev.theurgy.common.tile.IEssentiaReceiver;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        CompoundNBT compound = stack.getOrCreateTag();
        World world = context.getWorld();
        BlockPos pos = context.getPos();

        if (context.getPlayer().isSneaking()) {
            if (compound.contains("target")) {
                RegistryKey<World> dimensionKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY,
                        new ResourceLocation(compound.getString("targetDimensionKey")));
                if (world.getDimensionKey() != dimensionKey)
                    return ActionResultType.FAIL;

                TileEntity tile = world.getTileEntity(pos);
                BlockPos targetPos = BlockPos.fromLong(compound.getLong("target"));
                //TODO: Check link type here to link correctly to aether / essentia stuff -> or use separate tool
                if (tile instanceof IEssentiaEmitter) {
                    TileEntity targetTile = world.getTileEntity(targetPos);
                    if (targetTile instanceof IEssentiaReceiver) {
                        ((IEssentiaEmitter) tile).setTarget(Optional.of(targetPos));
                        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f,
                                1.9f + world.rand.nextFloat() * 0.2f);
                        return ActionResultType.SUCCESS;
                    }
                }

                compound.remove("targetDimensionKey");
                compound.remove("target");
                compound.remove("linkType");
            }
            else {
                compound.putString("targetDimensionKey", context.getWorld().getDimensionKey().getLocation().toString());
                compound.putLong("target", pos.toLong());
                compound.putInt("linkType", 0); //0 for essentia, 1 for future aether
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f,
                        1.9f + world.rand.nextFloat() * 0.2f);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        //TODO: remove debug code
        if(world.isRemote){
            BlockPos pos = player.getPosition();
            for(int i = 0; i < 5; i++){
                    SparkleParticleData data = new SparkleParticleData(
                            0.2f, 0.6f + world.rand.nextFloat() * 0.3f, 0.2f,
                            pos.getX() + 20, pos.getY() + 1, pos.getZ());

                    world.addParticle(data, pos.getX() + world.rand.nextFloat(), pos.getY() + world.rand.nextFloat(), pos.getZ() + world.rand.nextFloat(), 0, 0, 0);
            }
        }

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
        if (world.isRemote) {
            //Stop rendering when item use is stopped early
            ClientRenderEventHandler.displayChunkEssentia = false;
        }
        super.onPlayerStoppedUsing(stack, world, entityLiving, timeLeft);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
                               ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".tooltip"));
        CompoundNBT compound = stack.getOrCreateTag();
        if (compound.contains("target")) {
            RegistryKey<World> dimensionKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY,
                    new ResourceLocation(compound.getString("targetDimensionKey")));

            if (worldIn.getDimensionKey() == dimensionKey) {
                BlockPos pos = BlockPos.fromLong(compound.getLong("target"));
                BlockState blockState = worldIn.getBlockState(pos);
                tooltip.add(new TranslationTextComponent(
                        this.getTranslationKey() + ".tooltip.target_block",
                        blockState.getBlock().getTranslatedName().mergeStyle(TextFormatting.BOLD)));
                tooltip.add(new StringTextComponent(" X=" + pos.getX()));
                tooltip.add(new StringTextComponent(" Y=" + pos.getY()));
                tooltip.add(new StringTextComponent(" Z=" + pos.getZ()));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
    //endregion Overrides
}
