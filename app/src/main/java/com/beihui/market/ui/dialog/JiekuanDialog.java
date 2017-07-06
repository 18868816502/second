package com.beihui.market.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/22.
 */

public class JiekuanDialog extends Dialog {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_tishi)
    TextView tvTishi;

    private String title;
    private String tishi;


    public JiekuanDialog(Context context, String title, String tishi) {
        super(context, R.style.MyDialogStyleTop);
        this.title = title;
        this.tishi = tishi;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jiekuan);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        tvTitle.setText(title);
        tvTishi.setText(tishi);
    }


    @OnClick({R.id.tv_queding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_queding:
                onOkClick();
                break;
        }
    }


    public void onOkClick() {
        dismiss();
    }

}
