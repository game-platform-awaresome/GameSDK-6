package com.game.tobin.floatmenu;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsoluteLayout;

public class FloatMenuManager {
    private static final String MENU_TAG = "FloatMenu";
    private static FloatMenuManager instance;
    private Activity mActivity;
    private int mScreenWidth;
    private int mScreenHeight;

    private FloatIcon mFloatIcon;
    private AbsoluteLayout.LayoutParams mIconParams;

    private DeleteView mDeleteView;
    private View deleteBgView;
    private AbsoluteLayout.LayoutParams mDeleteParams;

    private FloatMenu mFloatMenu;
    private AbsoluteLayout.LayoutParams mMenuParams;
    private int mMenuHeight;
    private AbsoluteLayout mContainer;

    private boolean isFloatHideEnable = true;

    private FloatMenuManager(Activity activity) {
        this.mActivity = activity;
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        mContainer = new AbsoluteLayout(mActivity);
        mContainer.setTag(MENU_TAG);
        mContainer.setBackgroundColor(Color.argb(0, 0, 0, 0)); // 透明
    }

    public static FloatMenuManager getInstance(Activity activity) {
        if (instance == null) {
            synchronized (FloatMenuManager.class) {
                if (instance == null) {
                    instance = new FloatMenuManager(activity);
                }
            }
            if (activity != instance.mActivity) {
                instance.dismissMenu();
                instance.mActivity = activity;
            }
        }
        return instance;
    }

    public int getHeight() {
        if (mContainer != null) {
            int height = mContainer.getMeasuredHeight();
            return height == 0 ? mScreenHeight : height;
        }
        return mScreenHeight;
    }

    public void initParentView(ViewGroup parent) {
        ViewGroup decor = parent;
        if (decor == null) {
            decor = (ViewGroup) mActivity.getWindow().getDecorView();
        }
        if (mContainer.getParent() != null) {
            if (mContainer.getParent() == parent) {
                return;
            }
            try {
                removeView(mContainer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        decor.addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        if (mFloatIcon != null && !mFloatIcon.isMenuShowing()) {
            mFloatIcon.startViewAlpha();
        }
        updateMenuStatus();
        popupMenu();
        updateMenuView();
    }

    private void createIconView() {
        if (mFloatIcon != null)
            return;
        mFloatIcon = new FloatIcon(mActivity, mMenuHeight);
        int[] originCache = FloatMenuOriginSPUtil.getFloatMenuOrigin(mActivity);
        int x, y;
        if (originCache == null) {
            int[] origin = {0, 30};
            if (origin != null) {
                if (origin[0] != 0 && origin[0] != 100)
                    origin[0] = 0;
                if (origin[1] < 0 || origin[1] > 100)
                    origin[1] = 0;
            } else {
                origin = new int[]{0, 0};
            }
            x = (mScreenWidth - mMenuHeight) * origin[0] / 100;
            y = (getHeight() - mMenuHeight) * origin[1] / 100;
        } else {
            x = originCache[0];
            y = originCache[1];
        }
        mIconParams = new AbsoluteLayout.LayoutParams(mMenuHeight, mMenuHeight, x, y);
        mFloatIcon.setParams(mIconParams);
        mFloatIcon.checkParams();
        mIconParams = mFloatIcon.getParams();
        mContainer.addView(mFloatIcon, mIconParams);
        setViewPosition(mFloatIcon, mIconParams.x, mIconParams.y);
        // 刷新View
        mFloatIcon.invalidate();

        mFloatIcon.startViewAlpha();
    }

    private void removeView(View v) {
        ViewGroup parent = ((ViewGroup) v.getParent());
        if (parent != null)
            parent.removeView(v);
    }

    public void setViewPosition(View v, int x, int y) {
        if (x < 0)
            x = 0;
        if (x > mScreenWidth - v.getMeasuredWidth())
            x = mScreenWidth - v.getMeasuredWidth();
        if (y < 0)
            y = 0;
        if (y > getHeight() - v.getMeasuredHeight())
            y = getHeight() - v.getMeasuredHeight();
        v.layout(x, y, x + v.getMeasuredWidth(), y + v.getMeasuredHeight());
        v.invalidate();
    }

    private void removeIconView() {
        if (mFloatIcon != null) {
            removeView(mFloatIcon);
            mFloatIcon = null;
        }
    }

    private void createMenuView() {
        if (mFloatMenu != null)
            return;
        mFloatMenu = new FloatMenu(mActivity);
        mFloatMenu.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mMenuHeight = mFloatMenu.getMeasuredHeight();
        if (mMenuParams == null) {
            mMenuParams = new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
        }
        mContainer.addView(mFloatMenu, mMenuParams);
        mFloatMenu.setVisibility(View.VISIBLE);
    }

    private void removeMenuView() {
        if (mFloatMenu != null) {
            removeView(mFloatMenu);
            mFloatMenu = null;
        }
    }

    public void setServiceNoteNum(int num) {
        if (mFloatMenu != null)
            mFloatMenu.setServiceNum(num);
    }

    public void createDeleteView() {
        if (!isFloatHideEnable)
            return;
        if (mDeleteView != null)
            return;
        mDeleteView = new DeleteView(mActivity);
        int width = DensityUtil.dip2px(mActivity, 39);
        int height = DensityUtil.dip2px(mActivity, 39);
        if (mDeleteParams == null) {
            mDeleteParams = new AbsoluteLayout.LayoutParams(width, height, (mScreenWidth - width) / 2,
                    getHeight() - height - (DensityUtil.dip2px(mActivity, 35)));
        }
        mContainer.addView(mDeleteView, mDeleteParams);
        mDeleteView.setVisibility(View.INVISIBLE);

        deleteBgView = new View(mActivity);
        GradientDrawable bgDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0x66000000, 0xBB000000, 0xF0000000});
        deleteBgView.setBackgroundDrawable(bgDrawable);
        AbsoluteLayout.LayoutParams deleteBgParams = new AbsoluteLayout.LayoutParams(mScreenWidth,
                DensityUtil.dip2px(mActivity, 35), 0, getHeight() - DensityUtil.dip2px(mActivity, 35));

        mContainer.addView(deleteBgView, deleteBgParams);
        deleteBgView.setVisibility(View.VISIBLE);
    }

    private void removeDeleteView() {
        if (mDeleteView != null) {
            removeView(mDeleteView);
            removeView(deleteBgView);
            mDeleteView = null;
            deleteBgView = null;
        }
    }

    public void updateIconView() {
        if (mFloatIcon != null) {
            mFloatIcon.setNoteNumVisible(true);
            mFloatIcon.setMenuShowing(false);
            mFloatIcon.invalidate();
            if (tempX >= 0) {
                mIconParams.x = tempX;
                tempX = -1;
                setViewPosition(mFloatIcon, mIconParams.x, mIconParams.y);
            }
        }
        if (mFloatMenu != null) {
            mFloatMenu.setVisibility(View.INVISIBLE);
        }
        if (mContainer != null)
            mContainer.invalidate();
    }

    public void updateDeleteView() {
        if (isFloatHideEnable && mDeleteView != null) {
            mDeleteView.setVisibility(View.VISIBLE);
            deleteBgView.setVisibility(View.VISIBLE);
            mDeleteView.updateDeleteStatus(isReadyToDelete());
        }
    }

    public void hideMenuView() {
        mFloatMenu.setItemVisible(View.VISIBLE);
        mFloatMenu.setVisibility(View.INVISIBLE);
        mFloatIcon.setNoteNumVisible(true);
        mFloatIcon.setMenuShowing(false);
        mFloatIcon.startViewAlpha();
    }

    private int tempX = -1;

    public void updateMenuStatus() {
        if (mFloatIcon != null && mFloatMenu != null) {
            mFloatMenu.updateMenuIcon(99);
        }
    }

    private Handler handler = new Handler();
    private Runnable loginTodayRunnable = new Runnable() {
        @Override
        public void run() {
            updateMenuView();
        }
    };

//    public void updateMenuViewLoginToday() {
//        if (mFloatMenu == null)
//            return;
//        mFloatIcon.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateMenuView();
//            }
//        }, 500);
//
//        if (loginTodayRunnable != null) {
//            handler.postDelayed(loginTodayRunnable, 5000);
//        }
//    }

    public void updateMenuView() {
        if (mFloatMenu == null)
            return;
        if (mFloatMenu.getVisibility() == View.VISIBLE) {
            mFloatMenu.hideAnim(mFloatIcon.isLeft());
            hideMenuView();
            if (tempX >= 0) {
                mIconParams.x = tempX;
                tempX = -1;
                setViewPosition(mFloatIcon, mIconParams.x, mIconParams.y);
            }
        } else {
            mFloatMenu.updateMenuIcon(View.GONE);
            mFloatMenu.setVisibility(View.VISIBLE);
            mFloatIcon.setNoteNumVisible(false);
            if (mFloatIcon.isLeft()) {
                if (mIconParams.x + mFloatIcon.getMeasuredWidth() + mFloatMenu.getMenuContentWidth() > mScreenWidth) {
                    tempX = mIconParams.x;
                    mIconParams.x = mScreenWidth - mFloatIcon.getMeasuredWidth() - mFloatMenu.getMenuContentWidth();
                }
                mMenuParams.x = mIconParams.x + mFloatIcon.getMeasuredWidth();
            } else {
                if (mIconParams.x - mFloatMenu.getMenuContentWidth() < 0) {
                    tempX = mIconParams.x;
                    mIconParams.x = mFloatMenu.getMenuContentWidth();
                }
                mMenuParams.x = mIconParams.x - mFloatMenu.getMenuContentWidth();
            }
            mMenuParams.y = mIconParams.y;
            mFloatMenu.setVisibility(View.VISIBLE);
            mFloatIcon.setMenuShowing(true);
            setViewPosition(mFloatIcon, mIconParams.x, mIconParams.y);
            setViewPosition(mFloatMenu, mMenuParams.x, mMenuParams.y);
            mFloatMenu.showAnim(mFloatIcon.isLeft());
        }
    }

    public boolean hideDeleteView() {
        if (!isFloatHideEnable || mDeleteView == null)
            return false;
        mDeleteView.setVisibility(View.INVISIBLE);
        deleteBgView.setVisibility(View.INVISIBLE);
        if (mDeleteView.isDelete()) {
            mFloatIcon.resetPosition();
            mFloatIcon.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    private boolean isReadyToDelete() {
        if ((mIconParams.x + mIconParams.width) < mDeleteParams.x)
            return false;
        if ((mIconParams.x) > (mDeleteParams.x + mDeleteParams.width))
            return false;
        if ((mIconParams.y + mIconParams.height) < mDeleteParams.y)
            return false;
        return true;
    }

    public void popupMenu() {
        createMenuView();
        createDeleteView();
        createIconView();
    }

    public void dismissMenu() {
        removeIconView();
        removeMenuView();
        removeDeleteView();
    }

}
