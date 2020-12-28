package com.github.klikli_dev.theurgy.client.particle;

import com.github.klikli_dev.theurgy.registry.ParticleRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class CrucibleBubbleParticleData implements IParticleData {
    //region Fields
    public static final IDeserializer<CrucibleBubbleParticleData> DESERIALIZER =
            new IDeserializer<CrucibleBubbleParticleData>() {
                //region Overrides
                @Override
                public CrucibleBubbleParticleData deserialize(ParticleType<CrucibleBubbleParticleData> type,
                                                              StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    float red = reader.readFloat();
                    reader.expect(' ');
                    float green = reader.readFloat();
                    reader.expect(' ');
                    float blue = reader.readFloat();
                    CrucibleBubbleParticleData data = new CrucibleBubbleParticleData();
                    data.red = red;
                    data.green = green;
                    data.blue = blue;
                    return data;
                }

                @Override
                public CrucibleBubbleParticleData read(ParticleType<CrucibleBubbleParticleData> type,
                                                       PacketBuffer buf) {
                    float red = buf.readFloat();
                    float green = buf.readFloat();
                    float blue = buf.readFloat();
                    CrucibleBubbleParticleData data = new CrucibleBubbleParticleData();
                    data.red = red;
                    data.green = green;
                    data.blue = blue;
                    return data;
                }
                //endregion Overrides
            };

    public float red;
    public float green;
    public float blue;
    //endregion Fields


    public CrucibleBubbleParticleData() {
        super();
    }

    public CrucibleBubbleParticleData(float red, float green, float blue) {
        super();
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    //region Overrides
    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.CRUCIBLE_BUBBLES.get();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(this.red);
        buffer.writeFloat(this.green);
        buffer.writeFloat(this.blue);
    }

    @Override
    public String getParameters() {
        return this.getClass().getSimpleName() + ":internal";
    }
    //endregion Overrides

    //region Static Methods
    public static Codec<CrucibleBubbleParticleData> codecFor(ParticleType<?> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("red").forGetter(d -> d.red),
                Codec.FLOAT.fieldOf("green").forGetter(d -> d.green),
                Codec.FLOAT.fieldOf("blue").forGetter(d -> d.blue)
        ).apply(instance, (red, green, blue) -> {
            CrucibleBubbleParticleData data = new CrucibleBubbleParticleData();
            data.red = red;
            data.green = green;
            data.blue = blue;
            return data;
        }));
    }
    //endregion Static Methods
}
