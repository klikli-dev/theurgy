package com.klikli_dev.theurgy.logistics;

import net.minecraft.core.BlockPos;
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

    default List<BlockCapabilityCache<T, C>> buildTargetCapabilities(List<BlockPos> targets) {
        return targets.stream().map(pos -> BlockCapabilityCache.create(this.capabilityType(), this.serverLevel(), pos, this.getTargetContext(pos), () -> true, () -> {
            Logistics.get().onCapabilityInvalidated(pos, this);
        })).toList();
    }

    C getTargetContext(BlockPos targetPos);

    List<BlockCapabilityCache<T, C>> targetCapabilities();

}
