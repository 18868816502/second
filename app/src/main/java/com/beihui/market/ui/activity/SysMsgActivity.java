package com.beihui.market.ui.activity;


import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSysMsgComponent;
import com.beihui.market.injection.module.SysMsgModule;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.adapter.SysMsgAdapter;
import com.beihui.market.ui.contract.SysMsgContract;
import com.beihui.market.ui.dialog.AlertDialog;
import com.beihui.market.ui.presenter.SysMsgPresenter;
import com.beihui.market.ui.rvdecoration.NewsItemDeco;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.MessageStateViewProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 系统消息
 */
public class SysMsgActivity extends BaseComponentActivity implements SysMsgContract.View, View.OnClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.message_more)
    ImageView messaheMore;

    private SysMsgAdapter adapter;

    @Inject
    SysMsgPresenter presenter;

    private Dialog messageSelector;

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
                //ToastUtils.showToast(SysMsgActivity.this, "操作成功");
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
                //ToastUtils.showToast(SysMsgActivity.this, "已清空消息");
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
                    Intent intent = new Intent(SysMsgActivity.this, MainActivity.class);
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
    }

    @Override
    public void initDatas() {
        refreshLayout.setRefreshing(true);
        presenter.onStart();
    }

    @OnClick(R.id.message_more)
    void OnViewClick() {
        showMessageEdit();
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
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (adapter != null && adapter.isLoading()) {
            adapter.loadMoreEnd(true);
        }
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
