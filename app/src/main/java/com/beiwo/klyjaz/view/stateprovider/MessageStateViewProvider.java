package com.beiwo.klyjaz.view.stateprovider;

import android.view.LayoutInflater;
import android.view.View;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.view.StateLayout;


public class MessageStateViewProvider implements StateLayout.StateViewProvider {
    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_EMPTY) {
            return LayoutInflater.from(container.getContext())
                    .inflate(R.layout.empty_layout, container, false);
        }
        return null;
    }
}
