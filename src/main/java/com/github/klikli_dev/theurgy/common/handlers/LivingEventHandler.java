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

package com.github.klikli_dev.theurgy.common.handlers;

import com.github.klikli_dev.theurgy.Theurgy;
import com.github.klikli_dev.theurgy.registry.ItemRegistry;
import com.github.klikli_dev.theurgy.registry.TagRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Theurgy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEventHandler {
    //region Static Methods
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity source = (LivingEntity) event.getSource().getTrueSource();
            //apply increased damage to endermen for the end crystal swords
            if (TagRegistry.END_CRYSTAL_SWORDS.contains(source.getHeldItem(Hand.MAIN_HAND).getItem()) &&
                TagRegistry.ENDERMEN.contains(event.getEntity().getType())) {

                event.setAmount(event.getAmount() * 2);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity source = (LivingEntity) event.getSource().getTrueSource();
            ItemStack handHeld = source.getHeldItem(Hand.MAIN_HAND);

            //if an empty end crystal sword kills an enderman, convert it to end infused
            if (handHeld.getItem() == ItemRegistry.EMPTY_END_CRYSTAL_SWORD.get() &&
                TagRegistry.ENDERMEN.contains(event.getEntity().getType())) {

                ItemStack newHandHeld = new ItemStack(ItemRegistry.END_INFUSED_CRYSTAL_SWORD.get());
                newHandHeld.setTag(handHeld.getTag());
                newHandHeld.setDamage(handHeld.getDamage());
                source.setHeldItem(Hand.MAIN_HAND, newHandHeld);
            }
        }
    }
    //endregion Static Methods
}
