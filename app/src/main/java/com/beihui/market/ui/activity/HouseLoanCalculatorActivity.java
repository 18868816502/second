package com.beihui.market.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.entity.HouseLoanBean;
import com.beihui.market.ui.adapter.houseloan.HouseLoanAdapter;
import com.beihui.market.ui.adapter.houseloan.HouseLoanVPAdapter;
import com.beihui.market.view.dialog.PopDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 商业贷款给和公积金贷款计算详情页面
 */
public class HouseLoanCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private LinearLayout tabCommContainer;
    private TextView tvCommTab;
    private View indicateComm;

    private LinearLayout paybackCommInterestContainer;
    private ImageView ivPaybackCommInterest;
    private LinearLayout paybackCommPrincipalContainer;
    private ImageView ivPaybackCommPrincipal;


    private LinearLayout tabHafContainer;
    private TextView tvHafTab;
    private View indicateHaf;

    private LinearLayout paybackHafInterestContainer;
    private ImageView ivPaybackHafInterest;
    private LinearLayout paybackHafPrincipalContainer;
    private ImageView ivPaybackHafPrincipal;


    private LinearLayout tabCombContainer;
    private TextView tvCombTab;
    private View indicateComb;

    private LinearLayout paybackCombInterestContainer;
    private ImageView ivPaybackCombInterest;
    private LinearLayout paybackCombPrincipalContainer;
    private ImageView ivPaybackCombPrincipal;

    /**
     * 默认利率
     */
    private static final String DEFAULT_RATE = "4.90";//默认利率
    /**
     * 默认年限
     */
    private static final String DEFAULT_YEAR = "30";//默认年限
    /**
     * 滑动控件
     */
    private ViewPager viewPager;
    /**
     * 滑动控件集合
     */
    private List<View> viewList;
    /**
     * 滑动控件位置
     */
    private int currentItem = 0;
    /**
     * 计算按钮
     */
    private TextView tvCalculate;

    //商业贷款部分
    private RelativeLayout rlYearContainer;
    private TextView tvYear;

    private LinearLayout llSYRateContainer;
    private TextView tvSYRate;

    private Calendar CommCalendar;
    //动态添加的控件
    private EditText CommMortgageEditText;          //按贷款金额计算

    /**
     * 贷款时间（年）
     */
    private String CommTime;
    /**
     * 贷款金额（万元）
     */
    private String CommMortgage;
    /**
     * 贷款年利率（%）
     */
    private String CommRate;
    /**
     * 首次还款年
     */
    private int CommFirstYear;
    /**
     * 首次还款月
     */
    private int CommFirstMonth;
    /**
     * 还款方式（等额本息、等额本金）
     */
    private int CommPaybackMethod = 0;                  //

    //公积金贷款部分
    private RelativeLayout rlYearGJJContainer;
    private TextView tvYearGJJ;

    private LinearLayout llGJJRateContainer;
    private TextView tvGJJRate;

    private Calendar HAFCalendar;
    //动态添加的控件
    private EditText HAFMortgageEditText;           //按贷款金额计算

    /**
     * 贷款时间（年）
     */
    private String HAFTime;
    /**
     * 贷款金额（万元）
     */
    private String HAFMortgage;
    /**
     * 贷款年利率（%）
     */
    private String HAFRate;
    /**
     * 首次还款年
     */
    private int HAFFirstYear;
    /**
     * 首次还款月
     */
    private int HAFFirstMonth;
    /**
     * 还款方式（等额本息、等额本金）
     */
    private int HAFPaybackMethod = 0;

    //组合贷款部分
    private RelativeLayout rlYearZHContainer;
    private TextView tvZHYear;
    private LinearLayout llZHRate;
    private TextView tvZHRate;
    private LinearLayout llZHGJJRate;
    private TextView tvZHGJJRate;
    private Calendar CombCalendar;

    //动态添加的控件
    //按贷款金额计算
    private EditText CombMortgageHAFEditText;                       //公积金贷款部分
    private EditText CombMortgageCommEditText;                      //商业贷款部分

    private String CombTime;                        //贷款时间（年）
    private String CombPay;                         //贷款总额（万元）
    private String CombMortgageComm;                //商业贷款金额（万元）
    private String CombMortgageHAF;                 //公积金贷款金额（万元）
    private String CombHAFRate;                     //公积金贷款年利率（%）
    private String CombCommRate;                    //商业贷款年利率（%）
    private int CombFirstYear;                      //首次还款年月
    private int CombFirstMonth;
    private int CombPaybackMethod = 0;                  //还款方式（等额本息、等额本金）

    List<HouseLoanBean> yearList;

    String[] rateString = {"基准利率(4.95)", "基准利率7折(3.43%)", "基准利率85折(4.165%)", "基准利率88折(4.312%)",
            "基准利率9折(4.41%)", "基准利率1.1折(5.39%)", "基准利率1.2折(5.88%)", "基准利率1.3折(6.37%)"};
    Double[] rates = {4.95, 3.43, 4.165, 4.312, 4.41, 5.39, 5.88, 6.37};
    final List<HouseLoanBean> rateLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_loan);
        initViews();
        initViewPager();
        setListeners();
        initPopWindow();
    }

    //1.初始化Views
    public void initViews() {
        ivBack = findViewById(R.id.navigate);

        tabCommContainer = findViewById(R.id.tab_comm);
        tvCommTab = findViewById(R.id.tv_commb_tab);
        indicateComm = findViewById(R.id.indicate_comm);

        tabHafContainer = findViewById(R.id.tab_haf);
        tvHafTab = findViewById(R.id.tv_HAF_tab);
        indicateHaf = findViewById(R.id.indicate_haf);

        tabCombContainer = findViewById(R.id.tab_comb);
        tvCombTab = findViewById(R.id.tv_comb_tab);
        indicateComb = findViewById(R.id.indicate_comb);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tvCalculate = findViewById(R.id.tv_calculate);

        CommCalendar = Calendar.getInstance();
        CommFirstYear = CommCalendar.get(Calendar.YEAR);
        CommFirstMonth = CommCalendar.get(Calendar.MONTH) + 1;

        HAFCalendar = Calendar.getInstance();
        HAFFirstYear = HAFCalendar.get(Calendar.YEAR);
        HAFFirstMonth = HAFCalendar.get(Calendar.MONTH) + 1;

        CombCalendar = Calendar.getInstance();
        CombFirstYear = CombCalendar.get(Calendar.YEAR);
        CombFirstMonth = CombCalendar.get(Calendar.MONTH) + 1;
    }

    //2.初始化ViewPager
    public void initViewPager() {
        viewList = new ArrayList<View>();
        LayoutInflater layoutInflater = getLayoutInflater().from(this);

        //三个标题
        View commercialLoanView = layoutInflater.inflate(R.layout.viewpager_main_commercialloan, null);
        View HAFView = layoutInflater.inflate(R.layout.viewpager_main_haf, null);
        View combinationView = layoutInflater.inflate(R.layout.viewpager_main_combination, null);

        //商业贷款页面控件
        rlYearContainer = commercialLoanView.findViewById(R.id.rl_year_container);
        tvYear = commercialLoanView.findViewById(R.id.tv_year);
        llSYRateContainer = commercialLoanView.findViewById(R.id.ll_sy_rate_container);
        tvSYRate = commercialLoanView.findViewById(R.id.tv_sy_rate);
        CommMortgageEditText = commercialLoanView.findViewById(R.id.Commercial_MortgageEditText);

        paybackCommInterestContainer = commercialLoanView.findViewById(R.id.ll_comm_payback_interest);
        paybackCommPrincipalContainer = commercialLoanView.findViewById(R.id.ll_comm_payback_principal);
        ivPaybackCommInterest = commercialLoanView.findViewById(R.id.iv_comm_payback_interest);
        ivPaybackCommPrincipal = commercialLoanView.findViewById(R.id.iv_comm_payback_principal);

        //公积金贷款页面
        rlYearGJJContainer = HAFView.findViewById(R.id.rl_gjj_year_container);
        tvYearGJJ = HAFView.findViewById(R.id.tv_gjj_year);
        llGJJRateContainer = HAFView.findViewById(R.id.ll_gjj_rate_container);
        tvGJJRate = HAFView.findViewById(R.id.tv_gjj_rate);
        HAFMortgageEditText = HAFView.findViewById(R.id.HAF_MortgageEditText);

        paybackHafInterestContainer = HAFView.findViewById(R.id.ll_haf_payback_interest);
        paybackHafPrincipalContainer = HAFView.findViewById(R.id.ll_haf_payback_principal);
        ivPaybackHafInterest = HAFView.findViewById(R.id.iv_haf_payback_interest);
        ivPaybackHafPrincipal = HAFView.findViewById(R.id.iv_haf_payback_principal);

        //组合贷款页面
        rlYearZHContainer = combinationView.findViewById(R.id.rl_zh_year_container);
        tvZHYear = combinationView.findViewById(R.id.tv_zh_year);
        llZHGJJRate = combinationView.findViewById(R.id.ll_zh_gjj_rate_container);
        tvZHGJJRate = combinationView.findViewById(R.id.tv_zh_gjj_rate);
        llZHRate = combinationView.findViewById(R.id.ll_zh_rate_container);
        tvZHRate = combinationView.findViewById(R.id.tv_zh_rate);
        CombMortgageHAFEditText = combinationView.findViewById(R.id.Combination_Mortgage_HAF_EditText);
        CombMortgageCommEditText = combinationView.findViewById(R.id.Combination_Mortgage_Comm_EditText);

        paybackCombInterestContainer = combinationView.findViewById(R.id.ll_comb_payback_interest);
        paybackCombPrincipalContainer = combinationView.findViewById(R.id.ll_comb_payback_principal);
        ivPaybackCombInterest = combinationView.findViewById(R.id.iv_comb_payback_interest);
        ivPaybackCombPrincipal = combinationView.findViewById(R.id.iv_comb_payback_principal);

        viewList.add(commercialLoanView);
        viewList.add(HAFView);
        viewList.add(combinationView);

        HouseLoanVPAdapter pagerAdapter = new HouseLoanVPAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.setOnPageChangeListener(new PageChangeListener());        //2-1.ViewPager的监听器
    }

    //3.设置监听器
    public void setListeners() {
        tabCommContainer.setOnClickListener(this);
        tabHafContainer.setOnClickListener(this);
        tabCombContainer.setOnClickListener(this);

//        paybackCommInterestContainer.setOnClickListener(this);
//        paybackCommPrincipalContainer.setOnClickListener(this);
//        paybackHafInterestContainer.setOnClickListener(this);
//        paybackHafPrincipalContainer.setOnClickListener(this);
//        paybackCombInterestContainer.setOnClickListener(this);
//        paybackCombPrincipalContainer.setOnClickListener(this);

        ivPaybackCommInterest.setOnClickListener(this);
        ivPaybackCommPrincipal.setOnClickListener(this);
        ivPaybackHafInterest.setOnClickListener(this);
        ivPaybackHafPrincipal.setOnClickListener(this);
        ivPaybackCombInterest.setOnClickListener(this);
        ivPaybackCombPrincipal.setOnClickListener(this);

        tvCalculate.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    /**
     * 跳转到组合贷款详情页
     */
    private void toResult3Activity() {
        Intent intent3 = new Intent();
        intent3.setClass(HouseLoanCalculatorActivity.this, CombinationLoanResultActivity.class);        //跳转到结果列表
        Bundle bundle3 = new Bundle();
        bundle3.putString("mortgage", CombPay);                                         //贷款总额
        bundle3.putString("HAFMortgage", CombMortgageHAF);                              //公积金贷款
        bundle3.putString("commMortgage", CombMortgageComm);                            //商业贷款
        bundle3.putString("time", CombTime);
        bundle3.putString("HAFRate", CombHAFRate);                                      //公积金利率
        bundle3.putString("commRate", CombCommRate);                                    //商业利率
        bundle3.putString("aheadTime", "0");
        bundle3.putInt("firstYear", CombFirstYear);
        bundle3.putInt("firstMonth", CombFirstMonth);
        bundle3.putInt("paybackMethod", CombPaybackMethod);
        intent3.putExtras(bundle3);
        startActivity(intent3);
    }

    /**
     * 检验组合贷款填写是否合格
     *
     * @return 是否通过
     */
    private boolean checkGroupLoan() {
        CombMortgageHAF = CombMortgageHAFEditText.getText().toString();
        CombMortgageComm = CombMortgageCommEditText.getText().toString();
        if (TextUtils.isEmpty(CombMortgageHAF)) {
            Toast.makeText(HouseLoanCalculatorActivity.this, "请填写公积金贷款金额", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(CombMortgageComm)) {
            Toast.makeText(HouseLoanCalculatorActivity.this, "请填商贷金额", Toast.LENGTH_SHORT).show();
            return false;
        }
        double sum = Double.valueOf(CombMortgageHAF) + Double.valueOf(CombMortgageComm);
        CombPay = String.valueOf(sum);
        if (Double.valueOf(CombMortgageHAF) == 0) {
            Toast.makeText(HouseLoanCalculatorActivity.this, "公积金贷款金额不能为0", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(CombTime)) {
            CombTime = DEFAULT_YEAR;
        }

        if (TextUtils.isEmpty(CombHAFRate)) {
            CombHAFRate = DEFAULT_RATE;
        }

        if (TextUtils.isEmpty(CombCommRate)) {
            CombCommRate = DEFAULT_RATE;
        }
        return true;
    }

    /**
     * 跳转到公积金贷款详情页
     */
    private void toResult2Activity() {
        Intent intent2 = new Intent();
        intent2.setClass(HouseLoanCalculatorActivity.this, CommerceLoanResultActivity.class);      //跳转到结果列表
        Bundle bundle2 = new Bundle();
        bundle2.putString("mortgage", HAFMortgage);
        bundle2.putString("time", HAFTime);
        bundle2.putString("rate", HAFRate);
        bundle2.putString("aheadTime", "0");
        bundle2.putInt("firstYear", HAFFirstYear);
        bundle2.putInt("firstMonth", HAFFirstMonth);
        bundle2.putInt("paybackMethod", HAFPaybackMethod);
        bundle2.putInt("calculationMethod", 1);
        intent2.putExtras(bundle2);
        startActivity(intent2);
    }

    /**
     * 检查公积金贷款是否合格
     *
     * @return 是否通过
     */
    private boolean checkGongjijinLoan() {
        HAFMortgage = HAFMortgageEditText.getText().toString();
        if (TextUtils.isEmpty(HAFMortgage)) {
            Toast.makeText(HouseLoanCalculatorActivity.this, "请填写贷款金额", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.valueOf(HAFMortgage) == 0) {
            Toast.makeText(HouseLoanCalculatorActivity.this, "贷款金额不能为0", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(HAFTime)) {
            HAFTime = DEFAULT_YEAR;
        }

        if (TextUtils.isEmpty(HAFRate)) {
            HAFRate = DEFAULT_RATE;
        }
        return true;
    }

    /**
     * 跳转到商业贷款详情页面
     */
    private void toResultActivity() {
        Intent intent1 = new Intent();
        intent1.setClass(HouseLoanCalculatorActivity.this, CommerceLoanResultActivity.class);      //跳转到结果列表
        Bundle bundle1 = new Bundle();
        bundle1.putString("mortgage", CommMortgage);
        bundle1.putString("time", CommTime);
        bundle1.putString("rate", CommRate);
        bundle1.putString("aheadTime", "0");
        bundle1.putInt("firstYear", CommFirstYear);
        bundle1.putInt("firstMonth", CommFirstMonth);
        bundle1.putInt("paybackMethod", CommPaybackMethod);
        bundle1.putInt("calculationMethod", 0);
        intent1.putExtras(bundle1);
        startActivity(intent1);
    }

    /**
     * 检验商业贷款填写是否合格
     */
    private boolean checkCommercialLoan() {
        CommMortgage = CommMortgageEditText.getText().toString();
        if (TextUtils.isEmpty(CommMortgage)) {
            Toast.makeText(HouseLoanCalculatorActivity.this, "请填写贷款金额", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(CommRate)) {
            CommRate = DEFAULT_RATE;
        }
        if (TextUtils.isEmpty(CommTime)) {
            CommTime = DEFAULT_YEAR;
        }

        if (Double.valueOf(CommMortgage) == 0) {
            Toast.makeText(HouseLoanCalculatorActivity.this, "贷款金额不能为0", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //4.设置弹窗
    public void initPopWindow() {
        //装载商业贷款年限菜单
        initLoanYear();
        //利率菜单
        initLoanRate();
    }

    /**
     * 装载利率
     */
    private void initLoanRate() {
        int size = rates.length;
        for (int i = 0; i < size; i++) {
            HouseLoanBean bean = new HouseLoanBean();
            bean.setContent(rateString[i]);
            bean.setRate(rates[i]);
            if (i == 0) {
                bean.setSelect(true);
            } else {
                bean.setSelect(false);
            }
            rateLists.add(bean);
        }
        llSYRateContainer.setOnClickListener(this);
        llGJJRateContainer.setOnClickListener(this);
        llZHGJJRate.setOnClickListener(this);
        llZHRate.setOnClickListener(this);
    }


    /**
     * 显示利率弹窗
     *
     * @param rateList 利率data
     * @param type 利率类型
     */
    private void showRateDialog(final List<HouseLoanBean> rateList, final int type) {
        PopDialog mDialog = new PopDialog.Builder(getSupportFragmentManager(), HouseLoanCalculatorActivity.this)
                .setLayoutId(R.layout.dialog_house_loan_rate)
                .setCancelableOutside(true)
                .setGravity(Gravity.BOTTOM)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, final PopDialog dialog) {
                        //此处可以设置文字的大小、颜色，按钮点击事件
                        initRateList(view, type, rateList, dialog);
                        initCustomRate(view, type, dialog);
                    }
                })
                .create();
        mDialog.show();
    }

    /**
     * 装载弹窗自定义利率
     *
     * @param view 利率弹窗控件
     * @param type 利率类型
     * @param dialog 弹窗引用
     */
    private void initCustomRate(View view, final int type, final PopDialog dialog) {
        final EditText etRate = view.findViewById(R.id.et_rate);
        TextView tvEnsure = view.findViewById(R.id.tv_ensure);
        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case 1://商业
                        CommRate = etRate.getText().toString() + "";
                        tvSYRate.setText("基准利率(" + CommRate + "%)");
                        break;
                    case 2://公积金
                        HAFRate = etRate.getText().toString() + "";
                        tvGJJRate.setText("基准利率(" + HAFRate + "%)");
                        break;
                    case 3:
                        CombHAFRate = etRate.getText().toString() + "";
                        tvZHGJJRate.setText("基准利率(" + CombHAFRate + "%)");
                        break;
                    case 4:
                        CombCommRate = etRate.getText().toString();
                        tvZHRate.setText("基准利率(" + CombCommRate + "%)");
                        break;
                        default:
                            break;
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 装载弹窗利率列表
     *
     * @param view 利率弹窗控件
     * @param type 贷款类型
     * @param rateList 利率data
     * @param dialog 弹窗引用
     */
    private void initRateList(View view, final int type, List<HouseLoanBean> rateList, final PopDialog dialog) {
        RecyclerView rateRecycler = view.findViewById(R.id.rate_recycler);
        HouseLoanAdapter adapter = new HouseLoanAdapter(HouseLoanCalculatorActivity.this);
        adapter.setList(rateList);
        LinearLayoutManager manager = new LinearLayoutManager(HouseLoanCalculatorActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rateRecycler.setLayoutManager(manager);
        rateRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new HouseLoanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HouseLoanBean bean) {
                switch (type) {
                    case 1://商业贷款
                        CommRate = bean.getRate() + "";
                        tvSYRate.setText(bean.getContent());
                        break;
                    case 2://公积金贷款
                        CombHAFRate = bean.getRate() + "";
                        tvGJJRate.setText(bean.getContent());
                        break;
                    case 3://组合贷款-公积金
                        CombHAFRate = bean.getRate() + "";
                        tvZHGJJRate.setText(bean.getContent());
                        break;
                    case 4://组合贷款-商贷
                        CombCommRate = bean.getRate() + "";
                        tvZHRate.setText(bean.getContent());
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 装载年限
     */
    private void initLoanYear() {
        yearList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            HouseLoanBean bean = new HouseLoanBean();
            bean.setContent((i + 1) * 5 + "年 (" + (i + 1) * 12 + "期)");
            bean.setValue((i + 1) * 5);
            if (i == 5) {
                bean.setSelect(true);
            } else {
                bean.setSelect(false);
            }
            yearList.add(bean);
        }

        rlYearContainer.setOnClickListener(this);
        rlYearGJJContainer.setOnClickListener(this);
        rlYearZHContainer.setOnClickListener(this);
    }

    /**
     * 显示年限弹窗
     *
     * @param yearList 贷款年限data
     */
    private void showYearDialog(final List<HouseLoanBean> yearList, final int type) {
        PopDialog mDialog = new PopDialog.Builder(getSupportFragmentManager(), HouseLoanCalculatorActivity.this)
                .setLayoutId(R.layout.dialog_house_loan_year)
                .setCancelableOutside(true)
                .setGravity(Gravity.BOTTOM)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, final PopDialog dialog) {
                        //此处可以设置文字的大小、颜色，按钮点击事件
                        RecyclerView yearRecycler = view.findViewById(R.id.year_recycler);
                        HouseLoanAdapter adapter = new HouseLoanAdapter(HouseLoanCalculatorActivity.this);
                        adapter.setList(yearList);
                        LinearLayoutManager manager = new LinearLayoutManager(HouseLoanCalculatorActivity.this);
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        yearRecycler.setLayoutManager(manager);
                        yearRecycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClickListener(new HouseLoanAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(HouseLoanBean bean) {
                                switch (type) {
                                    case 1:
                                        CommTime = String.valueOf(bean.getValue());
                                        tvYear.setText(CommTime + "年(" + (bean.getValue() * 12) + "期)");
                                        break;
                                    case 2:
                                        HAFTime = String.valueOf(bean.getValue());
                                        tvYearGJJ.setText(HAFTime + "年(" + (bean.getValue() * 12) + "期)");
                                        break;
                                    case 3:
                                        CombTime = String.valueOf(bean.getValue());
                                        tvZHYear.setText(CombTime + "年(" + (bean.getValue() * 12) + "期)");
                                        break;
                                    default:
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .create();
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigate:
                finish();
                break;

                /*贷款年限*/
            case R.id.rl_year_container:
                showYearDialog(yearList, 1);
                break;
            case R.id.rl_gjj_year_container:
                showYearDialog(yearList, 2);
                break;
            case R.id.rl_zh_year_container:
                showYearDialog(yearList, 3);
                break;

                /*贷款利率*/
            case R.id.ll_sy_rate_container:
                showRateDialog(rateLists, 1);//商业贷款
                break;
            case R.id.ll_gjj_rate_container:
                showRateDialog(rateLists, 2);//公积金贷款
                break;
            case R.id.ll_zh_gjj_rate_container:
                showRateDialog(rateLists, 3);//组合贷款-公积金贷款
                break;
            case R.id.ll_zh_rate_container:
                showRateDialog(rateLists, 4);//组合贷款-商业贷款
                break;

                /*Tab*/
            case R.id.tab_comm://商业贷款
                viewPager.setCurrentItem(0);
                clearTabState();
                setSelectState(0);
                currentItem = 0;
                break;
            case R.id.tab_haf://公积金贷款
                viewPager.setCurrentItem(1);
                clearTabState();
                setSelectState(1);
                currentItem = 1;
                break;
            case R.id.tab_comb://组合贷款
                viewPager.setCurrentItem(2);
                clearTabState();
                setSelectState(2);
                currentItem = 2;
                break;

            /*商业贷款方式*/
            case R.id.iv_comm_payback_interest:
                ivPaybackCommInterest.setBackgroundResource(R.drawable.btn_on);
                ivPaybackCommPrincipal.setBackgroundResource(R.drawable.btn_off);
                CommPaybackMethod = 0;
                break;
            case R.id.iv_comm_payback_principal:
                ivPaybackCommInterest.setBackgroundResource(R.drawable.btn_off);
                ivPaybackCommPrincipal.setBackgroundResource(R.drawable.btn_on);
                CommPaybackMethod = 1;
                /*公积金贷款方式*/
                break;
            case R.id.iv_haf_payback_interest:
                ivPaybackHafInterest.setBackgroundResource(R.drawable.btn_on);
                ivPaybackHafPrincipal.setBackgroundResource(R.drawable.btn_off);
                HAFPaybackMethod = 0;
                break;
            case R.id.iv_haf_payback_principal:
                ivPaybackHafInterest.setBackgroundResource(R.drawable.btn_off);
                ivPaybackHafPrincipal.setBackgroundResource(R.drawable.btn_on);
                HAFPaybackMethod = 1;
                break;
            /*组合贷款方式*/
            case R.id.iv_comb_payback_interest:
                ivPaybackCombInterest.setBackgroundResource(R.drawable.btn_on);
                ivPaybackCombPrincipal.setBackgroundResource(R.drawable.btn_off);
                CombPaybackMethod = 0;
                break;
            case R.id.iv_comb_payback_principal:
                ivPaybackCombInterest.setBackgroundResource(R.drawable.btn_off);
                ivPaybackCombPrincipal.setBackgroundResource(R.drawable.btn_on);
                CombPaybackMethod = 1;
                break;

            /*开始计算*/
            case R.id.tv_calculate:
                startCalculation();
                break;
            default:
                break;
        }
    }

    /**
     * 清除Tab状态
     */
    private void clearTabState() {
        tvCommTab.setTextColor(getResources().getColor(R.color.c_909298));
        tvHafTab.setTextColor(getResources().getColor(R.color.c_909298));
        tvCombTab.setTextColor(getResources().getColor(R.color.c_909298));

        indicateComm.setBackgroundColor(getResources().getColor(R.color.c_909298));
        indicateHaf.setBackgroundColor(getResources().getColor(R.color.c_909298));
        indicateComb.setBackgroundColor(getResources().getColor(R.color.c_909298));

        indicateComm.setVisibility(View.GONE);
        indicateHaf.setVisibility(View.GONE);
        indicateComb.setVisibility(View.GONE);
    }

    /**
     * 设置Tab选中状态
     *
     * @param position tab位置
     */
    private void setSelectState(int position) {
        switch (position) {
            case 0:
                tvCommTab.setTextColor(getResources().getColor(R.color.c_ff5240));
                indicateComm.setBackgroundColor(getResources().getColor(R.color.c_ff5240));
                indicateComm.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvHafTab.setTextColor(getResources().getColor(R.color.c_ff5240));
                indicateHaf.setBackgroundColor(getResources().getColor(R.color.c_ff5240));
                indicateHaf.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvCombTab.setTextColor(getResources().getColor(R.color.c_ff5240));
                indicateComb.setBackgroundColor(getResources().getColor(R.color.c_ff5240));
                indicateComb.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 开始计算
     */
    private void startCalculation() {
        switch (currentItem) {
            case 0:
                //按贷款金额计算
                if (checkCommercialLoan()) {
                    toResultActivity();
                }
                break;
            case 1:
                //按贷款金额计算
                if (checkGongjijinLoan()) {
                    toResult2Activity();
                }
                break;
            case 2:
                //按贷款金额计算
                if (checkGroupLoan()) {
                    toResult3Activity();
                }
                break;
                default:
                    break;
        }
    }

    /**
     * ViewPager监听器
     */
    public class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            clearTabState();
            setSelectState(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
