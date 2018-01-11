package com.beihui.market.view.stateprovider;


import android.view.LayoutInflater;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.view.StateLayout;

public class DebtChannelSearchStateViewProvider implements StateLayout.StateViewProvider {

    private View.OnClickListener listener;

    public DebtChannelSearchStateViewProvider(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_EMPTY) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.layout_state_debt_channel_empty, container, false);
            if (listener != null) {
                view.findViewById(R.id.add).setOnClickListener(listener);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intercept
                }
            });
            return view;
        }
        return null;
    }
}
