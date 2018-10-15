package com.beiwo.klyjaz.ui.activity;


import android.support.v7.widget.Toolbar;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.ui.fragment.PageCollectionProductFragment;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * 我的收藏
 */
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
                .commitAllowingStateLoss();

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
