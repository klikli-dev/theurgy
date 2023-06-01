/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.item;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.client.scanner.ScanManager;
import com.klikli_dev.theurgy.entity.FollowProjectile;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetDivinationResult;
import com.klikli_dev.theurgy.registry.SoundRegistry;
import com.klikli_dev.theurgy.registry.TagRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DivinationRodItem extends Item {

    public static final float NOT_FOUND = 7.0f;
    public static final float SEARCHING = 8.0f;

    public Tier defaultTier;
    public TagKey<Block> defaultAllowedBlocksTag;
    public TagKey<Block> defaultDisallowedBlocksTag;

    public int defaultRange;
    public int defaultDuration;
    public int defaultDurability;
    public boolean defaultAllowAttuning;

    public DivinationRodItem(Properties pProperties, Tier defaultTier, TagKey<Block> defaultAllowedBlocksTag, TagKey<Block> defaultDisallowedBlocksTag, int defaultRange, int defaultDuration, int defaultDurability, boolean defaultAllowAttuning) {
        super(pProperties);
        this.defaultTier = defaultTier;
        this.defaultAllowedBlocksTag = defaultAllowedBlocksTag;
        this.defaultDisallowedBlocksTag = defaultDisallowedBlocksTag;
        this.defaultRange = defaultRange;
        this.defaultDuration = defaultDuration;
        this.defaultDurability = defaultDurability;
        this.defaultAllowAttuning = defaultAllowAttuning;
    }

    public static Set<Block> getScanTargetsForId(ResourceLocation linkedBlockId) {
        //First: try to get a tag for the given block.
        var tagKey = TagKey.create(Registry.BLOCK_REGISTRY, getOreTagFromBlockId(linkedBlockId));
        var tag = ForgeRegistries.BLOCKS.tags().getTag(tagKey);

        if (!tag.isEmpty())
            return tag.stream().collect(Collectors.toSet());

        //If no fitting tag succeeds, try to get block + deepslate variant
        var block = ForgeRegistries.BLOCKS.getValue(linkedBlockId);
        if (block != null) {
            Block deepslateBlock = null;

            //also search for deepslate ores
            if (linkedBlockId.getPath().contains("_ore") && !linkedBlockId.getPath().contains("deepslate_")) {
                var deepslateId = new ResourceLocation(linkedBlockId.getNamespace(), "deepslate_" + linkedBlockId.getPath());
                deepslateBlock = ForgeRegistries.BLOCKS.getValue(deepslateId);
            }

            //finally, only add deepslate variant, if it is not air
            return deepslateBlock != null && deepslateBlock != Blocks.AIR ? Set.of(block, deepslateBlock) : Set.of(block);
        }

        return Set.of();
    }

    public static ResourceLocation getOreTagFromBlockId(ResourceLocation blockId) {
        var path = blockId.getPath();

        String oreName = path
                .replace("_ore", "")
                .replace("ore_", "")
                .replace("_deepslate", "")
                .replace("deepslate_", "");

        return new ResourceLocation("forge:ores/" + oreName);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return stack.getOrCreateTag().getInt(TheurgyConstants.Nbt.Divination.SETTING_DURABILITY);
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

        var tier = this.getMiningTier(stack);
        var allowedBlocksTag = this.getAllowedBlocksTag(stack);
        var disallowedBlocksTag = this.getDisallowedBlocksTag(stack);

        if (player.isShiftKeyDown()) {

            if (!stack.getOrCreateTag().getBoolean(TheurgyConstants.Nbt.Divination.SETTING_ALLOW_ATTUNING)) {
                if (!level.isClientSide) {
                    player.sendMessage(
                            new TranslatableComponent(TheurgyConstants.I18n.Message.DIVINATION_ROD_ATTUNING_NOT_ALLOWED),
                            Util.NIL_UUID
                    );
                }
                return InteractionResult.FAIL;
            }

            BlockState state = level.getBlockState(pos);
            if (!state.isAir()) {
                if (!TierSortingRegistry.isCorrectTierForDrops(tier, state)) {
                    if (!level.isClientSide) {
                        player.sendMessage(
                                new TranslatableComponent(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_TIER_TOO_LOW,
                                        this.getBlockDisplayComponent(state.getBlock())
                                ),
                                Util.NIL_UUID
                        );
                    }
                    return InteractionResult.FAIL;
                } else if (!state.is(allowedBlocksTag)) {
                    if (!level.isClientSide) {
                        player.sendMessage(
                                new TranslatableComponent(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_NOT_ALLOWED,
                                        this.getBlockDisplayComponent(state.getBlock())
                                ),
                                Util.NIL_UUID
                        );
                    }
                    return InteractionResult.FAIL;
                } else if (state.is(disallowedBlocksTag)) {
                    if (!level.isClientSide) {
                        player.sendMessage(
                                new TranslatableComponent(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_DISALLOWED,
                                        this.getBlockDisplayComponent(state.getBlock())
                                ),
                                Util.NIL_UUID
                        );
                    }
                    return InteractionResult.FAIL;
                } else {
                    if (!level.isClientSide) {
                        stack.getOrCreateTag().putString(
                                TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID,
                                ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString()
                        );

                        player.sendMessage(
                                new TranslatableComponent(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_LINKED,
                                        this.getBlockDisplayComponent(state.getBlock())
                                ),
                                Util.NIL_UUID
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
            if (stack.getOrCreateTag().contains(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID)) {
                var tag = stack.getTag();
                tag.putFloat(TheurgyConstants.Nbt.Divination.DISTANCE, SEARCHING);
                player.startUsingItem(hand);
                level.playSound(player, player.blockPosition(), SoundRegistry.TUNING_FORK.get(), SoundSource.PLAYERS,
                        1, 1);

                if (level.isClientSide) {
                    var id = new ResourceLocation(stack.getTag().getString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID));

                    var blocks = getScanTargetsForId(id);
                    ScanManager.get().beginScan(player,
                            blocks,
                            tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_RANGE),
                            tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_DURATION)
                    );
                }
            } else if (!level.isClientSide) {
                player.sendMessage(
                        new TranslatableComponent(TheurgyConstants.I18n.Message.DIVINATION_ROD_NO_LINK),
                        Util.NIL_UUID);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        if (!(entityLiving instanceof Player player))
            return stack;

        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            //if in the last usage cycle the item was used up, we now actually break it to avoid over-use
            player.broadcastBreakEvent(player.getUsedItemHand());
            var item = stack.getItem();
            stack.shrink(1);
            player.awardStat(Stats.ITEM_BROKEN.get(item));
            stack.setDamageValue(0);
            return stack;
        }

        player.getCooldowns().addCooldown(this, stack.getOrCreateTag().getInt(TheurgyConstants.Nbt.Divination.SETTING_DURATION));
        stack.getOrCreateTag().putFloat(TheurgyConstants.Nbt.Divination.DISTANCE, NOT_FOUND);
        if (level.isClientSide) {
            BlockPos result = ScanManager.get().finishScan(player);
            float distance = this.getDistance(player.position(), result);
            stack.getTag().putFloat(TheurgyConstants.Nbt.Divination.DISTANCE, distance);

            Networking.sendToServer(new MessageSetDivinationResult(result, distance));

            if (result != null) {
                stack.getTag().putLong(TheurgyConstants.Nbt.Divination.POS, result.asLong());
                this.spawnResultParticle(result, level, player);
            }
        } else {
            //only hurt, but do not break -> this allows using the rod without breaking it when we just re-use a saved result.
            //we break it at the beginning of this method if we are at >= max damage.
            stack.hurt(1, player.getRandom(), null);
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return stack.getOrCreateTag().getInt(TheurgyConstants.Nbt.Divination.SETTING_DURATION);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity pLivingEntity, int pTimeCharged) {
        if (!stack.getOrCreateTag().contains(TheurgyConstants.Nbt.Divination.POS))
            //player interrupted, so we can safely set not found on server, if we don't have a previous result
            stack.getOrCreateTag().putFloat(TheurgyConstants.Nbt.Divination.DISTANCE, NOT_FOUND);
        else {
            //otherwise, restore distance from result
            //nice bonus: will update crystal status on every "display only" use.
            BlockPos result = BlockPos.of(stack.getTag().getLong(TheurgyConstants.Nbt.Divination.POS));
            float distance = this.getDistance(pLivingEntity.position(), result);
            stack.getTag().putFloat(TheurgyConstants.Nbt.Divination.DISTANCE, distance);
        }


        if (level.isClientSide) {
            ScanManager.get().cancelScan();

            //re-use old result
            if (stack.getTag().contains(TheurgyConstants.Nbt.Divination.POS)) {
                BlockPos result = BlockPos.of(stack.getTag().getLong(TheurgyConstants.Nbt.Divination.POS));
                this.spawnResultParticle(result, level, pLivingEntity);
            }
        }
        super.releaseUsing(stack, level, pLivingEntity, pTimeCharged);
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (pStack.hasTag()) {
            var tag = pStack.getTag();
            if (tag.contains(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID)) {
                var id = new ResourceLocation(pStack.getTag().getString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID));
                var block = ForgeRegistries.BLOCKS.getValue(id);
                if (block != null) {
                    //we''re not using getBlockDisplayComponent because we want custom formatting
                    var blockComponent = ComponentUtils.wrapInSquareBrackets(
                                    block.getName().withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withItalic(true))
                            )
                            .withStyle((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(new ItemStack(block)))));
                    return new TranslatableComponent(this.getDescriptionId() + ".linked", blockComponent);
                }
            }
        }

        return super.getName(pStack);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        var tag = stack.getOrCreateTag();

        //fill in any nbt that is not provided by the recipe with default values
        if (!tag.contains(TheurgyConstants.Nbt.Divination.SETTING_TIER))
            tag.putString(TheurgyConstants.Nbt.Divination.SETTING_TIER, TierSortingRegistry.getName(this.defaultTier).toString());

        if (!tag.contains(TheurgyConstants.Nbt.Divination.SETTING_ALLOWED_BLOCKS_TAG))
            tag.putString(TheurgyConstants.Nbt.Divination.SETTING_ALLOWED_BLOCKS_TAG, this.defaultAllowedBlocksTag.location().toString());

        if (!tag.contains(TheurgyConstants.Nbt.Divination.SETTING_DISALLOWED_BLOCKS_TAG))
            tag.putString(TheurgyConstants.Nbt.Divination.SETTING_DISALLOWED_BLOCKS_TAG, this.defaultDisallowedBlocksTag.location().toString());

        if (!tag.contains(TheurgyConstants.Nbt.Divination.SETTING_RANGE))
            tag.putInt(TheurgyConstants.Nbt.Divination.SETTING_RANGE, this.defaultRange);

        if (!tag.contains(TheurgyConstants.Nbt.Divination.SETTING_DURATION))
            tag.putInt(TheurgyConstants.Nbt.Divination.SETTING_DURATION, this.defaultDuration);

        if (!tag.contains(TheurgyConstants.Nbt.Divination.SETTING_DURABILITY))
            tag.putInt(TheurgyConstants.Nbt.Divination.SETTING_DURABILITY, this.defaultDurability);

        if (!tag.contains(TheurgyConstants.Nbt.Divination.SETTING_ALLOW_ATTUNING))
            tag.putBoolean(TheurgyConstants.Nbt.Divination.SETTING_ALLOW_ATTUNING, this.defaultAllowAttuning);

        return super.initCapabilities(stack, nbt);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            var tag = pStack.getTag();
            if (tag.contains(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID)) {
                var id = new ResourceLocation(pStack.getTag().getString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID));
                var block = ForgeRegistries.BLOCKS.getValue(id);
                if (block != null) {
                    var blockComponent = this.getBlockDisplayComponent(block);
                    pTooltipComponents.add(
                            new TranslatableComponent(
                                    TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LINKED_TO,
                                    blockComponent
                            ).withStyle(ChatFormatting.GRAY));

                    if (tag.contains(TheurgyConstants.Nbt.Divination.POS)) {
                        var pos = BlockPos.of(tag.getLong(TheurgyConstants.Nbt.Divination.POS));
                        pTooltipComponents.add(new TranslatableComponent(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LAST_RESULT,
                                blockComponent,
                                ComponentUtils.wrapInSquareBrackets(new TextComponent(pos.toShortString())).withStyle(ChatFormatting.GREEN)
                        ).withStyle(ChatFormatting.GRAY));
                    }
                }

            } else {
                pTooltipComponents.add(new TranslatableComponent(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_NO_LINK));
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

    protected MutableComponent getBlockDisplayComponent(Block block) {
        var displayName = block.getName();
        return ComponentUtils.wrapInSquareBrackets(displayName)
                .withStyle(ChatFormatting.GREEN)
                .withStyle((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(new ItemStack(block)))));
    }

    protected void spawnResultParticle(BlockPos result, Level level, LivingEntity entity) {
        final var visualizationRange = 10.0f;
        var from = new Vec3(entity.getX(), entity.getEyeY() - (double) 0.1F, entity.getZ());
        var resultVec = Vec3.atCenterOf(result);
        var dist = resultVec.subtract(from);
        var dir = dist.normalize();
        var to = dist.length() <= visualizationRange ? resultVec : from.add(dir.scale(visualizationRange));

        if (level.isLoaded(new BlockPos(to)) && level.isLoaded(new BlockPos(from)) && level.isClientSide) {
            FollowProjectile aoeProjectile = new FollowProjectile(level, from, to);
            DistHelper.spawnEntityClientSide(level, aoeProjectile);
        }
    }

    public Tier getMiningTier(ItemStack stack) {
        var tier = stack.getOrCreateTag().getString(TheurgyConstants.Nbt.Divination.SETTING_TIER);
        return TierSortingRegistry.byName(new ResourceLocation(tier));
    }

    public TagKey<Block> getAllowedBlocksTag(ItemStack stack) {
        var allowedBlocksTag = stack.getOrCreateTag().getString(TheurgyConstants.Nbt.Divination.SETTING_ALLOWED_BLOCKS_TAG);
        return TagRegistry.makeBlockTag(new ResourceLocation(allowedBlocksTag));
    }

    public TagKey<Block> getDisallowedBlocksTag(ItemStack stack) {
        var disallowedBlocksTag = stack.getOrCreateTag().getString(TheurgyConstants.Nbt.Divination.SETTING_DISALLOWED_BLOCKS_TAG);
        return TagRegistry.makeBlockTag(new ResourceLocation(disallowedBlocksTag));
    }

    /**
     * Inner class to avoid classloading issues on the server
     */
    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            DistHelper.fillItemCategory(this, tab, items);
        }
    }

    /**
     * Inner class to avoid classloading issues on the server
     */
    public static class DistHelper {

        @SuppressWarnings("deprecation")
        public static ItemPropertyFunction DIVINATION_DISTANCE = (stack, world, entity, i) -> {
            if (!stack.getOrCreateTag().contains(TheurgyConstants.Nbt.Divination.DISTANCE) ||
                    stack.getTag().getFloat(TheurgyConstants.Nbt.Divination.DISTANCE) < 0)
                return NOT_FOUND;
            return stack.getTag().getFloat(TheurgyConstants.Nbt.Divination.DISTANCE);
        };

        public static void fillItemCategory(DivinationRodItem item, CreativeModeTab tab, NonNullList<ItemStack> items) {
            if (tab != Theurgy.CREATIVE_MODE_TAB && tab != CreativeModeTab.TAB_SEARCH)
                return;

            var level = Minecraft.getInstance().level;
            if (level != null) {
                var recipeManager = level.getRecipeManager();
                recipeManager.getRecipes().forEach((recipe) -> {
                    if (recipe.getResultItem() != null && recipe.getResultItem().getItem() == item) {
                        //should not have to check for null here, but in the case of https://github.com/klikli-dev/theurgy/issues/68 we actually get a null ResultItem
                        items.add(recipe.getResultItem().copy());
                    }
                });
            }
        }

        public static void spawnEntityClientSide(Level level, Entity entity) {
            if (level instanceof ClientLevel clientLevel) {
                clientLevel.putNonPlayerEntity(entity.getId(), entity); //client only spawn of entity
            }
        }
    }
}
