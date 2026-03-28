// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.salt.render;

import com.klikli_dev.theurgy.content.item.salt.AlchemicalSaltItem;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.klikli_dev.theurgy.util.TagUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.function.Consumer;

/**
 * Simple renderer for alchemical salt items.
 * <p>
 * Normally renders the salt's flat item model. When shift is held, renders the source item
 * full size instead, allowing players to see what the salt was derived from.
 */
public class AlchemicalSaltRenderer implements SpecialModelRenderer<AlchemicalSaltRenderState> {

    @Override
    public void submit(@Nullable AlchemicalSaltRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, boolean hasFoil, int outlineColor) {
        if (state == null) return;

        // Counteract the -0.5 translation applied by ItemTransform.NO_TRANSFORM (from the derivative_base model)
        // so that our sub-items render centered rather than offset to the bottom-left corner of the slot.
        poseStack.translate(0.5f, 0.5f, 0.5f);

        // If shift is down, render the source item full size
        if (state.shiftDown()) {
            ItemStack sourceStack = this.resolveSourceStack(state);
            if (!sourceStack.isEmpty()) {
                this.submitItem(sourceStack, ItemDisplayContext.NONE, poseStack, submitNodeCollector, light, overlay, outlineColor);
            }
        }
        // When shift is not held, the default flat item model renders the salt texture - no special rendering needed.
    }

    /**
     * Resolves the source ItemStack from the render state's non-level-owned data.
     */
    private ItemStack resolveSourceStack(AlchemicalSaltRenderState state) {
        if (state.sourceItem() != null) {
            return new ItemStack(state.sourceItem());
        }
        if (state.sourceTag() != null) {
            return TagUtil.getItemStackForTag(state.sourceTag());
        }
        return ItemStack.EMPTY;
    }

    private void submitItem(ItemStack itemStack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, int outlineColor) {
        ItemStackRenderState renderState = new ItemStackRenderState();
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getItemModelResolver().updateForTopItem(renderState, itemStack, displayContext, minecraft.level, null, 0);
        renderState.submit(poseStack, submitNodeCollector, light, overlay, outlineColor);
    }

    @Override
    public @Nullable AlchemicalSaltRenderState extractArgument(ItemStack stack) {
        if (stack.isEmpty()) return null;

        var item = stack.getItem();
        if (!(item instanceof AlchemicalSaltItem)) return null;

        var sourceItem = stack.get(DataComponentRegistry.SOURCE_ITEM);
        var sourceTag = stack.get(DataComponentRegistry.SOURCE_TAG);
        var shiftDown = Minecraft.getInstance().hasShiftDown();

        return new AlchemicalSaltRenderState(sourceItem, sourceTag, shiftDown);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        output.accept(new Vector3f(-0.5f, -0.5f, -0.5f));
        output.accept(new Vector3f(0.5f, 0.5f, 0.5f));
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<AlchemicalSaltRenderState> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public @Nullable SpecialModelRenderer<AlchemicalSaltRenderState> bake(SpecialModelRenderer.BakingContext context) {
            return new AlchemicalSaltRenderer();
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<AlchemicalSaltRenderState>> type() {
            return MAP_CODEC;
        }
    }
}
