package com.science09.apple.gesturelock;

/**
 * Created by apple on 16/1/8.
 */
public class Point {
    enum Mode {STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP};
    private float x;
    private float y;
    public Mode state = Mode.STATUS_NO_FINGER;
    public int index = 0;

    public Point() {

    }

    public Point(float x, float y, int value) {
        this.x = x;
        this.y = y;
        index = value;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
