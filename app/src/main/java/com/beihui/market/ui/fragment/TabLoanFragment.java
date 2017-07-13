package com.beihui.market.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.adapter.BorrowAdapter;
import com.beihui.market.ui.contract.Main1Contract;
import com.beihui.market.ui.dialog.BrMoneyPopup;
import com.beihui.market.ui.dialog.BrTimePopup;
import com.beihui.market.ui.dialog.BrZhiyePopup;
import com.beihui.market.view.AutoTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;


public class TabLoanFragment extends BaseTabFragment implements Main1Contract.View, BrMoneyPopup.onBrMoneyListener,
        BrTimePopup.onBrTimeListener, BrZhiyePopup.onBrZhiyeListener {


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


    @BindView(R.id.recycle_view)
    RecyclerView recycleView;


    private BrMoneyPopup moneyPopup;
    private BrTimePopup timePopup;
    private BrZhiyePopup zhiyePopup;

    //target money to query, default 5000
    private String inputMoney = "5000";
    //记录选择的是什么范围的，一个月，三个月还是不限,从1 ~ 7
    public int selectTimeIndex = 1;

    private BorrowAdapter adapter;


    public static TabLoanFragment newInstance() {
        TabLoanFragment f = new TabLoanFragment();
        Bundle b = new Bundle();
        b.putString("type", "TabLoanFragment");
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle pending = getArguments();
        if (pending != null) {
            inputMoney = pending.getString("queryMoney");
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_loan;
    }

    @Override
    public void configViews() {
        //register to EventBus if hasn't yet
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        tv1.setText(inputMoney);
        tvTishi.setScrollMode(AutoTextView.SCROLL_FAST);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        adapter = new BorrowAdapter(getActivity());
        recycleView.setAdapter(adapter);

        setOnTimeSelect(selectTimeIndex);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
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
     *
     * @param selectIndex
     */
    @Override
    public void onZhiyeItemClick(int selectIndex) {
        switch (selectIndex) {
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
     *
     * @param selectTimeIndex
     */
    public void setOnTimeSelect(int selectTimeIndex) {
        switch (selectTimeIndex) {
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
                moneyPopup = new BrMoneyPopup(getActivity(), inputMoney, shadowView, tvTop1, iv1);
                moneyPopup.setShareItemListener(this);
                moneyPopup.showAsDropDown(ly_top);
                break;
            case R.id.ly_2:
                timePopup = new BrTimePopup(getActivity(), selectTimeIndex, shadowView, tvTop2, iv2);
                timePopup.setShareItemListener(this);
                timePopup.showAsDropDown(ly_top);
                break;
            case R.id.ly_3:
                zhiyePopup = new BrZhiyePopup(getActivity(), shadowView, tvTop3, iv3);
                zhiyePopup.setShareItemListener(this);
                zhiyePopup.showAsDropDown(ly_top);
                break;
            case R.id.iv_close:
                lyTishi.setVisibility(View.GONE);
                break;
        }
    }
}
