// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.datagen;

import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeItem;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurItem;
import com.klikli_dev.theurgy.content.item.derivative.AlchemicalDerivativeTier;
import com.klikli_dev.theurgy.content.item.sulfur.AlchemicalSulfurType;
import com.klikli_dev.theurgy.registry.SulfurRegistry;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;

public class SulfurMappings {
    private static Set<AlchemicalDerivativeItem> NO_AUTOMATIC_RECIPES_FOR;

    private static List<AlchemicalSulfurItem> EARTHEN_MATTERS_ABUNDANT;
    private static List<AlchemicalSulfurItem> EARTHEN_MATTERS_COMMON;

    private static List<AlchemicalSulfurItem> GEMS_ABUNDANT;
    private static List<AlchemicalSulfurItem> GEMS_COMMON;
    private static List<AlchemicalSulfurItem> GEMS_RARE;
    private static List<AlchemicalSulfurItem> GEMS_PRECIOUS;

    private static List<AlchemicalSulfurItem> METALS_ABUNDANT;
    private static List<AlchemicalSulfurItem> METALS_COMMON;
    private static List<AlchemicalSulfurItem> METALS_RARE;
    private static List<AlchemicalSulfurItem> METALS_PRECIOUS;

    private static List<AlchemicalSulfurItem> OTHER_MINERALS_ABUNDANT;
    private static List<AlchemicalSulfurItem> OTHER_MINERALS_COMMON;
    private static List<AlchemicalSulfurItem> OTHER_MINERALS_RARE;
    private static List<AlchemicalSulfurItem> OTHER_MINERALS_PRECIOUS;

    private static List<AlchemicalSulfurItem> LOGS_ABUNDANT;
    private static List<AlchemicalSulfurItem> CROPS_ABUNDANT;

    private static List<AlchemicalSulfurItem> ANIMALS_ABUNDANT;
    private static List<AlchemicalSulfurItem> ANIMALS_COMMON;
    private static List<AlchemicalSulfurItem> ANIMALS_RARE;

    private static List<AlchemicalSulfurItem> MOBS_ABUNDANT;
    private static List<AlchemicalSulfurItem> MOBS_COMMON;
    private static List<AlchemicalSulfurItem> MOBS_RARE;
    private static List<AlchemicalSulfurItem> MOBS_PRECIOUS;

    public static List<AlchemicalSulfurItem> earthenMattersAbundant() {
        if (EARTHEN_MATTERS_ABUNDANT != null) {
            return EARTHEN_MATTERS_ABUNDANT;
        }

        EARTHEN_MATTERS_ABUNDANT = find(AlchemicalSulfurType.EARTHEN_MATTERS, AlchemicalDerivativeTier.ABUNDANT);
        return EARTHEN_MATTERS_ABUNDANT;
    }

    public static List<AlchemicalSulfurItem> earthenMattersCommon() {
        if (EARTHEN_MATTERS_COMMON != null) {
            return EARTHEN_MATTERS_COMMON;
        }

        EARTHEN_MATTERS_COMMON = find(AlchemicalSulfurType.EARTHEN_MATTERS, AlchemicalDerivativeTier.COMMON);

        return EARTHEN_MATTERS_COMMON;
    }

    public static List<AlchemicalSulfurItem> gemsAbundant() {
        if (GEMS_ABUNDANT != null) {
            return GEMS_ABUNDANT;
        }

        GEMS_ABUNDANT = find(AlchemicalSulfurType.GEMS, AlchemicalDerivativeTier.ABUNDANT);

        return GEMS_ABUNDANT;
    }

    public static List<AlchemicalSulfurItem> gemsCommon() {
        if (GEMS_COMMON != null) {
            return GEMS_COMMON;
        }

        GEMS_COMMON = find(AlchemicalSulfurType.GEMS, AlchemicalDerivativeTier.COMMON);

        return GEMS_COMMON;
    }

    public static List<AlchemicalSulfurItem> gemsRare() {
        if (GEMS_RARE != null) {
            return GEMS_RARE;
        }

        GEMS_RARE = find(AlchemicalSulfurType.GEMS, AlchemicalDerivativeTier.RARE);

        return GEMS_RARE;
    }

    public static List<AlchemicalSulfurItem> gemsPrecious() {
        if (GEMS_PRECIOUS != null) {
            return GEMS_PRECIOUS;
        }

        GEMS_PRECIOUS = find(AlchemicalSulfurType.GEMS, AlchemicalDerivativeTier.PRECIOUS);

        return GEMS_PRECIOUS;
    }

    public static List<AlchemicalSulfurItem> metalsAbundant() {
        if (METALS_ABUNDANT != null) {
            return METALS_ABUNDANT;
        }

        METALS_ABUNDANT = find(AlchemicalSulfurType.METALS, AlchemicalDerivativeTier.ABUNDANT);
        return METALS_ABUNDANT;
    }

    public static List<AlchemicalSulfurItem> metalsCommon() {
        if (METALS_COMMON != null) {
            return METALS_COMMON;
        }

        METALS_COMMON = find(AlchemicalSulfurType.METALS, AlchemicalDerivativeTier.COMMON);

        return METALS_COMMON;
    }

    public static List<AlchemicalSulfurItem> metalsRare() {
        if (METALS_RARE != null) {
            return METALS_RARE;
        }

        METALS_RARE = find(AlchemicalSulfurType.METALS, AlchemicalDerivativeTier.RARE);

        return METALS_RARE;
    }

    public static List<AlchemicalSulfurItem> metalsPrecious() {
        if (METALS_PRECIOUS != null) {
            return METALS_PRECIOUS;
        }

        METALS_PRECIOUS = find(AlchemicalSulfurType.METALS, AlchemicalDerivativeTier.PRECIOUS);

        return METALS_PRECIOUS;
    }

    public static List<AlchemicalSulfurItem> otherMineralsAbundant() {
        if (OTHER_MINERALS_ABUNDANT != null) {
            return OTHER_MINERALS_ABUNDANT;
        }

        OTHER_MINERALS_ABUNDANT = find(AlchemicalSulfurType.OTHER_MINERALS, AlchemicalDerivativeTier.ABUNDANT);

        return OTHER_MINERALS_ABUNDANT;
    }

    public static List<AlchemicalSulfurItem> otherMineralsCommon() {
        if (OTHER_MINERALS_COMMON != null) {
            return OTHER_MINERALS_COMMON;
        }

        OTHER_MINERALS_COMMON = find(AlchemicalSulfurType.OTHER_MINERALS, AlchemicalDerivativeTier.COMMON);

        return OTHER_MINERALS_COMMON;
    }

    public static List<AlchemicalSulfurItem> otherMineralsRare() {
        if (OTHER_MINERALS_RARE != null) {
            return OTHER_MINERALS_RARE;
        }

        OTHER_MINERALS_RARE = find(AlchemicalSulfurType.OTHER_MINERALS, AlchemicalDerivativeTier.RARE);

        return OTHER_MINERALS_RARE;
    }

    public static List<AlchemicalSulfurItem> otherMineralsPrecious() {
        if (OTHER_MINERALS_PRECIOUS != null) {
            return OTHER_MINERALS_PRECIOUS;
        }

        OTHER_MINERALS_PRECIOUS = find(AlchemicalSulfurType.OTHER_MINERALS, AlchemicalDerivativeTier.PRECIOUS);

        return OTHER_MINERALS_PRECIOUS;
    }

    public static List<AlchemicalSulfurItem> logsAbundant() {
        if (LOGS_ABUNDANT != null) {
            return LOGS_ABUNDANT;
        }

        LOGS_ABUNDANT = find(AlchemicalSulfurType.LOGS, AlchemicalDerivativeTier.ABUNDANT);

        return LOGS_ABUNDANT;
    }

    public static List<AlchemicalSulfurItem> cropsAbundant() {
        if (CROPS_ABUNDANT != null) {
            return CROPS_ABUNDANT;
        }

        CROPS_ABUNDANT = find(AlchemicalSulfurType.CROPS, AlchemicalDerivativeTier.ABUNDANT);

        return CROPS_ABUNDANT;
    }

    public static List<AlchemicalSulfurItem> animalsAbundant() {
        if (ANIMALS_ABUNDANT != null) {
            return ANIMALS_ABUNDANT;
        }

        ANIMALS_ABUNDANT = find(AlchemicalSulfurType.ANIMALS, AlchemicalDerivativeTier.ABUNDANT);

        return ANIMALS_ABUNDANT;
    }

    public static List<AlchemicalSulfurItem> animalsCommon() {
        if (ANIMALS_COMMON != null) {
            return ANIMALS_COMMON;
        }

        ANIMALS_COMMON = find(AlchemicalSulfurType.ANIMALS, AlchemicalDerivativeTier.COMMON);

        return ANIMALS_COMMON;
    }

    public static List<AlchemicalSulfurItem> animalsRare() {
        if (ANIMALS_RARE != null) {
            return ANIMALS_RARE;
        }

        ANIMALS_RARE = find(AlchemicalSulfurType.ANIMALS, AlchemicalDerivativeTier.RARE);

        return ANIMALS_RARE;
    }

    public static List<AlchemicalSulfurItem> mobsAbundant() {
        if (MOBS_ABUNDANT != null) {
            return MOBS_ABUNDANT;
        }

        MOBS_ABUNDANT = find(AlchemicalSulfurType.MOBS, AlchemicalDerivativeTier.ABUNDANT);

        return MOBS_ABUNDANT;
    }

    public static List<AlchemicalSulfurItem> mobsCommon() {
        if (MOBS_COMMON != null) {
            return MOBS_COMMON;
        }

        MOBS_COMMON = find(AlchemicalSulfurType.MOBS, AlchemicalDerivativeTier.COMMON);

        return MOBS_COMMON;
    }

    public static List<AlchemicalSulfurItem> mobsRare() {
        if (MOBS_RARE != null) {
            return MOBS_RARE;
        }

        MOBS_RARE = find(AlchemicalSulfurType.MOBS, AlchemicalDerivativeTier.RARE);

        return MOBS_RARE;
    }

    public static List<AlchemicalSulfurItem> mobsPrecious() {
        if (MOBS_PRECIOUS != null) {
            return MOBS_PRECIOUS;
        }

        MOBS_PRECIOUS = find(AlchemicalSulfurType.MOBS, AlchemicalDerivativeTier.PRECIOUS);

        return MOBS_PRECIOUS;
    }

    public static Set<AlchemicalDerivativeItem> noAutomaticRecipesFor(){
        if (NO_AUTOMATIC_RECIPES_FOR != null) {
            return NO_AUTOMATIC_RECIPES_FOR;
        }

        NO_AUTOMATIC_RECIPES_FOR = Set.of(
                //Metals
                SulfurRegistry.ALLTHEMODIUM.get(),
                SulfurRegistry.UNOBTAINIUM.get(),
                SulfurRegistry.VIBRANIUM.get(),

                //Drops
                SulfurRegistry.SKELETON_SKULL.get(),
                SulfurRegistry.WITHER_SKELETON_SKULL.get(),
                SulfurRegistry.GHAST_TEAR.get(),
                SulfurRegistry.SHULKER_SHELL.get(),
                SulfurRegistry.NETHER_STAR.get(),
                SulfurRegistry.ELYTRA.get(),
                SulfurRegistry.DRAGON_EGG.get(),
                SulfurRegistry.HEART_OF_THE_SEA.get()
        );

        return NO_AUTOMATIC_RECIPES_FOR;
    }

    private static List<AlchemicalSulfurItem> find(AlchemicalSulfurType type, AlchemicalDerivativeTier tier) {
        return SulfurRegistry.SULFURS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(entry -> entry instanceof AlchemicalSulfurItem)
                .map(entry -> (AlchemicalSulfurItem) entry)
                .filter(entry -> entry.type() == type)
                .filter(entry -> entry.tier == tier)
                .toList();
    }
}
