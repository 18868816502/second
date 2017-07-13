package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;
import com.beihui.market.view.WatchableScrollView;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;


public class LoanDetailActivity extends BaseActivity {
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
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.dark_light_state_navigation);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        hitDistance = (int) (getResources().getDisplayMetrics().density * 30);
        scrollView.setOnScrollListener(new WatchableScrollView.OnScrollListener() {
            boolean selected;

            @Override
            public void onScrolled(int dy) {
                if (dy <= 5) {
                    changeToolBarIconState(false);
                } else if (dy >= hitDistance) {
                    changeToolBarIconState(true);
                }
            }
        });

        ImmersionBar.with(this).fitsSystemWindows(true).barColor("#D8D8D8").init();
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    private void changeToolBarIconState(boolean selected) {
        int[] state = selected ? selectedState : noneState;
        //noinspection ConstantConditions
        toolbar.getNavigationIcon().setState(state);
        toolbar.getMenu().findItem(R.id.share).getIcon().setState(state);
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
