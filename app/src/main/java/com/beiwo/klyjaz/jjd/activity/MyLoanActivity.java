package com.beiwo.klyjaz.jjd.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.jjd.CardAdapter;
import com.beiwo.klyjaz.ui.activity.MainActivity;
import com.beiwo.klyjaz.ui.activity.VestMainActivity;
import com.beiwo.klyjaz.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description: 我的借款
 * @modify:
 * @date: 2018/9/14
 */
public class MyLoanActivity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private CardAdapter adapter = new CardAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.vest_activity_myloan;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        initRecycler();
        empty();
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                ToastUtil.toast("position = " + position);
            }
        });
    }

    private void empty() {
        adapter.setEmptyView(R.layout.vest_empty_layout, recycler);
        View emptyView = adapter.getEmptyView();
        TextView tv_error_msg = emptyView.findViewById(R.id.tv_error_msg);
        TextView tv_retry_btn = emptyView.findViewById(R.id.tv_retry_btn);
        ImageView iv_empty_img = emptyView.findViewById(R.id.iv_empty_img);
        iv_empty_img.setImageResource(R.mipmap.no_data_loan);
        tv_error_msg.setText("您还没有借款记录");
        tv_retry_btn.setText("立即借款");
        tv_retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), App.audit == 2 ? MainActivity.class: VestMainActivity.class);
                intent.putExtra("home", true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}