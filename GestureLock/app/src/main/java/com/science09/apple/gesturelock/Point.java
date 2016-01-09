package com.science09.apple.gesturelock;

/**
 * Created by apple on 16/1/8.
 */
public class Point {
    public static int STATE_NORMAL = 0;
    public static int STATE_CHECK = 1;
    public static int STATE_CHECK_ERROR = 2;

    public float x;
    public float y;
    public int state = 0;
    public int index = 0;

    public Point() {

    }
    public Point(float x, float y, int value) {
        this.x = x;
        this.y = y;
        index = value;
    }

    public int getColNum(){
        if(index<4){
            return index;
        }else {
            return (index-4)%6;
        }
//        return (index -1)%3;
    }

    public int getRowNum(){
        if(index < 4) {
            return 0;
        }else {
            return (index-4)/6+1;
        }
//        return (index-1)/3;
    }
 }
