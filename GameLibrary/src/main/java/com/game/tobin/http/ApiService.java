package com.game.tobin.http;

import android.content.Context;

import com.game.tobin.bean.DataConfig;
import com.game.tobin.common.GameEncrypt;
import com.game.tobin.util.LogUtil;

import org.json.JSONObject;

import java.util.TreeMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Tobin on 2017/8/31.
 */

public interface ApiService {
    // jobb -d /temp/assets/ -o my-app-assets.obb -k secret-key -pn com.my.app.package -pv 11
    @FormUrlEncoded
    @POST("/api/getUrl")
    Call<DataConfig> getDataConfig(@Field("client_id") String count,
                                   @Field("package") String package_name,
                                   @Field("platform") String platform,
                                   @Field("time") String time,
                                   @Field("flag") String flag);
    @FormUrlEncoded
    @POST("/api/getUrl")
    Call<ResponseBody> getDataConfig2(@Field("client_id") String count,
                                      @Field("package") String package_name,
                                      @Field("platform") String platform,
                                      @Field("time") String time,
                                      @Field("flag") String flag);




}
