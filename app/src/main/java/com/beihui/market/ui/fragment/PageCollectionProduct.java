package com.beihui.market.ui.fragment;


import android.support.v7.widget.RecyclerView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;

public class PageCollectionProduct extends BaseComponentFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public int getLayoutResId() {
        return R.layout.pager_item_collection_product;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
