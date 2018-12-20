package com.beiwo.qnejqaz.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.beiwo.qnejqaz.view.dialog.PopDialog;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.util
 * @class describe
 * @time 2018/9/17 15:21
 */
public class KeyBoardUtils {

    private KeyBoardUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 切换软键盘的状态
     * 如当前为收起变为弹出,若当前为弹出变为收起
     */
    public static void toggleKeybord(EditText edittext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void toggleKeyboard(Context mContext) {
        InputMethodManager inputMgr = (InputMethodManager)
                mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMgr != null) {
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        }
    }

    /**
     * 强制隐藏输入法键盘
     */
    public static void hideKeybord(EditText edittext, PopDialog commentDialog) {
        InputMethodManager manager = (InputMethodManager) edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(commentDialog.getDialog().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制显示输入法键盘
     */
    public static void showKeybord(EditText edittext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 输入法是否显示
     */
    public static boolean isKeybord(EditText edittext) {
        boolean bool = false;
        InputMethodManager inputMethodManager = (InputMethodManager)
                edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            bool = true;
        }
        return bool;
    }
}