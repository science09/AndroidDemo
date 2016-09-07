package com.science09.apple.temperatureview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TemperatureView mTemperatureView;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTemperatureView = (TemperatureView) findViewById(R.id.temperature_view);

        mBtn = (Button) findViewById(R.id.btnStart);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new TempRun(mTemperatureView)).start();
            }
        });

    }

    class TempRun implements Runnable {
        private TemperatureView mView;

        public TempRun(TemperatureView view) {
            mView = view;
        }

        @Override
        public void run() {
            for (float i = 0; i <= 40; i++) {
                mView.setCurrentTemp(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
