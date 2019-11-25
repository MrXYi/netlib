package com.xiao.net.http.func;

import com.xiao.net.exception.HttpTimeException;

import rx.functions.Func1;

/**
 * 服务器返回数据判断
 * Created by WZG on 2017/3/23.
 */

public class ResulteFunc implements Func1<Object,Object>{
    @Override
    public Object call(Object o) {
        if (o == null || "".equals(o.toString())) {
            throw new HttpTimeException("数据错误");
        }
        return o;
    }
}
