package com.beiwo.qnejqaz.view.stateprovider;


import android.view.LayoutInflater;
import android.view.View;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.view.StateLayout;

public class DebtStateProvider implements StateLayout.StateViewProvider {
    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_EMPTY) {
            return LayoutInflater.from(container.getContext())
                    .inflate(R.layout.layout_debt_state_empty, container, false);
        }
        return null;
    }
}
