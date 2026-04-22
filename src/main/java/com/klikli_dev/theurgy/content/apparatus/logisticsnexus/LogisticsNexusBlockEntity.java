// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import com.klikli_dev.theurgy.content.behaviour.logistics.LogisticsNode;
import com.klikli_dev.theurgy.content.apparatus.logisticsconnectionnode.LogisticsConnectionNodeBlock;
import com.klikli_dev.theurgy.logistics.Logistics;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.DataComponentRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import java.util.UUID;

public class LogisticsNexusBlockEntity extends BlockEntity implements GeoBlockEntity, LogisticsNode {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private UUID nexusId;

    public LogisticsNexusBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LOGISTICS_NEXUS.get(), pos, state);
    }

    public UUID nexusId() {
        return this.nexusId;
    }

    public void ensureNexusId() {
        if (this.nexusId == null) {
            this.setNexusId(UUID.randomUUID());
        }
    }

    public void refreshNetworkState() {
        if (this.level == null || this.level.isClientSide() || this.nexusId == null) {
            return;
        }

        var logistics = Logistics.get();
        var self = GlobalPos.of(this.level.dimension(), this.getBlockPos());
        logistics.add(self);

        var partner = logistics.findPairedNexus(self, this.nexusId);
        if (partner != null) {
            logistics.add(self, partner);
        }

        for (var direction : Direction.values()) {
            var neighborPos = this.getBlockPos().relative(direction);
            var neighborState = this.level.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof LogisticsConnectionNodeBlock && neighborState.getValue(LogisticsConnectionNodeBlock.FACING) == direction) {
                logistics.add(self, GlobalPos.of(this.level.dimension(), neighborPos));
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide()) {
            if (this.nexusId == null) {
                this.setNexusId(UUID.randomUUID());
            } else {
                Logistics.get().registerNexus(GlobalPos.of(this.level.dimension(), this.getBlockPos()), this.nexusId);
                this.refreshNetworkState();
            }
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        var itemNexusId = components.get(DataComponentRegistry.LOGISTICS_NEXUS_ID.get());
        if (itemNexusId != null) {
            this.setNexusId(itemNexusId);
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (this.nexusId != null) {
            components.set(DataComponentRegistry.LOGISTICS_NEXUS_ID.get(), this.nexusId);
        }
    }

    public void setNexusId(UUID nexusId) {
        var oldNexusId = this.nexusId;
        this.nexusId = nexusId;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            var self = GlobalPos.of(this.level.dimension(), this.getBlockPos());
            var logistics = Logistics.get();
            if (oldNexusId != null) {
                logistics.unregisterNexus(self, oldNexusId);
                logistics.remove(self);
            }
            logistics.registerNexus(self, nexusId);
            this.refreshNetworkState();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.level != null && !this.level.isClientSide() && this.nexusId != null) {
            Logistics.get().unregisterNexus(GlobalPos.of(this.level.dimension(), pos), this.nexusId);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public List<Pair<BlockPos, Integer>> getStatusHighlights() {
        if (this.level == null || this.level.isClientSide() || this.nexusId == null) {
            return List.of();
        }

        var partner = Logistics.get().findPairedNexus(GlobalPos.of(this.level.dimension(), this.getBlockPos()), this.nexusId);
        if (partner != null && partner.dimension().equals(this.level.dimension())) {
            return List.of(Pair.of(partner.pos(), 0xFF00FF));
        }

        return List.of();
    }
}
