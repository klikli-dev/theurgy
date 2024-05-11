// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.wire;

import com.klikli_dev.theurgy.content.behaviour.logistics.HasLeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasWireEndPoint;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.logistics.Wire;
import com.klikli_dev.theurgy.logistics.WireEndPoint;
import com.klikli_dev.theurgy.logistics.Wires;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class WireItem extends Item {
    public WireItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var stack = pContext.getItemInHand();

        if (!this.isWireEndPoint(pContext)) {
            return InteractionResult.FAIL;
        }

        var wirePoint = WireEndPoint.load(stack);
        if (wirePoint != null) {
            return this.connectWire(pContext, wirePoint);
        } else {
            return this.storeWireEndPoint(pContext);
        }
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

        if (wireEndPoint.pos().distManhattan(pos) > 32) //TODO: implement max wire range properly
            return InteractionResult.FAIL; //can't connect to points too far away

        var stack = pContext.getItemInHand();
        WireEndPoint.removeFrom(stack);

        if (!pContext.getPlayer().getAbilities().instabuild) {
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

    protected InteractionResult storeWireEndPoint(UseOnContext pContext) {
        var stack = pContext.getItemInHand();

        var wirePoint = new WireEndPoint(pContext.getClickedPos(), pContext.getLevel().dimension());
        wirePoint.save(stack);

        return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide);
    }
}
