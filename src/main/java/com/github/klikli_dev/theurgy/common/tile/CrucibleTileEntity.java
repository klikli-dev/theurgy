package com.github.klikli_dev.theurgy.common.tile;

import com.github.klikli_dev.theurgy.registry.TagRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class CrucibleTileEntity extends NetworkedTileEntity implements ITickableTileEntity, IActivatableTileEntity {

    //region Fields
    static final int MAX_WATER_LEVEL = 4;

    /**
     * True if the water is boiling, allowing to dissolve items.
     */
    public boolean isBoiling;

    /**
     * True if anything has been dissolved in the crucible.
     */
    public boolean hasContents;

    /**
     * The current water level. 0 is empty, MAX_WATER_LEVEL is full.
     */
    public int waterLevel;

    /**
     * The ticks remaining during which items can be dropped in to fulfill recipes
     */
    public int remainingCraftingTicks;
    //endregion Fields

    //region Initialization
    public CrucibleTileEntity() {
        super(TileRegistry.CRUCIBLE.get());
    }
    //endregion Initialization

//region Getter / Setter
    public boolean isOnHeatSource() {
        BlockState heatSourceBlock = world.getBlockState(pos.down());
        if (TagRegistry.HEAT_SOURCES.contains(heatSourceBlock.getBlock()))
            return true;

        if (TagRegistry.HEAT_SOURCES_LIT.contains(heatSourceBlock.getBlock()) && heatSourceBlock.get(
                BlockStateProperties.LIT))
            return true;

        return false;
    }
//endregion Getter / Setter

    //region Overrides
    @Override
    public void tick() {

        //count down crafting ticks
        if (this.remainingCraftingTicks > 0)
            this.remainingCraftingTicks--;

        //every 20 ticks/1 second, check boiling status
        if (this.waterLevel > 0 && this.world.getGameTime() % 200 == 0) {
            boolean isOnHeatSource = this.isOnHeatSource();

            //if heat source was added, start boiling
            if (!this.isBoiling && isOnHeatSource) {
                this.isBoiling = true;
                if (!world.isRemote)
                    this.markNetworkDirty();
            }
            //if heat is removed, stop boiling
            else if (this.isBoiling && !isOnHeatSource) {
                this.isBoiling = false;
                if (!world.isRemote)
                    this.markNetworkDirty();
            }
        }

        //on client, show boiling particles
        if (this.world.isRemote && this.waterLevel > 0 && this.isBoiling) {
            //TODO: show boiling particles
            //TODO: show steam particles

            //TODO: if we have crafting ticks, show more steam
        }

        //TODO: consume dropped in items if boiling
        //TODO: if boiling and crafting timer, check for recipes
    }

    @Override
    public void readNetwork(CompoundNBT compound) {
        super.readNetwork(compound);
        this.waterLevel = compound.getByte("waterLevel");
        this.remainingCraftingTicks = compound.getByte("remainingCraftingTicks");
        this.isBoiling = compound.getBoolean("isBoiling");
        this.hasContents = compound.getBoolean("hasContents");
    }

    @Override
    public CompoundNBT writeNetwork(CompoundNBT compound) {
        compound.putByte("waterLevel", (byte) this.waterLevel);
        compound.putByte("remainingCraftingTicks", (byte) this.remainingCraftingTicks);
        compound.putBoolean("isBoiling", this.isBoiling);
        compound.putBoolean("hasContents", this.hasContents);
        return super.writeNetwork(compound);
    }

    @Override
    public ActionResultType onTileEntityActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            //On shift-click with empty hand, empty and reset the crucible
            if (player.isSneaking() && player.getHeldItem(hand).isEmpty() && this.waterLevel > 0) {
                this.resetCrucible();

                if (!this.world.isRemote) {
                    this.markNetworkDirty();
                    //vanilla cauldron empty sound
                    this.world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.SUCCESS;
            }

            //if clicked with filled bucket, and there is still capacity, fill the crucible further
            if (player.getHeldItem(hand).getItem() == Items.WATER_BUCKET && this.waterLevel < MAX_WATER_LEVEL) {
                player.setHeldItem(hand, new ItemStack(Items.BUCKET));
                this.waterLevel++;

                if (!this.world.isRemote) {
                    this.markNetworkDirty();
                    //vanilla cauldron fill sound
                    this.world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.SUCCESS;
            }
            //if clicked with empty bucket, and there is something in the crucible, fill the bucket
            if (player.getHeldItem(hand).getItem() == Items.BUCKET && this.waterLevel > 0) {
                player.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                this.waterLevel--;

                if(waterLevel == 0)
                    this.resetCrucible();

                if (!this.world.isRemote) {
                    this.markNetworkDirty();
                    //vanilla cauldron empty sound
                    this.world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.SUCCESS;
            }

            //TODO: if clicked with stick, it was "stirred" and crafting ticks need to be set;
            //      play fitting sound (entity.getSplashSound)
            //      crafting ticks can only be enabled while boiling
        }
        return ActionResultType.PASS;
    }
    //endregion Overrides

    //region Methods
    public void resetCrucible() {
        this.isBoiling = false;
        this.hasContents = false;
        this.waterLevel = 0;
        this.remainingCraftingTicks = 0;
    }
    //endregion Methods
}
