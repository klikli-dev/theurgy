package com.klikli_dev.theurgy.datagen.model;

import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.content.apparatus.reformationarray.render.ReformationTargetPedestalRenderer;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

import java.util.Optional;

public class TheurgySpriteSourceProvider extends SpriteSourceProvider {
    public TheurgySpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, Theurgy.MODID);
    }

    @Override
    protected void addSources() {
        this.atlas(BLOCKS_ATLAS)
                .addSource(new SingleFile(ReformationTargetPedestalRenderer.GLOW.texture(), Optional.empty()));
    }
}
