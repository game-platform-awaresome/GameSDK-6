package com.game.tobin.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Tobin on 2017/8/5.
 */

public class webviewDialog extends CustomDialog {
    private Context mContext;

    public webviewDialog(Activity act){
        super(act);
    }

    public webviewDialog(Activity act, int theme) {
        super(act,theme);
        mContext = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
