package com.beihui.market.ui.activity;


import android.media.Image;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.SysMsg;
import com.beihui.market.entity.SysMsgDetail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSysMsgDetailComponent;
import com.beihui.market.injection.module.SysMsgDetailModule;
import com.beihui.market.ui.contract.SysMsgDetailContract;
import com.beihui.market.ui.presenter.SysMsgDetailPresenter;
import com.beihui.market.util.DateFormatUtils;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import javax.inject.Inject;

import butterknife.BindView;

public class SysMsgDetailActivity extends BaseComponentActivity implements SysMsgDetailContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleTv;
    @BindView(R.id.date)
    TextView dateTv;
    @BindView(R.id.read_sum)
    TextView readSumTv;
    @BindView(R.id.content)
    TextView contentTv;
    @BindView(R.id.answer)
    TextView answerTv;
    @BindView(R.id.feed_img)
    ImageView imageView;
    @BindView(R.id.feed_liner_layout)
    LinearLayout feed_line;

    @Inject
    SysMsgDetailPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys_msg_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        //设置状态栏文字为黑色字体
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        SysMsg.Row sysMsg = getIntent().getParcelableExtra("sysMsg");
        if (sysMsg != null) {
            if (sysMsg.getId() != null) {
                presenter.queryMsgDetail(sysMsg.getId());
            }
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSysMsgDetailComponent.builder()
                .appComponent(appComponent)
                .sysMsgDetailModule(new SysMsgDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SysMsgDetailContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showSysMsgDetail(SysMsgDetail detail) {
        if (detail != null) {
            if (detail.getMessageType() == 0) {
                feed_line.setVisibility(View.GONE);
                answerTv.setText(detail.getContent());
            } else {
                feed_line.setVisibility(View.VISIBLE);
                if (detail.getAnswer() != null) {
                    answerTv.setText(detail.getAnswer());
                }
                if (detail.getContent() != null) {
                    contentTv.setText(detail.getContent());
                }
                if (detail.getImage() != null) {
                    Glide.with(this).load(detail.getImage()).into(imageView);
                }
            }
            if (detail.getTitle() != null) {
                titleTv.setText(detail.getTitle());
            }
            dateTv.setText(DateFormatUtils.formatMMddHHmm(detail.getGmtCreate()));

        }
    }
}
