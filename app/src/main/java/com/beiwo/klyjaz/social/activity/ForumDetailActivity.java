package com.beiwo.klyjaz.social.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.beiwo.klyjaz.social.inter.OnChildViewClickListener;
import com.beiwo.klyjaz.social.presenter.ForumDetailPresenter;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
public class ForumDetailActivity extends BaseComponentActivity implements ForumDetailContact.View,OnRefreshListener,
        View.OnClickListener,BaseQuickAdapter.OnItemChildClickListener,OnChildViewClickListener{

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
    private ForumInfoBean forumBean;
    private List<CommentReplyBean> commentLists;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forum_detail;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolBar).statusBarDarkFont(true).init();
        mAdapter = new ForumDetailAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(this);

        mPresenter = new ForumDetailPresenter(this,this);
        forumHelper = new ForumHelper(this);
        mAdapter.addHeaderView(forumHelper.initHead(recyclerView,this));
        mAdapter.addFooterView(forumHelper.initFoot(recyclerView,this));
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnChildViewClickListener(this);
    }

    @Override
    public void initDatas() {
        Intent intent = getIntent();
        this.forumId = intent.getStringExtra("forumId");
        fetchData();
    }

    private void fetchData(){
        mPresenter.queryForumInfo(forumId,pageNo,pageSize);
        mPresenter.queryCommentList(forumId,pageNo, pageSize);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onQueryForumInfoSucceed(ForumInfoBean forumBean) {
        this.forumBean = forumBean;
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
    public void setPresenter(ForumDetailContact.Presenter presenter) {
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        fetchData();
    }

    @Override
    public void onClick(View v) {
        if (!UserHelper.getInstance(this).isLogin()) {
            DlgUtil.loginDlg(this, null);
            return;
        }
        switch (v.getId()){
            case R.id.iv_comment:
                page2CommentActivity(0,0,0);
                break;
            case R.id.foot:
                page2CommentActivity(1,0,0);
                break;
                default:
                    break;
        }
    }

    private void page2CommentActivity(int type,int position,int childPosition){
        Intent intent = new Intent(this, ForumCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("forumId",forumId);
        bundle.putInt("type",type);
        bundle.putInt("position",position);
        bundle.putInt("childPosition",childPosition);
        bundle.putSerializable("list", (Serializable) commentLists);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_bottom_enter, R.anim.anim_bottom_exit);
    }

    private void page2PersonalActivity(String userId){
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
        switch (view.getId()){
            case R.id.iv_article_comment:
                page2CommentActivity(2,position,0);
                break;
            case R.id.tv_comment_delete:
                ToastUtil.toast("delete"+position);
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
        switch (v.getId()){
            case R.id.iv_commentator_avatar:
                page2PersonalActivity(replyBean.getUserId());
                break;
            case R.id.iv_article_comment:
                page2CommentActivity(3,position,childPosition);
                break;
            case R.id.tv_comment_delete:
                ToastUtil.toast("删除");
                break;
            default:
                break;
        }
    }
}
