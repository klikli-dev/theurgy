package com.github.klikli_dev.theurgy.common.tile;

import com.github.klikli_dev.theurgy.client.particle.CrucibleBubbleParticleData;
import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleItemStackFakeInventory;
import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.EssentiaRecipe;
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaCache;
import com.github.klikli_dev.theurgy.registry.RecipeRegistry;
import com.github.klikli_dev.theurgy.registry.TagRegistry;
import com.github.klikli_dev.theurgy.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CrucibleTileEntity extends NetworkedTileEntity implements ITickableTileEntity, IActivatableTileEntity {

    //region Fields
    static final int MAX_WATER_LEVEL = 4;
    static final int STIRRING_CRAFTING_TICKS = 100;
    /**
     * Random used to color bubble particles.
     */
    protected final Random colorRandom = new Random();
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

    /**
     * The essentia stored in the crucible
     */
    public EssentiaCache essentiaCache;

    /**
     * The fake inventory to use for dissolution crafting
     */
    public CrucibleItemStackFakeInventory fakeInventory;

    //endregion Fields
    //region Initialization
    public CrucibleTileEntity() {
        super(TileRegistry.CRUCIBLE.get());
        this.essentiaCache = new EssentiaCache();
        this.fakeInventory = new CrucibleItemStackFakeInventory(ItemStack.EMPTY, this.essentiaCache);
    }
    //endregion Initialization

    //region Getter / Setter
    public boolean isOnHeatSource() {
        BlockState heatSourceBlock = this.world.getBlockState(this.pos.down());
        if (TagRegistry.HEAT_SOURCES.contains(heatSourceBlock.getBlock()))
            return true;

        return TagRegistry.HEAT_SOURCES_LIT.contains(heatSourceBlock.getBlock()) && heatSourceBlock.get(
                BlockStateProperties.LIT);
    }

    //endregion Getter / Setter
    //region Overrides
    @Override
    public void tick() {

        //count down crafting ticks
        if (this.remainingCraftingTicks > 0) {
            this.remainingCraftingTicks--;
            if (this.remainingCraftingTicks <= 0)
                this.markNetworkDirty();
        }

        //every 10 seconds, check boiling status
        if (this.waterLevel > 0 && this.world.getGameTime() % 200 == 0) {
            boolean isOnHeatSource = this.isOnHeatSource();

            //if heat source was added, start boiling
            if (!this.isBoiling && isOnHeatSource) {
                this.isBoiling = true;
                if (!this.world.isRemote)
                    this.markNetworkDirty();
            }
            //if heat is removed, stop boiling
            else if (this.isBoiling && !isOnHeatSource) {
                this.isBoiling = false;
                this.remainingCraftingTicks = 0;
                this.hasContents = false;
                //TODO: release flux here
                if (!this.world.isRemote)
                    this.markNetworkDirty();
            }
        }

        //on client, show boiling particles
        if (this.world.isRemote && this.waterLevel > 0 && this.isBoiling) {

            float bubbleR = 1.0f;
            float bubbleG = 1.0f;
            float bubbleB = 1.0f;

            if (this.hasContents && this.remainingCraftingTicks > 0) {
                //if we are crafting, show random colored bubbles
                bubbleR = this.colorRandom.nextFloat();
                // /2.0f: reduce green and blue, to keep them slightly purplish
                // +0.5f: use only lighter green and blue tones to keep bubbles light
                bubbleG = this.colorRandom.nextFloat() / 2.0f + 0.5f;
                bubbleB = this.colorRandom.nextFloat() / 2.0f + 0.5f;
            }
            else if (this.hasContents) {
                //If we have contents but are not crafting, show purple bubbles to match purple render
                int bubbleColor = 0xDDA0DD;
                bubbleR = ColorHelper.PackedColor.getRed(bubbleColor) / 255.0f;
                bubbleG = ColorHelper.PackedColor.getGreen(bubbleColor) / 255.0f;
                bubbleB = ColorHelper.PackedColor.getBlue(bubbleColor) / 255.0f;
            }

            CrucibleBubbleParticleData data = new CrucibleBubbleParticleData(bubbleR, bubbleG, bubbleB);

            float waterPlaneHeight = 0.1f + 0.2f * this.waterLevel;

            this.world.addParticle(data,
                    this.pos.getX() + 0.15 + 0.7 * this.world.rand.nextFloat(),
                    this.pos.getY() + waterPlaneHeight,
                    this.pos.getZ() + 0.15 + 0.7 * this.world.rand.nextFloat(),
                    0.0, 0.015, 0.0
            );
        }

        //if we have boiling water, check dropped items on a slow tick
        if (!this.world.isRemote && this.waterLevel > 0 && this.isBoiling && this.world.getGameTime() % 10 == 0) {

            //first, disable instant pickup on any items in the crucible to avoid accidental pickup
            List<ItemEntity> items =
                    this.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(this.pos).shrink(0.125));
            //for (ItemEntity item : items) item.setPickupDelay(40); //2 second pickup delay

            if (this.remainingCraftingTicks > 0) {
                //If we are in crafting mode, check for crafting recipes
                boolean craftedAny = false;
                for (ItemEntity item : items) {
                    //place item in our fake inventory
                    this.fakeInventory.setInventorySlotContents(0, item.getItem());

                    //look up fitting recipes
                    Optional<CrucibleRecipe> recipe =
                            this.world.getRecipeManager().getRecipe(RecipeRegistry.CRUCIBLE_TYPE.get(),
                                    this.fakeInventory, this.world);

                    if (recipe.isPresent()) {
                        craftedAny = true;
                        item.remove();

                        //take essentia from cache
                        recipe.get().getEssentia()
                                .forEach(essentia -> this.essentiaCache.take(essentia.getItem(), essentia.getCount()));

                        //get crafting result
                        ItemStack result = recipe.get().getCraftingResult(this.fakeInventory);

                        //InventoryHelper.spawnItemStack(this.world, this.pos.getX() + 0.5, this.pos.getX() + 1.5, this.pos.getZ() + 0.5, result);
                        //spawn item with proper motion, spawn item stack sometimes gets stuck in the cauldron
                        double angle = this.world.rand.nextDouble() * Math.PI * 2;
                        ItemEntity entity = new ItemEntity(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.75,
                                this.pos.getZ() + 0.5, result);
                        entity.setMotion(Math.sin(angle) * 0.125, 0.25, Math.cos(angle) * 0.125);
                        entity.setPickupDelay(10);
                        this.world.addEntity(entity);
                    }
                }
                if (craftedAny) {
                    if (this.essentiaCache.isEmpty())
                        this.hasContents = false;
                    this.markNetworkDirty();
                    this.world.playSound(null, this.pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1,
                            1);
                }
            }
            else {
                //if we are not in crafting mode, check for dissolution recipes
                boolean dissolvedAnyItem = false;
                for (ItemEntity item : items) {
                    //place item in our fake inventory
                    this.fakeInventory.setInventorySlotContents(0, item.getItem());

                    //look up fitting recipes
                    Optional<EssentiaRecipe> recipe =
                            this.world.getRecipeManager().getRecipe(RecipeRegistry.ESSENTIA_TYPE.get(),
                                    this.fakeInventory, this.world);

                    if (recipe.isPresent()) {
                        dissolvedAnyItem = true;
                        item.remove();

                        //get crafting result
                        ItemStack result = recipe.get().getCraftingResult(this.fakeInventory);
                        //store result in essentia cache, always consume all
                        this.essentiaCache.add(result.getItem(), result.getCount() * item.getItem().getCount());
                        this.hasContents = true;
                    }
                }
                if (dissolvedAnyItem) {
                    this.markNetworkDirty();
                    this.world.playSound(null, this.pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1, 1);
                }
            }
        }

        //on slow tick very 10sec, dissolve essentia
        if (this.hasContents && this.world.getGameTime() % 200 == 0) {
            this.essentiaCache.removeAll(10);
            if (this.essentiaCache.isEmpty()) {
                this.hasContents = false;
            }
            //TODO: generate flux
            this.markNetworkDirty();
        }
    }

    @Override
    public void readNetwork(CompoundNBT compound) {
        super.readNetwork(compound);
        this.waterLevel = compound.getByte("waterLevel");
        this.remainingCraftingTicks = compound.getByte("remainingCraftingTicks");
        this.isBoiling = compound.getBoolean("isBoiling");
        this.hasContents = compound.getBoolean("hasContents");
        if (compound.contains("essentiaCache")) {
            this.essentiaCache.deserializeNBT(compound.getCompound("essentiaCache"));
        }
    }

    @Override
    public CompoundNBT writeNetwork(CompoundNBT compound) {
        compound.putByte("waterLevel", (byte) this.waterLevel);
        compound.putByte("remainingCraftingTicks", (byte) this.remainingCraftingTicks);
        compound.putBoolean("isBoiling", this.isBoiling);
        compound.putBoolean("hasContents", this.hasContents);
        compound.put("essentiaCache", this.essentiaCache.serializeNBT());
        return super.writeNetwork(compound);
    }

    @Override
    public ActionResultType onTileEntityActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            //On shift-click with empty hand, empty and reset the crucible
            if (player.isSneaking() && player.getHeldItem(hand).isEmpty() && this.waterLevel > 0) {
                this.resetCrucible();
                //TODO: release flux here
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

                if (this.waterLevel == 0) {
                    //TODO: release flux here
                    this.resetCrucible();
                }

                if (!this.world.isRemote) {
                    this.markNetworkDirty();
                    //vanilla cauldron empty sound
                    this.world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.SUCCESS;
            }

            if (TagRegistry.RODS_WOODEN.contains(player.getHeldItem(hand).getItem()) && this.isBoiling &&
                this.hasContents) {
                this.remainingCraftingTicks = STIRRING_CRAFTING_TICKS;

                if (!this.world.isRemote) {
                    this.markNetworkDirty();
                    //Play splash sound
                    this.world
                            .playSound(null, pos, SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, SoundCategory.BLOCKS, 1.0F,
                                    1.0F);
                }
                return ActionResultType.SUCCESS;
            }

        }
        return ActionResultType.PASS;
    }
    //endregion Overrides

    //region Methods
    public void resetCrucible() {
        this.isBoiling = false;
        this.hasContents = false;
        this.essentiaCache.clear();
        this.waterLevel = 0;
        this.remainingCraftingTicks = 0;
    }
    //endregion Methods
}
