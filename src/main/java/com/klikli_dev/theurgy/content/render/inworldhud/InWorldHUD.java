// SPDX-FileCopyrightText: 2026 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.render.inworldhud;

import com.klikli_dev.theurgy.config.ClientConfig;
import com.klikli_dev.theurgy.network.Networking;
import com.klikli_dev.theurgy.network.messages.MessageRequestInWorldHUD;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InWorldHUD implements GuiLayer {

    private static final InWorldHUD INSTANCE = new InWorldHUD();
    private static final int REQUEST_INTERVAL_TICKS = 5;
    private static final int TOP_PADDING = 4;

    private @Nullable BlockPos currentPos;
    private InWorldHUDSnapshot serverSnapshot = InWorldHUDSnapshot.EMPTY;
    private int nextRequestTick;

    public static InWorldHUD get() {
        return INSTANCE;
    }

    public void tick(Minecraft minecraft) {
        if (!this.canRun(minecraft)) {
            this.clear();
            return;
        }

        BlockPos lookedAtPos = this.getLookedAtPos(minecraft.hitResult);
        if (lookedAtPos == null || !InWorldHUDRegistry.hasProviders(minecraft.level, lookedAtPos)) {
            this.clear();
            return;
        }

        if (!lookedAtPos.equals(this.currentPos)) {
            this.currentPos = lookedAtPos;
            this.serverSnapshot = InWorldHUDSnapshot.EMPTY;
            this.nextRequestTick = 0;
        }

        if (minecraft.player.tickCount >= this.nextRequestTick) {
            Networking.sendToServer(new MessageRequestInWorldHUD(this.currentPos));
            this.nextRequestTick = minecraft.player.tickCount + REQUEST_INTERVAL_TICKS;
        }
    }

    public void acceptServerSnapshot(BlockPos pos, InWorldHUDSnapshot snapshot) {
        if (this.currentPos != null && this.currentPos.equals(pos)) {
            this.serverSnapshot = snapshot;
        }
    }

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!this.canRun(minecraft) || this.currentPos == null || minecraft.level == null) {
            return;
        }

        BlockPos lookedAtPos = this.getLookedAtPos(minecraft.hitResult);
        if (!this.currentPos.equals(lookedAtPos)) {
            return;
        }

        InWorldHUDBuilder builder = new InWorldHUDBuilder();
        builder.append(InWorldHUDRegistry.gatherClientSnapshot(minecraft.level, this.currentPos));
        builder.append(this.serverSnapshot);

        InWorldHUDSnapshot snapshot = builder.build();
        if (snapshot.isEmpty()) {
            return;
        }

        List<Either<FormattedText, TooltipComponent>> elements = new ArrayList<>();
        snapshot.title().ifPresent(title -> elements.add(Either.left(title)));
        snapshot.lines().forEach(line -> elements.add(Either.left(line)));
        if (!snapshot.items().isEmpty()) {
            elements.add(Either.right(new ItemStacksTooltip(snapshot.items())));
        }

        List<ClientTooltipComponent> components = ClientHooks.gatherTooltipComponentsFromElements(
                ItemStack.EMPTY,
                elements,
                guiGraphics.guiWidth() / 2,
                guiGraphics.guiWidth(),
                guiGraphics.guiHeight(),
                minecraft.font
        );

        guiGraphics.tooltip(minecraft.font, components, guiGraphics.guiWidth() / 2, TOP_PADDING, TopCenterTooltipPositioner.INSTANCE, null);
    }

    private boolean canRun(Minecraft minecraft) {
        return minecraft.player != null
                && minecraft.level != null
                && !minecraft.player.isSpectator()
                && !minecraft.gui.hud.isHidden()
                && ClientConfig.get().rendering.enableInWorldHUD.get();
    }

    private void clear() {
        this.currentPos = null;
        this.serverSnapshot = InWorldHUDSnapshot.EMPTY;
        this.nextRequestTick = 0;
    }

    private @Nullable BlockPos getLookedAtPos(@Nullable HitResult hitResult) {
        if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
            return blockHitResult.getBlockPos();
        }
        return null;
    }
}
