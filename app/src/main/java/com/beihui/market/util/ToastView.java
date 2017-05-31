package com.beihui.market.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;


public class ToastView {

    private static ToastView toastView = null;

    public static ToastView initToast() {
        if (toastView == null) {
            toastView = new ToastView();
        }
        return toastView;
    }

    /**
     * 只显示提示文字的 toast弹出层
     *
     * @param context
     * @param content
     */
    public void textToast(Context context, String content) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.toast_text, null);
            TextView text = (TextView) view.findViewById(R.id.toast_text);
            text.setText(content);
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
    }



    /**
     * 只显示提示文字的 toast弹出层
     *
     * @param context
     * @param content
     */
    public void textToastTop(Context context, String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_text, null);
        TextView text = (TextView) view.findViewById(R.id.toast_text);
        text.setText(content);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    /**
     * 只显示提示文字的 toast弹出层
     *
     * @param context
     * @param content
     */
    public void textToast3(Context context, String content) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.toast_text, null);
            TextView text = (TextView) view.findViewById(R.id.toast_text);
            text.setText(content);

            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
    }

    /**
     * 只显示提示文字的 toast弹出层
     *
     * @param context
     */
    public void textToast2(Context context, String content1, String content2) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_text, null);
        TextView text = (TextView) view.findViewById(R.id.toast_text);
        TextView text1 = (TextView) view.findViewById(R.id.toast_text1);
        text1.setVisibility(View.VISIBLE);
        text.setText(content2);
        text1.setText(content1);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    /**
     * 只显示提示文字的 toast弹出层
     *
     * @param context
     */
    public void textToast3(Context context, String content1, String content2) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_text, null);
        TextView text = (TextView) view.findViewById(R.id.toast_text);
        TextView text1 = (TextView) view.findViewById(R.id.toast_text1);
        text1.setVisibility(View.VISIBLE);
        text.setText(content2);
        text1.setText(content1);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


    /**
     * 显示图标（上）和提示文字（下）的 toast弹出层
     * ［举报成功］
     *
     * @param context
     * @param content
     */
    public void imageTextToast(Context context, String content, int drawableId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_image_text, null);
        TextView text = (TextView) view.findViewById(R.id.tv_tishi);
        ImageView image = (ImageView) view
                .findViewById(R.id.iv_image);
        if (drawableId != 0) {
            image.setBackgroundResource(drawableId);
        }
        text.setText(content);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }


    /**
     * 显示图标（左）和提示文字（右）的 toast弹出层
     * ［举报成功］
     *
     * @param context
     * @param content
     */
    public void imageTextHorToast(Context context, String content) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_image_hor_text, null);
        TextView text = (TextView) view.findViewById(R.id.tv_tishi);
        text.setText(content);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
