package com.beihui.market.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.AccountFlowIconBean;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtChannelComponent;
import com.beihui.market.injection.module.DebtChannelModule;
import com.beihui.market.ui.adapter.AccountFlowLoanRvAdapter;
import com.beihui.market.ui.adapter.AccountFlowLoanSearchAdapter;
import com.beihui.market.ui.contract.DebtChannelContract;
import com.beihui.market.ui.dialog.AccountFlowRemarkDialog;
import com.beihui.market.ui.presenter.DebtChannelPresenter;
import com.beihui.market.ui.rvdecoration.AccountFlowLoanItemDeco;
import com.beihui.market.ui.rvdecoration.AccountFlowLoanStickyHeaderItemDeco;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtils;
import com.beihui.market.view.AlphabetIndexBar;
import com.beihui.market.view.AutoAdjustSizeEditText;
import com.beihui.market.view.customekeyboard.CustomBaseKeyboard;
import com.beihui.market.view.customekeyboard.CustomKeyboardManager;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.beihui.market.view.pickerview.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2018/6/15.
 */

public class AccountFlowLoanFragment extends BaseComponentFragment implements DebtChannelContract.View {

    private final String[] mRepaymentCycle = {"每月", "每两月", "每三月", "每四月", "每五月", "每六月", "每七月", "每八月", "每九月", "每十月", "每十一月", "每年"};

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private final SimpleDateFormat saveDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @BindView(R.id.auto_et_num)
    public AutoAdjustSizeEditText etInputPrice;
    @BindView(R.id.rv_fg_account_flow_loan_search)
    RecyclerView recyclerViewSearch;
    @BindView(R.id.rv_fg_account_flow_loan)
    RecyclerView recyclerView;
    @BindView(R.id.alphabet_fg_account_flow_loan)
    AlphabetIndexBar alphabetIndexBar;

    @BindView(R.id.tv_fg_first_pay_loan_date)
    TextView mFirstPayLoanDate;
    @BindView(R.id.et_fg_accout_flow_loan)
    EditText etLoan;
    @BindView(R.id.ll_account_flow_bottom)
    LinearLayout mBottom;

    private AccountFlowLoanRvAdapter adapter;
    private AccountFlowLoanSearchAdapter adapterCustomeIcon;

    private FragmentActivity activity;

    //软键盘帮助类
    private KeyBoardHelper boardHelper;
    private AccountFlowRemarkDialog dialog;

    //自定义键盘管理
    public CustomKeyboardManager customKeyboardManager;

    @Inject
    DebtChannelPresenter presenter;
    private LinearLayoutManager manager;
    private List<AccountFlowIconBean> list = null;

    @Override
    public int getLayoutResId() {
        return R.layout.x_fragment_account_flow_loan;
    }

    public BigDecimal sum;
    public StringBuilder temp = new StringBuilder();

    private KeyBoardHelper.OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener = new KeyBoardHelper.OnKeyBoardStatusChangeListener() {

        /**
         * 软键盘弹出时候
         */
        @Override
        public void OnKeyBoardPop(int keyBoardHeight) {
            Log.e("dafs", "keyBoardHeight--> " + keyBoardHeight);
        }

        /**
         * 软件盘隐藏的时候
         */
        @Override
        public void OnKeyBoardClose(int oldKeyBoardHeight) {
            Log.e("dafs", "oldKeyBoardHeight--> " + oldKeyBoardHeight);
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    };

    @Override
    public void configViews() {
        activity = getActivity();
        mBottom.setVisibility(View.GONE);

        adapter = new AccountFlowLoanRvAdapter(R.layout.list_item_debt_channel);

        /**
         * 点击显示的列表平台
         */
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        recyclerView.addItemDecoration(new AccountFlowLoanItemDeco());
        recyclerView.addItemDecoration(new AccountFlowLoanStickyHeaderItemDeco(activity) {
            @Override
            public String getHeaderName(int pos) {
                return adapter.getItem(pos).iconInitials;
            }
        });
        manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        alphabetIndexBar.setAlphabetSelectedListener(new AlphabetIndexBar.AlphabetSelectedListener() {
            @Override
            public void onAlphabetSelected(int index, String alphabet) {

            }
        });

        /**
         * 列表滑动驱动自定义键盘的显示隐藏问题
         */
       recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
           }

           @Override
           public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               if (Math.abs(dy) <= 2) {
                   return;
               }
              if (dy > 0) {
                  //向上滚动  显示自定义键盘
                  customKeyboardManager.showSoftKeyboard(etInputPrice);
              } else {
                  customKeyboardManager.hideSoftKeyboard(etInputPrice, 1);
              }
           }
       });


        customKeyboardManager = new CustomKeyboardManager(activity);

        /**
         * 开启然键盘监听事件
         */
        boardHelper = new KeyBoardHelper(activity);
        boardHelper.onCreate();
        boardHelper.setOnKeyBoardStatusChangeListener(onKeyBoardStatusChangeListener);


        /**
         * 数字键盘
         * 48 : 0   50 : 2   57 : 9
         * 43 : +   45 : -
         * -5 : 退格  61 : =  46 : .
         */

        CustomBaseKeyboard priceKeyboard = new CustomBaseKeyboard(activity, R.xml.stock_price_num_keyboard) {
            @Override
            public boolean handleSpecialKey(EditText etCurrent, int primaryCode) {
                //绑定的输入框的字符序列
                String currentContent = etCurrent.getText().toString();
                //为空不拦截操作
                if (TextUtils.isEmpty(currentContent)) {
                    etCurrent.setText("0");
                    if (primaryCode == 48) {
                        return true;
                    }
                    return false;
                }

                //数字不拦截操作
                if (primaryCode >= 48 && primaryCode <= 57) {
                    if (currentContent.substring(1).contains("+") || currentContent.substring(1).contains("-")) {
                        if (temp.length()<=0 && primaryCode == 48) {
                            return true;
                        }
                        if ("0".equals(temp.toString()) && primaryCode == 48) {
                            return true;
                        }
                        if (temp.toString().contains(".")) {
                            int i = temp.toString().indexOf(".");
                            int length = temp.toString().length();
                            if (length - i >= 3) {
                                return true;
                            }
                        }
                        if (Double.parseDouble(temp.toString()+(primaryCode - 48)) > 999999999D) {
                            ToastUtils.showToast(activity, "输入的金额太大啦");
                            return true;
                        }
                        temp.append(primaryCode - 48);
                        return false;
                    } else {
                        if (etCurrent.getText().toString().contains(".")) {
                            int i = etCurrent.getText().toString().indexOf(".");
                            int length = etCurrent.getText().toString().length();
                            if (length - i >= 3) {
                                return true;
                            }
                        }
                        if ("0".equals(etCurrent.getText().toString()) && primaryCode == 48) {
                            return true;
                        }
                        String s = etCurrent.getText().toString() + (primaryCode - 48);
                        if (Double.parseDouble(s) > 999999999D) {
                            ToastUtils.showToast(activity, "输入的金额太大啦");
                            return true;
                        }
                        etCurrent.setText(s);
                        return true;
                    }
                }

                //如果是算术运算符
                if ((primaryCode == 43 || primaryCode == 45) && currentContent.length() > 1) {
                    if ((!currentContent.substring(1).contains("+") && !currentContent.substring(1).contains("-"))) {
                        sum = new BigDecimal(currentContent);
                    } else {
                        return true;
                    }
                    return false;
                }

                //如果是小数点
                if (primaryCode == 46) {
                    if ("+-".contains(currentContent.substring(currentContent.length() - 1, currentContent.length()))){
                        etCurrent.setText(currentContent+"0.");
                        temp.append("0.");
                        return true;
                    } else if (temp.length() > 0) {
                        if (!temp.toString().contains(".")) {
                            temp.append(".");
                            etCurrent.setText(currentContent+".");
                        }
                        return true;
                    } else if (sum != null && sum.toString().length() > 0 && temp.toString().contains(".")) {
                        return true;
                    }
                    return false;
                }

                //退格键
                if (primaryCode == -5) {
                    if (temp.toString().length() > 0 && (currentContent.substring(1).contains("+") || currentContent.substring(1).contains("-"))) {
                        temp.delete(temp.length() - 1, temp.length());
                    }
                    String substring = currentContent.substring(0, currentContent.length() - 1);
                    if (substring.length() == 0) {
                        etCurrent.setText("0");
                    } else {
                        etCurrent.setText(substring);
                    }
                    return true;
                }

                //求和
                if (primaryCode == 61) {
                    if (temp.length() > 0) {
                        if (temp.equals("0.")) {
                            temp.append("0");
                        }
                        if (currentContent.substring(1).contains("+")) {
                            sum = sum.add(new BigDecimal(temp.toString()));
                        }
                        if (currentContent.substring(1).contains("-")) {
                            sum = sum.subtract(new BigDecimal(temp.toString()));
                        }
                        temp.delete(0, temp.length());
                        etCurrent.setText(sum.toString());
                    }
                    return true;
                }
                return false;
            }
        };

        customKeyboardManager.attachTo(etInputPrice, priceKeyboard);
        customKeyboardManager.setShowUnderView(mBottom);


        etLoan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (marryList.size() > 0) {
                    marryList.clear();
                }

                if (list != null && list.size() > 0 && !TextUtils.isEmpty(etLoan.getText().toString())) {
                    for (int i = 0; i < list.size(); i++) {
                        if ((list.get(i).iconName).contains(etLoan.getText().toString())) {
                            marryList.add(list.get(i));
                        }
                    }
                    alphabetIndexBar.setVisibility(View.GONE);
                    adapter.notifyDebtChannelChanged(marryList);
                    if (marryList.size() == 0) {
                        showSearch();
                    } else {
                        recyclerViewSearch.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (!TextUtils.isEmpty(etLoan.getText().toString())) {
                        showSearch();
                    } else {
                        recyclerViewSearch.setVisibility(View.INVISIBLE);
                    }
                    alphabetIndexBar.setVisibility(View.VISIBLE);
                    adapter.notifyDebtChannelChanged(list);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void showSearch() {
        //搜索无记录
        Api.getInstance().queryLoanAccountIcon(UserHelper.getInstance(activity).getProfile().getId(), etLoan.getText().toString())
                .compose(RxUtil.<ResultEntity<List<LoanAccountIconBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<LoanAccountIconBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<LoanAccountIconBean>> result) throws Exception {
                                   recyclerViewSearch.setVisibility(View.VISIBLE);
                                   adapterCustomeIcon= new AccountFlowLoanSearchAdapter(activity);
                                   final GridLayoutManager manager = new GridLayoutManager(activity, 5);
                                   manager.setOrientation(GridLayoutManager.VERTICAL);
                                   manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                       @Override
                                       public int getSpanSize(int position) {
                                           return position == 0 ? manager.getSpanCount() : 1;
                                       }
                                   });
                                   recyclerViewSearch.setLayoutManager(manager);
                                   recyclerViewSearch.setAdapter(adapterCustomeIcon);
                                   adapterCustomeIcon.setData(result.getData());
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    public List<AccountFlowIconBean> marryList = new ArrayList<>();

    @Override
    public void initDatas() {
//        presenter.onStart();

        Api.getInstance().queryIconList(UserHelper.getInstance(activity).getProfile().getId(), "LNetLoan").compose(RxUtil.<ResultEntity<List<AccountFlowIconBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AccountFlowIconBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<AccountFlowIconBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (adapter != null) {
                                           list = result.getData();
                                           Collections.sort(list, new Comparator<AccountFlowIconBean>() {
                                               @Override
                                               public int compare(AccountFlowIconBean one, AccountFlowIconBean two) {
                                                   return one.iconInitials.compareTo(two.iconInitials);
                                               }
                                           });
                                           adapter.notifyDebtChannelChanged(list);
                                       }
                                   } else {
                                       Toast.makeText(activity, result.getMsg(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });

        etInputPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodUtil.closeSoftKeyboard(activity);
                    customKeyboardManager.showSoftKeyboard(etInputPrice);
                }
            }
        });
    }

    /**
     * 控件的点击事件
     */
    @OnClick({R.id.tv_fg_first_pay_loan_date, R.id.ll_account_flow_remark, R.id.tv_fg_first_pay_loan_times})
    public void ononItemClicked(View view){
        InputMethodUtil.closeSoftKeyboard(activity);
        switch (view.getId()) {
            case R.id.tv_fg_first_pay_loan_date:
                showFirstPayLoanDateDialog();
                break;
            case R.id.ll_account_flow_remark:
                dialog = new AccountFlowRemarkDialog();
                dialog.show(getFragmentManager(), "accountflowremark");
                break;
            case R.id.tv_fg_first_pay_loan_times:
                showLoanTimes();
                break;
        }

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtChannelComponent.builder()
                .appComponent(appComponent)
                .debtChannelModule(new DebtChannelModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void setPresenter(DebtChannelContract.Presenter presenter) {

    }

    @Override
    public void showDebtChannel(List<DebtChannel> list) {
//        adapter.notifyDebtChannelChanged(list);
    }

    @Override
    public void showDebtChannelHistory(List<DebtChannel> list) {

    }

    @Override
    public void showSearchResult(List<DebtChannel> list) {

    }

    @Override
    public void showNoSearchResult() {

    }

    @Override
    public void showSearchChannelSelected(DebtChannel channel) {

    }

    @Override
    public void scrollToPosition(int position) {

    }

    /**
     * 还款周期
     */
    private void showLoanTimes() {
        OptionsPickerView pickerView = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                int monthLimit = options1 + 1;
//                times.setText(monthLimit + "个月");
//                times.setTag(monthLimit);
            }
        }).setCancelText("取消")
                .setCancelColor(Color.parseColor("#808080"))
                .setSubmitText("确认")
                .setSubmitColor(Color.parseColor("#287BF7"))
                .setTitleText("选择还款周期和期数")
                .setTitleColor((Color.parseColor("#2E2E2E")))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .build();


        List<String> option1 = new ArrayList<>();
        for (int i = 0; i < mRepaymentCycle.length; i++) {
            option1.add(mRepaymentCycle[i]);
        }

        List<List<String>> option2 = new ArrayList<>();
        for (int i = 1; i <= 12; ++i) {
            List<String> list = new ArrayList<>();
            list.add("循环");
            for (int j = 1; j <= 36; j++) {
                list.add(j + "期");
            }
            if (i == 1) {
                for (int k = 48; k <= 360; k+=12) {
                    list.add(k + "期("+k/12+"年)");
                }
            }
            option2.add(list);
        }
        pickerView.setPicker(option1, option2);
        pickerView.setSelectOptions(0, 1);
        pickerView.show();
    }


    /**
     * 首次还款日弹框
     */
    private void showFirstPayLoanDateDialog() {
        Date date = new Date();
        mFirstPayLoanDate.setTag(date);

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime((Date) mFirstPayLoanDate.getTag());

        TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mFirstPayLoanDate.setTag(date);
                mFirstPayLoanDate.setText(dateFormat.format(date));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setCancelColor(Color.parseColor("#808080"))
                .setSubmitText("确认")
                .setSubmitColor(Color.parseColor("#287BF7"))
                .setTitleText("选择首次还款日")
                .setTitleColor(getResources().getColor(R.color.black_1))
                .setTitleBgColor(Color.WHITE)
                .setBgColor(Color.WHITE)
                .setLabel("年", "月", "日", null, null, null)
                .isCenterLabel(false)
                .setDate(calendar)
                .build();
        pickerView.show();
    }
}
