package com.beiwo.qnejqaz.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.ui.contract.EditUserNameContract;
import com.beiwo.qnejqaz.ui.presenter.EditUserNamePresenter;
import com.beiwo.qnejqaz.util.InputMethodUtil;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;
import com.beiwo.qnejqaz.view.ClearEditText;
import com.beiwo.qnejqaz.view.EditTextUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改昵称的页面
 */
public class EditNickNameActivity extends BaseComponentActivity implements EditUserNameContract.View {

    public static final int RESULT_OK_EDIT_NICK_NAME_ACTIVITY = 10;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.edit_text)
    ClearEditText editText;
    @BindView(R.id.confirm)
    TextView confirmBtn;

    EditUserNamePresenter presenter;
    private int black_2;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_nick_name;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        presenter = new EditUserNamePresenter(this,this);

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source != null) {
                    try {
                        String destStr = dest.toString();
                        String srcStr = source.toString();
                        String result = srcStr + destStr;
                        byte[] bytes = result.getBytes("GBK");
                        //10个中文或者20个字符
                        if (bytes.length > 20) {
                            int srcEnd = srcStr.length() - 1;
                            bytes = (destStr + srcStr.substring(0, srcEnd)).getBytes("GBk");
                            while (bytes.length > 20) {
                                srcEnd--;
                                bytes = (destStr + srcStr.substring(0, srcEnd)).getBytes("GBk");
                            }
                            return srcStr.substring(0, srcEnd);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

        InputFilter[] filters = editText.getFilters();
        if (filters == null) {
            filters = new InputFilter[]{filter};
        } else {
            InputFilter[] temp = filters;
            filters = new InputFilter[temp.length + 1];
            System.arraycopy(temp, 0, filters, 0, temp.length);
            filters[temp.length] = filter;
        }
        EditTextUtils.addDisableEmojiInputFilter(editText);
        editText.setFilters(filters);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                int length = 0;
//                if (s.toString() != null) {
//                    length = s.toString().getBytes().length;
//                    Log.e("afs", "length---> " + length);
//                }
//                boolean isConfirm = length > 0 && length < 17;
//
//                if (isConfirm ||  length == 0) {
//                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter((16))});
//                } else {
//                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter((s.toString().length()))});
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                int length = 0;
//                if (s.toString() != null) {
//                    length = s.toString().getBytes().length;
//                    Log.e("afs", "length---> " + length);
//                }
                if (TextUtils.isEmpty(s.toString().trim())) {
                    confirmBtn.setEnabled(false);
                    confirmBtn.setTextColor(black_2);
                    return;
                }
                byte[] bytes = new byte[0];
                try {
                    if (!TextUtils.isEmpty(s.toString())) {
                        bytes = s.toString().getBytes("GBk");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                boolean isConfirm = bytes.length > 0 && bytes.length < 17;
                confirmBtn.setEnabled(isConfirm);
                confirmBtn.setTextColor(isConfirm ? Color.parseColor("#ff5240") : black_2);


                if (bytes.length > 16) {
                    //com.beihui.market.util.WeakRefToastUtil.showToast(EditNickNameActivity.this, "支持输入1~16个字符");
                    ToastUtil.toast("支持输入1~16个字符");
                }

                //Log.e("xhxhxb", "bytes --> " + bytes.length);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SlidePanelHelper.attach(this);
    }


    @Override
    public void initDatas() {
        black_2 = getResources().getColor(R.color.black_2);
        presenter.onStart();
    }

    @OnClick(R.id.confirm)
    void onViewClicked() {
        presenter.updateUserName(editText.getText().toString());
    }

    @Override
    public void setPresenter(EditUserNameContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissProgress();
        WeakRefToastUtil.showShort(this, msg, null);
    }

    @Override
    public void showUserName(String name) {
        editText.setText(name);
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void showUpdateNameSuccess(String msg, String nickName) {
        dismissProgress();
        WeakRefToastUtil.showShort(this, msg, null);
        Intent intent = new Intent();
        intent.putExtra("updateNickName", nickName);
        setResult(RESULT_OK_EDIT_NICK_NAME_ACTIVITY, intent);
        finish();
    }

    @Override
    public void finish() {
        InputMethodUtil.closeSoftKeyboard(this);
        super.finish();
    }
}
