package com.beiwo.klyjaz.ui.listeners;

import android.view.View;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.listeners
 * @class describe
 * @time 2018/9/18 15:26
 */
public interface OnViewClickListener {

    /**
     * 关注和点赞点击事件
     * @param view 点击的控件
     * @param type 类型，用于判断点击的是哪个控件
     */
    void onViewClick(View view, int type,int position);

}
