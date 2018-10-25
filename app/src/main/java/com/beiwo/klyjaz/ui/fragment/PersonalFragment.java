package com.beiwo.klyjaz.ui.fragment;

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

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseTabFragment;
import com.beiwo.klyjaz.entity.EventBean;
import com.beiwo.klyjaz.entity.UserProfileAbstract;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerTabMineComponent;
import com.beiwo.klyjaz.injection.module.TabMineModule;
import com.beiwo.klyjaz.jjd.activity.MyBankCardActivity;
import com.beiwo.klyjaz.jjd.activity.MyLoanActivity;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.activity.WalletActivity;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.BillSummaryActivity;
import com.beiwo.klyjaz.ui.activity.H5Activity;
import com.beiwo.klyjaz.ui.activity.HelpAndFeedActivity;
import com.beiwo.klyjaz.ui.activity.InvitationWebActivity;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.ui.activity.RemindActivity;
import com.beiwo.klyjaz.ui.activity.SettingsActivity;
import com.beiwo.klyjaz.ui.activity.SysMsgActivity;
import com.beiwo.klyjaz.ui.activity.UserProfileActivity;
import com.beiwo.klyjaz.ui.adapter.DeployAdapter;
import com.beiwo.klyjaz.ui.contract.TabMineContract;
import com.beiwo.klyjaz.ui.presenter.TabMinePresenter;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.FastClickUtils;
import com.beiwo.klyjaz.util.LegalInputUtils;
import com.beiwo.klyjaz.view.CircleImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.dctv_loan)
    View loanContainer;
    @BindView(R.id.dctv_bank)
    View bankContainer;
    @Inject
    TabMinePresenter presenter;

    @BindView(R.id.activity_deploy_recycler)
    RecyclerView deployRecyclerView;
    @BindView(R.id.my_wallet)
    View my_wallet;

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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieve(String msg) {
        if (TextUtils.equals("1", msg)) {
            System.out.println("personal fregment 1");
            request();
            loginTv.setVisibility(View.GONE);
            userNameTv.setVisibility(View.VISIBLE);
            userInfo.setText("查看并编辑资料");
            UserHelper.Profile profile = UserHelper.getInstance(getActivity()).getProfile();
            if (profile != null) {
                Glide.with(getActivity()).load(profile.getHeadPortrait()).error(R.drawable.mine_icon_head).into(avatarIv);
                String username = profile.getUserName();
                if (username != null) {
                    if (LegalInputUtils.validatePhone(username)) {
                        userNameTv.setText(CommonUtils.phone2Username(username));
                    } else {
                        userNameTv.setText(username);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
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
        my_wallet.setVisibility(App.audit == 2 ? View.VISIBLE : View.GONE);
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
                    .error(R.drawable.mine_icon_head)
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

    @OnClick({R.id.bill_summary, R.id.my_wallet, R.id.remind, R.id.login,
            R.id.avatar, R.id.ll_navigate_user_profile, R.id.invite_friend,
            R.id.help_center, R.id.settings, R.id.mine_msg, R.id.dctv_loan, R.id.dctv_bank})
    public void onViewClicked(View view) {
        if (!UserHelper.getInstance(getActivity()).isLogin()) {
            goLogin();
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
            case R.id.login://登录
                if (!FastClickUtils.isFastClick()) {
                    navigateLogin();
                }
                break;
            //点击头像 如果未登陆 会跳转到登陆页面
            case R.id.avatar:
            case R.id.ll_navigate_user_profile:
                Intent intent = new Intent(getActivity(), PersonalCenterActivity.class);
                intent.putExtra("userId", UserHelper.getInstance(getActivity()).getProfile().getId());
                startActivity(intent);
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
            case R.id.dctv_loan:
                startActivity(new Intent(getActivity(), MyLoanActivity.class));
                break;
            case R.id.dctv_bank:
                startActivity(new Intent(getActivity(), MyBankCardActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void navigateLogin() {
        goLogin();
    }

    private void goLogin() {
        DlgUtil.loginDlg(getActivity(), new DlgUtil.OnLoginSuccessListener() {
            @Override
            public void success(UserProfileAbstract data) {
                loginTv.setVisibility(View.GONE);
                userNameTv.setVisibility(View.VISIBLE);
                userInfo.setText("查看并编辑资料");
                if (data.getHeadPortrait() != null) {
                    Glide.with(getActivity())
                            .load(data.getHeadPortrait())
                            .asBitmap()
                            .into(avatarIv);
                }
                String username = data.getUserName();
                if (username != null) {
                    if (LegalInputUtils.validatePhone(username)) {
                        userNameTv.setText(CommonUtils.phone2Username(username));
                    } else {
                        userNameTv.setText(username);
                    }
                }
            }
        });
    }

    /*个人中心页面*/
    @Override
    public void navigateUserProfile(String userId) {
        startActivity(new Intent(getActivity(), UserProfileActivity.class));
    }

    /*直接进入系统消息 抛弃公告的选择*/
    @Override
    public void navigateRemind(String userId) {
        Intent intent = new Intent(getActivity(), RemindActivity.class);
        startActivity(intent);
    }

    /*跳转到我的账单*/
    @Override
    public void navigateMineBill(String userId) {
        startActivity(new Intent(getActivity(), BillSummaryActivity.class));
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

    /*进入设置页面*/
    @Override
    public void navigateSetting(String userId) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    /*是否显示我的账单按钮*/
    @Override
    public void updateMyLoanVisible(boolean visible) {
    }

    /*直接进入系统消息 抛弃公告的选择*/
    @Override
    public void navigateMessage(String userId) {
        Intent intent = new Intent(getActivity(), SysMsgActivity.class);
        startActivity(intent);
    }

    /*消息数量*/
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
                            .into(avatarIv);
                }
            }
        }
    }
}