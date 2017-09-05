package com.game.tobin.floatmenu;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.tobin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FloatIcon extends FrameLayout {
	private static final int FADE_OUT_TIME = 3000;
	private static final int ICON_ALPHE = 125;

	private FloatMenuManager mMenuManager;

	private AbsoluteLayout.LayoutParams mParams;
	private int mScreenWidth;
	private int mViewWidth;

	private boolean isLeft = true;

	private boolean isAlignBorder = true;

	private boolean isMenuShowing = false;

	private Point lastPoint;
	private TextView mNoteNum;
	private int mNoteCount = 0;
	private boolean isTouchable = true;
	private boolean isRedPoint;

	public boolean isTouchable() {
		return isTouchable;
	}
	public void setTouchable(boolean i) {
		isTouchable = i;
	}
	public boolean isLeft() {
		return this.isLeft;
	}
	public boolean isMenuShowing() {
		return this.isMenuShowing;
	}
	public void setMenuShowing(boolean isShowing) {
		this.isMenuShowing = isShowing;
	}

	public Point getLastPosition() {
		return this.lastPoint;
	}

	public void resetPosition() {
		mParams.x = lastPoint.x;
		mParams.y = lastPoint.y;
		checkParams();
		mMenuManager.setViewPosition(this, mParams.x, mParams.y);
		FloatMenuOriginSPUtil.setFloatMenuOrigin(getContext(), mParams.x, mParams.y);
	}

	private ImageView mIconImage;

	public FloatIcon(Activity activity, int menuHeight) {
		super(activity);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// 从3.0开始，安卓开始支持硬件加速.
			// 对于已经在系统设置项中开启硬件加速，但是硬件加速会给应用程序带来问题的情况，
			// 可以使用如下方法为应用程序View级别取消硬件加速
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		mViewWidth = menuHeight;
		mIconImage = new ImageView(activity);
		int p = DensityUtil.dip2px(activity, 2);

		// 悬浮按钮设置图片
		mIconImage.setImageResource(R.drawable.tobin_float_icon);

		mIconImage.setPadding(p, p, p, p);
		addView(mIconImage, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mNoteNum = new TextView(activity);
		mNoteNum.setBackgroundResource(R.drawable.tobin_float_num_bg);
		mNoteNum.setTextColor(Color.WHITE);
		mNoteNum.setGravity(Gravity.CENTER);
		mNoteNum.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
		mNoteNum.setPadding(0, 0, DensityUtil.dip2px(activity, 3), DensityUtil.dip2px(activity, 4));
		int numSize = DensityUtil.dip2px(activity, 20);
		LayoutParams params = new LayoutParams(numSize, numSize);
		params.rightMargin = -DensityUtil.dip2px(activity, 3);
		params.gravity = Gravity.TOP | Gravity.RIGHT;
		addView(mNoteNum, params);
		setNoteNumVisible(false);
		// 获取屏幕信息
		DisplayMetrics dm = activity.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mMenuManager = FloatMenuManager.getInstance(activity);
	}

	public void setRedPoint(boolean i) {
		isRedPoint = i;
	}

	public void setNoteNum(int num) {
		mNoteCount = num;
		if (mNoteNum != null) {
			if (num > 0) {
				mNoteNum.setText(num + "");
				if (!isMenuShowing) {
					resetViewAlpha();
					mNoteNum.setVisibility(View.VISIBLE);
					startViewAlpha();
				}
			} else if (isRedPoint) {
				mNoteNum.setText("");
				if (!isMenuShowing)
					mNoteNum.setVisibility(View.VISIBLE);
			} else {
				mNoteNum.setText("");
				mNoteNum.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void setNoteNumVisible(boolean visible) {
		if (mNoteNum != null) {
			mNoteNum.setVisibility(visible && (mNoteCount > 0 || isRedPoint) ? View.VISIBLE : View.INVISIBLE);
		}
	}

	private int getScreenHeight() {
		return FloatMenuManager.getInstance(null).getHeight();
	}

	public void checkParams() {
		if (mParams.x < 0) {
			mParams.x = 0;
		}
		if (mParams.x > mScreenWidth - mViewWidth) {
			mParams.x = mScreenWidth - mViewWidth;
		}
		if (getScreenHeight() > 0 && mParams.y > getScreenHeight() - mViewWidth) {
			mParams.y = getScreenHeight() - mViewWidth;
		}
		int i = DensityUtil.dip2px(getContext(), 24);
		if (mParams.x < i) {
			mParams.x = 0;
			isAlignBorder = true;
		} else if ((mScreenWidth - mViewWidth - mParams.x) < i) {
			mParams.x = mScreenWidth - mViewWidth;
			isAlignBorder = true;
		} else {
			isAlignBorder = false;
		}
		if (mParams.x < mScreenWidth / 2) {
			isLeft = true;
		} else {
			isLeft = false;
		}
	}

	public void setParams(AbsoluteLayout.LayoutParams params) {
		mParams = params;
		checkParams();
	}

	public AbsoluteLayout.LayoutParams getParams() {
		return mParams;
	}

	private int firstX, firstY, lastX, lastY;
	private int deltaX, deltaY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isTouchable) {
			return false;
		}
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastPoint = new Point(mParams.x, mParams.y);
			resetViewAlpha();
			firstX = x;
			firstY = y;
			lastX = firstX;
			lastY = firstY;
			deltaX = x - getLeft();
			deltaY = y - getTop();
			break;
		case MotionEvent.ACTION_MOVE:
			int dx = (int) event.getRawX() - lastX;
			int dy = (int) event.getRawY() - lastY;
			int ax = (int) event.getRawX() - firstX;
			int ay = (int) event.getRawY() - firstY;
			if (Math.abs(ax) < 10 && Math.abs(ay) < 10)
				break;
			isMoving = true;
			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();
			mParams.x = x - deltaX;
			mParams.y = y - deltaY;
			mMenuManager.setViewPosition(this, mParams.x, mParams.y);
			mMenuManager.updateIconView();
			mMenuManager.updateDeleteView();
			break;
		case MotionEvent.ACTION_UP:
			isMoving = false;
			checkParams();
			if (Math.abs(firstX - lastX) == 0 && Math.abs(firstY - lastY) == 0) {
				mMenuManager.updateMenuView();
				mMenuManager.hideDeleteView();
				JSONObject data = new JSONObject();
				try {
					data.put("id", 0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return true;
			} else {
				FloatMenuOriginSPUtil.setFloatMenuOrigin(getContext(), mParams.x, mParams.y);
				mParams.y = getTop();
				mMenuManager.setViewPosition(this, mParams.x, mParams.y);
				mMenuManager.updateIconView();
				if (!mMenuManager.hideDeleteView()) {
					startViewAlpha();
				}
			}
			break;
		}
		return true;
	}

	private boolean isMoving;

	private Runnable fadeOut = new Runnable() {
		@Override
		public void run() {
			if (!isMenuShowing && !isMoving) {
				alphaAnim();
			}
		}
	};

	private void setViewAlpha(int alpha) {
		if(Build.VERSION.SDK_INT >= 16){
			// 设置透明度
            mIconImage.setImageAlpha(alpha);
		}else if(Build.VERSION.SDK_INT < 16 && Build.VERSION.SDK_INT >= 14){
            mIconImage.setAlpha((float)alpha);
		}
		mNoteNum.getBackground().setAlpha(alpha);
		mNoteNum.setTextColor(Color.argb(alpha, 255, 255, 255));
	}

	private void alphaAnim() {
		AlphaAnimation a = new AlphaAnimation(1f, ICON_ALPHE / 255f);
		a.setDuration(300);
		a.setFillAfter(true);
		startAnimation(a);
	}

	public void removeAlphaCallback() {
		removeCallbacks(rotate);
		removeCallbacks(fadeOut);
	}

	public void resetViewAlpha() {
		removeAlphaCallback();
		clearAnimation();
		setViewAlpha(255);
	}

	public void startViewAlpha() {
		resetViewAlpha();
		if (isAlignBorder) {
			postDelayed(rotate, ROTATE_OUT_TIME);
		} else {
			postDelayed(fadeOut, FADE_OUT_TIME);
		}
	}

	private static final int ROTATE_OUT_TIME = 1500;

	private Runnable rotate = new Runnable() {
		@Override
		public void run() {
			if (!isMenuShowing && !isMoving)
				rotateAnim();
		}
	};

	private void rotateAnim() {
        // AnimationSet提供了一个把多个动画组合成一个组合的机制，并可设置组中动画的时序关系，如同时播放，顺序播放等
		AnimationSet as = new AnimationSet(true);
		float rotate = isLeft ? 45 : -45;
		/**
		 * 旋转变化动画类
		 * fromDegrees	为动画起始时的旋转角度
		 * toDegrees	为动画旋转到的角度
		 * pivotXType	为动画在X轴相对于物件位置类型，X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
		 * pivotXValue	为动画相对于物件的X坐标的开始位置
		 * pivotXType	为动画在Y轴相对于物件位置类型，Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
		 * pivotYValue	为动画相对于物件的Y坐标的开始位置
		 */
		RotateAnimation a = new RotateAnimation(0, rotate, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		// 设置时间持续时间
		a.setDuration(300);
		as.addAnimation(a);
		float tx = isLeft ? -0.5f : 0.5f;
		/**
		 * 位移动画
		 * fromXDelta	为动画起始时 X坐标上的移动位置
		 * toXDelta		为动画结束时 X坐标上的移动位置
		 * fromYDelta	为动画起始时Y坐标上的移动位置
		 * toYDelta		为动画结束时Y坐标上的移动位置
		 */
		TranslateAnimation a1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, tx,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
		a1.setDuration(300);
		/**
		 * 渐变动画（淡入淡出）
		 * 第一个参数fromAlpha为 动画开始时候透明度
		 * 第二个参数toAlpha为 动画结束时候透明度
		 * AlphaAnimation(float fromAlpha, float toAlpha)
		 */
		AlphaAnimation a2 = new AlphaAnimation(1f, ICON_ALPHE / 255f);
		a2.setStartOffset(ROTATE_OUT_TIME / 2);
		a2.setDuration(300);

		as.addAnimation(a2);
		as.addAnimation(a1);
		as.setFillAfter(true);

		startAnimation(as);
	}
}
