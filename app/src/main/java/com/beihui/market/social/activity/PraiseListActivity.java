package com.beihui.market.social.activity;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.social.adapter.PraiseListAdapter;
import com.beihui.market.social.bean.PraiseListBean;
import com.beihui.market.util.ParamsUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.SlideRecyclerView;
import com.beihui.market.view.StateLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class PraiseListActivity extends BaseComponentActivity implements BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemChildClickListener ,
        SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
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
                tvEmptyData.setText("还未收到点赞哦~");
            }else{
                tvEmptyData.setText("还未收到评论哦~");
            }
        }
        adapter = new PraiseListAdapter(type);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initDatas() {
        datas = new ArrayList<>();
        fetchData();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onLoadMoreRequested() {
        pageNo ++;
        fetchData();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        fetchData();
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
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    /**
     * 查询点赞列表
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
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

}