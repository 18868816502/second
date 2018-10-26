package com.beiwo.klyjaz.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.constant.ConstantTag;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerArticleDetailComponent;
import com.beiwo.klyjaz.injection.module.ArticleDetailModule;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.ui.adapter.ArticleCommentListAdapter;
import com.beiwo.klyjaz.ui.adapter.ArticleDetailAdapter;
import com.beiwo.klyjaz.ui.contract.ArticleDetailContact;
import com.beiwo.klyjaz.ui.listeners.LengthTextWatcherListener;
import com.beiwo.klyjaz.ui.listeners.OnViewClickListener;
import com.beiwo.klyjaz.ui.presenter.ArticleDetailPresenter;
import com.beiwo.klyjaz.util.KeyBoardUtils;
import com.beiwo.klyjaz.util.PopUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.beiwo.klyjaz.view.dialog.PopDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.activity
 * @class describe 文章详情
 * @time 2018/9/12 18:55
 */
public class ArticleDetailActivity extends BaseComponentActivity implements ArticleDetailContact.View,
        View.OnClickListener, OnRefreshListener, OnLoadMoreListener,
        PopDialog.OnInitPopListener, PopDialog.OnDismissListener, OnViewClickListener {

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
    @Inject
    ArticleDetailPresenter presenter;

    private ArticleDetailAdapter adapter;
    private ClearEditText etInput;
    private FragmentManager fManager;
    /**
     * 0:关注 1:删除 2:其它用户更多（举报）3：自己用户更多（删除） 4:评论弹窗列表  5.评论输入框弹窗  6.评论审核提示框
     */
    private int mPopType = 0;
    private TextView tvCommentTitle;
    private ForumInfoBean.ForumBean forumBean;
    private String userId;
    private String forumId;
    private int pageNo = 1;
    private int pageSize = 30;

    private List<CommentReplyBean> datas;
    private CommentReplyBean replyBean;
    private CommentReplyBean.ReplyDtoListBean replyDtoListBean;

    private ArticleCommentListAdapter childAdapter;
    private int tag = 1;

    private PopDialog auditDialog;
    private PopDialog commentDialog;
    /**
     * 点击类型 2 回复 3 子回复 4 查看全部
     */
    private int clickType = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_article_detail;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolBar).statusBarDarkFont(true).init();
        fManager = getSupportFragmentManager();

        adapter = new ArticleDetailAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ivNavigate.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        adapter.setOnViewClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void initDatas() {
        datas = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            this.userId = intent.getStringExtra("userId");
            this.forumId = intent.getStringExtra("forumId");
            fetchForumInfo();
        }
    }

    private void fetchForumInfo() {
        String userId = "";
        if (UserHelper.getInstance(this).isLogin()) {
            userId = UserHelper.getInstance(this).getProfile().getId();
        }
        if (!TextUtils.isEmpty(forumId)) {
            presenter.queryForumInfo(userId, forumId, pageNo, pageSize);
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerArticleDetailComponent.builder()
                .appComponent(appComponent)
                .articleDetailModule(new ArticleDetailModule(this))
                .build()
                .inject(this);
    }

    private void fetchData() {
        if (forumBean != null) {
            presenter.queryCommentList(forumBean.getForumId(), pageNo, 10000);
        }
    }

    @Override
    public void setPresenter(ArticleDetailContact.Presenter presenter) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieve(String msg) {
        if (TextUtils.equals("1", msg)) {
            System.out.println("ArticleDetailActivity recieve 1");
            fetchForumInfo();
//            fetchData();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigate:
                finish();
                break;
            case R.id.iv_more:
                if (UserHelper.getInstance(this).isLogin()) {
                    //判断是否是自己的文章
                    if (TextUtils.equals(forumBean.getUserId(), UserHelper.getInstance(this).getProfile().getId())) {
                        mPopType = 3;
                        PopUtils.showBottomPopWindow(R.layout.dialog_article_mine_more, fManager, this, this);
                    } else {
                        mPopType = 2;
                        PopUtils.showBottomPopWindow(R.layout.dialog_article_other_more, fManager, this, this);
                    }
                } else {
                    DlgUtil.loginDlg(this, null);
                }
                break;
            case R.id.tv_cancel:
                hideDialog();
                break;
            case R.id.tv_save:
                hideDialog();
                requestEnsure();
                break;
            case R.id.tv_delete:
                mPopType = 1;
                hideDialog();
                PopUtils.showCenterPopWindow(R.layout.dialog_article_delete, fManager, this, this);
                break;
            case R.id.report01:
                hideDialog();
                presenter.fetchSaveReport("", forumBean.getForumId(), "1", getString(R.string.article_more_report_content1));
                break;
            case R.id.report02:
                hideDialog();
                presenter.fetchSaveReport("", forumBean.getForumId(), "1", getString(R.string.article_more_report_content2));
                break;
            case R.id.report03:
                hideDialog();
                presenter.fetchSaveReport("", forumBean.getForumId(), "1", getString(R.string.article_more_report_content3));
                break;
            case R.id.report04:
                hideDialog();
                presenter.fetchSaveReport("", forumBean.getForumId(), "1", getString(R.string.article_more_report_content4));
                break;
            case R.id.iv_close:
                if (auditDialog != null) {
                    auditDialog.dismiss();
                }
                PopUtils.dismissComment();
                break;
            case R.id.tv_comment:
                mPopType = 5;
                tag = ConstantTag.TAG_COMMENT_ARTICLE;
                PopUtils.showCommentPopWindow(R.layout.dialog_comment_input, fManager, this, this, this);
                break;
            case R.id.tv_send:
                if (TextUtils.isEmpty(etInput.getText().toString())) {
                    ToastUtil.toast(getString(R.string.article_comment_input_tips));
                    return;
                }
                mPopType = 6;
                PopUtils.showCenterPopWindow(R.layout.dialog_article_comment_audit, fManager, this, this);
                break;
            default:
                break;
        }
    }

    /**
     * 回复/评论
     */
    private void reply() {
        switch (tag) {
            //动态评论
            case ConstantTag.TAG_COMMENT_ARTICLE:
                presenter.fetchReplyForumInfo("", "1",
                        etInput.getText().toString(), forumBean.getForumId(), "", "", "", "");
                break;
            //评论回复
            case ConstantTag.TAG_COMMENT_OUTSIDE:
            case ConstantTag.TAG_REPLY_COMMENT:
                presenter.fetchReplyForumInfo("", "2",
                        etInput.getText().toString(), forumBean.getForumId(), replyBean.getUserId(), replyBean.getId(), "", "");
                break;
            //子评论回复
            case ConstantTag.TAG_REPLY_OUTSIDE:
            case ConstantTag.TAG_CHILD_REPLY_COMMENT:
                presenter.fetchReplyForumInfo("", "2",
                        etInput.getText().toString(), forumBean.getForumId(), replyDtoListBean.getUserId(), replyBean.getId(), replyDtoListBean.getId(), replyDtoListBean.getContent());
                break;
            default:
                break;
        }
    }

    /**
     * 弹窗确定按钮的不同作用
     */
    private void requestEnsure() {
        switch (mPopType) {
            case 0:
                ToastUtil.toast("关注");
                break;
            case 1:
                ToastUtil.toast("删除");
                presenter.fetchCancelForum(forumBean.getForumId());
                break;
            case 2:
                break;
            case 6:
                PopUtils.dismiss();
                reply();
                PopUtils.dismissComment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        pageNo++;
//        fetchData();
        fetchForumInfo();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        fetchForumInfo();
//        presenter.queryForumInfo(UserHelper.getInstance(this).getProfile().getId(), forumId, pageNo, pageSize);
    }

    @Override
    public void onViewClick(View view, int type, int position) {
        tag = type;
        switch (type) {
            //关注
            case ConstantTag.TAG_ATTENTION:
                mPopType = 0;
                PopUtils.showCenterPopWindow(R.layout.dialog_article_attention, fManager, this, this);
                break;
            //文章点赞
            case ConstantTag.TAG_PARISE_ARTICLE:
                if (forumBean.getIsPraise() == 0) {
                    presenter.fetchClickPraise(0, forumBean.getForumId(), "");
                } else {
                    presenter.fetchCancelPraise(0, forumBean.getForumId(), "");
                }
                break;
            //文章评论
            case ConstantTag.TAG_COMMENT_ARTICLE:
                mPopType = 4;
//                PopUtils.showBottomPopWindow(R.layout.dialog_article_comment_list, fManager, this, this);
                PopUtils.showBottomListWindow(R.layout.dialog_article_comment_list, fManager, this, this);
                break;
            //评论给点赞
            case ConstantTag.TAG_PRAISE_COMMENT:
                replyBean = datas.get(position);
                presenter.fetchClickPraise(2, replyBean.getId(), "");
                break;
            //评论回复
            case ConstantTag.TAG_REPLY_COMMENT:
                replyBean = datas.get(position);
                mPopType = 5;
                PopUtils.showCommentPopWindow(R.layout.dialog_comment_input, fManager, this, this, this);
                break;
            //子评论点赞
            case ConstantTag.TAG_CHILD_PARISE_COMMENT:
                int praisePosition = (int) view.getTag();
                replyDtoListBean = datas.get(praisePosition).getReplyDtoList().get(position);
                presenter.fetchClickPraise(2, replyDtoListBean.getId(), "");
                break;
            //子评论回复
            case ConstantTag.TAG_CHILD_REPLY_COMMENT:
                int comPosition = (int) view.getTag();
                replyBean = datas.get(comPosition);
                replyDtoListBean = datas.get(comPosition).getReplyDtoList().get(position);
                mPopType = 5;
                PopUtils.showCommentPopWindow(R.layout.dialog_comment_input, fManager, this, this, this);
                break;
            //删除评论
            case ConstantTag.TAG_COMMENT_DELETE:
                presenter.fetchCancelReply(datas.get(position).getId());
                break;
            //删除子评论
            case ConstantTag.TAG_CHILD_COMMENT_DELETE:
                int delPosition = (int) view.getTag();
                replyDtoListBean = datas.get(delPosition).getReplyDtoList().get(position);
                presenter.fetchCancelReply(replyDtoListBean.getId());
                break;
            //外层子回复
            case ConstantTag.TAG_REPLY_OUTSIDE:
                int pos = (int) view.getTag();
                replyBean = datas.get(pos);
                replyDtoListBean = datas.get(pos).getReplyDtoList().get(position);
                mPopType = 4;
                clickType = 3;
//                PopUtils.showBottomPopWindow(R.layout.dialog_article_comment_list, fManager, this, this);
                PopUtils.showBottomListWindow(R.layout.dialog_article_comment_list, fManager, this, this);
                break;
            //外层回复
            case ConstantTag.TAG_COMMENT_OUTSIDE:
                replyBean = datas.get(position);
                mPopType = 4;
                clickType = 2;
//                PopUtils.showBottomPopWindow(R.layout.dialog_article_comment_list, fManager, this, this);
                PopUtils.showBottomListWindow(R.layout.dialog_article_comment_list, fManager, this, this);
                break;
            case ConstantTag.TAG_COMMENT_MORE:
                mPopType = 4;
                clickType = 4;
//                PopUtils.showBottomPopWindow(R.layout.dialog_article_comment_list, fManager, this, this);
                PopUtils.showBottomListWindow(R.layout.dialog_article_comment_list, fManager, this, this);
                break;
            default:
                break;
        }
    }

    /*隐藏弹窗*/
    private void hideDialog() {
        PopUtils.dismiss();
    }

    @Override
    public void initPop(View view, PopDialog mPopDialog) {
        switch (mPopType) {
            /*关注*/
            case 0:
                /*删除*/
            case 1:
                setOnClick(view.findViewById(R.id.tv_cancel), view.findViewById(R.id.tv_save));
                break;
            /*别人的文章（举报）*/
            case 2:
                setOnClick(view.findViewById(R.id.report01), view.findViewById(R.id.report02),
                        view.findViewById(R.id.report03), view.findViewById(R.id.report04),
                        view.findViewById(R.id.tv_cancel));
                break;
            /*自己的文章（删除）*/
            case 3:
                setOnClick(view.findViewById(R.id.tv_delete), view.findViewById(R.id.tv_cancel));
                break;
            /*评论列表弹出框*/
            case 4:
                auditDialog = mPopDialog;
                initCommentListPop(view);
//                auditDialog.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//                            PopUtils.dismiss();
//                            return true;
//                        }
//                            return false;
//                    }
//                });
                break;
            /*评论输入框*/
            case 5:
                commentDialog = mPopDialog;
                etInput = view.findViewById(R.id.et_comment);
                etInput.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        KeyBoardUtils.toggleKeybord(etInput);
                    }
                }, 300);
//                etInput.setFocusable(true);
                etInput.addTextChangedListener(new LengthTextWatcherListener(etInput, 100));
                setOnClick(view.findViewById(R.id.tv_send));
                showHintForEditInput(etInput);
                commentDialog.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                            PopUtils.dismissComment();
                            commentDialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                break;
            case 6:
                setOnClick(view.findViewById(R.id.tv_cancel), view.findViewById(R.id.tv_save));
                break;
            default:
                break;
        }
    }

    /**
     * 显示提示信息
     *
     * @param etInput
     */
    private void showHintForEditInput(ClearEditText etInput) {
        switch (tag) {
            case ConstantTag.TAG_COMMENT_ARTICLE:
                etInput.setHint("在这里畅所欲言吧");
                break;
            case ConstantTag.TAG_REPLY_COMMENT:
            case ConstantTag.TAG_COMMENT_OUTSIDE:
                etInput.setHint("回复@" + replyBean.getUserName());
                break;
            case ConstantTag.TAG_CHILD_REPLY_COMMENT:
            case ConstantTag.TAG_REPLY_OUTSIDE:
                etInput.setHint("回复@" + replyDtoListBean.getUserName());
                break;
            default:
                break;
        }
    }

    private void initCommentListPop(View view) {
        setOnClick(view.findViewById(R.id.iv_close), view.findViewById(R.id.tv_comment));
        View emptyContainer = view.findViewById(R.id.empty_container);
        tvCommentTitle = view.findViewById(R.id.tv_comment_title);
        RecyclerView itemRecycler = view.findViewById(R.id.comment_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        itemRecycler.setLayoutManager(manager);
        childAdapter = new ArticleCommentListAdapter(this);
        itemRecycler.setAdapter(childAdapter);
        tvCommentTitle.setText(String.valueOf("全部" + datas.size() + "条评论"));
        childAdapter.setDatas(datas);
        childAdapter.notifyDataSetChanged();
        childAdapter.setOnViewClickListener(this);
        if (datas.size() == 0) {
            emptyContainer.setVisibility(View.VISIBLE);
            itemRecycler.setVisibility(View.GONE);
        } else {
            emptyContainer.setVisibility(View.GONE);
            itemRecycler.setVisibility(View.VISIBLE);
        }

        if (clickType != 4) {
            mPopType = 5;
            PopUtils.showCommentPopWindow(R.layout.dialog_comment_input, fManager, this, this, this);
        }
    }

    private void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onDismiss(PopDialog mPopDialog) {
//        KeyBoardUtils.toggleKeyboard(ArticleDetailActivity.this);
        PopUtils.dismissComment();
    }

    @Override
    public void onQueryForumInfoSucceed(ForumInfoBean forumBean) {
        this.forumBean = forumBean.getForum();
        fetchData();
    }

    @Override
    public void onQueryCommentSucceed(List<CommentReplyBean> list) {
        if (pageNo == 1) {
            refreshLayout.finishRefresh();
            this.datas.clear();
            this.datas = list;
            adapter.setDatas(datas, forumBean);
        } else {
            refreshLayout.finishLoadMore();
            this.datas.addAll(list);
            adapter.setDatas(datas, forumBean);
        }
        if (tvCommentTitle != null) {
            tvCommentTitle.setText(String.valueOf("全部" + datas.size() + "条评论"));
        }
        if (childAdapter != null) {
            childAdapter.setDatas(datas);
        }
    }

    @Override
    public void onReplyCommentSucceed() {
        ToastUtil.toast("已提交审核请耐心等待");
    }

    @Override
    public void onSaveReportSucceed() {
        ToastUtil.toast("举报成功，我们尽快处理！");
    }

    @Override
    public void onCancelForumSucceed() {
        ToastUtil.toast("删除成功");
        finish();
    }

    @Override
    public void onCancelReplySucceed() {
        ToastUtil.toast("删除成功");
        fetchForumInfo();
//        fetchData();
    }

    @Override
    public void onPraiseSucceed() {
        switch (tag) {
            //话题点赞
            case ConstantTag.TAG_PARISE_ARTICLE:
                forumBean.setIsPraise(1);
                forumBean.setPraiseCount(forumBean.getPraiseCount() + 1);
                break;
            //评论给点赞
            case ConstantTag.TAG_PRAISE_COMMENT:
                break;
            //子评论点赞
            case ConstantTag.TAG_CHILD_PARISE_COMMENT:
                break;
            default:
                break;
        }
        adapter.setDatas(datas, forumBean);
    }

    @Override
    public void OnCancelPraiseSucceed() {
        switch (tag) {
            //话题点赞
            case ConstantTag.TAG_PARISE_ARTICLE:
                forumBean.setIsPraise(0);
                forumBean.setPraiseCount(forumBean.getPraiseCount() - 1);
                break;
            //评论给点赞
            case ConstantTag.TAG_PRAISE_COMMENT:
                break;
            //子评论点赞
            case ConstantTag.TAG_CHILD_PARISE_COMMENT:
                break;
            default:
                break;
        }
        adapter.setDatas(datas, forumBean);
    }
}