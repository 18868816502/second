package com.beiwo.klyjaz.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
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

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.HouseLoanBean;
import com.beiwo.klyjaz.ui.adapter.houseloan.HouseLoanAdapter;
import com.beiwo.klyjaz.ui.adapter.houseloan.HouseLoanVPAdapter;
import com.beiwo.klyjaz.view.dialog.EditInputFilter;
import com.beiwo.klyjaz.view.dialog.PopDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @name
 * @class name：com.beihui.market
 * @class describe 商业贷款给和公积金贷款计算详情页面
 * @anthor chenguoguo
 * @time
 */
public class HouseLoanCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 默认利率
     */
    private static final String DEFAULT_RATE = "4.9";//默认利率
    /**
     * 默认年限
     */
    private static final String DEFAULT_YEAR = "30";//默认年限
    /**
     * 滑动控件位置
     */
    private int currentItem = 0;
    /**
     * 贷款时间（年）
     */
    private String commTime;
    /**
     * 贷款金额（万元）
     */
    private String commMortgage;
    /**
     * 贷款年利率（%）
     */
    private String commRate;
    /**
     * 还款方式（等额本息、等额本金）
     */
    private int commPaybackMethod = 0;
    /**
     * 贷款时间（年）
     */
    private String mHAFTime;
    /**
     * 贷款金额（万元）
     */
    private String mHAFMortgage;
    /**
     * 贷款年利率（%）
     */
    private String mHAFRate;
    /**
     * 还款方式（等额本息、等额本金）
     */
    private int mHAFPaybackMethod = 0;

    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.navigate)
    ImageView ivBack;
    @BindView(R.id.tab_comm)
    LinearLayout tabCommContainer;
    @BindView(R.id.tv_commb_tab)
    TextView tvCommTab;
    @BindView(R.id.indicate_comm)
    View indicateComm;

    private ImageView ivPaybackCommInterest;
    private ImageView ivPaybackCommPrincipal;

    @BindView(R.id.tab_haf)
    LinearLayout tabHafContainer;
    @BindView(R.id.tv_HAF_tab)
    TextView tvHafTab;
    @BindView(R.id.indicate_haf)
    View indicateHaf;

    private ImageView ivPaybackHafInterest;
    private ImageView ivPaybackHafPrincipal;

    @BindView(R.id.tab_comb)
    LinearLayout tabCombContainer;
    @BindView(R.id.tv_comb_tab)
    TextView tvCombTab;
    @BindView(R.id.indicate_comb)
    View indicateComb;

    private ImageView ivPaybackCombInterest;
    private ImageView ivPaybackCombPrincipal;

    /**
     * 滑动控件
     */
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    /**
     * 计算按钮
     */
    @BindView(R.id.tv_calculate)
    TextView tvCalculate;

    //商业贷款部分
    private RelativeLayout rlYearContainer;
    private TextView tvYear;
    private LinearLayout llSYRateContainer;
    private TextView tvSYRate;
    private EditText etCommMortgage;//按贷款金额计算

    //公积金贷款部分
    private RelativeLayout rlYearGJJContainer;
    private TextView tvYearGJJ;
    private LinearLayout llGJJRateContainer;
    private TextView tvGJJRate;
    private EditText etHAFMortgage;//按贷款金额计算

    //组合贷款部分
    private RelativeLayout rlYearZHContainer;
    private TextView tvZHYear;
    private LinearLayout llZHRate;
    private TextView tvZHRate;
    private LinearLayout llZHGJJRate;
    private TextView tvZHGJJRate;
    private EditText etCombMortgageHAF;//公积金贷款
    private EditText etCombMortgageComm;//商业贷款

    /**
     * 贷款时间（年）
     */
    private String combTime;
    /**
     * 贷款总额（万元）
     */
    private String combPay;
    /**
     * 商业贷款金额（万元）
     */
    private String combMortgageComm;
    /**
     * 公积金贷款金额（万元）
     */
    private String combMortgageHAF;
    /**
     * 公积金贷款年利率（%）
     */
    private String combHAFRate;
    /**
     * 商业贷款年利率（%）
     */
    private String combCommRate;
    /**
     * 还款方式（等额本息、等额本金）
     */
    private int combPaybackMethod = 0;

    /**
     * 贷款年限
     */
    private List<HouseLoanBean> sdYearLists;
    private List<HouseLoanBean> gjjYearLists;
    private List<HouseLoanBean> combYearLists;

    /**
     * 商贷利率
     */
    private String[] sdRateStrings;
    private Double[] sdRates = {4.95, 3.43, 4.165, 4.312, 4.41, 5.39, 5.88, 6.37};
    private List<HouseLoanBean> sdRateLists = new ArrayList<>();
    private List<HouseLoanBean> combSdRateLists = new ArrayList<>();

    /**
     * 公积金利率
     */
    private String[] gjjRateStrings;
    private Double[] gjjRates = {3.25, 2.275, 2.7625, 2.86, 2.925, 3.575, 3.9, 4.225};
    private List<HouseLoanBean> gjjRateLists = new ArrayList<>();
    private List<HouseLoanBean> combGjjRateLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_loan);
        ButterKnife.bind(this);

        sdRateStrings = getResources().getStringArray(R.array.loan_sd_rate_content);
        gjjRateStrings = getResources().getStringArray(R.array.loan_gjj_rate_content);
        initViews();
        setListeners();
        initPopWindow();
    }

    //2.初始化ViewPager
    public void initViews() {
        List<View> viewList = new ArrayList<>();
        //三个标题
        View commercialLoanView = LayoutInflater.from(this).inflate(R.layout.viewpager_main_commercialloan, rlContainer, false);
        View HAFView = LayoutInflater.from(this).inflate(R.layout.viewpager_main_haf, rlContainer, false);
        View combinationView = LayoutInflater.from(this).inflate(R.layout.viewpager_main_combination, rlContainer, false);

        //商业贷款页面控件
        rlYearContainer = commercialLoanView.findViewById(R.id.rl_year_container);
        tvYear = commercialLoanView.findViewById(R.id.tv_year);
        llSYRateContainer = commercialLoanView.findViewById(R.id.ll_sy_rate_container);
        tvSYRate = commercialLoanView.findViewById(R.id.tv_sy_rate);
        etCommMortgage = commercialLoanView.findViewById(R.id.Commercial_MortgageEditText);
        InputFilter[] filters = {new EditInputFilter(EditInputFilter.MAX_VALUE)};
        etCommMortgage.setFilters(filters);
        ivPaybackCommInterest = commercialLoanView.findViewById(R.id.iv_comm_payback_interest);
        ivPaybackCommPrincipal = commercialLoanView.findViewById(R.id.iv_comm_payback_principal);

        //公积金贷款页面
        rlYearGJJContainer = HAFView.findViewById(R.id.rl_gjj_year_container);
        tvYearGJJ = HAFView.findViewById(R.id.tv_gjj_year);
        llGJJRateContainer = HAFView.findViewById(R.id.ll_gjj_rate_container);
        tvGJJRate = HAFView.findViewById(R.id.tv_gjj_rate);
        etHAFMortgage = HAFView.findViewById(R.id.HAF_MortgageEditText);
        etHAFMortgage.setFilters(filters);
        ivPaybackHafInterest = HAFView.findViewById(R.id.iv_haf_payback_interest);
        ivPaybackHafPrincipal = HAFView.findViewById(R.id.iv_haf_payback_principal);

        //组合贷款页面
        rlYearZHContainer = combinationView.findViewById(R.id.rl_zh_year_container);
        tvZHYear = combinationView.findViewById(R.id.tv_zh_year);
        llZHGJJRate = combinationView.findViewById(R.id.ll_zh_gjj_rate_container);
        tvZHGJJRate = combinationView.findViewById(R.id.tv_zh_gjj_rate);
        llZHRate = combinationView.findViewById(R.id.ll_zh_rate_container);
        tvZHRate = combinationView.findViewById(R.id.tv_zh_rate);
        etCombMortgageHAF = combinationView.findViewById(R.id.Combination_Mortgage_HAF_EditText);
        etCombMortgageComm = combinationView.findViewById(R.id.Combination_Mortgage_Comm_EditText);
        etCombMortgageHAF.setFilters(filters);
        etCombMortgageComm.setFilters(filters);
        ivPaybackCombInterest = combinationView.findViewById(R.id.iv_comb_payback_interest);
        ivPaybackCombPrincipal = combinationView.findViewById(R.id.iv_comb_payback_principal);

        viewList.add(commercialLoanView);
        viewList.add(HAFView);
        viewList.add(combinationView);

        HouseLoanVPAdapter pagerAdapter = new HouseLoanVPAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.addOnPageChangeListener(new PageChangeListener());        //2-1.ViewPager的监听器
    }

    //3.设置监听器
    public void setListeners() {
        tabCommContainer.setOnClickListener(this);
        tabHafContainer.setOnClickListener(this);
        tabCombContainer.setOnClickListener(this);
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
        Intent intent = new Intent();
        intent.setClass(this, CombinationLoanResultActivity.class);        //跳转到结果列表
        Bundle bundle = new Bundle();
        bundle.putString("mortgage", combPay);                                         //贷款总额
        bundle.putString("HAFMortgage", combMortgageHAF);                              //公积金贷款
        bundle.putString("commMortgage", combMortgageComm);                            //商业贷款
        bundle.putString("time", combTime);
        bundle.putString("HAFRate", combHAFRate);                                      //公积金利率
        bundle.putString("commRate", combCommRate);                                    //商业利率
        bundle.putString("aheadTime", "0");
        bundle.putInt("paybackMethod", combPaybackMethod);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 检验组合贷款填写是否合格
     *
     * @return 是否通过
     */
    private boolean checkGroupLoan() {
        combMortgageHAF = etCombMortgageHAF.getText().toString();
        combMortgageComm = etCombMortgageComm.getText().toString();
        if (TextUtils.isEmpty(combMortgageHAF)) {
            Toast.makeText(this, getString(R.string.loan_gjj_empty_tips), Toast.LENGTH_SHORT).show();
            etCombMortgageHAF.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(combMortgageComm)) {
            Toast.makeText(this, getString(R.string.loan_sd_empty_tips), Toast.LENGTH_SHORT).show();
            etCombMortgageComm.requestFocus();
            return false;
        }
        double sum = Double.valueOf(combMortgageHAF) + Double.valueOf(combMortgageComm);
        combPay = String.valueOf(sum);
        if (Double.valueOf(combMortgageHAF) == 0) {
            Toast.makeText(this, getString(R.string.loan_zero_tips), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(combTime)) {
            combTime = DEFAULT_YEAR;
        }

        if (TextUtils.isEmpty(combHAFRate)) {
            combHAFRate = DEFAULT_RATE;
        }

        if (TextUtils.isEmpty(combCommRate)) {
            combCommRate = DEFAULT_RATE;
        }
        return true;
    }

    /**
     * 跳转到公积金贷款详情页
     */
    private void toResult2Activity() {
        Intent intent = new Intent();
        intent.setClass(this, CommerceLoanResultActivity.class);      //跳转到结果列表
        Bundle bundle = new Bundle();
        bundle.putString("mortgage", mHAFMortgage);
        bundle.putString("time", mHAFTime);
        bundle.putString("rate", mHAFRate);
        bundle.putString("aheadTime", "0");
        bundle.putInt("paybackMethod", mHAFPaybackMethod);
        bundle.putInt("calculationMethod", 1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 检查公积金贷款是否合格
     *
     * @return 是否通过
     */
    private boolean checkGongjijinLoan() {
        mHAFMortgage = etHAFMortgage.getText().toString();
        if (TextUtils.isEmpty(mHAFMortgage)) {
            Toast.makeText(this, getString(R.string.loan_sd_empty_tips), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Double.valueOf(mHAFMortgage) == 0) {
            Toast.makeText(this, getString(R.string.loan_zero_tips), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mHAFTime)) {
            mHAFTime = DEFAULT_YEAR;
        }

        if (TextUtils.isEmpty(mHAFRate)) {
            mHAFRate = DEFAULT_RATE;
        }
        return true;
    }

    /**
     * 跳转到商业贷款详情页面
     */
    private void toResultActivity() {
        Intent intent = new Intent();
        intent.setClass(this, CommerceLoanResultActivity.class);      //跳转到结果列表
        Bundle bundle = new Bundle();
        bundle.putString("mortgage", commMortgage);
        bundle.putString("time", commTime);
        bundle.putString("rate", commRate);
        bundle.putString("aheadTime", "0");
        bundle.putInt("paybackMethod", commPaybackMethod);
        bundle.putInt("calculationMethod", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 检验商业贷款填写是否合格
     */
    private boolean checkCommercialLoan() {
        commMortgage = etCommMortgage.getText().toString();
        if (TextUtils.isEmpty(commMortgage)) {
            Toast.makeText(this, getString(R.string.loan_sd_empty_tips), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(commRate)) {
            commRate = DEFAULT_RATE;
        }
        if (TextUtils.isEmpty(commTime)) {
            commTime = DEFAULT_YEAR;
        }

        if (Double.valueOf(commMortgage) == 0) {
            Toast.makeText(this, getString(R.string.loan_zero_tips), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //4.设置弹窗
    public void initPopWindow() {
        initLoanYear();
        initLoanRate();
    }

    /**
     * 装载利率
     */
    private void initLoanRate() {
        //装载商贷利率data
        int size = sdRates.length;
        sdRateLists = getRateLists(size, sdRateStrings, sdRates);
        combSdRateLists = getRateLists(size, sdRateStrings, sdRates);

        //装载公积金利率data
        int length = sdRates.length;
        gjjRateLists = getRateLists(length, gjjRateStrings, gjjRates);
        combGjjRateLists = getRateLists(length, gjjRateStrings, gjjRates);

        llSYRateContainer.setOnClickListener(this);
        llGJJRateContainer.setOnClickListener(this);
        llZHGJJRate.setOnClickListener(this);
        llZHRate.setOnClickListener(this);
    }

    /**
     * 获取利率（公积金利率和商业利率）
     *
     * @param size        默认数据大小
     * @param rateStrings 利率填充内容数组
     * @param rates       利率值数组
     * @return 返回利率数组
     */
    private List<HouseLoanBean> getRateLists(int size, String[] rateStrings, Double[] rates) {
        List<HouseLoanBean> rateList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            HouseLoanBean bean = new HouseLoanBean();
            bean.setContent(rateStrings[i]);
            bean.setRate(rates[i]);
            if (i == 0) {
                bean.setSelect(true);
            } else {
                bean.setSelect(false);
            }
            rateList.add(bean);
        }
        return rateList;
    }


    /**
     * 显示利率弹窗
     *
     * @param rateList 利率data
     * @param type     利率类型
     */
    private void showRateDialog(final List<HouseLoanBean> rateList, final int type) {
        PopDialog mDialog = new PopDialog.Builder(getSupportFragmentManager(), this)
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
     * @param view   利率弹窗控件
     * @param type   利率类型
     * @param dialog 弹窗引用
     */
    private void initCustomRate(View view, final int type, final PopDialog dialog) {
        final EditText etRate = view.findViewById(R.id.et_rate);
        InputFilter[] filters = {new EditInputFilter(EditInputFilter.MAX_VALUE_PERCENT)};
        etRate.setFilters(filters);
        TextView tvEnsure = view.findViewById(R.id.tv_ensure);
        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case 1://商业
                        commRate = etRate.getText().toString() + "";
                        tvSYRate.setText(getRateString(commRate));
                        clearRateState(sdRateLists);
                        break;
                    case 2://公积金
                        mHAFRate = etRate.getText().toString() + "";
                        tvGJJRate.setText(getRateString(mHAFRate));
                        clearRateState(gjjRateLists);
                        break;
                    case 3:
                        combHAFRate = etRate.getText().toString() + "";
                        tvZHGJJRate.setText(getRateString(combHAFRate));
                        clearRateState(combGjjRateLists);
                        break;
                    case 4:
                        combCommRate = etRate.getText().toString();
                        tvZHRate.setText(getRateString(combCommRate));
                        clearRateState(combSdRateLists);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 获取拼接好的利率字符串
     *
     * @param rateValue 利率值
     * @return string
     */
    private String getRateString(String rateValue) {
        return getString(R.string.loan_base_rate) + rateValue + getString(R.string.loan_base_rate_percent);
    }

    /**
     * 装载弹窗利率列表
     *
     * @param view     利率弹窗控件
     * @param type     贷款类型
     * @param rateList 利率data
     * @param dialog   弹窗引用
     */
    private void initRateList(View view, final int type, final List<HouseLoanBean> rateList, final PopDialog dialog) {
        RecyclerView rateRecycler = view.findViewById(R.id.rate_recycler);
        final HouseLoanAdapter adapter = new HouseLoanAdapter(this);
        adapter.setList(rateList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rateRecycler.setLayoutManager(manager);
        rateRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new HouseLoanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HouseLoanBean bean, int position) {
                switch (type) {
                    case 1://商业贷款
                        commRate = bean.getRate() + "";
                        tvSYRate.setText(bean.getContent());
                        setRateState(sdRateLists, position);
                        break;
                    case 2://公积金贷款
                        combHAFRate = bean.getRate() + "";
                        tvGJJRate.setText(bean.getContent());
                        setRateState(gjjRateLists, position);
                        break;
                    case 3://组合贷款-公积金
                        combHAFRate = bean.getRate() + "";
                        tvZHGJJRate.setText(bean.getContent());
                        setRateState(combGjjRateLists, position);
                        break;
                    case 4://组合贷款-商贷
                        combCommRate = bean.getRate() + "";
                        tvZHRate.setText(bean.getContent());
                        setRateState(combSdRateLists, position);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    private void setRateState(List<HouseLoanBean> list, int position) {
        clearRateState(list);
        list.get(position).setSelect(true);
    }

    private void clearRateState(List<HouseLoanBean> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelect(false);
        }
    }

    /**
     * 装载年限
     */
    private void initLoanYear() {
        sdYearLists = new ArrayList<>();
        gjjYearLists = new ArrayList<>();
        combYearLists = new ArrayList<>();
        sdYearLists = getYearLists();
        gjjYearLists = getYearLists();
        combYearLists = getYearLists();

        rlYearContainer.setOnClickListener(this);
        rlYearGJJContainer.setOnClickListener(this);
        rlYearZHContainer.setOnClickListener(this);
    }

    /**
     * 获取贷款年限数据
     *
     * @return list
     */
    private List<HouseLoanBean> getYearLists() {
        List<HouseLoanBean> yearList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            HouseLoanBean bean = new HouseLoanBean();
            bean.setContent((i + 1) * 5 + getString(R.string.loan_year) + (i + 1) * 5 * 12 + getString(R.string.loan_phase));
            bean.setValue((i + 1) * 5);
            if (i == 5) {
                bean.setSelect(true);
            } else {
                bean.setSelect(false);
            }
            yearList.add(bean);
        }
        return yearList;
    }

    /**
     * 显示年限弹窗
     *
     * @param yearList 贷款年限data
     */
    private void showYearDialog(final List<HouseLoanBean> yearList, final int type) {
        PopDialog mDialog = new PopDialog.Builder(getSupportFragmentManager(), this)
                .setLayoutId(R.layout.dialog_house_loan_year)
                .setCancelableOutside(true)
                .setGravity(Gravity.BOTTOM)
                .setInitPopListener(new PopDialog.OnInitPopListener() {
                    @Override
                    public void initPop(View view, final PopDialog dialog) {
                        //此处可以设置文字的大小、颜色，按钮点击事件
                        RecyclerView yearRecycler = view.findViewById(R.id.year_recycler);
                        final HouseLoanAdapter adapter = new HouseLoanAdapter(getApplicationContext());
                        adapter.setList(yearList);
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        yearRecycler.setLayoutManager(manager);
                        yearRecycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClickListener(new HouseLoanAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(HouseLoanBean bean, int position) {
                                switch (type) {
                                    case 1:
                                        commTime = String.valueOf(bean.getValue());
                                        tvYear.setText(getString(R.string.loan_year_text, commTime, (bean.getValue() * 12)));
                                        setYearState(sdYearLists, position);
                                        break;
                                    case 2:
                                        mHAFTime = String.valueOf(bean.getValue());
                                        tvYearGJJ.setText(getString(R.string.loan_year_text, mHAFTime, (bean.getValue() * 12)));
                                        setYearState(gjjYearLists, position);
                                        break;
                                    case 3:
                                        combTime = String.valueOf(bean.getValue());
                                        tvZHYear.setText(getString(R.string.loan_year_text, combTime, (bean.getValue() * 12)));
                                        setYearState(combYearLists, position);
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

    /**
     * 设置贷款年限选中状态
     *
     * @param list     年限数据列表
     * @param position 选中位置
     */
    private void setYearState(List<HouseLoanBean> list, int position) {
        clearYearState(list);
        list.get(position).setSelect(true);
    }

    /**
     * 清除贷款年限选中状态
     *
     * @param list 年限数据列表
     */
    private void clearYearState(List<HouseLoanBean> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelect(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigate:
                finish();
                break;

                /*贷款年限*/
            case R.id.rl_year_container:
                showYearDialog(sdYearLists, 1);
                break;
            case R.id.rl_gjj_year_container:
                showYearDialog(gjjYearLists, 2);
                break;
            case R.id.rl_zh_year_container:
                showYearDialog(combYearLists, 3);
                break;

                /*贷款利率*/
            case R.id.ll_sy_rate_container:
                showRateDialog(sdRateLists, 1);//商业贷款
                break;
            case R.id.ll_gjj_rate_container:
                showRateDialog(gjjRateLists, 2);//公积金贷款
                break;
            case R.id.ll_zh_gjj_rate_container:
                showRateDialog(combGjjRateLists, 3);//组合贷款-公积金贷款
                break;
            case R.id.ll_zh_rate_container:
                showRateDialog(combSdRateLists, 4);//组合贷款-商业贷款
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
                commPaybackMethod = 0;
                break;
            case R.id.iv_comm_payback_principal:
                ivPaybackCommInterest.setBackgroundResource(R.drawable.btn_off);
                ivPaybackCommPrincipal.setBackgroundResource(R.drawable.btn_on);
                commPaybackMethod = 1;
                /*公积金贷款方式*/
                break;
            case R.id.iv_haf_payback_interest:
                ivPaybackHafInterest.setBackgroundResource(R.drawable.btn_on);
                ivPaybackHafPrincipal.setBackgroundResource(R.drawable.btn_off);
                mHAFPaybackMethod = 0;
                break;
            case R.id.iv_haf_payback_principal:
                ivPaybackHafInterest.setBackgroundResource(R.drawable.btn_off);
                ivPaybackHafPrincipal.setBackgroundResource(R.drawable.btn_on);
                mHAFPaybackMethod = 1;
                break;
            /*组合贷款方式*/
            case R.id.iv_comb_payback_interest:
                ivPaybackCombInterest.setBackgroundResource(R.drawable.btn_on);
                ivPaybackCombPrincipal.setBackgroundResource(R.drawable.btn_off);
                combPaybackMethod = 0;
                break;
            case R.id.iv_comb_payback_principal:
                ivPaybackCombInterest.setBackgroundResource(R.drawable.btn_off);
                ivPaybackCombPrincipal.setBackgroundResource(R.drawable.btn_on);
                combPaybackMethod = 1;
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
