package com.beiwo.qnejqaz.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * @author chenguoguo
 * @name SelfDesignViews
 * @class name：com.vinny.selfdesignviews
 * @descripe
 * @time 2018/12/6 11:37
 */
public class Util {

    /**
     * 加载文字
     * @param textView 加载的本控件
     * @param text 加载的文案
     */
    public static void loadText(TextView textView, String text) {
        if (textView != null) {
            if (TextUtils.isEmpty(text)) {
                textView.setText("");
            } else {
                textView.setText(text);
            }
        }
    }

    /**
     * 加载图片(可在此切换图片加载框架)
     * @param url 图片路径
     * @param placeholderId 本地图片id，用于占位图
     * @param placeholderId 本地图片id，用于加载错误图
     */
    public static void loadImage(Context mContext, String url,int placeholderId,int errorId, ImageView imageView){
        if(TextUtils.isEmpty(url)){
            Glide.with(mContext).load(placeholderId).into(imageView);
        }else{
            Glide.with(mContext).load(url).placeholder(placeholderId).error(errorId).into(imageView);
        }
    }

    /**
     * 加载图片(可在此切换图片加载框架)
     * @param url 图片路径
     */
    public static void loadImage(Context mContext, String url, ImageView imageView){
        if(TextUtils.isEmpty(url)){
            Glide.with(mContext).load("").into(imageView);
        }else{
            Glide.with(mContext).load(url).into(imageView);
        }
    }
}
