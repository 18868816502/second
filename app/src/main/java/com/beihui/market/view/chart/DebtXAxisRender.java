package com.beihui.market.view.chart;


import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class DebtXAxisRender extends XAxisRenderer {
    public DebtXAxisRender(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

}
