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

import com.klikli_dev.theurgy.Theurgy
import com.klikli_dev.theurgy.client.divination.ScanManager
import com.klikli_dev.theurgy.client.render.SelectedBlockRenderer
import com.klikli_dev.theurgy.registry.SoundRegistry
import net.minecraft.block.BlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemTier
import net.minecraft.item.ItemUseContext
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.registries.ForgeRegistries

class DivinationRodItem
    (private val itemTier: ItemTier, properties: Properties) : Item(properties) {

    override fun onUsingTick(stack: ItemStack?, entityLiving: LivingEntity, count: Int) {
        if (entityLiving.world.isRemote && entityLiving is PlayerEntity) {
            ScanManager.updateScan(entityLiving, false)
        }
    }

    override fun onItemUse(context: ItemUseContext): ActionResultType {
        val world: World = context.world
        val player: PlayerEntity = context.player!!
        val pos: BlockPos = context.pos
        val stack: ItemStack = context.item
        if (player.isSneaking) {
            val state: BlockState = world.getBlockState(pos)
            if (!state.block.isAir(state, world, pos)) {
                //TODO: handle block type and harvest level

                if (this.itemTier.harvestLevel >= state.block.getHarvestLevel(state)) {
                    if (!world.isRemote) {
                        val translationKey = state.block.translationKey
                        stack.orCreateTag.putString("linkedBlockId", state.block.registryName.toString())
                        stack.tag?.putFloat("linked", 1.0f)
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
                } else {
                    if (!world.isRemote) {
                        //TODO: Useful response - mention if ore tier is the issue or if invalid block

                        player.sendMessage(
                            TranslationTextComponent("$translationKey.message.no_link_found"), Util.DUMMY_UUID
                        )
                    }
                }
                return ActionResultType.SUCCESS
            } else {
                if(!world.isRemote){
                    //TODO: Unlink
                    stack.tag?.putFloat("linked", 0.0f)
                    player.sendMessage(
                        TranslationTextComponent("$translationKey.message.unlinked_block"), Util.DUMMY_UUID
                    )
                }
            }
        }
        return ActionResultType.PASS
    }

    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val stack: ItemStack = player.getHeldItem(hand)

        if (!player.isSneaking) {
            if (stack.orCreateTag.contains("linkedBlockId")) {
                stack.tag!!.putFloat("linked", 0.0f)
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
                    val id = ResourceLocation(stack.tag!!.getString("linkedBlockId"))
                    ScanManager.beginScan(player, ForgeRegistries.BLOCKS.getValue(id)!!)
                }
            } else if (!world.isRemote) {
                player.sendMessage(
                    TranslationTextComponent("$translationKey.message.no_linked_block"),
                    Util.DUMMY_UUID
                )
            }
        }

        return ActionResult(ActionResultType.SUCCESS, stack)
    }

    override fun onItemUseFinish(stack: ItemStack, world: World, entityLiving: LivingEntity): ItemStack {
        if (entityLiving !is PlayerEntity) return stack
        val player: PlayerEntity = entityLiving
        player.cooldownTracker.setCooldown(this, 40)

        if (world.isRemote) {
            //TODO: finish scan
             val result: BlockPos? = ScanManager.finishScan(player)
            if (result != null) {
                //TODO: show particle here instead
                //Show debug visualization
                SelectedBlockRenderer.selectBlock(result, System.currentTimeMillis() + 10000)
            }
        }
        return stack
    }

    override fun getUseDuration(stack: ItemStack?): Int {
        return ScanManager.SCAN_DURATION_TICKS
    }

    override fun onPlayerStoppedUsing(stack: ItemStack, world: World, entityLiving: LivingEntity?, timeLeft: Int) {
        //player interrupted, so we can safely set not found on server
       // stack.orCreateTag.putFloat("distance", NOT_FOUND)
        if (world.isRemote) {
            ScanManager.cancelScan()
        }
        super.onPlayerStoppedUsing(stack, world, entityLiving, timeLeft)
    }

    override fun addInformation(
        stack: ItemStack, worldIn: World?, tooltip: List<ITextComponent>,
        flagIn: ITooltipFlag
    ) {
        // TODO: Add tooltip
//        if (stack.orCreateTag.contains("linkedBlockId")) {
//            val id = ResourceLocation(stack.tag.getString("linkedBlockId"))
//            val block: Block = ForgeRegistries.BLOCKS.getValue(id)
//            val translationKey = if (block is IOtherworldBlock) (block as IOtherworldBlock).getUncoveredBlock()
//                .getTranslationKey() else block.translationKey
//            tooltip.add(
//                TranslationTextComponent(
//                    this.translationKey + ".tooltip.linked_block",
//                    TranslationTextComponent(translationKey)
//                        .mergeStyle(TextFormatting.BOLD, TextFormatting.ITALIC)
//                )
//            )
//        } else {
//            tooltip.add(TranslationTextComponent(translationKey + ".tooltip.no_linked_block"))
//        }
//        super.addInformation(stack, worldIn, tooltip, flagIn)
    }
}