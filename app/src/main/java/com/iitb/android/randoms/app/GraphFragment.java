package com.iitb.android.randoms.app;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment{

    List<ParseObject> mNumbers;
    ArrayList<String> mNumbersForGraph;

    private RelativeLayout mainLayout;
    private LineChart mChart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        TextView view = (TextView) rootView.findViewById(R.id.graph);

        mainLayout = (RelativeLayout) rootView.findViewById(R.id.mainLayout);
        mChart = new LineChart(getActivity());
        mainLayout.addView(mChart);

        // Customize line chart
        mChart.setDescription("Random numbers");
        mChart.setNoDataTextDescription("No data for the moment");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable dragging and scaling
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);


        // enable pinch to zoom
        mChart.setPinchZoom(true);

        // set background color
        mChart.setBackgroundColor(Color.LTGRAY);


        // data management
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // get legend object
        Legend legend = mChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);

        YAxis yl = mChart.getAxisLeft();
        yl.setAxisMaxValue(120f);
        yl.setDrawGridLines(true);

        YAxis yl2 = mChart.getAxisRight();
        yl2.setEnabled(false);


        //getAllParseData();

        return rootView;
    }

    public void getAllParseData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RandomNumbers");
        query.setLimit(15);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    mNumbers = parseObjects;
                    mNumbersForGraph = new ArrayList<String>();
                    int i = 0;
                    for (ParseObject obj : mNumbers) {
                        mNumbersForGraph.add(obj.getString("number"));
                        //Log.i("parse object", obj.getString("number"));
                    }
                } else {
                    Log.i("parse error in Graph fragment", e.getMessage());
                }
            }
        });
    }
}
