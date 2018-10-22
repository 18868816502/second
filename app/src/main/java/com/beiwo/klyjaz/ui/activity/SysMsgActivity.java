package com.beiwo.klyjaz.ui.activity;


import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.SysMsg;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerSysMsgComponent;
import com.beiwo.klyjaz.injection.module.SysMsgModule;
import com.beiwo.klyjaz.social.activity.PraiseListActivity;
import com.beiwo.klyjaz.social.bean.SocialMessageBean;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.adapter.SysMsgAdapter;
import com.beiwo.klyjaz.ui.contract.SysMsgContract;
import com.beiwo.klyjaz.ui.dialog.AlertDialog;
import com.beiwo.klyjaz.ui.presenter.SysMsgPresenter;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.SlideRecyclerView;
import com.beiwo.klyjaz.view.StateLayout;
import com.beiwo.klyjaz.view.stateprovider.MessageStateViewProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 系统消息
 */
public class SysMsgActivity extends BaseComponentActivity implements SysMsgContract.View, View.OnClickListener,BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    SlideRecyclerView recyclerView;
    @BindView(R.id.message_more)
    ImageView messaheMore;
    @BindView(R.id.praise_container)
    View praiseContainer;
    @BindView(R.id.comment_container)
    View commentContainer;
    @BindView(R.id.tv_praise_num)
    TextView tvPraiseNum;
    @BindView(R.id.tv_comment_num)
    TextView tvCommentNum;

    private SysMsgAdapter adapter;

    @Inject
    SysMsgPresenter presenter;

    private Dialog messageSelector;
    private List<SysMsg.Row> datas;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (messageSelector != null) {
            messageSelector.dismiss();
        }
        switch (v.getId()) {
            case R.id.from_read:
                readAllMessage();
                break;
            case R.id.from_delete:
                showAlertDialog();
                break;
                default:
                    break;
        }

    }


    /**
     * 弹窗
     */
    private void showAlertDialog() {
        final AlertDialog dialog = new AlertDialog(this);
        dialog.builder().setTitle("确定清空消息？")
                .setPositiveButton("确定", new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        deleteMessage();

                    }
                }).setNegativeButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
            }
        }).show();


    }

    /**
     * 设置消息全部已读
     */
    private void readAllMessage() {
        String userId = UserHelper.getInstance(this).getProfile().getId();
        Api.getInstance().onReadAll(userId).compose(RxUtil.<ResultEntity>io2main()).subscribe(new ApiObserver<ResultEntity>() {
            @Override
            public void onNext(ResultEntity data) {
                //WeakRefToastUtil.showToast(SysMsgActivity.this, "操作成功");
                ToastUtil.toast("操作成功");
                presenter.loadMeaasge();
            }
        });

    }

    private void deleteMessage() {
        String userId = UserHelper.getInstance(this).getProfile().getId();
        Api.getInstance().onDeleteMessageAll(userId).compose(RxUtil.<ResultEntity>io2main()).subscribe(new ApiObserver<ResultEntity>() {
            @Override
            public void onNext(ResultEntity data) {
                //WeakRefToastUtil.showToast(SysMsgActivity.this, "已清空消息");
                ToastUtil.toast("已清空消息");
                presenter.loadMeaasge();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys_msg;
    }

    @Override
    public void configViews() {
        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.MYMESSAGE);

        setupToolbar(toolbar);
        //设置状态栏文字为黑色字体
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        adapter = new SysMsgAdapter(SysMsgActivity.this);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SysMsg.Row row = (SysMsg.Row) adapter.getData().get(position);
                row.setIsRead(1);
                adapter.notifyItemChanged(position);
                if (row.getStyle() == 0) {
                    Intent intent = new Intent(SysMsgActivity.this, SysMsgDetailActivity.class);
                    intent.putExtra("sysMsg", row);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SysMsgActivity.this, App.audit == 2 ? MainActivity.class: VestMainActivity.class);
                    intent.putExtra("account", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.loadMore();
            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onStart();
            }
        });

        stateLayout.setStateViewProvider(new MessageStateViewProvider());

        SlidePanelHelper.attach(this);

//        loadHeadView();
    }

    private void loadHeadView() {

        View headerView=getLayoutInflater().inflate(R.layout.layout_header_system_message, null);

        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        adapter.addHeaderView(headerView);
    }

    @Override
    public void initDatas() {
        refreshLayout.setRefreshing(true);
        presenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.queryCountView();
    }

    @OnClick({R.id.message_more,R.id.praise_container,R.id.comment_container})
    void OnViewClick(View view) {
        switch (view.getId()){
            case R.id.message_more:
                showMessageEdit();
                break;
            case R.id.praise_container:
                Intent pariaseIntent = new Intent(this, PraiseListActivity.class);
                pariaseIntent.putExtra("type",1);
                startActivity(pariaseIntent);
                break;
            case R.id.comment_container:
                Intent commentIntent = new Intent(this, PraiseListActivity.class);
                commentIntent.putExtra("type",2);
                startActivity(commentIntent);
                break;
                default:
                    break;
        }

    }

    private void showMessageEdit() {
        messageSelector = new Dialog(SysMsgActivity.this, R.style.AvatarSelectorStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_message_layout, null);
        view.findViewById(R.id.from_read).setOnClickListener(this);
        view.findViewById(R.id.from_delete).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        messageSelector.setContentView(view);
        messageSelector.setCanceledOnTouchOutside(true);
        Window window = messageSelector.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
        }
        messageSelector.show();
    }


    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSysMsgComponent.builder()
                .appComponent(appComponent)
                .sysMsgModule(new SysMsgModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SysMsgContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showSysMsg(List<SysMsg.Row> sysMsg) {
        stateLayout.switchState(StateLayout.STATE_CONTENT);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if (adapter.isLoading()) {
            adapter.loadMoreComplete();
        }
        datas = sysMsg;
        adapter.notifySysMsgChanged(sysMsg);
    }

    @Override
    public void showNoSysMsg() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        stateLayout.switchState(StateLayout.STATE_EMPTY);
    }

    @Override
    public void showNoMoreSysMsg() {
        adapter.loadMoreEnd(true);
    }

    @Override
    public void onCountViewSucceed(SocialMessageBean msgBean) {
        if(msgBean != null){
            if(msgBean.getPraiseCount()>999){
                tvPraiseNum.setVisibility(View.VISIBLE);
                tvPraiseNum.setText("999+");
            }else if(msgBean.getPraiseCount() == 0){
                tvPraiseNum.setVisibility(View.GONE);
            }else{
                tvPraiseNum.setVisibility(View.VISIBLE);
                tvPraiseNum.setText(String.valueOf(msgBean.getPraiseCount()));
            }
            if(msgBean.getCommentCount()>999){
                tvCommentNum.setVisibility(View.VISIBLE);
                tvCommentNum.setText("999+");
            }else if(msgBean.getCommentCount() == 0){
                tvCommentNum.setVisibility(View.GONE);
            }else{
                tvCommentNum.setVisibility(View.VISIBLE);
                tvCommentNum.setText(String.valueOf(msgBean.getCommentCount()));
            }
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (adapter != null && adapter.isLoading()) {
            adapter.loadMoreEnd(true);
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.tv_delete:
                deleteMessage(datas.get(position).getId());
                break;
            default:
                break;
        }
    }

    private void deleteMessage(String messageId) {

    }


}
