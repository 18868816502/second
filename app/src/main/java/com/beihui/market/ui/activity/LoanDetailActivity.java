package com.beihui.market.ui.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ReplacementSpan;
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
import com.beihui.market.entity.LoanProduct;
import com.beihui.market.entity.LoanProductDetail;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerLoanDetailComponent;
import com.beihui.market.injection.module.LoanDetailModule;
import com.beihui.market.ui.contract.LoanProductDetailContract;
import com.beihui.market.ui.presenter.LoanDetailPresenter;
import com.beihui.market.view.WatchableScrollView;
import com.beihui.market.view.busineesrel.RateView;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class LoanDetailActivity extends BaseComponentActivity implements LoanProductDetailContract.View {
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
    @BindView(R.id.tab_1)
    TextView tab1Tv;
    @BindView(R.id.tab_2)
    TextView tab2Tv;
    @BindView(R.id.tab_3)
    TextView tab3Tv;


    private int hitDistance;

    @Inject
    LoanDetailPresenter presenter;

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
        LoanProduct.Row loan = getIntent().getParcelableExtra("loan");
        if (loan != null) {
            bindAbstractInfo(loan);
        }
        presenter.onStart();
        presenter.queryDetail(loan.getId());
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerLoanDetailComponent.builder()
                .appComponent(appComponent)
                .loanDetailModule(new LoanDetailModule(this))
                .build()
                .inject(this);
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

    private void inflateTag(String[] tags) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (String tag : tags) {
            TextView textView = (TextView) inflater.inflate(R.layout.layout_tag_text, null);
            textView.setText(tag);
            tagContainer.addView(textView);
        }
    }

    private void inflateFeature(List<LoanProductDetail.Explain> list) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < list.size(); ++i) {
            View rootView = inflater.inflate(R.layout.layout_loan_detail_feature, null);
            LoanProductDetail.Explain explain = list.get(i);

            if (explain.getExplainName() != null) {
                ((TextView) rootView.findViewById(R.id.feature_title)).setText(explain.getExplainName());
            }
            if (explain.getExplainContent() != null) {
                ((TextView) rootView.findViewById(R.id.feature_content)).setText(Html.fromHtml(explain.getExplainContent()));
            }
            detailContainer.addView(rootView);
        }
    }

    private void bindAbstractInfo(LoanProduct.Row loan) {
        //name
        if (loan.getProductName() != null) {
            loanNameTitleTv.setText(loan.getProductName());
            loanNameTv.setText(loan.getProductName());
        }
        //logo
        if (loan.getLogo() != null) {
            Glide.with(this)
                    .load(loan.getLogo())
                    .into(loanIconIv);
        }
        //tags
        String[] feature = loan.getFeature() != null ? loan.getFeature().split(",") : null;
        if (feature != null && feature.length > 0 && !feature[0].equals("")) {
            inflateTag(feature);
        }
        //loaned number
        SpannableString ss = new SpannableString("成功借款" + loan.getSuccessCount() + "人");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff395e")), 4, ss.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        loanedNumberTv.setText(ss);
        //loan max amount
        if (loan.getBorrowingHighText() != null) {
            String maxAmount = loan.getBorrowingHighText();
            ss = new SpannableString(maxAmount);
            ss.setSpan(new SizePosSpan(11), maxAmount.length() - 1, maxAmount.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            loanMaxAmountTv.setText(ss);
        }
        //loan interest
        if (loan.getInterestLowText() != null) {
            String interestText = loan.getInterestLowText();
            ss = new SpannableString(interestText);
            ss.setSpan(new SizePosSpan(11), interestText.length() - 1, interestText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            loanInterestsTv.setText(ss);
        }
        //loan due time
        if (loan.getDueTimeText() != null) {
            String time = loan.getDueTimeText();
            ss = new SpannableString(time);
            ss.setSpan(new SizePosSpan(11), time.length() - 1, time.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            loanTimeRangeTv.setText(ss);
        }

    }

    @Override
    public void setPresenter(LoanProductDetailContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showLoanDetail(LoanProductDetail detail) {
        LoanProductDetail.Base base = detail.getBase();
        if (base != null) {
            if (base.getSuccessCountPointText() != null) {
                try {
                    int rate = (int) (Double.parseDouble(base.getSuccessCountPointText()) * 10);
                    int star = rate / 10;
                    int tail = rate % 10;
                    if (tail != 0) {
                        rateView.setRate(star + 1, true);
                    } else {
                        rateView.setRate(star, false);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (base.getOrientCareerText() != null && !base.getOrientCareerText().equals("全部")) {
                forPeopleTagTv.setVisibility(View.VISIBLE);
                forPeopleTagTv.setText(base.getOrientCareerText());
            }
            if (base.getFastestLoanTimeText() != null) {
                tab1Tv.setText(base.getFastestLoanTimeText());
            }
            if (base.getMortgageMethodText() != null) {
                tab2Tv.setText(base.getMortgageMethodText());
            }
            if (base.getRepayMethodText() != null) {
                tab3Tv.setText(base.getRepayMethodText());
            }
        }
        List<LoanProductDetail.Explain> list = detail.getExplain();
        if (list != null && list.size() > 0) {
            inflateFeature(list);
        }
    }

    class SizePosSpan extends ReplacementSpan {

        private int size;
        private TextPaint textPaint;


        SizePosSpan(int size) {
            this.size = (int) (size * getResources().getDisplayMetrics().density);
            textPaint = new TextPaint();
            textPaint.setTextSize(this.size);
        }

        @Override
        public int getSize(@NonNull Paint paint, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fm) {
            text = text.subSequence(start, end);
            return (int) paint.measureText(text.toString());
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
            text = text.subSequence(start, end);
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            textPaint.setColor(paint.getColor());
            canvas.drawText(text.toString(), x, top + (fm.bottom - fm.top) / 2, textPaint);
        }
    }
}
