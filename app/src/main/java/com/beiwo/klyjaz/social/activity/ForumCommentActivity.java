package com.beiwo.klyjaz.social.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.social.adapter.ForumCommentAdapter;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.contract.ForumCommentContact;
import com.beiwo.klyjaz.social.dialog.CommentDialog;
import com.beiwo.klyjaz.social.presenter.ForumCommentPresenter;
import com.beiwo.klyjaz.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForumCommentActivity extends BaseCommentActivity implements ForumCommentContact.View {

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_forum_comment;
    }

    @Override
    public void configViews() {
        mAdapter = new ForumCommentAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        mPresenter = new ForumCommentPresenter(this,this);
        forumId = getIntent().getStringExtra("forumId");
    }

    @Override
    public void initDatas() {
        fetchData();
    }

    /**
     * 获取评论数据
     */
    private void fetchData(){
        mPresenter.queryCommentList(forumId,1, 10000);
    }

    @OnClick({R.id.iv_close,R.id.tv_comment})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_comment:
                new CommentDialog("在这里畅所欲言吧", new CommentDialog.SendListener() {
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

    @Override
    public void onQueryCommentSucceed(List<CommentReplyBean> list) {
        if(list == null || list.size() == 0){
            recyclerView.setVisibility(View.GONE);
            emptyContainer.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyContainer.setVisibility(View.GONE);
            mAdapter.setDatas(list);
        }
    }

    @Override
    public void onReplyCommentSucceed() {

    }

    @Override
    public void onCancelReplySucceed() {

    }

    @Override
    public void setPresenter(ForumCommentContact.Presenter presenter) {

    }

}
