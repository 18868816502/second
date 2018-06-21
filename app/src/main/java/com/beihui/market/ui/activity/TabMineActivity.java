package com.beihui.market.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.base.Constant;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerTabMineComponent;
import com.beihui.market.injection.module.TabMineModule;
import com.beihui.market.ui.activity.CollectionActivity;
import com.beihui.market.ui.activity.HelpAndFeedActivity;
import com.beihui.market.ui.activity.InvitationActivity;
import com.beihui.market.ui.activity.MessageCenterActivity;
import com.beihui.market.ui.activity.MyDebtActivity;
import com.beihui.market.ui.activity.RewardPointActivity;
import com.beihui.market.ui.activity.SettingsActivity;
import com.beihui.market.ui.activity.SysMsgActivity;
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
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.xiaoneng.uiapi.Ntalker;

/**
 * @author xhb
 * 我的 模块 Fragment
 */
public class TabMineActivity extends BaseComponentActivity implements TabMineContract.View {

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
    @BindView(R.id.tv_message_num)
    TextView tvMessageNum;
    @BindView(R.id.wechat_surprise)
    View wechatSurpriseView;

    @Inject
    TabMinePresenter presenter;

    private String pendingPhone;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //umeng统计
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


    @Override
    public void onDestroy() {
        presenter.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_mine;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);

        SlidePanelHelper.attach(this);
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
        UserHelper.Profile profile = UserHelper.getInstance(this).getProfile();
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
                .load(R.mipmap.mine_head)
                .asBitmap()
                .into(avatarIv);

        pendingPhone = event.pendingPhone;
        if (event.pendingAction != null && event.pendingAction.equals(UserLogoutEvent.ACTION_START_LOGIN)
                && toolbar != null) {
            toolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateLogin();
                }
            }, 100);
        }
    }

    @OnClick({R.id.contact_kefu, R.id.mine_msg,
            R.id.mine_bill, R.id.login, R.id.avatar, R.id.ll_navigate_user_profile,
            R.id.my_collection, R.id.reward_points, R.id.invite_friend,
            R.id.helper_feedback, R.id.star_me, R.id.wechat_public})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //我的账单
            case R.id.mine_bill:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickMineBill();
                }
                break;
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
            //登录
            case R.id.login:
                if (!FastClickUtils.isFastClick()) {
                    navigateLogin();
                }
                break;
            //点击头像 如果未登陆 会跳转到登陆页面
            case R.id.avatar:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickUserProfile();
                }
                break;

            case R.id.ll_navigate_user_profile:
                if (!FastClickUtils.isFastClick()) {
                    presenter.clickUserProfile();
                }
                break;
            //我的收藏 被gone
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

                new WeChatPublicDialog().show(getSupportFragmentManager(), WeChatPublicDialog.class.getSimpleName());
//                if (wechatSurpriseView.getVisibility() != View.GONE) {
//                    wechatSurpriseView.setVisibility(View.GONE);
//                    SPUtils.setWechatSurpriseClicked(getContext(), true);
//                }
                break;

            /**
             * 鼓励一下
             */
            case R.id.star_me:
//               String model=android.os.Build.MODEL;
                //品牌
                String brand=android.os.Build.BRAND;
                //制造商
                String manufacturer=android.os.Build.MANUFACTURER;
                Log.e("MANUFACTURER", "MANUFACTURER--> " + manufacturer);
                if ("samsung".equals(manufacturer)) {
                    goToSamsungappsMarket();
                } else {
                    try {
                        Intent toMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationInfo().packageName));
                        startActivity(toMarket);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * https://www.cnblogs.com/qwangxiao/p/8030389.html
     */
    public void goToSamsungappsMarket(){
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" +getApplicationInfo().packageName);
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
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
            Glide.with(this)
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

    /**
     * 登录事件
     */
    @Override
    public void navigateLogin() {
        if (pendingPhone != null) {
            UserAuthorizationActivity.launch(this, pendingPhone);
            pendingPhone = null;
        } else {
            UserAuthorizationActivity.launch(this, null);
        }
    }

    /**
     * 个人中心页面
     * @param userId 用户id
     */
    @Override
    public void navigateUserProfile(String userId) {
        startActivity(new Intent(this, UserProfileActivity.class));
    }

    /**
     * 直接进入系统消息 抛弃公告的选择
     * @param userId 用户id
     */
    @Override
    public void navigateMessage(String userId) {
//        startActivity( new Intent(getActivity(), MessageCenterActivity.class));
        Intent intent = new Intent(this, SysMsgActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到我的账单
     * @param userId 用户id
     */
    @Override
    public void navigateMineBill(String userId) {
        startActivity(new Intent(this, MyDebtActivity.class));
    }

    @Override
    public void navigateCollection(String userId) {
        startActivity(new Intent(this, CollectionActivity.class));
    }

    @Override
    public void navigateRewardPoints() {
        startActivity(new Intent(this, RewardPointActivity.class));
    }

    @Override
    public void navigateInvitation(String userId) {
        startActivity(new Intent(this, InvitationActivity.class));
    }

    @Override
    public void navigateContactKefu(String userId, String userName) {
        //调起聊天窗口
        Ntalker.getBaseInstance().startChat(this, Constant.XN_CUSTOMER, getResources().getString(R.string.app_name), null);
    }

    @Override
    public void navigateHelpAndFeedback(String userId) {
        startActivity(new Intent(this, HelpAndFeedActivity.class));
    }

    /**
     * 进入设置页面
     * @param userId 用户id
     */
    @Override
    public void navigateSetting(String userId) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * 是否显示我的账单按钮
     * @param visible 是否显示
     */
    @Override
    public void updateMyLoanVisible(boolean visible) {
        mineProductContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
        mineProductContainer.setVisibility(visible ? View.VISIBLE : View.VISIBLE);
    }

    /**
     * 消息数量
     * @param data
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


}
