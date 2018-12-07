package com.beiwo.klyjaz.social.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.adapter.ForumDetailAdapter;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.ForumBean;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;
import com.beiwo.klyjaz.social.classhelper.ForumHelper;
import com.beiwo.klyjaz.social.contract.ForumDetailContact;
import com.beiwo.klyjaz.social.inter.OnChildViewClickListener;
import com.beiwo.klyjaz.social.presenter.ForumDetailPresenter;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.util.PopUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.activity
 * @descripe 动态详情页
 * @time 2018/10/29 11:35
 */
public class ForumDetailActivity extends BaseComponentActivity implements ForumDetailContact.View, OnRefreshListener,
        View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, OnChildViewClickListener {

    @BindView(R.id.tool_bar)
    RelativeLayout toolBar;
    @BindView(R.id.navigate)
    ImageView ivNavigate;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ForumDetailPresenter mPresenter;
    private FragmentManager fManager;

    private ForumDetailAdapter mAdapter;
    private ForumHelper forumHelper;
    private String forumId;
    private ForumBean forumBean;
    private List<CommentReplyBean> commentLists;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forum_detail;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolBar).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        fManager = getSupportFragmentManager();

        mAdapter = new ForumDetailAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(this);

        mPresenter = new ForumDetailPresenter(this, this);
        forumHelper = new ForumHelper(this);
        mAdapter.addHeaderView(forumHelper.initHead(recyclerView, this));
        mAdapter.addFooterView(forumHelper.initFoot(recyclerView, this));
        ivMore.setOnClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnChildViewClickListener(this);
    }

    @Override
    public void initDatas() {
        Intent intent = getIntent();
        this.forumId = intent.getStringExtra("forumId");
        fetchData();
    }

    private void fetchData() {
        mPresenter.queryForumInfo(forumId, 1, 10000);
        mPresenter.queryCommentList(forumId, 1, 10000);
    }

    @Override
    public void onQueryForumInfoSucceed(ForumInfoBean forumBean) {
        this.forumBean = forumBean.getForum();
        refreshLayout.finishRefresh();
        forumHelper.updateHeadDatas(forumBean.getForum());
        forumHelper.updateFootDatas(forumBean.getForum());
    }

    @Override
    public void onQueryCommentSucceed(List<CommentReplyBean> list) {
        refreshLayout.finishRefresh();
        this.commentLists = list;
        if (list != null) {
            if (list.size() > 3) {
                mAdapter.addData(list.subList(0, 3));
            } else {
                mAdapter.addData(list);
            }
        }
    }

    @Override
    public void onSaveReportSucceed() {
        ToastUtil.toast(getString(R.string.social_forum_report_succeed));
    }

    @Override
    public void onCancelForumSucceed() {
        ToastUtil.toast(getString(R.string.social_forum_delete_succeed));
        finish();
    }

    @Override
    public void onCancelReplySucceed() {
        ToastUtil.toast(getString(R.string.social_forum_delete_succeed));
        fetchData();
    }

    @Override
    public void setPresenter(ForumDetailContact.Presenter presenter) {
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        fetchData();
    }

    @Override
    public void onClick(View v) {
        if (!UserHelper.getInstance(this).isLogin()) {
            DlgUtil.loginDlg(this, null);
            return;
        }
        switch (v.getId()) {
            case R.id.iv_more:
                showOperateWindow();
                break;

            case R.id.iv_comment:
                page2CommentActivity(0, 0, 0);
                break;
            case R.id.foot:
                page2CommentActivity(1, 0, 0);
                break;

            case R.id.tv_delete:
                PopUtils.dismiss();
                mPresenter.fetchCancelForum(forumBean.getForumId());
                break;
            case R.id.tv_cancel:
                PopUtils.dismiss();
                break;
            case R.id.report01:
                PopUtils.dismiss();
                fetchReport(getString(R.string.article_more_report_content1));
                break;
            case R.id.report02:
                PopUtils.dismiss();
                fetchReport(getString(R.string.article_more_report_content2));
                break;
            case R.id.report03:
                PopUtils.dismiss();
                fetchReport(getString(R.string.article_more_report_content3));
                break;
            case R.id.report04:
                PopUtils.dismiss();
                fetchReport(getString(R.string.article_more_report_content4));
                break;
            default:
                break;
        }
    }

    /**
     * 举报动态
     *
     * @param reportContent 举报内容
     */
    private void fetchReport(String reportContent) {
        if (forumBean != null) {
            mPresenter.fetchSaveReport(forumBean.getForumId(), "1", reportContent);
        }
    }

    private void showOperateWindow() {
        if (UserHelper.getInstance(this).isLogin()) {
            //判断是否是自己的文章
            if (TextUtils.equals(forumBean.getUserId(), UserHelper.getInstance(this).getProfile().getId())) {
                PopUtils.showForumDeleteDialog(fManager, this, this);
            } else {
                PopUtils.showForumReportDialog(fManager, this, this);
            }
        } else {
            DlgUtil.loginDlg(this, null);
        }
    }

    private void page2CommentActivity(int type, int position, int childPosition) {
        Intent intent = new Intent(this, ForumCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("forumId", forumId);
        bundle.putInt("type", type);
        bundle.putInt("position", position);
        bundle.putInt("childPosition", childPosition);
        bundle.putSerializable("list", (Serializable) commentLists);
        intent.putExtras(bundle);
        startActivity(intent);
//        overridePendingTransition(R.anim.anim_bottom_enter, R.anim.anim_bottom_exit);
        overridePendingTransition(R.anim.anim_bottom_enter, 0);
    }

    private void page2PersonalActivity(String userId) {
        Intent intent = new Intent(ForumDetailActivity.this, PersonalCenterActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (!UserHelper.getInstance(this).isLogin()) {
            DlgUtil.loginDlg(this, null);
            return;
        }
        switch (view.getId()) {
            case R.id.iv_article_comment:
                page2CommentActivity(2, position, 0);
                break;
            case R.id.tv_comment_delete:
                mPresenter.fetchCancelReply(commentLists.get(position).getId());
                break;
            case R.id.iv_commentator_avatar:
                page2PersonalActivity(commentLists.get(position).getUserId());
                break;
            default:
                break;
        }
    }

    @Override
    public void onChildViewClick(View v, int position, int childPosition) {
        CommentReplyBean.ReplyDtoListBean replyBean = commentLists.get(position).getReplyDtoList().get(childPosition);
        switch (v.getId()) {
            case R.id.iv_commentator_avatar:
                page2PersonalActivity(replyBean.getUserId());
                break;
            case R.id.iv_article_comment:
                page2CommentActivity(3, position, childPosition);
                break;
            case R.id.tv_comment_delete:
                mPresenter.fetchCancelReply(commentLists.get(position).getReplyDtoList().get(childPosition).getId());
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent("refresh_layout");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        override = false;
        super.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieve(String msg) {
        if (TextUtils.equals("1", msg)) {
            fetchData();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}