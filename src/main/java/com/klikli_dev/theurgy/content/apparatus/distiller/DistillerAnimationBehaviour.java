// SPDX-FileCopyrightText: 2023 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.distiller;

import com.klikli_dev.theurgy.content.behaviour.animation.AnimationBehaviour;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
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
    public <E extends GeoBlockEntity> PlayState animationHandler(AnimationState<E> event) {

        var isProcessing = this.blockEntity.craftingBehaviour.isProcessing();

        if (this.wasProcessingLastTick && !isProcessing && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(STOP_AND_OFF_ANIM);
        }

        if (!this.wasProcessingLastTick && isProcessing && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(START_AND_ON_ANIM);
        }

        if (!this.wasProcessingLastTick && !isProcessing && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(OFF_ANIM);
        }

        if (this.wasProcessingLastTick && isProcessing && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(ON_ANIM);
        }

        this.wasProcessingLastTick = isProcessing;

        return PlayState.CONTINUE;
    }
}
