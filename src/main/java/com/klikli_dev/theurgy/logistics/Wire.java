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
        var z =  (this.start.getZ() + t * (this.end.getZ() - this.start.getZ()));

        // Calculate the total length of the wire
        double length = Math.sqrt(Math.pow(this.end.getX() - this.start.getX(), 2) + Math.pow(this.end.getZ() - this.start.getZ(), 2));

        // Calculate the horizontal position along the wire
        double horizontalPosition = t * length;

        // Calculate the height difference between the start and end points
        double heightDifference = this.end.getY() - this.start.getY();

        // Calculate the vertical position along the wire
        double verticalPosition = heightDifference * t;

        // Adjust the tautness factor to avoid division by zero and other potential issues
        double adjustedTautness = this.tautness == 0 ? 0.0001 : this.tautness;

        // Calculate the y coordinate using the quadratic formula
        var y = this.start.getY() + verticalPosition - adjustedTautness * (horizontalPosition * horizontalPosition + horizontalPosition) * 0.5F + 0.25F;

        return new Vec3(x, y, z);
    }
}
