// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.calcinationoven;

import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.state.AnimationTest;
import com.klikli_dev.theurgy.content.behaviour.animation.AnimationBehaviour;


public class CalcinationAnimationBehaviour extends AnimationBehaviour<CalcinationOvenBlockEntity> {

    private static final RawAnimation START_AND_ON_ANIM = RawAnimation.begin()
            .thenPlay("animation.calcination_oven.start")
            .thenLoop("animation.calcination_oven.on");
    private static final RawAnimation STOP_AND_OFF_ANIM = RawAnimation.begin()
            .thenPlay("animation.calcination_oven.stop")
            .thenLoop("animation.calcination_oven.off");
    private static final RawAnimation OFF_ANIM = RawAnimation.begin()
            .thenLoop("animation.calcination_oven.off");
    private static final RawAnimation ON_ANIM = RawAnimation.begin()
            .thenLoop("animation.calcination_oven.on");

    public CalcinationAnimationBehaviour(CalcinationOvenBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public PlayState animationHandler(AnimationTest<CalcinationOvenBlockEntity> event) {
        var isProcessing = this.blockEntity.craftingBehaviour.isProcessing();

        if (this.wasProcessingLastTick && !isProcessing) {
            event.setAnimation(STOP_AND_OFF_ANIM);
        } else if (!this.wasProcessingLastTick && isProcessing) {
            event.setAnimation(START_AND_ON_ANIM);
        } else if (isProcessing) {
            event.setAnimation(ON_ANIM);
        } else {
            event.setAnimation(OFF_ANIM);
        }

        this.wasProcessingLastTick = isProcessing;

        return PlayState.CONTINUE;
    }
}
