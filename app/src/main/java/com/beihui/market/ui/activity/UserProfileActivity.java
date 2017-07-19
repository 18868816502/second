package com.beihui.market.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.dialog.AvatarSelectorDialog;

import butterknife.BindView;
import butterknife.OnClick;


public class UserProfileActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    public static void launch(Context context) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_profile;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.avatar_item, R.id.nick_name_item, R.id.job_group_item})
    void OnItemsClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar_item:
                new AvatarSelectorDialog().show(getSupportFragmentManager(), AvatarSelectorDialog.class.getSimpleName());
                break;
            case R.id.nick_name_item:
                Intent toEditName = new Intent(this, EditNickNameActivity.class);
                startActivity(toEditName);
                break;
            case R.id.job_group_item:
                Intent toEditJob = new Intent(this, EditJobGroupActivity.class);
                startActivity(toEditJob);
                break;
        }
    }
}
