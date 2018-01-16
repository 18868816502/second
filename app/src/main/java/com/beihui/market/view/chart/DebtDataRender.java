package com.beihui.market.view.chart;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;

import com.beihui.market.R;
import com.beihui.market.util.CommonUtils;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class DebtDataRender extends LineChartRenderer {

    private Path mHighlightLinePath = new Path();
    private int topPadding;
    private Paint highlightValuePaint = new Paint();

    public DebtDataRender(Context context, LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        topPadding = (int) (context.getResources().getDisplayMetrics().density * 6);
        highlightValuePaint.setColor(context.getResources().getColor(R.color.colorPrimary));
        highlightValuePaint.setTextSize((int) (context.getResources().getDisplayMetrics().density * 9));
        highlightValuePaint.setTextAlign(Paint.Align.CENTER);
        highlightValuePaint.setTypeface(Typeface.DEFAULT);
        highlightValuePaint.setStrokeWidth(context.getResources().getDisplayMetrics().density * 1.5f);

    }

    @Override
    protected boolean shouldDrawValues(IDataSet set) {
        return true;
    }

    @Override
    public void drawData(Canvas c) {
        super.drawData(c);
    }

    @Override
    public void drawValues(Canvas c) {
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        LineData lineData = mChart.getLineData();

        for (Highlight high : indices) {

            ILineDataSet set = lineData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            Entry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), e.getY() * mAnimator
                    .getPhaseY());

            high.setDraw((float) pix.x, (float) pix.y);

            // draw the lines
            drawHighLines(c, (float) pix.x, (float) pix.y, set, indices[0]);
        }
    }

    private void drawHighLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set, Highlight highlight) {
        // set color and stroke-width
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());

        // draw highlighted lines (if enabled)
        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());

        // create vertical path
        mHighlightLinePath.reset();
        mHighlightLinePath.moveTo(x, Math.max(y - topPadding, mViewPortHandler.contentTop()));
        mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());
        c.drawPath(mHighlightLinePath, mHighlightPaint);

        String debtStr = CommonUtils.keep2digitsWithoutZero(highlight.getY());
        float textSize = highlightValuePaint.getTextSize();
        float exceptedTop = y - textSize - topPadding - 5;
        int entryCount = set.getEntryCount();
        if (entryCount > 1) {
            if (Math.abs(highlight.getX() - 0.0) <= 0.000001) {
                highlightValuePaint.setTextAlign(Paint.Align.LEFT);
            } else if (Math.abs(highlight.getX() - entryCount + 1) <= 0.000001) {
                highlightValuePaint.setTextAlign(Paint.Align.RIGHT);
            } else {
                highlightValuePaint.setTextAlign(Paint.Align.CENTER);
            }
            if (exceptedTop < mViewPortHandler.contentTop()) {
                if (x + textSize * debtStr.length() / 2 + 2 < mViewPortHandler.contentRight()) {
                    c.drawText(debtStr, x + textSize * debtStr.length() / 2 + 2, y, highlightValuePaint);
                } else {
                    c.drawText(debtStr, x - textSize * debtStr.length() / 2 - 2, y, highlightValuePaint);
                }
            } else {
                c.drawText(debtStr, x, exceptedTop + textSize, highlightValuePaint);
            }

        } else {
            highlightValuePaint.setTextAlign(Paint.Align.CENTER);
            c.drawText(debtStr, x, exceptedTop + textSize, highlightValuePaint);
        }
    }
}
