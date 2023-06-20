/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.calcinationoven;

import com.klikli_dev.theurgy.content.block.behaviour.AnimationBehaviour;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CalcinationOvenAnimationBehaviour extends AnimationBehaviour<CalcinationOvenBlockEntity> {

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

    private static final RawAnimation PLACE_AND_OFF_ANIM = RawAnimation.begin()
            .thenPlay("animation.calcination_oven.place")
            .thenLoop("animation.calcination_oven.off");
    
    private boolean wasProcessingLastTick;

    public CalcinationOvenAnimationBehaviour(CalcinationOvenBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public <E extends GeoBlockEntity> PlayState animationHandler(AnimationState<E> event) {

        var isProcessing = this.blockEntity.craftingBehaviour.isProcessing();

        if (!this.wasProcessingLastTick && !isProcessing && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().setAnimation(PLACE_AND_OFF_ANIM);
        } else if (this.wasProcessingLastTick && !isProcessing && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(STOP_AND_OFF_ANIM);
        } else if (!this.wasProcessingLastTick && isProcessing && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING) {
            event.getController().setAnimation(START_AND_ON_ANIM);
        } else if (!this.wasProcessingLastTick && !isProcessing && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(OFF_ANIM);
        } else if (this.wasProcessingLastTick && isProcessing && event.getController().getAnimationState() != AnimationController.State.RUNNING) {
            event.getController().setAnimation(ON_ANIM);
        }

        this.wasProcessingLastTick = isProcessing;

        return PlayState.CONTINUE;
    }
}
