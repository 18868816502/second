package com.beihui.market.view.stateprovider;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.view.StateLayout;

public class MyProductStateProvider implements StateLayout.StateViewProvider {
    private Context context;

    public MyProductStateProvider(Context context) {
        this.context = context;
    }

    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_EMPTY) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_state_empty, container, false);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(R.drawable.product_state_empty);
            ((TextView) view.findViewById(R.id.text)).setText("暂无产品");
            return view;
        }
        return null;
    }
}
