/*
 * SPDX-FileCopyrightText: 2022 the original author or authors of Ars Noveau https://github.com/baileyholl/Ars-Nouveau
 *
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.klikli_dev.theurgy.client.particle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

/**
 * Modified class of ElementType: https://github.com/Sirttas/ElementalCraft/blob/b91ca42b3d139904d9754d882a595406bad1bd18/src/main/java/sirttas/elementalcraft/ElementType.java
 */
public class ParticleColor implements Cloneable {

    private final float r;
    private final float g;
    private final int color;
    private final float b;

    public ParticleColor(int r, int g, int b) {
        this.r = r / 255F;
        this.g = g / 255F;
        this.b = b / 255F;
        this.color = (r << 16) | (g << 8) | b;
    }

    public ParticleColor(double red, double green, double blue) {
        this((int) red, (int) green, (int) blue);
    }

    public ParticleColor(float r, float g, float b) {
        this((int) r, (int) g, (int) b);
    }

    public static ParticleColor makeRandomColor(int r, int g, int b, RandomSource random) {
        return new ParticleColor(random.nextInt(r), random.nextInt(g), random.nextInt(b));
    }

    public static ParticleColor fromInt(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;
        return new ParticleColor(r, g, b);
    }

    public static ParticleColor defaultParticleColor() {
        return new ParticleColor(255, 25, 180);
    }

    // Needed because particles can be created over commands
    public static ParticleColor fromString(String string) {
        if (string == null || string.isEmpty())
            return defaultParticleColor();
        String[] arr = string.split(",");
        return new ParticleColor(Integer.parseInt(arr[0].trim()), Integer.parseInt(arr[1].trim()), Integer.parseInt(arr[2].trim()));
    }

    public static ParticleColor deserialize(CompoundTag tag) {
        if (tag == null || tag.isEmpty())
            return defaultParticleColor();
        return new ParticleColor(tag.getInt("r"), tag.getInt("g"), tag.getInt("b"));
    }

    public float getRed() {
        return this.r;
    }

    public float getGreen() {
        return this.g;
    }

    public float getBlue() {
        return this.b;
    }

    public int getColor() {
        return this.color;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        // Wrap and store as int because we don't want to lose precision
        IntWrapper wrapper = this.toWrapper();
        tag.putInt("r", wrapper.r);
        tag.putInt("g", wrapper.g);
        tag.putInt("b", wrapper.b);
        return tag;
    }

    public String toString() {
        return "" + this.r + "," + this.g + "," + this.b;
    }

    public IntWrapper toWrapper() {
        return new IntWrapper(this);
    }

    /**
     * Generates a new color within the max range of the given color.
     */
    public ParticleColor nextColor(RandomSource random) {
        IntWrapper wrapper = this.toWrapper();
        return new ParticleColor(random.nextInt(wrapper.r), random.nextInt(wrapper.g), random.nextInt(wrapper.b));
    }

    @Override
    public ParticleColor clone() {
        try {
            ParticleColor clone = (ParticleColor) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class IntWrapper implements Cloneable {
        public int r;
        public int g;
        public int b;

        public IntWrapper(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public IntWrapper(ParticleColor color) {
            this.r = (int) (color.getRed() * 255.0);
            this.g = (int) (color.getGreen() * 255.0);
            this.b = (int) (color.getBlue() * 255.0);
        }

        public ParticleColor toParticleColor() {
            return new ParticleColor(this.r, this.g, this.b);
        }

        @Override
        public IntWrapper clone() {
            try {
                IntWrapper clone = (IntWrapper) super.clone();
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
}
