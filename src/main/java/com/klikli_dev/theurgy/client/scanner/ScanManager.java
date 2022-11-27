/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.client.scanner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Based on https://github.com/MightyPirates/Scannable
 */
public class ScanManager {
    private static final ScanManager instance = new ScanManager();
    List<BlockPos> results = new ArrayList<>();
    private Scanner scanner;
    private int scanningTicks = -1;

    public static ScanManager get() {
        return instance;
    }

    public void beginScan(Player player, Block target, int range, int duration) {
        this.cancelScan();

        this.scanner = new Scanner(target);
        this.scanner.initialize(player, player.position(), range, duration);
    }

    public void updateScan(Player player, boolean forceFinish) {
        if(this.scanner == null)
            return;

        final int remainingTicks = this.scanner.totalTicks - this.scanningTicks;
        if (remainingTicks <= 0) {
            return;
        }

        //if we are not forcing we simply tick once
        if (!forceFinish) {
            this.scanner.scan(result -> this.results.add(result));
            this.scanningTicks++;
            return;
        }

        //when forcing we scan through all remaining ticks at once
        for (int i = 0; i < remainingTicks; i++) {
            this.scanner.scan(result -> this.results.add(result));
            this.scanningTicks++;
        }
    }

    public BlockPos finishScan(Player player) {
        this.updateScan(player, true);

        Vec3 scanCenter = player.position();
        this.results
                .sort(Comparator.comparing(result -> result.distToCenterSqr(scanCenter)));
        BlockPos result = !this.results.isEmpty() ? this.results.get(0) : null;
        this.cancelScan();
        return result;
    }

    public void cancelScan() {
        this.scanner = null;
        this.results.clear();
        this.scanningTicks = -1;
    }
}
