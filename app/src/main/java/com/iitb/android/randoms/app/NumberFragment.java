package com.iitb.android.randoms.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import java.sql.SQLException;
import java.util.List;

public class NumberFragment extends Fragment implements View.OnClickListener{
    private NumbersDataSource datasource;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_numbers, container, false);

        try {
            initializeDB(getActivity());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Button stopServiceButton = (Button) rootView.findViewById(R.id.stopButton);
        stopServiceButton.setOnClickListener(this);
        Button startServiceButton = (Button) rootView.findViewById(R.id.startButton);
        startServiceButton.setOnClickListener(this);

        parseEnableDataStore();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stopButton:
                Log.d("printed", "printed");
                getActivity().stopService(new Intent(getActivity().getBaseContext(), TimerService.class));
                try {
                    printValuesInLog();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.startButton:
                Log.d("start", "started");
                getActivity().registerReceiver(broadcastReceiver, new IntentFilter("RANDOM_NUMBER_INTENT"));
                getActivity().startService(new Intent(getActivity().getBaseContext(), TimerService.class));
                break;
            default:
                Log.d("default", "default");
        }
    }

    private void printValuesInLog() throws SQLException {
        List<RandomNumbers> values = datasource.getAllNumbers();
        for (RandomNumbers num : values) {
            Log.d("sql numbers", String.valueOf(num.getNumber()));
        }
    }

    private void initializeDB(Context context) throws SQLException {
        datasource = new NumbersDataSource(context);
        datasource.open();
    }

    public void parseEnableDataStore() {
        Parse.enableLocalDatastore(getActivity());
        Parse.initialize(getActivity(), "tLeY7yRq7L8cP7tzG70uzP2WWrcCjyPGXukxEsic", "LBQwZvoLOxUmG67FrxsN7chSfZmuVConrSZtlsqC");
    }

    public void parseStore(String number) {
        ParseObject randomNumbers = new ParseObject("RandomNumbers");
        randomNumbers.put("number", number);
        randomNumbers.saveInBackground();
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getExtras().getString("RESULT");
            TextView textView = (TextView) getView().findViewById(R.id.random_number_text);
            textView.setText(result);
            datasource.createComment(result);
            parseStore(result);
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        datasource.close();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(new Intent(getActivity().getBaseContext(), TimerService.class));
    }

    @Override
    public void onResume(){
        try {
            datasource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        datasource.close();
        super.onPause();
    }
}
