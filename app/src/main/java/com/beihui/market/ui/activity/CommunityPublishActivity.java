package com.beihui.market.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.CommunityPublishAdapter;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.dialog.PopDialog;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.activity
 * @class describe 社区发布页面
 * @anthor chenguoguo
 * @time 2018/9/11 18:45
 */
public class CommunityPublishActivity extends BaseComponentActivity implements View.OnClickListener,PopDialog.OnInitPopListener {

    @BindView(R.id.tool_bar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.navigate)
    ImageView ivNavigate;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private CommunityPublishAdapter adapter;
    private PopDialog popDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_community_publish;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(rlTitleBar).statusBarDarkFont(true).init();
        adapter = new CommunityPublishAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ivNavigate.setOnClickListener(this);
        tvPublish.setOnClickListener(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navigate:
                //先判断是否有数据
                showSaveTips();
                break;
            case R.id.tv_publish:
                ToastUtil.toast("发布");
                break;
            case R.id.tv_cancel:
                popDialog.dismiss();
                break;
            case R.id.tv_save:
                //保存
                finish();
                break;
                default:
                    break;
        }
    }

    private void showSaveTips() {
        popDialog = new PopDialog.Builder(getSupportFragmentManager(),this)
                .setLayoutId(R.layout.dialog_community_publish_save)
                .setWidth(270)
                .setHeight(120)
                .setGravity(Gravity.CENTER)
                .setCancelableOutside(false)
                .setInitPopListener(this)
                .create();
        popDialog.show();
    }

    @Override
    public void initPop(View view, PopDialog mPopDialog) {
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_save).setOnClickListener(this);
    }
}
