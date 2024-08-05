// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.behaviour.filter.Filter;
import com.klikli_dev.theurgy.content.behaviour.logistics.ExtractorNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import com.klikli_dev.theurgy.integration.occultism.OccultismIntegration;
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
    public static final int MAX_EXTRACTION_AMOUNT = 64; //how many items to extract per extraction tick
    private final int slowTickRandomOffset = (int) (Math.random() * 20);
    private int extractionAmount = MAX_EXTRACTION_AMOUNT;
    private Direction directionOverride = null;
    private boolean enabled = true;

    public LogisticsItemExtractorBehaviour(BlockEntity blockEntity) {
        super(blockEntity, Capabilities.ItemHandler.BLOCK);
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
        if (!this.enabled)
            return;

        super.tickServer();

        if ((this.slowTickRandomOffset + this.blockEntity.getLevel().getGameTime()) % EXTRACTION_EVERY_N_TICKS != 0) //slow tick
            return;

        this.distributor.tick(); //moved from super.tickServer() here because otherwise the distributor keeps moving targets despite not moving items

        var insertTarget = this.distributor.target();
        if (insertTarget == null)
            return;

        var extractTarget = this.extractTargets.getFirst(); //we only support one target
        if (extractTarget == null)
            return;

        var insertCap = insertTarget.capability().getCapability();
        if (insertCap == null)
            return;

        var extractCap = extractTarget.getCapability();
        if (extractCap == null)
            return;

        if (OccultismIntegration.get().tryPerformStorageActuatorExtraction(this.level(), extractCap, this.filter(), insertCap, insertTarget.inserter().filter(), this.extractionAmount))
            return;

        this.performExtraction(extractCap, this.filter(), insertCap, insertTarget.inserter().filter());
    }

    protected void performExtraction(IItemHandler extractCap, Filter extractFilter, IItemHandler insertCap, Filter insertFilter) {
        var level = this.level();

        //iterate over all slots in the extract inventory, but only extract from the first matching slot.
        for (int extractSlot = 0; extractSlot < extractCap.getSlots(); extractSlot++) {

            //the extract slot must match the filters both on the extractor and inserter.
            var stack = extractCap.getStackInSlot(extractSlot);
            if (!stack.isEmpty() && extractFilter.test(level, stack) && insertFilter.test(level, stack)) {

                //first simulate extraction, this tells us how much we can extract
                var extractStack = extractCap.extractItem(extractSlot, this.extractionAmount, true);
                if (extractStack.isEmpty()) //that should never be true, as we already checked emptiness above.
                    continue;

                //and insertion
                ItemStack inserted = ItemHandlerHelper.insertItemStacked(insertCap, extractStack, true);
                //TODO(optimization): does it make sense to cache "failed to insert" stacks?
                //      1) use our own insert code instead of ItemHandlerHelper.insertItemStacked that the first sequence of full slots?
                //      2) store itemstack + component (but not count) that failed to insert at all (not even 1 inserted in entire target container)

                //then if anything was inserted during the simulation, perform the real extraction and insertion
                if (inserted.getCount() != extractStack.getCount()) {
                    ItemStack remaining = ItemHandlerHelper.insertItemStacked(insertCap, extractStack, false);
                    extractCap.extractItem(extractSlot, extractStack.getCount() - remaining.getCount(), false);
                    break; //we transfer maximum one stack per iteration
                }
            }
        }
    }
}
