package com.beihui.market.ui.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.AccountFlowIconBean;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.FastDebtDetail;
import com.beihui.market.event.AccountFlowTypeActivityEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.AccountFlowActivity;
import com.beihui.market.ui.activity.AccountFlowTypeActivity;
import com.beihui.market.ui.activity.LoanDebtDetailActivity;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.adapter.AccountFlowAdapter;
import com.beihui.market.ui.dialog.AccountFlowRemarkDialog;
import com.beihui.market.ui.dialog.NicknameDialog;
import com.beihui.market.ui.dialog.RemarkDialog;
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.ui.dialog.XTabAccountDialog;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.FormatNumberUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.Px2DpUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.ToastUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBaseCallback;
import zhy.com.highlight.shape.RectLightShape;
import zhy.com.highlight.view.HightLightView;


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
    @BindView(R.id.iv_fg_account_flow_normal_remark)
    ImageView remarkImg;
    @BindView(R.id.tv_fg_account_flow_normal_remark)
    TextView remarkContent;

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

    //高亮
    private HighLight infoHighLight;

    float startY ;
    float moveY;

    private List<AccountFlowIconBean> list = new ArrayList<>();

    /**
     * 创建账单的字段
     */
    public Map<String, Object> map = new HashMap<>();

    //自定义键盘管理
    public CustomKeyboardManager customKeyboardManager;

    public boolean lockEtInput = false;

    //适配器
    public AccountFlowAdapter mAdapter;
    private FragmentActivity activity;

    //软键盘帮助类
    private KeyBoardHelper boardHelper;
    private AccountFlowRemarkDialog dialog;
    private String[] remarks = null;

    /**
     * 该字段不为空，则为编辑账单模式
     */
    public FastDebtDetail debtNormalDetail;

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
        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYCOMMON);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 引导图
     */
    public void showGuide() {
        if ("showGuideAccountFlow".equals(SPUtils.getValue(activity, "showGuideAccountFlow"))) {
            return;
        }
        infoHighLight = new HighLight(activity)
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        infoHighLight.autoRemove(false)
                                .intercept(true)
                                .enableNext()
                                .addHighLight(R.id.ll_ac_account_flow_tab_root, R.layout.layout_highlight_guide_two, new OnBaseCallback() {
                                    @Override
                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                                        marginInfo.topMargin = rectF.height() + Px2DpUtils.dp2px(activity, 40);
                                        marginInfo.leftMargin = Px2DpUtils.dp2px(activity, 16);
                                    }
                                }, new RectLightShape(0, 0, 0, 100, 100))
                                .addHighLight(R.id.rv_account_flow, R.layout.layout_highlight_guide_three, new OnBaseCallback() {
                                    @Override
                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {

                                        marginInfo.bottomMargin = bottomMargin - rectF.height()*2/3 - Px2DpUtils.dp2px(activity, 6);
                                        marginInfo.leftMargin = Px2DpUtils.dp2px(activity, 16);
                                    }
                                }, new RectLightShape())
                                .addHighLight(R.id.tv_amount_high_light, R.layout.layout_highlight_guide_four, new OnBaseCallback() {
                                    @Override
                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {

                                        marginInfo.bottomMargin = bottomMargin - rectF.height()*2 - Px2DpUtils.dp2px(activity, 14);
                                        marginInfo.rightMargin = rectF.width() - rectF.width()/2 ;
                                    }
                                }, new RectLightShape(0, 0, 0, 100, 100))
                                .addHighLight(R.id.tv_fg_first_pay_loan_date, R.layout.layout_highlight_guide_five, new OnBaseCallback() {
                                    @Override
                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {

                                        marginInfo.bottomMargin = bottomMargin + rectF.height();
                                        marginInfo.leftMargin = rectF.width();
                                    }
                                }, new RectLightShape(0, 0, 0, 100, 100))
                                .addHighLight(R.id.tv_fg_first_pay_loan_times, R.layout.layout_highlight_guide_six, new OnBaseCallback() {
                                    @Override
                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {

                                        marginInfo.bottomMargin = bottomMargin + rectF.height() + Px2DpUtils.dp2px(activity, 4);
                                        marginInfo.rightMargin = Px2DpUtils.dp2px(activity, 12);
                                    }
                                }, new RectLightShape(0, 0, 0, 100, 100))
                                .setOnNextCallback(new HighLightInterface.OnNextCallback() {
                            @Override
                            public void onNext(HightLightView hightLightView, View targetView, View tipView) {
                                // targetView 目标按钮 tipView添加的提示布局 可以直接找到'我知道了'按钮添加监听事件等处理
                                if (targetView.getId() == R.id.ll_ac_account_flow_tab_root) {
                                    infoHighLight.getHightLightView().findViewById(R.id.iv_bill_guide_two).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            infoHighLight.next();
                                        }
                                    });
                                }
                                if (targetView.getId() == R.id.rv_account_flow) {

                                    infoHighLight.getHightLightView().findViewById(R.id.iv_bill_guide_three).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            infoHighLight.next();
                                        }
                                    });
                                }
                                if (targetView.getId() == R.id.tv_amount_high_light) {

                                    infoHighLight.getHightLightView().findViewById(R.id.iv_bill_guide_four).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            infoHighLight.next();
                                        }
                                    });
                                }

                                if (targetView.getId() == R.id.tv_fg_first_pay_loan_date) {

                                    infoHighLight.getHightLightView().findViewById(R.id.iv_bill_guide_five).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            infoHighLight.next();
                                        }
                                    });
                                }
                                if (targetView.getId() == R.id.tv_fg_first_pay_loan_times) {

                                    infoHighLight.getHightLightView().findViewById(R.id.iv_bill_guide_six).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            infoHighLight.remove();
                                            SPUtils.setValue(activity, "showGuideAccountFlow");
                                        }
                                    });
                                }
                            }
                        }).show();
                    }
                });
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
                        if (temp.length()==1 && "0".equals(temp.toString())) {
                            temp.delete(0, 1).append(primaryCode - 48 + "");
                            return false;
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
                if ((primaryCode == 43 || primaryCode == 45)) {
                    if (currentContent.length() == 1) {
                        if (currentContent.equals("+") ||currentContent.equals("-")) {
                            return true;
                        } else {
                            sum = new BigDecimal(currentContent);
                        }
                    }else if ((!currentContent.substring(1).contains("+") && !currentContent.substring(1).contains("-"))) {
                        sum = new BigDecimal(currentContent);
                    } else {
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
                        etCurrent.setText("0");
                    } else {
                        etCurrent.setText(substring);
                    }
                    return true;
                }

                //求和
                if (primaryCode == 61) {
                    if ("确定".equals( getKeys().get(11).label)) {
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
//                            DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYKEYBOARDCONFIRMBUTTON);

                    List<Key> keys = getKeys();
                    keys.get(11).label = "确定";
                    customKeyboardManager.mKeyboardView.invalidateAllKeys();
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
//                if (Math.abs(dy) <= 2) {
//                    return;
//                }
//                if (dy > 0) {
//                    customKeyboardManager.hideSoftKeyboard(etInputPrice, 0);
//                } else {
//                    //向上滚动  显示自定义键盘
//                    customKeyboardManager.showSoftKeyboard(etInputPrice);
//                }
            }
        });



        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveY = event.getY();
                        Log.e("android_normal_account", "startY--> " + startY);
                        Log.e("android_normal_account", "moveY--> " + moveY);
                        if (moveY - startY > 1) {
                            customKeyboardManager.showSoftKeyboard(etInputPrice);
                        }
                        if (moveY - startY < -1) {
                            customKeyboardManager.hideSoftKeyboard(etInputPrice, 0);
                        }
                    case MotionEvent.ACTION_UP:
                        return false;
                }
                return true;
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
                //图标
                map.put("iconId", bean.iconId);
                //图标标识
                map.put("tallyId", bean.tallyId);
                int isPrivate = bean.isPrivate == null ? 1 : Integer.valueOf(bean.isPrivate);
                map.put("tallyType", isPrivate+1+"");
                //账单名称
                map.put("projectName", bean.iconName);

                if (!TextUtils.isEmpty(bean.remark)) {
                    remarks = bean.remark.split(",");
                }
            }
        });


        /**
         * 自定义键盘关联
         */
        customKeyboardManager.attachTo(etInputPrice, priceKeyboard);
        customKeyboardManager.setShowUnderView(mBottom);
        etInputPrice.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getUserVisibleHint()) {
                    customKeyboardManager.showSoftKeyboard(etInputPrice);
                    showGuide();
                }
            }
        }, 100);


        /**
         * 判断是否是编辑账单
         */
        if (debtNormalDetail != null) {
            //显示图标 账单名称 金额 首次还款日 还款周期 备注
            Glide.with(activity).load(debtNormalDetail.logo).into(mTypeIcon);
            mTypeName.setText(debtNormalDetail.getProjectName());
            //金额
            lockEtInput = true;
            etInputPrice.setText(FormatNumberUtils.FormatNumberForTabDouble(debtNormalDetail.getTermPayableAmount())+"");
            lockEtInput = false;

            mFirstPayNormalDate.setText(debtNormalDetail.getFirstRepayDate());
            //还款周期
            if (1 == debtNormalDetail.cycleType || 0 == debtNormalDetail.cycleType) {
                //日 一次性还款
                mFirstPayNormalTime.setText("每月");
            }
            if (2 == debtNormalDetail.cycleType) {
                //月
                mFirstPayNormalTime.setText((1 == debtNormalDetail.cycle? "每月" : "每"+debtNormalDetail.cycle+"月")+" "+(debtNormalDetail.getTerm() == -1 ? "循环": debtNormalDetail.getTerm()+"期"));
            }
            if (3 == debtNormalDetail.cycleType) {
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
            } else {
                remarkImg.setVisibility(View.VISIBLE);
                remarkContent.setText("备注");
            }
        }

        /**
         * 请求通用类型列表
         */
        Api.getInstance().queryIconList(UserHelper.getInstance(activity).getProfile().getId(), "LCommon").compose(RxUtil.<ResultEntity<List<AccountFlowIconBean>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<AccountFlowIconBean>>>() {
                               @Override
                               public void accept(ResultEntity<List<AccountFlowIconBean>> result)  {
                                   if (result.isSuccess()) {
                                       if (list.size() > 0) {
                                           list.clear();
                                       }
                                       list.addAll(result.getData());
                                       if (mAdapter != null) {
                                           mAdapter.notifyDebtChannelChanged(list);
                                       }

                                       /**
                                        * 判断是否是编辑账单
                                        */
                                       if (debtNormalDetail != null) {
                                           //图标
                                           map.put("iconId", debtNormalDetail.iconId);
                                           //图标标识
                                           map.put("tallyId", debtNormalDetail.tallyId);
                                           map.put("tallyType", debtNormalDetail.tallyType);
                                           //账单名称
                                           map.put("projectName", debtNormalDetail.getProjectName());
                                           //默认
                                           Date parse = null;
                                           try {
                                               parse = dateFormat.parse(debtNormalDetail.getFirstRepayDate());
                                           } catch (ParseException e) {
                                               e.printStackTrace();
                                           }
                                           map.put("firstRepaymentDate", saveDateFormat.format(parse));
                                           map.put("cycleType", debtNormalDetail.cycleType);
                                           map.put("cycle", debtNormalDetail.cycle);
                                           map.put("term", debtNormalDetail.getTerm());
                                           map.put("amount", debtNormalDetail.getTermPayableAmount() + "");
                                           if (!TextUtils.isEmpty(debtNormalDetail.getRemark())) {
                                               map.put("remark", debtNormalDetail.getRemark());
                                           }
                                       } else {
                                           if (list.size() > 0) {
                                               //图标
                                               map.put("iconId", list.get(0).iconId);
                                               //图标标识
                                               map.put("tallyId", list.get(0).tallyId);
                                               map.put("tallyType", Integer.valueOf(list.get(0).isPrivate) + 1 + "");
                                               //账单名称
                                               map.put("projectName", list.get(0).iconName);
                                               //默认
                                               map.put("firstRepaymentDate", saveDateFormat.format(new Date()));
                                               map.put("cycleType", "2");
                                               map.put("cycle", "1");
                                               map.put("term", "1");
                                               map.put("amount", "0");
                                               Glide.with(activity).load(list.get(0).logo).into(mTypeIcon);
                                               if (!TextUtils.isEmpty(list.get(0).iconName)) {
                                                   mTypeName.setText(list.get(0).iconName);
                                               }
                                               if (!TextUtils.isEmpty(list.get(0).remark)) {
                                                   remarks = list.get(0).remark.split(",");
                                               }
                                           }
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

                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.TALLYFIRSTPAYMENTDATE);

                showFirstPayLoanDateDialog();
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

                if (options1 < 11) {
                    map.put("cycleType", "2");
                    map.put("cycle", options1+1+"");
                } else {
                    map.put("cycleType", "3");
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
        pickerView.setSelectOptions(0, 1);
        pickerView.show();
    }


    /**
     * 首次还款日弹框
     */
    private void showFirstPayLoanDateDialog() {

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date date = new Date();
        mFirstPayNormalDate.setTag(date);
        calendar.setTime((Date) mFirstPayNormalDate.getTag());

        TimePickerView pickerView = new TimePickerView.Builder(getContext(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mFirstPayNormalDate.setTag(date);
                mFirstPayNormalDate.setText(dateFormat.format(date));

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