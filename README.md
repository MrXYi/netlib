# 网络库
> 引用本库：
>
> 
```
（1）在工程的build.gradle中添加maven { url 'https://jitpack.io' }
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
（2）在module中引用本项目
    implementation 'com.github.MrXYi:netlib:v1.0'
```
## 用法
（1）初始化
```
RxRetrofitApp.init(this);
```
（2）创建HttpService接口类，用于放网络请求接口，列如下面的天气请求接口
```
public interface HttpService {

    //天气
    @POST("weather")
    Observable<String> getWeather(@Body String info);

}
```
（3）继承HttpManagerApi，实现请求数据统一封装类，例如demo中的NetApi

（4）在需要网络请求的类中实现HttpOnNextListener，通过如下方式即可进行网络请求
```
new NetApi(this).getWeather(Flag.NET_GET_WEATHER, getWeather(), null);

请求的结果在如下方法中：
@Override
public void onNext(String resulte, String method, Object flag, Object obj) {
    //正常返回结果
    Log.i("test_resulte", "resulte:" + resulte);
}

@Override
public void onError(ApiException e, String method, Object flag, Object obj) {
    //错误结果
    Log.i("test_resulte", e.getDisplayMessage());
}
```

