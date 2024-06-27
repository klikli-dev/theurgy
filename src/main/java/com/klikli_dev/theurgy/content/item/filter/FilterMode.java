package com.klikli_dev.theurgy.content.item.filter;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum FilterMode implements StringRepresentable {
    /**
     * Items that have at least one of the attributes in the list are accepted.
     */
    ACCEPT_LIST_OR("accept_list_or"),
    /**
     * Items that have all of the attributes in the list are accepted.
     */
    ACCEPT_LIST_AND("accept_list_and"),
    /**
     * Items that have any of the attributes in the list are denied.
     */
    DENY_LIST("deny_list");

    public static final Codec<FilterMode> CODEC = StringRepresentable.fromEnum(FilterMode::values);

    public static final IntFunction<FilterMode> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, FilterMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, FilterMode::ordinal);

    private final String name;

    FilterMode(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
