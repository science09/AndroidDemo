package com.science09.apple.gesturelock;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mepa on 16-1-8.
 * 手势触摸操作
 */
@SuppressWarnings("unused")
public class GestureRfView extends View {
    private static final String TAG = "GestureRfView";
    private float width = 0;
    private float height = 0;
    private boolean isCache = false;
    private String[] mText = {"REF", "RFin", "RFout", "TRA",
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "11", "12"};
    public enum PointId {POINT_REF, POINT_RFIN, POINT_RFOUT, POINT_TRA, POINT_1, POINT_2, POINT_3,
        POINT_4,POINT_5,POINT_6,POINT_7,POINT_8,POINT_9,POINT_10,POINT_11,POINT_12};
    private Paint mPaint;
    private Point[] mPanelPoints;
    private float dotRadius = 0;
    private List<Point> selectPoints = new ArrayList<>();
    private boolean checking = false;
    private long CLEAR_TIME = 1000;
    private int pwdMaxLen = 9;
    private int pwdMinLen = 4;
    private boolean isTouch = true;
    private Paint arrowPaint;
    private Paint linePaint;
    private Paint selectedPaint;
    private Paint errorPaint;
    private Paint normalPaint;
    private Paint mFontPaint;
    private int errorColor = 0xffea0945;
    private int selectedColor = 0xff0596f6;
    private int outterSelectedColor = 0xff8cbad8;
    private int outterErrorColor = 0xff901032;
    /**
     * 四个颜色，可由用户自定义，初始化时由xml文件传入传入
     */
    private int mColorNoFingerInner = 0xEE2B48ED;
    private int mColorNoFingerOutter = 0xDDAAAAAA; //0xDD3DF121;
    private int mColorFingerOn = 0xDD3DF121; //0xFFE11D55;
    private int mColorFingerUp = 0xFFFF0000;

    private Point currentPoint;
    private float currentRadius;
    private float currentInnerRaidus;
    private float startR = 0;
    private Point pointDown;
    private Point pointUp;
    private Point lastPoint;

    public GestureRfView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GestureRfView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureRfView(Context context) {
        this(context, null, 0);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isCache) {
            initData();
            startAnimation();
        }
        drawToCanvas(canvas);
    }

    private void drawToCanvas(Canvas canvas) {
        float mFontWidth;
        int i;
        for (Point p : mPanelPoints) {
            switch (p.state) {
                case STATUS_NO_FINGER:
                    // 绘制外圆
                    normalPaint.setStyle(Paint.Style.FILL);
                    normalPaint.setColor(mColorNoFingerOutter);
                    canvas.drawCircle(p.getX(), p.getY(), currentRadius, normalPaint);
                    // 绘制内圆
                    normalPaint.setColor(mColorNoFingerInner);
                    canvas.drawCircle(p.getX(), p.getY(), currentInnerRaidus, normalPaint);
                    // 绘制文字
                    if (p.index < 4) {
                        mFontPaint.setTextSize(20);
                    } else {
                        mFontPaint.setTextSize(30);
                    }
                    mFontWidth = mFontPaint.measureText(mText[p.index]) / 2;
                    canvas.drawText(mText[p.index], p.getX() - mFontWidth, p.getY() + 10, mFontPaint);
//                    if(currentPoint == null){
//                        currentPoint = mPanelPoints[0];
//                        normalPaint.setStyle(Paint.Style.FILL);
//                        normalPaint.setColor(mColorNoFingerOutter);
//                        canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), dotRadius, normalPaint);
//                        normalPaint.setColor(mColorNoFingerInner);
//                        canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), dotRadius * 3 / 4, normalPaint);
//                        if (currentPoint.index < 4) {
//                            mFontPaint.setTextSize(20);
//                        } else {
//                            mFontPaint.setTextSize(30);
//                        }
//                        mFontWidth = mFontPaint.measureText(mText[currentPoint.index]) / 2;
//                        canvas.drawText(mText[currentPoint.index], currentPoint.getX() - mFontWidth, currentPoint.getY() + 10, mFontPaint);
//                        startAnimation();
//                    } else {
//                        normalPaint.setStyle(Paint.Style.FILL);
//                        normalPaint.setColor(mColorNoFingerOutter);
//                        canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), dotRadius, normalPaint);
//                        normalPaint.setColor(mColorNoFingerInner);
//                        canvas.drawCircle(currentPoint.getX(), currentPoint.getY(), dotRadius * 3 / 4, normalPaint);
//                        if (currentPoint.index < 4) {
//                            mFontPaint.setTextSize(20);
//                        } else {
//                            mFontPaint.setTextSize(30);
//                        }
//                        mFontWidth = mFontPaint.measureText(mText[currentPoint.index]) / 2;
//                        canvas.drawText(mText[currentPoint.index], currentPoint.getX() - mFontWidth, currentPoint.getY() + 10, mFontPaint);
//                    }
                    break;
                case STATUS_FINGER_ON:
                    // 绘制外圆
                    selectedPaint.setStyle(Paint.Style.FILL);
                    selectedPaint.setColor((mColorFingerOn));
                    canvas.drawCircle(p.getX(), p.getY(), dotRadius, selectedPaint);
                    // 绘制内圆
                    selectedPaint.setColor(mColorNoFingerInner);
                    canvas.drawCircle(p.getX(), p.getY(), dotRadius * 3 / 4, selectedPaint);
                    // 绘制文字
                    if (p.index < 4) {
                        mFontPaint.setTextSize(20);
                    } else {
                        mFontPaint.setTextSize(30);
                    }
                    mFontWidth = mFontPaint.measureText(mText[p.index]) / 2;
                    canvas.drawText(mText[p.index], p.getX() - mFontWidth, p.getY() + 10, mFontPaint);
                    break;
                case STATUS_FINGER_UP:
                    // 绘制外圆
                    normalPaint.setStyle(Paint.Style.FILL);
                    normalPaint.setColor(mColorFingerUp);
                    canvas.drawCircle(p.getX(), p.getY(), dotRadius, normalPaint);
                    // 绘制内圆
                    normalPaint.setColor(mColorNoFingerOutter);
                    canvas.drawCircle(p.getX(), p.getY(), dotRadius * 3 / 4, normalPaint);
                    // 绘制文字
                    if (p.index < 4) {
                        mFontPaint.setTextSize(20);
                    } else {
                        mFontPaint.setTextSize(30);
                    }
                    mFontWidth = mFontPaint.measureText(mText[p.index]) / 2;
                    canvas.drawText(mText[p.index], p.getX() - mFontWidth, p.getY() + 10, mFontPaint);
                    break;
            }
        }
        if (selectPoints.size() > 0) {
            int tmpAlpha = mPaint.getAlpha();
            Point tp = selectPoints.get(0);
            for (i = 1; i < selectPoints.size(); i++) {
                Point p = selectPoints.get(i);
                if(i != 2 && i!= 4){
                    drawLine(tp, p, canvas, linePaint);
                    drawArrow(canvas, arrowPaint, tp, p, dotRadius / 4, 38);
                }
                tp = p;
            }
            if (this.movingNoPoint) {
                drawLine(tp, new Point(moveingX, moveingY, -1), canvas, linePaint);
            }
            mPaint.setAlpha(tmpAlpha);
        }
    }

    private void drawLine(Point start, Point end, Canvas canvas, Paint paint) {
        double d = MathUtil.distance(start.getX(), start.getY(), end.getX(), end.getY());
        float rx = (float) ((end.getX() - start.getX()) * dotRadius / d); //计算两个圆点之间的偏移量
        float ry = (float) ((end.getY() - start.getY()) * dotRadius / d); //计算两个圆点之间的偏移量
        canvas.drawLine(start.getX() + rx, start.getY() + ry, end.getX() - rx, end.getY() - ry, paint);
    }

    private void drawArrow(Canvas canvas, Paint paint, Point start, Point end, float arrowHeight, int angle) {
        double d = MathUtil.distance(start.getX(), start.getY(), end.getX(), end.getY());
        float sin_B = (float) ((end.getX() - start.getX()) / d);
        float cos_B = (float) ((end.getY() - start.getY()) / d);
        float tan_A = (float) Math.tan(Math.toRadians(angle));
        float h = (float) (d - arrowHeight - dotRadius * 1.1);
        float l = arrowHeight * tan_A;
        float a = l * sin_B;
        float b = l * cos_B;
        float x0 = h * sin_B;
        float y0 = h * cos_B;
        float x1 = start.getX() + (h + arrowHeight) * sin_B;
        float y1 = start.getY() + (h + arrowHeight) * cos_B;
        float x2 = start.getX() + x0 - b;
        float y2 = start.getY() + y0 + a;
        float x3 = start.getX() + x0 + b;
        float y3 = start.getY() + y0 - a;
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void initData() {
        width = this.getWidth();
        height = this.getHeight();
        mPanelPoints = new Point[16]; //初始化 16 个点
        float mPointBetween = width / 6;
        float mPointYBetween = height / 3;
        float mDotX = mPointBetween / 2;
        float mDotY = mPointYBetween / 2;
        for (int i = 0; i < 16; i++) {
            if (i < 4) {
                mPanelPoints[i] = new Point(mDotX + (i + 1) * mPointBetween, mDotY, i);
            } else {
                mPanelPoints[i] = new Point(mDotX + ((i - 4) % 6) * mPointBetween, mDotY + ((i - 4) / 6 + 1) * mPointYBetween, i);
            }
        }
        Log.d(TAG, "canvas width:" + width);
        dotRadius = mDotX * 7 / 10;
        isCache = true;
        initPaints();
    }

    private void initPaints() {
        arrowPaint = new Paint();
        arrowPaint.setColor(selectedColor);
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setAntiAlias(true);
        linePaint = new Paint();
        linePaint.setColor(selectedColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(dotRadius / 4);
        selectedPaint = new Paint();
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setAntiAlias(true);
        selectedPaint.setStrokeWidth(dotRadius / 6);
        errorPaint = new Paint();
        errorPaint.setStyle(Paint.Style.STROKE);
        errorPaint.setAntiAlias(true);
        errorPaint.setStrokeWidth(dotRadius / 6);
        normalPaint = new Paint();
        normalPaint.setStyle(Paint.Style.STROKE);
        normalPaint.setAntiAlias(true);
        normalPaint.setStrokeWidth(dotRadius / 9);
        mFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFontPaint.setColor(Color.WHITE);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * @param x 轴的坐标点
     * @param y 轴的坐标点
     * @return 满足这组坐标的点
     */
    private Point checkSelectPoint(float x, float y) {
        for (Point p : mPanelPoints) {
            if(MathUtil.checkInRound(p.getX(), p.getY(), dotRadius, (int)x, (int)y)){
                return p;
            }
        }
        return null;
    }

    /**
     * 清空所有点的状态
     */
    public void reset() {
        for (Point p : mPanelPoints) {
            p.state = Point.Mode.STATUS_NO_FINGER;
        }
        selectPoints.clear();
        this.enableTouch();
        postInvalidate();
    }

    /**
     * @param p 传入坐标点
     * @return 返回
     */
    private int crossPoint(Point p) {
        if (selectPoints.contains(p)) {
            if (selectPoints.size() > 2) {
                if (selectPoints.get(selectPoints.size() - 1).index != p.index) {
                    return 2;
                }
            }
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @param point 传入被触摸到的点的坐标
     */
    private void addPoint(Point point) {
        selectPoints.add(point);
    }

    boolean movingNoPoint = false;
    float moveingX, moveingY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouch) {
            return false;
        }
        movingNoPoint = false;
        float ex = event.getX();
        float ey = event.getY();
        boolean isFinish = false;
        Point p = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointDown = checkSelectPoint(ex, ey);
                Log.d(TAG, "ACTION_DOWN****8****");
                if (p != null) {
                    checking = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (checking) {
                    p = checkSelectPoint(ex, ey);
                    if (p == null) {
                        movingNoPoint = true;
                        moveingX = ex;
                        moveingY = ey;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                pointUp = checkSelectPoint(ex, ey);
                Log.d(TAG, "Action Up======");
                checking = false;
                isFinish = true;
                break;
        }
        if(isFinish && (pointUp != null)){
            if(pointDown == pointUp){
                if(lastPoint == pointUp){
                    Log.d(TAG, "同一个点");
                    pointUp.state = Point.Mode.STATUS_NO_FINGER;
                    selectPoints.remove(pointUp);
                    lastPoint = null;
                } else {


                    if (selectPoints.size() == 0) {
                        if (pointUp.index != 1 && pointUp.index != 2) {
                            pointUp.state = Point.Mode.STATUS_FINGER_UP;
                            addPoint(pointUp);
                        }
                    } else if (selectPoints.size() == 1) {
                        if (!selectPoints.contains(pointUp)) {
                            if (selectPoints.get(0).index > 3) {
                                if (pointUp.index == 0 || pointUp.index == 3) {
                                    pointUp.state = Point.Mode.STATUS_FINGER_UP;
                                    addPoint(pointUp);
                                }
                            } else if (selectPoints.get(0).index == 0) {
                                if (pointUp.index > 3) {
                                    pointUp.state = Point.Mode.STATUS_FINGER_UP;
                                    addPoint(pointUp);
                                }
                            } else if (selectPoints.get(0).index == 3) {
                                if (pointUp.index > 3) {
                                    pointUp.state = Point.Mode.STATUS_FINGER_UP;
                                    addPoint(pointUp);
                                }
                            }
                        }
                    } else if (selectPoints.size() == 2) {
                        if (!selectPoints.contains(pointUp)) {
                            if (pointUp.index > 3) {
                                pointUp.state = Point.Mode.STATUS_FINGER_UP;
                                addPoint(pointUp);
                            } else if (pointUp.index == 3) {
                                pointUp.state = Point.Mode.STATUS_FINGER_UP;
                                addPoint(pointUp);
                            } else if (pointUp.index == 0) {
                                pointUp.state = Point.Mode.STATUS_FINGER_UP;
                                addPoint(pointUp);
                            }
                        }
                    } else if (selectPoints.size() == 3) {
                        if (selectPoints.contains(pointUp)) {
                            pointDown = selectPoints.get(2);
                            reset();
                            pointDown.state = Point.Mode.STATUS_FINGER_UP;
                            pointUp.state = Point.Mode.STATUS_FINGER_UP;
                            selectPoints.add(pointUp);
                            selectPoints.add(pointDown);
                        } else {
                            if (selectPoints.get(2).index > 3) {
                                if (pointUp.index == 0 || pointUp.index == 3) {
                                    pointUp.state = Point.Mode.STATUS_FINGER_UP;
                                    addPoint(pointUp);
                                }
                            } else if (selectPoints.get(2).index == 0 || selectPoints.get(2).index == 3) {
                                if (pointUp.index > 3) {
                                    pointUp.state = Point.Mode.STATUS_FINGER_UP;
                                    addPoint(pointUp);
                                }
                            }
                        }
                    } else if (selectPoints.size() == 4) {
                        if (!selectPoints.contains(pointUp) && pointUp.index > 3) {
                            pointUp.state = Point.Mode.STATUS_FINGER_UP;
                            addPoint(pointUp);
                        }
                    } else if (selectPoints.size() == 5) {
                        int id = selectPoints.indexOf(pointUp);
                        if (id == 0 || id == 1) {
                            //清掉前面两个点
                            selectPoints.get(0).state = Point.Mode.STATUS_NO_FINGER;
                            selectPoints.get(1).state = Point.Mode.STATUS_NO_FINGER;
                            selectPoints.remove(0);
                            selectPoints.remove(0);
                            pointUp.state = Point.Mode.STATUS_FINGER_UP;
                            selectPoints.add(pointUp);
                            invalidate();
                        } else if (id == 2 || id == 3) {
                            //清掉后面两个点
                            for (Point pt : selectPoints) {
                                Log.d(TAG, "selectID:" + pt.index);
                            }
                            selectPoints.get(2).state = Point.Mode.STATUS_NO_FINGER;
                            selectPoints.get(3).state = Point.Mode.STATUS_NO_FINGER;
                            selectPoints.remove(2);
                            selectPoints.remove(2);
                            Log.d(TAG, "ListSize:" + selectPoints.size());
                            for (Point pt : selectPoints) {
                                Log.d(TAG, "selectID:" + pt.index);
                            }
                            pointUp.state = Point.Mode.STATUS_FINGER_UP;
                            selectPoints.add(pointUp);
                        }
                        Log.d(TAG, "id:===" + id);
                        if (pointUp.index == 0) {

                        } else if (pointUp.index == 3) {

                        }
                    }
                    Log.d(TAG, "点数" + selectPoints.size());
                    for (Point pt : selectPoints) {
                        Log.d(TAG, "selectID:" + pt.index);
                    }
                    lastPoint = pointUp;
                }
            }
        }

        this.postInvalidate();
        return true;
    }

    public void enableTouch() {
        isTouch = true;
    }

    public void disableTouch() {
        isTouch = false;
    }

    private Timer timer = new Timer();
    private TimerTask task = null;

    public void clearPassword() {
        clearPassword(CLEAR_TIME);
    }

    public void clearPassword(final long time) {
        if (time > 1) {
            if (task != null) {
                task.cancel();
                Log.d("task", "clearPassword cancel()");
            }
            postInvalidate();
            task = new TimerTask() {
                public void run() {
                    reset();
                    postInvalidate();
                }
            };
            Log.d("task", "clearPassword schedule(" + time + ")");
            timer.schedule(task, time);
        } else {
            reset();
            postInvalidate();
        }
    }

    // 绘制完成的回调函数
    private OnCompleteListener mCompleteListener;

    /**
     * @param mCompleteListener 设置一个回调函数
     */
    public void setOnCompleteListener(OnCompleteListener mCompleteListener) {
        this.mCompleteListener = mCompleteListener;
    }

    public interface OnCompleteListener {
        void onComplete(String password);
    }

    public void JoinLine(int fromId, int toId){
        for(Point p : mPanelPoints){
            if(p.index == fromId){
                p.state = Point.Mode.STATUS_FINGER_ON;
                selectPoints.add(p);
            }
            if(p.index == toId){
                p.state = Point.Mode.STATUS_FINGER_ON;
                selectPoints.add(p);
            }
        }
        invalidate();
    }

//    private void startAnimation(){
//        Point startPoint = mPanelPoints[7];
//        Point endPoint = mPanelPoints[0];
//        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                currentPoint = (Point) animation.getAnimatedValue();
//                Log.d(TAG, "AnimationUpdate....");
//                postInvalidate(); //invalidate();
//            }
//        });
//        anim.setDuration(1000);
//        anim.start();
//    }

    private void startAnimation(){
        ValueAnimator OuterAnim = ValueAnimator.ofFloat(startR, dotRadius);
        OuterAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentRadius = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        OuterAnim.setDuration(1000);
        OuterAnim.start();
        ValueAnimator InnerAnim = ValueAnimator.ofFloat(startR, dotRadius * 3 / 4);
        InnerAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentInnerRaidus = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        InnerAnim.setDuration(1500);
        InnerAnim.start();
    }

    public class SelectPointPair {
        public int pointRefOrTra;
        public int point1To12;
    }

    public class PointEvaluator implements TypeEvaluator{

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
            float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
            Point point = new Point(x, y, endPoint.index);
            return point;
        }
    }

    public class SpreadEvaluator implements TypeEvaluator{

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            float startRadius = (float) startValue;
            float endRadius = (float) endValue;
            float radius = startRadius + fraction * (endRadius - startRadius);
            return radius;
        }
    }
}
