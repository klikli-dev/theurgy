// SPDX-FileCopyrightText: 2024 klikli-dev
//
// SPDX-License-Identifier: MIT

package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.content.item.mode.ItemMode;
import net.minecraft.world.item.ItemStack;

public abstract class MercurialWandItemMode extends ItemMode {
    protected Type type;

    protected MercurialWandItemMode() {
        super();
    }

    public static MercurialWandItemMode getMode(ItemStack stack) {
        var tag = stack.getOrCreateTag();
        var modeOrdinal = tag.getInt("theurgy:mode");
        return Type.values()[modeOrdinal].mode();
    }

    public static void setMode(ItemStack stack, MercurialWandItemMode mode) {
        var tag = stack.getOrCreateTag();
        tag.putInt("theurgy:mode", mode.type.ordinal());
    }

    public Type type() {
        return this.type;
    }

    @Override
    protected String typeName() {
        return this.type.name().toLowerCase();
    }


    public enum Type {
        ROTATE_DIRECTION(new RotateSelectedDirectionMode()),
        SWITCH_LOGISTICS_ENABLED(new SwitchLogisticsEnabledMode()),
        SELECT_DIRECTION(new SelectDirectionMode(), false),
        SET_SELECTED_DIRECTION(new SetSelectedDirectionMode(), false);

        final MercurialWandItemMode mode;
        final boolean enabled;

        Type(MercurialWandItemMode mode) {
            this(mode, true);
        }

        Type(MercurialWandItemMode mode, boolean enabled) {
            this.mode = mode;
            this.enabled = enabled;
            mode.type = this;
        }

        public MercurialWandItemMode mode() {
            return this.mode;
        }

        public Type next() {
            int next = this.ordinal() + 1;
            if (next >= Type.values().length) {
                next = 0;
            }
            var nextValue = Type.values()[next];
            if (!nextValue.enabled) {
                return nextValue.next();
            }
            return Type.values()[next];
        }

        public Type previous() {
            int previous = this.ordinal() - 1;
            if (previous < 0) {
                previous = Type.values().length - 1;
            }

            var previousValue = Type.values()[previous];
            if (!previousValue.enabled) {
                return previousValue.previous();
            }

            return Type.values()[previous];
        }

        public Type shift(int shift) {
            if (shift > 0) {
                return this.next();
            } else if (shift < 0) {
                return this.previous();
            }
            return this;
        }

    }
}
