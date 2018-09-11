package com.beihui.market.ui.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerPersonalCenterComponent;
import com.beihui.market.injection.module.PersonalCenterModule;
import com.beihui.market.ui.adapter.PersonalCenterAdapter;
import com.beihui.market.ui.contract.PersonalCenterContact;
import com.beihui.market.ui.presenter.PersonalCenterPresenter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @name loanmarket
 * @class name：com.beihui.market
 * @class describe 个人中心页面
 * @anthor chenguoguo
 * @time
 */
public class PersonalCenterActivity extends BaseComponentActivity implements PersonalCenterContact.View,View.OnClickListener,PersonalCenterAdapter.OnViewClickListener {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.navigate)
    ImageView ivBack;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @Inject
    PersonalCenterPresenter presenter;

    private PersonalCenterAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        ivBack.setOnClickListener(this);

        adapter = new PersonalCenterAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnViewClickListener(this);
        presenter.fetchPersonalArticle();
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerPersonalCenterComponent.builder()
                .appComponent(appComponent)
                .personalCenterModule(new PersonalCenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navigate:
                finish();
                break;
        }
    }

    @Override
    public void onMoreClick() {

    }

    @Override
    public void showPersonalData(List<String> list) {

    }

    @Override
    public void setPresenter(PersonalCenterContact.Presenter presenter) {

    }
}
