// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector;

import com.klikli_dev.theurgy.content.behaviour.filter.FilterBehaviour;
import com.klikli_dev.theurgy.content.behaviour.filter.HasFilterBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.HasLeafNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import com.klikli_dev.theurgy.content.item.mode.EnabledSetter;
import com.klikli_dev.theurgy.content.item.mode.FrequencySetter;
import com.klikli_dev.theurgy.content.item.mode.TargetDirectionSetter;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class LogisticsItemConnectorBlockEntity extends BlockEntity implements MenuProvider, HasLeafNodeBehaviour<ResourceHandler<ItemResource>, @Nullable Direction>, HasFilterBehaviour, TargetDirectionSetter, EnabledSetter, FrequencySetter {

    protected LeafNodeBehaviour<ResourceHandler<ItemResource>, @Nullable Direction> leafNodeBehaviour;
    protected FilterBehaviour filterBehaviour;

    protected LogisticsItemConnectorBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        //the leaf node behaviour is set in child
        this.filterBehaviour = new FilterBehaviour(this).withCallback(
                (filter) -> {
                    this.updateBlockStateToMatchFilter();
                    this.leafNode().filter(filter);
                }
        );
    }

    @Override
    public FilterBehaviour filter() {
        return this.filterBehaviour;
    }

    @Override
    public List<Pair<BlockPos, Integer>> getStatusHighlights() {
        if (this.level.isClientSide())
            return List.of();

        List<Pair<BlockPos, Integer>> result = new ArrayList<>();

        var targets = this.leafNode().targets();
        for (var target : targets) {
            result.add(Pair.of(target, 0x00FFFF));
        }

        //also show network?
//        var connected = Logistics.get().getNetwork(GlobalPos.of(this.level.dimension(), this.getBlockPos()));
//        if (connected != null) {
//            var shape = Shapes.block();
//            for (var block : connected.nodes()) {
//                if (block.dimension().equals(this.level.dimension())) {
//                    Outliner.get().showAABB(block, shape.bounds()
//                                    .move(block.pos()), 20 * 5)
//                            .colored(0x00FF00)
//                            .lineWidth(1 / 16f);
//                }
//            }
//        }

        return result;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (!this.level.isClientSide()) {
            this.leafNode().onLoad();

            this.updateBlockStateToMatchFilter();
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();

        if (!this.level.isClientSide()) {
            this.leafNode().onChunkUnload();
        }
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.leafNode().loadAdditional(input);
        this.filter().loadAdditional(input);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        this.leafNode().saveAdditional(output);
        this.filter().saveAdditional(output);
    }

    @Override
    public LeafNodeBehaviour<ResourceHandler<ItemResource>, @Nullable Direction> leafNode() {
        return this.leafNodeBehaviour;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    protected void updateBlockStateToMatchFilter() {
        var isEmpty = !this.getBlockState().getValue(LogisticsItemConnectorBlock.HAS_FILTER);
        if (this.filter().filter().isEmpty() != isEmpty) {
            var newState = this.getBlockState().setValue(LogisticsItemConnectorBlock.HAS_FILTER, !this.filter().filter().isEmpty());
            this.level.setBlock(this.getBlockPos(), newState, Block.UPDATE_ALL);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pPos, BlockState pState) {
        super.preRemoveSideEffects(pPos, pState);

        if (this.level != null) {
            this.filter().onRemove(pState, this.level, pPos, pState, false);

            var removedWires = Wires.get(this.level).removeWiresFor(pPos);
            Block.popResource(this.level, pPos, new net.minecraft.world.item.ItemStack(ItemRegistry.COPPER_WIRE.get(), removedWires));

            this.leafNode().onDestroyed();
        }
    }

}
