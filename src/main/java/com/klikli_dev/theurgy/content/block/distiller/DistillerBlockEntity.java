/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.content.block.distiller;

import com.klikli_dev.theurgy.content.block.HeatConsumer;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DistillerBlockEntity extends BlockEntity implements GeoBlockEntity, HeatConsumer {

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
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    boolean heatedCache;
    /**
     * Client-side we only use the blockstate to determine our animation state.
     */
    boolean wasLitLastTick;

    public DistillerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.DISTILLER.get(), pPos, pBlockState);
    }

    @Override
    public boolean getHeatedCache() {
        return this.heatedCache;
    }

    @Override
    public void setHeatedCache(boolean heated) {
        this.heatedCache = heated;
    }

    public void tickServer() {
        boolean wasHeated = this.isHeated();
    }


    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);


    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

    }

    private <E extends GeoBlockEntity> PlayState animationHandler(AnimationState<E> event) {

        var blockState = this.getBlockState();
        var isLit = blockState.getValue(BlockStateProperties.LIT);

            if(this.wasLitLastTick && !isLit && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING){
                event.getController().setAnimation(STOP_AND_OFF_ANIM);
            }

            if(!this.wasLitLastTick && isLit && event.getController().getAnimationState() != AnimationController.State.TRANSITIONING){
                event.getController().setAnimation(START_AND_ON_ANIM);
            }

            if(!wasLitLastTick && !isLit && event.getController().getAnimationState() != AnimationController.State.RUNNING){
                event.getController().setAnimation(OFF_ANIM);
            }

            if(wasLitLastTick && isLit && event.getController().getAnimationState() != AnimationController.State.RUNNING){
                event.getController().setAnimation(ON_ANIM);
            }


        this.wasLitLastTick = isLit;

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoBlockEntity>(this, "controller", 0, this::animationHandler));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

}
