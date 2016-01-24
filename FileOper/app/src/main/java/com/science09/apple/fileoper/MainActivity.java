package com.science09.apple.fileoper;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private EditText mEditText;
    private TextView mTextView;

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

        mEditText = (EditText) findViewById(R.id.id_edit);
        mTextView = (TextView) findViewById(R.id.id_show);
        findViewById(R.id.id_readBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputStream is = getResources().getAssets().open("myFile.txt");
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bfr = new BufferedReader(isr);
                    String str = "";
                    while ((str = bfr.readLine()) != null) {
                        Log.d("MainActivity", str);
                    }
                    bfr.close();
                    isr.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.d(TAG, getFilesDir().getAbsolutePath());

        findViewById(R.id.id_writeStorage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File sdcard = Environment.getExternalStorageDirectory();
                File myFile = new File(sdcard, "myfile.txt");
                if (sdcard.exists()) {
                    try {
                        myFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(myFile);
                        OutputStreamWriter osw = new OutputStreamWriter(fos);
                        osw.write(mEditText.getText().toString());
                        osw.flush();
                        osw.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "写入文件成功!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "当前系统没有SD卡!", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, sdcard.getName() + "==" + sdcard.getAbsolutePath().toString());
            }
        });

        findViewById(R.id.id_readStroage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File myFile = new File(Environment.getExternalStorageDirectory(), "myfile.txt");
                if(myFile.exists()){
                    try {
                        FileInputStream fis = new FileInputStream(myFile);
                        InputStreamReader isr = new InputStreamReader(fis);
                        char[] input = new char[fis.available()];
                        isr.read(input);
                        isr.close();
                        fis.close();
                        String str = new String(input);
                        mTextView.setText(str);
                        Toast.makeText(getApplicationContext(), "文件读取成功!", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateManager um = new UpdateManager(MainActivity.this);
                um.checkUpdate();
            }
        });
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
}
