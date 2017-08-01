package com.beihui.market.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.beihui.market.R;

public class CommStateViewProvider implements StateLayout.StateViewProvider {

    private Context context;
    private View.OnClickListener onClickListener;

    public CommStateViewProvider(Context context, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_EMPTY) {
            return LayoutInflater.from(context)
                    .inflate(R.layout.layout_state_empty, container, false);
        } else if (newState == StateLayout.STATE_NET_ERROR) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_state_net_error, container, false);
            view.findViewById(R.id.reload).setOnClickListener(onClickListener);
            return view;
        }
        return null;
    }
}
