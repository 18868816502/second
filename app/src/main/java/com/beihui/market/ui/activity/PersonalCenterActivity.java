package com.beihui.market.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.constant.ConstantTag;
import com.beihui.market.entity.UserTopicBean;
import com.beihui.market.entity.UserInfoBean;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerPersonalCenterComponent;
import com.beihui.market.injection.module.PersonalCenterModule;
import com.beihui.market.social.activity.MyAuditedTopicActivity;
import com.beihui.market.social.activity.MyDraftsActivity;
import com.beihui.market.social.bean.SocialTopicBean;
import com.beihui.market.ui.adapter.PersonalCenterAdapter;
import com.beihui.market.ui.contract.PersonalCenterContact;
import com.beihui.market.ui.listeners.OnItemClickListener;
import com.beihui.market.ui.listeners.OnViewClickListener;
import com.beihui.market.ui.presenter.PersonalCenterPresenter;
import com.beihui.market.util.DensityUtil;
import com.beihui.market.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market
 * @class describe 个人中心页面
 * @time
 */
public class PersonalCenterActivity extends BaseComponentActivity implements PersonalCenterContact.View,
        View.OnClickListener, OnViewClickListener,OnItemClickListener,OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.parent)
    ConstraintLayout parentView;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.navigate)
    ImageView ivBack;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @Inject
    PersonalCenterPresenter presenter;

    private UserInfoBean userInfoBean;
    private PersonalCenterAdapter adapter;
    private PopupWindow popWindow;
    private int pageNo = 0;
    private int pageSize = 10;
    private List<UserTopicBean> datas;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        ivBack.setOnClickListener(this);

        adapter = new PersonalCenterAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnViewClickListener(this);
        adapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void initDatas() {
        datas = new ArrayList<>();
        presenter.fetchPersonalInfo(UserHelper.getInstance(this).getProfile().getId());
//        presenter.fetchPersonalTopic(UserHelper.getInstance(this).getProfile().getId(), pageNo, pageSize);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerPersonalCenterComponent.builder()
                .appComponent(appComponent)
                .personalCenterModule(new PersonalCenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigate:
                finish();
                break;
            case R.id.tv_drafts:
                popWindow.dismiss();
                startActivity(new Intent(this, MyDraftsActivity.class));
                break;
            case R.id.tv_audits:
                popWindow.dismiss();
                startActivity(new Intent(this, MyAuditedTopicActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onQueryUserInfoSucceed(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
        presenter.fetchPersonalTopic(UserHelper.getInstance(this).getProfile().getId(), pageNo, pageSize);
    }

    @Override
    public void onQueryUserTopicSucceed(List<UserTopicBean> list) {
        refreshLayout.setEnableLoadMore(false);
        if (pageNo == 0) {
            adapter.setDatas(userInfoBean,list);
            datas.clear();
            datas.addAll(list);
        } else {
            adapter.appendTopicData(list);
            datas.addAll(list);
        }
    }

    @Override
    public void setPresenter(PersonalCenterContact.Presenter presenter) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        refreshLayout.setEnableRefresh(true);
        presenter.fetchPersonalInfo(UserHelper.getInstance(this).getProfile().getId());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo++;
        refreshLayout.setEnableLoadMore(true);
        presenter.fetchPersonalTopic(UserHelper.getInstance(this).getProfile().getId(), pageNo, pageSize);
    }

    /**
     * 显示切换弹窗
     * @param ivMore 更多控件
     */
    private void showPopWindow(ImageView ivMore) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_personal_more,parentView, false);
        view.findViewById(R.id.tv_drafts).setOnClickListener(this);
        view.findViewById(R.id.tv_audits).setOnClickListener(this);
        popWindow = new PopupWindow(view, DensityUtil.dp2px(this,77), DensityUtil.dp2px(this,75));
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        int[] location = new int[2];
        ivMore.getLocationOnScreen(location);
        setBackgroundbgAlpha(0.5f);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundbgAlpha(1f);
            }
        });
        //在控件V正下方显示
        popWindow.showAsDropDown(ivMore);
//        popWindow.showAtLocation(ivMore, Gravity.BOTTOM, location[0]- 100, location[1]);
    }

    /**
     * 设置窗口背景颜色值
     * @param bgAlpha 透明度
     */
    private void setBackgroundbgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    @Override
    public void onViewClick(View view, int type,int position) {
        switch (type){
            case ConstantTag.TAG_PERSONAL_AVATAR:
            case ConstantTag.TAG_PERSONAL_INFO_EDIT:
                Intent intent = new Intent(this, UserProfileActivity.class);
                intent.putExtra("introduce",userInfoBean.getIntroduce());
                intent.putExtra("sex",userInfoBean.getSex());
                startActivity(intent);
                break;
            case ConstantTag.TAG_PERSONAL_PUBLISH:

                break;
            case ConstantTag.TAG_PERSONAL_ATTENTION:
                break;
            case ConstantTag.TAG_PERSONAL_FANS:
                break;
            case ConstantTag.TAG_PERSONAL_PARISE:
                break;
            case ConstantTag.TAG_PERSONAL_MORE:
                showPopWindow((ImageView) view);
                break;
            case ConstantTag.TAG_PERSONAL_ARTICLE_AVATAR:
                break;
            case ConstantTag.TAG_ARTICLE_PARISE:

                break;
            case ConstantTag.TAG_ARTICLE_COMMENT:

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,ArticleDetailActivity.class);
        SocialTopicBean.ForumBean forumBean = new SocialTopicBean.ForumBean();
        forumBean.setForumId(datas.get(position).getForumId());
        forumBean.setUserId(datas.get(position).getUserId());
        forumBean.setTitle(datas.get(position).getTitle());
        forumBean.setUserHeadUrl(datas.get(position).getUserHeadUrl());
        forumBean.setUserName(datas.get(position).getUserName());
        forumBean.setGmtCreate(datas.get(position).getGmtCreate());
        forumBean.setPicUrl(datas.get(position).getPicUrl());
        forumBean.setContent(datas.get(position).getContent());
        intent.putExtra("topic",forumBean);
        startActivity(intent);
    }
}
