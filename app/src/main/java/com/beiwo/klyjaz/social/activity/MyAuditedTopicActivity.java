package com.beiwo.klyjaz.social.activity;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.social.adapter.AuditedTopicAdapter;
import com.beiwo.klyjaz.social.bean.DraftsBean;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.PopUtils;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.SlideRecyclerView;
import com.beiwo.klyjaz.view.dialog.PopDialog;
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
public class MyAuditedTopicActivity extends BaseComponentActivity implements OnRefreshListener,
        OnLoadMoreListener,BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener,
        PopDialog.OnInitPopListener,View.OnClickListener{

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
    private String forumId;

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
        adapter = new AuditedTopicAdapter(2);
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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo++;
        fetchData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        fetchData();
    }

    @SuppressLint("CheckResult")
    private void fetchData(){
        Api.getInstance().queryCenterForumAudit(ParamsUtils.generateDraftsParams(UserHelper.getInstance(this).getProfile().getId(),pageNo,pageSize))
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
        ToastUtil.toast("正在审核中");
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.tv_delete:
                forumId = datas.get(position).getForumId();
                PopUtils.showCenterPopWindow(R.layout.dialog_article_delete, getSupportFragmentManager(), this, this);
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

    @Override
    public void initPop(View view, PopDialog mPopDialog) {
        ((TextView)view.findViewById(R.id.content)).setText("确定删除吗？");
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_save).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                PopUtils.dismiss();
                break;
            case R.id.tv_save:
                deleteForum(forumId);
                break;
                default:
                    break;
        }
    }
}
