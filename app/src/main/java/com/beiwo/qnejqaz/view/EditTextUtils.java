package com.beiwo.qnejqaz.view;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class EditTextUtils {
    public static void addDisableEmojiInputFilter(EditText editText) {
        InputFilter[] filters = editText.getFilters();
        InputFilter[] newFilters;
        if (filters != null) {
            newFilters = new InputFilter[filters.length + 1];
            System.arraycopy(filters, 0, newFilters, 0, filters.length);
            newFilters[newFilters.length - 1] = new EmojiFilter();
        } else {
            newFilters = new InputFilter[]{new EmojiFilter()};
        }
        editText.setFilters(newFilters);
    }

    public static void addDecimalDigitsInputFilter(EditText editText) {
        InputFilter[] filters = editText.getFilters();
        InputFilter[] newFilters;
        if (filters != null) {
            newFilters = new InputFilter[filters.length + 1];
            System.arraycopy(filters, 0, newFilters, 0, filters.length);
            newFilters[newFilters.length - 1] = new DecimalFilter();
        } else {
            newFilters = new InputFilter[]{new EmojiFilter()};
        }
        editText.setFilters(newFilters);
    }

    static class EmojiFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }

    static class DecimalFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String s = new StringBuilder(dest).insert(dstart, source).toString();
            if (s.contains(".") && s.length() - s.indexOf(".") > 3) {
                return "";
            }
            return null;
        }
    }
}