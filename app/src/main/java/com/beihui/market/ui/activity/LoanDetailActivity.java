package com.beihui.market.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        setupToolbar(toolbar);
        hitDistance = (int) (getResources().getDisplayMetrics().density * 30);
        scrollView.setOnScrollListener(new WatchableScrollView.OnScrollListener() {
            boolean selected;

            @Override
            public void onScrolled(int dy) {
                if (dy <= 5) {
                    if (!selected) {
                        changeToolBarIconState(true);
                        selected = true;
                    }
                } else if (dy >= hitDistance) {
                    if (selected) {
                        changeToolBarIconState(false);
                        selected = false;
                    }
                } else {
                    renderBar((float) dy / hitDistance);
                }
            }
        });

        ImmersionBar.with(this).fitsSystemWindows(true).init();
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    private void changeToolBarIconState(boolean selected) {
        int[] state = selected ? selectedState : noneState;
        int color = selected ? Color.WHITE : getResources().getColor(R.color.colorPrimary);
        //noinspection ConstantConditions
        toolbar.getNavigationIcon().setState(state);
        toolbar.getMenu().findItem(R.id.share).getIcon().setState(state);
        toolbar.setBackgroundColor(color);
        ImmersionBar.with(this).statusBarColorInt(color).init();

        loanNameTitleTv.setSelected(!selected);
    }

    private void renderBar(float alpha) {
        int alphaInt = (int) (alpha * 255);
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
