package com.game.tobin.floatmenu;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tobin on 2017/8/16.
 */

public class FloatMenuOriginSPUtil {

    protected static void setFloatMenuOrigin(Context ctx, int x, int y) {
        SharedPreferences sp = ctx.getSharedPreferences("Tobin_Float_Menu_Origin", 0);
        sp.edit().putInt("FloatMenuOriginX", x).putInt("FloatMenuOriginY", y).commit();
    }

    protected static int[] getFloatMenuOrigin(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("Tobin_Float_Menu_Origin", 0);
        int x = sp.getInt("FloatMenuOriginX", -1);
        int y = sp.getInt("FloatMenuOriginY", -1);
        if (x < 0 || y < 0)
            return null;
        return new int[] { x, y };
    }

}
