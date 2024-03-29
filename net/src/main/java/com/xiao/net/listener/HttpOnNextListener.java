package com.xiao.net.listener;

import com.xiao.net.exception.ApiException;

/**
 * 成功回调处理
 * Created by WZG on 2016/7/16.
 */
public interface HttpOnNextListener {
    /**
     * 成功后回调方法
     *
     * @param resulte
     * @param method
     */
    void onNext(String resulte, String method, Object flag, Object obj);

    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     *
     * @param e
     * @param method
     */
    void onError(ApiException e, String method, Object flag, Object obj);
}
