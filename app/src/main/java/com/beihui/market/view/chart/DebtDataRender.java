package com.beihui.market.view.chart;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IValueFormatter;
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
    private float highlightTextSize;

    private Entry highlightEntry;

    public DebtDataRender(Context context, LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        highlightTextSize = context.getResources().getDisplayMetrics().density * 9;
    }

    @Override
    protected boolean shouldDrawValues(IDataSet set) {
        return true;
    }

    @Override
    public void drawValues(Canvas c) {
        super.drawValues(c);
    }

    @Override
    public void drawValue(Canvas c, IValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {
        if (entry == highlightEntry) {
            mValuePaint.setTextSize(highlightTextSize);
            float halfStrSize = mValuePaint.measureText(value + "") / 2.0f;
            if (halfStrSize > x) {
                x = halfStrSize + 5;
            } else if (mChart.getWidth() - halfStrSize < x) {
                x = mChart.getWidth() - halfStrSize - 5;
            }
            super.drawValue(c, formatter, value, entry, dataSetIndex, x, y, color);

        }
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        LineData lineData = mChart.getLineData();

        for (Highlight high : indices) {

            ILineDataSet set = lineData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            Entry e = set.getEntryForXValue(high.getX(), high.getY());
            highlightEntry = e;

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
        mHighlightLinePath.moveTo(x, y);
        mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());
        c.drawPath(mHighlightLinePath, mHighlightPaint);
    }
}
