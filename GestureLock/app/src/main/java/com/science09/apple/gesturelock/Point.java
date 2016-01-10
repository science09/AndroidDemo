package com.science09.apple.gesturelock;

/**
 * Created by apple on 16/1/8.
 */
public class Point {
    enum Mode {STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP};
    public float x;
    public float y;
    public Mode state = Mode.STATUS_NO_FINGER;
    public int index = 0;

    public Point() {

    }

    public Point(float x, float y, int value) {
        this.x = x;
        this.y = y;
        index = value;
    }

    public int getColNum() {
        if (index < 4) {
            return index;
        } else {
            return (index - 4) % 6;
        }
    }

    public int getRowNum() {
        if (index < 4) {
            return 0;
        } else {
            return (index - 4) / 6 + 1;
        }
    }
}
