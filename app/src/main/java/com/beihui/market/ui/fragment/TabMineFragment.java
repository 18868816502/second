package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.base.Constant;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabMineComponent;
import com.beihui.market.injection.module.TabMineModule;
import com.beihui.market.ui.activity.CollectionActivity;
import com.beihui.market.ui.activity.HelperAndFeedbackActivity;
import com.beihui.market.ui.activity.InvitationActivity;
import com.beihui.market.ui.activity.MessageCenterActivity;
import com.beihui.market.ui.activity.MyDebtActivity;
import com.beihui.market.ui.activity.RewardPointActivity;
import com.beihui.market.ui.activity.SettingsActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.UserProfileActivity;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.ui.contract.TabMineContract;
import com.beihui.market.ui.dialog.WeChatPublicDialog;
import com.beihui.market.ui.presenter.TabMinePresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.SPUtils;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xiaoneng.uiapi.Ntalker;


public class TabMineFragment extends BaseTabFragment implements TabMineContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.avatar)
    CircleImageView avatarIv;
    @BindView(R.id.user_name)
    TextView userNameTv;
    @BindView(R.id.login)
    TextView loginTv;
    @BindView(R.id.mine_product_container)
    View mineProductContainer;
    @BindView(R.id.points)
    TextView points;
    @BindView(R.id.wechat_surprise)
    View wechatSurpriseView;

    @Inject
    TabMinePresenter presenter;

    private String pendingPhone;

    public static TabMineFragment newInstance() {
        return new TabMineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //umeng统计
        Statistic.onEvent(Events.ENTER_MINE_PAGE);

        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onStart();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_mine;
    }

    @Override
    public void configViews() {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        wechatSurpriseView.setVisibility(SPUtils.getWechatSurpriseClicked(getContext()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerTabMineComponent.builder()
                .appComponent(appComponent)
                .tabMineModule(new TabMineModule(this))
                .build()
                .inject(this);
    }

    @Subscribe
    public void onLogin(UserLoginEvent event) {
        //登录小能客服
        UserHelper.Profile profile = UserHelper.getInstance(getContext()).getProfile();
        Ntalker.getBaseInstance().login(profile.getId(), profile.getUserName());
    }

    @Subscribe
    public void onLogout(UserLogoutEvent event) {
        //登出小能客服
        Ntalker.getBaseInstance().logout();

        loginTv.setVisibility(View.VISIBLE);
        userNameTv.setVisibility(View.GONE);
        points.setVisibility(View.GONE);

        Glide.with(this)
                .load(R.mipmap.mine_head_icon)
                .asBitmap()
                .into(avatarIv);

        pendingPhone = event.pendingPhone;
        if (event.pendingAction != null && event.pendingAction.equals(UserLogoutEvent.ACTION_START_LOGIN)
                && getView() != null) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateLogin();
                }
            }, 100);
        }
    }

    @OnClick({R.id.contact_kefu, R.id.mine_msg,
            R.id.mine_bill,
            R.id.login, R.id.avatar, R.id.user_name,
            R.id.my_collection, R.id.reward_points, R.id.invite_friend,
            R.id.helper_feedback, R.id.settings, R.id.wechat_public})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact_kefu:
                if (!FastClickUtils.isFastClick()) {
                    //umeng统计
                    Statistic.onEvent(Events.CLICK_CONTACT_KEFU);

                    presenter.clickContactKefu();
                }
                break;

            case R.id.mine_msg:
                //umeng统计
                Statistic.onEvent(Events.MINE_CLICK_MESSAGE);

                if (!FastClickUtils.isFastClick()) {
                    presenter.clickMessage();
                }
                break;

            case R.id.login:
                if (!FastClickUtils.isFastClick()) {
                    navigateLogin();
                }
                break;

            case R.id.avatar:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickUserProfile();
                }
                break;

            case R.id.user_name:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickUserProfile();
                }
                break;

            case R.id.mine_bill:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickMineBill();
                }
                break;

            case R.id.my_collection:
                if (!FastClickUtils.isFastClick()) {
                    //umeng统计
                    Statistic.onEvent(Events.CLICK_MY_COLLECTION);

                    presenter.clickCollection();
                }
                break;

            case R.id.reward_points:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickRewardPoints();
                }
                break;


            case R.id.invite_friend:
                //umeng统计
                Statistic.onEvent(Events.MINE_CLICK_INVITATION);

                if (!FastClickUtils.isFastClick()) {
                    presenter.clickInvitation();
                }
                break;

            case R.id.helper_feedback:
                //umeng统计
                Statistic.onEvent(Events.MINE_CLICK_HELP_FEEDBACK);

                if (!FastClickUtils.isFastClick()) {
                    presenter.clickHelpAndFeedback();
                }
                break;

            case R.id.wechat_public:
                //umeng统计
                Statistic.onEvent(Events.CLICK_WECHAT);

                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_WECHAT);

                new WeChatPublicDialog().show(getChildFragmentManager(), WeChatPublicDialog.class.getSimpleName());
                if (wechatSurpriseView.getVisibility() != View.GONE) {
                    wechatSurpriseView.setVisibility(View.GONE);
                    SPUtils.setWechatSurpriseClicked(getContext(), true);
                }
                break;

            case R.id.settings:
                //umeng统计
                Statistic.onEvent(Events.MINE_CLICK_SETTING);

                if (!FastClickUtils.isFastClick()) {
                    presenter.clickSetting();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void setPresenter(TabMineContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showProfile(UserHelper.Profile profile) {
        loginTv.setVisibility(View.GONE);
        userNameTv.setVisibility(View.VISIBLE);
        if (profile.getHeadPortrait() != null) {
            Glide.with(getContext())
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
        this.points.setVisibility(View.VISIBLE);
        String str = points + "  积分";
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#ff395e"));
        SpannableString ss = new SpannableString(str);
        ss.setSpan(span, 0, str.length() - 4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        this.points.setText(ss);
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
    public void navigateMessage(String userId) {
        startActivity(new Intent(getActivity(), MessageCenterActivity.class));
    }

    @Override
    public void navigateMineBill(String userId) {
        startActivity(new Intent(getActivity(), MyDebtActivity.class));
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
        startActivity(new Intent(getActivity(), InvitationActivity.class));
    }

    @Override
    public void navigateContactKefu(String userId, String userName) {
        //调起聊天窗口
        Ntalker.getBaseInstance().startChat(getContext(), Constant.XN_CUSTOMER, getResources().getString(R.string.app_name), null);
    }

    @Override
    public void navigateHelpAndFeedback(String userId) {
        startActivity(new Intent(getActivity(), HelperAndFeedbackActivity.class));
    }

    @Override
    public void navigateSetting(String userId) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    @Override
    public void updateMyLoanVisible(boolean visible) {
//        mineProductContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
        mineProductContainer.setVisibility(View.VISIBLE);
    }
}
