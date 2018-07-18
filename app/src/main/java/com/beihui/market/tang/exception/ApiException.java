package com.beihui.market.tang.exception;

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

public class ApiException extends BaseException {
    public ApiException(int code, String msg) {
        super(code, msg);
    }
}
