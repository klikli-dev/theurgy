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

package com.github.klikli_dev.theurgy.common.network;

import com.github.klikli_dev.theurgy.common.handlers.ClientRenderEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MessageEssentiaChunkData extends MessageBase {

    //region Fields
    //TODO: store essentia map here

    public Map<Item, Integer> essentia;

    //endregion Fields

    //region Initialization

    public MessageEssentiaChunkData(PacketBuffer buf) {
        this.decode(buf);
    }

    public MessageEssentiaChunkData(Map<Item, Integer> essentia) {
        this.essentia = essentia;
    }
    //endregion Initialization

    //region Overrides

    @Override
    public void onClientReceived(Minecraft minecraft, PlayerEntity player, NetworkEvent.Context context) {
        ClientRenderEventHandler.displayChunkEssentiaTicks = ClientRenderEventHandler.CHUNK_ESSENTIA_TICKS;
        ClientRenderEventHandler.chunkEssentia = this.essentia;
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeVarInt(this.essentia.size());
        this.essentia.forEach((item, amount) ->{
            buf.writeResourceLocation(item.getRegistryName());
            buf.writeVarInt(amount);
        });
    }

    @Override
    public void decode(PacketBuffer buf) {
        int size = buf.readVarInt();
        this.essentia = new HashMap<>(size);
        for(int i = 0; i < size; i++){
            Item item = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            int amount = buf.readVarInt();
            this.essentia.put(item, amount);
        }
    }
    //endregion Overrides
}
