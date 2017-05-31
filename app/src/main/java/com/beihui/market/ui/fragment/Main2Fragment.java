package com.beihui.market.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseRVFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.adapter.BorrowAdapter;
import com.beihui.market.ui.bean.support.ReturnMain2;
import com.beihui.market.ui.contract.Main1Contract;
import com.beihui.market.ui.dialog.BrMoneyPopup;
import com.beihui.market.ui.dialog.BrTimePopup;
import com.beihui.market.ui.dialog.BrZhiyePopup;
import com.beihui.market.ui.presenter.Main1Presenter;
import com.beihui.market.view.AutoTextView;
import com.beihui.market.view.yrecycleview.YRecycleview;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/1/22.
 * 办事中心页面
 */

public class Main2Fragment extends BaseRVFragment<Main1Presenter> implements Main1Contract.View, BrMoneyPopup.onBrMoneyListener,
        BrTimePopup.onBrTimeListener,BrZhiyePopup.onBrZhiyeListener {


    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_top_1)
    TextView tvTop1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_top_2)
    TextView tvTop2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_top_3)
    TextView tvTop3;
    @BindView(R.id.ly_top)
    LinearLayout ly_top;
    @BindView(R.id.shadow_view)
    View shadowView;
    @BindView(R.id.tv_tishi)
    AutoTextView tvTishi;
    @BindView(R.id.ly_tishi)
    LinearLayout lyTishi;


    @BindView(R.id.yrecycle_view)
    YRecycleview yrecycleView;


    private BrMoneyPopup moneyPopup;
    private BrTimePopup timePopup;
    private BrZhiyePopup zhiyePopup;

    private String inputMoney;
    //记录选择的是什么范围的，一个月，三个月还是不限,从1 ~ 7
    public int selectTimeIndex = 1;

    private BorrowAdapter adapter;


    public static Main2Fragment newInstance() {
        Main2Fragment f = new Main2Fragment();
        Bundle b = new Bundle();
        b.putString("type", "Main2Fragment");
        f.setArguments(b);
        return f;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main2;
    }

    @Override
    protected void immersionInit() {
        ImmersionBar.with(getActivity())
                .statusBarDarkFont(false)
                .navigationBarColor(R.color.colorPrimary)
                .init();
    }

    @Override
    public void configViews() {
        EventBus.getDefault().register(this);
        inputMoney = tv1.getText().toString();
        tvTishi.setScrollMode(AutoTextView.SCROLL_FAST);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        yrecycleView.setLayoutManager(layoutManager);
        adapter = new BorrowAdapter(getActivity());
        yrecycleView.setAdapter(adapter);

        yrecycleView.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                yrecycleView.setReFreshComplete();
            }

            @Override
            public void onLoadMore() {
                yrecycleView.setloadMoreComplete();
            }
        });

        setOnTimeSelect(selectTimeIndex);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }


    /**
     * 点击给我推荐，把条件带过去在第二个页面筛选
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void returnMenu2(ReturnMain2 event) {
        this.selectTimeIndex = event.selectTimeIndex;
        this.inputMoney = event.inputMoney;
        tv1.setText(inputMoney);
    }


    @Override
    public void onMoneyItemClick(String money) {
        this.inputMoney = money;
        tv1.setText(money);
    }

    @Override
    public void onTimeItemClick(int selectTimeIndex) {
        this.selectTimeIndex = selectTimeIndex;
        setOnTimeSelect(selectTimeIndex);
    }

    /**
     * 点击职业弹出框的选择
     * @param selectIndex
     */
    @Override
    public void onZhiyeItemClick(int selectIndex) {
        switch (selectIndex){
            case 1:
                tv3.setText("上班族");
                break;
            case 2:
                tv3.setText("学生");
                break;
            case 3:
                tv3.setText("个体户");
                break;
            case 4:
                tv3.setText("不限");
                break;
        }
    }

    /**
     * 借款期限彈出框的文字改變
     * @param selectTimeIndex
     */
    public void setOnTimeSelect(int selectTimeIndex){
        switch (selectTimeIndex){
            case 1:
                tv2.setText("1个月及以下");
                break;
            case 2:
                tv2.setText("3个月");
                break;
            case 3:
                tv2.setText("6个月");
                break;
            case 4:
                tv2.setText("12个月");
                break;
            case 5:
                tv2.setText("24个月");
                break;
            case 6:
                tv2.setText("36个月及以上");
                break;
            case 7:
                tv2.setText("不限");
                break;
        }
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @OnClick({R.id.ly_1, R.id.ly_2, R.id.ly_3, R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_1:
                moneyPopup = new BrMoneyPopup(getActivity(),inputMoney,shadowView,tvTop1,iv1);
                moneyPopup.setShareItemListener(this);
                moneyPopup.showAsDropDown(ly_top);
                break;
            case R.id.ly_2:
                timePopup = new BrTimePopup(getActivity(),selectTimeIndex,shadowView,tvTop2,iv2);
                timePopup.setShareItemListener(this);
                timePopup.showAsDropDown(ly_top);
                break;
            case R.id.ly_3:
                zhiyePopup = new BrZhiyePopup(getActivity(),shadowView,tvTop3,iv3);
                zhiyePopup.setShareItemListener(this);
                zhiyePopup.showAsDropDown(ly_top);
                break;
            case R.id.iv_close:
                lyTishi.setVisibility(View.GONE);
                break;
        }
    }
}
