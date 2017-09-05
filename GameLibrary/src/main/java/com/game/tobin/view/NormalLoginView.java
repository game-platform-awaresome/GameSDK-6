package com.game.tobin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.game.tobin.bean.DataConfig;
import com.game.tobin.http.ApiService;
import com.game.tobin.http.RetrofitClient;
import com.game.tobin.sdk.GameSDK;
import com.game.tobin.sdk.R;
import com.game.tobin.util.LogUtil;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NormalLoginView extends LinearLayout {

    public NormalLoginView() {
        super(GameSDK.getInstance().getActivity());
    }

    public NormalLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NormalLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalLoginView(Context context) {
        super(context);
    }

    private EditText accountEdit;
    private EditText passwdEdit;
    private ImageView facebookLogin;
    private TextView btnLogin;

    public void initView() {
        accountEdit = (EditText) findViewById(R.id.account_edit);
        passwdEdit = (EditText) findViewById(R.id.password_edit);
        facebookLogin = (ImageView) findViewById(R.id.btnLoginViewThirdLogin1);
        facebookLogin.setOnClickListener(facebookLoginListener);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(loginOnClickListener);

    }


    OnClickListener loginOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            Retrofit retrofit = RetrofitClient.getInstance().getLoginRetrofit();
//            Call<DataConfig> call =  retrofit.create(ApiService.class)
//                    .getDataConfig("1023","com.gamater.sample","android","1478775180","bbaf424a93936d6fea571aab0ee3e88e");
//            call.enqueue(new Callback<DataConfig>() {
//                @Override
//                public void onResponse(Call<DataConfig> call, Response<DataConfig> response) {
//
//                }
//
//                @Override
//                public void onFailure(Call<DataConfig> call, Throwable t) {
//
//                }
//            });


        }
    };


    private OnClickListener facebookLoginListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LogUtil.i("FaceBook Login Button is onClick");
            LoginManager.getInstance().registerCallback(GameSDK.getInstance().getFacebookCallbackManager(),
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            LogUtil.d("FaceBook Login Success");
                            LogUtil.d("FaceBook Login AccessToken: " + loginResult.getAccessToken().getToken());
                            LogUtil.d("FaceBook Login UserId: " + loginResult.getAccessToken().getUserId());
                        }

                        @Override
                        public void onCancel() {
                            LogUtil.d("FaceBook Login Cancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            LogUtil.d("FaceBook Login Cancel: " + exception.getMessage());
                        }
                    });
            LoginManager.getInstance()
                    .setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK)
                    .logInWithReadPermissions(GameSDK.getInstance().getActivity(),
                            Arrays.asList("public_profile", "user_friends", "email"));
        }
    };

    public static NormalLoginView createView(Context ctx) {
        if (ctx == null)
            return null;
        NormalLoginView view = (NormalLoginView) LayoutInflater.from(ctx)
                .inflate(R.layout.game_tobin_sdk_view_login, null);

        view.initView();
        return view;
    }

}
