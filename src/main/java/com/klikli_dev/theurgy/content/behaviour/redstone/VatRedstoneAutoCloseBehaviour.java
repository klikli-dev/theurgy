package com.klikli_dev.theurgy.content.behaviour.redstone;

import com.klikli_dev.theurgy.content.behaviour.BlockEntityBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.util.RedstoneUtil;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * A behaviour that automatically closes the vat if a redstone signal is present and a valid recipe is present.
 * It does not need a redstone change to be triggered.
 */
public class VatRedstoneAutoCloseBehaviour<R extends Recipe<?>> extends BlockEntityBehaviour{
    //Ensure slow ticks are not all on the same tick for all block entities
    protected int randomSlowTickOffset = (int) (Math.random() * 20);

    protected HasCraftingBehaviour<?, R, ?> hasCraftingBehaviour;

    public VatRedstoneAutoCloseBehaviour(BlockEntity blockEntity) {
        super(blockEntity);

        if(!(blockEntity instanceof HasCraftingBehaviour)){
            throw new IllegalArgumentException("Block entity must implement HasCraftingBehaviour");
        }

        //noinspection unchecked
        this.hasCraftingBehaviour = (HasCraftingBehaviour<?, R, ?>) blockEntity;
    }

    public void tickServer() {
        if((this.level().getGameTime() + this.randomSlowTickOffset) % 20 == 0) {
            this.tryAutoClose();
        }
    }

    /**
     * If a redstone signal is present, the vat is open, and we have a valid recipe, then close.
     */
    protected void tryAutoClose(){
        boolean hasSignal = RedstoneUtil.hasNeighborSignal(this.level(), this.getBlockPos(), this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite());
        boolean wasOpen = this.getBlockState().getValue(BlockStateProperties.OPEN);
        if (hasSignal && wasOpen) {
            var recipe = this.hasCraftingBehaviour.craftingBehaviour().getRecipe();
            if (recipe.isPresent() && this.hasCraftingBehaviour.craftingBehaviour().canCraft(recipe.get())) {
                this.level().setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.OPEN, false), Block.UPDATE_CLIENTS);
            }
        }
    }
}
