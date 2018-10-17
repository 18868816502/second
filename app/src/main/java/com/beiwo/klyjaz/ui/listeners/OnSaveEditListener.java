package com.beiwo.klyjaz.ui.listeners;

import android.widget.EditText;

import com.beiwo.klyjaz.view.ClearEditText;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.listeners
 * @class describe recyclerView的item点击事件监听
 * @time 2018/9/18 16:56
 */
public interface OnSaveEditListener {

    /**
     * 点击事件回调方法
     * @param flag
     * @param strEdit
     */
    void onSaveEdit(EditText editText, int flag, String strEdit);

}
