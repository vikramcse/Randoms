package com.iitb.android.randoms.app;

import android.app.IntentService;import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class TimerService extends Service {

    public static final long NOTIFY_INTERVAL = 5 * 1000; // 5 seconds
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.schedule(doAsynchronousTask, 0, NOTIFY_INTERVAL);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
    }

    TimerTask doAsynchronousTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @SuppressWarnings("unchecked")
                public void run() {
                    try {
                        int random = generateRandomNumber();
                        publishResults(String.valueOf(random));
                        Log.d("Random numbers", random + "");
                    }
                    catch (Exception e) {
                        Log.e("Error at timer catched", e.getMessage());
                    }
                }
            });
        }
    };

    private void publishResults(String result) {
        intent = new Intent("RANDOM_NUMBER_INTENT");
        intent.putExtra("RESULT", result);
        sendBroadcast(intent);
    }

    public int generateRandomNumber() {
        int min = 1;
        int max = 100;
        Random r = new Random();
        int number = r.nextInt(max - min + 1) + min;
        return number;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }


}
