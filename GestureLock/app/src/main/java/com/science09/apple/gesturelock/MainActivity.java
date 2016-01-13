package com.science09.apple.gesturelock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private GestureLockViewGroup mGestureLockViewGroup;
    private GestureRfView mGestureView;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
//        mGestureLockViewGroup.setAnswer(new int[] { 1, 2, 3, 4,5 });
//        mGestureLockViewGroup
//                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {
//
//                    @Override
//                    public void onUnmatchedExceedBoundary() {
//                        Toast.makeText(MainActivity.this, "错误5次...",
//                                Toast.LENGTH_SHORT).show();
//                        mGestureLockViewGroup.setUnMatchExceedBoundary(5);
//                    }
//
//                    @Override
//                    public void onGestureEvent(boolean matched) {
//                        Toast.makeText(MainActivity.this, matched + "",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onBlockSelected(int cId) {
//                    }
//                });

        mGestureView = (GestureRfView) findViewById(R.id.id_GestureView);
        mBtn = (Button) findViewById(R.id.id_btn);
        mBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn:
                mGestureView.reset();
                break;
            default:
                break;
        }
    }
}
