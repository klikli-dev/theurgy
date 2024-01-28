package com.klikli_dev.theurgy.content.item;

import com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.LogisticsItemConnectorBlock;
import com.klikli_dev.theurgy.logistics.WireConnection;
import com.klikli_dev.theurgy.logistics.WireEndPoint;
import com.klikli_dev.theurgy.logistics.Wires;
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

        //TODO: implement proper wire point check
        return level.getBlockState(pos).getBlock() instanceof LogisticsItemConnectorBlock;
    }

    protected InteractionResult connectWire(UseOnContext pContext, WireEndPoint wireEndPoint) {
        if(wireEndPoint.level() != pContext.getLevel().dimension())
            return InteractionResult.FAIL; //can't do cables across dimensions

        var pos = pContext.getClickedPos();
        if(wireEndPoint.pos().equals(pos))
            return InteractionResult.FAIL; //can't connect to self

        if(wireEndPoint.pos().distManhattan(pos) > 32) //TODO: implement max wire range properly
            return InteractionResult.FAIL; //can't connect to points too far away

        //TODO: add to graph
        //TODO: leaf node calculation on graph
        //TODO: add to wire renderer

        Wires.get(pContext.getLevel()).addWireConnection(new WireConnection(wireEndPoint.pos(), pos));

        return InteractionResult.SUCCESS;
    }

    protected InteractionResult storeWireEndPoint(UseOnContext pContext) {
        var stack = pContext.getItemInHand();

        var wirePoint = new WireEndPoint(pContext.getClickedPos(), pContext.getLevel().dimension());
        wirePoint.save(stack);

        return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide);
    }
}
