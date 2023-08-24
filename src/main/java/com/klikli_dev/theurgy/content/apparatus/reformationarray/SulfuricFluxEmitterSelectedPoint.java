package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.interaction.SelectedPoint;
import com.klikli_dev.theurgy.content.render.Color;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SulfuricFluxEmitterSelectedPoint extends SelectedPoint<SulfuricFluxEmitterSelectedPoint> {

    public static final Codec<SulfuricFluxEmitterSelectedPoint> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            BlockPos.CODEC.fieldOf("blockPos").forGetter(SulfuricFluxEmitterSelectedPoint::getBlockPos),
                            StringRepresentable.fromEnum(Type::values).fieldOf("type").forGetter(SulfuricFluxEmitterSelectedPoint::getType)
                    )
                    .apply(instance, SulfuricFluxEmitterSelectedPoint::new));

    public static final Codec<List<SulfuricFluxEmitterSelectedPoint>> LIST_CODEC = Codec.list(CODEC);

    protected Type type;

    public SulfuricFluxEmitterSelectedPoint(BlockPos blockPos, Type type) {
        super(blockPos);
        this.type = type;
    }

    protected SulfuricFluxEmitterSelectedPoint(Level level, BlockPos blockPos, BlockState blockState, Type type) {
        super(level, blockPos, blockState);
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public Color getColor() {
        return this.type.getColor();
    }

    @Override
    public Component getModeMessage() {
        return Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_MODE_CALORIC_FLUX_EMITTER);
    }

    @Override
    public boolean cycleMode() {
        //has no mode
        //but double-selecting removes the point
        return false;
    }

    @Override
    public Codec<SulfuricFluxEmitterSelectedPoint> codec() {
        return CODEC;
    }

    public enum Type implements StringRepresentable {
        SOURCE("SOURCE", new Color(0x32a832, false)),
        TARGET("TARGET", new Color(0xa832a4, false)),
        RESULT("RESULT", new Color(0x4c32a8, false));

        private final String name;
        private final Color color;

        Type(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return this.name;
        }

        public Color getColor() {
            return this.color;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
