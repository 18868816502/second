package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.component.AppComponent;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;


public class WorthTestActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_worth_test;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).init();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.select_menu1);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.worth_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("menuId " + item.getItemId());
        return true;
    }
}
