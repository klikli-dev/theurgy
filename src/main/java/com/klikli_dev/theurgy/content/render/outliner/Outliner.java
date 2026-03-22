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

import java.util.*;

public class Outliner {

    private static final Outliner instance = new Outliner();
    private final Map<Object, OutlineEntry> outlines = Collections.synchronizedMap(new HashMap<>());
    private final Map<Object, OutlineEntry> outlinesView = Collections.unmodifiableMap(this.outlines);

    public static Outliner get() {
        return instance;
    }

    public Outline.OutlineParams showAABB(Object slot, AABB bb, int ttl) {
        this.createAABBOutlineIfMissing(slot, bb);
        ChasingAABBOutline outline = this.getAndRefreshAABB(slot, ttl);
        outline.prevBB = outline.targetBB = outline.bb = bb;
        return outline.getParams();
    }

    public Outline.OutlineParams showAABB(Object slot, AABB bb) {
        this.createAABBOutlineIfMissing(slot, bb);
        ChasingAABBOutline outline = this.getAndRefreshAABB(slot);
        outline.prevBB = outline.targetBB = outline.bb = bb;
        return outline.getParams();
    }

    public Outline.OutlineParams chaseAABB(Object slot, AABB bb) {
        this.createAABBOutlineIfMissing(slot, bb);
        ChasingAABBOutline outline = this.getAndRefreshAABB(slot);
        outline.targetBB = bb;
        return outline.getParams();
    }

    public void keep(Object slot) {
        if (this.outlines.containsKey(slot))
            this.outlines.get(slot).ticksTillRemoval = 1;
    }

    public void remove(Object slot) {
        this.outlines.remove(slot);
    }

    public Optional<Outline.OutlineParams> edit(Object slot) {
        this.keep(slot);
        if (this.outlines.containsKey(slot))
            return Optional.of(this.outlines.get(slot)
                    .getOutline()
                    .getParams());
        return Optional.empty();
    }

    public Map<Object, OutlineEntry> getOutlines() {
        return this.outlinesView;
    }

    // Utility

    private void addOutline(Object slot, Outline outline) {
        this.outlines.put(slot, new OutlineEntry(outline));
    }

    private void createAABBOutlineIfMissing(Object slot, AABB bb) {
        if (!this.outlines.containsKey(slot) || !(this.outlines.get(slot).outline instanceof AABBOutline)) {
            ChasingAABBOutline outline = new ChasingAABBOutline(bb);
            this.addOutline(slot, outline);
        }
    }

    private ChasingAABBOutline getAndRefreshAABB(Object slot) {
        return this.getAndRefreshAABB(slot, 1);
    }

    private ChasingAABBOutline getAndRefreshAABB(Object slot, int ttl) {
        OutlineEntry entry = this.outlines.get(slot);
        entry.ticksTillRemoval = ttl;
        return (ChasingAABBOutline) entry.getOutline();
    }

    // Maintenance

    public void tick() {
        Iterator<OutlineEntry> iterator = this.outlines.values()
                .iterator();
        while (iterator.hasNext()) {
            OutlineEntry entry = iterator.next();
            entry.tick();
            if (!entry.isAlive())
                iterator.remove();
        }
    }

    public void render(PoseStack ms, MultiBufferSource.BufferSource buffer, Vec3 camera, float pt) {
        this.outlines.forEach((key, entry) -> {
            Outline outline = entry.getOutline();
            Outline.OutlineParams params = outline.getParams();
            params.alpha = 1;
            if (entry.isFading()) {
                int prevTicks = entry.ticksTillRemoval + 1;
                float fadeticks = OutlineEntry.FADE_TICKS;
                float lastAlpha = prevTicks >= 0 ? 1 : 1 + (prevTicks / fadeticks);
                float currentAlpha = 1 + (entry.ticksTillRemoval / fadeticks);
                float alpha = Mth.lerp(pt, lastAlpha, currentAlpha);

                params.alpha = alpha * alpha * alpha;
                if (params.alpha < 1 / 8f)
                    return;
            }
            outline.render(ms, buffer, camera, pt);
        });
    }

    public static class OutlineEntry {
        public static final int FADE_TICKS = 8;

        private final Outline outline;
        private int ticksTillRemoval = 1;

        public OutlineEntry(Outline outline) {
            this.outline = outline;
        }

        public Outline getOutline() {
            return this.outline;
        }

        public int getTicksTillRemoval() {
            return this.ticksTillRemoval;
        }

        public boolean isAlive() {
            return this.ticksTillRemoval >= -FADE_TICKS;
        }

        public boolean isFading() {
            return this.ticksTillRemoval < 0;
        }

        public void tick() {
            this.ticksTillRemoval--;
            this.outline.tick();
        }
    }

}
