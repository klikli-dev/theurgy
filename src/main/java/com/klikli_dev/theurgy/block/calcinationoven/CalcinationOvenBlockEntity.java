package com.klikli_dev.theurgy.block.calcinationoven;

import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CalcinationOvenBlockEntity extends BlockEntity {

    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    public final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            // mark the block entity as dirty so it is saved if inventory is modified.
            CalcinationOvenBlockEntity.this.setChanged();
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack newStack) {
            ItemStack currentStack = this.getStackInSlot(slot);
            boolean differentItem = !newStack.isEmpty() && newStack.sameItem(currentStack) && ItemStack.tagMatches(newStack, currentStack);

            super.setStackInSlot(slot, newStack);

            //do we really need to guard against to big stacks? Vanilla does it ..
            if (newStack.getCount() > this.getSlotLimit(slot)) {
                newStack.setCount(this.getSlotLimit(slot));
            }

            if (slot == INPUT_SLOT && !differentItem) {
                CalcinationOvenBlockEntity.this.calcinationTotalTime = CalcinationOvenBlockEntity.this.getTotalCalcinationTime();
                CalcinationOvenBlockEntity.this.calcinationProgress = 0;
                CalcinationOvenBlockEntity.this.setChanged();
            }
        }
    };
    private final LazyOptional<IItemHandler> inventoryProvider = LazyOptional.of(() -> this.inventory);
    private final RecipeWrapper inventoryRecipeWrapper = new RecipeWrapper(this.inventory);

    private final RangedWrapper inputSlot = new RangedWrapper(this.inventory, INPUT_SLOT, INPUT_SLOT + 1);
    private final LazyOptional<IItemHandler> inputProvider = LazyOptional.of(() -> this.inputSlot);

    private final RangedWrapper fuelSlot = new RangedWrapper(this.inventory, FUEL_SLOT, FUEL_SLOT + 1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
        }
    };
    private final LazyOptional<IItemHandler> fuelProvider = LazyOptional.of(() -> this.fuelSlot);

    private final RangedWrapper outputSlot = new RangedWrapper(this.inventory, OUTPUT_SLOT, OUTPUT_SLOT + 1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false; //this is a take-out slot only.
        }
    };
    private final LazyOptional<IItemHandler> outputProvider = LazyOptional.of(() -> this.outputSlot);

    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> recipeCachedCheck;

    int litTime;
    int litDuration;
    int calcinationProgress;
    int calcinationTotalTime;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            return switch (index) {
                case 0 -> CalcinationOvenBlockEntity.this.litTime;
                case 1 -> CalcinationOvenBlockEntity.this.litDuration;
                case 2 -> CalcinationOvenBlockEntity.this.calcinationProgress;
                case 3 -> CalcinationOvenBlockEntity.this.calcinationTotalTime;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> CalcinationOvenBlockEntity.this.litTime = value;
                case 1 -> CalcinationOvenBlockEntity.this.litDuration = value;
                case 2 -> CalcinationOvenBlockEntity.this.calcinationProgress = value;
                case 3 -> CalcinationOvenBlockEntity.this.calcinationTotalTime = value;
            }

        }

        public int getCount() {
            return 4;
        }
    };

    public CalcinationOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CALCINATION_OVEN.get(), pPos, pBlockState);

        this.recipeCachedCheck = RecipeManager.createCheck(RecipeTypeRegistry.CALCINATION.get());
    }

    public void tickServer() {
        boolean wasTurnedOnDuringThisTick = false;
        boolean wasLitAtBeginOfTick = this.isLit();
        if (wasLitAtBeginOfTick) {
            --this.litTime;
        }

        var fuelStack = this.fuelSlot.getStackInSlot(0);
        boolean hasInput = !this.inputSlot.getStackInSlot(0).isEmpty();
        boolean hasFuel = !fuelStack.isEmpty();

        if (this.isLit() || hasFuel && hasInput) {
            //only even check for recipe if we have fuel and input or are currently burning to avoid unnecessary lookups
            Recipe<?> recipe;
            if (hasInput) {
                recipe = this.recipeCachedCheck.getRecipeFor(this.inventoryRecipeWrapper, this.level).orElse(null);
            } else {
                recipe = null;
            }

            //light the block / turn it on if we have fuel and a recipe
            if (!this.isLit() && this.canCraft(recipe)) {
                this.litTime = this.getBurnDuration(fuelStack);
                this.litDuration = this.litTime;
                if (this.isLit()) {
                    wasTurnedOnDuringThisTick = true;
                    if (fuelStack.hasCraftingRemainingItem())
                        this.fuelSlot.setStackInSlot(0, fuelStack.getCraftingRemainingItem());
                    else if (hasFuel) {
                        fuelStack.shrink(1);
                        if (fuelStack.isEmpty()) {
                            this.fuelSlot.setStackInSlot(0, fuelStack.getCraftingRemainingItem());
                        }
                    }
                }
            }

            //if we are lit and have a recipe, update progress
            if (this.isLit() && this.canCraft(recipe)) {
                ++this.calcinationProgress;

                //if we hit max progress, craft the item and reset progress
                if (this.calcinationProgress == this.calcinationTotalTime) {
                    this.calcinationProgress = 0;
                    this.calcinationTotalTime = this.getTotalCalcinationTime();
                    if (this.craft(recipe)) {
                        //TODO: handle experience gain on taking out of items
                        //this.setRecipeUsed(recipe);
                    }

                    wasTurnedOnDuringThisTick = true;
                }
            } else {
                this.calcinationProgress = 0;
            }
        } else if (!this.isLit() && this.calcinationProgress > 0) {
            this.calcinationProgress = Mth.clamp(this.calcinationProgress - 2, 0, this.calcinationTotalTime);
        }

        if (wasLitAtBeginOfTick != this.isLit()) {
            wasTurnedOnDuringThisTick = true;
            var newState = this.getBlockState().setValue(CalcinationOvenBlock.LIT, this.isLit());
            this.level.setBlock(this.getBlockPos(), newState, 3);
        }

        if (wasTurnedOnDuringThisTick) {
            this.setChanged();
        }
    }

    private boolean canCraft(@Nullable Recipe<?> pRecipe) {
        if (pRecipe == null)
            return false;

        if (this.inputSlot.getStackInSlot(0).isEmpty())
            return false;

        var assembledStack = ((Recipe<Container>) pRecipe).assemble(this.inventoryRecipeWrapper);
        if (assembledStack.isEmpty()) {
            return false;
        } else {
            var outputStack = this.outputSlot.getStackInSlot(0);
            if (outputStack.isEmpty()) {
                return true;
            } else if (!outputStack.sameItem(assembledStack)) {
                return false;
            } else if (outputStack.getCount() + assembledStack.getCount() <= this.outputSlot.getSlotLimit(0)
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

        var inputStack = this.inputSlot.getStackInSlot(0);
        var assembledStack = ((Recipe<Container>) pRecipe).assemble(this.inventoryRecipeWrapper);
        var outputStack = this.outputSlot.getStackInSlot(0);
        if (outputStack.isEmpty()) {
            this.outputSlot.setStackInSlot(0, assembledStack.copy());
        } else if (outputStack.is(assembledStack.getItem())) {
            outputStack.grow(assembledStack.getCount());
        }

        var fuelStack = this.fuelSlot.getStackInSlot(0);
        if (inputStack.is(Blocks.WET_SPONGE.asItem()) && !fuelStack.isEmpty() && fuelStack.is(Items.BUCKET)) {
            this.fuelSlot.setStackInSlot(0, new ItemStack(Items.WATER_BUCKET));
        }

        inputStack.shrink(1);
        return true;

    }

    protected int getBurnDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            Item item = pFuel.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(pFuel, RecipeTypeRegistry.CALCINATION.get());
        }
    }

    protected int getTotalCalcinationTime() {
        return this.recipeCachedCheck.getRecipeFor(this.inventoryRecipeWrapper, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    public boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.UP) return this.inputProvider.cast();
            if (side == Direction.DOWN) return this.outputProvider.cast();
            if (side != null) return this.fuelProvider.cast();
            return this.inventoryProvider.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("inventory", this.inventory.serializeNBT());
        pTag.putShort("litTime", (short) this.litTime);
        pTag.putShort("calcinationProgress", (short) this.calcinationProgress);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        this.inventory.deserializeNBT(pTag.getCompound("inventory"));
        this.litTime = pTag.getShort("litTime");
        this.calcinationProgress = pTag.getShort("calcinationProgress");
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        //none, we don't load anything from the update tag
    }
}
