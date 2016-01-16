package com.science09.apple.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private TextView mTextView2;
    private JavaNative mJavaNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv);
        mJavaNative = new JavaNative();
        mTextView.setText(mJavaNative.getStringFromNative());
        mTextView2 = (TextView) findViewById(R.id.tv2);
        mTextView2.setText(mJavaNative.getInt()+"dd" + mJavaNative.setId(55));
    }
}
