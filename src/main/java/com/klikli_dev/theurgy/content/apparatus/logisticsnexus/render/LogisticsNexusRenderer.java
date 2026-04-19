// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsnexus.render;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.GeoBlockRenderer;
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
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);

        var blockState = blockEntity.getBlockState();
        renderState.addGeckolibData(CONNECTOR_UP, blockState.getValue(LogisticsNexusBlock.UP));
        renderState.addGeckolibData(CONNECTOR_DOWN, blockState.getValue(LogisticsNexusBlock.DOWN));
        renderState.addGeckolibData(CONNECTOR_NORTH, blockState.getValue(LogisticsNexusBlock.NORTH));
        renderState.addGeckolibData(CONNECTOR_SOUTH, blockState.getValue(LogisticsNexusBlock.SOUTH));
        renderState.addGeckolibData(CONNECTOR_EAST, blockState.getValue(LogisticsNexusBlock.EAST));
        renderState.addGeckolibData(CONNECTOR_WEST, blockState.getValue(LogisticsNexusBlock.WEST));
    }

    @Override
    public void adjustModelBonesForRender(RenderPassInfo<RenderState> renderPassInfo, BoneSnapshots snapshots) {
        snapshots.ifPresent("Portal", snapshot -> snapshot.skipRender(true).skipChildrenRender(true));
        snapshots.ifPresent("Top", snapshot -> hideBone(snapshot, renderPassInfo, CONNECTOR_UP));
        snapshots.ifPresent("Bottom", snapshot -> hideBone(snapshot, renderPassInfo, CONNECTOR_DOWN));
        snapshots.ifPresent("North", snapshot -> hideBone(snapshot, renderPassInfo, CONNECTOR_NORTH));
        snapshots.ifPresent("South", snapshot -> hideBone(snapshot, renderPassInfo, CONNECTOR_SOUTH));
        snapshots.ifPresent("East", snapshot -> hideBone(snapshot, renderPassInfo, CONNECTOR_EAST));
        snapshots.ifPresent("West", snapshot -> hideBone(snapshot, renderPassInfo, CONNECTOR_WEST));
    }

    private static void hideBone(com.geckolib.animation.state.BoneSnapshot snapshot, RenderPassInfo<RenderState> renderPassInfo, DataTicket<Boolean> ticket) {
        boolean visible = renderPassInfo.getOrDefaultGeckolibData(ticket, false);
        snapshot.skipRender(!visible).skipChildrenRender(!visible);
    }

    public static class RenderState extends BlockEntityRenderState implements GeoRenderState {
        private final Map<DataTicket<?>, Object> dataMap = new HashMap<>();

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.dataMap;
        }
    }
}
