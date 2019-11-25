package com.onecm.net;

import android.app.Application;
import android.content.Context;

import com.onecm.net.http.TokenInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * 全局app
 * Created by WZG on 2016/12/12.
 */

public class RxRetrofitApp {

    private static Application application;
    private static boolean debug;
    public static OkHttpClient.Builder builder;
    public static OkHttpClient okHttpClient;

    public static void init(Application app) {
        setApplication(app);
        setDebug(true);
        initOkhttp(app);
    }

    public static void init(Application app, boolean debug) {
        setApplication(app);
        setDebug(debug);
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        RxRetrofitApp.application = application;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        RxRetrofitApp.debug = debug;
    }

    public static void initOkhttp(Context context) {
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(6, TimeUnit.SECONDS);
        builder.readTimeout(6, TimeUnit.SECONDS);
        builder.writeTimeout(6, TimeUnit.SECONDS);
        builder.addInterceptor(new TokenInterceptor(context));
//        builder.retryOnConnectionFailure(false);
        builder.connectionPool(new ConnectionPool(5, 500, TimeUnit.MILLISECONDS));
        okHttpClient = builder.build();
    }
}
