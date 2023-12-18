package com.klikli_dev.theurgy.content.apparatus.reformationarray;

import com.klikli_dev.theurgy.TheurgyConstants;
import com.klikli_dev.theurgy.content.behaviour.CraftingBehaviour;
import com.klikli_dev.theurgy.content.behaviour.selection.SelectionBehaviour;
import com.klikli_dev.theurgy.content.capability.DefaultMercuryFluxStorage;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import com.klikli_dev.theurgy.content.recipe.wrapper.ReformationArrayRecipeWrapper;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.content.render.outliner.Outliner;
import com.klikli_dev.theurgy.registry.BlockEntityRegistry;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import com.klikli_dev.theurgy.registry.CapabilityRegistry;
import com.klikli_dev.theurgy.util.EntityUtil;
import com.mojang.datafixers.util.Pair;
import io.netty.handler.codec.EncoderException;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SulfuricFluxEmitterBlockEntity extends BlockEntity {

    public static final int CAPACITY = 1000;
    public MercuryFluxStorage mercuryFluxStorage;

    public LazyOptional<MercuryFluxStorage> mercuryFluxStorageCapability;
    public boolean isValidMultiblock;
    protected List<SulfuricFluxEmitterSelectedPoint> sourcePedestals;
    protected List<SulfuricFluxEmitterSelectedPoint> sourcePedestalsWithContents;
    protected SulfuricFluxEmitterSelectedPoint targetPedestal;
    protected SulfuricFluxEmitterSelectedPoint resultPedestal;
    protected CraftingBehaviour<?, ?, ?> craftingBehaviour;
    protected boolean checkValidMultiblockOnNextQuery;
    protected boolean hasSourceItems;
    protected boolean hasTargetItem;
    private ReformationArrayRecipeWrapper recipeWrapper;

    public SulfuricFluxEmitterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.SULFURIC_FLUX_EMITTER.get(), pPos, pBlockState);

        this.mercuryFluxStorage = new MercuryFluxStorage(CAPACITY);
        this.mercuryFluxStorageCapability = LazyOptional.of(() -> this.mercuryFluxStorage);

        this.checkValidMultiblockOnNextQuery = true;

        this.sourcePedestals = new ArrayList<>();
        this.sourcePedestalsWithContents = new ArrayList<>();

        this.craftingBehaviour = new ReformationArrayCraftingBehaviour(this, () -> this.recipeWrapper, () -> null, this::getOutputInventory, () -> this.mercuryFluxStorage);
    }

    public void removeResultPedestal(ReformationResultPedestalBlockEntity pedestal) {
        this.isValidMultiblock = false;
        this.onDisassembleMultiblock();
    }

    public void removeTargetPedestal(ReformationTargetPedestalBlockEntity pedestal) {
        this.isValidMultiblock = false;
        this.hasTargetItem = false;
        this.onDisassembleMultiblock();
    }

    public void removeSourcePedestal(ReformationSourcePedestalBlockEntity pedestal) {
        //check if any valid source pedestals remain.
        var hasRemainingPedestals = this.sourcePedestals.stream()
                .filter(p -> !p.getBlockPos().equals(pedestal.getBlockPos())) //skip the one that is being removed
                .map(p -> this.level.getBlockEntity(p.getBlockPos()))
                .anyMatch(e -> e instanceof ReformationSourcePedestalBlockEntity);

        //if not, we don't have a valid multiblock anymore
        if (!hasRemainingPedestals) {
            this.isValidMultiblock = false;
            this.hasSourceItems = false;
            this.onDisassembleMultiblock();
        } else {
            //force update of source pedestals with contents
            this.onSourcePedestalContentChange(null);
        }
    }

    public boolean isValidMultiblock() {
        if (this.checkValidMultiblockOnNextQuery) {
            this.checkValidMultiblockOnNextQuery = false;
            this.validateMultiblock();
        }
        return this.isValidMultiblock;
    }

    public void validateMultiblock() {
        var wasValidMultiblock = this.isValidMultiblock;

        this.isValidMultiblock = true; //set to true, then set to false if any of the checks fail

        if (this.targetPedestal != null) {
            var targetPedestalBlockEntity = (ReformationTargetPedestalBlockEntity) this.level.getBlockEntity(this.targetPedestal.getBlockPos());
            if (targetPedestalBlockEntity == null) {
                this.isValidMultiblock = false;
            }
        } else {
            this.isValidMultiblock = false;
        }

        if (this.resultPedestal != null) {
            var resultPedestalBlockEntity = (ReformationResultPedestalBlockEntity) this.level.getBlockEntity(this.resultPedestal.getBlockPos());
            if (resultPedestalBlockEntity == null) {
                this.isValidMultiblock = false;
            }
        } else {
            this.isValidMultiblock = false;
        }

        var hasSourcePedestals = false;
        for (var sourcePedestal : this.sourcePedestals) {
            var sourcePedestalBlockEntity = (ReformationSourcePedestalBlockEntity) this.level.getBlockEntity(sourcePedestal.getBlockPos());
            if (sourcePedestalBlockEntity != null) {
                hasSourcePedestals = true;
            }
        }

        this.sourcePedestalsWithContents.clear();
        if (!hasSourcePedestals)
            this.isValidMultiblock = false;
        else {
            //now force rebuilding the source pedestals with contents
            this.onSourcePedestalContentChange(null);
        }

        if (wasValidMultiblock != this.isValidMultiblock) {
            if (this.isValidMultiblock) {
                this.onAssembleMultiblock();
            } else {
                this.onDisassembleMultiblock();
            }
        }
    }

    public void onAssembleMultiblock() {
        var targetPedestalBlockEntity = (ReformationTargetPedestalBlockEntity) this.level.getBlockEntity(this.targetPedestal.getBlockPos());
        targetPedestalBlockEntity.setSulfuricFluxEmitter(this);
        this.onTargetPedestalContentChange(targetPedestalBlockEntity);

        var resultPedestalBlockEntity = (ReformationResultPedestalBlockEntity) this.level.getBlockEntity(this.resultPedestal.getBlockPos());
        resultPedestalBlockEntity.setSulfuricFluxEmitter(this);

        var sourceInventories = this.sourcePedestals.stream()
                .map(p -> this.level.getBlockEntity(p.getBlockPos()))
                .filter(e -> e instanceof ReformationSourcePedestalBlockEntity) //filter out potentially (currently) null ones
                .map(e -> (ReformationSourcePedestalBlockEntity) e)
                .peek(e -> e.setSulfuricFluxEmitter(this))
                .map(e -> e.inputInventory)
                .map(e -> (IItemHandlerModifiable) e)
                .toList();

        this.onSourcePedestalContentChange(null); //only call it once as we don't need to call it on each


        this.recipeWrapper = new ReformationArrayRecipeWrapper(sourceInventories, targetPedestalBlockEntity.inputInventory, this.mercuryFluxStorage);
    }

    public void onDisassembleMultiblock() {
        this.recipeWrapper = null;
    }

    public IItemHandlerModifiable getOutputInventory() {
        var pos = this.resultPedestal.getBlockPos();
        var blockEntity = this.level.getBlockEntity(pos);
        if (blockEntity instanceof ReformationResultPedestalBlockEntity pedestal) {
            return pedestal.outputInventory;
        }
        return null;
    }

    public void tickServer() {
        boolean hasInput = this.isValidMultiblock() && this.hasSourceItems && this.hasTargetItem;
        this.craftingBehaviour.tickServer(true, hasInput);
    }

    public void tickClient() {
        if (this.craftingBehaviour.isProcessing()) {
            if (this.level.getGameTime() % 40 == 0) {
                DistHelper.sendTargetProjectile(this);
            }
        }
    }

    @Override
    public void onLoad() {
        if (this.targetPedestal != null)
            this.targetPedestal.setLevel(this.level);
        if (this.resultPedestal != null)
            this.resultPedestal.setLevel(this.level);
        this.sourcePedestals.forEach(point -> point.setLevel(this.getLevel()));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityRegistry.MERCURY_FLUX) {
            return this.mercuryFluxStorageCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        this.mercuryFluxStorageCapability.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);

        pTag.put("mercuryFluxStorage", this.mercuryFluxStorage.serializeNBT());
        pTag.put("sourcePedestals", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.sourcePedestals), (e) -> new EncoderException("Failed to encode: " + e + " " + this.sourcePedestals)));

        if (this.targetPedestal != null)
            pTag.put("targetPedestal", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(NbtOps.INSTANCE, this.targetPedestal), (e) -> new EncoderException("Failed to encode: " + e + " " + this.targetPedestal)));

        if (this.resultPedestal != null)
            pTag.put("resultPedestal", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(NbtOps.INSTANCE, this.resultPedestal), (e) -> new EncoderException("Failed to encode: " + e + " " + this.resultPedestal)));

        this.craftingBehaviour.saveAdditional(pTag);

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        if (pTag.contains("mercuryFluxStorage"))
            //get instead of getCompound here because the storage serializes as int tag
            this.mercuryFluxStorage.deserializeNBT(pTag.get("mercuryFluxStorage"));

        if (pTag.contains("sourcePedestals")) {
            this.sourcePedestals.clear();
            this.sourcePedestals.addAll(Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.LIST_CODEC.parse(NbtOps.INSTANCE, pTag.get("sourcePedestals")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("sourcePedestals"))));
        }

        if (pTag.contains("targetPedestal")) {
            this.targetPedestal = Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.parse(NbtOps.INSTANCE, pTag.get("targetPedestal")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("targetPedestal")));
        }

        if (pTag.contains("resultPedestal")) {
            this.resultPedestal = Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.parse(NbtOps.INSTANCE, pTag.get("resultPedestal")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("resultPedestal")));
        }

        this.craftingBehaviour.load(pTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        this.writeNetwork(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.readNetwork(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        var tag = packet.getTag();
        if (tag != null) {
            this.readNetwork(tag);
        }
    }

    public void readNetwork(CompoundTag pTag) {

        if (pTag.contains("sourcePedestalsWithContents")) {
            this.sourcePedestalsWithContents.clear();
            this.sourcePedestalsWithContents.addAll(Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.LIST_CODEC.parse(NbtOps.INSTANCE, pTag.get("sourcePedestalsWithContents")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("sourcePedestalsWithContents"))));
        }

        if (pTag.contains("targetPedestal")) {
            this.targetPedestal = Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.parse(NbtOps.INSTANCE, pTag.get("targetPedestal")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("targetPedestal")));
        }

        if (pTag.contains("resultPedestal")) {
            this.resultPedestal = Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.parse(NbtOps.INSTANCE, pTag.get("resultPedestal")), (e) -> new EncoderException("Failed to decode: " + e + " " + pTag.get("resultPedestal")));
        }

        this.craftingBehaviour.readNetwork(pTag);
    }

    public void writeNetwork(CompoundTag pTag) {
        pTag.put("sourcePedestalsWithContents", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.sourcePedestalsWithContents), (e) -> new EncoderException("Failed to encode: " + e + " " + this.sourcePedestalsWithContents)));

        if (this.targetPedestal != null)
            pTag.put("targetPedestal", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(NbtOps.INSTANCE, this.targetPedestal), (e) -> new EncoderException("Failed to encode: " + e + " " + this.targetPedestal)));

        if (this.resultPedestal != null)
            pTag.put("resultPedestal", Util.getOrThrow(SulfuricFluxEmitterSelectedPoint.CODEC.encodeStart(NbtOps.INSTANCE, this.resultPedestal), (e) -> new EncoderException("Failed to encode: " + e + " " + this.resultPedestal)));

        this.craftingBehaviour.writeNetwork(pTag);
    }

    public SelectionBehaviour<SulfuricFluxEmitterSelectedPoint> getSelectionBehaviour() {
        return BlockRegistry.SULFURIC_FLUX_EMITTER.get().selectionBehaviour();
    }

    public void setSelectedPoints(List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        var range = this.getSelectionBehaviour().getBlockRange();

        this.sourcePedestals.clear();
        this.sourcePedestals.addAll(sourcePedestals);
        this.sourcePedestals.removeIf(p -> !p.getBlockPos().closerThan(this.getBlockPos(), range));

        this.targetPedestal = targetPedestal == null ? null : targetPedestal.getBlockPos().closerThan(this.getBlockPos(), range) ? targetPedestal : null;
        this.resultPedestal = resultPedestal == null ? null : resultPedestal.getBlockPos().closerThan(this.getBlockPos(), range) ? resultPedestal : null;

        this.checkValidMultiblockOnNextQuery = true;

        this.setChanged();
    }

    /**
     * client-side variant that does no checks
     */
    public void setSelectedPointsClient(List<SulfuricFluxEmitterSelectedPoint> sourcePedestals, SulfuricFluxEmitterSelectedPoint targetPedestal, SulfuricFluxEmitterSelectedPoint resultPedestal) {
        this.sourcePedestals.clear();
        this.sourcePedestals.addAll(sourcePedestals);

        this.targetPedestal = targetPedestal;
        this.resultPedestal = resultPedestal;
    }

    public void onTargetPedestalContentChange(ReformationTargetPedestalBlockEntity pedestal) {
        this.hasTargetItem = !pedestal.inputInventory.getStackInSlot(0).isEmpty();
    }

    public void onSourcePedestalContentChange(ReformationSourcePedestalBlockEntity pedestal) {
        this.sourcePedestalsWithContents.clear();
        this.hasSourceItems = this.sourcePedestals.stream().map(p -> new Pair<>(p, this.level.getBlockEntity(p.getBlockPos())))
                .filter(p -> p.getSecond() instanceof ReformationSourcePedestalBlockEntity)
                .map(p -> new Pair<>(p.getFirst(), (ReformationSourcePedestalBlockEntity) p.getSecond()))
                .filter(p -> !p.getSecond().inputInventory.getStackInSlot(0).isEmpty())
                .peek(p -> this.sourcePedestalsWithContents.add(p.getFirst()))
                .count() > 0; //need to count to force the stream to run on all elements. findAny() would only run on one.
        this.setChanged();
    }

    public static class DistHelper {

        static void sendTargetProjectile(SulfuricFluxEmitterBlockEntity emitter) {
            var normal = Vec3.atLowerCornerOf(emitter.getBlockState().getValue(BlockStateProperties.FACING).getNormal());
            var from = Vec3.atCenterOf(emitter.getBlockPos()).subtract(normal.scale(0.5));
            var to = Vec3.atCenterOf(emitter.targetPedestal.getBlockPos()).add(0, 0.5, 0);

            if (emitter.level.isLoaded(BlockPos.containing(to)) && emitter.level.isLoaded(BlockPos.containing(from)) && emitter.level.isClientSide) {
                FollowProjectile projectile = new FollowProjectile(emitter.level, from, to, new Color(0xffffff, false), new Color(0x0000ff, false), 0.1f, 0.3f, (targetProjectile) -> {
                    DistHelper.sendSourceProjectiles(targetProjectile, emitter);
                });
                projectile.setDeltaMovement(normal.scale(0.3f));

                EntityUtil.spawnEntityClientSide(emitter.level, projectile);
            }
        }

        static void sendSourceProjectiles(FollowProjectile targetProjectile, SulfuricFluxEmitterBlockEntity emitter) {
            for (var sourcePedestal : emitter.sourcePedestalsWithContents) {
                var from = targetProjectile.position();
                var to = Vec3.atCenterOf(sourcePedestal.getBlockPos()).add(0, 0.7, 0);
                var normal = targetProjectile.to().subtract(targetProjectile.from()).normalize();

                if (emitter.level.isLoaded(BlockPos.containing(to)) && emitter.level.isLoaded(BlockPos.containing(from))) {
                    FollowProjectile projectile = new FollowProjectile(emitter.level, from, to, new Color(0x0000ff, false), new Color(0xFF00FF, false), 0.1f, 0.3f,
                            (sourceProjectile) -> {
                                DistHelper.sendResultProjectile(sourceProjectile, emitter);
                            });

                    //the scale is "force" with which the projectile starts moving in the direction of the normal
                    projectile.setDeltaMovement(normal.scale(0.3f));

                    EntityUtil.spawnEntityClientSide(emitter.level, projectile);
                }
            }
        }

        static void sendResultProjectile(FollowProjectile sourceProjectile, SulfuricFluxEmitterBlockEntity emitter) {
            var from = sourceProjectile.position();
            var to = Vec3.atCenterOf(emitter.resultPedestal.getBlockPos()).add(0, 0.7, 0);
            var normal = sourceProjectile.to().subtract(sourceProjectile.from()).normalize();

            if (emitter.level.isLoaded(BlockPos.containing(to)) && emitter.level.isLoaded(BlockPos.containing(from))) {
                FollowProjectile projectile = new FollowProjectile(emitter.level, from, to, new Color(0xAA08AA, false), new Color(0x00FF00, false), 0.1f, 1f);

                //the scale is "force" with which the projectile starts moving in the direction of the normal
                projectile.setDeltaMovement(normal.scale(0.3f));

                EntityUtil.spawnEntityClientSide(emitter.level, projectile);
            }
        }
    }

    public class MercuryFluxStorage extends DefaultMercuryFluxStorage {

        public MercuryFluxStorage(int capacity) {
            super(capacity);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            var received = super.receiveEnergy(maxReceive, simulate);

            if (received > 0) {
                SulfuricFluxEmitterBlockEntity.this.setChanged();
            }

            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            var extracted = super.extractEnergy(maxExtract, simulate);

            if (extracted > 0) {
                SulfuricFluxEmitterBlockEntity.this.setChanged();
            }

            return extracted;
        }
    }
}
