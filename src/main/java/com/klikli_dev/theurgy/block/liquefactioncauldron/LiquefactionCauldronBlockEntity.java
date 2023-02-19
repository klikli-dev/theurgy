package com.klikli_dev.theurgy.block.liquefactioncauldron;

import com.klikli_dev.theurgy.block.calcinationoven.CalcinationOvenBlock;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.registry.FluidTagRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LiquefactionCauldronBlockEntity extends BlockEntity {

    private static final int CHECK_HEAT_TICK_INTERVAL = 20;
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> recipeCachedCheck;
    public ItemStackHandler inputInventory;
    public ItemStackHandler outputInventory;

    public CombinedInvWrapper inventory;
    public LazyOptional<IItemHandler> inventoryCapability;
    public LazyOptional<IItemHandler> inputInventoryCapability;
    public LazyOptional<IItemHandler> outputInventoryCapability;

    public RecipeWrapper inputRecipeWrapper;
    public LazyOptional<IFluidHandler> solventTankCapability;
    protected FluidTank solventTank;
    int liquificationProgress;
    int liquificationTotalTime;

    boolean isHeated;

    public LiquefactionCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LIQUEFACTION_CAULDRON.get(), pPos, pBlockState);

        this.inputInventory = new InputInventory();
        this.outputInventory = new OutputInventory();

        this.inventory = new CombinedInvWrapper(this.inputInventory, this.outputInventory);

        this.inventoryCapability = LazyOptional.of(() -> this.inventory);
        this.inputInventoryCapability = LazyOptional.of(() -> this.inputInventory);
        this.outputInventoryCapability = LazyOptional.of(() -> this.outputInventory);

        this.inputRecipeWrapper = new RecipeWrapper(this.inputInventory);

        this.solventTank = new FluidTank(FluidType.BUCKET_VOLUME, (fluidStack -> fluidStack.getFluid().is(FluidTagRegistry.SOLVENT)));
        this.solventTankCapability = LazyOptional.of(() -> this.solventTank);

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
                ++this.liquificationProgress;

                //if we hit max progress, craft the item and reset progress
                if (this.liquificationProgress == this.liquificationTotalTime) {
                    this.liquificationProgress = 0;
                    this.liquificationTotalTime = this.getTotalCalcinationTime();
                    if (this.craft(recipe)) {
                        //TODO: handle experience gain on taking out of items
                        //this.setRecipeUsed(recipe);
                    }
                }
            } else {
                this.liquificationProgress = 0;
            }
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
            if (side == Direction.UP) return this.inputInventoryCapability.cast();
            if (side == Direction.DOWN) return this.outputInventoryCapability.cast();
            return this.inventoryCapability.cast();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return this.solventTankCapability.cast();

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inputInventory", this.inputInventory.serializeNBT());
        pTag.put("outputInventory", this.outputInventory.serializeNBT());
        pTag.putShort("liquificationProgress", (short) this.liquificationProgress);
        var solventTankTag = new CompoundTag();
        this.solventTank.writeToNBT(pTag);
        pTag.put("solventTank", solventTankTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("inputInventory"))
            this.inputInventory.deserializeNBT(pTag.getCompound("inputInventory"));
        if (pTag.contains("outputInventory"))
            this.outputInventory.deserializeNBT(pTag.getCompound("outputInventory"));
        if (pTag.contains("liquificationProgress"))
            this.liquificationProgress = pTag.getShort("liquificationProgress");

        if (pTag.contains("solventTank")) {
            var solventTankTag = pTag.getCompound("solventTank");
            this.solventTank.readFromNBT(solventTankTag);
        }
    }

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tempInv = new ItemStackHandler(1);
        tempInv.setStackInSlot(0, stack);
        RecipeWrapper tempRecipeWrapper = new RecipeWrapper(tempInv);

        return this.recipeCachedCheck.getRecipeFor(tempRecipeWrapper, this.level).isPresent();
    }

    public class InputInventory extends ItemStackHandler {

        public InputInventory() {
            super(1);
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack newStack) {

            var oldStack = this.getStackInSlot(slot);

            boolean sameItem = !newStack.isEmpty() && newStack.sameItem(oldStack) && ItemStack.tagMatches(newStack, oldStack);

            super.setStackInSlot(slot, newStack);

            if (!sameItem) {
                LiquefactionCauldronBlockEntity.this.liquificationTotalTime = LiquefactionCauldronBlockEntity.this.getTotalCalcinationTime();
                LiquefactionCauldronBlockEntity.this.liquificationProgress = 0;
                LiquefactionCauldronBlockEntity.this.setChanged();
            }

        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return LiquefactionCauldronBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }
    }

    public class OutputInventory extends ItemStackHandler {

        public OutputInventory() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }
    }
}
