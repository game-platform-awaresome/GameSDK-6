package com.game.tobin.sdk;

import android.app.Activity;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.game.tobin.bean.DataConfig;
import com.game.tobin.common.Configs;
import com.game.tobin.common.GameEncrypt;
import com.game.tobin.dialog.GameSDKUserDialog;
import com.game.tobin.floatmenu.FloatMenuManager;
import com.game.tobin.http.ApiService;
import com.game.tobin.http.RetrofitClient;
import com.game.tobin.util.LogUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Tobin on 2017/7/20.
 */

public class GameSDK {
    private Activity activity;

    private static GameSDK ourInstance;

    CallbackManager callbackManager;

    public Activity getActivity() {
        return activity;
    }

    public CallbackManager getFacebookCallbackManager() {
        return callbackManager;
    }



    public void initSDK(Activity activity, String clientId, boolean isShowLog){
        this.activity = activity;
        if (!isShowLog){
            LogUtil.mDebuggable = 0;
        }
        Configs.clientId = clientId;

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(activity.getApplication());
//        AppEventsLogger.activateApp(activity);

        callbackManager = CallbackManager.Factory.create();

        FloatMenuManager.getInstance(activity).initParentView(null);
        LogUtil.v("clientId: " + clientId +" //nisShowLog: " + isShowLog);

        initDataConfig();
    }




    public static GameSDK getInstance() {
        if( ourInstance == null){
            synchronized (GameSDK.class){
                if( ourInstance == null){
                    ourInstance = new GameSDK();
                }
            }
        }
        return ourInstance;
    }

    public void GameLogin(){
        GameSDKUserDialog gameSDKLoginDialog = new GameSDKUserDialog(activity);
        gameSDKLoginDialog.showDialog();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void initDataConfig(){
//        Retrofit retrofit = RetrofitClient.getInstance().getBaseConfigRetrofit();
//        Call<DataConfig> call =  retrofit.create(ApiService.class)
//                .getDataConfig("1023","com.gamater.sample","android","1478775180","bbaf424a93936d6fea571aab0ee3e88e");
//        call.enqueue(new Callback<DataConfig>() {
//            @Override
//            public void onResponse(Call<DataConfig> call, Response<DataConfig> response) {
//                LogUtil.e(response.body().getStatus() + "ddd " + response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<DataConfig> call, Throwable t) {
//                LogUtil.e(t.getMessage());
//            }
//        });

        String[] params = new String[3];
        params[0] = activity.getPackageName();
        params[1] = "android";
        String time = System.currentTimeMillis() / 1000 + "";
        params[2] = time;

        String flag = GameEncrypt.encrypt(activity, params);
        Retrofit retrofit = RetrofitClient.getInstance().getBaseConfigRetrofit();
        Call<ResponseBody> call =  retrofit.create(ApiService.class)
                .getDataConfig2(Configs.clientId, activity.getPackageName(),params[1], time, flag);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    LogUtil.e(response.body().string());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtil.e("getDataConfig2 Failure: " + t.getMessage());
            }
        });
    }



}
