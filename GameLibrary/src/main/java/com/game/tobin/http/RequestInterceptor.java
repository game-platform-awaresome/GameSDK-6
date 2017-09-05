package com.game.tobin.http;

import com.game.tobin.common.Configs;
import com.game.tobin.sdk.GameSDK;
import com.game.tobin.util.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Tobin on 2017/9/1.
 * 请求拦截器，修改请求header
 *
 */
public class RequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
//                .addHeader("Content-Type", "text/html; charset=UTF-8")
//                .addHeader("Accept-Encoding", "*")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Accept", "*/*")
//                .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
//                .addHeader("Vary", "Accept-Encoding")
//                .addHeader("Access-Control-Allow-Origin", "*") // 跨域
//                .addHeader("Cookie", "add cookies here")
                .addHeader( "games-model", "Mi-4c")
                .addHeader( "games-imei", "869634020762395")
                .addHeader( "games-gid", "")
                .addHeader( "games-mac", "10:2a:b3:d1:a0:8c")
                .addHeader( "games-androidId", "737ed702963c56c5")
                .addHeader( "games-appVersionCode", "123")
                .addHeader( "games-appVersionName", "1.2.3")
                .addHeader( "games-sysVersionCode", "22")
                .addHeader( "games-sysVersionName", "5.1.1")
                .addHeader( "games-screenSize", "1920x1080")
                .addHeader( "games-language", "zh_CN")
                .addHeader( "games-netType", "WIFI")
                .addHeader( "games-campaign", "other")
                .addHeader( "games-packageName", GameSDK.getInstance().getActivity().getPackageName())
                .addHeader( "games-phoneNumber", "")
                .addHeader( "games-countryCode", "cn")
                .addHeader( "games-customerId", "548560867f0da776f4248043b8216904")
                .addHeader( "games-phoneMNC", "46001")
                .addHeader( "games-platform", "android")
                .addHeader( "games-releasePlatform", "sgmala")
                .addHeader( "games-client-id", Configs.clientId)
                .addHeader( "games-user-id", "0")
                .build();

        LogUtil.v("request:" + request.toString());
        LogUtil.v("request headers:" + request.headers().toString());

        return chain.proceed(request);
    }
}