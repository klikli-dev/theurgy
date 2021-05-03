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
package com.klikli_dev.theurgy.item.tool

import com.klikli_dev.theurgy.api.TheurgyConstants
import com.klikli_dev.theurgy.client.divination.ScanManager
import com.klikli_dev.theurgy.client.render.SelectedBlockRenderer
import com.klikli_dev.theurgy.client.tooltip.TooltipHandler
import com.klikli_dev.theurgy.config.TheurgyServerConfig
import com.klikli_dev.theurgy.network.MessageSetDivinationResult
import com.klikli_dev.theurgy.network.TheurgyPackets
import com.klikli_dev.theurgy.registry.SoundRegistry
import net.minecraft.block.BlockState
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.common.Tags
import net.minecraftforge.registries.ForgeRegistries

class DivinationRodItem
    (private val itemTier: ItemTier, properties: Properties) : Item(properties) {

    override fun onUsingTick(stack: ItemStack?, entityLiving: LivingEntity, count: Int) {
        if (entityLiving.world.isRemote && entityLiving is PlayerEntity) {
            ScanManager.updateScan(entityLiving, false)
        }
    }

    override fun getUseAction(stack: ItemStack): UseAction {
        return UseAction.BOW;
    }

    override fun onItemUse(context: ItemUseContext): ActionResultType {
        val world: World = context.world
        val player: PlayerEntity = context.player!!
        val pos: BlockPos = context.pos
        val stack: ItemStack = context.item
        if (player.isSneaking) {
            val state: BlockState = world.getBlockState(pos)

            //only allow blocks that fit the harvest level
            if (this.itemTier.harvestLevel >= state.block.getHarvestLevel(state)) {
                //only allow ores
                if (Tags.Blocks.ORES.contains(state.block)) {
                    if (!world.isRemote) {
                        val translationKey = state.block.translationKey
                        stack.orCreateTag.putString(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID, state.block.registryName.toString())
                        stack.tag?.putFloat(TheurgyConstants.Nbt.DIVINATION_IS_LINKED, 1.0f)
                        player.sendMessage(
                            TranslationTextComponent(
                                "${this.translationKey}.message.linked_block",
                                TranslationTextComponent(translationKey)
                            ), Util.DUMMY_UUID
                        )
                    }
                    world.playSound(
                        player, player.position, SoundRegistry.tuningFork,
                        SoundCategory.PLAYERS, 1f, 1f
                    )
                }
                //not an ore
                else {
                    if (!world.isRemote) {
                        player.sendMessage(
                            TranslationTextComponent(
                                "${this.translationKey}.message.not_an_ore",
                                TranslationTextComponent(translationKey)
                            ), Util.DUMMY_UUID
                        )
                    }
                }
            } else {
                if (!world.isRemote) {
                    player.sendMessage(
                        TranslationTextComponent("${this.translationKey}.message.tier_too_low"), Util.DUMMY_UUID
                    )
                }
            }
            return ActionResultType.SUCCESS
        }
        return ActionResultType.PASS
    }

    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val stack: ItemStack = player.getHeldItem(hand)

        if (!player.isSneaking) {
            if (stack.orCreateTag.contains(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID)) {
                player.activeHand = hand
                world.playSound(
                    player,
                    player.position,
                    SoundRegistry.tuningFork,
                    SoundCategory.PLAYERS,
                    1f,
                    1f
                )
                if (world.isRemote) {
                    val id = ResourceLocation(stack.tag!!.getString(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID))
                    ScanManager.beginScan(player, ForgeRegistries.BLOCKS.getValue(id)!!)
                }
            } else if (!world.isRemote) {
                player.sendMessage(
                    TranslationTextComponent("$translationKey.message.no_linked_block"),
                    Util.DUMMY_UUID
                )
            }
        } else {
            //if we shift-right click, we reset linked block
            if (!world.isRemote) {
                stack.tag?.putFloat(TheurgyConstants.Nbt.DIVINATION_IS_LINKED, 0.0f)
                stack.tag?.remove(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID)
                stack.tag?.remove(TheurgyConstants.Nbt.DIVINATION_RESULT)
                player.sendMessage(
                    TranslationTextComponent("$translationKey.message.unlinked_block"), Util.DUMMY_UUID
                )
            }
        }

        return ActionResult(ActionResultType.SUCCESS, stack)
    }

    override fun onItemUseFinish(stack: ItemStack, world: World, entityLiving: LivingEntity): ItemStack {
        if (entityLiving !is PlayerEntity) return stack
        val player: PlayerEntity = entityLiving
        player.cooldownTracker.setCooldown(this, 40)

        if(!world.isRemote){
            stack.damageItem(1, entityLiving) {}
        }

        if (world.isRemote) {
            val result: BlockPos? = ScanManager.finishScan(player)
            if (result != null) {
                TheurgyPackets.sendToServer(MessageSetDivinationResult(result))
                //TODO: show particle here instead
                //Show debug visualization
                SelectedBlockRenderer.selectBlock(result, System.currentTimeMillis() + 10000)
            }
        }
        return stack
    }

    override fun getUseDuration(stack: ItemStack?): Int {
        return TheurgyServerConfig.divinationSettings.divinationTicks.get()
    }

    override fun onPlayerStoppedUsing(stack: ItemStack, world: World, entityLiving: LivingEntity, timeLeft: Int) {
        if (world.isRemote) {
            //player interrupted, so we cancel scan
            ScanManager.cancelScan()

            //if we have a result stored, we display it
            val result = stack.tag?.getLong(TheurgyConstants.Nbt.DIVINATION_RESULT)?.let { BlockPos.fromLong(it) };
            if(result != null){
                //TODO: show particle here instead
                //Show debug visualization
                SelectedBlockRenderer.selectBlock(result, System.currentTimeMillis() + 10000)
            }
        }


        super.onPlayerStoppedUsing(stack, world, entityLiving, timeLeft)
    }

    override fun addInformation(
        stack: ItemStack, worldIn: World?, tooltip: List<ITextComponent>,
        flagIn: ITooltipFlag
    ) {
        tooltip as MutableList<ITextComponent>
        if (!Screen.hasShiftDown()) {
            tooltip.add(TranslationTextComponent("${this.translationKey}.tooltip"));
            //if we are linked we show the linked object
            val linkedBlockId = stack.tag?.getString(TheurgyConstants.Nbt.DIVINATION_LINKED_BLOCK_ID)
            if (linkedBlockId != null) {
                val block = ForgeRegistries.BLOCKS.getValue(ResourceLocation(linkedBlockId))
                if (block != null) {
                    tooltip.add(
                        TranslationTextComponent(
                            "${this.translationKey}.tooltip.attuned",
                            TranslationTextComponent(block.translationKey)
                        )
                    );
                }
            } else {
                tooltip.add(TranslationTextComponent("${this.translationKey}.tooltip.not_attuned"));
            }
            tooltip.add(TooltipHandler.shiftForMoreInformation)
        } else {
            tooltip.add(TranslationTextComponent("${this.translationKey}.tooltip.shift"));
        }

        super.addInformation(stack, worldIn, tooltip, flagIn)
    }
}