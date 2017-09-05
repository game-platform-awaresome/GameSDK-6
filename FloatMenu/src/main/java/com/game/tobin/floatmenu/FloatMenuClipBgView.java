package com.game.tobin.floatmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FloatMenuClipBgView extends LinearLayout {

	public FloatMenuClipBgView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FloatMenuClipBgView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FloatMenuClipBgView(Context context) {
		super(context);
	}

	private Path clipPath;

	private Path makePath(int width, int height, int radius) {
		if (clipPath == null)
			clipPath = new Path();
		clipPath.reset();
		clipPath.addRoundRect(new RectF(0.0F, 0.0F, width, height), radius, radius, Path.Direction.CW);
		return clipPath;
	}

	public void setClipPath(Path clipPath) {
		this.clipPath = clipPath;
	}

	public Path getClipPath() {
		return clipPath;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.clipPath(makePath(getWidth(), getHeight(), getHeight() / 2 + 1));
		super.dispatchDraw(canvas);
	}
}
