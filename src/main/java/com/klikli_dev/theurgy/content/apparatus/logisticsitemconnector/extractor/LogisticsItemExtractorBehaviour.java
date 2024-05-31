// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.behaviour.logistics.ExtractorNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemExtractorBehaviour extends ExtractorNodeBehaviour<IItemHandler, @Nullable Direction> {

    public static final int EXTRACTION_EVERY_N_TICKS = 20; // 1 second
    public static final int MAX_EXTRACTION_AMOUNT = 10; //how many items to extract per extraction tick
    private final int slowTickRandomOffset = (int) (Math.random() * 20);
    private int extractionAmount = MAX_EXTRACTION_AMOUNT;
    private Direction directionOverride = null;
    private boolean enabled = true;

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
    public @Nullable Direction getTargetContext(BlockPos targetPos) {
        return this.directionOverride != null ? this.directionOverride :
                this.blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
    }

    public boolean enabled() {
        return this.enabled;
    }

    public void enabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void directionOverride(Direction directionOverride) {
        this.directionOverride = directionOverride;
        this.rebuildExtractTargets();
    }

    public Direction directionOverride() {
        return this.directionOverride;
    }

    @Override
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);

        pTag.putInt("extractionAmount", this.extractionAmount);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        if (pTag.contains("extractionAmount")) {
            this.extractionAmount = pTag.getInt("extractionAmount");
        }

    }

    @Override
    public void writeNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.writeNetwork(pTag, pRegistries);

        pTag.putBoolean("enabled", this.enabled);
        if (this.directionOverride != null)
            pTag.putInt("directionOverride", this.directionOverride.get3DDataValue());
    }

    @Override
    public void readNetwork(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.readNetwork(pTag, pRegistries);

        if (pTag.contains("directionOverride")) {
            this.directionOverride = Direction.from3DDataValue(pTag.getInt("directionOverride"));
        }
        if (pTag.contains("enabled")) {
            this.enabled = pTag.getBoolean("enabled");
        }
    }

    @Override
    public void tickServer() {
        //TODO: extraction should happen on a low tick, and in bulk.

        if (!this.enabled)
            return;

        super.tickServer();

        if ((this.slowTickRandomOffset + this.blockEntity.getLevel().getGameTime()) % EXTRACTION_EVERY_N_TICKS != 0) //slow tick
            return;

        this.distributor.tick(); //moved from super.tickServer() here because otherwise the distributor keeps moving targets despite not moving items


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
        int extractSlot = getFirstMatchingSlot(extractCap);
        if (extractSlot == -1)
            return; //nothing found to extract


        //first simulate extraction, this tells us how much we can extract
        var extractStack = extractCap.extractItem(extractSlot, this.extractionAmount, true);
        if (extractStack.isEmpty())
            return;


        //TODO: better round robin distribution that works with extraction amounts > 1?
        //  Simulate extract to get available
        //  Simulate insert on each to get available space
        //  Split evenly and take care of remainder
        //  Then insert/extract
        //  BUUUT: we should move that logic into the distributor
        //  and: figure out if we do round robin only within a single tick, or if we do it across ticks
        //      we probably need both depending on the transferred amount

        //and insertion
        ItemStack inserted = ItemHandlerHelper.insertItemStacked(insertCap, extractStack, true);

        //then if anything was inserted during the simulation, perform the real extraction and insertion
        if (inserted.getCount() != extractStack.getCount()) {
            ItemStack remaining = ItemHandlerHelper.insertItemStacked(insertCap, extractStack, false);
            extractCap.extractItem(extractSlot, extractStack.getCount() - remaining.getCount(), false);
        }
    }
}
