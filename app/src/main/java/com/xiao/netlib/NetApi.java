package com.xiao.netlib;

import com.xiao.net.Api.HttpManagerApi;
import com.xiao.net.listener.HttpOnNextListener;

/**
 * Created by xy on 2019/11/25.
 */
public class NetApi extends HttpManagerApi {

    //release
    private static final String YUN_PORT = "6006";
    private static final String YUN_IP = "https://www.onecm.com.cn";

    //test
//    private static final String YUN_PORT = "6022";
//    private static final String YUN_IP = "https://www.onecm.com.cn";

    public NetApi(HttpOnNextListener onNextListener) {
        super(onNextListener);
        NetApiStackManager.getInstance().addNetApi(this);
        setCache(true);
        setIp(null);
        setPort(null);
    }

    private void setHostPort() {
        setPort(":5004");
    }

    private void setYUNBaseUrl() {
        setBaseUrl(YUN_IP + ":" + YUN_PORT);
    }

    public void getWeather(Object flag, String info, String method) {
        setCache(false);
        setFlag(flag);
        setMethod(method);
        setBaseUrl("http://112.74.102.119:5007");
        //or
//        setBaseUrl("http://");
//        setIp("112.74.102.119");
//        setPort(":5007");
        HttpService httpService = getRetrofit().create(HttpService.class);
        doHttpDeal(httpService.getWeather(info));
    }
}
