package com.xiao.netlib;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.xiao.net.exception.ApiException;
import com.xiao.net.listener.HttpOnNextListener;

public class MainActivity extends Activity implements HttpOnNextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new NetApi(this).getWeather(Flag.NET_GET_WEATHER, getWeather(), null);
    }

    @Override
    public void onNext(String resulte, String method, Object flag, Object obj) {
        Log.i("test_resulte", "resulte:" + resulte);
    }

    @Override
    public void onError(ApiException e, String method, Object flag, Object obj) {
        Log.i("test_resulte", e.getDisplayMessage());
    }

    public String getWeather() {
        String info = "";
        try {
            info = "{\n" +
                    "\t\"StateCode\": -1000,\n" +
                    "\t\"Latitude\": 0,\n" +
                    "\t\"Longitude\": 0,\n" +
                    "\t\"Radius\": -1\n" +
                    "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

}
