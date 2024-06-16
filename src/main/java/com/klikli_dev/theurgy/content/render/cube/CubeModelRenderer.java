// SPDX-FileCopyrightText: 2023 Aidan C. Brady
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.cube;

import com.klikli_dev.theurgy.util.EnumUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;

/**
 * From Mekanism RenderResizableCuboid, see https://github.com/mekanism/Mekanism/blob/d22f6e2028009ed043f8b40c4ea1f7912be3002c/src/main/java/mekanism/client/render/RenderResizableCuboid.java
 * Adapted from Mantle's FluidRenderer and Tinker's SmelteryTankRenderer
 */
public class CubeModelRenderer {

    /**
     * Used to not need to create multiple arrays when we just want to fill it differently at times, and given rendering TERs is not multithreaded it is perfectly safe to
     * just use one backing "temporary" array.
     */
    private static final int[] combinedARGB = new int[EnumUtil.DIRECTIONS.length];
    private static final Vector3f setNormal = new Vector3f(1, 1, 1).normalize();
    private static final int X_AXIS_MASK = 1 << Axis.X.ordinal();
    private static final int Y_AXIS_MASK = 1 << Axis.Y.ordinal();
    private static final int Z_AXIS_MASK = 1 << Axis.Z.ordinal();

    private CubeModelRenderer() {
    }

    public static void renderCube(CubeModel object, PoseStack matrix, VertexConsumer buffer, int argb, int light, int overlay,
                                  FaceDisplay faceDisplay, Camera camera) {
        renderCube(object, matrix, buffer, argb, light, overlay, faceDisplay, camera, null);
    }

    public static void renderCube(CubeModel object, PoseStack matrix, VertexConsumer buffer, int[] colors, int light, int overlay,
                                  FaceDisplay faceDisplay, Camera camera) {
        renderCube(object, matrix, buffer, colors, light, overlay, faceDisplay, camera, null);
    }

    public static void renderCube(CubeModel cube, PoseStack matrix, VertexConsumer buffer, int argb, int light, int overlay, FaceDisplay faceDisplay, Camera camera,
                                  @Nullable Vec3 renderPos) {
        Arrays.fill(combinedARGB, argb);
        renderCube(cube, matrix, buffer, combinedARGB, light, overlay, faceDisplay, camera, renderPos);
    }

    /**
     * @implNote Based off of Tinker's
     */
    public static void renderCube(CubeModel cube, PoseStack matrix, VertexConsumer buffer, int[] colors, int light, int overlay, FaceDisplay faceDisplay, Camera camera, @Nullable Vec3 renderPos) {
        TextureAtlasSprite[] sprites = new TextureAtlasSprite[6];
        int axisToRender = 0;
        //TODO: Eventually try not rendering faces that are covered by things? At the very least for things like multiblocks
        // when one face is entirely casing and not glass
        if (renderPos != null && faceDisplay != FaceDisplay.BOTH) {
            //If we know the position this model is based around in the world, and we aren't displaying both faces
            // then calculate to see if we can skip rendering any faces due to the camera not facing them
            Vec3 camPos = camera.getPosition();
            Vec3 minPos = renderPos.add(cube.minX, cube.minY, cube.minZ);
            Vec3 maxPos = renderPos.add(cube.maxX, cube.maxY, cube.maxZ);
            for (Direction direction : EnumUtil.DIRECTIONS) {
                TextureAtlasSprite sprite = cube.getSpriteToRender(direction);
                if (sprite != null) {
                    Axis axis = direction.getAxis();
                    AxisDirection axisDirection = direction.getAxisDirection();
                    double planeLocation = switch (axisDirection) {
                        case POSITIVE -> axis.choose(maxPos.x, maxPos.y, maxPos.z);
                        case NEGATIVE -> axis.choose(minPos.x, minPos.y, minPos.z);
                    };
                    double cameraPosition = axis.choose(camPos.x, camPos.y, camPos.z);
                    //Check whether the camera's position is past the side that it can render on for the face
                    // that we want to be rendering
                    if (faceDisplay.front == (axisDirection == AxisDirection.POSITIVE)) {
                        if (cameraPosition >= planeLocation) {
                            sprites[direction.ordinal()] = sprite;
                            axisToRender |= 1 << axis.ordinal();
                        }
                    } else if (cameraPosition <= planeLocation) {
                        sprites[direction.ordinal()] = sprite;
                        axisToRender |= 1 << axis.ordinal();
                    }
                }
            }
        } else {
            for (Direction direction : EnumUtil.DIRECTIONS) {
                TextureAtlasSprite sprite = cube.getSpriteToRender(direction);
                if (sprite != null) {
                    sprites[direction.ordinal()] = sprite;
                    axisToRender |= 1 << direction.getAxis().ordinal();
                }
            }
        }
        if (axisToRender == 0) {
            //Skip rendering if no sides are meant to be rendered
            return;
        }
        //TODO: Further attempt to fix z-fighting at larger distances if we make it not render the sides when it is in a solid block
        // that may improve performance some, but definitely would reduce/remove the majority of remaining z-fighting that is going on
        //Shift it so that the min values are all greater than or equal to zero as the various drawing code
        // has some issues when it comes to handling negative numbers
        int xShift = Mth.floor(cube.minX);
        int yShift = Mth.floor(cube.minY);
        int zShift = Mth.floor(cube.minZ);
        float minX = cube.minX - xShift;
        float minY = cube.minY - yShift;
        float minZ = cube.minZ - zShift;
        float maxX = cube.maxX - xShift;
        float maxY = cube.maxY - yShift;
        float maxZ = cube.maxZ - zShift;
        int xDelta = calculateDelta(minX, maxX);
        int yDelta = calculateDelta(minY, maxY);
        int zDelta = calculateDelta(minZ, maxZ);
        float[] xBounds = getBlockBounds(xDelta, minX, maxX);
        float[] yBounds = getBlockBounds(yDelta, minY, maxY);
        float[] zBounds = getBlockBounds(zDelta, minZ, maxZ);

        matrix.pushPose();
        matrix.translate(xShift, yShift, zShift);
        PoseStack.Pose lastMatrix = matrix.last();
        Matrix4f matrix4f = lastMatrix.pose();
        NormalData setNormal = new NormalData(lastMatrix.setNormal(), setNormal, faceDisplay);
        Vector3f from = new Vector3f();
        Vector3f to = new Vector3f();

        //Calculate if we can speed up any of the for loops by skipping ones that will never draw anything
        int xIncrement = 1;
        int yIncrement = 1;
        int zIncrement = 1;
        if (axisToRender == X_AXIS_MASK) {
            //Only sides are on the x-axis, we can skip from min to max x
            xIncrement = Math.max(xDelta, 1);
        } else if (axisToRender == Y_AXIS_MASK) {
            //Only sides are on the y-axis, we can skip from min to max y
            yIncrement = Math.max(yDelta, 1);
        } else if (axisToRender == Z_AXIS_MASK) {
            //Only sides are on the z-axis, we can skip from min to max z
            zIncrement = Math.max(zDelta, 1);
        }

        // render each side
        for (int y = 0; y <= yDelta; y += yIncrement) {
            TextureAtlasSprite upSprite = y == yDelta ? sprites[Direction.UP.ordinal()] : null;
            TextureAtlasSprite downSprite = y == 0 ? sprites[Direction.DOWN.ordinal()] : null;
            from.y = yBounds[y];
            to.y = yBounds[y + 1];
            for (int z = 0; z <= zDelta; z += zIncrement) {
                TextureAtlasSprite northSprite = z == 0 ? sprites[Direction.NORTH.ordinal()] : null;
                TextureAtlasSprite southSprite = z == zDelta ? sprites[Direction.SOUTH.ordinal()] : null;
                from.z = zBounds[z];
                to.z = zBounds[z + 1];
                for (int x = 0; x <= xDelta; x += xIncrement) {
                    TextureAtlasSprite westSprite = x == 0 ? sprites[Direction.WEST.ordinal()] : null;
                    TextureAtlasSprite eastSprite = x == xDelta ? sprites[Direction.EAST.ordinal()] : null;
                    //Set bounds
                    from.x = xBounds[x];
                    to.x = xBounds[x + 1];
                    putTexturedQuad(buffer, matrix4f, westSprite, from, to, Direction.WEST, colors, light, overlay, faceDisplay, setNormal);
                    putTexturedQuad(buffer, matrix4f, eastSprite, from, to, Direction.EAST, colors, light, overlay, faceDisplay, setNormal);
                    putTexturedQuad(buffer, matrix4f, northSprite, from, to, Direction.NORTH, colors, light, overlay, faceDisplay, setNormal);
                    putTexturedQuad(buffer, matrix4f, southSprite, from, to, Direction.SOUTH, colors, light, overlay, faceDisplay, setNormal);
                    putTexturedQuad(buffer, matrix4f, upSprite, from, to, Direction.UP, colors, light, overlay, faceDisplay, setNormal);
                    putTexturedQuad(buffer, matrix4f, downSprite, from, to, Direction.DOWN, colors, light, overlay, faceDisplay, setNormal);
                }
            }
        }
        matrix.popPose();
    }

    /**
     * @implNote From Tinker's
     */
    private static float[] getBlockBounds(int delta, float start, float end) {
        float[] bounds = new float[2 + delta];
        bounds[0] = start;
        int offset = (int) start;
        for (int i = 1; i <= delta; i++) {
            bounds[i] = i + offset;
        }
        bounds[delta + 1] = end;
        return bounds;
    }

    /**
     * @implNote From Tinker's
     */
    private static int calculateDelta(float min, float max) {
        //The texture can stretch over more blocks than the subtracted height is if min's decimal is bigger than max's decimal (causing setUv over 1)
        // ignoring the decimals prevents this, as yd then equals exactly how many ints are between the two
        // for example, if max = 5.1 and min = 2.3, 2.8 (which rounds to 2), with the face array becoming 2.3, 3, 4, 5.1
        int delta = (int) (max - (int) min);
        // except in the rare case of max perfectly aligned with the block, causing the top face to render multiple times
        // for example, if max = 3 and min = 1, the values of the face array become 1, 2, 3, 3 as we then have middle ints
        if (max % 1d == 0) {
            delta--;
        }
        return delta;
    }

    /**
     * @implNote From Mantle with some adjustments
     */
    private static void putTexturedQuad(VertexConsumer buffer, Matrix4f matrix, @Nullable TextureAtlasSprite spriteInfo, Vector3f from, Vector3f to, Direction face, int[] colors,
                                        int light, int overlay, FaceDisplay faceDisplay, NormalData setNormal) {
        if (spriteInfo == null) {
            return;
        }
        // start with texture coordinates
        float x1 = from.x(), y1 = from.y(), z1 = from.z();
        float x2 = to.x(), y2 = to.y(), z2 = to.z();
        // choose setUv based on opposite two axis
        Bounds uBounds, vBounds;
        switch (face.getAxis()) {
            case Z -> {
                uBounds = Bounds.calculate(x2, x1);
                vBounds = Bounds.calculate(y1, y2);
            }
            case X -> {
                uBounds = Bounds.calculate(z2, z1);
                vBounds = Bounds.calculate(y1, y2);
            }
            default -> {
                uBounds = Bounds.calculate(x1, x2);
                vBounds = Bounds.calculate(z2, z1);
            }
        }

        float minU = spriteInfo.getU(uBounds.min());
        float maxU = spriteInfo.getU(uBounds.max());
        //Flip V
        float minV = spriteInfo.getV(1 - vBounds.max());
        float maxV = spriteInfo.getV(1 - vBounds.min());
        int argb = colors[face.ordinal()];
        // add quads
        switch (face) {
            case DOWN -> drawFace(buffer, matrix, argb, minU, maxU, minV, maxV, light, overlay, faceDisplay, setNormal,
                    x1, y1, z2,
                    x1, y1, z1,
                    x2, y1, z1,
                    x2, y1, z2);
            case UP -> drawFace(buffer, matrix, argb, minU, maxU, minV, maxV, light, overlay, faceDisplay, setNormal,
                    x1, y2, z1,
                    x1, y2, z2,
                    x2, y2, z2,
                    x2, y2, z1);
            case NORTH -> drawFace(buffer, matrix, argb, minU, maxU, minV, maxV, light, overlay, faceDisplay, setNormal,
                    x1, y1, z1,
                    x1, y2, z1,
                    x2, y2, z1,
                    x2, y1, z1);
            case SOUTH -> drawFace(buffer, matrix, argb, minU, maxU, minV, maxV, light, overlay, faceDisplay, setNormal,
                    x2, y1, z2,
                    x2, y2, z2,
                    x1, y2, z2,
                    x1, y1, z2);
            case WEST -> drawFace(buffer, matrix, argb, minU, maxU, minV, maxV, light, overlay, faceDisplay, setNormal,
                    x1, y1, z2,
                    x1, y2, z2,
                    x1, y2, z1,
                    x1, y1, z1);
            case EAST -> drawFace(buffer, matrix, argb, minU, maxU, minV, maxV, light, overlay, faceDisplay, setNormal,
                    x2, y1, z1,
                    x2, y2, z1,
                    x2, y2, z2,
                    x2, y1, z2);
        }
    }

    private static void drawFace(VertexConsumer buffer, Matrix4f matrix, int argb, float minU, float maxU, float minV, float maxV, int light, int overlay,
                                 FaceDisplay faceDisplay, NormalData setNormal,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float x3, float y3, float z3,
                                 float x4, float y4, float z4) {
        int red = FastColor.ARGB32.red(argb);
        int green = FastColor.ARGB32.green(argb);
        int blue = FastColor.ARGB32.blue(argb);
        int alpha = FastColor.ARGB32.alpha(argb);
        if (faceDisplay.front) {
            buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha).setUv(minU, maxV).setOverlay(overlay).setLight(light).setNormal(setNormal.front.x(), setNormal.front.y(), setNormal.front.z());
            buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha).setUv(minU, minV).setOverlay(overlay).setLight(light).setNormal(setNormal.front.x(), setNormal.front.y(), setNormal.front.z());
            buffer.addVertex(matrix, x3, y3, z3).setColor(red, green, blue, alpha).setUv(maxU, minV).setOverlay(overlay).setLight(light).setNormal(setNormal.front.x(), setNormal.front.y(), setNormal.front.z());
            buffer.addVertex(matrix, x4, y4, z4).setColor(red, green, blue, alpha).setUv(maxU, maxV).setOverlay(overlay).setLight(light).setNormal(setNormal.front.x(), setNormal.front.y(), setNormal.front.z());
        }
        if (faceDisplay.back) {
            buffer.addVertex(matrix, x4, y4, z4).setColor(red, green, blue, alpha).setUv(maxU, maxV).setOverlay(overlay).setLight(light).setNormal(setNormal.back.x(), setNormal.back.y(), setNormal.back.z());
            buffer.addVertex(matrix, x3, y3, z3).setColor(red, green, blue, alpha).setUv(maxU, minV).setOverlay(overlay).setLight(light).setNormal(setNormal.back.x(), setNormal.back.y(), setNormal.back.z());
            buffer.addVertex(matrix, x2, y2, z2).setColor(red, green, blue, alpha).setUv(minU, minV).setOverlay(overlay).setLight(light).setNormal(setNormal.back.x(), setNormal.back.y(), setNormal.back.z());
            buffer.addVertex(matrix, x1, y1, z1).setColor(red, green, blue, alpha).setUv(minU, maxV).setOverlay(overlay).setLight(light).setNormal(setNormal.back.x(), setNormal.back.y(), setNormal.back.z());
        }
    }

    public enum FaceDisplay {
        FRONT(true, false),
        BACK(false, true),
        BOTH(true, true);

        private final boolean front;
        private final boolean back;

        FaceDisplay(boolean front, boolean back) {
            this.front = front;
            this.back = back;
        }
    }

    private record Bounds(float min, float max) {

        public static Bounds calculate(float min, float max) {
            // wrap setUv to be between 0 and 1, assumes none of the positions lie outside the 0, 0, 0 to 1, 1, 1 range
            // however, one of them might be exactly on the 1.0 bound, that one should be set to 1 instead of left at 0
            boolean bigger = min > max;
            min = min % 1;
            max = max % 1;
            if (bigger) {
                return new Bounds(min == 0 ? 1 : min, max);
            }
            return new Bounds(min, max == 0 ? 1 : max);
        }
    }

    /**
     * Used to only have to calculate normals once rather than transforming based on the matrix for every vertex call. If a face shouldn't be displayed the setNormal vector
     * will be zero.
     */
    private record NormalData(Vector3f front, Vector3f back) {

        private NormalData(Matrix3f normalMatrix, Vector3f setNormal, FaceDisplay faceDisplay) {
            this(faceDisplay.front ? calculate(normalMatrix, setNormal.x(), setNormal.y(), setNormal.z()) : new Vector3f(),
                    faceDisplay.back ? calculate(normalMatrix, -setNormal.x(), -setNormal.y(), -setNormal.z()) : new Vector3f());
        }

        private static Vector3f calculate(Matrix3f normalMatrix, float x, float y, float z) {
            Vector3f matrixAdjustedNormal = new Vector3f(x, y, z);
            return matrixAdjustedNormal.mul(normalMatrix);
        }
    }
}