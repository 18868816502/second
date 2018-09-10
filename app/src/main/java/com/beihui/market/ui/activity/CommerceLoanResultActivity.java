package com.beihui.market.ui.activity;

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

import com.beihui.market.R;
import com.beihui.market.ui.adapter.houseloan.HouseLoanResultVPAdapter;
import com.beihui.market.ui.adapter.houseloan.HouseLoanVPAdapter;
import com.beihui.market.view.dialog.LoanListView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @name
 * @class name：com.beihui.market
 * @class describe 商业和公积金放贷计算结果页面
 * @anthor chenguoguo
 * @time
 */
public class CommerceLoanResultActivity extends AppCompatActivity implements View.OnClickListener {

    private double mortgage;
    private int time;
    private double montRate;
    private int aheadTime;
    private String title;
    private double sum = 0;

    private RelativeLayout rlContainer;
    private ImageView ivBack;
    private LinearLayout tabLeftContainer;
    private LinearLayout tabRightContainer;
    private TextView tvTabLeftTitle;
    private TextView tvTabRightTitle;
    private View indicateLeft;
    private View indicateRight;

    //用于显示等额本息和等额本金的ViewPager
    private ViewPager viewPager;
    private LoanListView listViewOne;                     //等额本息的ListView
    private LoanListView listViewTwo;                     //等额本金的ListView

    private TextView tvMonthPayLeft;
    private TextView tvSumInterestLeft;
    private TextView tvSumPaybackLeft;

    private TextView tvMonthPayRight;
    private TextView tvSumInterestRight;
    private TextView tvSumPaybackRight;

    private String oneSumString;                        //等额本息的结果数据
    private String oneMonthPayString;
    private String[] oneTimeStrings;
    private String[] oneCapitalStrings;
    private String[] oneInterestStrings;
    private String[] oneMonthPayStrings;
    private String[] remainPays;

    private String twoSumString;                        //等额本金的结果数据
    private String twoFistMonthSum;
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
                    HouseLoanResultVPAdapter adapterList1 = new HouseLoanResultVPAdapter(CommerceLoanResultActivity.this, oneTimeStrings, oneCapitalStrings, oneInterestStrings, remainPays);
                    listViewOne.setAdapter(adapterList1);
                    //等额本金的结果
                    HouseLoanResultVPAdapter adapterList2 = new HouseLoanResultVPAdapter(CommerceLoanResultActivity.this, twoTimeStrings, twoCapitalStrings, twoInterestStrings, twoRemainPays);
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
                remainPays[i] = mdf.format((df.parse(oneSumString).doubleValue() - mPaybackSum) > 0 ? (df.parse(oneSumString).doubleValue() - mPaybackSum) : 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        double mTwoPaybackSum = 0.0;
        twoRemainPays = new String[twoMonthPayStrings.length];
        try {
            for(int i = 0 ; i < twoMonthPayStrings .length ; i++){
                mTwoPaybackSum = mTwoPaybackSum + df.parse(twoMonthPayStrings[i]).doubleValue();
                twoRemainPays[i] = mdf.format((df.parse(twoSumString).doubleValue() - mTwoPaybackSum) > 0 ?(df.parse(twoSumString).doubleValue() - mTwoPaybackSum):0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_loan_result);
        progressDialog = ProgressDialog.show(CommerceLoanResultActivity.this, "", "正在计算...", false, true);
        df = new DecimalFormat("#,##0.00");
        mdf = new DecimalFormat("######0.00");
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
        String m = bundle.getString("mortgage");
        String r = bundle.getString("rate");
        String t = bundle.getString("time");
        String a = bundle.getString("aheadTime");
        currentItem = bundle.getInt("paybackMethod");
        int calculationMethod = bundle.getInt("calculationMethod");

        switch (calculationMethod){
            case 0:
                title = getString(R.string.loan_comm);
                break;
            case 1:
                title = getString(R.string.loan_gjj);
                break;
            case 2:
                title = getString(R.string.loan_comb);
                break;
        }
        this.setTitle(title);

        //万元转换为元
        mortgage = Double.valueOf(m);
        mortgage = mortgage * 10000;

        //年利率转换为月利率
        double rate = Double.valueOf(r);
        rate = rate / 100;
        montRate = rate / 12;

        //贷款时间转换为月
        time = Integer.valueOf(t);
        time = time * 12;

        //第几年还款转换为月
        aheadTime = Integer.valueOf(a);
        aheadTime = aheadTime * 12;
    }

    //2.初始化控件
    public void initViews(){
        rlContainer = findViewById(R.id.rl_container);
        ivBack = findViewById(R.id.navigate);
        viewPager = findViewById(R.id.viewpager);
        tabLeftContainer = findViewById(R.id.tab_left);
        tabRightContainer = findViewById(R.id.tab_right);
        tvTabLeftTitle = findViewById(R.id.tv_left_title);
        tvTabRightTitle = findViewById(R.id.tv_right_title);
        indicateLeft = findViewById(R.id.indicate_left);
        indicateRight = findViewById(R.id.indicate_right);

    }

    //4.设置监听器
    public void setListeners() {
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


    //3.设置ViewPager
    public void initViewPager(){
        List<View> viewList = new ArrayList<>();

        View view1 = LayoutInflater.from(this).inflate(R.layout.viewpager_result_capital_interest,rlContainer,false);
        View view2 = LayoutInflater.from(this).inflate(R.layout.viewpager_result_capital,rlContainer,false);

        viewList.add(view1);
        viewList.add(view2);

        tvMonthPayLeft = view1.findViewById(R.id.tv_month_pay_left);
        tvSumInterestLeft = view1.findViewById(R.id.tv_sum_interest_left);
        tvSumPaybackLeft = view1.findViewById(R.id.tv_sum_payback_left);
        listViewOne = view1.findViewById(R.id.listOne);

        tvMonthPayRight = view2.findViewById(R.id.tv_month_pay_right);
        tvSumInterestRight = view2.findViewById(R.id.tv_sum_interest_right);
        tvSumPaybackRight = view2.findViewById(R.id.tv_sum_payback_right);
        listViewTwo = view2.findViewById(R.id.listTwo);

        HouseLoanVPAdapter adapter = new HouseLoanVPAdapter(viewList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());        //3-1.ViewPager的监听器
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


    //5.等额本息的计算方法
    public void calculateTypeOne(int time, int aheadTime){
        //如果没有提前还款
        if (aheadTime == 0){
            aheadTime = time;
        }

        oneTimeStrings = new String[aheadTime];
        oneCapitalStrings = new String[aheadTime];
        oneInterestStrings = new String[aheadTime];
        oneMonthPayStrings = new String[aheadTime];

        String monthCapital[] = new String[time + 1];
        String monthInterest[] = new String[time + 1];
        String monthSum[] = new String[time + 1];

        double paidCapital = 0;     //已还本金
        double paidInterest = 0;    //已还利息
        double paid = 0;            //总共已还
        DecimalFormat df = new DecimalFormat("#,##0.00");       //保留两位小数
        for (int i = 1; i <= aheadTime; i++){
            //每月应还本金：每月应还本金=贷款本金×月利率×(1+月利率)^(还款月序号-1)÷〔(1+月利率)^还款月数-1〕
            monthCapital[i] = df.format(mortgage * montRate * Math.pow((1 + montRate), i - 1) / (Math.pow(1 + montRate, time) - 1));
            //每月应还利息：贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
            monthInterest[i] = df.format(mortgage * montRate * (Math.pow(1 + montRate, time) - Math.pow(1 + montRate, i - 1)) / (Math.pow(1 + montRate, time) - 1));
            //月供：每月月供额=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
            monthSum[i] = df.format(mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1));

            //得到输出字符串
            oneTimeStrings[i-1] = i + "";
            oneCapitalStrings[i-1] = monthCapital[i];
            oneInterestStrings[i-1] = monthInterest[i];
            oneMonthPayStrings[i-1] = monthSum[i];

            //已还本金
            paidCapital = paidCapital + mortgage * montRate * Math.pow((1 + montRate), i - 1) / (Math.pow(1 + montRate, time) - 1);
            //已还利息
            paidInterest = paidInterest + mortgage * montRate * (Math.pow(1 + montRate, time) - Math.pow(1 + montRate, i - 1)) / (Math.pow(1 + montRate, time) - 1);
            //总共已还
            paid = paid + mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1);
        }

        //月供
        double monthPay = mortgage * montRate * Math.pow((1 + montRate), time) / (Math.pow(1 + montRate, time) - 1);

        //还款总额
        sum = monthPay * time;

        //格式化
        oneSumString = df.format(sum);
        oneMonthPayString = df.format(monthPay);

    }

    //6.等额本金的计算方法
    public void calculateTypeTwo(int time, int aheadTime){
        if (aheadTime == 0){
            aheadTime = time;
        }

        twoTimeStrings = new String[aheadTime];
        twoCapitalStrings = new String[aheadTime];
        twoInterestStrings = new String[aheadTime];
        twoMonthPayStrings = new String[aheadTime];

        String monthCapital[] = new String[time + 1];
        String monthInterest[] = new String[time + 1];
        String monthSum[] = new String[time + 1];

        DecimalFormat df = new DecimalFormat("#,##0.00");
        double paid = 0;
        double paidCapital = 0;
        double paidInterest = 0;
        double paidSum = 0;
        for (int i = 1; i <= aheadTime; i++){
            //每月应还本金：贷款本金÷还款月数
            monthCapital[i] = df.format(mortgage / time);
            //每月应还利息：剩余本金×月利率=(贷款本金-已归还本金累计额)×月利率
            monthInterest[i] = df.format((mortgage - paid) * montRate);
            //月供：(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率
            monthSum[i] = df.format((mortgage / time) + (mortgage - paid) * montRate);
            //已归还本金累计额
            paid = paid + mortgage / time;
            //已还本金
            paidCapital = paidCapital + mortgage / time;
            //已还利息
            paidInterest = paidInterest + (mortgage - paid) * montRate;
            //总共已还
            paidSum = paidSum + (mortgage / time) + (mortgage - paid) * montRate;

            twoTimeStrings[i-1] = i + "";
            twoCapitalStrings[i-1] = monthCapital[i];
            twoInterestStrings[i-1] = monthInterest[i];
            twoMonthPayStrings[i-1] = monthSum[i];
        }
        sum = time * (mortgage * montRate - montRate * (mortgage / time) * (time - 1) / 2 + mortgage / time);
        twoSumString = df.format(sum);
        twoFistMonthSum = monthSum[1];

    }

    //9.显示结果
    public void showResult(){

        //计算总利息
        double inOneSum = 0.0;
        double inTwoSum = 0.0;
        try {
            for (String interest:
                    oneInterestStrings) {
                inOneSum = inOneSum + df.parse(interest).doubleValue();
            }
            for (String interest:
                    twoInterestStrings) {
                inTwoSum = inTwoSum + df.parse(interest).doubleValue();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (aheadTime == 0){
            //等额本息的结果
            tvMonthPayLeft.setText(oneMonthPayString);
            tvSumInterestLeft.setText(df.format(inOneSum / 10000));
            if (TextUtils.isEmpty(oneSumString)){
                oneSumString = "0";
            }
            double paySum = 0;
            try {
                paySum = df.parse(oneSumString).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvSumPaybackLeft.setText(df.format(paySum / 10000));


            //等额本金的结果
            tvMonthPayRight.setText(twoFistMonthSum);
            tvSumInterestRight.setText(df.format(inTwoSum / 10000));
            if (TextUtils.isEmpty(twoSumString)){
                twoSumString = "0";
            }
            double paySumTwo = 0;
            try {
                paySumTwo = df.parse(twoSumString).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvSumPaybackRight.setText(df.format(paySumTwo / 10000));
        }
    }
}
