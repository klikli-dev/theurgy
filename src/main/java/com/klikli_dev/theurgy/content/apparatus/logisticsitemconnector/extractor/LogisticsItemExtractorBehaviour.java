// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.apparatus.logisticsitemconnector.extractor;

import com.klikli_dev.theurgy.content.storage.ItemStorageHelper;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.content.behaviour.filter.Filter;
import com.klikli_dev.theurgy.content.behaviour.logistics.ExtractorNodeBehaviour;
import com.klikli_dev.theurgy.content.behaviour.logistics.LeafNodeBehaviour;
import com.klikli_dev.theurgy.integration.occultism.OccultismIntegration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.Nullable;

public class LogisticsItemExtractorBehaviour extends ExtractorNodeBehaviour<ResourceHandler<ItemResource>, @Nullable Direction> {

    public static final int EXTRACTION_EVERY_N_TICKS = 20; // 1 second
    public static final int MAX_EXTRACTION_AMOUNT = 64; //how many items to extract per extraction tick
    private final int slowTickRandomOffset = (int) (Math.random() * 20);
    private int extractionAmount = MAX_EXTRACTION_AMOUNT;
    private Direction directionOverride = null;
    private boolean enabled = true;

    public LogisticsItemExtractorBehaviour(BlockEntity blockEntity) {
        super(blockEntity, CapabilityRegistry.ITEM_HANDLER);
    }

    @Override
    protected boolean isValidInsertTarget(LeafNodeBehaviour<ResourceHandler<ItemResource>, @Nullable Direction> leafNode, BlockCapabilityCache<ResourceHandler<ItemResource>, @Nullable Direction> capability) {
        //any target we get is guaranteed to exist and have an item capability, so we just return true here.
        return true;
    }

    @Override
    public @Nullable Direction getTargetContext(BlockPos targetPos) {
        return this.directionOverride != null ? this.directionOverride :
                this.blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public boolean enabled() {
        return this.enabled;
    }

    @Override
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
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("extractionAmount", this.extractionAmount);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.extractionAmount = input.getIntOr("extractionAmount", this.extractionAmount);
    }

    @Override
    public void writeNetwork(ValueOutput output) {
        super.writeNetwork(output);
        output.putBoolean("enabled", this.enabled);
        if (this.directionOverride != null)
            output.putInt("directionOverride", this.directionOverride.get3DDataValue());
    }

    @Override
    public void readNetwork(ValueInput input) {
        super.readNetwork(input);
        input.getInt("directionOverride").ifPresent(direction -> this.directionOverride = Direction.from3DDataValue(direction));
        this.enabled = input.getBooleanOr("enabled", this.enabled);
    }

    @Override
    public void tickServer() {
        if (!this.enabled || this.extractTargets.isEmpty())
            return;

        super.tickServer();

        if ((this.slowTickRandomOffset + this.blockEntity.getLevel().getGameTime()) % EXTRACTION_EVERY_N_TICKS != 0) //slow tick
            return;

        this.distributor.tick(); //moved from super.tickServer() here because otherwise the distributor keeps moving targets despite not moving items

        var insertTarget = this.distributor.target();
        if (insertTarget == null || !insertTarget.inserter().enabled()) //TODO: this should be improved, disabled targets should be skipped by the distributor
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

    protected void performExtraction(ResourceHandler<ItemResource> extractCap, Filter extractFilter, ResourceHandler<ItemResource> insertCap, Filter insertFilter) {
        var level = this.level();

        //iterate over all slots in the extract inventory, but only extract from the first matching slot.
        for (int extractSlot = 0; extractSlot < ItemStorageHelper.getSlots(extractCap); extractSlot++) {

            //the extract slot must match the filters both on the extractor and inserter.
            var stack = ItemStorageHelper.getStackInSlot(extractCap, extractSlot);
            if (!stack.isEmpty() && extractFilter.test(level, stack) && insertFilter.test(level, stack)) {

                //first simulate extraction, this tells us how much we can extract
                var extractStack = ItemStorageHelper.extractItem(extractCap, extractSlot, this.extractionAmount, true);
                if (extractStack.isEmpty()) //that should never be true, as we already checked emptiness above.
                    continue;

                //and insertion
                ItemStack inserted = ItemStorageHelper.insertItemStacked(insertCap, extractStack, true);
                //TODO(optimization): does it make sense to cache "failed to insert" stacks?
                //      1) use our own insert code instead of ItemHandlerHelper.insertItemStacked that the first sequence of full slots?
                //      2) store itemstack + component (but not count) that failed to insert at all (not even 1 inserted in entire target container)

                //then if anything was inserted during the simulation, perform the real extraction and insertion
                if (inserted.getCount() != extractStack.getCount()) {
                    ItemStack remaining = ItemStorageHelper.insertItemStacked(insertCap, extractStack, false);
                    ItemStorageHelper.extractItem(extractCap, extractSlot, extractStack.getCount() - remaining.getCount(), false);
                    break; //we transfer maximum one stack per iteration
                }
            }
        }
    }
}
