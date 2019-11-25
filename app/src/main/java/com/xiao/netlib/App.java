package com.xiao.netlib;

import android.app.Application;

import com.xiao.net.RxRetrofitApp;

/**
 * Created by xy on 2019/11/25.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RxRetrofitApp.init(this);
    }

}
