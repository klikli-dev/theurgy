package com.klikli_dev.theurgy.content.behaviour.interaction;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectionBehaviours are client-side behaviours that allow to select and highlight blocks (e.g. to indicate that they later will be linked to the item/blockitem in hand)
 *
 * @param <T>
 */
public abstract class SelectionBehaviour<T extends SelectedPoint> {

    protected List<T> selectedPoints = new ArrayList<>();
    protected ItemStack currentItem;

    public boolean onRightClickBlock(Level level, Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        if (this.currentItem == null || player == null)
            return false;

        if (!level.isClientSide)
            return false;

        if (player.isSpectator())
            return false;

        var selected = this.get(pos);
        BlockState state = level.getBlockState(pos);

        if (selected == null) {
            //If we do not have the target block selected yet, select it
            var point = this.create(level, pos, state);
            if (point == null)
                return false;

            selected = point;
            this.add(point);
        } else {
            //if it is already selected, cycle the mode and if needed remove
            if(!selected.cycleMode()){
                this.remove(pos);

                //skip message, but return true to have calling code cancel the click event.
                return true;
            }
        }

        //show the current mode + target block to player
        player.displayClientMessage(this.getModeMessage(selected, state), true);
        return true;
    }

    public boolean onLeftClickBlock(Level level, Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        if (this.currentItem == null)
            return false;
        if (!level.isClientSide)
            return false;

        //if we had a selected block we return true to have calling code cancel the event
        return this.remove(pos) != null;
    }

    public void onPlace(BlockPos pos, Player player) {

        int removed = 0;
        for (var iterator = this.selectedPoints.iterator(); iterator.hasNext(); ) {
            var point = iterator.next();
            if (point.getBlockPos().closerThan(pos, this.getBlockRange()))
                continue;
            iterator.remove();
            removed++;
        }

        if (removed > 0) {
            player.displayClientMessage(this.getOutsideRangeMessage(removed), true);
        } else {
            this.displaySummary(pos, player);
        }

        this.sendPlacementPacket(pos);

        this.selectedPoints.clear();
        this.currentItem = null;
    }

    public void tick(Player player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!this.isSelectionItem(heldItem)) {
            this.currentItem = null;
        } else {
            if (heldItem != this.currentItem) {
                this.selectedPoints.clear();
                this.currentItem = heldItem;
            }

            this.prepareRender();
        }
    }

    protected void prepareRender() {
        for (var iterator = this.selectedPoints.iterator(); iterator.hasNext(); ) {
            var point = iterator.next();

            if (!this.isValid(point)) {
                iterator.remove();
                continue;
            }

            Level level = point.getLevel();
            BlockPos pos = point.getBlockPos();
            BlockState state = level.getBlockState(pos);
            VoxelShape shape = state.getShape(level, pos);
            if (shape.isEmpty())
                continue;

            int color = point.getColor().getRGB();
            Outliner.get().showAABB(point, shape.bounds()
                            .move(pos))
                    .colored(color)
                    .lineWidth(1 / 16f);
        }
    }

    protected void add(T point) {
        this.selectedPoints.add(point);
    }

    protected T remove(BlockPos pos) {
        var result = this.get(pos);
        if (result != null)
            this.selectedPoints.remove(result);
        return result;
    }

    protected T get(BlockPos pos) {
        for (T point : this.selectedPoints)
            if (point.getBlockPos().equals(pos))
                return point;
        return null;
    }

    public boolean isValid(T point){
        point.refreshBlockStateCache();
        return this.canCreate(point.getLevel(), point.getBlockPos(), point.getBlockState());
    }

    protected Component getModeMessage(T point, BlockState state) {
        return Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_MODE, point.getModeMessage(), state.getBlock().getName().withStyle(ChatFormatting.WHITE)).withStyle(s -> s.withColor(point.getColor().getRGB()));
    }

    protected Component getOutsideRangeMessage(int removed) {
        return Component.translatable(TheurgyConstants.I18n.Behaviour.SELECTION_OUTSIDE_RANGE, removed).withStyle(ChatFormatting.RED
        );
    }

    /**
     * Called by onPlace, at that point all out of range selection points are already cleaned up.
     */
    protected abstract void displaySummary(BlockPos pos, Player player);

    protected abstract void sendPlacementPacket(BlockPos pos);

    public abstract int getBlockRange();

    public abstract boolean canCreate(Level level, BlockPos pos, BlockState state);
    protected abstract T create(Level level, BlockPos pos, BlockState state);

    /**
     * Should return true for the ItemStack that is used to select clicked blocks.
     * That usually means it is the block item of the block that should be linked to the clicked block.
     */
    protected abstract boolean isSelectionItem(ItemStack stack);


}
