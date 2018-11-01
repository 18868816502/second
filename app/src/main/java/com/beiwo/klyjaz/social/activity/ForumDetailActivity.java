package com.beiwo.klyjaz.social.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.social.adapter.ForumDetailAdapter;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;
import com.beiwo.klyjaz.social.classhelper.ForumHelper;
import com.beiwo.klyjaz.social.contract.ForumDetailContact;
import com.beiwo.klyjaz.social.dialog.CommentDialog;
import com.beiwo.klyjaz.social.dialog.CommentInputDialog;
import com.beiwo.klyjaz.social.presenter.ForumDetailPresenter;
import com.beiwo.klyjaz.social.utils.DialogUtils;
import com.beiwo.klyjaz.util.PopUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.activity
 * @descripe 动态详情页
 * @time 2018/10/29 11:35
 */
public class ForumDetailActivity extends BaseComponentActivity implements ForumDetailContact.View,OnRefreshListener,View.OnClickListener{

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

    private ForumDetailContact.Presenter mPresenter;

    private ForumDetailAdapter mAdapter;
    private ForumHelper forumHelper;
    private int pageNo = 1;
    private int pageSize = 10000;
    private String forumId;
    private List<CommentReplyBean> commentLists;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forum_detail;
    }

    @Override
    public void configViews() {
        mAdapter = new ForumDetailAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(this);

        mPresenter = new ForumDetailPresenter(this,this);
        forumHelper = new ForumHelper(this);
        mAdapter.addHeaderView(forumHelper.initHead(recyclerView));
        mAdapter.addFooterView(forumHelper.initFoot(recyclerView,this));
    }

    @Override
    public void initDatas() {
        Intent intent = getIntent();
        this.forumId = intent.getStringExtra("forumId");
        fetchData();
    }

    private void fetchData(){
        String userId = "";
        if (UserHelper.getInstance(this).isLogin()) {
            userId = UserHelper.getInstance(this).getProfile().getId();
        }
        mPresenter.queryForumInfo(userId,forumId,pageNo,pageSize);
        mPresenter.queryCommentList(forumId,pageNo, pageSize);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onQueryForumInfoSucceed(ForumInfoBean forumBean) {
        refreshLayout.finishRefresh();
        forumHelper.updateHeadDatas(forumBean.getForum());
        forumHelper.updateFootDatas(forumBean.getForum());
    }

    @Override
    public void onQueryCommentSucceed(List<CommentReplyBean> list) {
        refreshLayout.finishRefresh();
        this.commentLists = list;
        if(list != null){
            if(list.size() > 3){
                mAdapter.addData(list.subList(0,3));
            }else{
                mAdapter.addData(list);
            }
        }
    }

    @Override
    public void onReplyCommentSucceed() {

    }

    @Override
    public void onSaveReportSucceed() {

    }

    @Override
    public void onCancelForumSucceed() {

    }

    @Override
    public void onCancelReplySucceed() {

    }

    @Override
    public void onPraiseSucceed() {

    }

    @Override
    public void onCancelPraiseSucceed() {

    }

    @Override
    public void setPresenter(ForumDetailContact.Presenter presenter) {
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        fetchData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.foot:
//                ToastUtil.toast("footer");
//                DialogUtils.showBottomListWindow(R.layout.dialog_article_comment_list, getSupportFragmentManager(), this, this,commentLists);
//                DialogUtils.showBottomListWindow(R.layout.dialog_article_comment_list, getSupportFragmentManager(), this, this,commentLists);

//                DialogUtils.showCommentPopWindow(R.layout.dialog_comment_input, getSupportFragmentManager(), this,  this);
                new CommentDialog("优质评论将会被优先展示", new CommentDialog.SendListener() {
                    @Override
                    public void sendComment(String inputText) {
                        ToastUtil.toast(inputText);
                    }
                }).show(getSupportFragmentManager(), "comment");
                break;
                default:
                    break;
        }
    }
}
