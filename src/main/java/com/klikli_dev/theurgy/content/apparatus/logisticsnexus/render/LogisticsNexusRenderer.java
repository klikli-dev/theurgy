// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus.render;

import com.geckolib.constant.DataTickets;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.LogisticsNexusBlock;
import com.klikli_dev.theurgy.content.apparatus.logisticsnexus.LogisticsNexusBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LogisticsNexusRenderer extends GeoBlockRenderer<LogisticsNexusBlockEntity, LogisticsNexusRenderer.RenderState> {
    public static final DataTicket<Boolean> CONNECTOR_UP = DataTicket.create("theurgy_logistics_nexus_up", Boolean.class);
    public static final DataTicket<Boolean> CONNECTOR_DOWN = DataTicket.create("theurgy_logistics_nexus_down", Boolean.class);
    public static final DataTicket<Boolean> CONNECTOR_NORTH = DataTicket.create("theurgy_logistics_nexus_north", Boolean.class);
    public static final DataTicket<Boolean> CONNECTOR_SOUTH = DataTicket.create("theurgy_logistics_nexus_south", Boolean.class);
    public static final DataTicket<Boolean> CONNECTOR_EAST = DataTicket.create("theurgy_logistics_nexus_east", Boolean.class);
    public static final DataTicket<Boolean> CONNECTOR_WEST = DataTicket.create("theurgy_logistics_nexus_west", Boolean.class);

    public LogisticsNexusRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new LogisticsNexusModel<>());
        this.withRenderLayer(new LogisticsNexusPortalLayer(this));
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            LogisticsNexusBlockEntity blockEntity,
            RenderState renderState,
            float partialTick,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderState.extractBase(blockEntity, renderState, breakProgress);
        long instanceId = this.getInstanceId(blockEntity, null);
        renderState.addGeckolibData(DataTickets.ANIMATABLE_INSTANCE_ID, instanceId);
        renderState.addGeckolibData(DataTickets.ANIMATABLE_MANAGER, blockEntity.getAnimatableInstanceCache().getManagerForId(instanceId));
        this.fillRenderState(blockEntity, null, renderState, partialTick);

        var blockState = blockEntity.getBlockState();
        renderState.up = blockState.getValue(LogisticsNexusBlock.UP);
        renderState.down = blockState.getValue(LogisticsNexusBlock.DOWN);
        renderState.north = blockState.getValue(LogisticsNexusBlock.NORTH);
        renderState.south = blockState.getValue(LogisticsNexusBlock.SOUTH);
        renderState.east = blockState.getValue(LogisticsNexusBlock.EAST);
        renderState.west = blockState.getValue(LogisticsNexusBlock.WEST);
    }

    @Override
    public void adjustModelBonesForRender(RenderPassInfo<RenderState> renderPassInfo, BoneSnapshots snapshots) {
        snapshots.ifPresent("Portal", snapshot -> snapshot.skipRender(true).skipChildrenRender(true));
        snapshots.ifPresent("Top", snapshot -> snapshot.skipRender(!renderPassInfo.renderState().up).skipChildrenRender(!renderPassInfo.renderState().up));
        snapshots.ifPresent("Bottom", snapshot -> snapshot.skipRender(!renderPassInfo.renderState().down).skipChildrenRender(!renderPassInfo.renderState().down));
        snapshots.ifPresent("North", snapshot -> snapshot.skipRender(!renderPassInfo.renderState().north).skipChildrenRender(!renderPassInfo.renderState().north));
        snapshots.ifPresent("South", snapshot -> snapshot.skipRender(!renderPassInfo.renderState().south).skipChildrenRender(!renderPassInfo.renderState().south));
        snapshots.ifPresent("East", snapshot -> snapshot.skipRender(!renderPassInfo.renderState().east).skipChildrenRender(!renderPassInfo.renderState().east));
        snapshots.ifPresent("West", snapshot -> snapshot.skipRender(!renderPassInfo.renderState().west).skipChildrenRender(!renderPassInfo.renderState().west));
    }

    public static class RenderState extends BlockEntityRenderState implements GeoRenderState {
        private final Map<DataTicket<?>, Object> dataMap = new HashMap<>();
        boolean up;
        boolean down;
        boolean north;
        boolean south;
        boolean east;
        boolean west;

        @Override
        public <D> void addGeckolibData(DataTicket<D> dataTicket, D data) {
            this.dataMap.put(dataTicket, data);
        }

        @Override
        public boolean hasGeckolibData(DataTicket<?> dataTicket) {
            return this.dataMap.containsKey(dataTicket);
        }

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.dataMap;
        }
    }
}
