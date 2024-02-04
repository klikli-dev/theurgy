// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.behaviour.logistics.ExtractorNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemExtractorBehaviour extends ExtractorNodeBehaviour<IItemHandler, @Nullable Direction> {
    public LogisticsItemExtractorBehaviour(BlockEntity blockEntity) {
        super(blockEntity, Capabilities.ItemHandler.BLOCK);
    }

    protected static int getFirstMatchingSlot(IItemHandler handler) {
        return getFirstMatchingSlotAfter(handler, -1);
    }

    protected static int getFirstMatchingSlotAfter(IItemHandler handler, int slot) {
        for (int i = slot + 1; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isValidInsertTarget(LeafNodeBehaviour<IItemHandler, @Nullable Direction> leafNode, BlockCapabilityCache<IItemHandler, @Nullable Direction> capability) {
        //any target we get is guaranteed to exist and have an item capability, so we just return true here.
        return true;
    }

    @Override
    protected @Nullable Direction getTargetContext(BlockPos targetPos) {
        //TODO: handle context / direction override!
        return this.blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public void tickServer() {
        //TODO: extract and insert nodes have to be disable-able

        //TODO: extraction should happen on a low tick, and in bulk.

        super.tickServer(); //this moves our distributor to the next target if needed.

        var insertTarget = this.distributor.target();
        if (insertTarget == null)
            return;

        var extractTarget = this.extractTargets.get(0); //we only support one target
        if (extractTarget == null)
            return;

        var insertCap = insertTarget.getCapability();
        if (insertCap == null)
            return;

        var extractCap = extractTarget.getCapability();
        if (extractCap == null)
            return;

        //TODO: item filter!
        this.performExtraction(extractCap, insertCap);
    }

    protected void performExtraction(IItemHandler extractCap, IItemHandler insertCap) {

        //TODO: verify if this is the best way to transfer things with itemhandlers
        int extractSlot = getFirstMatchingSlot(extractCap);
        if (extractSlot == -1)
            return; //nothing found to extract

        //first simulate extraction
        var extractStack = extractCap.extractItem(extractSlot, 1, true);
        if (extractStack.isEmpty())
            return;


        //and insertion
        ItemStack inserted = ItemHandlerHelper.insertItemStacked(insertCap, extractStack, true);

        //then if anything was inserted during the simulation, perform the real extraction and insertion
        if (inserted.getCount() != extractStack.getCount()) {
            ItemStack remaining = ItemHandlerHelper.insertItemStacked(insertCap, extractStack, false);
            extractCap.extractItem(extractSlot, extractStack.getCount() - remaining.getCount(), false);
        }
    }
}
