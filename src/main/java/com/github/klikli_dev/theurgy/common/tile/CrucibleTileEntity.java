/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.klikli_dev.theurgy.common.tile;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.client.particle.CrucibleBubbleParticleData;
import com.github.klikli_dev.theurgy.client.particle.GlowingBallParticleData;
import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleCraftingType;
import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleItemStackFakeInventory;
import com.github.klikli_dev.theurgy.common.crafting.recipe.CrucibleRecipe;
import com.github.klikli_dev.theurgy.common.crafting.recipe.EssentiaRecipe;
import com.github.klikli_dev.theurgy.common.entity.AetherBallEntity;
import com.github.klikli_dev.theurgy.common.entity.EssentiaBallEntity;
import com.github.klikli_dev.theurgy.common.entity.GlowingBallEntity;
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaCache;
import com.github.klikli_dev.theurgy.common.theurgy.EssentiaType;
import com.github.klikli_dev.theurgy.common.theurgy.essentia_chunks.EssentiaChunkHandler;
import com.github.klikli_dev.theurgy.registry.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CrucibleTileEntity extends NetworkedTileEntity implements ITickableTileEntity, IActivatableTileEntity {

    //region Fields
    static final int MAX_WATER_LEVEL = 4;
    static final int STIRRING_CRAFTING_TICKS = 100;
    static final int MAX_STIRRING_CRAFTING_TICKS = STIRRING_CRAFTING_TICKS * 5;
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
     * The current crucible crafting type - depends on the tool used to stir.
     */
    public CrucibleCraftingType craftingType = CrucibleCraftingType.PURIFICATION;

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

        if(this.world.isRemote && this.testLifetime > 0){
            double targetX = this.testTarget.getX() + 0.5;
            double targetY = this.testTarget.getY() + 0.5;
            double targetZ = this.testTarget.getZ() + 0.5;
            if(this.testTarget.getX() != 0 || this.testTarget.getY() != 0 || this.testTarget.getZ() != 0) {
                Vector3d targetVector = new Vector3d(targetX - this.testPosX, targetY - this.testPosY, targetZ - this.testPosZ);
                double length = targetVector.length();
                targetVector = targetVector.scale(0.3/length);
                double weight  = 0;
                if (length <= 3){
                    weight = 0.9*((3.0-length)/3.0);
                }
                this.testMotionX = (0.9-weight)*this.testMotionX+(0.1+weight)*targetVector.x;
                this.testMotionY = (0.9-weight)*this.testMotionY+(0.1+weight)*targetVector.y;
                this.testMotionZ = (0.9-weight)*this.testMotionZ+(0.1+weight)*targetVector.z;
            }
            this.testPrevPosX = this.testPosX;
            this.testPrevPosY = this.testPosY;
            this.testPrevPosZ = this.testPosZ;
            this.testPosX += this.testMotionX;
            this.testPosY += this.testMotionY;
            this.testPosZ += this.testMotionZ;

            //distance to test target block center
            if(Math.abs(this.testPosX - targetX) < 0.5 && Math.abs(this.testPosY - targetY) < 0.5 && Math.abs(this.testPosZ - targetZ) < 0.5){
                //stop if reached target
                this.testLifetime = 0;
                this.testMotionX = 0;
                this.testMotionY = 0;
                this.testMotionZ = 0;
            }

            double deltaX =  this.testPosX - this.testPrevPosX;
            double deltaY =  this.testPosY - this.testPrevPosY;
            double deltaZ =  this.testPosZ - this.testPrevPosZ;
            double dist = Math.ceil(Math.sqrt(deltaX*deltaX+deltaY*deltaY+deltaZ*deltaZ) * 20);
            for (double i = 0; i < dist; i ++){
                double coeff = i/dist;
               // GlowingBallParticleData data = new GlowingBallParticleData(1.0f, 64.0f/255.0f, 16.0f/255.0f, 0.5f, 0.5f, 12);
                GlowingBallParticleData data = new GlowingBallParticleData(60.0f/255.0f, 1.0f, 16.0f/255.0f, 0.5f, 0.5f, 12);

                this.world.addParticle(data,
                        (float) (this.testPrevPosX + deltaX * coeff),
                        (float) (this.testPrevPosY + deltaY * coeff),
                        (float) (this.testPrevPosZ + deltaZ * coeff),
                        0.0125f*(this.world.rand.nextFloat()-0.5f),
                        0.0125f*(this.world.rand.nextFloat()-0.5f),
                        0.0125f*(this.world.rand.nextFloat()-0.5f)
                        );
            }
        }

        //count down crafting ticks
        if (!this.world.isRemote && this.remainingCraftingTicks > 0) {
            this.remainingCraftingTicks--;
            if (this.remainingCraftingTicks <= 0) {
                this.markNetworkDirty();
            }
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

                this.diffuseAllEssentia();

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
                    Optional<? extends CrucibleRecipe> recipe = Optional.empty();
                    switch (this.craftingType) {
                        case PURIFICATION:
                            recipe = this.world.getRecipeManager().getRecipe(RecipeRegistry.PURIFICATION_TYPE.get(),
                                    this.fakeInventory, this.world);
                            break;
                        case REPLICATION:
                            recipe = this.world.getRecipeManager().getRecipe(RecipeRegistry.REPLICATION_TYPE.get(),
                                    this.fakeInventory, this.world);
                            break;
                        case TRANSMUTATION:
                            recipe = this.world.getRecipeManager().getRecipe(RecipeRegistry.TRANSMUTATION_TYPE.get(),
                                    this.fakeInventory, this.world);
                            break;
                    }


                    if (recipe.isPresent()) {
                        craftedAny = true;

                        //get the maximum crafting count that the input stack allows
                        int craftingCount = item.getItem().getCount();
                        //then check each essentia and find the maximum amount of crafting the essentia cache allows
                        for (ItemStack essentia : recipe.get().getEssentia()) {
                            //if the recipe specifies the essentia, but provides 0, we skip here to avoid div by zero
                            if(essentia.getCount() == 0){
                                continue;
                            }

                            int maxPossibleCraftings =
                                    Math.floorDiv(this.essentiaCache.get(essentia.getItem()), essentia.getCount());
                            if (maxPossibleCraftings < craftingCount)
                                craftingCount = maxPossibleCraftings;
                        }

                        //now take the essentia from the cache
                        for (ItemStack essentia : recipe.get().getEssentia()) {
                            //multiply by crafting count
                            this.essentiaCache.take(essentia.getItem(), essentia.getCount() * craftingCount);
                        }

                        //take input item from stack
                        item.getItem().shrink(craftingCount);
                        //if stack is empty, despawn entity
                        if (item.getItem().isEmpty())
                            item.remove();

                        //get crafting result
                        ItemStack result = recipe.get().getCraftingResult(this.fakeInventory);
                        result.setCount(result.getCount() * craftingCount);

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

                        //get resulting essentia
                        List<ItemStack> essentia = recipe.get().getEssentia();

                        //store result in essentia cache, always use up entire stack
                        essentia.forEach(itemStack -> this.essentiaCache.add(itemStack.getItem(),
                                itemStack.getCount() * item.getItem().getCount()));

                        this.hasContents = true;
                    }
                }
                if (dissolvedAnyItem) {
                    this.markNetworkDirty();
                    this.world.playSound(null, this.pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 1, 1);
                }
            }
        }

        //on slow tick, diffuse essentia
        if (!this.world.isRemote && this.hasContents &&
            this.world.getGameTime() % Theurgy.CONFIG.essentiaSettings.crucibleDiffuseTicks.get() == 0) {

            EssentiaCache chunkEssentia =
                    EssentiaChunkHandler.getEssentiaCache(this.world.getDimensionKey(), new ChunkPos(this.pos));

            //get the amount we want to diffuse
            int amountToDissolve = Theurgy.CONFIG.essentiaSettings.crucibleEssentiaToDiffuse.get();
            List<Item> essentiaToDissolve = new ArrayList<>(this.essentiaCache.essentia.keySet());
            for (Item e : essentiaToDissolve) {
                //get the amount we can actually diffuse
                int amount = Math.min(this.essentiaCache.get(e), amountToDissolve);
                //remove it from crucible cache
                this.essentiaCache.remove(e, amount);
                //add to chunk cache
                chunkEssentia.add(e, amount);
            }

            //Mark essentia dimension for saving
            EssentiaChunkHandler.markDirty(this.world.getDimensionKey());

            if (this.essentiaCache.isEmpty()) {
                this.hasContents = false;
            }
            this.markNetworkDirty();
        }
    }

    @Override
    public void readNetwork(CompoundNBT compound) {
        super.readNetwork(compound);
        this.waterLevel = compound.getByte("waterLevel");
        this.remainingCraftingTicks = compound.getShort("remainingCraftingTicks");
        this.craftingType = CrucibleCraftingType.values()[compound.getByte("craftingType")];
        this.isBoiling = compound.getBoolean("isBoiling");
        this.hasContents = compound.getBoolean("hasContents");
        if (compound.contains("essentiaCache")) {
            this.essentiaCache.deserializeNBT(compound.getCompound("essentiaCache"));
        }
    }

    @Override
    public CompoundNBT writeNetwork(CompoundNBT compound) {
        compound.putByte("waterLevel", (byte) this.waterLevel);
        compound.putShort("remainingCraftingTicks", (short) this.remainingCraftingTicks);
        compound.putByte("craftingType", (byte) this.craftingType.ordinal());
        compound.putBoolean("isBoiling", this.isBoiling);
        compound.putBoolean("hasContents", this.hasContents);
        compound.put("essentiaCache", this.essentiaCache.serializeNBT());
        return super.writeNetwork(compound);
    }

    @Override
    public ActionResultType onTileEntityActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
        if(!this.world.isRemote){
            if (hand == Hand.MAIN_HAND) {
                //On shift-click with empty hand, empty and reset the crucible
                if (player.isSneaking() && player.getHeldItem(hand).isEmpty() && this.waterLevel > 0) {
                    this.diffuseAllEssentia();
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

                    if (this.waterLevel == 0) {
                        this.resetCrucible();
                        this.diffuseAllEssentia();
                    }

                    if (!this.world.isRemote) {
                        this.markNetworkDirty();
                        //vanilla cauldron empty sound
                        this.world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    return ActionResultType.SUCCESS;
                }

                //handle stirring
                if (this.isBoiling && this.hasContents) {
                    ItemStack held = player.getHeldItem(hand);
                    if (held.getItem() == ItemRegistry.IRON_STIRRER.get()) {
                        //Stick leads to purification
                        this.craftingType = CrucibleCraftingType.PURIFICATION;
                    }
                    else if (held.getItem() == ItemRegistry.PURE_CRYSTAL_STIRRER.get()) {
                        //pure crystal is for replication
                        this.craftingType = CrucibleCraftingType.REPLICATION;
                        held.damageItem(1, player, (p) -> {
                        });
                    }
                    else if (held.getItem() == ItemRegistry.PRIMA_MATERIA_CRYSTAL_STIRRER.get()) {
                        //prima materia crystal is for transmutation
                        this.craftingType = CrucibleCraftingType.TRANSMUTATION;
                        held.damageItem(1, player, (p) -> {
                        });
                    }
                    else {
                        //other items do not interest us here
                        return ActionResultType.PASS;
                    }

                    this.remainingCraftingTicks += STIRRING_CRAFTING_TICKS;
                    if (this.remainingCraftingTicks > MAX_STIRRING_CRAFTING_TICKS)
                        this.remainingCraftingTicks = MAX_STIRRING_CRAFTING_TICKS;

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
        }
        return ActionResultType.PASS;
    }
    //endregion Overrides

    //region Methods
    public void diffuseAllEssentia() {
        EssentiaCache chunkEssentia =
                EssentiaChunkHandler.getEssentiaCache(this.world.getDimensionKey(), new ChunkPos(this.pos));

        //take all essentia from the cache and add to the chunk
        List<Item> essentiaToDissolve = new ArrayList<>(this.essentiaCache.essentia.keySet());
        for (Item e : essentiaToDissolve) {
            //get the amount we can actually diffuse
            int amount = this.essentiaCache.get(e);
            //remove it from crucible cache
            this.essentiaCache.remove(e, amount);
            //add to chunk cache
            chunkEssentia.add(e, amount);
        }

        //Mark essentia dimension for saving
        EssentiaChunkHandler.markDirty(this.world.getDimensionKey());
    }

    public void resetCrucible() {
        this.isBoiling = false;
        this.hasContents = false;
        this.essentiaCache.clear();
        this.waterLevel = 0;
        this.remainingCraftingTicks = 0;
    }
    //endregion Methods
}
