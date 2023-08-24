package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class MessageRequestSulfuricFluxEmitterSelection implements Message {

    private BlockPos blockPos;

    public MessageRequestSulfuricFluxEmitterSelection(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public MessageRequestSulfuricFluxEmitterSelection(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player, NetworkEvent.Context context) {
        BlockRegistry.SULFURIC_FLUX_EMITTER.get().getSelectionBehaviour().onPlace(this.blockPos, player);
    }
}
