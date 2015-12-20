package com.science09.apple.clockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by apple on 15/12/16.
 */
public class FanView extends View {
    private final String TAG = "FanView";
    private float mWidth = 300;
    private float mHeight = 300;
    private float width_circle = 20;

    public FanView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FanView(Context context) {
        super(context);
    }

    public FanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.mWidth = Math.min(getWidth(), getHeight());
        this.mHeight = Math.max(getWidth(), getHeight());
        Log.w(TAG, "onDraw: " + mWidth + "," + mHeight);
        Paint paintCircle = new Paint();
        paintCircle.setColor(Color.GRAY);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeWidth(width_circle);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - width_circle, paintCircle);

        Paint paintDot = new Paint();
        paintDot.setColor(Color.MAGENTA);
        canvas.drawCircle(mWidth/2, mHeight/2, 20, paintDot);

        Paint paintFan = new Paint();
        paintFan.setColor(Color.BLUE);

    }
}
