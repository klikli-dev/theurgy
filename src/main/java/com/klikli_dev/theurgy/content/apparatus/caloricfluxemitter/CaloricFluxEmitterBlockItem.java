package com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter;

import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class CaloricFluxEmitterBlockItem extends BlockItem {
    public CaloricFluxEmitterBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (BlockRegistry.CALORIC_FLUX_EMITTER.get().getSelectionBehaviour().canCreate(level, pos, level.getBlockState(pos)))
            return InteractionResult.SUCCESS;
        return super.useOn(ctx);
    }
}
