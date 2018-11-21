package com.beiwo.klyjaz.social.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.beiwo.klyjaz.util.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.utils
 * @descripe
 * @time 2018/11/14 14:16
 */
public class InputFilterUtils {

    public static InputFilter emojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast("不支持输入Emoji表情符号");
                return "";
            }
            return null;
        }
    };


}
