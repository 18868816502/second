package com.beihui.market.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.AccountFlowIconBean;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.AccountFlowActivity;
import com.beihui.market.ui.adapter.AccountFlowLoanRvAdapter;
import com.beihui.market.ui.adapter.AccountFlowLoanSearchAdapter;
import com.beihui.market.ui.contract.DebtChannelContract;
import com.beihui.market.ui.dialog.AccountFlowRemarkDialog;
import com.beihui.market.ui.presenter.DebtChannelPresenter;
import com.beihui.market.ui.rvdecoration.AccountFlowLoanItemDeco;
import com.beihui.market.ui.rvdecoration.AccountFlowLoanStickyHeaderItemDeco;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.FormatNumberUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.Px2DpUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtils;
import com.beihui.market.view.AlphabetIndexBar;
import com.beihui.market.view.AutoAdjustSizeEditText;
import com.beihui.market.view.EditTextUtils;
import com.beihui.market.view.GlideCircleTransform;
import com.beihui.market.view.customekeyboard.CustomBaseKeyboard;
import com.beihui.market.view.customekeyboard.CustomKeyboardManager;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.beihui.market.view.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

/**
 * Created by admin on 2018/6/15.
 */

public class AccountFlowLoanFragment extends BaseComponentFragment implements DebtChannelContract.View {

    private final String[] mRepaymentCycle = {"每月", "每两月", "每三月", "每四月", "每五月", "每六月", "每七月", "每八月", "每九月", "每十月", "每十一月", "每年"};

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private final SimpleDateFormat saveDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @BindView(R.id.iv_fg_account_flow_loan_icon)
    ImageView ivCustomIcon;
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
    public EditText etLoan;
    @BindView(R.id.ll_account_flow_bottom)
    LinearLayout mBottom;
    @BindView(R.id.tv_fg_first_pay_loan_times)
    TextView mFirstPayNormalTime;


    @BindView(R.id.iv_fg_account_flow_normal_remark)
    ImageView remarkImg;
    @BindView(R.id.tv_fg_account_flow_normal_remark)
    TextView remarkContent;

    private AccountFlowLoanRvAdapter adapter;
    private AccountFlowLoanSearchAdapter adapterCustomeIcon;

    private FragmentActivity activity;

    //软键盘帮助类
    private KeyBoardHelper boardHelper;
    private AccountFlowRemarkDialog dialog;

    //自定义键盘管理
    public CustomKeyboardManager customKeyboardManager;

//    @Inject
//    DebtChannelPresenter presenter;

    public boolean isLockEtLoan = false;

    public boolean lockEtInput = false;

    /**
     * 该字段不为空，则为编辑账单模式
     */
    public DebtDetail debtNormalDetail;

    /**
     * 创建账单的字段
     */
    public Map<String, Object> map = new HashMap<>();

    private String[] remarks = null;


    private LinearLayoutManager manager;
    private List<LoanAccountIconBean> list = null;
    private List<AccountFlowIconBean> customReperty = new ArrayList<>();

    //自定义图标
    private List<LoanAccountIconBean> data = new ArrayList<>();

    private String[] alphabetCountList;

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

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYNETLOAN);

        //限制emoji输入
        EditTextUtils.addDisableEmojiInputFilter(etLoan);

        activity = getActivity();
        mBottom.setVisibility(View.VISIBLE);

        adapter = new AccountFlowLoanRvAdapter(R.layout.list_item_debt_channel);


        /**
         * 判断是否是编辑账单
         */
        if (debtNormalDetail != null) {
            //显示图标 账单名称 金额 首次还款日 还款周期 备注
            Glide.with(activity).load(debtNormalDetail.getLogo()).transform(new GlideCircleTransform(activity)).into(ivCustomIcon);
            isLockEtLoan = true;
            etLoan.setText(debtNormalDetail.getChannelName());
            etLoan.setSelection(debtNormalDetail.getChannelName().length());
            isLockEtLoan = false;
            //金额
            lockEtInput = true;
            etInputPrice.setText(FormatNumberUtils.FormatNumberForTabDouble(debtNormalDetail.getTermPayableAmount())+"");
            lockEtInput = false;

            if (debtNormalDetail.getTermPayableAmount() > 1000000D) {
                etInputPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            } else {
                etInputPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            }

            mFirstPayLoanDate.setText(debtNormalDetail.getFirstRepayDate());
            //还款周期
            if (1 == debtNormalDetail.getTermType() || 0 == debtNormalDetail.getTermType()) {
                //日 一次性还款
                mFirstPayNormalTime.setText("每月");
            }
            if (2 == debtNormalDetail.getTermType()) {
                //月
                mFirstPayNormalTime.setText((1 == debtNormalDetail.cycle? "每月" : "每"+debtNormalDetail.cycle+"月")+" "+(debtNormalDetail.getTerm() == -1 ? "循环": debtNormalDetail.getTerm()+"期"));
            }
            if (3 == debtNormalDetail.getTermType()) {
                //年
                mFirstPayNormalTime.setText(("每年 "+(debtNormalDetail.getTerm() == -1 ? "循环": debtNormalDetail.getTerm()+"期")));
            }
            // 备注
            if (!TextUtils.isEmpty(debtNormalDetail.getRemark())) {
                remarks = debtNormalDetail.getRemark().split(",");
                if (debtNormalDetail.getRemark().length() > 4) {
                    remarkContent.setText(debtNormalDetail.getRemark().substring(0, 4)+"...");
                } else {
                    remarkContent.setText(debtNormalDetail.getRemark());
                }
                remarkImg.setVisibility(View.GONE);
                map.put("remark", debtNormalDetail.getRemark());
            } else {
                remarkImg.setVisibility(View.VISIBLE);
                remarkContent.setText("备注");
            }

            //图标
            map.put("iconId", debtNormalDetail.iconId);
            //图标标识
            map.put("channelId", debtNormalDetail.channelId);
            map.put("tallyType", debtNormalDetail.tallyType);
            //账单名称
            map.put("channelName", debtNormalDetail.getChannelName());
            //默认
            Date parse = null;
            try {
                parse = dateFormat.parse(debtNormalDetail.getFirstRepayDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            map.put("firstRepaymentDate", saveDateFormat.format(parse));
            map.put("termType", debtNormalDetail.getTermType());
            map.put("cycle", debtNormalDetail.cycle);
            map.put("term", debtNormalDetail.getTerm());
            map.put("amount", debtNormalDetail.getTermPayableAmount() + "");
        } else {
            //默认
            map.put("firstRepaymentDate", saveDateFormat.format(new Date()));
            map.put("termType", "2");
            map.put("cycle", "1");
            map.put("term", "1");
            map.put("amount", "0");
        }

        /**
         * 点击显示的列表平台
         */
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LoanAccountIconBean accountFlowIconBean = null;
                if (alphabetIndexBar.getVisibility() == View.VISIBLE) {
                    if (list != null && list.size() > 0) {
                        accountFlowIconBean = list.get(position);
                    }
                } else {
                    if (marryList != null && marryList.size() > 0) {
                        accountFlowIconBean = marryList.get(position);
                    }
                }
                if (accountFlowIconBean == null) {
                    return;
                }

                //图标
                map.put("iconId", accountFlowIconBean.iconId);
                //图标标识
                map.put("channelId", accountFlowIconBean.tallyId);
                map.put("tallyType", Integer.valueOf(accountFlowIconBean.isPrivate) + 1 + "");
                //账单名称
                map.put("channelName", accountFlowIconBean.iconName);

                Glide.with(activity).load(accountFlowIconBean.logo).transform(new GlideCircleTransform(activity)).into(ivCustomIcon);
                isLockEtLoan = true;
                etLoan.setText(accountFlowIconBean.iconName);
                etLoan.setSelection(accountFlowIconBean.iconName.length());
                isLockEtLoan = false;


                if (!TextUtils.isEmpty(accountFlowIconBean.remark)) {
                    remarks = accountFlowIconBean.remark.split(",");
                }

//                InputMethodUtil.openSoftKeyboard(activity, etLoan);
//                etLoan.setFocusable(true);
//                etLoan.setFocusableInTouchMode(true);
//                etLoan.requestFocus();

                    etLoan.clearFocus();
                    etLoan.setFocusable(false);
                    etInputPrice.setFocusable(true);
                    etInputPrice.requestFocus();
                    customKeyboardManager.showSoftKeyboard(etInputPrice);
                    InputMethodUtil.closeSoftKeyboard(activity);
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
                for (int i = 0; i < list.size(); i++) {
                    if (alphabet.equals(list.get(i).iconInitials)) {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    }
                }
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
               int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

               if (recyclerViewSearch.getVisibility() == View.INVISIBLE) {
                   String iconInitials = list.get(firstVisibleItemPosition).iconInitials;
                   for (int i = 0; i < alphabetCountList.length; i++) {
                       if (iconInitials.equals(alphabetCountList[i])) {
                           alphabetIndexBar.selectedIndex = i;
                           alphabetIndexBar.invalidate();
                       }
                   }
               }


               if (Math.abs(dy) <= 2) {
                   return;
               }
              if (dy > 0) {
                  customKeyboardManager.hideSoftKeyboard(etInputPrice, 1);
                  InputMethodUtil.closeSoftKeyboard(activity);
              } else {
                  //向上滚动  显示自定义键盘
                  customKeyboardManager.showSoftKeyboard(etInputPrice);
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
                        if ("0.00".equals(etCurrent.getText().toString())) {
                            etCurrent.setText(primaryCode - 48 + "");
                            return true;
                        }
                        if (etCurrent.getText().toString().length()==1 && "0".equals(etCurrent.getText().toString())) {
                            etCurrent.setText(primaryCode - 48 + "");
                            return true;
                        }
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
                    if (currentContent.length() == 1) {
                        if (currentContent.equals("+") ||currentContent.equals("-")) {
                            return true;
                        } else {
                            sum = new BigDecimal(currentContent);
                        }
                    }else if ((!currentContent.substring(1).contains("+") && !currentContent.substring(1).contains("-"))) {
                        sum = new BigDecimal(currentContent);
                    } else {


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


                        return true;
                    }
                    getKeys().get(11).label = "=";
                    customKeyboardManager.mKeyboardView.invalidateAllKeys();
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
                        etCurrent.setText("0.00");
                    } else {
                        etCurrent.setText(substring);
                    }
                    return true;
                }

                //求和
                if (primaryCode == 61) {
                    if ("确定".equals(getKeys().get(11).label)) {
                        ((AccountFlowActivity)activity).createAccount();
                        return true;
                    }
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
                    //pv，uv统计
//                    DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYKEYBOARDCONFIRMBUTTON);

                    List<Key> keys = getKeys();
                    keys.get(11).label = "确定";
                    customKeyboardManager.mKeyboardView.invalidateAllKeys();
                    return true;
                }
                return false;
            }
        };

        customKeyboardManager.attachTo(etInputPrice, priceKeyboard);
        customKeyboardManager.setShowUnderView(mBottom);


        etLoan.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getUserVisibleHint()) {
                    InputMethodUtil.openSoftKeyboard(activity, etLoan);
                    etLoan.setFocusable(true);
                    etLoan.setFocusableInTouchMode(true);
                    etLoan.requestFocus();
                }
            }
        }, 500);

//        etInputPrice.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                boardHelper.showKeyBoard(etInputPrice);
//            }
//        }, 100);


        etLoan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isLockEtLoan) {
                    return;
                }

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
                        showCustomeReporty();
                    } else {
                        recyclerViewSearch.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (!TextUtils.isEmpty(etLoan.getText().toString())) {
                        showCustomeReporty();
                    } else {
                        recyclerViewSearch.setVisibility(View.INVISIBLE);
                        alphabetIndexBar.setVisibility(View.VISIBLE);
                        adapter.notifyDebtChannelChanged(list);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                    //账单名称
                    map.put("channelName", s.toString());
            }
        });
    }

    /**
     * 获取自定义库
     */
    private void queryCustomeReporty() {
        Api.getInstance().queryCustomIconList()
                .compose(RxUtil.<ResultEntity<List<AccountFlowIconBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AccountFlowIconBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<AccountFlowIconBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData().size() > 0) {
                                           /**
                                        * 判断是否是编辑账单
                                        */
                                           if (debtNormalDetail == null) {

                                               Glide.with(activity).load(result.getData().get(0).logo).transform(new GlideCircleTransform(activity)).into(ivCustomIcon);
                                               //图标
                                               map.put("iconId", result.getData().get(0).iconId);
                                               //图标标识
                                               map.put("channelId", result.getData().get(0).tallyId);
                                               map.put("tallyType", "2");

                                               if (!TextUtils.isEmpty(result.getData().get(0).remark)) {
                                                   remarks = result.getData().get(0).remark.split(",");
                                                   map.put("remark", result.getData().get(0).remark);
                                               }
                                           }

                                           if (customReperty.size() > 0) {
                                               customReperty.clear();
                                           }
                                           customReperty.addAll(result.getData());
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
    }

    private void showCustomeReporty() {
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
        adapterCustomeIcon.setData(customReperty);

        adapterCustomeIcon.setOnItemClickListener(new AccountFlowLoanSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AccountFlowIconBean bean) {
                //图标
                map.put("iconId", bean.iconId);
                //图标标识
                map.put("channelId", bean.tallyId);
                map.put("tallyType", "2");

                Glide.with(activity).load(bean.logo).transform(new GlideCircleTransform(activity)).into(ivCustomIcon);

                remarks = null;
                recyclerViewSearch.setVisibility(View.INVISIBLE);
                alphabetIndexBar.setVisibility(View.VISIBLE);
                adapter.notifyDebtChannelChanged(list);

                etLoan.clearFocus();
                etLoan.setFocusable(false);
                etInputPrice.setFocusable(true);
                etInputPrice.requestFocus();
                customKeyboardManager.showSoftKeyboard(etInputPrice);
                InputMethodUtil.closeSoftKeyboard(activity);
            }
        });
    }



    public List<LoanAccountIconBean> marryList = new ArrayList<>();

    @Override
    public void initDatas() {
        etLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLoan.setFocusable(true);
                etLoan.setFocusableInTouchMode(true);
                etLoan.requestFocus();
            }
        });

        Api.getInstance().queryLoanAccountIcon(UserHelper.getInstance(activity).getProfile().getId())
                .compose(RxUtil.<ResultEntity<List<LoanAccountIconBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<LoanAccountIconBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<LoanAccountIconBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (adapter != null) {
                                           list = result.getData();
                                           Collections.sort(list, new Comparator<LoanAccountIconBean>() {
                                               @Override
                                               public int compare(LoanAccountIconBean one, LoanAccountIconBean two) {
                                                   return one.iconInitials.compareTo(two.iconInitials);
                                               }
                                           });
                                           adapter.notifyDebtChannelChanged(list);

                                           LinkedHashSet<String> set = new LinkedHashSet<>();
                                           for (int i = 0; i < list.size(); i++) {
                                               set.add(list.get(i).iconInitials);
                                           }
                                           alphabetCountList = new String[set.size()];
                                           Iterator<String> iterator = set.iterator();
                                           int i = 0;
                                           while (iterator.hasNext()) {
                                               alphabetCountList[i] = iterator.next();
                                               i++;
                                           }
                                           alphabetIndexBar.setStringArray(alphabetCountList);
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

        /**
         * 查询自定义图标
         */
        queryCustomeReporty();

        etInputPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodUtil.closeSoftKeyboard(activity);
                }
            }
        });

        etInputPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (lockEtInput) {
                    return;
                }
                if (!TextUtils.isEmpty(s)) {
                    map.put("amount", s.toString());
                } else {
                    map.put("amount", "");
                }
//                if (!TextUtils.isEmpty(s) && !s.toString().contains("+") && !s.toString().contains("-")) {
//                    double amount = Double.parseDouble(s.toString());
//                    if (amount < 0D) {
//                        map.put("amount", null);
//                    } else if (amount == 0D) {
//                        map.put("amount", null);
//                    } else if (amount > 9999999999D) {
//                        map.put("amount", null);
//                    } else {
//                        map.put("amount", Double.parseDouble(s.toString())+"");
//                    }
//                }
            }
        });

        etInputPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    customKeyboardManager.showSoftKeyboard(etInputPrice);
                    InputMethodUtil.closeSoftKeyboard(activity);
                }
            }
        });

        etInputPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customKeyboardManager.showSoftKeyboard(etInputPrice);
                InputMethodUtil.closeSoftKeyboard(activity);
            }
        });

        etLoan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(etLoan, InputMethodManager.SHOW_IMPLICIT);
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

                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYFIRSTPAYMENTDATE);
                break;
            case R.id.ll_account_flow_remark:
                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYREMARK);

                if (dialog == null) {
                    dialog = new AccountFlowRemarkDialog();
                }
                if (remarks != null) {
                    dialog.setTagList(remarks);
                }
                if (debtNormalDetail != null && !TextUtils.isEmpty(debtNormalDetail.getRemark())) {
                    dialog.setContent(debtNormalDetail.getRemark());
                }
                dialog.show(getFragmentManager(), "accountflowremark");

                dialog.setOnTextChangeListener(new AccountFlowRemarkDialog.OnTextChangeListener() {
                    @Override
                    public void textChange(String text) {
                        if (TextUtils.isEmpty(text)) {
                            remarkImg.setVisibility(View.VISIBLE);
                            remarkContent.setText("备注");
                            if (map.containsKey("remark")) {
                                map.remove("remark");
                            }
                            return;
                        }
                        map.put("remark", text);
                        if (text.length() > 4) {
                            remarkContent.setText(text.substring(0, 4)+"...");
                        } else {
                            remarkContent.setText(text);
                        }
                        remarkImg.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.tv_fg_first_pay_loan_times:
                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYPAYMENTCYCLE);
                showLoanTimes();
                break;
        }

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
//        DaggerDebtChannelComponent.builder()
//                .appComponent(appComponent)
//                .debtChannelModule(new DebtChannelModule(this))
//                .build()
//                .inject(this);
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
        etLoan.clearFocus();
        etLoan.setFocusable(false);
        etInputPrice.setFocusable(true);
        etInputPrice.requestFocus();

        final List<String> option1 = new ArrayList<>();
        for (int i = 0; i < mRepaymentCycle.length; i++) {
            option1.add(mRepaymentCycle[i]);
        }

        final List<List<String>> option2 = new ArrayList<>();
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

        OptionsPickerView pickerView = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String monthLimit = option1.get(options1);
                String termLimit = " " + option2.get(options1).get(options2);
                mFirstPayNormalTime.setText(monthLimit + termLimit);
                mFirstPayNormalTime.setTag(monthLimit + termLimit);

                if (options1 < 11) {
                    map.put("termType", "2");
                    map.put("cycle", options1+1+"");
                } else {
                    map.put("termType", "3");
                    map.put("cycle", "1");
                }
                if (options2 == 0) {
                    map.put("term", "-1");
                } else if (options2 <= 36){
                    map.put("term", options2+"");
                } else {
                    map.put("term", ((options2 - 36)*12+36)+"");
                }
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

        pickerView.setPicker(option1, option2);
        /**
         * 判断是否是编辑账单
         */

        map.put("termType", "2");
        map.put("cycle", "1");
        map.put("term", "1");
        if (debtNormalDetail != null) {
            //还款周期
            if (1 == debtNormalDetail.getTermType() || 0 == debtNormalDetail.getTermType()) {
                //日 一次性还款
                pickerView.setSelectOptions(0, 1);
            }
            if (2 == debtNormalDetail.getTermType()) {
                //月
                int term = debtNormalDetail.getTerm();
                if (term == -1) {
                    pickerView.setSelectOptions(debtNormalDetail.cycle-1, 0);
                } else if (term <= 36) {
                    pickerView.setSelectOptions(debtNormalDetail.cycle-1, term);
                } else {
                    pickerView.setSelectOptions(debtNormalDetail.cycle-1, 36+(term-36)/12);
                }
            }
            if (3 == debtNormalDetail.getTermType()) {
                //年
                pickerView.setSelectOptions(11, debtNormalDetail.getTerm() == -1 ? 0 : debtNormalDetail.getTerm());
            }
        } else {
            pickerView.setSelectOptions(0, 1);
        }
        pickerView.show();
    }


    /**
     * 首次还款日弹框
     */
    private void showFirstPayLoanDateDialog() {
        etLoan.clearFocus();
        etLoan.setFocusable(false);
        etInputPrice.setFocusable(true);
        etInputPrice.requestFocus();

        /**
         * 判断是否是编辑账单
         */
        if (debtNormalDetail != null) {
            try {
                mFirstPayLoanDate.setTag(dateFormat.parse(debtNormalDetail.getFirstRepayDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Date date = new Date();
            mFirstPayLoanDate.setTag(date);
        }


        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime((Date) mFirstPayLoanDate.getTag());

        TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mFirstPayLoanDate.setTag(date);
                mFirstPayLoanDate.setText(dateFormat.format(date));

                //首次还款日
                map.put("firstRepaymentDate", saveDateFormat.format(date));

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
