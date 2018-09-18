package com.beihui.market.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.EventBean;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabMineComponent;
import com.beihui.market.injection.module.TabMineModule;
import com.beihui.market.tang.activity.WalletActivity;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.activity.ArticleDetailActivity;
import com.beihui.market.ui.activity.BillSummaryActivity;
import com.beihui.market.ui.activity.CollectionActivity;
import com.beihui.market.ui.activity.H5Activity;
import com.beihui.market.ui.activity.HelpAndFeedActivity;
import com.beihui.market.ui.activity.InvitationWebActivity;
import com.beihui.market.ui.activity.PersonalCenterActivity;
import com.beihui.market.ui.activity.RemindActivity;
import com.beihui.market.ui.activity.RewardPointActivity;
import com.beihui.market.ui.activity.SettingsActivity;
import com.beihui.market.ui.activity.SysMsgActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.UserProfileActivity;
import com.beihui.market.ui.adapter.DeployAdapter;
import com.beihui.market.ui.contract.TabMineContract;
import com.beihui.market.ui.presenter.TabMinePresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalFragment extends BaseTabFragment implements TabMineContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.avatar)
    CircleImageView avatarIv;
    @BindView(R.id.user_name)
    TextView userNameTv;
    @BindView(R.id.tv_user_info)
    TextView userInfo;
    @BindView(R.id.login)
    TextView loginTv;
    @BindView(R.id.tv_message_num)
    TextView tvMessageNum;
    @Inject
    TabMinePresenter presenter;

    @BindView(R.id.activity_deploy_recycler)
    RecyclerView deployRecyclerView;

    private DeployAdapter deployAdapter;

    private List<EventBean> list = new ArrayList<>();

    private String pendingPhone;
    MyRecevier myRecevier = new MyRecevier();

    private String title;

    private String webViewUrl;

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Statistic.onEvent(Events.ENTER_MINE_PAGE);
        if (!EventBus.getDefault().isRegistered(getActivity())) {
            EventBus.getDefault().register(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onStart();
    }


    @Override
    public void onDestroy() {
        presenter.onDestroy();
        if (EventBus.getDefault().isRegistered(getActivity())) {
            EventBus.getDefault().unregister(getActivity());
        }
        if (myRecevier != null) {
            getActivity().unregisterReceiver(myRecevier);
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_personal_layout;
    }

    @Override
    public void configViews() {
        deployAdapter = new DeployAdapter(R.layout.activity_deploy_list_item, list, getActivity());
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        deployRecyclerView.setLayoutManager(linearLayout);
        deployRecyclerView.setAdapter(deployAdapter);

        deployAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.size() >= position && list.get(position) != null) {
                    title = list.get(position).getTitle().split("#")[0];
                    webViewUrl = list.get(position).getUrl();
                    presenter.clickKaolaGroup();
                }
            }
        });
    }

    @Override
    public void initDatas() {
        request();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("logout");
        if (getActivity() != null) {
            getActivity().registerReceiver(myRecevier, intentFilter);
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerTabMineComponent.builder()
                .appComponent(appComponent)
                .tabMineModule(new TabMineModule(this))
                .build()
                .inject(this);
    }

    private void request() {
        Api.getInstance().isShowOwnerActive("4", 1)
                .compose(RxResponse.<List<EventBean>>compatT())
                .subscribe(new ApiObserver<List<EventBean>>() {
                    @Override
                    public void onNext(List<EventBean> data) {
                        list = data;
                        deployAdapter.setNewData(data);
                    }
                });
    }

    @Override
    public void showProfile(UserHelper.Profile profile) {
        loginTv.setVisibility(View.GONE);
        userNameTv.setVisibility(View.VISIBLE);
        if (UserHelper.getInstance(getActivity()).isLogin()) {
            userInfo.setText("查看并编辑资料");
        } else {
            userInfo.setText("立即登录，开启记账旅程！");
        }
        if (profile.getHeadPortrait() != null) {
            Glide.with(getActivity())
                    .load(profile.getHeadPortrait())
                    .asBitmap()
                    .into(avatarIv);
        }
        String username = profile.getUserName();
        if (username != null) {
            if (LegalInputUtils.validatePhone(username)) {
                userNameTv.setText(CommonUtils.phone2Username(username));
            } else {
                userNameTv.setText(username);
            }
        }
    }

    @Override
    public void showRewardPoints(int points) {
    }

    @Override
    public void setPresenter(TabMineContract.Presenter presenter) {
    }

    @OnClick({R.id.bill_summary, R.id.my_wallet,
            R.id.remind, R.id.login, R.id.avatar, R.id.ll_navigate_user_profile,
            R.id.invite_friend, R.id.help_center, R.id.settings, R.id.mine_msg})
    public void onViewClicked(View view) {
        if (UserHelper.getInstance(getActivity()).getProfile() == null || UserHelper.getInstance(getActivity()).getProfile().getId() == null) {
            UserAuthorizationActivity.launch(getActivity(), null);
            return;
        }
        switch (view.getId()) {
            case R.id.bill_summary:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickMineBill();
                }
                break;
            case R.id.remind:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickRemind();
                }
                break;
            //登录
            case R.id.login:
                if (!FastClickUtils.isFastClick()) {
                    navigateLogin();
                }
                break;
            //点击头像 如果未登陆 会跳转到登陆页面
            case R.id.avatar:
//                if (!FastClickUtils.isFastClick()) {
//                    presenter.clickUserProfile();
//                }
                startActivity(new Intent(getActivity(), PersonalCenterActivity.class));
                break;
            case R.id.ll_navigate_user_profile:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickUserProfile();
                }
                break;
            case R.id.invite_friend:
                Statistic.onEvent(Events.MINE_CLICK_INVITATION);
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickInvitation();
                }
                break;
            case R.id.help_center:
                Statistic.onEvent(Events.MINE_CLICK_HELP_FEEDBACK);
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickHelpAndFeedback();
                }
                break;
            case R.id.settings:
                Statistic.onEvent(Events.MINE_CLICK_SETTING);
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickSetting();
                }
                break;
            case R.id.mine_msg:
                Statistic.onEvent(Events.MINE_CLICK_MESSAGE);
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickMessage();
                }
                break;
            case R.id.my_wallet://我的钱包
                if (!FastClickUtils.isFastClick())
                    startActivity(new Intent(getActivity(), WalletActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 登录事件
     */
    @Override
    public void navigateLogin() {
        if (pendingPhone != null) {
            UserAuthorizationActivity.launch(getActivity(), pendingPhone);
            pendingPhone = null;
        } else {
            UserAuthorizationActivity.launch(getActivity(), null);
        }
    }

    /**
     * 个人中心页面
     *
     * @param userId 用户id
     */
    @Override
    public void navigateUserProfile(String userId) {
        startActivity(new Intent(getActivity(), UserProfileActivity.class));
    }

    /**
     * 直接进入系统消息 抛弃公告的选择
     *
     * @param userId 用户id
     */
    @Override
    public void navigateRemind(String userId) {
        Intent intent = new Intent(getActivity(), RemindActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到我的账单
     *
     * @param userId 用户id
     */
    @Override
    public void navigateMineBill(String userId) {
        startActivity(new Intent(getActivity(), BillSummaryActivity.class));
    }

    @Override
    public void navigateCollection(String userId) {
        startActivity(new Intent(getActivity(), CollectionActivity.class));
    }

    @Override
    public void navigateRewardPoints() {
        startActivity(new Intent(getActivity(), RewardPointActivity.class));
    }

    @Override
    public void navigateInvitation(String userId) {
        startActivity(new Intent(getActivity(), InvitationWebActivity.class));
    }

    @Override
    public void navigateKaolaGroup(String userId, String userName) {
        if (title.equals("邀请好友")) {
            startActivity(new Intent(getActivity(), InvitationWebActivity.class));
        } else {
            Intent intent = new Intent(getActivity(), H5Activity.class);
            intent.putExtra("webViewUrl", webViewUrl);
            intent.putExtra("title", title);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void navigateHelpAndFeedback(String userId) {
        startActivity(new Intent(getActivity(), HelpAndFeedActivity.class));
    }

    /**
     * 进入设置页面
     *
     * @param userId 用户id
     */
    @Override
    public void navigateSetting(String userId) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    /**
     * 是否显示我的账单按钮
     *
     * @param visible 是否显示
     */
    @Override
    public void updateMyLoanVisible(boolean visible) {
    }

    /**
     * 直接进入系统消息 抛弃公告的选择
     *
     * @param userId 用户id
     */
    @Override
    public void navigateMessage(String userId) {
        Intent intent = new Intent(getActivity(), SysMsgActivity.class);
        startActivity(intent);
    }

    /**
     * 消息数量
     */
    @Override
    public void updateMessageNum(String data) {
        if (TextUtils.isEmpty(data)) {
            tvMessageNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(data) > 9) {
            tvMessageNum.setVisibility(View.VISIBLE);
            tvMessageNum.setText("9+");
        } else if (Integer.parseInt(data) <= 0) {
            tvMessageNum.setVisibility(View.GONE);
        } else {
            tvMessageNum.setVisibility(View.VISIBLE);
            tvMessageNum.setText(data);
        }
    }

    class MyRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("logout")) {
                    userNameTv.setText("请登录");
                    userInfo.setText("立即登录，开启记账旅程！");
                    Glide.with(getActivity())
                            .load(R.drawable.mine_icon_head)
                            .asBitmap()
                            .into(avatarIv);
                }
            }
        }
    }
}