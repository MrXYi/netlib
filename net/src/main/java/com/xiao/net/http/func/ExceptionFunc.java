package com.xiao.net.http.func;

import android.util.Log;

import com.xiao.net.exception.FactoryException;

import rx.Observable;
import rx.functions.Func1;

/**
 * 异常处理
 * Created by WZG on 2017/3/23.
 */

public class ExceptionFunc implements Func1<Throwable, Observable> {
    @Override
    public Observable call(Throwable throwable) {
        Log.e("Tag","-------->"+throwable.getMessage());
        return Observable.error(FactoryException.analysisExcetpion(throwable));
    }
}
