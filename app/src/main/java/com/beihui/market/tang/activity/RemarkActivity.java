package com.beihui.market.tang.activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.EventBean;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.ToastUtils;
import com.beihui.market.view.EditTextUtils;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;


/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/23
 */

public class RemarkActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text)
    EditText edit_text;
    @BindView(R.id.confirm)
    TextView confirmBtn;

    private RemarkActivity activity;
    private String recordId = "";
    private String remark = "";
    private int type;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_remark;
    }

    @Override
    public void configViews() {
        activity = this;
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        try {
            type = getIntent().getIntExtra("type", -1);
            recordId = getIntent().getStringExtra("recordId");
            remark = getIntent().getStringExtra("remark");
        } catch (Exception e) {
        }
        if (remark != null && !remark.isEmpty()) {
            edit_text.setText(remark);
            edit_text.setSelection(remark.length());
        }
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

        InputFilter[] filters = edit_text.getFilters();
        if (filters == null) {
            filters = new InputFilter[]{filter};
        } else {
            InputFilter[] temp = filters;
            filters = new InputFilter[temp.length + 1];
            System.arraycopy(temp, 0, filters, 0, temp.length);
            filters[temp.length] = filter;
        }
        EditTextUtils.addDisableEmojiInputFilter(edit_text);
        edit_text.setFilters(filters);

        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    confirmBtn.setEnabled(false);
                    confirmBtn.setTextColor(ContextCompat.getColor(activity, R.color.black_2));
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
                boolean isConfirm = bytes.length > 0 && bytes.length < 21;
                confirmBtn.setEnabled(isConfirm);
                confirmBtn.setTextColor(isConfirm ? Color.parseColor("#ff5240") : ContextCompat.getColor(activity, R.color.black_2));

                if (bytes.length > 20) {
                    ToastUtils.showToast(activity, "支持输入1~20个字符");
                    return;
                }
                remark = s.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtil.closeSoftKeyboard(activity);
                if (type == 1)//通用
                    Api.getInstance().updateFastDebtBillRemark(UserHelper.getInstance(activity).id(), recordId, remark)
                            .compose(RxResponse.compatO())
                            .subscribe(new ApiObserver<Object>() {
                                @Override
                                public void onNext(@NonNull Object data) {
                                    ToastUtils.showToast(activity, "保存成功");
                                    EventBus.getDefault().post(remark);
                                    finish();
                                }
                            });
                if (type == 2)//网贷
                    Api.getInstance().updateLoanDebtBillRemark(UserHelper.getInstance(activity).id(), recordId, remark)
                            .compose(RxResponse.compatO())
                            .subscribe(new ApiObserver<Object>() {
                                @Override
                                public void onNext(@NonNull Object data) {
                                    ToastUtils.showToast(activity, "保存成功");
                                    EventBus.getDefault().post(remark);
                                    finish();
                                }
                            });
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}
