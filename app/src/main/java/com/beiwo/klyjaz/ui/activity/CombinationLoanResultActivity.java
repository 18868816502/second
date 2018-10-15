package com.beiwo.klyjaz.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.ui.adapter.houseloan.HouseLoanResultVPAdapter;
import com.beiwo.klyjaz.ui.adapter.houseloan.HouseLoanVPAdapter;
import com.beiwo.klyjaz.view.dialog.LoanListView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @name
 * @class name：com.beihui.market
 * @class describe 组合房贷计算结果页面
 * @anthor chenguoguo
 * @time
 */
public class CombinationLoanResultActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlContianer;

    private int time;                           //贷款时间
    private double commMortgage;                //商业贷款总额
    private double HAFMortgage;                 //公积金贷款总额
    private double commMonthRate;               //商业贷款月利率
    private double HAFMonthRate;                //公积金贷款月利率
    private int aheadTime;

    //用于显示等额本息和等额本金的ViewPager
    private ViewPager viewPager;
    private ImageView ivBack;

    private LinearLayout tabLeftContainer;
    private LinearLayout tabRightContainer;
    private TextView tvTabLeftTitle;
    private TextView tvTabRightTitle;
    private View indicateLeft;
    private View indicateRight;

    private LoanListView listViewOne;                     //等额本息的ListView
    private LoanListView listViewTwo;                     //等额本金的ListView

    private TextView tvMonthPayLeft;
    private TextView tvSumInterestLeft;
    private TextView tvSumPaybackLeft;

    private TextView tvMonthPayRight;
    private TextView tvSumInterestRight;
    private TextView tvSumPaybackRight;

    //等额本息的结果数据
    private String onePayString;                        //还款总额
    private String[] oneTimeStrings;
    private String[] oneCapitalStrings;
    private String[] oneInterestStrings;
    private String[] oneMonthPayStrings;
    private String[] remainPays;

    //等额本金的结果数据
    private String twoPayString;                        //还款总额
    private String[] twoTimeStrings;
    private String[] twoCapitalStrings;
    private String[] twoInterestStrings;
    private String[] twoMonthPayStrings;
    private String[] twoRemainPays;

    private int currentItem = 0;

    private ProgressDialog progressDialog = null;
    DecimalFormat df;
    DecimalFormat mdf;

    private static final int DONE = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DONE:
                    //9.显示结果
                    showResult();
                    //剩余未还
                    calculateRemainPayback();

                    //等额本息的结果
                    HouseLoanResultVPAdapter adapterList1 = new HouseLoanResultVPAdapter(CombinationLoanResultActivity.this, oneTimeStrings, oneCapitalStrings, oneInterestStrings, remainPays);
                    listViewOne.setAdapter(adapterList1);

                    //等额本金的结果
                    HouseLoanResultVPAdapter adapterList2 = new HouseLoanResultVPAdapter(CombinationLoanResultActivity.this, twoTimeStrings, twoCapitalStrings, twoInterestStrings, twoRemainPays);
                    listViewTwo.setAdapter(adapterList2);
                    viewPager.setCurrentItem(currentItem);
                    progressDialog.dismiss();
                    break;
            }
        }
    };

    /**
     * 计算剩余未还
     */
    private void calculateRemainPayback() {
        double mPaybackSum = 0.0;
        remainPays = new String[oneMonthPayStrings.length];
        try {
            for(int i = 0 ; i < oneMonthPayStrings .length ; i++){
                mPaybackSum = mPaybackSum + df.parse(oneMonthPayStrings[i]).doubleValue();
                remainPays[i] = mdf.format((df.parse(onePayString).doubleValue() - mPaybackSum) > 0 ? (df.parse(onePayString).doubleValue() - mPaybackSum) : 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        double mTwoPaybackSum = 0.0;
        twoRemainPays = new String[twoMonthPayStrings.length];
        try {
            for(int i = 0 ; i < twoMonthPayStrings .length ; i++){
                mTwoPaybackSum = mTwoPaybackSum + df.parse(twoMonthPayStrings[i]).doubleValue();
                twoRemainPays[i] = mdf.format((df.parse(twoPayString).doubleValue() - mTwoPaybackSum) > 0 ?(df.parse(twoPayString).doubleValue() - mTwoPaybackSum):0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_loan_result);
        df = new DecimalFormat("#,##0.00");
        mdf = new DecimalFormat("######0.00");
        progressDialog = ProgressDialog.show(CombinationLoanResultActivity.this, "", "正在计算...", false, true);

        getData();                  //1.获得数据
        initViews();                //2.初始化控件
        initViewPager();            //3.设置ViewPager
        setListeners();             //4.设置监听器

        //启动新线程来计算
        new Thread(new Runnable() {
            @Override
            public void run() {
                calculateTypeOne(time, aheadTime);              //5.等额本息的计算方法
                calculateTypeTwo(time, aheadTime);              //6.等额本金的计算方法
                handler.sendEmptyMessage(DONE);
            }
        }).start();
    }

    //1.获得数据
    public void getData(){
        //从前一个Activity传来的数据
        Bundle bundle = this.getIntent().getExtras();
        if(bundle == null){
            return;
        }
        String HAFMortgageString = bundle.getString("HAFMortgage");             //公积金贷款
        String commMortgageString = bundle.getString("commMortgage");           //商业贷款
        String timeString = bundle.getString("time");
        String HAFRateString = bundle.getString("HAFRate");                     //公积金利率
        String commRateString = bundle.getString("commRate");                   //商业利率
        String a = bundle.getString("aheadTime");
        currentItem = bundle.getInt("paybackMethod");

        this.setTitle("组合贷款");

        HAFMortgage = Double.valueOf(HAFMortgageString);
        HAFMortgage = HAFMortgage * 10000;

        commMortgage = Double.valueOf(commMortgageString);
        commMortgage = commMortgage * 10000;

        //年利率转换为月利率
        double HAFRate = Double.valueOf(HAFRateString);
        HAFRate = HAFRate / 100;
        HAFMonthRate = HAFRate / 12;

        double commRate = Double.valueOf(commRateString);
        commRate = commRate / 100;
        commMonthRate = commRate / 12;

        //贷款时间转换为月
        time = Integer.valueOf(timeString);
        time = time * 12;

        //第几年还款转换为月
        aheadTime = Integer.valueOf(a);
        aheadTime = aheadTime * 12;
    }

    //2.初始化控件
    public void initViews(){
        rlContianer = findViewById(R.id.rl_container);
        ivBack = findViewById(R.id.navigate);
        viewPager = findViewById(R.id.Result_Combination_Viewpager);

        tabLeftContainer = findViewById(R.id.tab_left);
        tabRightContainer = findViewById(R.id.tab_right);
        tvTabLeftTitle = findViewById(R.id.tv_left_title);
        tvTabRightTitle = findViewById(R.id.tv_right_title);
        indicateLeft = findViewById(R.id.indicate_left);
        indicateRight = findViewById(R.id.indicate_right);

    }

    //3.设置ViewPager
    public void initViewPager() {
        List<View> viewList = new ArrayList<>();
        View view1 = LayoutInflater.from(this).inflate(R.layout.viewpager_result_capital_interest_combination,rlContianer, false);
        View view2 = LayoutInflater.from(this).inflate(R.layout.viewpager_result_capital_combination, rlContianer,false);

        viewList.add(view1);
        viewList.add(view2);

        tvMonthPayLeft = view1.findViewById(R.id.tv_month_pay_left);
        tvSumInterestLeft = view1.findViewById(R.id.tv_sum_interest_left);
        tvSumPaybackLeft = view1.findViewById(R.id.tv_sum_payback_left);
        listViewOne = view1.findViewById(R.id.CapitalInterestCombination_ListOne);

        tvMonthPayRight = view2.findViewById(R.id.tv_month_pay_right);
        tvSumInterestRight = view2.findViewById(R.id.tv_sum_interest_right);
        tvSumPaybackRight = view2.findViewById(R.id.tv_sum_payback_right);
        listViewTwo = view2.findViewById(R.id.CapitalCombination_ListTwo);

        HouseLoanVPAdapter adapter = new HouseLoanVPAdapter(viewList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());//3-1.ViewPager的监听器
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tab_left:
                viewPager.setCurrentItem(0);
                clearTabState();
                setSelectState(0);
                break;
            case R.id.tab_right:
                viewPager.setCurrentItem(1);
                clearTabState();
                setSelectState(1);
                break;
            case R.id.navigate:
                finish();
                break;
                default:
                    break;
        }
    }

    //3-1.ViewPager的监听器
    public class PageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            clearTabState();
            setSelectState(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //4.设置监听器
    public void setListeners(){
        tabLeftContainer.setOnClickListener(this);
        tabRightContainer.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    /**
     * 清除Tab状态
     */
    private void clearTabState(){
        tvTabLeftTitle.setTextColor(getResources().getColor(R.color.c_909298));
        tvTabRightTitle.setTextColor(getResources().getColor(R.color.c_909298));

        indicateLeft.setBackgroundColor(getResources().getColor(R.color.c_909298));
        indicateRight.setBackgroundColor(getResources().getColor(R.color.c_909298));

        indicateLeft.setVisibility(View.GONE);
        indicateRight.setVisibility(View.GONE);
    }

    /**
     * 设置Tab选中状态
     * @param position tab位置
     */
    private void setSelectState(int position){
        switch (position){
            case 0:
                tvTabLeftTitle.setTextColor(getResources().getColor(R.color.c_ff5240));
                indicateLeft.setBackgroundColor(getResources().getColor(R.color.c_ff5240));
                indicateLeft.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvTabRightTitle.setTextColor(getResources().getColor(R.color.c_ff5240));
                indicateRight.setBackgroundColor(getResources().getColor(R.color.c_ff5240));
                indicateRight.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    //5.等额本息的计算方法
    public void calculateTypeOne(int time, int aheadTime) {
        //如果没有提前还款
        if (aheadTime == 0) {
            aheadTime = time;
        }
        oneTimeStrings = new String[aheadTime];
        oneCapitalStrings = new String[aheadTime];
        oneInterestStrings = new String[aheadTime];
        oneMonthPayStrings = new String[aheadTime];

        double monthCommCapital[] = new double[time + 1];
        double monthCommInterest[] = new double[time + 1];
        double monthCommSum[] = new double[time + 1];
        double monthHAFCapital[] = new double[time + 1];
        double monthHAFInterest[] = new double[time + 1];
        double monthHAFSum[] = new double[time + 1];

        double paidCommCapital = 0;     //已还本金
        double paidCommInterest = 0;    //已还利息
        double paidComm = 0;            //总共已还
        double paidHAFCapital = 0;      //已还本金
        double paidHAFInterest = 0;     //已还利息
        double paidHAF = 0;             //总共已还

        DecimalFormat df = new DecimalFormat("#,##0.00");       //保留两位小数

        for (int i = 1; i <= aheadTime; i++) {
            monthCommCapital[i] = commMortgage * commMonthRate * Math.pow((1 + commMonthRate), i - 1) / (Math.pow(1 + commMonthRate, time) - 1);
            monthCommInterest[i] = commMortgage * commMonthRate * (Math.pow(1 + commMonthRate, time) - Math.pow(1 + commMonthRate, i - 1)) / (Math.pow(1 + commMonthRate, time) - 1);
            monthCommSum[i] = commMortgage * commMonthRate * Math.pow((1 + commMonthRate), time) / (Math.pow(1 + commMonthRate, time) - 1);

            monthHAFCapital[i] = HAFMortgage * HAFMonthRate * Math.pow((1 + HAFMonthRate), i - 1) / (Math.pow(1 + HAFMonthRate, time) - 1);
            monthHAFInterest[i] = HAFMortgage * HAFMonthRate * (Math.pow(1 + HAFMonthRate, time) - Math.pow(1 + HAFMonthRate, i - 1)) / (Math.pow(1 + HAFMonthRate, time) - 1);
            monthHAFSum[i] = HAFMortgage * HAFMonthRate * Math.pow((1 + HAFMonthRate), time) / (Math.pow(1 + HAFMonthRate, time) - 1);

            oneTimeStrings[i-1] = i + "";
            oneCapitalStrings[i-1] = df.format(monthCommCapital[i] + monthHAFCapital[i]);
            oneInterestStrings[i-1] = df.format(monthCommInterest[i] + monthHAFInterest[i]);
            oneMonthPayStrings[i-1] = df.format(monthCommSum[i] + monthHAFSum[i]);

            paidCommCapital = paidCommCapital + commMortgage * commMonthRate * Math.pow((1 + commMonthRate), i - 1) / (Math.pow(1 + commMonthRate, time) - 1);
            paidCommInterest = paidCommInterest + commMortgage * commMonthRate * (Math.pow(1 + commMonthRate, time) - Math.pow(1 + commMonthRate, i - 1)) / (Math.pow(1 + commMonthRate, time) - 1);
            paidComm = paidComm + commMortgage * commMonthRate * Math.pow((1 + commMonthRate), time) / (Math.pow(1 + commMonthRate, time) - 1);

            paidHAFCapital = paidHAFCapital + HAFMortgage * HAFMonthRate * Math.pow((1 + HAFMonthRate), i - 1) / (Math.pow(1 + HAFMonthRate, time) - 1);
            paidHAFInterest = paidHAFInterest + HAFMortgage * HAFMonthRate * (Math.pow(1 + HAFMonthRate, time) - Math.pow(1 + HAFMonthRate, i - 1)) / (Math.pow(1 + HAFMonthRate, time) - 1);
            paidHAF = paidHAF + HAFMortgage * HAFMonthRate * Math.pow((1 + HAFMonthRate), time) / (Math.pow(1 + HAFMonthRate, time) - 1);

        }

        double monthPayComm = commMortgage * commMonthRate * Math.pow((1 + commMonthRate), time) / (Math.pow(1 + commMonthRate, time) - 1);
        double sumComm = monthPayComm * time;

        double monthPayHAF = HAFMortgage * HAFMonthRate * Math.pow((1 + HAFMonthRate), time) / (Math.pow(1 + HAFMonthRate, time) - 1);
        double sumHAF = monthPayHAF * time;

        double onePay = (sumComm) + (sumHAF);
        onePayString = df.format(onePay);
    }

    //6.等额本金的计算方法
    public void calculateTypeTwo(int time, int aheadTime) {
        if (aheadTime == 0) {
            aheadTime = time;
        }
        twoTimeStrings = new String[aheadTime];
        twoCapitalStrings = new String[aheadTime];
        twoInterestStrings = new String[aheadTime];
        twoMonthPayStrings = new String[aheadTime];

        double monthCommCapital[] = new double[time + 1];
        double monthCommInterest[] = new double[time + 1];
        double monthCommSum[] = new double[time + 1];
        double monthHAFCapital[] = new double[time + 1];
        double monthHAFInterest[] = new double[time + 1];
        double monthHAFSum[] = new double[time + 1];

        double paidComm = 0;
        double paidCommCapital = 0;
        double paidCommInterest = 0;
        double paidCommSum = 0;
        double paidHAF = 0;
        double paidHAFCapital = 0;
        double paidHAFInterest = 0;
        double paidHAFSum = 0;

        DecimalFormat df = new DecimalFormat("#,###.00");

        for (int i = 1; i <= aheadTime; i++) {
            monthCommCapital[i] = commMortgage / time;
            monthCommInterest[i] = (commMortgage - paidComm) * commMonthRate;
            monthCommSum[i] = (commMortgage / time) + (commMortgage - paidComm) * commMonthRate;
            paidComm = paidComm + commMortgage / time;
            paidCommCapital = paidCommCapital + commMortgage / time;
            paidCommInterest = paidCommInterest + (commMortgage - paidComm) * commMonthRate;
            paidCommSum = paidCommSum + (commMortgage / time) + (commMortgage - paidComm) * commMonthRate;

            monthHAFCapital[i] = HAFMortgage / time;
            monthHAFInterest[i] = (HAFMortgage - paidHAF) * HAFMonthRate;
            monthHAFSum[i] = (HAFMortgage / time) + (HAFMortgage - paidHAF) * HAFMonthRate;
            paidHAF = paidHAF + HAFMortgage / time;
            paidHAFCapital = paidHAFCapital + HAFMortgage / time;
            paidHAFInterest = paidHAFInterest + (HAFMortgage - paidHAF) * HAFMonthRate;
            paidHAFSum = paidHAFSum + (HAFMortgage / time) + (HAFMortgage - paidComm) * HAFMonthRate;

            twoTimeStrings[i-1] = i + "";
            twoCapitalStrings[i-1] = df.format(monthCommCapital[i] + monthHAFCapital[i]);
            twoInterestStrings[i-1] = df.format(monthCommInterest[i] + monthHAFInterest[i]);
            twoMonthPayStrings[i-1] = df.format(monthCommSum[i] + monthHAFSum[i]);
        }
        double sumComm = time * (commMortgage * commMonthRate - commMonthRate * (commMortgage / time) * (time - 1) / 2 + commMortgage / time);
        double sumHAF = time * (HAFMortgage * HAFMonthRate - HAFMonthRate * (HAFMortgage / time) * (time - 1) / 2 + HAFMortgage / time);
        double twoPay = sumComm + sumHAF;
        twoPayString = df.format(twoPay);

    }

    //9.显示结果
    public void showResult(){
        DecimalFormat df = new DecimalFormat("#,##0.00");

        double inOneSum = 0.0;
        double inTwoSum = 0.0;
        try {
            for (String interest:
                 oneInterestStrings) {
                inOneSum = inOneSum + df.parse(interest).doubleValue();
            }
            for (String interest:
                    oneInterestStrings) {
                inTwoSum = inTwoSum + df.parse(interest).doubleValue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (aheadTime == 0){
            //等额本息的结果
            tvMonthPayLeft.setText(oneMonthPayStrings[0]);
            tvSumInterestLeft.setText(df.format(inOneSum / 10000));
            if (TextUtils.isEmpty(onePayString)){
                onePayString = "0";
            }
            double paySum = 0;
            try {
                paySum = df.parse(onePayString).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvSumPaybackLeft.setText(df.format(paySum / 10000));


            //等额本金的结果
            tvMonthPayRight.setText(twoMonthPayStrings[0]);
            tvSumInterestRight.setText(df.format(inTwoSum / 10000));
            if (TextUtils.isEmpty(twoPayString)){
                twoPayString = "0";
            }
            double paySumTwo = 0;
            try {
                paySumTwo = df.parse(twoPayString).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvSumPaybackRight.setText(df.format(paySumTwo / 10000));
        }
    }
}
