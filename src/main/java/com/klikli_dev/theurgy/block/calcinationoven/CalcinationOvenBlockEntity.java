package com.klikli_dev.theurgy.block.calcinationoven;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CalcinationOvenBlockEntity extends BlockEntity {

    private static final int CHECK_HEAT_TICK_INTERVAL = 20;
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> recipeCachedCheck;
    public ItemStackHandler inputInventory;
    public ItemStackHandler outputInventory;

    public IItemHandler inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;
    public RecipeWrapper inputRecipeWrapper;
    int calcinationProgress;
    int calcinationTotalTime;

    boolean isHeated;

    public CalcinationOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALCINATION_OVEN.get(), pPos, pBlockState);

        this.inputInventory = new ItemStackHandler(1);
        this.outputInventory = new ItemStackHandler(1);
        this.inventory = new Inventory();

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventory);

        this.inputRecipeWrapper = new RecipeWrapper(this.inputInventory);

        this.recipeCachedCheck = RecipeManager.createCheck(RecipeTypeRegistry.CALCINATION.get());
    }

    public boolean hasHeatProvider() {
        var blockEntity = this.level.getBlockEntity(this.getBlockPos().below());
        if (blockEntity == null) {
            return false;
        }

        return blockEntity.getCapability(CapabilityRegistry.HEAT_PROVIDER, Direction.UP).map(provider -> provider.isHot()).orElse(false);
    }

    public boolean isHeated() {
        if (this.level.getGameTime() % CHECK_HEAT_TICK_INTERVAL == 0) {
            var wasHeated = this.isHeated;
            this.isHeated = this.hasHeatProvider();

            if (wasHeated != this.isHeated) {
                var newState = this.getBlockState().setValue(CalcinationOvenBlock.LIT, this.isHeated);
                this.level.setBlock(this.getBlockPos(), newState, 1 | 2);
                this.setChanged();
            }
        }
        return this.isHeated;
    }

    public void tickServer() {
        boolean isHeated = this.isHeated();
        boolean hasInput = !this.inputInventory.getStackInSlot(0).isEmpty();

        if (hasInput) {
            //only even check for recipe if we have fuel and input or are currently burning to avoid unnecessary lookups
            Recipe<?> recipe;
            if (hasInput) {
                recipe = this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).orElse(null);
            } else {
                recipe = null;
            }

            //if we are lit and have a recipe, update progress
            if (isHeated && this.canCraft(recipe)) {
                ++this.calcinationProgress;

                //if we hit max progress, craft the item and reset progress
                if (this.calcinationProgress == this.calcinationTotalTime) {
                    this.calcinationProgress = 0;
                    this.calcinationTotalTime = this.getTotalCalcinationTime();
                    if (this.craft(recipe)) {
                        //TODO: handle experience gain on taking out of items
                        //this.setRecipeUsed(recipe);
                    }
                }
            } else {
                this.calcinationProgress = 0;
            }
        } else if (!isHeated && this.calcinationProgress > 0) {
            //if we are not heated, we slowly lose progress
            this.calcinationProgress = Mth.clamp(this.calcinationProgress - 2, 0, this.calcinationTotalTime);
        }
    }

    private boolean canCraft(@Nullable Recipe<?> pRecipe) {
        if (pRecipe == null)
            return false;

        if (this.inputInventory.getStackInSlot(0).isEmpty())
            return false;

        var assembledStack = ((Recipe<Container>) pRecipe).assemble(this.inputRecipeWrapper);
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var outputStack = this.outputInventory.getStackInSlot(0);
            if (outputStack.isEmpty()) {
                return true;
            } else if (!outputStack.sameItem(assembledStack)) {
                return false;
            } else if (outputStack.getCount() + assembledStack.getCount() <= this.outputInventory.getSlotLimit(0)
                    && outputStack.getCount() + assembledStack.getCount() <= outputStack.getMaxStackSize()) {
                return true;
            } else {
                return outputStack.getCount() + assembledStack.getCount() <= assembledStack.getMaxStackSize();
            }
        }

    }

    private boolean craft(@Nullable Recipe<?> pRecipe) {
        if (!this.canCraft(pRecipe))
            return false;

        var inputStack = this.inputInventory.getStackInSlot(0);
        var assembledStack = ((Recipe<Container>) pRecipe).assemble(this.inputRecipeWrapper);
        var outputStack = this.outputInventory.getStackInSlot(0);
        if (outputStack.isEmpty()) {
            this.inputInventory.setStackInSlot(0, assembledStack.copy());
        } else if (outputStack.is(assembledStack.getItem())) {
            outputStack.grow(assembledStack.getCount());
        }

        inputStack.shrink(1);
        return true;

    }

    protected int getTotalCalcinationTime() {
        return this.recipeCachedCheck.getRecipeFor(this.inputRecipeWrapper, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            //TODO: make this return handlers per side, but still use our input/output validation
            //probably just need to move the validation to the input/output inventories
            return this.inventoryCapability.cast();
//            if (side == Direction.UP) return this.inputInventoryCapability.cast();
//            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
//            return this.inventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.putShort("calcinationProgress", (short) this.calcinationProgress);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("calcinationProgress"))
            this.calcinationProgress = pTag.getShort("calcinationProgress");
    }

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tempInv = new ItemStackHandler(1);
        tempInv.setStackInSlot(0, stack);
        RecipeWrapper tempRecipeWrapper = new RecipeWrapper(tempInv);

        return this.recipeCachedCheck.getRecipeFor(tempRecipeWrapper, this.level).isPresent();
    }

    private class Inventory extends CombinedInvWrapper {

        public Inventory() {
            super(CalcinationOvenBlockEntity.this.inputInventory, CalcinationOvenBlockEntity.this.outputInventory);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (CalcinationOvenBlockEntity.this.outputInventory == this.getHandlerFromIndex(this.getIndexForSlot(slot)))
                return false; //insert into output inventory is never possible

            return CalcinationOvenBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (!this.isItemValid(slot, stack))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }
    }
}
