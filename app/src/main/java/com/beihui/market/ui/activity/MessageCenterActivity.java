package com.beihui.market.ui.activity;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageCenterActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private HeaderViewHolder headerViewHolder;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_message_center_header, recyclerView, false);
        headerViewHolder = new HeaderViewHolder(view);
        headerViewHolder.annItemView.setOnClickListener(this);
        headerViewHolder.msgItemView.setOnClickListener(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ann_item) {

        } else if (v.getId() == R.id.msg_item) {

        }
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


        public HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}