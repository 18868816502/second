package com.beihui.market.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *    ┏┓　　　┏┓
 *  ┏┛┻━━━┛┻┓
 *  ┃　　　　　　　┃
 *  ┃　　　━　　　┃
 *  ┃　┳┛　┗┳　┃
 *  ┃　　　　　　　┃
 *  ┃　　　┻　　　┃
 *  ┃　　　　　　　┃
 *  ┗━┓　　　┏━┛
 *     ┃　　　┃   神兽保佑
 *     ┃　　　┃   代码无BUG！
 *     ┃　　　┗━━━┓
 *     ┃　　　　　　　┣┓
 *     ┃　　　　　　　┏┛
 *     ┗┓┓┏━┳┓┏┛
 *       ┃┫┫　┃┫┫
 *       ┗┻┛　┗┻┛
 *
 * Created by xhb on 2017/6/12.
 * 线程工具类
 */

public class ThreadUtils {

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    /**
     * 在子线程中运行一段任务
     * @param runnable
     */
    public static void runOnSubThread(Runnable runnable){
        sExecutor.execute(runnable);
    }

    /**
     * 在主线程中运行一段任务
     * @param runnable
     */
    public static void runOnMainThread(Runnable runnable){
        sHandler.post(runnable);
    }
}
