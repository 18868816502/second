package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.fragment.PageCollectionProductFragment;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

public class CollectionActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).titleBar(toolbar).init();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, new PageCollectionProductFragment())
                .commit();

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
