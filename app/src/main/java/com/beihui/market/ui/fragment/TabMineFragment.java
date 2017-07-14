package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.activity.MessageCenterActivity;
import com.beihui.market.ui.activity.SettingsActivity;
import com.beihui.market.ui.activity.UserProfileActivity;
import com.beihui.market.ui.contract.Main1Contract;
import com.beihui.market.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;


public class TabMineFragment extends BaseTabFragment implements Main1Contract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.avatar)
    CircleImageView avatarIv;
    @BindView(R.id.phone)
    LinearLayout phoneLl;
    @BindView(R.id.phone_number_head)
    TextView phoneNumberHead;
    @BindView(R.id.phone_number_tail)
    TextView phoneNumberTail;


    public static TabMineFragment newInstance() {
        return new TabMineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void showError(String err) {
        dismissDialog();
    }

    @Override
    public void complete() {
        dismissDialog();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tab_mine, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.avatar, R.id.mine_msg, R.id.invite_friend, R.id.helper_feedback, R.id.settings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar:
//                LoginActivity.startActivity(getActivity());
                UserProfileActivity.launch(getActivity());
                break;
            case R.id.mine_msg:
                Intent toMsg = new Intent(getActivity(), MessageCenterActivity.class);
                startActivity(toMsg);
                break;
            case R.id.invite_friend:
                break;
            case R.id.helper_feedback:
                break;
            case R.id.settings:
                Intent toSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivity(toSettings);
                break;
        }
    }
}
