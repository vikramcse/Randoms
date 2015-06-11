package com.iitb.android.randoms.app;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment{

    List<ParseObject> mNumbers;
    ArrayList<BarEntry> valueSet1;

    BarChart chart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        setHasOptionsMenu(true);
        getAllParseData();

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                getAllParseData();
                Log.i("refresh", "clicked");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<String> getXAxisValues(List<ParseObject> mNumbers) {
        ArrayList<String> xAxis = new ArrayList<String>();
        for (int i = 1; i <= 24; i++) {
            xAxis.add(i + "");
        }

        return xAxis;
    }

    public void getAllParseData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RandomNumbers");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    mNumbers = parseObjects;

                    setData(mNumbers);
                } else {
                    Log.i("parse error in Graph fragment", e.getMessage());
                }
            }
        });
    }

    private void setData(List<ParseObject> mNumbers) {
        chart = (BarChart) getView().findViewById(R.id.chart);
        ArrayList<BarDataSet> dataSet = null;
        valueSet1 = new ArrayList<BarEntry>();

        int i = 0;
        for (ParseObject s : mNumbers) {
            BarEntry v1e1 = new BarEntry(Integer.parseInt(s.getString("number")), i);
            valueSet1.add(v1e1);
            i++;
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Number");
        barDataSet1.setColor(Color.rgb(0, 155, 0));

        dataSet = new ArrayList<BarDataSet>();
        dataSet.add(barDataSet1);

        BarData data = new BarData(getXAxisValues(mNumbers), dataSet);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable dragging and scaling
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // enable pinch to zoom
        chart.setPinchZoom(true);

        chart.setData(data);

        // enable the way the chart know the values has changed
        chart.notifyDataSetChanged();

        // limit no of visible entries
        chart.setVisibleXRange(6);

        // scroll to last entry
        chart.moveViewToX(data.getXValCount() - 7);

        chart.setDescription("My Chart");
        chart.animateXY(800, 800);
        chart.invalidate();
    }
}
