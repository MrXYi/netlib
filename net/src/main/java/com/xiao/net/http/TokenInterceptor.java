package com.onecm.net.http;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.onecm.net.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by xiaoyi on 2017/11/13.
 * Mail:919118325@qq.com
 */

/**
 * 全局自动刷新Token的拦截器
 * <p>
 * 作者：余天然 on 16/9/5 下午3:31
 */
public class TokenInterceptor implements Interceptor {

    private Context context;

    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (BuildConfig.DEBUG) {
            Log.i("onecm_re", "\n");
            Log.i("onecm_re", "-----------onecm_net----------------");
            Log.i("onecm_request", "Url:" + request.url());
            String method = request.method();
            if ("POST".equals(method)) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                Charset charset = UTF8;
                MediaType contentType = request.body().contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("onecm_request", "request_headers:" + request.headers());
                String result = buffer.clone().readString(charset);
                Log.i("onecm_request", "request_parms:" + result);
            }
        }
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        if (!bodyEncoded(response.headers())) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    return response;
                }
            }

            if (!isPlaintext(buffer)) {
                return response;
            }

            if (contentLength != 0) {
                String result = buffer.clone().readString(charset);
                if (BuildConfig.DEBUG) {
                    Log.i("onecm_response", "response.code():" + response.code() + " url:" + response.toString() + " body:" + result);
                }
                //得到所需的string，开始判断是否异常
                //***********************do something*****************************
                //静态注册广播
                if (response.code() == 404) {
                    Intent intent = new Intent();
                    //与清单文件的receiver的anction对应
                    intent.setAction("com.onecm.smarthome.login");
                    intent.setComponent(new ComponentName("com.onecm.smarthome", "com.onecm.smarthome.service.LoginReceiver"));
                    intent.putExtra("info", result);
                    //发送广播
                    context.sendBroadcast(intent);
                }
                try {
                    JSONObject obj = new JSONObject(result);
                } catch (JSONException e) {
                    Intent intent = new Intent();
                    //与清单文件的receiver的anction对应
                    intent.setAction("com.onecm.smarthome.login");
                    intent.setComponent(new ComponentName("com.onecm.smarthome", "com.onecm.smarthome.service.LoginReceiver"));
                    intent.putExtra("info", result);
                    //发送广播
                    context.sendBroadcast(intent);
                }
            } else {
                Log.i("onecm_response", "error_");
            }
        }
        return response;
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}
