/*
 * SPDX-FileCopyrightText: 2019 simibubi
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.render.outliner;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class ChasingAABBOutline extends AABBOutline {

    AABB targetBB;
    AABB prevBB;

    public ChasingAABBOutline(AABB bb) {
        super(bb);
        this.prevBB = bb.inflate(0);
        this.targetBB = bb.inflate(0);
    }

    private static AABB interpolateBBs(AABB current, AABB target, float pt) {
        return new AABB(Mth.lerp(pt, current.minX, target.minX), Mth.lerp(pt, current.minY, target.minY),
                Mth.lerp(pt, current.minZ, target.minZ), Mth.lerp(pt, current.maxX, target.maxX),
                Mth.lerp(pt, current.maxY, target.maxY), Mth.lerp(pt, current.maxZ, target.maxZ));
    }

    public void target(AABB target) {
        this.targetBB = target;
    }

    @Override
    public void tick() {
        this.prevBB = this.bb;
        this.setBounds(interpolateBBs(this.bb, this.targetBB, .5f));
    }

    @Override
    public void render(PoseStack ms, MultiBufferSource.BufferSource buffer, Vec3 camera, float pt) {
        this.params.loadColor(this.colorTemp);
        Vector4f color = this.colorTemp;
        int lightmap = this.params.lightmap;
        boolean disableLineNormals = this.params.disableLineNormals;
        this.renderBox(ms, buffer, camera, interpolateBBs(this.prevBB, this.bb, pt), color, lightmap, disableLineNormals);
    }

}
