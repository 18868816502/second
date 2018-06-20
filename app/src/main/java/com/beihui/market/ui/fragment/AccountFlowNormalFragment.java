package com.beihui.market.ui.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.AccountFlowIconBean;
import com.beihui.market.event.AccountFlowTypeActivityEvent;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.AccountFlowTypeActivity;
import com.beihui.market.ui.activity.LoanDebtDetailActivity;
import com.beihui.market.ui.adapter.AccountFlowAdapter;
import com.beihui.market.ui.dialog.AccountFlowRemarkDialog;
import com.beihui.market.ui.dialog.NicknameDialog;
import com.beihui.market.ui.dialog.RemarkDialog;
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.ui.dialog.XTabAccountDialog;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.RxUtil;
import com.beihui.market.view.AutoAdjustSizeEditText;
import com.beihui.market.view.customekeyboard.CustomBaseKeyboard;
import com.beihui.market.view.customekeyboard.CustomKeyboardManager;
import com.beihui.market.view.flowlayout.FlowLayout;
import com.beihui.market.view.flowlayout.TagAdapter;
import com.beihui.market.view.flowlayout.TagFlowLayout;
import com.beihui.market.view.pickerview.OptionsPickerView;
import com.beihui.market.view.pickerview.TimePickerView;
import com.beihui.market.view.pulltoswipe.PulledRecyclerView;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by admin on 2018/6/14.
 */

public class AccountFlowNormalFragment extends BaseComponentFragment {

    private final String[] mRepaymentCycle = {"每月", "每两月", "每三月", "每四月", "每五月", "每六月", "每七月", "每八月", "每九月", "每十月", "每十一月", "每年"};

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private final SimpleDateFormat saveDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @BindView(R.id.ll_account_flow_bottom)
    LinearLayout mBottom;
    @BindView(R.id.ll_account_flow_remark)
    LinearLayout remark;

    @BindView(R.id.rv_account_flow)
    RecyclerView recyclerView;
    @BindView(R.id.auto_et_num)
    public AutoAdjustSizeEditText etInputPrice;


    @BindView(R.id.tv_fg_first_pay_loan_date)
    TextView mFirstPayNormalDate;
    @BindView(R.id.tv_fg_first_pay_loan_times)
    TextView mFirstPayNormalTime;


    @BindView(R.id.tv_account_flow_normal_type_name)
    TextView mTypeName;
    @BindView(R.id.iv_fg_account_flow_normal_icon)
    ImageView mTypeIcon;

    private List<AccountFlowIconBean> list = new ArrayList<>();


    //自定义键盘管理
    public CustomKeyboardManager customKeyboardManager;

    //适配器
    public AccountFlowAdapter mAdapter;
    private FragmentActivity activity;

    //软键盘帮助类
    private KeyBoardHelper boardHelper;
    private AccountFlowRemarkDialog dialog;
    private String[] remarks = null;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(AccountFlowTypeActivityEvent event){
        if (mAdapter != null && event.bean != null) {
            list.add(event.bean);
            mAdapter.notifyDebtChannelChanged(list);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.x_fragment_account_flow_normal;
    }

    @Override
    public void configViews() {
        activity = getActivity();
        mBottom.setVisibility(View.VISIBLE);

        mAdapter = new AccountFlowAdapter(activity, 1);
        GridLayoutManager manager = new GridLayoutManager(activity, 5);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public BigDecimal sum;
    public StringBuilder temp = new StringBuilder();

    private KeyBoardHelper.OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener = new KeyBoardHelper.OnKeyBoardStatusChangeListener() {

        /**
         * 软键盘弹出时候
         */
        @Override
        public void OnKeyBoardPop(int keyBoardHeight) {
        }

        /**
         * 软件盘隐藏的时候
         */
        @Override
        public void OnKeyBoardClose(int oldKeyBoardHeight) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    };

    @Override
    public void initDatas() {
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
                    etCurrent.setText("0.00");
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
                        temp.append(primaryCode - 48);
                    }
                    return false;
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
                    }
                    if (temp.length() > 0) {
                        if (!temp.toString().contains(".")) {
                            temp.append(".");
                            etCurrent.setText(currentContent+".");
                        }
                        return true;
                    }
                    if (sum.toString().length() > 0 && temp.toString().contains(".")) {
                        return true;
                    }
                    return false;
                }

                //退格键
                if (primaryCode == -5) {
                    if (temp.toString().length() > 0 && (currentContent.substring(1).contains("+") || currentContent.substring(1).contains("-"))) {
                        temp.delete(temp.length() - 1, temp.length());
                    }
                    etCurrent.setText(currentContent.substring(0, currentContent.length()-1));
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
                    customKeyboardManager.hideSoftKeyboard(etInputPrice, 0);
                }
            }
        });

        /**
         * 适配器item的点击事件
         */
        mAdapter.setOnItemClickListener(new AccountFlowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AccountFlowIconBean bean) {
                Glide.with(activity).load(bean.logo).into(mTypeIcon);
                if (!TextUtils.isEmpty(bean.iconName)) {
                    mTypeName.setText(bean.iconName);
                } else {
                    mTypeName.setText("");
                }
            }
        });


        /**
         * 自定义键盘关联
         */
        customKeyboardManager.attachTo(etInputPrice, priceKeyboard);
        customKeyboardManager.setShowUnderView(mBottom);

        /**
         * 请求通用类型列表
         */
        Api.getInstance().queryIconList(UserHelper.getInstance(activity).getProfile().getId(), "LCommon").compose(RxUtil.<ResultEntity<List<AccountFlowIconBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AccountFlowIconBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<AccountFlowIconBean>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (mAdapter != null) {
                                           if (list.size() > 0) {
                                               list.clear();
                                           }
                                           list.addAll(result.getData());
                                           if (list.size() > 0) {
                                               Glide.with(activity).load(list.get(0).logo).into(mTypeIcon);
                                               if (!TextUtils.isEmpty(list.get(0).iconName)) {
                                                   mTypeName.setText(list.get(0).iconName);
                                               }

                                               remarks = list.get(0).remark.split(",");
                                           }
                                           mAdapter.notifyDebtChannelChanged(list);
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


    /**
     * 控件的点击事件
     */
    @OnClick({R.id.tv_fg_first_pay_loan_date, R.id.ll_account_flow_remark, R.id.tv_fg_first_pay_loan_times, R.id.auto_et_num})
    public void ononItemClicked(View view){
        InputMethodUtil.closeSoftKeyboard(activity);
        switch (view.getId()) {
            case R.id.tv_fg_first_pay_loan_date:
                showFirstPayLoanDateDialog();
                break;
            case R.id.ll_account_flow_remark:
                dialog = new AccountFlowRemarkDialog();
                if (remarks != null) {
                    dialog.setTagList(remarks);
                }
                dialog.show(getFragmentManager(), "accountflowremark");
                break;
            case R.id.tv_fg_first_pay_loan_times:
                showLoanTimes();
                break;
            case R.id.auto_et_num:
                customKeyboardManager.showSoftKeyboard(etInputPrice);
                break;
        }
    }


    /**
     * 还款周期
     */
    private void showLoanTimes() {

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
        pickerView.setSelectOptions(0, 1);
        pickerView.show();
    }


    /**
     * 首次还款日弹框
     */
    private void showFirstPayLoanDateDialog() {
        Date date = new Date();
        mFirstPayNormalDate.setTag(date);

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime((Date) mFirstPayNormalDate.getTag());

        TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mFirstPayNormalDate.setTag(date);
                mFirstPayNormalDate.setText(dateFormat.format(date));
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}