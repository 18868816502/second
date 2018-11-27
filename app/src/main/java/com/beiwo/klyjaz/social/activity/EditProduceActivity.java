package com.beiwo.klyjaz.social.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.ui.listeners.TextWatcherListener;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

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
