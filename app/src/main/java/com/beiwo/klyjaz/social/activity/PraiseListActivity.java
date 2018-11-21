package com.beiwo.klyjaz.social.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.social.adapter.PraiseListAdapter;
import com.beiwo.klyjaz.social.bean.PraiseListBean;
import com.beiwo.klyjaz.ui.activity.ArticleDetailActivity;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.StateLayout;
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
 * 点赞列表、评论列表
 */
public class PraiseListActivity extends BaseComponentActivity implements OnRefreshListener, OnLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemChildClickListener{

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout emptyContainer;
    @BindView(R.id.tv_empty_data)
    TextView tvEmptyData;

    private List<PraiseListBean> datas;
    private PraiseListAdapter adapter;
    private int pageNo = 1;
    private int pageSize = 30;
    private int type = 1;//1:点赞 2:评论

    @Override
    public int getLayoutId() {
        return R.layout.activity_praise_comment;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        //设置状态栏文字为黑色字体
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        if(getIntent()!=null){
            type = getIntent().getIntExtra("type",1);
            if(type == 1){
                tvTitle.setText("赞");
                tvEmptyData.setText("还未收到点赞哦~");
            }else{
                tvTitle.setText("评论");
                tvEmptyData.setText("还未收到评论哦~");
            }
        }
        adapter = new PraiseListAdapter(type);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void initDatas() {
        datas = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        Intent intent = new Intent(this, ArticleDetailActivity.class);
        Intent intent = new Intent(this, ForumDetailActivity.class);
        intent.putExtra("forumId",datas.get(position).getForumId());
        intent.putExtra("userId",datas.get(position).getUserId());
        startActivity(intent);
    }
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.iv_avatar:
                Intent intent = new Intent(this, PersonalCenterActivity.class);
                intent.putExtra("userId",datas.get(position).getUserId());
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    private void fetchData(){
        if(type == 1){
            fetchPraiseList();
        }else{
            fetchCommentList();
        }
    }

    /**
     * 查询点赞列表
     */
    @SuppressLint("CheckResult")
    private void fetchPraiseList(){
        Api.getInstance().queryPraiseList(ParamsUtils.generateDraftsParams(UserHelper.getInstance(this).getProfile().getId(),pageNo,pageSize))
                .compose(RxUtil.<ResultEntity<List<PraiseListBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<PraiseListBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<PraiseListBean>> result){
                                   if (result.isSuccess()) {
                                       if(1 == pageNo){
                                           refreshLayout.finishRefresh();
                                           if(result.getData() == null || result.getData().size() == 0){
                                               stateLayout.setVisibility(View.GONE);
                                               emptyContainer.setVisibility(View.VISIBLE);
                                           }else{
                                               stateLayout.setVisibility(View.VISIBLE);
                                               emptyContainer.setVisibility(View.GONE);
                                               datas.clear();
                                               datas.addAll(result.getData());
                                               adapter.notifyPraiseChanged(datas);
                                           }
                                       }else{
                                           refreshLayout.finishLoadMore();
                                           datas.addAll(result.getData());
                                           adapter.notifyPraiseChanged(datas);
                                       }
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    /**
     * 查询评论列表
     */
    @SuppressLint("CheckResult")
    private void fetchCommentList(){
        Api.getInstance().queryCommentList(ParamsUtils.generateDraftsParams(UserHelper.getInstance(this).getProfile().getId(),pageNo,pageSize))
                .compose(RxUtil.<ResultEntity<List<PraiseListBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<PraiseListBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<PraiseListBean>> result){
                                   if (result.isSuccess()) {
                                       if(1 == pageNo){
                                           refreshLayout.finishRefresh();
                                           if(result.getData() == null || result.getData().size() == 0){
                                               stateLayout.setVisibility(View.GONE);
                                               emptyContainer.setVisibility(View.VISIBLE);
                                           }else{
                                               stateLayout.setVisibility(View.VISIBLE);
                                               emptyContainer.setVisibility(View.GONE);
                                               datas.clear();
                                               datas.addAll(result.getData());
                                               adapter.notifyPraiseChanged(datas);
                                           }
                                       }else{
                                           refreshLayout.finishLoadMore();
                                           datas.addAll(result.getData());
                                           adapter.notifyPraiseChanged(datas);
                                       }
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                refreshLayout.finishLoadMore();
                                refreshLayout.finishRefresh();
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo ++;
        fetchData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        fetchData();
    }
}
