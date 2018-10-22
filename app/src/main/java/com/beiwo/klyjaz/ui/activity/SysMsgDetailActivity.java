package com.beiwo.klyjaz.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.SysMsg;
import com.beiwo.klyjaz.entity.SysMsgDetail;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerSysMsgDetailComponent;
import com.beiwo.klyjaz.injection.module.SysMsgDetailModule;
import com.beiwo.klyjaz.ui.contract.SysMsgDetailContract;
import com.beiwo.klyjaz.ui.presenter.SysMsgDetailPresenter;
import com.beiwo.klyjaz.util.DateFormatUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import javax.inject.Inject;

import butterknife.BindView;
import cn.xiaoneng.uiutils.ToastUtils;

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

    private SysMsg.Row sysMsg;

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
        sysMsg = getIntent().getParcelableExtra("sysMsg");
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


            //linkTableType == 2 为社区消息
            if(sysMsg.getLinkTableType() == 2) {
                dealContent(detail);
            }

        }
    }

    private void dealContent(final SysMsgDetail detail) {
        SpannableStringBuilder style = new SpannableStringBuilder();
        String content = detail.getContent();
        style.append(content);
        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                dealClick(detail);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#2a84ff"));
            }
        };
        if(content != null){
            if(content.contains("点击查看详情>>>")){
                style.setSpan(clickableSpan, content.length() - 9, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                answerTv.setText(style);
            }else if(content.contains("点击查看>>>")){
                style.setSpan(clickableSpan, content.length() - 7, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                answerTv.setText(style);
            }else if(content.contains("点击返回>>>")){
                style.setSpan(clickableSpan, content.length() - 7, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                answerTv.setText(style);
            }else{
                answerTv.setText(content);
            }
        }
        //开始响应点击事件,如果不设置，则点击事件不响应
        answerTv.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /**
     * 处理点击事件
     * @param detail
     */
    private void dealClick(SysMsgDetail detail) {
        Intent intent;
        switch (detail.getTitle()){
            case "动态审核成功":
            case "评论审核成功":
            case "评论审核失败":
            case "评论下线通知":
                intent = new Intent(this,ArticleDetailActivity.class);
                intent.putExtra("userId", UserHelper.getInstance(this).getProfile().getId());
                intent.putExtra("forumId",sysMsg.getForumId());
                startActivity(intent);
                break;
            case "动态审核失败":
            case "动态下线通知":
                intent = new Intent(this,CommunityPublishActivity.class);
                intent.putExtra("forumId",sysMsg.getForumId());
                startActivity(intent);
                break;
            default:
                break;
        }
    }


}
