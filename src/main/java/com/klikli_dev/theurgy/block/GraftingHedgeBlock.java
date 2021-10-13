package com.klikli_dev.theurgy.block;

import com.klikli_dev.theurgy.blockentity.GraftingHedgeBlockEntity;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Random;

public class GraftingHedgeBlock extends BushBlock implements BonemealableBlock, EntityBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final int MAX_AGE = BlockStateProperties.MAX_AGE_3;

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(1.0D, 2.0D, 1.0D, 15.0D, 16.0D, 15.0D),
            Block.box(5.0D, 0.D, 5.0D, 11.0D, 2.0D, 11.0D));

    public GraftingHedgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        int age = pState.getValue(AGE);
        if (age < MAX_AGE && pLevel.getRawBrightness(pPos.above(), 0) >= 9 && ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt(5) == 0)) {
            pLevel.setBlock(pPos, pState.setValue(AGE, age + 1), Constants.BlockFlags.BLOCK_UPDATE);
            ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity) {
            pEntity.makeStuckInBlock(pState, new Vec3(0.8D, 0.75D, 0.8D));
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof GraftingHedgeBlockEntity hedge) {
            ItemStack heldItem = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);

            //Graft fruit to hedge if there is none
            if(hedge.getFruitToGrow().isEmpty() && TagRegistry.FRUITS.contains(heldItem.getItem())){
                ItemStack itemToGraft = heldItem.copy();
                itemToGraft.setCount(1);
                hedge.setFruitToGrow(itemToGraft);
                //reset hedge to avoid people cheesing an instant harvest
                pLevel.setBlock(pPos, pState.setValue(AGE, 0), Constants.BlockFlags.BLOCK_UPDATE);
                pPlayer.swing(InteractionHand.MAIN_HAND);
            }

            //if we have a grafted fruit and it's ripe, harvest
             if(!hedge.getFruitToGrow().isEmpty() && pState.getValue(AGE) == MAX_AGE){
                ItemStack fruit = hedge.getFruitToGrow().copy();
                ItemHandlerHelper.giveItemToPlayer(pPlayer, fruit);
                //reset hedge
                pLevel.setBlock(pPos, pState.setValue(AGE, 0), Constants.BlockFlags.BLOCK_UPDATE);
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return pState.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, Random pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, Random pRandom, BlockPos pPos, BlockState pState) {
        int newAge = Math.min(MAX_AGE, pState.getValue(AGE) + 1);
        pLevel.setBlock(pPos, pState.setValue(AGE, newAge), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegistry.GRAFTING_HEDGE.get().create(pPos, pState);
    }
}
