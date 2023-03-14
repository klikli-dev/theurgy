/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 *
 * SPDX-License-Identifier: MIT
 */

package com.klikli_dev.theurgy.datagen.multiblock;

import com.klikli_dev.modonomicon.api.datagen.MultiblockProvider;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.BlockRegistry;
import net.minecraft.data.PackOutput;

public class TheurgyMultiblockProvider extends MultiblockProvider {

    public TheurgyMultiblockProvider(PackOutput packOutput) {
        super(packOutput, Theurgy.MODID);
    }

    @Override
    public void buildMultiblocks() {
        this.add(this.modLoc("placement/calcination_oven"),
                new DenseMultiblockBuilder()
                        .layer("0")
                        .layer("b")
                        .block('b', BlockRegistry.PYROMANTIC_BRAZIER)
                        .block('0', BlockRegistry.CALCINATION_OVEN)
                        .build()
        );
    }
}
