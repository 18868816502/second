package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.UsedEmail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.UsedEmailAdapter;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import app.u51.com.newnutsdk.NutSDK;
import app.u51.com.newnutsdk.net.TenantProvider;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class UsedEmailActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private UsedEmailAdapter adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_used_email;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsedEmailAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        //获取坚果配置
        NutSDK.getDefault().setTenantProvider(getApplicationContext(), new TenantProvider() {
            @Override
            public String getTenantId() {
                return "AXGJ";
            }

            @Override
            public String getSdkToken() {
                return "b88b738cdad9d4a94790409f12697f06";
            }

            @Override
            public String getUserId() {
                return UserHelper.getInstance(UsedEmailActivity.this).getProfile().getId();
            }
        });
        NutSDK.getDefault().getConfig();

        Api.getInstance().fetchUsedEmail(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxUtil.<ResultEntity<List<UsedEmail>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<UsedEmail>>>() {
                               @Override
                               public void accept(ResultEntity<List<UsedEmail>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       adapter.notifyUsedEmailChanged(result.getData());
                                   } else {
                                       ToastUtils.showShort(UsedEmailActivity.this, result.getMsg(), null);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("UsedEmailActivity", throwable.toString());
                                ToastUtils.showShort(UsedEmailActivity.this, "请求出错", null);
                            }
                        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick(R.id.add_email)
    void onItemClicked() {
        startActivityForResult(new Intent(this, NutEmailActivity.class), 1);
    }
}
