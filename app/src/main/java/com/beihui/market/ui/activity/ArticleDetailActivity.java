package com.beihui.market.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.UserArticleBean;
import com.beihui.market.entity.UserInfoBean;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerArticleDetailComponent;
import com.beihui.market.injection.module.ArticleDetailModule;
import com.beihui.market.ui.adapter.ArticleDetailAdapter;
import com.beihui.market.ui.contract.ArticleDetailContact;
import com.beihui.market.ui.presenter.ArticleDetailPresenter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.activity
 * @class describe 文章详情
 * @time 2018/9/12 18:55
 */
public class ArticleDetailActivity extends BaseComponentActivity implements ArticleDetailContact.View,
        View.OnClickListener,OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tool_bar)
    RelativeLayout toolBar;
    @BindView(R.id.navigate)
    ImageView ivNavigate;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @Inject
    ArticleDetailPresenter presenter;

    private ArticleDetailAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_article_detail;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolBar).statusBarDarkFont(true).init();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ivNavigate.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerArticleDetailComponent.builder()
                .appComponent(appComponent)
                .articleDetailModule(new ArticleDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onQueryArticleDetailSucceed(UserInfoBean userInfoBean) {

    }

    @Override
    public void onQueryArticleCommentSucceed(List<UserArticleBean> list) {

    }

    @Override
    public void setPresenter(ArticleDetailContact.Presenter presenter) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigate:
                finish();
                break;
            case R.id.iv_more:

                break;
                default:
                    break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
