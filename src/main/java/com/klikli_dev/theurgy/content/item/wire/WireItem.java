// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.wire;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasLeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasWireEndPoint;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.Wire;
import com.klikli_dev.theurgy.logistics.WireEndPoint;
import com.klikli_dev.theurgy.logistics.Wires;
import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class WireItem extends Item {

    private final int maxRange;

    public WireItem(Properties pProperties, int maxRange) {
        super(pProperties);
        this.maxRange = maxRange;
    }

    public static void onClientTick(Player player) {
        var stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof WireItem))
            return;

        var wirePoint = WireEndPoint.load(stack);
        if (wirePoint == null || wirePoint.level() != player.level().dimension())
            return;

        Outliner.get().showAABB(wirePoint,
                Shapes.block().bounds().move(wirePoint.pos()), 1).colored(Color.YELLOW).lineWidth(1 / 32f);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        var stack = pContext.getItemInHand();

        //TODO: wires do not exist on client wire cache (only render cache)
        //      so when we check for getWire we get null -> and thus we may return the wrong interactionresult and never run the serverside logkc

        if (!this.isWireEndPoint(pContext)) {
            if (Objects.requireNonNull(pContext.getPlayer()).isShiftKeyDown()) {
                WireEndPoint.removeFrom(stack);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }

        var wirePoint = WireEndPoint.load(stack);

        if (wirePoint != null) {
            var existingWire = this.getWire(pContext, wirePoint);
            if (existingWire != null) {
                return this.disconnectWire(pContext, existingWire);
            } else {
                return this.connectWire(pContext, wirePoint);
            }
        } else {
            return this.storeWireEndPoint(pContext);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if (usedHand == InteractionHand.MAIN_HAND && player.isShiftKeyDown()) {
            WireEndPoint.removeFrom(player.getMainHandItem());
            return InteractionResultHolder.sidedSuccess(player.getMainHandItem(), level.isClientSide());
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        var wirePoint = WireEndPoint.load(stack);
        if (wirePoint != null) {
            tooltipComponents.add(Component.translatable(
                    stack.getDescriptionId() + TheurgyConstants.I18n.Tooltip.DYNMIC_SUFFIX,
                    Component.literal("[" + wirePoint.pos().toShortString() + "]").withStyle(ChatFormatting.GREEN)
            ).withStyle(ChatFormatting.GRAY));
        }
    }

    protected Wire getWire(UseOnContext pContext, WireEndPoint wireEndPoint) {
        if (wireEndPoint.level() != pContext.getLevel().dimension())
            return null; //theoretically we could have a wire in place in the same spot in another dimension

        return Wires.get(pContext.getLevel()).getWire(wireEndPoint.pos(), pContext.getClickedPos());
    }

    protected boolean isWireEndPoint(UseOnContext pContext) {
        var level = pContext.getLevel();
        var pos = pContext.getClickedPos();

        return level.getBlockState(pos).getBlock() instanceof HasWireEndPoint;
    }

    protected InteractionResult connectWire(UseOnContext pContext, WireEndPoint wireEndPoint) {
        if (wireEndPoint.level() != pContext.getLevel().dimension())
            return InteractionResult.FAIL; //can't do cables across dimensions

        var pos = pContext.getClickedPos();
        if (wireEndPoint.pos().equals(pos))
            return InteractionResult.FAIL; //can't connect to self

        if (wireEndPoint.pos().distManhattan(pos) > this.maxRange)
            return InteractionResult.FAIL; //can't connect to points too far away

        var stack = pContext.getItemInHand();
        WireEndPoint.removeFrom(stack);

        if (!Objects.requireNonNull(pContext.getPlayer()).getAbilities().instabuild) {
            stack.shrink(1);
        }

        var level = pContext.getLevel();

        Wires.get(level).addWire(new Wire(wireEndPoint.pos(), pos));

        if (!level.isClientSide) {
            var posA = GlobalPos.of(level.dimension(), pos);
            var posB = GlobalPos.of(wireEndPoint.level(), wireEndPoint.pos());
            Logistics.get().add(posA, posB);

            if (level.getBlockEntity(posA.pos()) instanceof HasLeafNodeBehaviour<?, ?> blockEntity) {
                Logistics.get().add(blockEntity.leafNode());
            }

            if (level.getBlockEntity(posB.pos()) instanceof HasLeafNodeBehaviour<?, ?> blockEntity) {
                Logistics.get().add(blockEntity.leafNode());
            }
        }

        return InteractionResult.SUCCESS;
    }

    protected InteractionResult disconnectWire(UseOnContext pContext, Wire wire) {
        var stack = pContext.getItemInHand();

        WireEndPoint.removeFrom(stack); //removing the wire should also remove the stored wire endpoint, otherwise our next click creates a new wire from the start of this old wire.

        if (!Objects.requireNonNull(pContext.getPlayer()).getAbilities().instabuild) {
            stack.grow(1); //return the wire item
        }

        var level = pContext.getLevel();

        Wires.get(level).removeWire(wire);
        if (!level.isClientSide) {
            var posA = GlobalPos.of(level.dimension(), wire.from());
            var posB = GlobalPos.of(level.dimension(), wire.to());
            Logistics.get().remove(posA, posB);
        }

        return InteractionResult.SUCCESS;
    }

    protected InteractionResult storeWireEndPoint(UseOnContext pContext) {
        var stack = pContext.getItemInHand();

        var wirePoint = new WireEndPoint(pContext.getClickedPos(), pContext.getLevel().dimension());
        wirePoint.save(stack);

        return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide);
    }
}
