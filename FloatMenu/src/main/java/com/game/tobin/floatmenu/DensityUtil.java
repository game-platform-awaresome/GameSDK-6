package com.game.tobin.floatmenu;

import android.content.Context;

/**
 * dp转换px工具类
 * 
 * @author Tobin
 *
 */
public class DensityUtil {
	public static int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
