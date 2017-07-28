package com.beihui.market.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.LoanRVAdapter;
import com.beihui.market.ui.dialog.BrMoneyPopup;
import com.beihui.market.ui.dialog.BrTimePopup;
import com.beihui.market.ui.dialog.BrZhiyePopup;
import com.beihui.market.view.drawable.BlurDrawable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class TabLoanFragment extends BaseTabFragment implements BrMoneyPopup.onBrMoneyListener,
        BrTimePopup.onBrTimeListener, BrZhiyePopup.onBrZhiyeListener {

    @BindView(R.id.filter_container)
    LinearLayout filterContainer;
    @BindView(R.id.money_filter_text)
    TextView moneyFilterText;
    @BindView(R.id.money_filter_image)
    ImageView moneyFilterImage;
    @BindView(R.id.money_filter_content)
    TextView moneyFilterContent;
    @BindView(R.id.time_filter_text)
    TextView timeFilterText;
    @BindView(R.id.time_filter_image)
    ImageView timeFilterImage;
    @BindView(R.id.time_filter_content)
    TextView timeFilterContent;
    @BindView(R.id.pro_filter_text)
    TextView proFilterText;
    @BindView(R.id.pro_filter_image)
    ImageView proFilterImage;
    @BindView(R.id.pro_filter_content)
    TextView proFilterContent;

    @BindView(R.id.loan_container)
    FrameLayout loanContainer;
    @BindView(R.id.blur_view)
    View blurView;

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;


    private BrMoneyPopup moneyPopup;
    private BrTimePopup timePopup;
    private BrZhiyePopup zhiyePopup;

    //target money to query, default 5000
    private String inputMoney = "5000";
    //记录选择的是什么范围的，一个月，三个月还是不限,从1 ~ 7
    public int selectTimeIndex = 1;

    String tags[] = {"1个月及以下", "3个月", "6个月", "12个月", "24个月", "36个月及以上", "不限"};

    private LoanRVAdapter loanRVAdapter;


    public static TabLoanFragment newInstance() {
        return new TabLoanFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_loan;
    }

    @Override
    public void configViews() {
        blurView.setBackgroundDrawable(new BlurDrawable(getContext(), loanContainer));

        moneyFilterContent.setText(inputMoney);
        setOnTimeSelect(selectTimeIndex);

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        loanRVAdapter = new LoanRVAdapter();
        recycleView.setAdapter(loanRVAdapter);
    }

    @Override
    public void initDatas() {
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            tempList.add("" + i);
        }
        loanRVAdapter.notifyDataSetChanged(tempList);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }


    @Override
    public void onMoneyItemClick(String money) {
        this.inputMoney = money;
        moneyFilterContent.setText(money);
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
                proFilterContent.setText("上班族");
                break;
            case 2:
                proFilterContent.setText("学生");
                break;
            case 3:
                proFilterContent.setText("个体户");
                break;
            case 4:
                proFilterContent.setText("不限");
                break;
        }
    }

    public void setOnTimeSelect(int selectTimeIndex) {
        timeFilterContent.setText(tags[selectTimeIndex]);
    }


    @OnClick({R.id.money_filter, R.id.time_filter, R.id.pro_filter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.money_filter:
                moneyPopup = new BrMoneyPopup(getActivity(), inputMoney, blurView, moneyFilterText, moneyFilterImage);
                moneyPopup.setShareItemListener(this);
                moneyPopup.showAsDropDown(filterContainer);
                break;
            case R.id.time_filter:
                timePopup = new BrTimePopup(getActivity(), selectTimeIndex, blurView, timeFilterText, timeFilterImage, tags);
                timePopup.setShareItemListener(this);
                timePopup.showAsDropDown(filterContainer);
                break;
            case R.id.pro_filter:
                zhiyePopup = new BrZhiyePopup(getActivity(), blurView, proFilterText, proFilterImage);
                zhiyePopup.setShareItemListener(this);
                zhiyePopup.showAsDropDown(filterContainer);
                break;
        }
    }

    public void setQueryMoney(String queryMoney) {
        this.inputMoney = queryMoney;
    }
}
