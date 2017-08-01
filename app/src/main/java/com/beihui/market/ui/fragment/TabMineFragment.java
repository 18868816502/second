package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabMineComponent;
import com.beihui.market.injection.module.TabMineModule;
import com.beihui.market.ui.activity.HelperAndFeedbackActivity;
import com.beihui.market.ui.activity.InvitationActivity;
import com.beihui.market.ui.activity.MessageCenterActivity;
import com.beihui.market.ui.activity.SettingsActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.UserProfileActivity;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.ui.contract.TabMineContract;
import com.beihui.market.ui.presenter.TabMinePresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class TabMineFragment extends BaseTabFragment implements TabMineContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.avatar)
    CircleImageView avatarIv;
    @BindView(R.id.user_name)
    TextView userNameTv;
    @BindView(R.id.login)
    TextView loginTv;

    @BindView(R.id.has_message)
    TextView hasMessageTv;

    @Inject
    TabMinePresenter presenter;


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
    public void onLogout(UserLogoutEvent event) {
        loginTv.setVisibility(View.VISIBLE);
        userNameTv.setVisibility(View.GONE);

        Glide.with(this)
                .load(R.mipmap.mine_head_icon)
                .into(avatarIv);

        if (event.pendingAction != null && event.pendingAction.equals(UserLogoutEvent.ACTION_START_LOGIN)
                && getView() != null) {
            getView().post(new Runnable() {
                @Override
                public void run() {
                    navigateLogin();
                }
            });
        }
    }

    @OnClick({R.id.avatar, R.id.mine_msg, R.id.invite_friend, R.id.helper_feedback, R.id.settings, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                presenter.checkUserProfile();
                break;
            case R.id.mine_msg:
                presenter.checkMessage();
                break;
            case R.id.invite_friend:
                presenter.checkInvitation();
                break;
            case R.id.helper_feedback:
                presenter.checkHelpAndFeedback();
                break;
            case R.id.settings:
                presenter.checkSetting();
                break;
            case R.id.login:
                navigateLogin();
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
    public void showHasMessage(boolean hasMessage) {
        hasMessageTv.setSelected(hasMessage);
    }

    @Override
    public void navigateLogin() {
        UserAuthorizationActivity.launch(getContext(), getView());
    }

    @Override
    public void navigateUserProfile(String userId) {
        Intent toUserProfile = new Intent(getActivity(), UserProfileActivity.class);
        startActivity(toUserProfile);
    }

    @Override
    public void navigateMessage(String userId) {
        Intent toMsg = new Intent(getActivity(), MessageCenterActivity.class);
        startActivity(toMsg);
    }

    @Override
    public void navigateInvitation(String userId) {
        Intent toInviteFriend = new Intent(getActivity(), InvitationActivity.class);
        startActivity(toInviteFriend);
    }

    @Override
    public void navigateHelpAndFeedback(String userId) {
        Intent toHelp = new Intent(getActivity(), HelperAndFeedbackActivity.class);
        startActivity(toHelp);
    }

    @Override
    public void navigateSetting(String userId) {
        Intent toSettings = new Intent(getActivity(), SettingsActivity.class);
        startActivity(toSettings);
    }
}
