package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.Notice;
import com.beihui.market.entity.NoticeDetail;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerNoticeDetailComponent;
import com.beihui.market.injection.module.NoticeDetailModule;
import com.beihui.market.ui.contract.NoticeDetailContract;
import com.beihui.market.ui.presenter.NoticeDetailPresenter;

import javax.inject.Inject;

import butterknife.BindView;

public class NoticeDetailActivity extends BaseComponentActivity implements NoticeDetailContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.content)
    TextView contentTv;

    @Inject
    NoticeDetailPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
    }

    @Override
    public void initDatas() {
        Notice.Row notice = getIntent().getParcelableExtra("notice");
        if (notice != null) {
            if (notice.getId() != null) {
                presenter.queryDetail(notice.getId());
            }
            if (notice.getTitle() != null) {
                titleTv.setText(notice.getTitle());
            }
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerNoticeDetailComponent.builder()
                .appComponent(appComponent)
                .noticeDetailModule(new NoticeDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(NoticeDetailContract.Presenter presenter) {
        //injected.nothing to do;
    }

    @Override
    public void showDetail(NoticeDetail detail) {
        if (detail != null && detail.getContent() != null) {
            contentTv.setText(Html.fromHtml(detail.getContent()));
        }
    }
}
