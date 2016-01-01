package com.science09.apple.demo;

/**
 * Created by apple on 15/12/30.
 */
public class JavaNative {

    static {
        System.loadLibrary("JavaNative");
    }


    public native String getStringFromNative();
}

