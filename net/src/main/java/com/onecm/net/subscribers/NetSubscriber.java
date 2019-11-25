package com.onecm.net.subscribers;


import com.onecm.net.Api.BaseApi;
import com.onecm.net.RxRetrofitApp;
import com.onecm.net.exception.ApiException;
import com.onecm.net.exception.CodeException;
import com.onecm.net.exception.HttpTimeException;
import com.onecm.net.http.cookie.CookieResulte;
import com.onecm.net.listener.HttpOnNextListener;
import com.onecm.net.utils.AppUtil;
import com.onecm.net.utils.CookieDbUtil;

import java.lang.ref.SoftReference;

import rx.Observable;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.
 */
public class NetSubscriber<T> extends Subscriber<T> {
    //    回调接口
    private SoftReference<HttpOnNextListener> mSubscriberOnNextListener;
    /*请求数据*/
    private BaseApi api;


    /**
     * 构造
     *
     * @param api
     */
    public NetSubscriber(BaseApi api, SoftReference<HttpOnNextListener> listenerSoftReference) {
        this.api = api;
        this.mSubscriberOnNextListener = listenerSoftReference;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        /*缓存并且有网*/
        if (api.isCache() && AppUtil.isNetworkAvailable(RxRetrofitApp.getApplication())) {
             /*获取缓存数据*/
            CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrl());
            if (cookieResulte != null) {
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < api.getCookieNetWorkTime()) {
                    if (mSubscriberOnNextListener.get() != null) {
                        mSubscriberOnNextListener.get().onNext(cookieResulte.getResulte(), api.getMethod(), api.getFlag(), api.getObj());
                    }
                    onCompleted();
                    unsubscribe();
                }
            }
        }
    }

    @Override
    public void onCompleted() {

    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        /*需要緩存并且本地有缓存才返回*/
        if (api.isCache()) {
            getCache();
        } else {
            errorDo(e);
        }
    }

    /**
     * 获取cache数据
     */
    private void getCache() {
        Observable.just(api.getUrl()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                errorDo(e);
            }

            @Override
            public void onNext(String s) {
                           /*获取缓存数据*/
                CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(s);
                if (cookieResulte == null) {
                    throw new HttpTimeException(HttpTimeException.NO_CHACHE_ERROR);
                }
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < api.getCookieNoNetWorkTime()) {
                    if (mSubscriberOnNextListener.get() != null) {
                        mSubscriberOnNextListener.get().onNext(cookieResulte.getResulte(), api.getMethod(), api.getFlag(), api.getObj());
                    }
                } else {
                    CookieDbUtil.getInstance().deleteCookie(cookieResulte);
                    throw new HttpTimeException(HttpTimeException.CHACHE_TIMEOUT_ERROR);
                }
            }
        });
    }


    /**
     * 错误统一处理
     *
     * @param e
     */
    private void errorDo(Throwable e) {
        HttpOnNextListener httpOnNextListener = mSubscriberOnNextListener.get();
        if (httpOnNextListener == null) return;
        if (e instanceof ApiException) {
            httpOnNextListener.onError((ApiException) e, api.getMethod(), api.getFlag(), api.getObj());
        } else if (e instanceof HttpTimeException) {
            HttpTimeException exception = (HttpTimeException) e;
            httpOnNextListener.onError(new ApiException(exception, CodeException.RUNTIME_ERROR, exception.getMessage()), api.getMethod(), api.getFlag(), api.getObj());
        } else {
            httpOnNextListener.onError(new ApiException(e, CodeException.UNKNOWN_ERROR, e.getMessage()), api.getMethod(), api.getFlag(), api.getObj());
        }
    }


    /**
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
         /*缓存处理*/
        if (api.isCache()) {
            CookieResulte resulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrl());
            long time = System.currentTimeMillis();
            /*保存和更新本地数据*/
            if (resulte == null) {
                resulte = new CookieResulte(api.getUrl(), t.toString(), time);
                CookieDbUtil.getInstance().saveCookie(resulte);
            } else {
                resulte.setResulte(t.toString());
                resulte.setTime(time);
                CookieDbUtil.getInstance().updateCookie(resulte);
            }
        }
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext((String) t, api.getMethod(), api.getFlag(), api.getObj());
        }
    }


    /**
     * 取消对observable的订阅，同时也取消了http请求
     */
    public void onCancel() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}