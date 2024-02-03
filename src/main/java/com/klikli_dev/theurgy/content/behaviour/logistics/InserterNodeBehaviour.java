package com.klikli_dev.theurgy.content.behaviour.logistics;

import com.klikli_dev.theurgy.logistics.Logistics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import java.util.ArrayList;
import java.util.List;

/**
 * A special leaf node whose targets can be inserted into.
 */
public abstract class InserterNodeBehaviour<T, C> extends LeafNodeBehaviour<T, C> {
    List<BlockCapabilityCache<T, C>> targetCapabilities;

    public InserterNodeBehaviour(BlockEntity blockEntity, BlockCapability<T, C> capabilityType) {
        super(blockEntity, capabilityType);

        this.targetCapabilities = new ArrayList<>();
    }

    @Override
    public LeafNodeMode mode() {
        return LeafNodeMode.INSERT;
    }

    @Override
    public void onLoad() {
        this.buildTargetCapabilities(this.targets());
        //TODO: they also need to be called if targets change
        super.onLoad();
    }

    public List<BlockCapabilityCache<T, C>> buildTargetCapabilities(List<BlockPos> targets) {
        var serverLevel = (ServerLevel) this.level();
        return targets.stream().map(target -> BlockCapabilityCache.create(this.capabilityType(), serverLevel, target, this.getTargetContext(target), () -> true, () -> {
            Logistics.get().onCapabilityInvalidated(GlobalPos.of(serverLevel.dimension(), target), this);
        })).toList();
    }

    protected abstract C getTargetContext(BlockPos targetPos);

    public List<BlockCapabilityCache<T, C>> targetCapabilities(){
        return this.targetCapabilities;
    }

}
