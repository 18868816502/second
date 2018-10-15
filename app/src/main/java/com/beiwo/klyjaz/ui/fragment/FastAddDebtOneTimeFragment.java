package com.beiwo.klyjaz.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.FastDebtDetail;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.ui.activity.MainActivity;
import com.beiwo.klyjaz.ui.listeners.EtAmountWatcher;
import com.beiwo.klyjaz.ui.listeners.EtTextLengthWatcher;
import com.beiwo.klyjaz.util.FastClickUtils;
import com.beiwo.klyjaz.util.InputMethodUtil;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.WeakRefToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;
import com.beiwo.klyjaz.view.EditTextUtils;
import com.bigkoo.pickerview.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.beiwo.klyjaz.util.CommonUtils.keep2digitsWithoutZero;

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

    public Handler mHandler = new Handler();

    /**
     * 该字段不为空，则为编辑账单模式
     */
    private FastDebtDetail fastDebtDetail;

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
        if (fastDebtDetail == null) {
            Date date = new Date();
            //设置到期还款日 为今天
            tvDebtPayDay.setText(dateFormat.format(date));
            tvDebtPayDay.setTag(date);
        } else {
            //到期应还金额
            etDebtAmount.setText(keep2digitsWithoutZero(fastDebtDetail.getPayableAmount()).replace(",", ""));//去货币化 也就是去掉逗号
            //到期还款日
            tvDebtPayDay.setText(fastDebtDetail.termRepayDate);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            try {
                tvDebtPayDay.setTag(sdf.parse(fastDebtDetail.termRepayDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //备注
            etRemark.setText((TextUtils.isEmpty(fastDebtDetail.getProjectName()) || "快捷记账".equals(fastDebtDetail.getProjectName()))  ? "" : fastDebtDetail.getProjectName());
        }

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

        /**
         * 开启软键盘
         */
        etDebtAmount.requestFocus();
        if (!TextUtils.isEmpty(etDebtAmount.getText().toString())) {
            etDebtAmount.setSelection(etDebtAmount.getText().toString().length());
        }
        mHandler.postDelayed(new Runnable(){
            public void run() {
                InputMethodUtil.openSoftKeyboard(activity, etDebtAmount);
            }
        }, 500);
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
                    if (fastDebtDetail != null) {
                        //先删除订单
                        deleteDebt();
                    } else {
                        saveDebt();
                    }
                }
                break;
        }
    }

    /**
     * 删除订单
     */
    private void deleteDebt() {
        Api.getInstance().deleteFastDebt(UserHelper.getInstance(activity).getProfile().getId(), fastDebtDetail.getId())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       saveDebt();
                                   } else {
                                       showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {


                            }
                        });

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
            WeakRefToastUtil.showShort(getContext(), "请输入到期还款金额", null);
            return;
        } else {
            if (Double.parseDouble(etDebtAmount.getText().toString()) <= 0D) {
                WeakRefToastUtil.showShort(getContext(), "账单金额必须大于0", null);
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
                            WeakRefToastUtil.showShort(getContext(), resultEntity.getMsg(), null);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }


    /**
     * 获取Fragment
     */
    public static FastAddDebtOneTimeFragment newInstance(FastDebtDetail fastDebtDetail) {
        FastAddDebtOneTimeFragment fragment = new FastAddDebtOneTimeFragment();
        fragment.fastDebtDetail = fastDebtDetail;
        return fragment;
    }


}
