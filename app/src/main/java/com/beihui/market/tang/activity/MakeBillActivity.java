package com.beihui.market.tang.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/20
 */

public class MakeBillActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_save_bill)
    TextView tvSaveBill;
    @BindView(R.id.et_input_money)
    EditText etInputMoney;
    @BindView(R.id.fl_money_wrap)
    FrameLayout flMoneyWrap;
    @BindView(R.id.tv_repay_date)
    TextView tvRepayDate;
    @BindView(R.id.fl_repay_date_wrap)
    FrameLayout flRepayDateWrap;
    @BindView(R.id.tv_repay_times)
    TextView tvRepayTimes;
    @BindView(R.id.fl_repay_times_wrap)
    FrameLayout flRepayTimesWrap;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.fl_notice_wrap)
    FrameLayout flNoticeWrap;
    @BindView(R.id.ll_more_setting_wrap)
    LinearLayout llMoreSettingWrap;
    @BindView(R.id.tv_expand_shrink)
    TextView tvExpandShrink;
    @BindView(R.id.iv_expand_shrink)
    ImageView ivExpandShrink;
    @BindView(R.id.rl_expand_shrink)
    RelativeLayout rlExpandShrink;

    private String iconId;
    private String tallyId;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_make_bill;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        String title = getIntent().getStringExtra("title");
        tvToolbarTitle.setText(title);
        iconId = getIntent().getStringExtra("iconId");
        tallyId = getIntent().getStringExtra("tallyId");

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_save_bill, R.id.fl_money_wrap, R.id.fl_repay_date_wrap,
            R.id.fl_repay_times_wrap, R.id.fl_notice_wrap, R.id.rl_expand_shrink})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save_bill:

                break;
            case R.id.fl_money_wrap:
                break;
            case R.id.fl_repay_date_wrap:
                break;
            case R.id.fl_repay_times_wrap:
                break;
            case R.id.fl_notice_wrap:
                break;
            case R.id.rl_expand_shrink:
                break;
            default:
                break;
        }
    }
}
