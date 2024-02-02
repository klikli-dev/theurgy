package com.klikli_dev.theurgy.logistics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.List;

/**
 * A special leaf node whose targets can be inserted into.
 */
public interface LogisticsInserterNode<T, C> extends LogisticsLeafNode<T, C> {
    @Override
    default LeafNodeMode mode() {
        return LeafNodeMode.INSERT;
    }

    default List<BlockCapabilityCache<T, C>> buildTargetCapabilities(List<GlobalPos> targets) {
        var serverLevel = (ServerLevel) this.level();
        return targets.stream().map(pos -> BlockCapabilityCache.create(this.capabilityType(), serverLevel, pos.pos(), this.getTargetContext(pos), () -> true, () -> {
            Logistics.get().onCapabilityInvalidated(pos, this);
        })).toList();
    }

    C getTargetContext(GlobalPos targetPos);

    List<BlockCapabilityCache<T, C>> targetCapabilities();

}
