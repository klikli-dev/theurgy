package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterBlockEntity;
import com.klikli_dev.theurgy.content.apparatus.caloricfluxemitter.CaloricFluxEmitterSelectedPoint;
import com.klikli_dev.theurgy.content.entity.FollowProjectile;
import com.klikli_dev.theurgy.content.item.DivinationRodItem;
import com.klikli_dev.theurgy.content.render.Color;
import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;

public class MessageShowCaloricFlux implements Message {

    public static final Color COLOR = new Color(0xdb3f07, false);

    private BlockPos from;
    private BlockPos to;

    private Direction emitterDirection;

    public MessageShowCaloricFlux(BlockPos from, BlockPos to, Direction emitterDirection) {
        this.from = from;
        this.to = to;
        this.emitterDirection = emitterDirection;
    }

    public MessageShowCaloricFlux(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.from);
        buf.writeBlockPos(this.to);
        buf.writeEnum(this.emitterDirection);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.from = buf.readBlockPos();
        this.to = buf.readBlockPos();
        this.emitterDirection = buf.readEnum(Direction.class);
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player, NetworkEvent.Context context) {
        var level = minecraft.level;
        var normal = Vec3.atLowerCornerOf(this.emitterDirection.getNormal());
        var from = Vec3.atCenterOf(this.from).subtract(normal.scale(0.5));
        var to = Vec3.atCenterOf(this.to);

        if (level.isLoaded(BlockPos.containing(to)) && level.isLoaded(BlockPos.containing(from)) && level.isClientSide) {
            FollowProjectile projectile = new FollowProjectile(level, from, to, COLOR, 0.1f);
            projectile.setDeltaMovement(normal.scale(0.3f));

            EntityUtil.spawnEntityClientSide(level, projectile);
        }
    }

}
