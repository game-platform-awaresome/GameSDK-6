package com.game.tobin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * @author Tobin
 * @since 2015-11-11
 */
public class CustomDialog extends Dialog{

	/**
	 * current Context
	 */
	private Context mContext = null;
	
	/**
	 * Show view
	 */
	private View layout = null;
	
	/**
	 * width
	 */
	private int width = 0;
	
	/**
	 * height
	 */
	public int height = 0;
	
	/**
	 * current screen width
	 */
	private int screenWidth = 0;
	
	/**
	 * current screen height
	 */
	private int screenHeight = 0;
	
	/**
	 * off set on x 
	 */
	private int offSet_x = 0;
	
	/**
	 * off set on y 
	 */
	private int offSet_y = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout myLayout = new LinearLayout(mContext);
		myLayout.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams lpWW = new LinearLayout.LayoutParams(width - 6, LayoutParams.WRAP_CONTENT);//height - 6);
		lpWW.setMargins(0, 0, 0, 0);
		lpWW.gravity = Gravity.CENTER;

		myLayout.addView(layout, lpWW);
		setContentView(myLayout);
	}
	
	/**
	 * @param act
	 */
	public CustomDialog(Activity act) {
		super(act);
		mContext = act;
	}
	
	public CustomDialog(Activity act, int theme) {
		super(act,theme);
		mContext = act;
	}

	/**
	 * @param width
	 * @param height
	 */
	public void setDialogSize(int width, int height) {
		this.width = width;
		this.height = height;
		Window win = getWindow();
		WindowManager.LayoutParams wl = win.getAttributes();
		wl.width = width;
		wl.height = height;
		win.setAttributes(wl);

		// 官方废弃
//		final Display display = getWindow().getWindowManager().getDefaultDisplay();
//		screenWidth =  display.getWidth();
//		screenHeight = display.getHeight();

		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		
		offSet_x = (screenWidth - this.width) / 2;
		offSet_y = (screenHeight - this.height) / 2;

        Log.i(getClass().getName()," tobin offSet_x: " + offSet_x + "  offSet_y : " + offSet_y);
	}
	
	/**
	 * set dialog position
	 * @param x
	 * @param y
	 */
	public void setDialogPosition(int x, int y) {
		Window win = getWindow();
		WindowManager.LayoutParams wl = win.getAttributes();
		int x_t = wl.x + x;
		int y_t = wl.y + y;
		
		if(x_t < -offSet_x) {
			x_t = -offSet_x;
		}else if(x_t > offSet_x) {
			x_t = offSet_x;
		}
		
		if(y_t < -offSet_y) {
			y_t = -offSet_y;
		}else if(y_t > offSet_y) {
			y_t = offSet_y;
		}
		
		wl.x = x_t;
		wl.y = y_t;
		win.setAttributes(wl);
		Log.i(getClass().getName()," tobin: wl.x: " + x_t + " wl.y: " + y_t);
	}
	
	/**
	 * picture
	 * @param resID
	 */
	@SuppressWarnings("deprecation")
    public void setBackground(int resID) {
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resID);
		bitmap = bitmapResize(bitmap, width, height);
		setBackground(new BitmapDrawable(bitmap));
	}

	/**
	 * color
	 * @param resID
	 */
	public void setBackgroundColor(int resID) {
		Window win = this.getWindow();
		win.setBackgroundDrawableResource(resID);
	}
	
	/**
	 * @param drawable
	 */
	public void setBackground(Drawable drawable) {
		Window win = this.getWindow();
		win.setBackgroundDrawable(drawable);
	}
	
	/**
	 * 设置View
	 */
	public void setMyContentView(View v) {
		layout = v;
		if(layout != null) {
			layout.setOnTouchListener(viewTouchListener);
		}
	}
	
	/**
	 * 改变背景图片的大小
	 * @param b
	 * @param w
	 * @param h
	 * @return
	 */
	private Bitmap bitmapResize(Bitmap b, int w, int h) {
		if (b == null) {
			return null;
		}
		
		int oldW = b.getWidth();
		int oldH = b.getHeight();
		
		if (oldW == w && oldH == h) {
			return b;
		}
		//
		float scaleWidth = ((float) w) / oldW;
		float scaleHeight = ((float) h) / oldH;
		// 
		Matrix matrix = new Matrix();
		// 
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resized = Bitmap.createBitmap(b, 0, 0, oldW, oldH, matrix, true);
		
		return resized;
	}

	private int x_old = 0;
	private int y_old = 0;
	private int x_new = 0;
	private int y_new = 0;
	private int x_gap = 0;
	private int y_gap = 0;
	
	private boolean isTouchTitle = false;
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				x_old = (int) event.getRawX();
				y_old = (int) event.getRawY();
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				x_new = (int) event.getRawX();
				y_new = (int) event.getRawY();
				
				x_gap = x_new - x_old;
				y_gap = y_new - y_old;
				
				if(isTouchTitle) {
					if(Math.abs(x_gap) > 5 || Math.abs(y_gap) > 5) {
						setDialogPosition((x_gap), (y_gap));
						x_old = x_new;
						y_old = y_new;
					}
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				x_new = (int) event.getRawX();
				y_new = (int) event.getRawY();
				
				x_gap = x_new - x_old;
				y_gap = y_new - y_old;
				
				if(isTouchTitle) {
					setDialogPosition((x_gap), (y_gap));
				}
				
				isTouchTitle = false;
				break;
			}
		}
	    return super.onTouchEvent(event);
    }
	
	/**
	 * current view touch listener
	 */
	public OnTouchListener viewTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					int y = (int) event.getY();
					if(y < 50) {
//						isTouchTitle = true;
					}
					break;
				}
				case MotionEvent.ACTION_MOVE: {
					break;
				}
				case MotionEvent.ACTION_UP: {
					break;
				}
			}
	        return false;
        }
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}

