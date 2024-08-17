package com.klikli_dev.theurgy.content.render.itemhud;

import com.klikli_dev.theurgy.content.behaviour.crafting.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.crafting.HasCraftingBehaviour;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.capabilities.Capabilities;

public class ApparatusInsertItemHUD {

    private static final ApparatusInsertItemHUD instance = new ApparatusInsertItemHUD();

    private boolean lastCanProcess = false;
    private BlockPos lastHitBlockPos = null;
    private ItemStack lastStack = ItemStack.EMPTY;

    public static ApparatusInsertItemHUD get() {
        return instance;
    }

    public void tick(Player player) {
        var hitResult = Minecraft.getInstance().hitResult;
        var level = Minecraft.getInstance().level;
        var stack = player.getMainHandItem();

        if (stack.isEmpty()) {
            this.reset();
            return;
        }

        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
            var blockPos = blockHitResult.getBlockPos();

            if (level.getBlockEntity(blockPos) instanceof HasCraftingBehaviour<?, ?, ?> hasCraftingBehaviour) {

                if (!blockPos.equals(this.lastHitBlockPos) || !ItemStack.isSameItemSameComponents(stack, this.lastStack)) {
                    this.lastHitBlockPos = blockPos;
                    this.lastStack = stack;
                    this.lastCanProcess = this.canProcess(hasCraftingBehaviour.craftingBehaviour(), stack);
                }
            }
            //TODO: here handle theurgy blocks without a crafting behaviour
            //      sal ammoniac tank
            //      brazier
            //      incubator vessels
            //      reformation pedestals
            //      this should likely be handled with the storage behaviour!
            else {
                this.reset();
            }
        } else {
            this.reset();
        }

        if (this.lastHitBlockPos != null) {
            if (this.lastCanProcess) {
                Outliner.get().showAABB(this.lastHitBlockPos, Shapes.block().bounds()
                                .move(this.lastHitBlockPos))
                        .colored(Color.GREEN)
                        .lineWidth(1 / 32f);
            } else {
                Outliner.get().showAABB(this.lastHitBlockPos, Shapes.block().bounds()
                                .move(this.lastHitBlockPos))
                        .colored(Color.RED)
                        .lineWidth(1 / 32f);
            }
        }
    }

    private boolean canProcess(CraftingBehaviour<?, ?, ?> craftingBehaviour, ItemStack stack) {
        if (craftingBehaviour.canProcess(stack))
            return true;

        var fluidHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidHandler == null)
            return false;

        var fluidStack = fluidHandler.getFluidInTank(0);
        if (fluidStack.isEmpty())
            return false;

        return craftingBehaviour.canProcess(fluidStack);
    }

    private void reset() {
        if (this.lastHitBlockPos != null)
            Outliner.get().remove(this.lastHitBlockPos);

        this.lastCanProcess = false;
        this.lastHitBlockPos = null;
        this.lastStack = ItemStack.EMPTY;
    }
}
