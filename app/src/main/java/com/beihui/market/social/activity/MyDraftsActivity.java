package com.beihui.market.social.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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
import com.beihui.market.ui.activity.CommunityPublishActivity;
import com.beihui.market.util.ParamsUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.SlideRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * 我的草稿箱
 */
public class MyDraftsActivity extends BaseComponentActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemChildClickListener  {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    SlideRecyclerView recyclerView;
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
        return R.layout.activity_my_drafts;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        datas = new ArrayList<>();
        adapter = new AuditedTopicAdapter(1);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
    }

    @Override
    public void initDatas() {
        fetchData();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @SuppressLint("CheckResult")
    private void fetchData(){
        Api.getInstance().queryCenterForum(ParamsUtils.generateDraftsParams(UserHelper.getInstance(this).getProfile().getId(),pageNo,pageSize))
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
        Intent intent = new Intent(this, CommunityPublishActivity.class);
        intent.putExtra("forumId",datas.get(position).getForumId());
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.tv_delete:
                deleteForum(datas.get(position).getForumId());
                break;
                default:
                    break;
        }
    }

    @SuppressLint("CheckResult")
    private void deleteForum(String forumId){
        Api.getInstance().fetchCancelForum(forumId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result){
                                   if (result.isSuccess()) {
                                       ToastUtil.toast("删除成功");
                                       fetchData();
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }
}
