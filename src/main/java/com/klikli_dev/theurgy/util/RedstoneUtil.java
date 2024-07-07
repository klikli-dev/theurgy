package com.klikli_dev.theurgy.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class RedstoneUtil {

    public static boolean hasNeighborSignal(Level level, BlockPos pPos, Direction ignore) {
        if (ignore != Direction.DOWN && level.getSignal(pPos.below(), Direction.DOWN) > 0) {
            return true;
        } else if (ignore != Direction.UP && level.getSignal(pPos.above(), Direction.UP) > 0) {
            return true;
        } else if (ignore != Direction.NORTH && level.getSignal(pPos.north(), Direction.NORTH) > 0) {
            return true;
        } else if (ignore != Direction.SOUTH && level.getSignal(pPos.south(), Direction.SOUTH) > 0) {
            return true;
        } else if(ignore != Direction.WEST && level.getSignal(pPos.west(), Direction.WEST) > 0) {
            return true;
        } else if(ignore != Direction.EAST && level.getSignal(pPos.east(), Direction.EAST) > 0){
            return true;
        } else {
            return false;
        }
    }
}
