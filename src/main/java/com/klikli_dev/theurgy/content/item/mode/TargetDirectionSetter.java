package com.klikli_dev.theurgy.content.item.mode;

import net.minecraft.core.Direction;

public interface TargetDirectionSetter {
    void targetDirection(Direction direction);

    Direction targetDirection();
}
