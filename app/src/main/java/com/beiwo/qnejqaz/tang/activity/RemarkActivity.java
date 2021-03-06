package com.beiwo.qnejqaz.tang.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.util.InputMethodUtil;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.beiwo.qnejqaz.view.ClearEditText;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

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
    ClearEditText edit_text;
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
        InputMethodUtil.openSoftKeyboard(this, edit_text);
        edit_text.setMaxLenght(20);

        if (TextUtils.isEmpty(edit_text.getText().toString().trim())) {
            confirmBtn.setEnabled(false);
            confirmBtn.setTextColor(ContextCompat.getColor(activity, R.color.black_2));
        } else {
            confirmBtn.setEnabled(true);
            confirmBtn.setTextColor(ContextCompat.getColor(activity, R.color.refresh_one));
        }

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
                confirmBtn.setEnabled(true);
                confirmBtn.setTextColor(ContextCompat.getColor(activity, R.color.refresh_one));
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
                                    ToastUtil.toast("保存成功");
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
                                    ToastUtil.toast("保存成功");
                                    EventBus.getDefault().post(remark);
                                    finish();
                                }
                            });
            }
        });
    }
}
