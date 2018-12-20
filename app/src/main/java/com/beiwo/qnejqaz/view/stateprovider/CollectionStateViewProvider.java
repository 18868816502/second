package com.beiwo.qnejqaz.view.stateprovider;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.view.StateLayout;

public class CollectionStateViewProvider implements StateLayout.StateViewProvider {
    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_EMPTY) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.layout_state_empty, container, false);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(R.drawable.product_state_empty);
            ((TextView) view.findViewById(R.id.text)).setText("暂无收藏");
            return view;
        }
        return null;
    }
}
