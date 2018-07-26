package com.beihui.market.util;

import android.content.Context;
import android.widget.Toast;

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
 * Created by xhb on 2017/4/24.
 * 单例吐司
 */
public class ToastUtils {

    private static Toast sToast;

    /**
     * 显示吐司
     */
    public static void showToast(final Context context, final String msg){
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if(sToast == null){
                    //用ApplicationContext防止内存泄露
                    sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
                } else {
                    //如果这个Toast已经在显示了，那么这里会立即改变Toast的文本
                    sToast.setText(msg);
                }
                sToast.show();
            }
        });
    }

    /**
     * 显示吐司
     */
    public static void showToastLongTime(Context context, String msg){
        if(sToast == null){
            //用ApplicationContext防止内存泄露
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
        } else {
            //如果这个Toast已经在显示了，那么这里会立即改变Toast的文本
            sToast.setText(msg);
        }
        sToast.show();
    }
}
