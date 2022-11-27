/*
 * SPDX-FileCopyrightText: 2022 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.TierSortingRegistry;

public class ServerConfig {

    private static final ServerConfig instance = new ServerConfig();

    public final DivinationRods divinationRods;
    public final ForgeConfigSpec spec;

    private ServerConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        this.divinationRods = new DivinationRods(builder);
        this.spec = builder.build();
    }

    public static ServerConfig get() {
        return instance;
    }

    public static class DivinationRods {

        public final DivinationRodTier tier1;

        public DivinationRods(ForgeConfigSpec.Builder builder) {
            builder.comment("Divination Rod Settings").push("divination_rods");

            this.tier1 =
                    new DivinationRodTier(builder, "tier1", TierSortingRegistry.getName(Tiers.STONE), 10, 40, 96);


            builder.pop();
        }

        public static class DivinationRodTier {

            public final ForgeConfigSpec.ConfigValue<String> miningTier;

            public final ForgeConfigSpec.ConfigValue<Integer> durability;
            public final ForgeConfigSpec.ConfigValue<Integer> scanDurationTicks;
            public final ForgeConfigSpec.ConfigValue<Integer> scanRange;

            public DivinationRodTier(ForgeConfigSpec.Builder builder, String name, ResourceLocation miningTier, int durability, int scanDurationTicks, int scanRange) {
                builder.comment("Divination Rod Tier Settings").push(name);

                this.miningTier =
                        builder.comment("The amount of time it takes the spirit to perform one mining operation.")
                                .define("miningTier", miningTier.toString());
                this.durability =
                        builder.comment("The durability of the divination rod.")
                                .define("durability", durability);
                this.scanDurationTicks =
                        builder.comment("How long a scan should take (in ticks). Generally this should not be changed, as a low value can cause client-side lag for the player.")
                                .define("scanDurationTicks", scanDurationTicks);
                this.scanRange =
                        builder.comment("The block detection range of this divination rod in blocks.")
                                .define("scanRange", scanRange);
                builder.pop();
            }

            public Tier getMiningTier() {
                return TierSortingRegistry.byName(new ResourceLocation(this.miningTier.get()));
            }
        }
    }
}
