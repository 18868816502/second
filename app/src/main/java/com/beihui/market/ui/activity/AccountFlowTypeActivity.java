package com.beihui.market.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.AccountFlowIconBean;
import com.beihui.market.event.AccountFlowTypeActivityEvent;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.AccountFlowAdapter;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.RxUtil;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2018/6/19.
 */

public class AccountFlowTypeActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.iv_ac_account_flow_type_back)
    ImageView mReturn;
    @BindView(R.id.iv_ac_account_flow_type_confirm)
    ImageView confirm;
    @BindView(R.id.rv_ac_account_flow_type)
    RecyclerView recyclerView;
    @BindView(R.id.ll_ac_account_flow_type_bottom)
    LinearLayout mBottom;


    @BindView(R.id.iv_ac_account_flow_type_icon)
    ImageView mTypeIcon;
    @BindView(R.id.et_ac_account_flow_type_name)
    EditText mTypeName;

    //适配器
    public AccountFlowAdapter mAdapter;

    //软键盘帮助类
    private KeyBoardHelper boardHelper;

    private List<AccountFlowIconBean> list = null;

    private String mTypeIconId = null;
    private String mTallyType = null;

    public AccountFlowIconBean bean = null;

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_account_flow_type;
    }

    private KeyBoardHelper.OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener = new KeyBoardHelper.OnKeyBoardStatusChangeListener() {

        /**
         * 软键盘弹出时候
         */
        @Override
        public void OnKeyBoardPop(int keyBoardHeight) {
            ViewGroup.LayoutParams layoutParams = mBottom.getLayoutParams();
            layoutParams.height = 0;
            mBottom.setLayoutParams(layoutParams);
        }

        /**
         * 软件盘隐藏的时候
         */
        @Override
        public void OnKeyBoardClose(int oldKeyBoardHeight) {
            ViewGroup.LayoutParams layoutParams = mBottom.getLayoutParams();
            if (layoutParams.height > oldKeyBoardHeight) {
                layoutParams.height += oldKeyBoardHeight;
                mBottom.setLayoutParams(layoutParams);
            }
        }
    };

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

        /**
         * 开启然键盘监听事件
         */
        boardHelper = new KeyBoardHelper(this);
        boardHelper.onCreate();
        boardHelper.setOnKeyBoardStatusChangeListener(onKeyBoardStatusChangeListener);
        mTypeName.postDelayed(new Runnable() {
            @Override
            public void run() {
                boardHelper.showKeyBoard(mTypeName);
            }
        }, 500);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE ) {
                    InputMethodUtil.closeSoftKeyboard(AccountFlowTypeActivity.this);
                }
                return false;
            }
        });
    }

    @Override
    public void initDatas() {

        mAdapter = new AccountFlowAdapter(this, 0);
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        SlidePanelHelper.attach(this);

        Api.getInstance().queryCustomIconList().compose(RxUtil.<ResultEntity<List<AccountFlowIconBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AccountFlowIconBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<AccountFlowIconBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (mAdapter != null) {
                                           list = result.getData();
                                           if (list.size() > 0) {
                                               Glide.with(AccountFlowTypeActivity.this).load(list.get(0).logo).into(mTypeIcon);
                                               AccountFlowTypeActivity.this.bean = list.get(0);
                                               mTypeIconId = list.get(0).iconId;
                                               mTallyType = list.get(0).tallyId;
                                           }
                                           mAdapter.notifyDebtChannelChanged(list);
                                       }
                                   } else {
                                       Toast.makeText(AccountFlowTypeActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //Log.e("exception_custom", throwable.getMessage());
                            }
                        });

        mAdapter.setOnItemClickListener(new AccountFlowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AccountFlowIconBean bean) {
                AccountFlowTypeActivity.this.bean = bean;
                mTypeIconId = bean.iconId;
                mTallyType = bean.tallyId;
                Glide.with(AccountFlowTypeActivity.this).load(bean.logo).into(mTypeIcon);
            }
        });
    }

    @OnClick({R.id.iv_ac_account_flow_type_confirm, R.id.iv_ac_account_flow_type_back })
    public void onItemClick(View view) {
        if (view.getId() == R.id.iv_ac_account_flow_type_confirm) {
            save();
        } else if (view.getId() == R.id.iv_ac_account_flow_type_back) {
//            TextViewDialog dialog = new TextViewDialog();
//            dialog.setName("退出本次编辑?");
//            dialog.show(getSupportFragmentManager(), "textViewDialog");
            finish();
        }
    }

    /**
     * 保存自定义图标
     */
    private void save() {
        if (TextUtils.isEmpty(mTypeName.getText().toString())) {
            Toast.makeText(AccountFlowTypeActivity.this, "类别名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mTypeIconId)) {
            return;
        }

        Api.getInstance().saveCustomIcon(UserHelper.getInstance(AccountFlowTypeActivity.this).getProfile().getId(), mTypeName.getText().toString(), mTypeIconId,  "LCommon" ).compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       AccountFlowTypeActivity.this.bean.iconName = mTypeName.getText().toString();
                                       EventBus.getDefault().post(new AccountFlowTypeActivityEvent(AccountFlowTypeActivity.this.bean));
                                       AccountFlowTypeActivity.this.finish();
                                   } else {
                                       Toast.makeText(AccountFlowTypeActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }



    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}