package com.klikli_dev.theurgy.logistics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public record Wire(BlockPos start, BlockPos end, double tautness) {

    @Override
    public BlockPos start() {
        return this.start;
    }

    @Override
    public BlockPos end() {
        return this.end;
    }

    /**
     * Returns the tautness factor of the wire = how much slack it has.
     */
    @Override
    public double tautness() {
        return this.tautness;
    }

    /**
     * Returns the point on the wire at the given position.
     *
     * @param t The position along the wire. 0.0 represents the start of the wire, 1.0 represents the end of the wire.
     * @return The point on the wire at the given position.
     */
    public Vec3 getPointAt(double t) {
        var x =  (this.start.getX() + t * (this.end.getX() - this.start.getX()));
        var y =  (this.start.getY() + t * (this.end.getY() - this.start.getY()) - this.tautness * Math.pow(t, 2) * (this.end.getY() - this.start.getY()));
        var z =  (this.start.getZ() + t * (this.end.getZ() - this.start.getZ()));
        return new Vec3(x, y, z);
    }
}
