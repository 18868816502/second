package com.beihui.market.tang.rx;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


import com.beihui.market.App;
import com.beihui.market.tang.exception.ApiException;
import com.beihui.market.tang.exception.BaseException;

import org.json.JSONException;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/18
 */

public class RxErrorHandler {
    private Context context = App.getInstance();

    public BaseException errorHandle(Throwable t) {
        BaseException exception = new BaseException();
        if (t instanceof ApiException) {
            ApiException apiException = (ApiException) t;
            exception.setCode(apiException.getCode());
            exception.setMsg(apiException.getMsg());
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            exception.setCode(httpException.code());
            exception.setMsg(httpException.message());
        } else if (t instanceof SocketException) {
            exception.setCode(BaseException.SOCKET_ERROR);
            exception.setMsg("网络错误");
        } else if (t instanceof SocketTimeoutException) {
            exception.setCode(BaseException.TIMEOUT_ERROR);
            exception.setMsg("连接超时");
        } else if (t instanceof JSONException) {
            exception.setCode(BaseException.JSON_ERROR);
            exception.setMsg("json解析错误");
        } else {
            exception.setCode(BaseException.UNKNOWN_ERROR);
            exception.setMsg("未知错误");
        }
        return exception;
    }

    public void showError(BaseException exception) {
        if (!TextUtils.isEmpty(exception.getMsg()) && !"未知错误".equals(exception.getMsg())) {
            Toast.makeText(context, exception.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}