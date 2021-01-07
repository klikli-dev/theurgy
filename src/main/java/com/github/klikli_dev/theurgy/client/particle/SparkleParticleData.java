/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.client.particle;

import com.github.klikli_dev.theurgy.registry.ParticleRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class SparkleParticleData implements IParticleData {
    //region Fields
    public static final IDeserializer<SparkleParticleData> DESERIALIZER =
            new IDeserializer<SparkleParticleData>() {
                //region Overrides
                @Override
                public SparkleParticleData deserialize(ParticleType<SparkleParticleData> type,
                                                       StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    float red = reader.readFloat();
                    reader.expect(' ');
                    float green = reader.readFloat();
                    reader.expect(' ');
                    float blue = reader.readFloat();
                    reader.expect(' ');
                    float targetX = reader.readFloat();
                    reader.expect(' ');
                    float targetY = reader.readFloat();
                    reader.expect(' ');
                    float targetZ = reader.readFloat();
                    SparkleParticleData data = new SparkleParticleData();
                    data.red = red;
                    data.green = green;
                    data.blue = blue;
                    data.targetX = targetX;
                    data.targetY = targetY;
                    data.targetZ = targetZ;
                    return data;
                }

                @Override
                public SparkleParticleData read(ParticleType<SparkleParticleData> type,
                                                PacketBuffer buf) {
                    float red = buf.readFloat();
                    float green = buf.readFloat();
                    float blue = buf.readFloat();
                    float targetX = buf.readFloat();
                    float targetY = buf.readFloat();
                    float targetZ = buf.readFloat();
                    SparkleParticleData data = new SparkleParticleData();
                    data.red = red;
                    data.green = green;
                    data.blue = blue;
                    data.targetX = targetX;
                    data.targetY = targetY;
                    data.targetZ = targetZ;
                    return data;
                }
                //endregion Overrides
            };

    public float red;
    public float green;
    public float blue;
    public float targetX;
    public float targetY;
    public float targetZ;
    //endregion Fields


    //region Initialization
    public SparkleParticleData() {
        super();
    }

    public SparkleParticleData(float red, float green, float blue, float targetX, float targetY, float targetZ) {
        super();
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }
    //endregion Initialization

    //region Overrides
    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.SPARKLE.get();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
        buffer.writeFloat(this.targetX);
        buffer.writeFloat(this.targetY);
        buffer.writeFloat(this.targetZ);
    }

    @Override
    public String getParameters() {
        return this.getClass().getSimpleName() + ":internal";
    }
    //endregion Overrides

    //region Static Methods
    public static Codec<SparkleParticleData> codecFor(ParticleType<?> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
                Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
                Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
                Codec.FLOAT.fieldOf("targetX").forGetter(d -> d.targetX),
                Codec.FLOAT.fieldOf("targetY").forGetter(d -> d.targetY),
                Codec.FLOAT.fieldOf("targetZ").forGetter(d -> d.targetY)
        ).apply(instance, (red, green, blue, targetX, targetY, targetZ) -> {
            SparkleParticleData data = new SparkleParticleData();
            data.red = red;
            data.green = green;
            data.blue = blue;
            data.targetX = targetX;
            data.targetY = targetY;
            data.targetZ = targetZ;
            return data;
        }));
    }
    //endregion Static Methods
}
