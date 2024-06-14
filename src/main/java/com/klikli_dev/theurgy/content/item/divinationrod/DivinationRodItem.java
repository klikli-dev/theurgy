// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.divinationrod;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageSetDivinationResult;
import com.klikli_dev.theurgy.registry.BlockTagRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.SoundRegistry;
import com.klikli_dev.theurgy.scanner.ScanManager;
import com.klikli_dev.theurgy.util.EntityUtil;
import com.klikli_dev.theurgy.util.LevelUtil;
import com.klikli_dev.theurgy.util.TagUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DivinationRodItem extends Item {

    public static final float NOT_FOUND = 7.0f;
    public static final float SEARCHING = 8.0f;

    public Tiers defaultTier;
    public TagKey<Block> defaultAllowedBlocksTag;
    public TagKey<Block> defaultDisallowedBlocksTag;

    public int defaultRange;
    public int defaultDuration;
    public int defaultDurability;
    public boolean defaultAllowAttuning;

    public DivinationRodItem(Properties pProperties, Tiers defaultTier, TagKey<Block> defaultAllowedBlocksTag, TagKey<Block> defaultDisallowedBlocksTag, int defaultRange, int defaultDuration, int defaultDurability, boolean defaultAllowAttuning) {
        super(pProperties
                .component(DataComponentRegistry.DIVINATION_SETTINGS_TIER, defaultTier)
                .component(DataComponentRegistry.DIVINATION_SETTINGS_ALLOWED_BLOCKS_TAG, defaultAllowedBlocksTag)
                .component(DataComponentRegistry.DIVINATION_SETTINGS_DISALLOWED_BLOCKS_TAG, defaultDisallowedBlocksTag)
                .component(DataComponentRegistry.DIVINATION_SETTINGS_RANGE, defaultRange)
                .component(DataComponentRegistry.DIVINATION_SETTINGS_DURATION, defaultDuration)
                .component(DataComponentRegistry.DIVINATION_SETTINGS_MAX_DAMAGE, defaultDurability)
                .component(DataComponentRegistry.DIVINATION_SETTINGS_ALLOW_ATTUNING, defaultAllowAttuning)
        );
        this.defaultTier = defaultTier;
        this.defaultAllowedBlocksTag = defaultAllowedBlocksTag;
        this.defaultDisallowedBlocksTag = defaultDisallowedBlocksTag;
        this.defaultRange = defaultRange;
        this.defaultDuration = defaultDuration;
        this.defaultDurability = defaultDurability;
        this.defaultAllowAttuning = defaultAllowAttuning;
    }

    private static final Map<Holder<Block>, ItemStack> linkedBlockCache = new Object2ObjectOpenHashMap<>();
    private static final Map<TagKey<Block>, ItemStack> linkedTagCache = new Object2ObjectOpenHashMap<>();

    public static ItemStack getLinkedBlockStack(ItemStack divinationRod) {
        if (divinationRod.has(DataComponentRegistry.DIVINATION_LINKED_BLOCK))
            return linkedBlockCache.computeIfAbsent(divinationRod.get(DataComponentRegistry.DIVINATION_LINKED_BLOCK), b -> new ItemStack(b.value()));


        if (divinationRod.has(DataComponentRegistry.DIVINATION_LINKED_TAG))
            return linkedTagCache.computeIfAbsent(divinationRod.get(DataComponentRegistry.DIVINATION_LINKED_TAG), TagUtil::getItemStackForBlockTag);

        return ItemStack.EMPTY;
    }

    private static void scanLinkedBlock(Player player, Holder<Block> blockHolder, int range, int duration) {
        var blocks = getScanTargetsForId(blockHolder.unwrapKey().get().location());
        ScanManager.get().beginScan(player, blocks, range, duration);
    }

    private static void scanLinkedTag(Player player, TagKey<Block> tagKey, int range, int duration) {
        var blocks = BuiltInRegistries.BLOCK.getTag(tagKey)
                .map(tag -> tag.stream().map(Holder::value).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        if (!blocks.isEmpty()) {
            ScanManager.get().beginScan(player, blocks, range, duration);
        }
    }

    public static Set<Block> getScanTargetsForId(ResourceLocation linkedBlockId) {
        //First: try to get a tag for the given block.
        var tagKey = TagKey.create(Registries.BLOCK, getOreTagFromBlockId(linkedBlockId));
        var tag = BuiltInRegistries.BLOCK.getTag(tagKey);

        if (tag.map(HolderSet.ListBacked::size).orElse(0) > 0)
            return tag.map(t -> t.stream().map(Holder::value).collect(Collectors.toSet())).orElse(Collections.emptySet());

        //If no fitting tag succeeds, try to get block + deepslate variant
        var block = BuiltInRegistries.BLOCK.get(linkedBlockId);
        if (block != null) {
            Block deepslateBlock = null;

            //also search for deepslate ores
            if (linkedBlockId.getPath().contains("_ore") && !linkedBlockId.getPath().contains("deepslate_")) {
                var deepslateId = ResourceLocation.fromNamespaceAndPath(linkedBlockId.getNamespace(),  "deepslate_" + linkedBlockId.getPath());
                deepslateBlock = BuiltInRegistries.BLOCK.get(deepslateId);
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

        return ResourceLocation.parse("c:ores/" + oreName);
    }

    public static void registerCreativeModeTabs(DivinationRodItem item, CreativeModeTab.Output output) {
        var level = LevelUtil.getLevelWithoutContext();
        if (level != null) {
            var recipeManager = level.getRecipeManager();
            recipeManager.getRecipes().forEach((recipe) -> {
                if (recipe.value().getResultItem(level.registryAccess()) != null && recipe.value().getResultItem(level.registryAccess()).getItem() == item) {
                    output.accept(recipe.value().getResultItem(level.registryAccess()).copy());
                }
            });
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_MAX_DAMAGE, 1);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity.level().isClientSide && pLivingEntity instanceof Player player) {
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

            if (!stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_ALLOW_ATTUNING, false)) {
                if (!level.isClientSide) {
                    player.sendSystemMessage(
                            Component.translatable(TheurgyConstants.I18n.Message.DIVINATION_ROD_ATTUNING_NOT_ALLOWED)
                    );
                }
                return InteractionResult.FAIL;
            }

            BlockState state = level.getBlockState(pos);
            if (!state.isAir()) {
                if (state.is(tier.getIncorrectBlocksForDrops())) {
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
                        stack.set(DataComponentRegistry.DIVINATION_LINKED_BLOCK, state.getBlockHolder());

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
            if (stack.has(DataComponentRegistry.DIVINATION_LINKED_BLOCK) || stack.has(DataComponentRegistry.DIVINATION_LINKED_TAG)) {

                stack.set(DataComponentRegistry.DIVINATION_DISTANCE, SEARCHING);
                player.startUsingItem(hand);
                level.playSound(player, player.blockPosition(), SoundRegistry.TUNING_FORK.get(), SoundSource.PLAYERS,
                        1, 1);

                if (level.isClientSide) {
                    if (stack.has(DataComponentRegistry.DIVINATION_LINKED_TAG)) {
                        scanLinkedTag(player,
                                stack.get(DataComponentRegistry.DIVINATION_LINKED_TAG),
                                stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_RANGE, this.defaultRange),
                                stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DURATION, this.defaultDuration));
                    } else if(stack.has(DataComponentRegistry.DIVINATION_LINKED_BLOCK)){
                        scanLinkedBlock(player,
                                stack.get(DataComponentRegistry.DIVINATION_LINKED_BLOCK),
                                stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_RANGE, this.defaultRange),
                                stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DURATION, this.defaultDuration));
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
            player.broadcastBreakEvent(LivingEntity.getSlotForHand(player.getUsedItemHand()));
            var item = stack.getItem();
            stack.shrink(1);
            player.awardStat(Stats.ITEM_BROKEN.get(item));
            stack.setDamageValue(0);
            return stack;
        }

        player.getCooldowns().addCooldown(this, stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DURATION, this.defaultDuration));

        stack.set(DataComponentRegistry.DIVINATION_DISTANCE, NOT_FOUND);

        if (level.isClientSide) {
            BlockPos result = ScanManager.get().finishScan(player);
            float distance = this.getDistance(player.position(), result);
            stack.set(DataComponentRegistry.DIVINATION_DISTANCE, distance);

            Networking.sendToServer(new MessageSetDivinationResult(result, distance));

            if (result != null) {
                stack.set(DataComponentRegistry.DIVINATION_POS, result);
                this.spawnResultParticle(result, level, player);
            }
        } else {
            //no damage for players in creative mode
            if (!player.getAbilities().instabuild)
                //only hurt, but do not break -> this allows using the rod without breaking it when we just re-use a saved result.
                //we break it at the beginning of this method if we are at >= max damage.
                stack.hurtAndBreak(1, player.getRandom(), null, () -> {});
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DURATION, this.defaultDuration);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity pLivingEntity, int pTimeCharged) {
        if(!stack.has(DataComponentRegistry.DIVINATION_POS))
            //player interrupted, so we can safely set not found on server, if we don't have a previous result
            stack.set(DataComponentRegistry.DIVINATION_DISTANCE, NOT_FOUND);
        else {
            //otherwise, restore distance from result
            //nice bonus: will update crystal status on every "display only" use.
            float distance = this.getDistance(pLivingEntity.position(), stack.get(DataComponentRegistry.DIVINATION_POS));
            stack.set(DataComponentRegistry.DIVINATION_DISTANCE, distance);
        }


        if (level.isClientSide) {
            ScanManager.get().cancelScan();

            //re-use old result
            if (stack.has(DataComponentRegistry.DIVINATION_POS)) {
                this.spawnResultParticle(stack.get(DataComponentRegistry.DIVINATION_POS), level, pLivingEntity);
            }
        }
        super.releaseUsing(stack, level, pLivingEntity, pTimeCharged);
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (pStack.has(DataComponentRegistry.DIVINATION_LINKED_BLOCK) || pStack.has(DataComponentRegistry.DIVINATION_LINKED_TAG)) {
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
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if (pStack.has(DataComponentRegistry.DIVINATION_LINKED_BLOCK) || pStack.has(DataComponentRegistry.DIVINATION_LINKED_TAG)) {
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

                if (pStack.has(DataComponentRegistry.DIVINATION_POS)) {
                    pTooltipComponents.add(Component.translatable(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_LAST_RESULT,
                            blockComponent,
                            ComponentUtils.wrapInSquareBrackets(Component.literal(
                                    pStack.get(DataComponentRegistry.DIVINATION_POS).toShortString()
                            ).withStyle(ChatFormatting.GREEN))
                    ).withStyle(ChatFormatting.GRAY));
                }
            }

        } else {
            pTooltipComponents.add(Component.translatable(TheurgyConstants.I18n.Tooltip.DIVINATION_ROD_NO_LINK));
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
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
            FollowProjectile aoeProjectile = new FollowProjectile(level, from, to, 255, 25, 180, 0.25f);
            EntityUtil.spawnEntityClientSide(level, aoeProjectile);
        }
    }

    public Tier getMiningTier(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_TIER, this.defaultTier);
    }

    public TagKey<Block> getAllowedBlocksTag(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_ALLOWED_BLOCKS_TAG, this.defaultAllowedBlocksTag);
    }

    public TagKey<Block> getDisallowedBlocksTag(ItemStack stack) {
        return stack.getOrDefault(DataComponentRegistry.DIVINATION_SETTINGS_DISALLOWED_BLOCKS_TAG, this.defaultDisallowedBlocksTag);
    }

    /**
     * Inner class to avoid classloading issues on the server
     */
    public static class DistHelper {
        @SuppressWarnings("deprecation")
        public static ItemPropertyFunction DIVINATION_DISTANCE = (stack, world, entity, i) -> {
            if (stack.getOrDefault(DataComponentRegistry.DIVINATION_DISTANCE, -1.0f) < 0)
                return NOT_FOUND;
            return stack.get(DataComponentRegistry.DIVINATION_DISTANCE);
        };
    }
}
