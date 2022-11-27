/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.item;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.client.scanner.ScanManager;
import com.klikli_dev.theurgy.entity.FollowProjectile;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetDivinationResult;
import com.klikli_dev.theurgy.registry.SoundRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class DivinationRodItem extends Item {

    public static final float NOT_FOUND = 7.0f;
    public static final float SEARCHING = 8.0f;

    @SuppressWarnings("deprecation")
    public static ItemPropertyFunction DIVINATION_DISTANCE = (stack, world, entity, i) -> {
        if (!stack.getOrCreateTag().contains(TheurgyConstants.Nbt.DIVINATION_DISTANCE) || stack.getTag().getFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE) < 0)
            return NOT_FOUND;
        return stack.getTag().getFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE);
    };

    public DivinationRodItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity entityLiving, int count) {
        if (entityLiving.level.isClientSide && entityLiving instanceof Player) {
            ScanManager.get().updateScan((Player) entityLiving, false);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (player.isShiftKeyDown()) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir()) {
                //TODO: low tier rods are attuned by clicking target block, higher tiers require sulfur and some smart translation logic

                Block block = state.getBlock();
                if (!level.isClientSide) {
                    stack.getOrCreateTag().putString(
                            TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID,
                            ForgeRegistries.BLOCKS.getKey(block).toString()
                    );

                    player.sendSystemMessage(
                            Component.translatable(
                                    TheurgyConstants.I18n.Message.DIVINATION_ROD_LINKED,
                                    Component.translatable(block.getDescriptionId())
                            )
                    );
                }

                level.playSound(player, player.blockPosition(), SoundRegistry.TUNING_FORK.get(),
                        SoundSource.PLAYERS,
                        1, 1);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (!player.isShiftKeyDown()) {
            if (stack.getOrCreateTag().contains(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID)) {
                stack.getTag().putFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE, SEARCHING);
                player.startUsingItem(hand);
                level.playSound(player, player.blockPosition(), SoundRegistry.TUNING_FORK.get(), SoundSource.PLAYERS,
                        1, 1);

                if (level.isClientSide) {
                    var id = new ResourceLocation(stack.getTag().getString(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID));
                    //TODO: tier based range here -> should probably come as supplier via constructor, from a config
                    ScanManager.get().beginScan(player, ForgeRegistries.BLOCKS.getValue(id), 96);
                }
            } else if (!level.isClientSide) {
                player.sendSystemMessage(Component.translatable(TheurgyConstants.I18n.Message.DIVINATION_ROD_NO_LINK));
            }
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!(entityLiving instanceof Player player))
            return stack;

        player.getCooldowns().addCooldown(this, 40); //TODO: configurable cooldown?
        stack.getOrCreateTag().putFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE, NOT_FOUND);
        if (level.isClientSide) {
            BlockPos result = ScanManager.get().finishScan(player);
            float distance = this.getDistance(player.position(), result);
            stack.getTag().putFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE, distance);

            Networking.sendToServer(new MessageSetDivinationResult(distance));

            if (result != null) {
                //Show visualization
                 final var visualizationRange = 10.0f;
                    var from =  new Vec3(player.getX(), player.getEyeY() - (double)0.1F, player.getZ());
                    var resultVec = Vec3.atCenterOf(result);
                    var dist = resultVec.subtract(from);
                    var dir = dist.normalize();
                    var to = dist.length() <= visualizationRange ? resultVec : from.add(dir.scale(visualizationRange));


                    if (level.isLoaded(new BlockPos(to)) && level.isLoaded(new BlockPos(from))) {
                        FollowProjectile aoeProjectile = new FollowProjectile(level, from, to);
                        ((ClientLevel) level).putNonPlayerEntity(aoeProjectile.getId(), aoeProjectile);
                    }
            }


        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return ScanManager.SCAN_DURATION_TICKS;
    }


    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity pLivingEntity, int pTimeCharged) {
        //player interrupted, so we can safely set not found on server
        stack.getOrCreateTag().putFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE, NOT_FOUND);

        if (level.isClientSide) {
            ScanManager.get().cancelScan();
        }
        super.releaseUsing(stack, level, pLivingEntity, pTimeCharged);
    }


    /**
     * Calculates the distance parameter representing the actual distance.
     *
     * @param playerPosition the player position.
     * @param result         the result position to get the distance to.
     * @return the distance parameter as used in the distance property, not the actual distance.
     */
    public float getDistance(Vec3 playerPosition, BlockPos result) {
        if (result == null)
            return NOT_FOUND;

        Vec3 resultCenter = Vec3.atCenterOf(result);
        Vec3 playerPosition2d = new Vec3(playerPosition.x, 0, playerPosition.z);
        Vec3 resultCenter2d = new Vec3(resultCenter.x, 0, resultCenter.z);
        double distance = playerPosition2d.distanceTo(resultCenter2d);

        if (distance < 6.0)
            return 0.0f;
        if (distance < 15.0)
            return 1.0f;
        if (distance < 25.0)
            return 2.0f;
        if (distance < 35.0)
            return 3.0f;
        if (distance < 45)
            return 4.0f;
        if (distance < 65)
            return 5.0f;
        return 6.0f;
    }
}
