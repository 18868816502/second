package com.beiwo.qnejqaz.ui.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ReplacementSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.LoanProduct;
import com.beiwo.qnejqaz.entity.LoanProductDetail;
import com.beiwo.qnejqaz.entity.ThirdAuthorization;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.ui.contract.LoanProductDetailContract;
import com.beiwo.qnejqaz.ui.dialog.ThirdAuthorizationDialog;
import com.beiwo.qnejqaz.ui.presenter.LoanDetailPresenter;
import com.beiwo.qnejqaz.umeng.Events;
import com.beiwo.qnejqaz.umeng.Statistic;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.util.FastClickUtils;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;
import com.beiwo.qnejqaz.view.WatchableScrollView;
import com.beiwo.qnejqaz.view.busineesrel.RateView;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 产品详情 (如热门产品单个item点击事件跳转)
 */
public class LoanDetailActivity extends BaseComponentActivity implements LoanProductDetailContract.View {

    @BindView(R.id.navigate)
    View navigateView;
    @BindView(R.id.collect)
    View collectView;
    @BindView(R.id.share)
    View shareView;
    @BindView(R.id.base_container)
    View baseContainer;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.scroll_view)
    WatchableScrollView scrollView;
    //我要借款
    @BindView(R.id.apply)
    TextView applyBtn;
    @BindView(R.id.product_off_sell_container)
    View productOffSellContainer;
    @BindView(R.id.detail_container)
    LinearLayout detailContainer;
    @BindView(R.id.product_detail_container)
    LinearLayout productDetailContainer;
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
    @BindView(R.id.interest_text)
    TextView interestText;

    private int hitDistance;
    private LoanProduct.Row productAbstract;
    private LoanProductDetail productDetail;
    LoanDetailPresenter presenter;
    private ThirdAuthorizationDialog authDialog;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_loan_detail;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        presenter = new LoanDetailPresenter(this, this);
        hitDistance = (int) (getResources().getDisplayMetrics().density * 30);
        scrollView.setOnScrollListener(new WatchableScrollView.OnScrollListener() {
            @Override
            public void onScrolled(int dy) {
                renderBar(dy / (float) hitDistance);
                boolean selected = dy >= hitDistance / 2;
                navigateView.setSelected(selected);
                collectView.setSelected(selected);
                shareView.setSelected(selected);
                loanNameTitleTv.setSelected(selected);
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FastClickUtils.isFastClick()) {
                    //pv，uv统计
                    DataHelper.getInstance(LoanDetailActivity.this).onCountUv(DataHelper.ID_CLICK_LOAN_REQUESTED);
                    presenter.clickLoanRequested();
                }
            }
        });
        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
        LoanProduct.Row loan = getIntent().getParcelableExtra("loan");
        String loanId = getIntent().getStringExtra("loanId");
        String loanName = getIntent().getStringExtra("loanName");
        if (loanName != null) {
            loanNameTitleTv.setText(loanName);
        }
        if (loan != null) {
            bindAbstractInfo(loan);
            presenter.queryDetail(loan.getId());
        } else {
            presenter.queryDetail(loanId);
        }
    }

    @Override
    public void setPresenter(LoanProductDetailContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showLoanDetail(LoanProductDetail detail) {
        productDetail = detail;
        LoanProductDetail.Base base = detail.getBase();
        if (base != null) {
            //参考日，月息
            if (detail.getBase().getInterestTimeText() != null) {
                interestText.setText(detail.getBase().getInterestTimeText());
            }
            //name
            if (base.getProductName() != null) {
                loanNameTitleTv.setText(base.getProductName());
                loanNameTv.setText(base.getProductName());
            }
            //logo
            if (!TextUtils.isEmpty(base.getLogoUrl())) {
                Glide.with(this)
                        .load(base.getLogoUrl())
                        .asBitmap()
                        .placeholder(R.drawable.image_place_holder)
                        .into(loanIconIv);
            } else {
                loanIconIv.setImageResource(R.drawable.image_place_holder);
            }
            //tags
            String[] feature = base.getFeature() != null ? base.getFeature().split(",") : null;
            if (feature != null && feature.length > 0 && !feature[0].equals("")) {
                inflateTag(feature);
            }
            //loaned number
            SpannableString ss = new SpannableString("已借款" + CommonUtils.getFormatNumber(base.getSuccessCount()) + "人");
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff395e")), 3, ss.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            loanedNumberTv.setText(ss);
            //success point
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
            //loan max amount
            if (base.getBorrowingHighText() != null) {
                loanMaxAmountTv.setText(getSpan(base.getBorrowingHighText()));
            }
            //loan interest
            if (base.getInterestLowText() != null) {
                loanInterestsTv.setText(getSpan(base.getInterestLowText()));
            }
            //loan due time
            if (base.getDueTimeText() != null) {
                loanTimeRangeTv.setText(getSpan(base.getDueTimeText()));
            }
            //bottom tags
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
            //是否已经收藏
            collectView.setActivated(base.isCollected());
        }
        List<LoanProductDetail.Explain> list = detail.getExplain();
        if (list != null && list.size() > 0) {
            inflateFeature(list);
        }
    }

    @Override
    public void showLoanRequestText(String text) {
        applyBtn.setText(text);
    }

    @Override
    public void showLoanOffSell() {
        productDetailContainer.setVisibility(View.GONE);
        productOffSellContainer.setVisibility(View.VISIBLE);

        toolbar.getMenu().clear();
    }

    @Override
    public void showAddCollectionSuccess(String msg) {
        //umeng统计
        Statistic.onEvent(Events.CLICK_LOAN_COLLECT);

        WeakRefToastUtil.showShort(this, msg, null);
        collectView.setActivated(true);
    }

    @Override
    public void showDeleteCollectionSuccess(String msg) {
        //umeng统计
        Statistic.onEvent(Events.CLICK_LOAN_DELETE_COLLECTION);

        WeakRefToastUtil.showShort(this, msg, null);
        collectView.setActivated(false);
    }

    @Override
    public void navigateThirdPartLoanPage(String title, String url) {
        Intent intent = new Intent(LoanDetailActivity.this, ComWebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    @Override
    public void navigateAuthorizationPage(String id) {
        //umeng统计
        Statistic.onEvent(Events.LOAN_ENTER_AUTHORIZE);

        authDialog = new ThirdAuthorizationDialog();
        authDialog.setPresenter(presenter);
        authDialog.show(getSupportFragmentManager(), "Auth");
    }

    @Override
    public void navigateRecommendProduct(int amount) {
        //umeng统计
        Statistic.onEvent(Events.RESUME_RELEVANT_PRODUCT_FROM_DETAIL);

        Intent intent = new Intent(this, RecommendProductActivity.class);
        intent.putExtra("amount", amount);
        startActivity(intent);
    }

    @Override
    public void showThirdAuthorization(ThirdAuthorization auth) {
        if (authDialog != null && authDialog.isAdded()) {
            authDialog.updateAuthorization(auth);
        }
    }

    @Override
    public void showAuthorizeResult(boolean success, String msg) {
        if (authDialog != null) {
            authDialog.dismiss();
        }
        //注册不成功，并且有提示
        if (!success && msg != null) {
            WeakRefToastUtil.showShort(this, msg, null);
        }
    }

    @Override
    public void updateRegisterDialogVisibility(boolean visible) {
        if (visible) {
            showProgress(null);
        } else {
            dismissProgress();
        }
    }

    @OnClick({R.id.collect, R.id.share})
    void onToolbarIconClicked(View view) {
        switch (view.getId()) {
            case R.id.collect:
                presenter.clickCollection();
                break;
            case R.id.share:
                //umeng统计
                Statistic.onEvent(Events.LOAN_DETAIL_CLICK_SHARE);

                /*UMWeb umWeb = null;
                if (productDetail != null && productDetail.getBase() != null) {
                    umWeb = new UMWeb(NetConstants.generateProductUrl(productDetail.getBase().getId()));
                    UMImage image = new UMImage(this, productDetail.getBase().getLogoUrl());
                    umWeb.setThumb(image);
                    umWeb.setTitle(productDetail.getBase().getProductName());
                    umWeb.setDescription(productDetail.getBase().getExplains());
                } else if (productAbstract != null) {
                    umWeb = new UMWeb(NetConstants.generateProductUrl(productAbstract.getId()));
                    UMImage image = new UMImage(this, productAbstract.getLogoUrl());
                    umWeb.setThumb(image);
                    umWeb.setTitle(productAbstract.getProductName());
                }

                if (umWeb != null) {
                    new ShareDialog()
                            .setUmWeb(umWeb)
                            .show(getSupportFragmentManager(), ShareDialog.class.getSimpleName());
                }*/
                break;
        }
    }

    private void renderBar(float alpha) {
        int alphaInt = (int) (alpha * 255);
        alphaInt = alphaInt < 10 ? 0 : alphaInt;
        alphaInt = alphaInt > 255 ? 255 : alphaInt;
        int color = Color.argb(alphaInt, 85, 145, 255);
        toolbar.setBackgroundColor(color);
    }

    private void inflateTag(String[] tags) {
        tagContainer.removeAllViews();
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
                ((TextView) rootView.findViewById(R.id.feature_content)).setText(explain.getExplainContent());

            }
            detailContainer.addView(rootView);
        }
    }

    private void bindAbstractInfo(LoanProduct.Row loan) {
        productAbstract = loan;
        //name
        if (loan.getProductName() != null) {
            loanNameTitleTv.setText(loan.getProductName());
            loanNameTv.setText(loan.getProductName());
        }
        //logo
        if (!TextUtils.isEmpty(loan.getLogoUrl())) {
            Glide.with(this)
                    .load(loan.getLogoUrl())
                    .asBitmap()
                    .placeholder(R.drawable.image_place_holder)
                    .into(loanIconIv);
        } else {
            loanIconIv.setImageResource(R.drawable.image_place_holder);
        }
        //tags
        String[] feature = loan.getFeature() != null ? loan.getFeature().split(",") : null;
        if (feature != null && feature.length > 0 && !feature[0].equals("")) {
            inflateTag(feature);
        }
        //loaned number
        SpannableString ss = new SpannableString("已借款" + CommonUtils.getFormatNumber(loan.getSuccessCount()) + "人");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff395e")), 3, ss.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        loanedNumberTv.setText(ss);
        //loan max amount
        if (loan.getBorrowingHighText() != null) {
            loanMaxAmountTv.setText(getSpan(loan.getBorrowingHighText()));
        }
        //loan interest
        if (loan.getInterestLowText() != null) {
            loanInterestsTv.setText(getSpan(loan.getInterestLowText()));
        }
        //loan due time
        if (loan.getDueTimeText() != null) {
            loanTimeRangeTv.setText(getSpan(loan.getDueTimeText()));
        }

    }

    private SpannableString getSpan(String text) {
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new SizePosSpan(11), text.length() - 1, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        return ss;
    }


    private class SizePosSpan extends ReplacementSpan {

        private int size;
        private TextPaint textPaint;

        SizePosSpan(int size) {
            this.size = (int) (size * getResources().getDisplayMetrics().density);
            textPaint = new TextPaint();
            textPaint.setTextSize(this.size);
            textPaint.setFakeBoldText(true);
            textPaint.setAntiAlias(true);
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
            canvas.drawText(text.toString(), x + 6, top + (fm.bottom - fm.top) / 2, textPaint);
        }
    }
}
