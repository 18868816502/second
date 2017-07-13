package com.beihui.market.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.App;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.dialog.JuhuaDialog;

import java.util.regex.Pattern;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    protected View parentView;

    protected JuhuaDialog juhuaDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        parentView = inflater.inflate(getLayoutResId(), container, false);

        /** 预防 点击击穿，实现下面的fragment的点击事件*/
        parentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        return parentView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupActivityComponent(App.getInstance().getAppComponent());
        configViews();
        initDatas();
    }


    @LayoutRes
    public abstract int getLayoutResId();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void configViews();

    public abstract void initDatas();


    protected abstract void setupActivityComponent(AppComponent appComponent);

    /**
     * 显示dialog
     *
     * @param dialogText 可为空，为空不显示文字
     */
    protected void showDialog(String dialogText) {
        // TODO Auto-generated method stub
        juhuaDialog = new JuhuaDialog(getActivity(), dialogText);
        juhuaDialog.show();
    }

    /**
     * 隐藏dialog
     */
    protected void dismissDialog() {
        // TODO Auto-generated method stub

        if (juhuaDialog != null)
            juhuaDialog.dismiss();
    }

    public boolean matchPhone(String text) {
        if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
            return true;
        }
        return false;
    }
}
