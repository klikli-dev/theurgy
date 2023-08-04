package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.network.Message;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class MessageRequestCaloricFluxEmitterSelection implements Message {

    private BlockPos blockPos;

    public MessageRequestCaloricFluxEmitterSelection(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public MessageRequestCaloricFluxEmitterSelection(FriendlyByteBuf buf) {
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
        BlockRegistry.CALORIC_FLUX_EMITTER.get().getSelectionBehaviour().onPlace(this.blockPos);
    }
}
