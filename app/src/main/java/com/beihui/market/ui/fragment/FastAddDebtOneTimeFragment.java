package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.DebtChannel;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtNewComponent;
import com.beihui.market.injection.module.DebtNewModule;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.contract.DebtNewContract;
import com.beihui.market.ui.listeners.EtAmountWatcher;
import com.beihui.market.ui.listeners.EtTextLengthWatcher;
import com.beihui.market.ui.presenter.DebtNewPresenter;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.ClearEditText;
import com.beihui.market.view.EditTextUtils;
import com.beihui.market.view.pickerview.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author xhb
 * @version 3.0.1
 * 快速记账 一次性还款
 */
public class FastAddDebtOneTimeFragment extends BaseComponentFragment{

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private final SimpleDateFormat saveDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    //账单名称
    @BindView(R.id.ll_one_time_account_name_root)
    LinearLayout llAccountNameRoot;
    //账单名称
    @BindView(R.id.cet_one_time_account_name)
    ClearEditText etAccountName;
    //到期还款日
    @BindView(R.id.tv_one_time_dead_time)
    TextView tvDebtPayDay;
    //应还金额
    @BindView(R.id.cet_one_time_money)
    ClearEditText etDebtAmount;
    //备注
    @BindView(R.id.cet_one_time_mark_content)
    ClearEditText etRemark;

    //依附的Activity
    private FragmentActivity activity;


    /**
     * 返回布局
     */
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_debt_new_one_time;
    }

    /**
     * Dagger2注入方法
     */
    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {
        activity = getActivity();
        //隐藏账单名称
        llAccountNameRoot.setVisibility(View.GONE);

    }


    @Override
    public void configViews() {
        Date date = new Date();
        //设置到期还款日 为今天
        tvDebtPayDay.setText(dateFormat.format(date));
        tvDebtPayDay.setTag(date);

        //账单名称
        etAccountName.addTextChangedListener(new EtTextLengthWatcher(etAccountName, 16 * 2));
        //备注监听器
        etRemark.addTextChangedListener(new EtTextLengthWatcher(etRemark, 20 * 2));
        //应还金额监听器
        etDebtAmount.addTextChangedListener(new EtAmountWatcher(etDebtAmount));

        //限制小数位
        EditTextUtils.addDecimalDigitsInputFilter(etDebtAmount);
        //限制emoji输入
        EditTextUtils.addDisableEmojiInputFilter(etRemark);
        EditTextUtils.addDisableEmojiInputFilter(etAccountName);
    }



    @OnClick({R.id.tv_one_time_dead_time, R.id.tv_one_time_confirm_button})
    public void onClick(View view){
        InputMethodUtil.closeSoftKeyboard(getActivity());
        switch (view.getId()) {
            //到期还款日
            case R.id.tv_one_time_dead_time:
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                calendar.setTime((Date) tvDebtPayDay.getTag());

                TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvDebtPayDay.setTag(date);
                        tvDebtPayDay.setText(dateFormat.format(date));
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})
                        .setCancelText("取消")
                        .setCancelColor(Color.parseColor("#ff5200"))
                        .setSubmitText("确认")
                        .setSubmitColor(Color.parseColor("#ff5200"))
                        .setTitleText("到期还款日")
                        .setTitleColor(getResources().getColor(R.color.black_1))
                        .setTitleBgColor(Color.WHITE)
                        .setBgColor(Color.WHITE)
                        .setLabel("年", "月", "日", null, null, null)
                        .isCenterLabel(false)
                        .setDate(calendar)
                        .build();
                pickerView.show();
                break;
            /**
             * 确认按钮
             * 如果是保存编辑账单 则先有一个删除账单的过程
             */
            case R.id.tv_one_time_confirm_button:
                if (!FastClickUtils.isFastClick()) {
                    saveDebt();
                }
                break;
        }
    }

    /**
     * 保存账单
     * @userId 用户ID
     * @projectName 快速记账 把备注当成账单名称 （默认为“快速记账”）
     * @repayType 还款方式 1-一次性还本付息, 2-分期
     * @term     期限
     * @payableAmount 到期还款 (repayType =1 必须)
     * @everyTermAmount 每期应还金额(repayType =2 必须)
     * @remark  不传
     * @deuRepaymentDate 	最终还款时间 (repayType =1 必须)
     * @firstRepaymentDate  首期还款时间 (repayType =2 必须)
     */
    private void saveDebt() {
        /**
         * 需要判断到期应还金额
         */
        if (TextUtils.isEmpty(etDebtAmount.getText().toString())) {
            ToastUtils.showShort(getContext(), "请输入到期还款金额", null);
            return;
        } else {
            if (Double.parseDouble(etDebtAmount.getText().toString()) <= 0D) {
                ToastUtils.showShort(getContext(), "账单金额必须大于0", null);
                return;
            }
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", UserHelper.getInstance(activity).getProfile().getId());
        params.put("projectName", TextUtils.isEmpty(etRemark.getText().toString()) ? "快捷记账" : etRemark.getText().toString());
        params.put("repayType","1");
        params.put("term","1");
        params.put("payableAmount",etDebtAmount.getText().toString());
        params.put("deuRepaymentDate",saveDateFormat.format((Date) tvDebtPayDay.getTag()));

        Api.getInstance().saveFastDebt(params)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {

                    @Override
                    public void accept(ResultEntity resultEntity) throws Exception {
                        if (resultEntity.isSuccess()) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("account", true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            if (activity != null) {
                                activity.finish();
                            }
                        } else {
                            ToastUtils.showShort(getContext(), resultEntity.getMsg(), null);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    /**
     * 注销presenter
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 获取Fragment
     */
    public static FastAddDebtOneTimeFragment newInstance() {
        FastAddDebtOneTimeFragment fragment = new FastAddDebtOneTimeFragment();
        return fragment;
    }


}
