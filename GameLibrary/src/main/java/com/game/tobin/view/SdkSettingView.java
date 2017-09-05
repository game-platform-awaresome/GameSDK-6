package com.game.tobin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.game.tobin.sdk.GameSDK;
import com.game.tobin.sdk.R;


public class SdkSettingView extends LinearLayout {

	public SdkSettingView() {
		super(GameSDK.getInstance().getActivity());
	}

	public SdkSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SdkSettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SdkSettingView(Context context) {
		super(context);
	}

	public void initView() {

	}

	public static SdkSettingView createView(Context ctx) {
		if (ctx == null)
			return null;
		SdkSettingView view = (SdkSettingView) LayoutInflater.from(ctx).inflate(R.layout.game_tobin_sdk_view_setting, null);
		view.initView();
		return view;
	}

}
