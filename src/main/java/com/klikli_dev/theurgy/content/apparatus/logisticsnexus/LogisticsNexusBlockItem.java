// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.util.GeckoLibUtil;
import com.google.common.base.Suppliers;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.render.LogisticsNexusItemRenderer;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LogisticsNexusBlockItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public LogisticsNexusBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        var stack = super.getDefaultInstance();
        this.ensureHasId(stack);
        return stack;
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, net.minecraft.world.entity.player.Player player) {
        super.onCraftedBy(itemStack, player);
        this.ensureHasId(itemStack);
    }

    @Override
    public void onCraftedPostProcess(ItemStack itemStack, Level level) {
        super.onCraftedPostProcess(itemStack, level);
        this.ensureHasId(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);
        this.ensureHasId(itemStack);
    }

    private void ensureHasId(ItemStack stack) {
        if (!stack.has(DataComponentRegistry.LOGISTICS_NEXUS_ID.get())) {
            stack.set(DataComponentRegistry.LOGISTICS_NEXUS_ID.get(), UUID.randomUUID());
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, tooltipFlag);

        var nexusId = stack.get(DataComponentRegistry.LOGISTICS_NEXUS_ID.get());
        if (nexusId != null) {
            tooltipAdder.accept(Component.literal("Nexus ID: ")
                    .append(Component.literal(nexusId.toString()).withStyle(ChatFormatting.DARK_GRAY))
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.@NonNull ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private final Supplier<GeoItemRenderer<LogisticsNexusBlockItem>> renderer = Suppliers.memoize(LogisticsNexusItemRenderer::new);

            @Override
            public @Nullable GeoItemRenderer<LogisticsNexusBlockItem> getGeoItemRenderer() {
                return this.renderer.get();
            }
        });
    }
}
