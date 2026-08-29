package com.yuuhamu.routerupgradecore.internal;

import me.desht.modularrouters.util.BeamData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;

public final class BeamDataAccess {

    private static final Field DEST_FIELD;
    private static final Field DURATION_FIELD;
    private static final Field REVERSED_FIELD;

    static {
        try {
            DEST_FIELD = BeamData.class.getDeclaredField("dest");
            DEST_FIELD.setAccessible(true);
            DURATION_FIELD = BeamData.class.getDeclaredField("duration");
            DURATION_FIELD.setAccessible(true);
            REVERSED_FIELD = BeamData.class.getDeclaredField("reversed");
            REVERSED_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("ModularRoutersのBeamDataのフィールド構成が変更された可能性があります(本家APIが変更された可能性があります)", e);
        }
    }

    private BeamDataAccess() {
    }

    public static BeamData withColor(BeamData original, int color) {
        try {
            BlockPos dest = (BlockPos) DEST_FIELD.get(original);
            int duration = DURATION_FIELD.getInt(original);
            boolean reversed = REVERSED_FIELD.getBoolean(original);
            ItemStack stack = original.getStack();
            BeamData copy = stack.isEmpty()
                    ? new BeamData(duration, dest, color)
                    : new BeamData(duration, dest, stack, color);
            if (original.isItemFade()) {
                copy.fadeItems();
            }
            if (reversed) {
                copy.reverseItems();
            }
            return copy;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("BeamDataのフィールド読み取りに失敗しました", e);
        }
    }
}
