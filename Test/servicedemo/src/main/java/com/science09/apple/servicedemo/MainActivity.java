package com.science09.apple.servicedemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String TIME_CHANGED_ACTION = "com.science09.TIME_CHANGED_ACTION";

    private TextView mtime;
    private Intent timeService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btn1 = (Button) findViewById(R.id.id_btn1);
        Button btn2 = (Button) findViewById(R.id.id_btn2);
        Button btn3 = (Button) findViewById(R.id.id_btn3);
        Button btn4 = (Button) findViewById(R.id.id_btn4);
        mtime = (TextView) findViewById(R.id.time);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        Intent i = new Intent(this, LongRunningService.class);
        startService(i);

        registerBroadcastReceiver();
        startTimeService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ServiceDemo", "ServiceDemo onDestroy");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MyService.class);
        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("serviceDemo", "ServiceConnection onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("ServiceDemo", "ServiceConnection onServiceDisconnected");
            }
        };
        switch (v.getId()){
            case R.id.id_btn1:
                startService(intent);
                break;
            case R.id.id_btn2:
                stopService(intent);
                break;
            case R.id.id_btn3:
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
                break;
            case R.id.id_btn4:
                unbindService(conn);
                break;
        }
    }

    private void startTimeService(){
        timeService = new Intent(this, TimeService.class);
        startService(timeService);
    }

    private void registerBroadcastReceiver(){
        UITimerReceiver receiver = new UITimerReceiver();
        IntentFilter filter = new IntentFilter(TIME_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }

    public class UITimerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(MainActivity.TIME_CHANGED_ACTION.equals(action)){
                Bundle bundle = intent.getExtras();
                String strtime = bundle.getString("time");
                mtime.setText(strtime);
            }
        }
    }
}
