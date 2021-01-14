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

package com.github.klikli_dev.theurgy.common.config;

import com.github.klikli_dev.theurgy.common.config.value.CachedInt;
import net.minecraftforge.common.ForgeConfigSpec;

public class TheurgyConfig extends ConfigBase {

    //region Fields
    public final ForgeConfigSpec spec;
    public final CrystalSettings crystalSettings;
    public final EssentiaSettings essentiaSettings;
    public final AetherSettings aetherSettings;
    //endregion Fields

    //region Initialization
    public TheurgyConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        this.crystalSettings = new CrystalSettings(this, builder);
        this.essentiaSettings = new EssentiaSettings(this, builder);
        this.aetherSettings = new AetherSettings(this, builder);

        this.spec = builder.build();
    }

    //endregion Initialization

    public class CrystalSettings extends ConfigCategoryBase {
        //region Fields
        public final CachedInt primaMateriaSpreadEssentia;
        public final CachedInt pureCrystalChanceToSpread;
        public final CachedInt aerCrystalChanceToSpread;
        public final CachedInt aquaCrystalChanceToSpread;
        public final CachedInt ignisCrystalChanceToSpread;
        public final CachedInt terraCrystalChanceToSpread;
        public final CachedInt primaMateriaCrystalChanceToSpread;
        //endregion Fields

        //region Initialization
        public CrystalSettings(IConfigCache parent, ForgeConfigSpec.Builder builder) {
            super(parent, builder);
            builder.comment("Crystal Settings").push("crystals");

            this.primaMateriaSpreadEssentia = CachedInt.cache(this,
                    builder.comment(
                            "The amount of dissolved essentia required in a chunk for a pure crystal to create a prima materia crystal.")
                            .define("primaMateriaSpreadEssentia", 1000));

            this.pureCrystalChanceToSpread = CachedInt.cache(this,
                    builder.comment(
                            "The chance for a crystal to spread to a neighbor block on a random tick. " +
                            "Calculated as 1 in x, therefore a higher number here means a lower chance of spread.")
                            .define("pureCrystalChanceToSpread", 100));

            this.aerCrystalChanceToSpread = CachedInt.cache(this,
                    builder.comment(
                            "The chance for a crystal to spread to a neighbor block on a random tick. " +
                            "Calculated as 1 in x, therefore a higher number here means a lower chance of spread.")
                            .define("aerCrystalChanceToSpread", 100));

            this.aquaCrystalChanceToSpread = CachedInt.cache(this,
                    builder.comment(
                            "The chance for a crystal to spread to a neighbor block on a random tick. " +
                            "Calculated as 1 in x, therefore a higher number here means a lower chance of spread.")
                            .define("aquaCrystalChanceToSpread", 100));

            this.ignisCrystalChanceToSpread = CachedInt.cache(this,
                    builder.comment(
                            "The chance for a crystal to spread to a neighbor block on a random tick. " +
                            "Calculated as 1 in x, therefore a higher number here means a lower chance of spread.")
                            .define("ignisCrystalChanceToSpread", 100));

            this.terraCrystalChanceToSpread = CachedInt.cache(this,
                    builder.comment(
                            "The chance for a crystal to spread to a neighbor block on a random tick. " +
                            "Calculated as 1 in x, therefore a higher number here means a lower chance of spread.")
                            .define("terraCrystalChanceToSpread", 100));

            this.primaMateriaCrystalChanceToSpread = CachedInt.cache(this,
                    builder.comment(
                            "The chance for a crystal to spread to a neighbor block on a random tick. " +
                            "Calculated as 1 in x, therefore a higher number here means a lower chance of spread.")
                            .define("primaMateriaCrystalChanceToSpread", 10));

            builder.pop();
        }
        //endregion Initialization
    }

    public class EssentiaSettings extends ConfigCategoryBase {
        //region Fields
        public final CachedInt crucibleEssentiaToDiffuse;
        public final CachedInt crucibleDiffuseTicks;
        //endregion Fields

        //region Initialization
        public EssentiaSettings(IConfigCache parent, ForgeConfigSpec.Builder builder) {
            super(parent, builder);
            builder.comment("Essentia Settings").push("essentia");

            this.crucibleEssentiaToDiffuse = CachedInt.cache(this,
                    builder.comment(
                            "The amount of essentia to diffuse from cauldrons after crucibleDiffuseTicks have elapsed.")
                            .define("cruciblcrucibleEssentiaToDiffuseAmountToDissolve", 10));

            this.crucibleDiffuseTicks = CachedInt.cache(this,
                    builder.comment(
                            "The amount of ticks between diffusing essentia from crucibles.")
                            .define("crucibleDiffuseTicks", 400));

            builder.pop();
        }
        //endregion Initialization
    }

    public class AetherSettings extends ConfigCategoryBase {
        //region Fields
        public final CachedInt tier1GenerationRate;
        public final CachedInt tier1GenerationAmount;
        //endregion Fields

        //region Initialization
        public AetherSettings(IConfigCache parent, ForgeConfigSpec.Builder builder) {
            super(parent, builder);
            builder.comment("Aether Settings").push("aether");

            this.tier1GenerationRate = CachedInt.cache(this,
                    builder.comment(
                            "The ticks between two aether generation events.")
                            .define("tier1GenerationRate", 20 * 60));

            this.tier1GenerationAmount = CachedInt.cache(this,
                    builder.comment(
                            "The amount of aether to generate on each generation event.")
                            .define("tier1GenerationAmount", 500));

            builder.pop();
        }
        //endregion Initialization
    }
}
