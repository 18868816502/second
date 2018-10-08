package com.beihui.market.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.CommunityPublishAdapter;
import com.beihui.market.ui.listeners.OnItemClickListener;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.view.dialog.PopDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.zhihu.matisse.Matisse;

import java.util.List;

import butterknife.BindView;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.activity
 * @class describe 社区发布页面
 * @author chenguoguo
 * @time 2018/9/11 18:45
 */
public class CommunityPublishActivity extends BaseComponentActivity implements View.OnClickListener,PopDialog.OnInitPopListener,OnItemClickListener {

    public static final int REQUEST_CODE_CHOOSE = 23;

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
    private int mPopType = 0;
    private List<Uri> uriList;

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
        adapter.setOnItemClickListener(this);

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
                mPopType = 0;
                showDialogTips(R.layout.dialog_community_publish_save);
                break;
            case R.id.tv_publish:
                mPopType = 1;
                showDialogTips(R.layout.dialog_community_publish_commit);
                break;
            case R.id.cancel:
            case R.id.tv_cancel:
                popDialog.dismiss();
                break;
            case R.id.tv_save:
                //保存
                finish();
                break;
            case R.id.tv_commit:
                //提交发布的内容
                ToastUtil.toast("提交");
                break;
                default:
                    break;
        }
    }

    /**
     * 根据布局显示弹窗
     * @param layoutId 布局id
     */
    private void showDialogTips(int layoutId){
        popDialog = new PopDialog.Builder(getSupportFragmentManager(),this)
                .setLayoutId(layoutId)
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
        switch (mPopType){
            case 0:
                view.findViewById(R.id.tv_cancel).setOnClickListener(this);
                view.findViewById(R.id.tv_save).setOnClickListener(this);
                break;
            case 1:
                view.findViewById(R.id.tv_cancel).setOnClickListener(this);
                view.findViewById(R.id.tv_commit).setOnClickListener(this);
                break;
                default:
                    break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
//            adapter.setHeadData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            uriList = Matisse.obtainResult(data);
            adapter.setHeadData(uriList);
        }
    }

    @Override
    public void onItemClick(int position) {
        if(uriList != null) {
            uriList.remove(position);
            adapter.setHeadData(uriList);
        }
    }
}
