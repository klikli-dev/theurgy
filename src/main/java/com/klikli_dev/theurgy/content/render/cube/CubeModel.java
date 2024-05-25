// SPDX-FileCopyrightText: 2023 Aidan C. Brady
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.cube;

import com.klikli_dev.theurgy.content.render.FluidRenderer;
import com.klikli_dev.theurgy.util.EnumUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * From Mekanism Model3D, see https://github.com/mekanism/Mekanism/blob/d22f6e2028009ed043f8b40c4ea1f7912be3002c/src/main/java/mekanism/client/render/MekanismRenderer.java#L349-L349
 */
public class CubeModel {

    private static final Map<Direction, CubeModel> cachedOverlays = new EnumMap<>(Direction.class);
    private final TextureAtlasSprite[] textures = new TextureAtlasSprite[6];
    private final boolean[] renderSides = {true, true, true, true, true, true};
    public float minX, minY, minZ;
    public float maxX, maxY, maxZ;

    public static CubeModel getOverlayModel(Direction side, TextureAtlasSprite sprite) {
        return cachedOverlays.computeIfAbsent(side, s -> new CubeModel()
                .setTexture(sprite)
                .prepSingleFaceModelSize(side)
        );
    }

    public CubeModel setSideRender(Predicate<Direction> shouldRender) {
        for (Direction direction : EnumUtil.DIRECTIONS) {
            this.setSideRender(direction, shouldRender.test(direction));
        }
        return this;
    }

    public CubeModel setSideRender(Direction side, boolean value) {
        this.renderSides[side.ordinal()] = value;
        return this;
    }

    public CubeModel copy() {
        CubeModel copy = new CubeModel();
        System.arraycopy(this.textures, 0, copy.textures, 0, this.textures.length);
        System.arraycopy(this.renderSides, 0, copy.renderSides, 0, this.renderSides.length);
        return copy.bounds(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    @Nullable
    public TextureAtlasSprite getSpriteToRender(Direction side) {
        int ordinal = side.ordinal();
        return this.renderSides[ordinal] ? this.textures[ordinal] : null;
    }

    public CubeModel shrink(float amount) {
        return this.grow(-amount);
    }

    public CubeModel grow(float amount) {
        return this.bounds(this.minX - amount, this.minY - amount, this.minZ - amount, this.maxX + amount, this.maxY + amount, this.maxZ + amount);
    }

    public CubeModel xBounds(float min, float max) {
        this.minX = min;
        this.maxX = max;
        return this;
    }

    public CubeModel yBounds(float min, float max) {
        this.minY = min;
        this.maxY = max;
        return this;
    }

    public CubeModel zBounds(float min, float max) {
        this.minZ = min;
        this.maxZ = max;
        return this;
    }

    public CubeModel bounds(float min, float max) {
        return this.bounds(min, min, min, max, max, max);
    }

    public CubeModel bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return this.xBounds(minX, maxX)
                .yBounds(minY, maxY)
                .zBounds(minZ, maxZ);
    }

    public CubeModel prepSingleFaceModelSize(Direction face) {
        this.bounds(0, 1);
        return switch (face) {
            case DOWN -> this.yBounds(-0.01F, -0.001F);
            case UP -> this.yBounds(1.001F, 1.01F);
            case NORTH -> this.zBounds(-0.01F, -0.001F);
            case SOUTH -> this.zBounds(1.001F, 1.01F);
            case WEST -> this.xBounds(-0.01F, -0.001F);
            case EAST -> this.xBounds(1.001F, 1.01F);
        };
    }

    public CubeModel prepFlowing(@NotNull FluidStack fluid) {
        TextureAtlasSprite still = FluidRenderer.getFluidTexture(fluid, FluidRenderer.FluidTextureType.STILL);
        TextureAtlasSprite flowing = FluidRenderer.getFluidTexture(fluid, FluidRenderer.FluidTextureType.FLOWING);
        return this.setTextures(still, still, flowing, flowing, flowing, flowing);
    }

    public CubeModel setTexture(Direction side, @Nullable TextureAtlasSprite sprite) {
        this.textures[side.ordinal()] = sprite;
        return this;
    }

    public CubeModel setTexture(TextureAtlasSprite tex) {
        Arrays.fill(this.textures, tex);
        return this;
    }

    public CubeModel setTextures(TextureAtlasSprite down, TextureAtlasSprite up, TextureAtlasSprite north, TextureAtlasSprite south, TextureAtlasSprite west,
                                 TextureAtlasSprite east) {
        this.textures[0] = down;
        this.textures[1] = up;
        this.textures[2] = north;
        this.textures[3] = south;
        this.textures[4] = west;
        this.textures[5] = east;
        return this;
    }

    public interface ModelBoundsSetter {

        CubeModel set(float min, float max);
    }
}