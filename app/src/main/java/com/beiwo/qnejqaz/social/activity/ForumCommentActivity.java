package com.beiwo.qnejqaz.social.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.social.adapter.ForumCommentAdapter;
import com.beiwo.qnejqaz.social.bean.CommentReplyBean;
import com.beiwo.qnejqaz.social.contract.ForumCommentContact;
import com.beiwo.qnejqaz.social.dialog.CommentDialog;
import com.beiwo.qnejqaz.social.inter.OnChildViewClickListener;
import com.beiwo.qnejqaz.social.presenter.ForumCommentPresenter;
import com.beiwo.qnejqaz.ui.activity.PersonalCenterActivity;
import com.beiwo.qnejqaz.util.PopUtils;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.activity
 * @descripe 动态详情评论弹窗页
 * @time 2018/11/01 14:21
 */
public class ForumCommentActivity extends BaseCommentActivity implements ForumCommentContact.View,
        BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener, OnChildViewClickListener {

    @BindView(R.id.hold_view)
    View hold_view;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_comment_title)
    TextView tvTitle;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.empty_container)
    View emptyContainer;
    @BindView(R.id.comment_container)
    View commentContainer;

    private ForumCommentPresenter mPresenter;
    private ForumCommentAdapter mAdapter;
    private String forumId;
    private int position;
    private int childPosition;
    private int size = 0;
    private List<CommentReplyBean> datas;
    /**
     * type = 0 评论 1 查看评论 2 回复 3 子回复
     */
    private int type = 0;

    private FragmentManager fManager;
    private String commentContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forum_comment;
    }

    @Override
    public void configViews() {
        int statusHeight = com.beiwo.qnejqaz.util.CommonUtils.getStatusBarHeight(this);
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setBackgroundResource(R.color.transparent_half);
        hold_view.setLayoutParams(params);

        fManager = getSupportFragmentManager();
        mAdapter = new ForumCommentAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        mPresenter = new ForumCommentPresenter(this, this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnChildViewClickListener(this);
        getIntentData();

    }

    private void getIntentData() {
        datas = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            forumId = intent.getStringExtra("forumId");
            type = intent.getIntExtra("type", 0);
            position = intent.getIntExtra("position", 0);
            childPosition = intent.getIntExtra("childPosition", 0);
            List<CommentReplyBean> comments = (List<CommentReplyBean>) intent.getSerializableExtra("list");
            if (comments != null) {
                datas.addAll(comments);
            }
        }
    }

    @Override
    public void initDatas() {
        bindCommentData(datas);
        if (type == 0) {
            showInputDialog(getString(R.string.social_forum_comment_hint));
        } else if (type == 2) {
            showInputDialog(String.format(getString(R.string.social_forum_comment_reply_hint),
                    datas.get(position).getUserName()));
        } else if (type == 3) {
            showInputDialog(String.format(getString(R.string.social_forum_comment_reply_hint),
                    datas.get(position).getReplyDtoList().get(childPosition).getUserName()));
        }
    }

    /**
     * 获取评论数据
     */
    private void fetchData() {
        mPresenter.queryCommentList(forumId, 1, 10000);
    }

    @OnClick({R.id.iv_close, R.id.tv_comment})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                overridePendingTransition(0, R.anim.anim_bottom_exit);
                break;
            case R.id.tv_comment:
                showInputDialog(getString(R.string.social_forum_comment_hint));
                break;
            default:
                break;
        }
    }

    private void showInputDialog(final String hint) {
        new CommentDialog(hint, new CommentDialog.SendListener() {
            @Override
            public void sendComment(String inputText) {
                showCommentAuditPop();
                commentContent = inputText;
            }
        }).show(getSupportFragmentManager(), "comment");
}

    private void showCommentAuditPop() {
        PopUtils.showCommentAuditWindow(fManager, this, this);
    }

    @Override
    public void onQueryCommentSucceed(List<CommentReplyBean> list) {
        this.datas.clear();
        this.datas.addAll(list);
        bindCommentData(datas);
    }

    private void bindCommentData(List<CommentReplyBean> list) {
        tvTitle.setText(String.format(getResources().getString(R.string.social_forum_comment_num), list.size()));
        if (list.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyContainer.setVisibility(View.GONE);
            mAdapter.setDatas(list);
        }
    }

    @Override
    public void onReplyCommentSucceed() {
        ToastUtil.toast(getString(R.string.social_forum_comment_send_tips));
    }

    @Override
    public void onCancelReplySucceed() {
        ToastUtil.toast(getString(R.string.social_forum_delete_succeed));
        fetchData();
    }

    @Override
    public void setPresenter(ForumCommentContact.Presenter presenter) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        this.position = position;
        switch (view.getId()) {
            case R.id.iv_article_comment:
                type = 2;
                this.position = position;
                showInputDialog(String.format(getString(R.string.social_forum_comment_reply_hint),
                        datas.get(position).getUserName()));
                break;
            case R.id.tv_comment_delete:
                mPresenter.fetchCancelReply(datas.get(position).getId());
                break;
            case R.id.iv_commentator_avatar:
                page2PersonalActivity(datas.get(position).getUserId());
                break;
            default:
                break;
        }
    }

    @Override
    public void onChildViewClick(View v, int position, int childPosition) {
        this.position = position;
        this.childPosition = childPosition;
        CommentReplyBean.ReplyDtoListBean replyBean = datas.get(position).getReplyDtoList().get(childPosition);
        switch (v.getId()) {
            case R.id.iv_commentator_avatar:
                page2PersonalActivity(replyBean.getUserId());
                break;
            case R.id.iv_article_comment:
                type = 3;
                showInputDialog(String.format(getString(R.string.social_forum_comment_reply_hint), replyBean.getUserName()));
                break;
            case R.id.tv_comment_delete:
                mPresenter.fetchCancelReply(datas.get(position).getReplyDtoList().get(childPosition).getId());
                break;
            default:
                break;
        }
    }

    private void page2PersonalActivity(String userId) {
        Intent intent = new Intent(ForumCommentActivity.this, PersonalCenterActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                PopUtils.dismiss();
                break;
            case R.id.tv_save:
                PopUtils.dismiss();
                sendComment();
                break;
            default:
                break;
        }
    }

    private void sendComment() {
        switch (type) {
            //动态评论
            case 0:
                mPresenter.fetchReplyInfo("", "1", commentContent, forumId,
                        "", "", "", "");
                break;
            //评论回复
            case 2:
                CommentReplyBean commentReplyBean = datas.get(position);
                mPresenter.fetchReplyInfo("", "2", commentContent, forumId,
                        commentReplyBean.getUserId(), commentReplyBean.getId(),
                        commentReplyBean.getId(), commentReplyBean.getContent());
                break;
            //评论二级回复
            case 3:
                CommentReplyBean commentBean = datas.get(position);
                CommentReplyBean.ReplyDtoListBean replyBean = datas.get(position).getReplyDtoList().get(childPosition);
                mPresenter.fetchReplyInfo("", "2", commentContent, forumId,
                        replyBean.getUserId(), commentBean.getId(),
                        replyBean.getId(), replyBean.getContent());
                break;
            default:
                break;
        }
    }
}
