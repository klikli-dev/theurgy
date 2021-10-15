/*
 * MIT License
 *
 * Copyright 2021 klikli-dev
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

package com.klikli_dev.theurgy.network;

import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeData;
import com.klikli_dev.theurgy.data.grafting_hedges.GraftingHedgeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Collections;
import java.util.Map;

public class SyncGraftingHedgesMessage implements IMessage{

    public Map<ResourceLocation, GraftingHedgeData> graftingHedgeData = Collections.emptyMap();

    public SyncGraftingHedgesMessage(Map<ResourceLocation, GraftingHedgeData> graftingHedgeData) {
        this.graftingHedgeData = graftingHedgeData;
    }

    public SyncGraftingHedgesMessage(FriendlyByteBuf buf) {
        this.decode(buf);
    }

    @Override
    public void onClientReceived(Minecraft minecraft, Player player, NetworkEvent.Context context) {
        GraftingHedgeManager.get().onDatapackSyncPacket(this);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.graftingHedgeData.size());
        this.graftingHedgeData.forEach((key, value) -> {
            buf.writeResourceLocation(value.id);
            value.toNetwork(buf);
        });
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        for(int i = 0; i < count; i++){
            ResourceLocation id = buf.readResourceLocation();
            this.graftingHedgeData.put(id, GraftingHedgeData.fromNetwork(id, buf));
        }
    }
}
