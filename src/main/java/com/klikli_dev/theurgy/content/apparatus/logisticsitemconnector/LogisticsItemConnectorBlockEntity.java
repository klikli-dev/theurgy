package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector;

import com.klikli_dev.theurgy.content.behaviour.logistics.HasLeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class LogisticsItemConnectorBlockEntity extends BlockEntity implements HasLeafNodeBehaviour<IItemHandler, @Nullable Direction>{

    protected LeafNodeBehaviour<IItemHandler, @Nullable Direction> leafNodeBehaviour;

    protected LogisticsItemConnectorBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.leafNodeBehaviour.onLoad();
    }

    @Override
    public LeafNodeBehaviour<IItemHandler, @Nullable Direction> leafNode() {
        return this.leafNodeBehaviour;
    }
}
