package com.beiwo.klyjaz.scdk.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseTabFragment;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.injection.component.DaggerTabMineComponent;
import com.beiwo.klyjaz.injection.module.TabMineModule;
import com.beiwo.klyjaz.jjd.activity.MyBankCardActivity;
import com.beiwo.klyjaz.jjd.activity.MyLoanActivity;
import com.beiwo.klyjaz.ui.activity.BillSummaryActivity;
import com.beiwo.klyjaz.ui.activity.CollectionActivity;
import com.beiwo.klyjaz.ui.activity.HelpAndFeedActivity;
import com.beiwo.klyjaz.ui.activity.InvitationWebActivity;
import com.beiwo.klyjaz.ui.activity.RemindActivity;
import com.beiwo.klyjaz.ui.activity.RewardPointActivity;
import com.beiwo.klyjaz.ui.activity.SettingsActivity;
import com.beiwo.klyjaz.ui.activity.SysMsgActivity;
import com.beiwo.klyjaz.ui.activity.UserAuthorizationActivity;
import com.beiwo.klyjaz.ui.activity.UserProfileActivity;
import com.beiwo.klyjaz.ui.contract.TabMineContract;
import com.beiwo.klyjaz.ui.presenter.TabMinePresenter;
import com.beiwo.klyjaz.umeng.Events;
import com.beiwo.klyjaz.umeng.Statistic;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.FastClickUtils;
import com.beiwo.klyjaz.util.LegalInputUtils;
import com.beiwo.klyjaz.view.CircleImageView;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/14
 */
public class MineFragment extends BaseTabFragment implements TabMineContract.View {

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

    private String pendingPhone;
    MyRecevier myRecevier = new MyRecevier();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Statistic.onEvent(Events.ENTER_MINE_PAGE);
        if (!EventBus.getDefault().isRegistered(getActivity())) {
            EventBus.getDefault().register(getActivity());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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
        return R.layout.vest_fragment_mine;
    }

    @Override
    public void configViews() {
    }

    @Override
    public void initDatas() {
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

    @OnClick({R.id.my_loan, R.id.bank_card, R.id.login, R.id.avatar, R.id.ll_navigate_user_profile,
            R.id.help_center, R.id.settings, R.id.mine_msg})
    public void onViewClicked(View view) {
        if (UserHelper.getInstance(getActivity()).getProfile() == null || UserHelper.getInstance(getActivity()).getProfile().getId() == null) {
            UserAuthorizationActivity.launch(getActivity(), null);
            return;
        }
        switch (view.getId()) {
            case R.id.my_loan:
                if (!FastClickUtils.isFastClick())
                    startActivity(new Intent(getActivity(), MyLoanActivity.class));
                break;
            case R.id.bank_card:
                if (!FastClickUtils.isFastClick())
                    startActivity(new Intent(getActivity(), MyBankCardActivity.class));
                break;
            //登录
            case R.id.login:
                if (!FastClickUtils.isFastClick()) navigateLogin();
                break;
            //点击头像 如果未登陆 会跳转到登陆页面
            case R.id.avatar:
                if (!FastClickUtils.isFastClick()) presenter.clickUserProfile();
                break;
            case R.id.ll_navigate_user_profile:
                if (!FastClickUtils.isFastClick()) presenter.clickUserProfile();
                break;
            case R.id.help_center:
                Statistic.onEvent(Events.MINE_CLICK_HELP_FEEDBACK);
                if (!FastClickUtils.isFastClick())
                    startActivity(new Intent(getActivity(), HelpAndFeedActivity.class));
                break;
            case R.id.settings:
                Statistic.onEvent(Events.MINE_CLICK_SETTING);
                if (!FastClickUtils.isFastClick())
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.mine_msg:
                Statistic.onEvent(Events.MINE_CLICK_MESSAGE);
                if (!FastClickUtils.isFastClick()) presenter.clickMessage();
                break;
            default:
                break;
        }
    }

    @Override
    public void navigateLogin() {
        if (pendingPhone != null) {
            UserAuthorizationActivity.launch(getActivity(), pendingPhone);
            pendingPhone = null;
        } else {
            UserAuthorizationActivity.launch(getActivity(), null);
        }
    }

    @Override
    public void navigateUserProfile(String userId) {
        startActivity(new Intent(getActivity(), UserProfileActivity.class));
    }

    @Override
    public void navigateRemind(String userId) {
        Intent intent = new Intent(getActivity(), RemindActivity.class);
        startActivity(intent);
    }

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
    }

    @Override
    public void navigateHelpAndFeedback(String userId) {
    }

    @Override
    public void navigateSetting(String userId) {
    }

    @Override
    public void updateMyLoanVisible(boolean visible) {
    }

    @Override
    public void navigateMessage(String userId) {
        Intent intent = new Intent(getActivity(), SysMsgActivity.class);
        startActivity(intent);
    }

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

    public static MineFragment newInstance() {
        return new MineFragment();
    }
}