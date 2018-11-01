package com.beiwo.klyjaz.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.constant.ConstantTag;
import com.beiwo.klyjaz.entity.UserTopicBean;
import com.beiwo.klyjaz.entity.UserInfoBean;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerPersonalCenterComponent;
import com.beiwo.klyjaz.injection.module.PersonalCenterModule;
import com.beiwo.klyjaz.social.activity.MyAuditedTopicActivity;
import com.beiwo.klyjaz.social.activity.MyDraftsActivity;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.social.dialog.CommentDialog;
import com.beiwo.klyjaz.ui.adapter.PersonalCenterAdapter;
import com.beiwo.klyjaz.ui.contract.PersonalCenterContact;
import com.beiwo.klyjaz.ui.listeners.OnItemClickListener;
import com.beiwo.klyjaz.ui.listeners.OnViewClickListener;
import com.beiwo.klyjaz.ui.presenter.PersonalCenterPresenter;
import com.beiwo.klyjaz.util.DensityUtil;
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
    LinearLayout parentView;
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
    private int pageNo = 1;
    private int pageSize = 10;
    private List<UserTopicBean> datas;
    private String userId;

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
        if(getIntent()!=null){
            userId = getIntent().getStringExtra("userId");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.fetchPersonalInfo(userId);
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
                new CommentDialog("优质评论将会被优先展示", new CommentDialog.SendListener() {
                    @Override
                    public void sendComment(String inputText) {
                        Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_SHORT).show();
                    }
                }).show(getSupportFragmentManager(), "comment");
//                finish();
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
        adapter.setHeadData(userInfoBean);
        presenter.fetchPersonalTopic(userId, pageNo, pageSize);
    }

    @Override
    public void onQueryUserTopicSucceed(List<UserTopicBean> list) {
        refreshLayout.finishRefresh();
        if (pageNo == 1) {
            adapter.setDatas(userInfoBean,list);
            datas.clear();
            datas.addAll(list);
        } else {
            refreshLayout.finishLoadMore();
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
        presenter.fetchPersonalInfo(userId);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo++;
        presenter.fetchPersonalTopic(userId, pageNo, pageSize);
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
        intent.putExtra("userId",datas.get(position).getUserId());
        intent.putExtra("forumId",datas.get(position).getForumId());
        startActivity(intent);
    }
}
