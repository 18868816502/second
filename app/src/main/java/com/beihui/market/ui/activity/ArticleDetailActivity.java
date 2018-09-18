package com.beihui.market.ui.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.UserArticleBean;
import com.beihui.market.entity.UserInfoBean;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerArticleDetailComponent;
import com.beihui.market.injection.module.ArticleDetailModule;
import com.beihui.market.ui.adapter.ArticleCommentListAdapter;
import com.beihui.market.ui.adapter.ArticleDetailAdapter;
import com.beihui.market.ui.contract.ArticleDetailContact;
import com.beihui.market.ui.presenter.ArticleDetailPresenter;
import com.beihui.market.util.KeyBoardUtils;
import com.beihui.market.util.PopUtils;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.dialog.PopDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
        View.OnClickListener,OnRefreshListener, OnLoadMoreListener,ArticleDetailAdapter.OnViewClickListener,
        PopDialog.OnInitPopListener,PopDialog.OnDismissListener{

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
    private EditText etInput;
    private FragmentManager fManager;
    /**
     * 0:关注 1:删除 2:其它用户更多（举报）3：自己用户更多（删除） 4:评论弹窗列表  5.评论输入框弹窗
     */
    private int mPopType = 0;
    private TextView tvCommentTitle;

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
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerArticleDetailComponent.builder()
                .appComponent(appComponent)
                .articleDetailModule(new ArticleDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onQueryArticleDetailSucceed(UserInfoBean userInfoBean) {

    }

    @Override
    public void onQueryArticleCommentSucceed(List<UserArticleBean> list) {

    }

    @Override
    public void setPresenter(ArticleDetailContact.Presenter presenter) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigate:
                finish();
                break;
            case R.id.iv_more:
                //判断是否是自己的文章
                mPopType = 2;
                PopUtils.showBottomPopWindow(R.layout.dialog_article_other_more, fManager,this,this);

//                mPopType = 3;
//                PopUtils.showBottomPopWindow(R.layout.dialog_article_mine_more, fManager,this,this);
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
                PopUtils.showCenterPopWindow(R.layout.dialog_article_delete,fManager,this,this);
                break;
            case R.id.report01:
                hideDialog();
                ToastUtil.toast(getString(R.string.article_more_report_content1));
                break;
            case R.id.report02:
                hideDialog();
                ToastUtil.toast(getString(R.string.article_more_report_content2));
                break;
            case R.id.report03:
                hideDialog();
                ToastUtil.toast(getString(R.string.article_more_report_content3));
                break;
            case R.id.report04:
                hideDialog();
                ToastUtil.toast(getString(R.string.article_more_report_content4));
                break;
            case R.id.iv_close:
                hideDialog();
                break;
            case R.id.tv_comment:
                mPopType = 5;
                PopUtils.showCommentPopWindow(R.layout.dialog_comment_input,fManager,this,this,this);
                break;
            case R.id.tv_send:
                if(TextUtils.isEmpty(etInput.getText().toString())){
                    ToastUtil.toast(getString(R.string.article_comment_input_tips));
                    return;
                }
                ToastUtil.toast(etInput.getText().toString());
                break;
                default:
                    break;
        }
    }

    /**
     * 弹窗确定按钮的不同作用
     */
    private void requestEnsure(){
        switch (mPopType){
            case 0:
                ToastUtil.toast("关注");
                break;
            case 1:
                ToastUtil.toast("删除");
                break;
            case 2:

                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onViewClick(TextView view,int type) {
        switch (type){
            case 0:
                mPopType = 0;
                PopUtils.showCenterPopWindow(R.layout.dialog_article_attention,fManager,this,this);
                break;
            case 1:
                mPopType = 4;
                PopUtils.showBottomPopWindow(R.layout.dialog_article_comment_list,fManager,this,this);
                break;
            case 2:
                mPopType = 5;
                PopUtils.showCommentPopWindow(R.layout.dialog_comment_input,fManager,this,this,this);
                break;
                default:
                    break;
        }

    }

    /**
     * 隐藏弹窗
     */
    private void hideDialog(){
        PopUtils.dismiss();
    }

    @Override
    public void initPop(View view, PopDialog mPopDialog) {
        switch (mPopType){
                /*关注*/
            case 0:
                /*删除*/
            case 1:
                setOnClick(view.findViewById(R.id.tv_cancel),view.findViewById(R.id.tv_save));
                break;
                /*别人的文章（举报）*/
            case 2:
                setOnClick(view.findViewById(R.id.report01),view.findViewById(R.id.report02),
                        view.findViewById(R.id.report03),view.findViewById(R.id.report04),
                        view.findViewById(R.id.tv_cancel));
                break;
                /*自己的文章（删除）*/
            case 3:
                setOnClick(view.findViewById(R.id.tv_delete),view.findViewById(R.id.tv_cancel));
                break;
                /*评论列表弹出框*/
            case 4:
                setOnClick(view.findViewById(R.id.iv_close),view.findViewById(R.id.tv_comment));
                tvCommentTitle = view.findViewById(R.id.tv_comment_title);
                RecyclerView itemRecycler = view.findViewById(R.id.comment_recycler);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                itemRecycler.setLayoutManager(manager);
                ArticleCommentListAdapter adapter = new ArticleCommentListAdapter(this);
                itemRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
                /*评论输入框*/
            case 5:
                etInput = view.findViewById(R.id.et_comment);
                etInput.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        KeyBoardUtils.toggleKeybord(etInput);
                    }
                },300);
                setOnClick(view.findViewById(R.id.tv_send));
                break;
                default:
                    break;
        }

    }

    private void setOnClick(View ...views){
        for(View view:views){
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onDismiss(PopDialog mPopDialog) {
        KeyBoardUtils.toggleKeyboard(ArticleDetailActivity.this);
    }
}
