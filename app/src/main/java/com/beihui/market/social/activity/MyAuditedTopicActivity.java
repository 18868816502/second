package com.beihui.market.social.activity;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.social.adapter.AuditedTopicAdapter;
import com.beihui.market.social.bean.DraftsBean;
import com.beihui.market.util.ParamsUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beihui.market.social.activity
 * @descripe
 * @time 2018/10/12 10:40
 */
public class MyAuditedTopicActivity extends BaseComponentActivity implements OnRefreshListener, OnLoadMoreListener,BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private AuditedTopicAdapter adapter;
    private List<DraftsBean> datas;

    private int pageNo = 1;
    private int pageSize = 30;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_audit_topic;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        datas = new ArrayList<>();
        adapter = new AuditedTopicAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
    }

    @Override
    public void initDatas() {
        fetchData();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo++;
        fetchData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
    }

    @SuppressLint("CheckResult")
    private void fetchData(){
        Api.getInstance().queryCenterForum(ParamsUtils.generateDraftsParams(UserHelper.getInstance(this).getProfile().getId(),"2#3",pageNo,pageSize))
                .compose(RxUtil.<ResultEntity<List<DraftsBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<DraftsBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<DraftsBean>> result){
                                   if (result.isSuccess()) {
                                       if(1 == pageNo){
                                           if(result.getData() == null || result.getData().size() == 0){
                                               refreshLayout.setVisibility(View.GONE);
                                               llNoData.setVisibility(View.VISIBLE);
                                           }else{
                                               refreshLayout.setVisibility(View.VISIBLE);
                                               llNoData.setVisibility(View.GONE);
                                               datas.clear();
                                               datas.addAll(result.getData());
                                               refreshLayout.finishRefresh();
                                               adapter.notifyDraftsChanged(datas);
                                           }
                                       }else{
                                           datas.addAll(result.getData());
                                           refreshLayout.finishLoadMore();
                                           adapter.notifyDraftsChanged(datas);
                                       }
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
