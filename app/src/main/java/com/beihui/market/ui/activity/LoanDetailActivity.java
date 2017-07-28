package com.beihui.market.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.view.WatchableScrollView;
import com.beihui.market.view.busineesrel.RateView;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;


public class LoanDetailActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.scroll_view)
    WatchableScrollView scrollView;
    @BindView(R.id.apply)
    Button applyBtn;

    @BindView(R.id.detail_container)
    LinearLayout detailContainer;
    @BindView(R.id.loan_name_title)
    TextView loanNameTitleTv;
    @BindView(R.id.loan_icon)
    ImageView loanIconIv;
    @BindView(R.id.loan_name)
    TextView loanNameTv;
    @BindView(R.id.for_people_tag)
    TextView forPeopleTagTv;
    @BindView(R.id.tag_container)
    LinearLayout tagContainer;
    @BindView(R.id.rate_view)
    RateView rateView;
    @BindView(R.id.loaned_number)
    TextView loanedNumberTv;
    @BindView(R.id.loan_max_amount)
    TextView loanMaxAmountTv;
    @BindView(R.id.loan_interests)
    TextView loanInterestsTv;
    @BindView(R.id.loan_time_range)
    TextView loanTimeRangeTv;

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
        bindFakeData();
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

    private void bindFakeData() {
        loanNameTitleTv.setText("现金巴士");
        loanNameTv.setText("现金巴士");
        forPeopleTagTv.setText("上班族");
        inflateTag();
        rateView.setRate(3, true);
        String loanedNumber = "999";
        SpannableString ss = new SpannableString("成功借款" + loanedNumber + "人");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff395e")), 4, ss.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        loanedNumberTv.setText(ss);
        loanMaxAmountTv.setText("50万元");
        loanInterestsTv.setText("0.1%");
        loanTimeRangeTv.setText("12-36月");
        inflateFeature();
    }

    private void inflateTag() {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < 3; ++i) {
            TextView textView = (TextView) inflater.inflate(R.layout.layout_tag_text, null);
            textView.setText("tag " + i);
            tagContainer.addView(textView);
        }
    }

    private void inflateFeature() {
        LayoutInflater inflater = LayoutInflater.from(this);
        String content = "<p>哈哈哈</p><br/><p>哈哈哈</p><br/><p>哈哈哈</p><br/>";
        for (int i = 0; i < 6; ++i) {
            View rootView = inflater.inflate(R.layout.layout_loan_detail_feature, null);
            ((TextView) rootView.findViewById(R.id.feature_title)).setText("FeatureTitle " + i);
            ((TextView) rootView.findViewById(R.id.feature_content)).setText(Html.fromHtml(content));
            detailContainer.addView(rootView);
        }
    }
}
