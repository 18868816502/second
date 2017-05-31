package com.beihui.market.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.beihui.market.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/22.
 */

public class JuhuaDialog extends Dialog {


    @BindView(R.id.tv_dialog)
    TextView tvTitle;

    private String title;


    public JuhuaDialog(Context context, String title) {
        super(context, R.style.MyDialogStyleTop1);
        this.title = title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_juhua);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        tvTitle.setText(title);
    }



}
