package com.klikli_dev.theurgy.util;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Codecs {
    public static final Codec<Vec3> VEC3 = Codec.DOUBLE.listOf().comapFlatMap(
            (fromList) -> {
                return Util.fixedSize(fromList, 3).map((list) -> {
                    return new Vec3(list.get(0), list.get(1), list.get(2));
                });
            }, (fromVec3) -> {
                return List.of(fromVec3.x(), fromVec3.y(), fromVec3.z());
            });
}
