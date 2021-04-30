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

package com.klikli_dev.theurgy.command

import com.klikli_dev.theurgy.Theurgy
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import mezz.jei.api.constants.ModIds
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.util.text.*
import net.minecraft.util.text.event.ClickEvent
import net.minecraftforge.items.ItemHandlerHelper
import vazkii.patchouli.api.PatchouliAPI


object HelpCommand : Command<CommandSource> {
    override fun run(context: CommandContext<CommandSource>): Int {
        val text =
            TranslationTextComponent("message.${Theurgy.MOD_ID}.command.help.reply")
                .setStyle(
                    Style.EMPTY.setClickEvent(
                        ClickEvent(
                            ClickEvent.Action.OPEN_URL,
                            "https://invite.gg/klikli"
                        )
                    )
                )

        context.source.sendFeedback(text, false)
        return 0
    }

    fun register(dispatcher: CommandDispatcher<CommandSource>): LiteralArgumentBuilder<CommandSource>? {
        return Commands.literal("help")
            .requires() { cs -> cs.hasPermissionLevel(0) }
            .executes(this)
    }
}