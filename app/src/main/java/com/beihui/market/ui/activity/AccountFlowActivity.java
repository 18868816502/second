package com.beihui.market.ui.activity;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.KeyBoardHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.AccountFlowAdapter;
import com.beihui.market.view.AutoAdjustSizeEditText;
import com.beihui.market.view.customekeyboard.CustomBaseKeyboard;
import com.beihui.market.view.customekeyboard.CustomKeyboardManager;
import com.gyf.barlibrary.ImmersionBar;

import java.math.BigDecimal;

import butterknife.BindView;

/**
 * Created by admin on 2018/6/13.
 * 账单流程页面 通用记账 网贷记账 信用卡记账
 */

public class AccountFlowActivity extends BaseComponentActivity {

    @BindView(R.id.ll_account_flow_root)
    LinearLayout mRoot;
    @BindView(R.id.ll_account_flow_bottom)
    LinearLayout mBottom;
    @BindView(R.id.ll_account_flow_remark)
    LinearLayout remark;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.rv_account_flow)
    RecyclerView recyclerView;
    @BindView(R.id.auto_et_num)
    AutoAdjustSizeEditText etInputPrice;

    //自定义键盘管理
    public CustomKeyboardManager customKeyboardManager;

    //适配器
    public AccountFlowAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_account_flow;
    }

    @Override
    public void configViews() {
        setupToolbarBackNavigation(toolbar,R.drawable.x_delete);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);

        mAdapter = new AccountFlowAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
    }


    public BigDecimal sum;
    public StringBuilder temp = new StringBuilder();

    @Override
    public void initDatas() {
        customKeyboardManager = new CustomKeyboardManager(this);

        /**
         * 数字键盘
         * 48 : 0   50 : 2   57 : 9
         * 43 : +   45 : -
         * -5 : 退格  61 : =  46 : .
         */

        CustomBaseKeyboard priceKeyboard = new CustomBaseKeyboard(this, R.xml.stock_price_num_keyboard) {
            @Override
            public boolean handleSpecialKey(EditText etCurrent, int primaryCode) {
                //绑定的输入框的字符序列
                String currentContent = etCurrent.getText().toString();
                //为空不拦截操作
                if (TextUtils.isEmpty(currentContent)) {
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


        customKeyboardManager.attachTo(etInputPrice, priceKeyboard);
        customKeyboardManager.setShowUnderView(mBottom);

        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                customKeyboardManager.hideSoftKeyboard(etInputPrice);
//                mBottom.setVisibility(View.GONE);
                showKeyBoard(etInputPrice);
            }
        });
    }


    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    /**
     * 显示软键盘
     */
    public void showKeyBoard(final View editText) {
        editText.requestFocus();
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(editText, 0);
    }
}
