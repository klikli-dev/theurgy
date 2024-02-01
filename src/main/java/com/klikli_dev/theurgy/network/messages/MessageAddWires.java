package com.klikli_dev.theurgy.network.messages;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.logistics.Wire;
import com.klikli_dev.theurgy.logistics.Wires;
import com.klikli_dev.theurgy.network.Message;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class MessageAddWires implements Message {

    public static final ResourceLocation ID = Theurgy.loc("add_wires");

    public Set<Wire> wires;

    public MessageAddWires(Set<Wire> wires) {
        this.wires = wires;
    }

    public MessageAddWires(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(this.wires, (buf1, wire) -> buf1.writeNbt(wire.save(new CompoundTag())));
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.wires = buf.readCollection(ObjectOpenHashSet::new, (buf1) -> Wire.load(buf1.readNbt()));
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player) {
        this.wires.forEach(Wires.get(minecraft.level)::addWire);
    }
}
