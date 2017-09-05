package com.game.tobin.dialog;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tobin on 2017/7/24.
 */

public class UserDialogViewManager {

    public static void addUserSdkView(final ViewGroup parentView, final View subView) {
        if (parentView.getChildCount() > 0) {
            final View oldView = parentView.getChildAt(parentView.getChildCount() - 1);
            if (oldView.getClass().getName().equals(subView.getClass().getName())) {
                return;
            }
            parentView.addView(subView);
            oldView.setVisibility(View.GONE);
        }
    }

    public static boolean doViewBackPressed(final ViewGroup parent) {
        if (parent == null) {
            return false;
        } else if (parent.getChildCount() > 1) {
            final View firstView = parent.getChildAt(parent.getChildCount() - 1);
            final View secondView = parent.getChildAt(parent.getChildCount() - 2);
            secondView.setVisibility(View.VISIBLE);
            firstView.setVisibility(View.GONE);
            parent.removeView(firstView);
            return true;
        }
        return false;
    }

}
