/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetDivinationResult;
import com.klikli_dev.theurgy.registry.BlockTagRegistry;
import com.klikli_dev.theurgy.registry.SoundRegistry;
import com.klikli_dev.theurgy.scanner.ScanManager;
import com.klikli_dev.theurgy.util.TagUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
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
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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

    public static String getLinkedBlockId(ItemStack divinationRod) {
        return divinationRod.getOrCreateTag().getString(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID);
    }

    public static boolean hasLinkedBlock(ItemStack divinationRod) {
        return divinationRod.hasTag() && divinationRod.getTag().contains(TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID);
    }

    public static ItemStack getLinkedBlockStack(ItemStack divinationRod) {
        if (hasLinkedBlock(divinationRod)) {
            var targetId = getLinkedBlockId(divinationRod);
            ItemStack targetStack;

            if (targetId.startsWith("#")) {
                var tagId = new ResourceLocation(targetId.substring(1));
                var tag = TagKey.create(Registries.ITEM, tagId);
                targetStack = TagUtil.getItemStackForTag(tag);
            } else {
                var itemId = new ResourceLocation(targetId);
                targetStack = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId));
            }

            return targetStack;
        }

        return ItemStack.EMPTY;
    }

    private static void scanLinkedBlock(Player player, String id, int range, int duration) {
        var targetId = new ResourceLocation(id);

        var blocks = getScanTargetsForId(targetId);
        ScanManager.get().beginScan(player, blocks, range, duration);
    }

    private static void scanLinkedTag(Player player, String id, int range, int duration) {
        var targetId = new ResourceLocation(id.substring(1)); //skip the #
        var tagKey = TagKey.create(Registries.BLOCK, targetId);
        var blocks = ForgeRegistries.BLOCKS.tags().getTag(tagKey).stream().collect(Collectors.toSet());

        if (!blocks.isEmpty()) {
            ScanManager.get().beginScan(player, blocks, range, duration);
        }
    }

    public static Set<Block> getScanTargetsForId(ResourceLocation linkedBlockId) {
        //First: try to get a tag for the given block.
        var tagKey = TagKey.create(Registries.BLOCK, getOreTagFromBlockId(linkedBlockId));
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
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity.level.isClientSide && pLivingEntity instanceof Player player) {
            ScanManager.get().updateScan(player, false);
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
                    player.sendSystemMessage(
                            Component.translatable(TheurgyConstants.I18n.Message.DIVINATION_ROD_ATTUNING_NOT_ALLOWED)
                    );
                }
                return InteractionResult.FAIL;
            }

            BlockState state = level.getBlockState(pos);
            if (!state.isAir()) {
                if (!TierSortingRegistry.isCorrectTierForDrops(tier, state)) {
                    if (!level.isClientSide) {
                        player.sendSystemMessage(
                                Component.translatable(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_TIER_TOO_LOW,
                                        this.getBlockDisplayComponent(state.getBlock())
                                )
                        );
                    }
                    return InteractionResult.FAIL;
                } else if (!state.is(allowedBlocksTag)) {
                    if (!level.isClientSide) {
                        player.sendSystemMessage(
                                Component.translatable(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_NOT_ALLOWED,
                                        this.getBlockDisplayComponent(state.getBlock())
                                )
                        );
                    }
                    return InteractionResult.FAIL;
                } else if (state.is(disallowedBlocksTag)) {
                    if (!level.isClientSide) {
                        player.sendSystemMessage(
                                Component.translatable(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_BLOCK_DISALLOWED,
                                        this.getBlockDisplayComponent(state.getBlock())
                                )
                        );
                    }
                    return InteractionResult.FAIL;
                } else {
                    if (!level.isClientSide) {
                        stack.getOrCreateTag().putString(
                                TheurgyConstants.Nbt.Divination.LINKED_BLOCK_ID,
                                ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString()
                        );

                        player.sendSystemMessage(
                                Component.translatable(
                                        TheurgyConstants.I18n.Message.DIVINATION_ROD_LINKED,
                                        this.getBlockDisplayComponent(state.getBlock())
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
            if (hasLinkedBlock(stack)) {
                var tag = stack.getTag();
                tag.putFloat(TheurgyConstants.Nbt.Divination.DISTANCE, SEARCHING);
                player.startUsingItem(hand);
                level.playSound(player, player.blockPosition(), SoundRegistry.TUNING_FORK.get(), SoundSource.PLAYERS,
                        1, 1);

                if (level.isClientSide) {
                    var targetId = getLinkedBlockId(stack);

                    if (targetId.startsWith("#")) {
                        scanLinkedTag(player, targetId,
                                tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_RANGE),
                                tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_DURATION));
                    } else {
                        scanLinkedBlock(player, targetId,
                                tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_RANGE),
                                tag.getInt(TheurgyConstants.Nbt.Divination.SETTING_DURATION));
                    }
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
            //no damage for players in creative mode
            if (!player.getAbilities().instabuild)
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
        if (hasLinkedBlock(pStack)) {
            var stack = getLinkedBlockStack(pStack);
            if (!stack.isEmpty()) {
                var blockComponent = ComponentUtils.wrapInSquareBrackets(
                                Component.empty().append(stack.getHoverName()).withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN).withItalic(true))
                        )
                        .withStyle((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack))));
                return Component.translatable(this.getDescriptionId() + ".linked", blockComponent);
            } else {
                //in case the block is not found, we indicate something went wrong
                var blockComponent = ComponentUtils.wrapInSquareBrackets(
                                Component.translatable(TheurgyConstants.I18n.Item.DIVINATION_ROD_UNKNOWN_LINKED_BLOCK).withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withItalic(true))
                        )
                        .withStyle((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack))));
                return Component.translatable(this.getDescriptionId() + ".linked", blockComponent);
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
        if (hasLinkedBlock(pStack)) {
            var stack = getLinkedBlockStack(pStack);
            if (!stack.isEmpty()) {
                var blockComponent = Component.empty().append(stack.getHoverName())
                        .withStyle(ChatFormatting.GREEN)
                        .withStyle((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack))));

                this.getBlockDisplayComponent(stack);
                pTooltipComponents.add(
                        Component.translatable(
                                TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LINKED_TO,
                                blockComponent
                        ).withStyle(ChatFormatting.GRAY));

                if (pStack.getTag().contains(TheurgyConstants.Nbt.Divination.POS)) {
                    var pos = BlockPos.of(pStack.getTag().getLong(TheurgyConstants.Nbt.Divination.POS));
                    pTooltipComponents.add(Component.translatable(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LAST_RESULT,
                            blockComponent,
                            ComponentUtils.wrapInSquareBrackets(Component.literal(pos.toShortString()).withStyle(ChatFormatting.GREEN))
                    ).withStyle(ChatFormatting.GRAY));
                }
            }

        } else {
            pTooltipComponents.add(Component.translatable(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_NO_LINK));
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
        return this.getBlockDisplayComponent(new ItemStack(block));
    }

    protected MutableComponent getBlockDisplayComponent(ItemStack stack) {
        var displayName = stack.getHoverName();
        return ComponentUtils.wrapInSquareBrackets(displayName)
                .withStyle(ChatFormatting.GREEN)
                .withStyle((style) -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack))));
    }

    protected void spawnResultParticle(BlockPos result, Level level, LivingEntity entity) {
        final var visualizationRange = 10.0f;
        var from = new Vec3(entity.getX(), entity.getEyeY() - (double) 0.1F, entity.getZ());
        var resultVec = Vec3.atCenterOf(result);
        var dist = resultVec.subtract(from);
        var dir = dist.normalize();
        var to = dist.length() <= visualizationRange ? resultVec : from.add(dir.scale(visualizationRange));

        if (level.isLoaded(BlockPos.containing(to)) && level.isLoaded(BlockPos.containing(from)) && level.isClientSide) {
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
        return BlockTagRegistry.tag(new ResourceLocation(allowedBlocksTag));
    }

    public TagKey<Block> getDisallowedBlocksTag(ItemStack stack) {
        var disallowedBlocksTag = stack.getOrCreateTag().getString(TheurgyConstants.Nbt.Divination.SETTING_DISALLOWED_BLOCKS_TAG);
        return BlockTagRegistry.tag(new ResourceLocation(disallowedBlocksTag));
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

        public static void registerCreativeModeTabs(DivinationRodItem item, CreativeModeTab.Output output) {
            var level = Minecraft.getInstance().level;
            if (level != null) {
                var recipeManager = level.getRecipeManager();
                recipeManager.getRecipes().forEach((recipe) -> {
                    if (recipe.getResultItem(level.registryAccess()) != null && recipe.getResultItem(level.registryAccess()).getItem() == item) {
                        output.accept(recipe.getResultItem(level.registryAccess()).copy());
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
