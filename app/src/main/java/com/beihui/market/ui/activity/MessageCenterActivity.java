package com.beihui.market.ui.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AnnounceAbstract;
import com.beihui.market.entity.SysMsgAbstract;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerMessageCenterComponent;
import com.beihui.market.injection.module.MessageCenterModule;
import com.beihui.market.ui.adapter.MessageCenterAdapter;
import com.beihui.market.ui.contract.MessageCenterContract;
import com.beihui.market.ui.presenter.MessageCenterPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageCenterActivity extends BaseComponentActivity implements View.OnClickListener, MessageCenterContract.View {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.ignore_unread)
    TextView ignoreUnreadTv;

    private MessageCenterAdapter adapter;
    private HeaderViewHolder headerViewHolder;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM月dd日", Locale.CHINA);

    @Inject
    MessageCenterPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        adapter = new MessageCenterAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_message_center_header, recyclerView, false);
        headerViewHolder = new HeaderViewHolder(view);
        headerViewHolder.annItemView.setOnClickListener(this);
        headerViewHolder.msgItemView.setOnClickListener(this);
        adapter.setHeaderView(view);

        ignoreUnreadTv.setOnClickListener(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerMessageCenterComponent.builder()
                .appComponent(appComponent)
                .messageCenterModule(new MessageCenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ann_item) {
            Intent intent = new Intent(this, AnnouncementActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.msg_item) {
            Intent intent = new Intent(this, SysMsgActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.ignore_unread) {

        }
    }

    @Override
    public void setPresenter(MessageCenterContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showAnnounce(AnnounceAbstract announce) {
        if (announce.getTitle() != null) {
            headerViewHolder.annContentTv.setText(announce.getTitle());
        }
        headerViewHolder.annDataTv.setText(dateFormatter.format(new Date(announce.getGmtCreate())));
    }

    @Override
    public void showSysMsg(SysMsgAbstract sysMsg) {
        if (sysMsg.getExplain() != null) {
            headerViewHolder.msgContentTv.setText(sysMsg.getExplain());
        }
        headerViewHolder.annDataTv.setText(dateFormatter.format(new Date(sysMsg.getGmtCreate())));
    }

    @Override
    public void showNoRecommend() {
        adapter.notifyMessageChanged(null);
        View footer = LayoutInflater.from(this).inflate(R.layout.layout_message_center_no_message, null);
        adapter.setFooterView(footer);
    }

    class HeaderViewHolder {
        @BindView(R.id.ann_item)
        View annItemView;
        @BindView(R.id.ann_content)
        TextView annContentTv;
        @BindView(R.id.ann_date)
        TextView annDataTv;
        @BindView(R.id.msg_item)
        View msgItemView;
        @BindView(R.id.msg_content)
        TextView msgContentTv;
        @BindView(R.id.msg_date)
        TextView msgDateTv;


        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}