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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UsedEmailActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private UsedEmailAdapter adapter;

    private String emailSymbol;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data == null) {
            Intent intent = new Intent(UsedEmailActivity.this, EmailLeadingInProgressActivity.class);
            intent.putExtra("email_symbol", emailSymbol);
            startActivity(intent);
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
                emailSymbol = ((UsedEmail) adapter.getItem(position)).getSymbol();
                try {
                    Field field = NutSDK.getDefault().getClass().getDeclaredField("mailSupportConfig");
                    field.setAccessible(true);
                    MailSupportConfig config = (MailSupportConfig) field.get(NutSDK.getDefault());
                    if (config != null) {
                        Intent intent = new Intent(UsedEmailActivity.this, EmailLoginActivity.class);
                        intent.putExtra("extra_mail_config", config.getMailItemConfig(emailSymbol));
                        startActivityForResult(intent, 1);
                    } else {
                        WeakRefToastUtil.showShort(UsedEmailActivity.this, "正在获取配置", null);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
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

        Disposable dis = Api.getInstance().fetchUsedEmail(UserHelper.getInstance(this).getProfile().getId())
                .compose(RxUtil.<ResultEntity<List<UsedEmail>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<UsedEmail>>>() {
                               @Override
                               public void accept(ResultEntity<List<UsedEmail>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       adapter.notifyUsedEmailChanged(result.getData());
                                   } else {
                                       WeakRefToastUtil.showShort(UsedEmailActivity.this, result.getMsg(), null);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("UsedEmailActivity", throwable.toString());
                                WeakRefToastUtil.showShort(UsedEmailActivity.this, "请求出错", null);
                            }
                        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.add_email, R.id.help_feedback})
    void onItemClicked(View view) {
        if (view.getId() == R.id.add_email) {
            startActivity(new Intent(this, NutEmailActivity.class));
        } else if (view.getId() == R.id.help_feedback) {
            startActivity(new Intent(this, HelpAndFeedActivity.class));
        }
    }
}
