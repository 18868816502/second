package com.beihui.market.ui.dialog;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.ThirdAuthorization;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.presenter.LoanDetailPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ThirdAuthorizationDialog extends DialogFragment {

    @BindView(R.id.user_agreement)
    TextView userAgreement;

    Unbinder unbinder;

    private LoanDetailPresenter presenter;
    private ThirdAuthorization authorization;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CommonBottomDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_third_authorization, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.queryThirdAuthorization();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick({R.id.close, R.id.authorize, R.id.user_agreement})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.authorize:
                //umeng统计
                Statistic.onEvent(Events.LOAN_AUTHORIZE_CONFIRM);

                //pv,uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_THIRD_AUTHORIZATION);

                presenter.clickConfirmAuthorize();
                break;
            case R.id.user_agreement:
                Intent intent = new Intent(getContext(), ComWebViewActivity.class);
                intent.putExtra("title", authorization.getAgreementName());
                intent.putExtra("url", authorization.getAgreementURL());
                startActivity(intent);
                break;
        }
    }

    public void setPresenter(LoanDetailPresenter presenter) {
        this.presenter = presenter;
    }

    public void updateAuthorization(ThirdAuthorization auth) {
        authorization = auth;
        userAgreement.setText("《" + authorization.getAgreementName() + "》");
    }
}
