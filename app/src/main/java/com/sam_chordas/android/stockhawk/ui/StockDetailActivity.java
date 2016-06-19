package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import com.sam_chordas.android.stockhawk.R;

import java.util.ArrayList;


public class StockDetailActivity extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart mLineChart;

    private static final float Y_AXIS_MIN_VALUE = 0f;
    private static final int X_AXIS_INITIAL_VALUE = 10;
    private static final int X_AXIS_LIMIT = 26;

    String name;
    String open;
    String daysHigh;
    String daysLow;
    String previousClose;
    String fiftydayMovingAvg;
    String twoHundredDayMovingavg;
    String lastTradePriceOnly;
    String bookValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        open = bundle.getString("open");
        daysHigh = bundle.getString("daysHigh");
        daysLow = bundle.getString("daysLow");
        previousClose = bundle.getString("previousClose");
        fiftydayMovingAvg = bundle.getString("fiftydayMovingAvg");
        twoHundredDayMovingavg = bundle.getString("twoHundredDayMovingavg");
        lastTradePriceOnly = bundle.getString("lastTradePriceOnly");
        bookValue = bundle.getString("bookValue");

        mLineChart = (LineChart) findViewById(R.id.line_chart);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(name);
        }

        mLineChart.setOnChartGestureListener(this);
        mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.setDrawGridBackground(false);

        // Description
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription(getString(R.string.label_no_data));

        // EaseIn animation
        mLineChart.animateX(2000, Easing.EasingOption.EaseInOutBounce);

        // Enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.setLogEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);

        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(1f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMinValue(Y_AXIS_MIN_VALUE);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(true);
        mLineChart.getAxisRight().setEnabled(false);
        setData();

    }

    private void setData() {

        ArrayList<String> xVals = new ArrayList<String>();

        for(int i = X_AXIS_INITIAL_VALUE; i < X_AXIS_LIMIT; i+=2){
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        yVals.add(new Entry(Float.parseFloat(previousClose), 0));
        yVals.add(new Entry(Float.parseFloat(open), 1));
        yVals.add(new Entry(Float.parseFloat(bookValue), 2));
        yVals.add(new Entry(Float.parseFloat(daysLow), 3));
        yVals.add(new Entry(Float.parseFloat(daysHigh), 4));
        yVals.add(new Entry(Float.parseFloat(fiftydayMovingAvg), 5));
        yVals.add(new Entry(Float.parseFloat(twoHundredDayMovingavg), 6));
        yVals.add(new Entry(Float.parseFloat(lastTradePriceOnly), 7));

        // Data-set creation
        LineDataSet set1 = new LineDataSet(yVals, getString(R.string.y_axis_label));
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillColor(getResources().getColor(R.color.graph_trail));
        set1.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);
        LineData data = new LineData(xVals, dataSets);
        mLineChart.setData(data);
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if(String.valueOf(e.getVal()).equalsIgnoreCase(previousClose)) {
            showSnackbar(getString(R.string.label_previous_close) + " : " + e.getVal());
        }else if (String.valueOf(e.getVal()).equalsIgnoreCase(open)){
            showSnackbar(getString(R.string.label_open) + " : " + e.getVal());
        }else if (String.valueOf(e.getVal()).equalsIgnoreCase(bookValue)){
            showSnackbar(getString(R.string.label_boook_value) + " : " + e.getVal());
        }else if (String.valueOf(e.getVal()).equalsIgnoreCase(daysLow)){
            showSnackbar(getString(R.string.label_days_low) + " : " + e.getVal());
        }else if (String.valueOf(e.getVal()).equalsIgnoreCase(daysHigh)){
            showSnackbar(getString(R.string.label_days_high) + " : " + e.getVal());
        }else if (String.valueOf(e.getVal()).equalsIgnoreCase(fiftydayMovingAvg)){
            showSnackbar(getString(R.string.label_fifty_day_moving_avg) + " : " + e.getVal());
        }else if (String.valueOf(e.getVal()).equalsIgnoreCase(twoHundredDayMovingavg)){
            showSnackbar(getString(R.string.label_two_hundred_day) + " : " + e.getVal());
        }else if (String.valueOf(e.getVal()).equalsIgnoreCase(lastTradePriceOnly)) {
            showSnackbar(getString(R.string.label_last_trade_price) + " : " + e.getVal());
        }

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void showSnackbar(String content){
        View view = (View) findViewById(R.id
                .detail_layout);
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).show();
    }
}
