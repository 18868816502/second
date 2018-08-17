package com.beihui.market.tang.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
 * @date: 2018/8/16
 */

public class WithdrawActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getLayoutId() {
        return R.layout.f_activity_withdraw;
    }

    @Override
    public void configViews() {
        setupToolbar(mToolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @OnClick({R.id.tv_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_withdraw:
                startActivity(new Intent(this, BindAlpActivity.class));
                break;
            default:
                break;
        }
    }
}
