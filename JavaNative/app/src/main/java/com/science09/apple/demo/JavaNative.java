package com.science09.apple.demo;

/**
 * Created by apple on 15/12/30.
 */
public class JavaNative {

    static {
        try {
            System.loadLibrary("JavaNative");
        }catch (UnsatisfiedLinkError err){
            System.err.println("Error: Could not load library!");
        }
    }


    public native int getInt();
    public native int setId(int id);

    public native String getStringFromNative();
}

