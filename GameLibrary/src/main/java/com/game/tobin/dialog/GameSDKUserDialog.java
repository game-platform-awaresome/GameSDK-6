package com.game.tobin.dialog;

import android.app.Activity;

import com.game.tobin.sdk.R;
import com.game.tobin.util.DensityUtils;
import com.game.tobin.util.ScreenUtils;
import com.game.tobin.view.NormalLoginView;
import com.game.tobin.view.SdkSettingView;

import android.content.res.Configuration;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Tobin on 2017/7/20.
 */

public class GameSDKUserDialog {

    private CustomDialog dialog;
    private Activity activity;
    private int size;
    private ImageView btnLoginViewBack,btnLoginMenu;

    RelativeLayout containerView;

    /**
     * 一个自定义的 登录对话框
     */
    public GameSDKUserDialog(Activity activity) {
        this.activity = activity;
        this.size = ScreenUtils.getScreenWidth(activity);
    }

    public void showDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
        // 顶部 Title View
        RelativeLayout topView = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.game_tobin_sdk_view_base, null);
        // 内容 View
        containerView =(RelativeLayout) topView.findViewById(R.id.container_view);
        LinearLayout normalLoginView = NormalLoginView.createView(activity);
        containerView.addView(normalLoginView);

        // 返回按钮
        btnLoginViewBack = (ImageView) topView.findViewById(R.id.btnLoginViewBack);
        // 菜单按钮
        btnLoginMenu = (ImageView) topView.findViewById(R.id.btnLoginViewMenu);

        btnLoginViewBack.setOnClickListener(backOnclick);
        btnLoginMenu.setOnClickListener(menuOnclick);
        this.dialog = new CustomDialog(activity);

        dialog.setMyContentView(topView);
        // 判断Android当前的屏幕是横屏还是竖屏。横竖屏判断
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            dialog.setDialogSize((int) (size * 0.85), (ScreenUtils.getScreenWidth(activity)));
        } else {
            // 横屏
            dialog.setDialogSize(DensityUtils.dp2px(activity, 320), ScreenUtils.getScreenWidth(activity));
        }
        // 点击周围空白处弹出层不自动消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void dismiss() {
        if (null != dialog) {
            dialog.cancel();
        }
    }

    private OnClickListener menuOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialog != null) {
                UserDialogViewManager.addUserSdkView(containerView, SdkSettingView.createView(activity));
//                containerView.addView(SdkSettingView.createView(activity));
            }
        }
    };

    private OnClickListener backOnclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialog != null) {
                UserDialogViewManager.doViewBackPressed(containerView);
//              dialog.onBackPressed();
            }
        }
    };

}
