package com.klikli_dev.theurgy.content.item.mercurialwand.mode;

import com.klikli_dev.theurgy.content.item.mode.ItemMode;
import net.minecraft.world.item.ItemStack;

public abstract class MercurialWandItemMode extends ItemMode {
    protected Type type;

    protected MercurialWandItemMode(){
        super();
    }

    public Type type(){
        return this.type;
    }

    @Override
    protected String typeName() {
        return this.type.name().toLowerCase();
    }

    public static MercurialWandItemMode getMode(ItemStack stack){
        var tag = stack.getOrCreateTag();
        var modeOrdinal = tag.getInt("theurgy:mode");
        return Type.values()[modeOrdinal].mode();
    }


    public static void setMode(ItemStack stack, MercurialWandItemMode mode){
        var tag = stack.getOrCreateTag();
        tag.putInt("theurgy:mode", mode.type.ordinal());
    }


    public enum Type{

        SELECT_DIRECTION(new SelectDirectionMode()),
        SET_SELECTED_DIRECTION(new SetSelectedDirectionMode());

        final MercurialWandItemMode mode;

        Type(MercurialWandItemMode mode){
            this.mode = mode;
            mode.type = this;
        }

        public MercurialWandItemMode mode(){
            return this.mode;
        }

        public Type next(){
            int next = this.ordinal() + 1;
            if(next >= Type.values().length){
                next = 0;
            }
            return Type.values()[next];
        }

        public Type previous(){
            int previous = this.ordinal() - 1;
            if(previous < 0){
                previous = Type.values().length - 1;
            }
            return Type.values()[previous];
        }

        public Type shift(int shift){
            if(shift > 0){
                return this.next();
            }
            else if(shift < 0){
                return this.previous();
            }
            return this;
        }

    }
}
