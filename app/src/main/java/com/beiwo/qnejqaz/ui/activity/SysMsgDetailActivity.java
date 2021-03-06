package com.beiwo.qnejqaz.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.SysMsg;
import com.beiwo.qnejqaz.entity.SysMsgDetail;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.social.activity.ForumDetailActivity;
import com.beiwo.qnejqaz.social.activity.ForumPublishActivity;
import com.beiwo.qnejqaz.social.bean.DraftEditForumBean;
import com.beiwo.qnejqaz.social.bean.ForumInfoBean;
import com.beiwo.qnejqaz.ui.contract.SysMsgDetailContract;
import com.beiwo.qnejqaz.ui.presenter.SysMsgDetailPresenter;
import com.beiwo.qnejqaz.util.DateFormatUtils;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

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

    SysMsgDetailPresenter presenter;

    private SysMsg.Row sysMsg;
    private SysMsgDetail detail;

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
        presenter = new SysMsgDetailPresenter(this);
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
    public void setPresenter(SysMsgDetailContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showSysMsgDetail(SysMsgDetail detail) {
        this.detail = detail;
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
            if (sysMsg.getLinkTableType() == 2) {
                dealContent(detail);
            }

        }
    }

    @Override
    public void onEditForumSucceed(DraftEditForumBean forumBean) {
//        Intent intent = new Intent(this,CommunityPublishActivity.class);
        Intent intent = new Intent(this,ForumPublishActivity.class);
        intent.putExtra("forumId",sysMsg.getForumId());
        startActivity(intent);
    }

    @Override
    public void onEditForumFailure() {
//        ToastUtil.toast("该动态已重新提交审核");
    }

    @Override
    public void onQueryForumInfoSucceed(ForumInfoBean forumBean) {
        if(forumBean.getForum() == null){
            ToastUtil.toast("该动态已删除");
        }else{
//            Intent intent = new Intent(this,ArticleDetailActivity.class);
            Intent intent = new Intent(this,ForumDetailActivity.class);
            intent.putExtra("userId", UserHelper.getInstance(this).getProfile().getId());
            intent.putExtra("forumId",sysMsg.getForumId());
            startActivity(intent);
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
        if (content != null) {
            if (content.contains("点击查看详情>>>")) {
                style.setSpan(clickableSpan, content.length() - 9, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                answerTv.setText(style);
            } else if (content.contains("点击查看>>>")) {
                style.setSpan(clickableSpan, content.length() - 7, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                answerTv.setText(style);
            } else if (content.contains("点击返回>>>")) {
                style.setSpan(clickableSpan, content.length() - 7, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                answerTv.setText(style);
            } else {
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
        switch (detail.getTitle()) {
            case "动态审核成功":
            case "评论审核成功":
            case "评论审核失败":
            case "评论下线通知":
                if (!TextUtils.isEmpty(sysMsg.getForumId())) {
                    presenter.queryForumInfo(UserHelper.getInstance(this).getProfile().getId(), sysMsg.getForumId(), 1, 30);
                }
                break;
            case "动态审核失败":
            case "动态下线通知":
                if (!TextUtils.isEmpty(sysMsg.getForumId())) {
                    presenter.fetchEditForum(sysMsg.getForumId());
                }
                break;
            default:
                break;
        }
    }
}