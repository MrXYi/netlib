package com.xiao.netlib;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * http接口
 * Created by xy on 2019/11/25.
 */
public interface HttpService {

    //天气
    @POST("weather")
    Observable<String> getWeather(@Body String info);

}
