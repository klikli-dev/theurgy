// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.content.item.mode.ItemMode;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public abstract class MercurialWandItemMode extends ItemMode {

    public static final Codec<MercurialWandItemMode> CODEC = Type.CODEC.xmap(Type::mode, MercurialWandItemMode::type);

    public static final StreamCodec<ByteBuf, MercurialWandItemMode> STREAM_CODEC = Type.STREAM_CODEC.map(Type::mode, MercurialWandItemMode::type);

    protected Type type;

    protected MercurialWandItemMode() {
        super();
    }

    public Type type() {
        return this.type;
    }

    @Override
    protected String typeName() {
        return this.type.name().toLowerCase();
    }


    public enum Type implements StringRepresentable {
        CYCLE_DIRECTION("cycle_direction",new CycleSelectedDirectionMode()),
        SWITCH_LOGISTICS_ENABLED("switch_logistics_enabled", new SwitchLogisticsEnabledMode());

        public static final Codec<Type> CODEC = StringRepresentable.fromValues(Type::values);
        public static final IntFunction<Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::ordinal);
        final String name;
        final MercurialWandItemMode mode;
        final boolean enabled;

        Type(String name, MercurialWandItemMode mode) {
            this(name, mode, true);
        }

        Type(String name, MercurialWandItemMode mode, boolean enabled) {
            this.name = name;
            this.mode = mode;
            this.enabled = enabled;
            mode.type = this;
        }

        public MercurialWandItemMode mode() {
            return this.mode;
        }

        public Type next() {
            int next = this.ordinal() + 1;
            if (next >= Type.values().length) {
                next = 0;
            }
            var nextValue = Type.values()[next];
            if (!nextValue.enabled) {
                return nextValue.next();
            }
            return Type.values()[next];
        }

        public Type previous() {
            int previous = this.ordinal() - 1;
            if (previous < 0) {
                previous = Type.values().length - 1;
            }

            var previousValue = Type.values()[previous];
            if (!previousValue.enabled) {
                return previousValue.previous();
            }

            return Type.values()[previous];
        }

        public Type shift(int shift) {
            if (shift > 0) {
                return this.next();
            } else if (shift < 0) {
                return this.previous();
            }
            return this;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
