package com.beiwo.qnejqaz.view.stateprovider;

import android.view.LayoutInflater;
import android.view.View;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.view.StateLayout;


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
