package com.beihui.market.social.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerEditUserNameComponent;
import com.beihui.market.injection.module.EditUserNameModule;
import com.beihui.market.ui.contract.EditUserNameContract;
import com.beihui.market.ui.listeners.TextWatcherListener;
import com.beihui.market.ui.presenter.EditUserNamePresenter;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.ParamsUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.util.WeakRefToastUtil;
import com.beihui.market.view.ClearEditText;
import com.beihui.market.view.EditTextUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 修改个人简介
 */
public class EditProduceActivity extends BaseComponentActivity {

    public static final int RESULT_OK_EDIT_PRODUCE_ACTIVITY = 11;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.et_produce)
    EditText etProduce;
    @BindView(R.id.tv_num_tips)
    TextView tvNumTips;

    private int black_2;
    private int mSex = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_produce;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);

        Intent intent = getIntent();
        if(intent!=null){
            mSex = intent.getIntExtra("sex",1);
        }
        etProduce.addTextChangedListener(new TextWatcher());
        etProduce.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
    }


    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick(R.id.confirm)
    void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.confirm:
                String introduce = etProduce.getText().toString();
                if(TextUtils.isEmpty(introduce)){
                    ToastUtil.toast("请输入简介");
                    etProduce.requestFocus();
                    return;
                }
                fetchSaveUserInfo(introduce,mSex);
                break;
                default:
                    break;
        }
    }

    class TextWatcher extends TextWatcherListener {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvNumTips.setText((100 - s.length()) + "/100");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @SuppressLint("CheckResult")
    private void fetchSaveUserInfo(final String introduce,int sex){
        Api.getInstance().fetchSaveUserInfo(ParamsUtils.generateUserInfoParams(UserHelper.getInstance(this).getProfile().getId(),sex,introduce))
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if(result.isSuccess()){
                                       Intent intent = new Intent();
                                       intent.putExtra("introduce",introduce);
                                       setResult(RESULT_OK,intent);
                                       finish();
                                   }else{
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                showErrorMsg("网络错误");
                            }
                        });
    }

}
