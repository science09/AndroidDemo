package com.science09.apple.gesturelock;

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
 */
public class GestureRfView extends View {
    private static final String TAG = "GestureRfView";
    private float width = 0;
    private float height = 0;
    private boolean isCache = false;
    private String[] mText = {"REF", "RFin", "RFout", "TRA",
            "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "10", "11", "12"};
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Point[] mPanelPoints;
    private float dotRadius = 0;
    private List<Point> selectPoints = new ArrayList<>();
    private boolean checking = false;
    private long CLEAR_TIME = 1000;
    private int pwdMaxLen = 9;
    private int pwdMinLen = 4;
    private boolean isTouch = true;
    private int mRow = 3; //行数
    private int mCol = 6; //列数
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
    private int mColorNoFingerOutter = 0xDD3DF121;
    private int mColorFingerOn = 0xFFE11D55;
    private int mColorFingerUp = 0xFFFF0000;

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
        }
        drawToCanvas(canvas);
    }

    private void drawToCanvas(Canvas canvas) {
        float mFontWidth;
        boolean inErrorState = false;
        int i;
        for (Point p : mPanelPoints) {
            switch (p.state) {
                case STATUS_NO_FINGER:
                    // 绘制外圆
                    normalPaint.setStyle(Paint.Style.FILL);
                    normalPaint.setColor(mColorNoFingerOutter);
                    canvas.drawCircle(p.x, p.y, dotRadius, normalPaint);
                    // 绘制内圆
                    normalPaint.setColor(mColorNoFingerInner);
                    canvas.drawCircle(p.x, p.y, dotRadius * 3 / 4, normalPaint);
                    // 绘制文字
                    if (p.index < 4) {
                        mFontPaint.setTextSize(20);
                    } else {
                        mFontPaint.setTextSize(30);
                    }
                    mFontWidth = mFontPaint.measureText(mText[p.index]) / 2;
                    canvas.drawText(mText[p.index], p.x - mFontWidth, p.y + 10, mFontPaint);
                    break;
                case STATUS_FINGER_ON:
                    // 绘制外圆
                    selectedPaint.setStyle(Paint.Style.FILL);
                    selectedPaint.setColor((mColorFingerOn));
                    canvas.drawCircle(p.x, p.y, dotRadius, selectedPaint);
                    // 绘制内圆
                    selectedPaint.setColor(mColorNoFingerInner);
                    canvas.drawCircle(p.x, p.y, dotRadius * 3 / 4, selectedPaint);
                    // 绘制文字
                    if (p.index < 4) {
                        mFontPaint.setTextSize(20);
                    } else {
                        mFontPaint.setTextSize(30);
                    }
                    mFontWidth = mFontPaint.measureText(mText[p.index]) / 2;
                    canvas.drawText(mText[p.index], p.x - mFontWidth, p.y + 10, mFontPaint);
                    break;
                case STATUS_FINGER_UP:
                    inErrorState = true;
                    // 绘制外圆
                    normalPaint.setStyle(Paint.Style.FILL);
                    normalPaint.setColor(mColorFingerUp);
                    canvas.drawCircle(p.x, p.y, dotRadius, normalPaint);
                    // 绘制内圆
                    normalPaint.setColor(mColorNoFingerOutter);
                    canvas.drawCircle(p.x, p.y, dotRadius * 3 / 4, normalPaint);
                    // 绘制文字
                    if (p.index < 4) {
                        mFontPaint.setTextSize(20);
                    } else {
                        mFontPaint.setTextSize(30);
                    }
                    mFontWidth = mFontPaint.measureText(mText[p.index]) / 2;
                    canvas.drawText(mText[p.index], p.x - mFontWidth, p.y + 10, mFontPaint);
                    break;
            }
        }
        if (inErrorState) {
            arrowPaint.setColor(errorColor);
            linePaint.setColor(errorColor);
        } else {
            arrowPaint.setColor(selectedColor);
            linePaint.setColor(selectedColor);
        }
        if (selectPoints.size() > 0) {
            int tmpAlpha = mPaint.getAlpha();
            Point tp = selectPoints.get(0);
            for (i = 1; i < selectPoints.size(); i++) {
                Point p = selectPoints.get(i);
                if(i != 2){
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
        double d = MathUtil.distance(start.x, start.y, end.x, end.y);
        float rx = (float) ((end.x - start.x) * dotRadius / d); //计算两个圆点之间的偏移量
        float ry = (float) ((end.y - start.y) * dotRadius / d); //计算两个圆点之间的偏移量
        canvas.drawLine(start.x + rx, start.y + ry, end.x - rx, end.y - ry, paint);
    }

    private void drawArrow(Canvas canvas, Paint paint, Point start, Point end, float arrowHeight, int angle) {
        double d = MathUtil.distance(start.x, start.y, end.x, end.y);
        float sin_B = (float) ((end.x - start.x) / d);
        float cos_B = (float) ((end.y - start.y) / d);
        float tan_A = (float) Math.tan(Math.toRadians(angle));
        float h = (float) (d - arrowHeight - dotRadius * 1.1);
        float l = arrowHeight * tan_A;
        float a = l * sin_B;
        float b = l * cos_B;
        float x0 = h * sin_B;
        float y0 = h * cos_B;
        float x1 = start.x + (h + arrowHeight) * sin_B;
        float y1 = start.y + (h + arrowHeight) * cos_B;
        float x2 = start.x + x0 - b;
        float y2 = start.y + y0 + a;
        float x3 = start.x + x0 + b;
        float y3 = start.y + y0 - a;
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
    }

    /**
     * @param x 轴的坐标点
     * @param y 轴的坐标点
     * @return 满足这组坐标的点
     */
    private Point checkSelectPoint(float x, float y) {
        for (Point p : mPanelPoints) {
            if(MathUtil.checkInRound(p.x, p.y, dotRadius, (int)x, (int)y)){
                return p;
            }
        }
        return null;
    }

    /**
     * 清空所有点的状态
     */
    private void reset() {
        for (Point p : selectPoints) {
            p.state = Point.Mode.STATUS_NO_FINGER;
        }
        selectPoints.clear();
        this.enableTouch();
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

    /**
     * @return 转换为字符串
     */
    private String toPointString() {
        if (selectPoints.size() >= pwdMinLen && selectPoints.size() <= pwdMaxLen) {
            StringBuffer sf = new StringBuffer();
            for (Point p : selectPoints) {
                sf.append(p.index);
            }
            return sf.toString();
        } else {
            return "";
        }
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
        boolean redraw = false;
        Point p = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(selectPoints.size() == 4 || selectPoints.size() > 4){
                    reset();
                }
                p = checkSelectPoint(ex, ey);
                Log.d(TAG, "ACTION_DOWN****8****");
                if (p != null) {
                    checking = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE======");
                if (checking) {
                    Log.d(TAG, "ACTION_MOVE=+++++++++");
                    p = checkSelectPoint(ex, ey);
                    if (p == null) {
                        movingNoPoint = true;
                        moveingX = ex;
                        moveingY = ey;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                p = checkSelectPoint(ex, ey);
                Log.d(TAG, "Action Up======");
                checking = false;
                isFinish = true;
                break;
        }
        if (!isFinish && checking && p != null) {
            int rk = crossPoint(p);
            if (rk == 2) {
                movingNoPoint = true;
                moveingX = ex;
                moveingY = ey;
            } else if (rk == 0) {
                p.state = Point.Mode.STATUS_FINGER_ON;
                addPoint(p);
            }
        }
        if (isFinish) {
            if(!isValid((ArrayList<Point>)selectPoints)){
                reset();
                if(selectPoints.size()>0){
                    selectPoints.remove(selectPoints.size()-1);
                }
            }
            // 判断所点击的是否符合规则
            if(selectPoints.size() == 1) {
                if(selectPoints.get(0).index != 0 && selectPoints.get(0).index != 3){
                    reset();
                }
            }
//            if (selectPoints.size() < pwdMinLen || selectPoints.size() > pwdMaxLen) {
//                // mCompleteListener.onPasswordTooMin(selectPoints.size());
//                error();
//                //clearPassword();
//            } else if (mCompleteListener != null) {
//                this.disableTouch();
//                mCompleteListener.onComplete(toPointString());
//            }
        }

        this.postInvalidate();
        return true;
    }

    /**
     * 判断所选择的触摸点是否符合规则
     * @param mList 触摸屏所选择的点的列表
     * @return true:满足矩阵开关的点, false:不满足要求
     */
    private boolean isValid(ArrayList<Point> mList) {
        if(mList.contains(mPanelPoints[1]) || mList.contains(mPanelPoints[2])){
            return false;
        }
        switch (mList.size()){
            case 0:
            case 1:
                return true;
            case 2:
                if(mList.contains(mPanelPoints[0]) && mList.contains(mPanelPoints[3])){
                    return false;
                }
                return true;
            case 3:
                return true;
            case 4:
                if(mList.contains(mPanelPoints[0]) && mList.contains(mPanelPoints[3])){
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     *
     */
    private void error() {
        for (Point p : selectPoints) {
            p.state = Point.Mode.STATUS_FINGER_UP;
        }
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
}
