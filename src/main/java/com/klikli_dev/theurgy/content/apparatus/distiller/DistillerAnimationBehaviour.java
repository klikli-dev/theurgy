// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.behaviour.animation.AnimationBehaviour;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class DistillerAnimationBehaviour extends AnimationBehaviour<DistillerBlockEntity> {

    private static final RawAnimation START_AND_ON_ANIM = RawAnimation.begin()
            .thenPlay("animation.distiller.start")
            .thenLoop("animation.distiller.on");
    private static final RawAnimation STOP_AND_OFF_ANIM = RawAnimation.begin()
            .thenPlay("animation.distiller.stop")
            .thenLoop("animation.distiller.off");
    private static final RawAnimation OFF_ANIM = RawAnimation.begin()
            .thenLoop("animation.distiller.off");
    private static final RawAnimation ON_ANIM = RawAnimation.begin()
            .thenLoop("animation.distiller.on");

    public DistillerAnimationBehaviour(DistillerBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public PlayState animationHandler(AnimationTest<DistillerBlockEntity> event) {
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
