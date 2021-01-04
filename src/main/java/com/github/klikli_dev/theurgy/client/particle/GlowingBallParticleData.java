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
import net.minecraft.client.particle.Particle;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class GlowingBallParticleData implements IParticleData {
    //region Fields
    public static final IDeserializer<GlowingBallParticleData> DESERIALIZER =
            new IDeserializer<GlowingBallParticleData>() {
                //region Overrides
                @Override
                public GlowingBallParticleData deserialize(ParticleType<GlowingBallParticleData> type,
                                                           StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    float red = reader.readFloat();
                    reader.expect(' ');
                    float green = reader.readFloat();
                    reader.expect(' ');
                    float blue = reader.readFloat();
                    reader.expect(' ');
                    float alpha = reader.readFloat();
                    reader.expect(' ');
                    float scale = reader.readFloat();
                    reader.expect(' ');
                    float lifetime = reader.readFloat();
                    GlowingBallParticleData data = new GlowingBallParticleData();
                    data.red = red;
                    data.green = green;
                    data.blue = blue;
                    data.alpha = alpha;
                    data.scale = scale;
                    data.lifetime = lifetime;
                    return data;
                }

                @Override
                public GlowingBallParticleData read(ParticleType<GlowingBallParticleData> type,
                                                    PacketBuffer buf) {
                    float red = buf.readFloat();
                    float green = buf.readFloat();
                    float blue = buf.readFloat();
                    float alpha = buf.readFloat();
                    float scale = buf.readFloat();
                    float lifetime = buf.readFloat();
                    GlowingBallParticleData data = new GlowingBallParticleData();
                    data.red = red;
                    data.green = green;
                    data.blue = blue;
                    data.alpha = alpha;
                    data.scale = scale;
                    data.lifetime = lifetime;
                    return data;
                }
                //endregion Overrides
            };

    public float red;
    public float green;
    public float blue;
    public float alpha;
    public float scale;
    public float lifetime;
    //endregion Fields


    //region Initialization
    public GlowingBallParticleData() {
        super();
    }

    public GlowingBallParticleData(float red, float green, float blue, float alpha, float scale, float lifetime) {
        super();
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.scale = scale;
        this.lifetime = lifetime;
    }
    //endregion Initialization

    //region Overrides
    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.GLOWING_BALL.get();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
        buffer.writeFloat(this.alpha);
        buffer.writeFloat(this.scale);
        buffer.writeFloat(this.lifetime);
    }

    @Override
    public String getParameters() {
        return this.getClass().getSimpleName() + ":internal";
    }
    //endregion Overrides

    //region Static Methods
    public static Codec<GlowingBallParticleData> codecFor(ParticleType<?> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
                Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
                Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue),
                Codec.FLOAT.fieldOf("alpha").forGetter(d -> d.alpha),
                Codec.FLOAT.fieldOf("scale").forGetter(d -> d.scale),
                Codec.FLOAT.fieldOf("lifetime").forGetter(d -> d.lifetime)
        ).apply(instance, (red, green, blue, alpha, scale, lifetime) -> {
            GlowingBallParticleData data = new GlowingBallParticleData();
            data.red = red;
            data.green = green;
            data.blue = blue;
            data.alpha = alpha;
            data.scale = scale;
            data.lifetime = lifetime;
            return data;
        }));
    }
    //endregion Static Methods
}
