package com.klikli_dev.theurgy.content.behaviour.logistics;

import com.klikli_dev.theurgy.logistics.Logistics;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.List;

/**
 * A special leaf node whose targets can be inserted into.
 */
public abstract class InserterNodeBehaviour<T, C> extends LeafNodeBehaviour<T, C> {
    @Override
    public LeafNodeMode mode() {
        return LeafNodeMode.INSERT;
    }

    public List<BlockCapabilityCache<T, C>> buildTargetCapabilities(List<GlobalPos> targets) {
        var serverLevel = (ServerLevel) this.level();
        return targets.stream().map(target -> BlockCapabilityCache.create(this.capabilityType(), serverLevel, target.pos(), this.getTargetContext(target), () -> true, () -> {
            Logistics.get().onCapabilityInvalidated(target, this);
        })).toList();
    }

    protected abstract C getTargetContext(GlobalPos targetPos);

    public abstract List<BlockCapabilityCache<T, C>> targetCapabilities();

}
