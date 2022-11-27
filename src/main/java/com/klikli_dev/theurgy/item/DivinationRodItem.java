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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class DivinationRodItem extends Item {

    public static final float NOT_FOUND = 7.0f;
    public static final float SEARCHING = 8.0f;

    protected Supplier<Integer> scanDurationTicks;
    protected Supplier<Integer> scanRange;
    protected Supplier<Integer> durability;
    protected Supplier<Tier> tier;
    protected TagKey<Block> allowedBlocks;

    public DivinationRodItem(Properties pProperties, TagKey<Block> allowedBlocks, Supplier<Tier> tier, Supplier<Integer> durability, Supplier<Integer> scanDurationTicks, Supplier<Integer> scanRange) {
        super(pProperties);
        //TODO: tooltip, maybe also name
        this.allowedBlocks = allowedBlocks;
        this.tier = tier;
        this.durability = durability;
        this.scanDurationTicks = scanDurationTicks;
        this.scanRange = scanRange;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.durability.get();
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
                //TODO: low tier rods are attuned by clicking target block, higher tiers require sulfur and some smart translation logic during crafting

                if (!TierSortingRegistry.isCorrectTierForDrops(this.tier.get(), state)) {
                    if (!level.isClientSide) {
                        player.sendSystemMessage(
                                Component.translatable(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_TIER_TOO_LOW,
                                        this.getBlockComponent(state, pos, level, player)
                                )
                        );
                    }
                    return InteractionResult.FAIL;
                } else if (!state.is(this.allowedBlocks)) {
                    if (!level.isClientSide) {
                        player.sendSystemMessage(
                                Component.translatable(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_NOT_ALLOWED,
                                        this.getBlockComponent(state, pos, level, player)
                                )
                        );
                    }
                    return InteractionResult.FAIL;
                } else {
                    if (!level.isClientSide) {
                        stack.getOrCreateTag().putString(
                                TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID,
                                ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString()
                        );

                        player.sendSystemMessage(
                                Component.translatable(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_LINKED,
                                        this.getBlockComponent(state, pos, level, player)
                                )
                        );
                    }

                    level.playSound(player, player.blockPosition(), SoundRegistry.TUNING_FORK.get(),
                            SoundSource.PLAYERS,
                            1, 1);

                    return InteractionResult.SUCCESS;
                }
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
                    ScanManager.get().beginScan(player, ForgeRegistries.BLOCKS.getValue(id), this.scanRange.get(), this.scanDurationTicks.get());
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

        player.getCooldowns().addCooldown(this, this.scanDurationTicks.get());
        stack.getOrCreateTag().putFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE, NOT_FOUND);
        if (level.isClientSide) {
            BlockPos result = ScanManager.get().finishScan(player);
            float distance = this.getDistance(player.position(), result);
            stack.getTag().putFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE, distance);

            Networking.sendToServer(new MessageSetDivinationResult(result, distance));

            if (result != null) {
                stack.getTag().putLong(TheurgyConstants.Nbt.DIVINATION_POS, result.asLong());
                this.spawnResultParticle(result, (ClientLevel) level, player);
            }
        } else {
            stack.hurtAndBreak(1, player, (entity) -> {
                entity.broadcastBreakEvent(player.getUsedItemHand());
            });
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.scanDurationTicks.get();
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity pLivingEntity, int pTimeCharged) {
        //player interrupted, so we can safely set not found on server
        stack.getOrCreateTag().putFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE, NOT_FOUND);

        if (level.isClientSide) {
            ScanManager.get().cancelScan();

            //re-use old result
            if (stack.hasTag() && stack.getTag().contains(TheurgyConstants.Nbt.DIVINATION_POS)) {
                BlockPos result = BlockPos.of(stack.getTag().getLong(TheurgyConstants.Nbt.DIVINATION_POS));
                this.spawnResultParticle(result, (ClientLevel) level, pLivingEntity);
            }
        }
        super.releaseUsing(stack, level, pLivingEntity, pTimeCharged);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            var tag = pStack.getTag();
            if (tag.contains(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID)) {
                var id = new ResourceLocation(pStack.getTag().getString(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID));
                var blockComponent = this.getBlockComponent(ForgeRegistries.BLOCKS.getValue(id).defaultBlockState(), BlockPos.ZERO, pLevel, Minecraft.getInstance().player);
                pTooltipComponents.add(
                        Component.translatable(
                                TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LINKED_TO,
                                blockComponent
                        ).withStyle(ChatFormatting.GRAY));

                if (tag.contains(TheurgyConstants.Nbt.DIVINATION_POS)) {
                    var pos = BlockPos.of(tag.getLong(TheurgyConstants.Nbt.DIVINATION_POS));
                    pTooltipComponents.add(Component.translatable(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LAST_RESULT,
                            blockComponent,
                            ComponentUtils.wrapInSquareBrackets(Component.literal(pos.toShortString())).withStyle(ChatFormatting.GREEN)
                    ).withStyle(ChatFormatting.GRAY));
                }
            } else {
                pTooltipComponents.add(Component.translatable(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_NO_LINK));
            }

        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
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

    protected MutableComponent getBlockComponent(BlockState block, BlockPos pos, Level level, Player player) {
        var stack = block.getCloneItemStack(BlockHitResult.miss(Vec3.ZERO, Direction.UP, pos), level, pos, player);
        var displayName = Component.empty().append(stack.getHoverName());
        return ComponentUtils.wrapInSquareBrackets(displayName).withStyle(ChatFormatting.GREEN).withStyle((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack))));
    }

    protected void spawnResultParticle(BlockPos result, ClientLevel level, LivingEntity entity) {
        final var visualizationRange = 10.0f;
        var from = new Vec3(entity.getX(), entity.getEyeY() - (double) 0.1F, entity.getZ());
        var resultVec = Vec3.atCenterOf(result);
        var dist = resultVec.subtract(from);
        var dir = dist.normalize();
        var to = dist.length() <= visualizationRange ? resultVec : from.add(dir.scale(visualizationRange));

        if (level.isLoaded(new BlockPos(to)) && level.isLoaded(new BlockPos(from))) {
            FollowProjectile aoeProjectile = new FollowProjectile(level, from, to);
            level.putNonPlayerEntity(aoeProjectile.getId(), aoeProjectile); //client only spawn of entity
        }
    }

    /**
     * Inner class to avoid classloading of client only property functions on server
     */
    public static class PropertyFunctions {
        @SuppressWarnings("deprecation")
        public static ItemPropertyFunction DIVINATION_DISTANCE = (stack, world, entity, i) -> {
            if (!stack.getOrCreateTag().contains(TheurgyConstants.Nbt.DIVINATION_DISTANCE) ||
                    stack.getTag().getFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE) < 0)
                return NOT_FOUND;
            return stack.getTag().getFloat(TheurgyConstants.Nbt.DIVINATION_DISTANCE);
        };
    }
}
