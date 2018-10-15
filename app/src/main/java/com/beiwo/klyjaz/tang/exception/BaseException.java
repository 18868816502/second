package com.beiwo.klyjaz.tang.exception;

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

public class BaseException extends Exception {

    public static final int SOCKET_ERROR = 103;//网络错误
    public static final int TIMEOUT_ERROR = 102;//连接超时
    public static final int JSON_ERROR = 101;//json解析错误
    public static final int UNKNOWN_ERROR = 104;//未知

    private int code;
    private String msg;

    public BaseException() {
    }

    public BaseException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
