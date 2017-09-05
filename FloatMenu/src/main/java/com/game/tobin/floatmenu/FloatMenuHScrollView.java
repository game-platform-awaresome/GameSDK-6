package com.game.tobin.floatmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.OverScroller;

import java.lang.reflect.Field;

public class FloatMenuHScrollView extends HorizontalScrollView {

	private OverScroller mScroller;
	private boolean isInTouch;
	public FloatMenuHScrollView(Context context) {
		super(context);
		setScroller(context);
	}

	public FloatMenuHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setScroller(context);
	}

	public FloatMenuHScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setScroller(context);
	}

	private void setScroller(Context context) {
		mScroller = new OverScroller(context);
		try {
			Field field = getClass().getDeclaredField("mScroller");
			field.setAccessible(true);
			field.set(this, mScroller);
		}catch (Exception e) {
			e.printStackTrace();
			Log.e("setScroller", e.getMessage());
		}
	}

	private void checkScrollStop() {
		boolean mIsBeingDragged = false;
		try {
			Field field = getClass().getDeclaredField("mIsBeingDragged");
			field.setAccessible(true);
			mIsBeingDragged = field.getBoolean(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mScroller.isFinished() && !mIsBeingDragged && !isInTouch) {
			if (listener != null && mScroller.isFinished()) {
				listener.onFinish();
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean result = super.onTouchEvent(ev);
		final int actionMasked = ev.getActionMasked();
		switch (actionMasked) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				isInTouch = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				isInTouch = false;
				checkScrollStop();
				break;
			default:
				break;
		}
		return result;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		checkScrollStop();
	}

	private ScrollFinishListener listener;

	public void setScrollFinishListener(ScrollFinishListener listener) {
		this.listener = listener;
	}

	public interface ScrollFinishListener {
		void onFinish();
	}
}
