// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.derivative;

import com.google.common.base.Suppliers;
import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.klikli_dev.theurgy.util.TagUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A base class for alchemical items that are derived from a source item.
 * This class provides automatic rendering and naming based on the source item.
 */
public class AlchemicalDerivativeItem extends Item {
    private static final Map<Holder<Item>, ItemStack> sourceItemCache = new Object2ObjectOpenHashMap<>();
    private static final Map<TagKey<Item>, ItemStack> sourceTagCache = new Object2ObjectOpenHashMap<>();
    /**
     * if true, an item model based on builtin/entity will be generated for this item.
     * This will cause the BEWLR to be used to render the item dynamically based on the source components, instead of a json model.
     * <p>
     * Note: this variable cannot be used to conditionally register an IClientItemExtensions,
     * because the registration happens in the Item constructor, before this variable is set.
     * However, it is also not necessary, because the render behaviour is governed by the item model anyway.
     */
    public boolean useAutomaticIconRendering;
    /**
     * if true, will use the source components to provide a parameter to the item name component that can be accessed with %s in the language file.
     */
    public boolean useAutomaticNameRendering;
    /**
     * If true, data gen will generate a lang entry for the item name.
     */
    public boolean useAutomaticNameLangGeneration;
    /**
     * if true, will use the source components to provide a parameter to the item tooltip component that can be accessed with %s in the language file.
     */
    public boolean provideAutomaticTooltipData;
    /**
     * If true, data gen will generate a lang entry for the item tooltip.
     */
    public boolean useAutomaticTooltipLangGeneration;

    /**
     * If true will use the source name from the lang file, instead of using the name of the source stack.
     * Uses {@link TheurgyConstants.I18n.Item#ALCHEMICAL_DERIVATIVE_SOURCE_SUFFIX} as suffix to the item description id.
     */
    public boolean useCustomSourceName;

    public Supplier<ItemStack> emptyJarStackSupplier;
    public AlchemicalDerivativeTier tier;

    public AlchemicalDerivativeItem(Properties pProperties) {
        super(pProperties);
        this.useAutomaticIconRendering = true;
        this.useAutomaticNameRendering = true;
        this.useAutomaticNameLangGeneration = true;
        this.provideAutomaticTooltipData = true;
        this.useAutomaticTooltipLangGeneration = true;
        this.useCustomSourceName = false;
        this.emptyJarStackSupplier = Suppliers.memoize(() -> new ItemStack(ItemRegistry.EMPTY_JAR_ICON.get()));
        this.tier = AlchemicalDerivativeTier.ABUNDANT;
    }

    public static ItemStack getEmptyJarStack(ItemStack sulfurStack) {

        if (sulfurStack.getItem() instanceof AlchemicalDerivativeItem sulfur) {
            return sulfur.emptyJarStackSupplier.get();
        }

        return ItemStack.EMPTY;
    }

    public static AlchemicalDerivativeTier getTier(ItemStack sulfurStack) {
        if (sulfurStack.getItem() instanceof AlchemicalDerivativeItem sulfur) {
            return sulfur.tier;
        }

        return AlchemicalDerivativeTier.ABUNDANT;
    }

    public AlchemicalDerivativeItem tier(AlchemicalDerivativeTier tier) {
        this.tier = tier;
        return this;
    }

    public AlchemicalDerivativeTier tier() {
        return this.tier;
    }

    public AlchemicalDerivativeItem noAuto() {
        this.useAutomaticIconRendering = false;
        this.useAutomaticNameRendering = false;
        this.useAutomaticNameLangGeneration = false;
        this.provideAutomaticTooltipData = false;
        this.useAutomaticTooltipLangGeneration = false;
        return this;
    }

    public AlchemicalDerivativeItem autoIcon(boolean value) {
        this.useAutomaticIconRendering = value;
        return this;
    }

    public AlchemicalDerivativeItem withJarIcon(Supplier<ItemStack> emptyJarStackSupplier) {
        this.emptyJarStackSupplier = emptyJarStackSupplier;
        return this;
    }

    public AlchemicalDerivativeItem autoName(boolean value) {
        return this.autoName(value, value);
    }

    public AlchemicalDerivativeItem autoName(boolean rendering, boolean langGeneration) {
        this.useAutomaticNameRendering = rendering;
        this.useAutomaticNameLangGeneration = langGeneration;
        return this;
    }

    public AlchemicalDerivativeItem autoTooltip(boolean value) {
        return this.autoTooltip(value, value);
    }

    public AlchemicalDerivativeItem autoTooltip(boolean rendering, boolean langGeneration) {
        this.provideAutomaticTooltipData = rendering;
        this.useAutomaticTooltipLangGeneration = langGeneration;
        return this;
    }

    public AlchemicalDerivativeItem useCustomSourceName(boolean value) {
        this.useCustomSourceName = value;
        return this;
    }

    public MutableComponent formatSourceName(MutableComponent sourceName, AlchemicalDerivativeTier tier) {
        return sourceName.withStyle(Style.EMPTY
                .withColor(tier.color())
                .withItalic(true)
        );
    }

    /**
     * Get the source item stack from the derivative item stack components.
     * The source *should* be the item that was used to create the derivative item.
     * Due to this only being used for automatic rendering and naming purposes, the source might be a different item for some reason, and in many cases could be empty.
     */
    public ItemStack getSourceStack(ItemStack stack) {
        if (stack.has(DataComponentRegistry.SOURCE_ITEM))
            //noinspection DataFlowIssue
            return sourceItemCache.computeIfAbsent(stack.get(DataComponentRegistry.SOURCE_ITEM), ItemStack::new);

        if (stack.has(DataComponentRegistry.SOURCE_TAG))
            return sourceTagCache.computeIfAbsent(stack.get(DataComponentRegistry.SOURCE_TAG), TagUtil::getItemStackForTag);

        return ItemStack.EMPTY;
    }

    public MutableComponent getSourceName(ItemStack pStack) {
        //If we have a source name set, we use that
        if (this.useCustomSourceName) {
            return this.formatSourceName(Component.translatable(pStack.getDescriptionId() + TheurgyConstants.I18n.Item.ALCHEMICAL_DERIVATIVE_SOURCE_SUFFIX), this.tier);
        }

        //finally, fall back to getting a stack
        var source = this.getSourceStack(pStack);
        if (!source.isEmpty()) {
            if (source.getHoverName() instanceof MutableComponent hoverName)
                return this.formatSourceName(hoverName, this.tier);
        }

        return Component.translatable(TheurgyConstants.I18n.Item.ALCHEMICAL_DERIVATIVE_UNKNOWN_SOURCE);
    }

    public List<MutableComponent> getTooltipData(ItemStack stack) {
        var result = new ArrayList<MutableComponent>();

        if (this.provideAutomaticTooltipData) {
            var tier = getTier(stack);
            result.add(this.getSourceName(stack));
            result.add(
                    ComponentUtils.wrapInSquareBrackets(
                            Component.translatable(tier.descriptionId())
                                    .withStyle(Style.EMPTY
                                            .withColor(tier.color)
                                            .withItalic(true))
                    ));
        }

        return result;
    }

    public List<MutableComponent> getNameData(ItemStack stack) {
        var result = new ArrayList<MutableComponent>();

        if (this.useAutomaticNameRendering) {
            var tier = getTier(stack);
            result.add(ComponentUtils.wrapInSquareBrackets(this.getSourceName(stack)));
            result.add(ComponentUtils.wrapInSquareBrackets(
                    Component.translatable(tier.descriptionId())
                            .withStyle(Style.EMPTY
                                    .withColor(tier.color)
                                    .withItalic(true))
            ));
        }

        return result;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        if (this.useAutomaticNameRendering) {
            var translationArgs = this.getNameData(pStack);
            return Component.translatable(this.getDescriptionId(pStack), translationArgs.toArray());
        }
        return super.getName(pStack);
    }
}
