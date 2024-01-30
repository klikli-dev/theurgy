// SPDX-FileCopyrightText: 2020 Commoble
//
// SPDX-License-Identifier: MIT

//See upstream https://github.com/Commoble/morered/blob/HEAD/src/main/java/commoble/morered/wire_post/SlackInterpolator.java

package com.klikli_dev.theurgy.logistics;

import net.minecraft.world.phys.Vec3;

public class WireSlackHelper {
    public static Vec3[] getInterpolatedDifferences(Vec3 vector) {
        int points = 17; // 16 segments
        Vec3[] list = new Vec3[points];

        double dx = vector.x();
        double dy = vector.y();
        double dz = vector.z();

        double sag = 0.3;

        for (int point = 0; point < points; point++) {
            double startLerp = getFractionalLerp(point, points - 1);
            double startYLerp = getYLerp(startLerp, dy);
//            list[point] = new Vec3(startLerp * dx, startYLerp * dy, startLerp * dz);

            // Use a sine function for the sag factor
//            double sagFactor = sag * Math.sin(Math.PI * startLerp);
//
//            // Subtract the sag factor from the y-coordinate calculation
//            double y = dy != 0 ? (startYLerp - sagFactor) * dy : -sagFactor;
//
//            list[point] = new Vec3(startLerp * dx, y, startLerp * dz);

            double sagFactor = sag * (1 - (4 * Math.pow(startLerp - 0.5, 2)));

            // Subtract the sag factor from the y-coordinate calculation
            double y = dy != 0 ? (startYLerp - sagFactor) * dy : -sagFactor;

            list[point] = new Vec3(startLerp * dx, y, startLerp * dz);

        }

        return list;
    }

    public static Vec3[] getInterpolatedPoints(Vec3 lower, Vec3 upper) {
        Vec3 diff = upper.subtract(lower);
        Vec3[] diffs = getInterpolatedDifferences(diff);
        Vec3[] points = new Vec3[diffs.length];
        for (int i = 0; i < points.length; i++) {
            points[i] = lower.add(diffs[i]);
        }
        return points;
    }

    public static double getFractionalLerp(int current, int max) {
        return (double) current / (double) max;
    }

    public static double getYLerp(double lerp, double dY) {
        return Math.pow(lerp, Math.log(Math.abs(dY) + 3));
    }
}
