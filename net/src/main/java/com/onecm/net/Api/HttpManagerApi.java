package com.onecm.net.Api;

import com.onecm.net.http.HttpManager;
import com.onecm.net.listener.HttpOnNextListener;
import com.onecm.net.listener.HttpOnNextSubListener;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * 请求数据统一封装类
 * Created by WZG on 2016/7/16.
 */
public class HttpManagerApi extends BaseApi {
    private HttpManager manager;

    public HttpManagerApi(HttpOnNextListener onNextListener) {
        manager = new HttpManager(onNextListener);
    }


    public HttpManagerApi(HttpOnNextSubListener onNextSubListener) {
        manager = new HttpManager(onNextSubListener);
    }

    protected Retrofit getRetrofit() {
        return  manager.getReTrofit(getConnectionTime(), getBaseUrl());
    }


    protected void doHttpDeal(Observable observable) {
            manager.httpDeal(observable, this);
    }

    protected void doHttpDealThread(Observable observable) {
        manager.httpDealThread(observable, this);
    }

    public void cancel(){
        manager.cancel();
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return null;
    }
}
