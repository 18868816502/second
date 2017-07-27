package com.beihui.market.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.view.WatchableScrollView;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;


public class LoanDetailActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.scroll_view)
    WatchableScrollView scrollView;
    @BindView(R.id.apply)
    Button applyBtn;

    @BindView(R.id.loan_name_title)
    TextView loanNameTitleTv;
    @BindView(R.id.loan_icon)
    ImageView loanIconIv;
    @BindView(R.id.loan_name)
    TextView loanNameTv;
    @BindView(R.id.for_people_tag)
    TextView forPeopleTagTv;
    @BindView(R.id.loaned_number)
    TextView loanedNumberTv;
    @BindView(R.id.loan_max_amount)
    TextView loanMaxAmountTv;
    @BindView(R.id.loan_interests)
    TextView loanInterestsTv;
    @BindView(R.id.loan_time_range)
    TextView loanTimeRangeTv;
    @BindView(R.id.for_people)
    TextView forPeopleTv;
    @BindView(R.id.stuff)
    TextView stuffTv;
    @BindView(R.id.review_des)
    TextView reviewDesTv;

    private int hitDistance;

    private int[] selectedState = new int[]{android.R.attr.state_selected};
    private int[] noneState = new int[]{};

    @Override
    public int getLayoutId() {
        return R.layout.activity_loan_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar, false);
        setupToolbarBackNavigation(toolbar, R.drawable.dark_light_state_navigation);
        ImmersionBar.with(this).fitsSystemWindows(true).init();
        hitDistance = (int) (getResources().getDisplayMetrics().density * 30);
        scrollView.setOnScrollListener(new WatchableScrollView.OnScrollListener() {
            @Override
            public void onScrolled(int dy) {
                renderBar(dy / (float) hitDistance);
                changeToolBarIconState(dy >= hitDistance / 2);
            }
        });
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    private void changeToolBarIconState(boolean selected) {
        int[] state = selected ? selectedState : noneState;
        //noinspection ConstantConditions
        toolbar.getNavigationIcon().setState(state);
        toolbar.getMenu().findItem(R.id.share).getIcon().setState(state);
        loanNameTitleTv.setSelected(selected);
    }

    private void renderBar(float alpha) {
        int alphaInt = (int) (alpha * 255);
        alphaInt = alphaInt < 10 ? 0 : alphaInt;
        alphaInt = alphaInt > 255 ? 255 : alphaInt;
        int color = Color.argb(alphaInt, 85, 145, 255);
        toolbar.setBackgroundColor(color);
        ImmersionBar.with(this).statusBarColorInt(color).init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_loan_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
