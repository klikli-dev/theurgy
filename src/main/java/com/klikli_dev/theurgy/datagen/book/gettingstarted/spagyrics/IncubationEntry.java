
package com.klikli_dev.theurgy.datagen.book.gettingstarted.spagyrics;

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.EntryBackground;
import com.klikli_dev.modonomicon.api.datagen.EntryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.BookTextPageModel;
import com.klikli_dev.theurgy.Theurgy;
import com.klikli_dev.theurgy.registry.ItemRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;

public class IncubationEntry extends EntryProvider {

    public static final String ENTRY_ID = "incubation";

    public IncubationEntry(CategoryProvider parent) {
        super(parent);
    }

    @Override
    protected void generatePages() {
        this.page("intro", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Incubating Iron");
        this.pageText(
                """
                        The final step is to recombine the three principles into usable Iron Ingots.
                        """
        );

        this.page("step1", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Filling the Vessels");
        this.pageText(
                """
                        [#]($INPUT)Right-click[#]() the {0}, {1} and {2} with the Sulfur, Salt and Mercury you obtained earlier respectively to fill them.
                         """,
                this.itemLink(ItemRegistry.INCUBATOR_SULFUR_VESSEL.get()),
                this.itemLink(ItemRegistry.INCUBATOR_SALT_VESSEL.get()),
                this.itemLink(ItemRegistry.INCUBATOR_MERCURY_VESSEL.get())
        );

        this.page("step2", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Providing Heat");
        this.pageText(
                """
                        Now add fuel, such as Coal, to the {0} below the Incubator to heat it up.
                        """,
                this.itemLink(ItemRegistry.PYROMANTIC_BRAZIER.get())
        );

        this.page("step3", () -> BookTextPageModel.builder()
                .withTitle(this.context().pageTitle())
                .withText(this.context().pageText())
                .build());
        this.pageTitle("Obtaining the Incubated Item");
        this.pageText(
                """
                        After a while the input items will have been consumed and incubated into the result.
                        \\
                        \\
                        You can [#]($INPUT)right-click[#]() the {0} with an empty hand to obtain 3x {1}.
                        \\
                        \\
                        *Congratulations, you created 3 Ingots from 1 Raw Metal!*
                        """,
                this.itemLink(ItemRegistry.INCUBATOR.get()),
                this.itemLink(Items.IRON_INGOT)
        );
    }

    @Override
    protected String entryName() {
        return "Incubating Iron";
    }

    @Override
    protected String entryDescription() {
        return "Creating Iron Ingots from Sulfur, Salt and Mercury";
    }

    @Override
    protected Pair<Integer, Integer> entryBackground() {
        return EntryBackground.DEFAULT;
    }

    @Override
    protected BookIconModel entryIcon() {
        return BookIconModel.create(Theurgy.loc("textures/gui/book/ingot_x3.png"));
    }

    @Override
    protected String entryId() {
        return ENTRY_ID;
    }
}