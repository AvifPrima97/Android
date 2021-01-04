package helpers;

import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.Calendar;
import java.util.Date;

public class ChartHelper1 implements OnChartValueSelectedListener {

    private LineChart mChart1;

    public ChartHelper1(LineChart chart) {
        mChart1 = chart;
        mChart1.setOnChartValueSelectedListener(this);

        // no description text
        mChart1.setNoDataText("You need to provide data for the chart.");

        // enable touch gestures
        mChart1.setTouchEnabled(true);

        // enable scaling and dragging
        mChart1.setDragEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart1.setPinchZoom(true);

        mChart1.setDoubleTapToZoomEnabled (true);
        mChart1.setHorizontalScrollBarEnabled (true);
        mChart1.getViewPortHandler (). setMaximumScaleX (5f);
        mChart1.getViewPortHandler (). setMaximumScaleY (5f);

        // set an alternative background color
        mChart1.setBackgroundColor(Color.WHITE);
        mChart1.setBorderColor(Color.rgb(67,164,34));


        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart1.setData(data);



        // get the legend (only possible after setting data)
        Legend l = mChart1.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(Typeface.MONOSPACE);
        l.setTextColor(Color.rgb(67, 164, 34));

        XAxis xl = mChart1.getXAxis();
        xl.setTypeface(Typeface.MONOSPACE);
        xl.setTextColor(Color.rgb(67, 164, 34));
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xl.setValueFormatter(new DateAxisValueFormatter(null));
        mChart1.getXAxis().setValueFormatter(new DateAxisValueFormatter(null));
        mChart1.invalidate(); //refresh



        YAxis leftAxis = mChart1.getAxisLeft();
        leftAxis.setTypeface(Typeface.MONOSPACE);
        leftAxis.setTextColor(Color.rgb(67, 164, 34));

        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart1.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setChart(LineChart chart){ this.mChart1 = chart; }

    public void addEntry(float value) {

        LineData data = mChart1.getData();

        if (data != null){

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(),value),0);
            Log.w("chart", set.getEntryForIndex(set.getEntryCount()-1).toString());

            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart1.notifyDataSetChanged();

            // limit the number of visible entries
            mChart1.setVisibleXRangeMaximum(10);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart1.moveViewTo(set.getEntryCount()-1, data.getYMax(), YAxis.AxisDependency.LEFT);



            // this automatically refreshes the chart (calls invalidate())
//             mChart1.moveViewTo(data.getXValCount()-7, 55f,
//             YAxis.AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "SPO2");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.rgb(67, 164, 34));
        //set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        //set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.rgb(67, 164, 34));
        set.setHighLightColor(Color.rgb(67, 164, 34));
        set.setValueTextColor(Color.rgb(67, 164, 34));
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected(){
        Log.i("Nothing selected", "Nothing selected.");
    }

    class DateAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;
        public DateAxisValueFormatter(String[] values) {
            this.mValues = values; }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
//        return sdf.format(new Date((long) value));
//        return sdf.format(c1.getTime());
            long msTime = System.currentTimeMillis();
            Date date = new Date(msTime);
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mma");
            String strdate1 = sdf1.format(c1.getTime());
            axis.setLabelCount(4, true);
            return sdf1.format(date);
//            String result = "";
//            Date date = new Date((long) value);
//            SimpleDateFormat prettyFormat = new SimpleDateFormat("H:mm");
//            Calendar c1 = Calendar.getInstance();
//            prettyFormat.format(c1.getTime());
////            prettyFormat.setTimeZone(TimeZone.getDefault());
//            result = prettyFormat.format(date);
//
//            return result;
        }
    }
}




//class DateAxisFormatter implements IAxisValueFormatter {
//
//    private Chart chart;
//
//    public  DateAxisFormatter( Chart chart ) {
//
//        this.chart = chart;
//    }
//
//    @Override
//    public String getFormattedValue(float value, AxisBase axis) {
//
//        String result = "";
//        Date date = new Date((long) value);
//        SimpleDateFormat prettyFormat = new SimpleDateFormat("H:mm");
//        prettyFormat.setTimeZone(TimeZone.getDefault());
//        result = prettyFormat.format(date);
//
//        return result;
//    }
//}