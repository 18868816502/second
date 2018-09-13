package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.NutEmail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.NutEmailAdapter;
import com.beihui.market.ui.rvdecoration.CommVerItemDeco;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.WeakRefToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.lang.reflect.Field;
import java.util.List;

import app.u51.com.newnutsdk.NutSDK;
import app.u51.com.newnutsdk.model.MailSupportConfig;
import app.u51.com.newnutsdk.net.TenantProvider;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class NutEmailActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.zs_container)
    FrameLayout flZsContainer;

    private NutEmailAdapter adapter;

    private String emailSymbol;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //邮箱登录页面授权完成，开启进度页面
        if (requestCode == 1 && data == null) {
            Intent intent = new Intent(NutEmailActivity.this, EmailLeadingInProgressActivity.class);
            intent.putExtra("email_symbol", emailSymbol);
            startActivity(intent);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_nut_email;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NutEmailAdapter();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                emailSymbol = ((NutEmail) adapter.getItem(position)).getSymbol();
                try {
                    Field field = NutSDK.getDefault().getClass().getDeclaredField("mailSupportConfig");
                    field.setAccessible(true);
                    MailSupportConfig config = (MailSupportConfig) field.get(NutSDK.getDefault());
                    if (config != null) {
                        Intent intent = new Intent(NutEmailActivity.this, EmailLoginActivity.class);
                        intent.putExtra("extra_mail_config", config.getMailItemConfig(emailSymbol));
                        startActivityForResult(intent, 1);
                    } else {
                        WeakRefToastUtil.showShort(NutEmailActivity.this, "正在获取配置", null);
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        float density = getResources().getDisplayMetrics().density;
        recyclerView.addItemDecoration(new CommVerItemDeco((int) (density * 0.5), (int) (15 * density), (int) (15 * density)));

        SlidePanelHelper.attach(this);
    }

    @SuppressLint("CheckResult")
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
                return UserHelper.getInstance(NutEmailActivity.this).getProfile().getId();
            }
        });
        NutSDK.getDefault().getConfig();

        Api.getInstance().fetchNutEmail()

                .compose(RxUtil.<ResultEntity<List<NutEmail>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<NutEmail>>>() {
                               @Override
                               public void accept(ResultEntity<List<NutEmail>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       adapter.notifyNutEmailChanged(result.getData());
                                   } else {
                                       WeakRefToastUtil.showShort(NutEmailActivity.this, result.getMsg(), null);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //Log.e("NutEmailActivity", throwable.toString());
                                WeakRefToastUtil.showShort(NutEmailActivity.this, "请求出错", null);
                            }
                        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.help_feedback, R.id.zs_close, R.id.zs_now})
    void onItemClicked(View view) {
        switch (view.getId()) {
            case R.id.help_feedback:
                startActivity(new Intent(this, HelpAndFeedActivity.class));
                break;
            case R.id.zs_close:
                flZsContainer.setVisibility(View.GONE);
                break;
            case R.id.zs_now:
                startActivity(new Intent(this, EBankActivity.class));
                break;
        }
    }
}
