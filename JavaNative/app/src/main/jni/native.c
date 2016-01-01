//
// Created by apple on 15/12/30.
//

#include "com_science09_apple_demo_JavaNative.h"

JNIEXPORT jstring JNICALL Java_com_science09_apple_demo_JavaNative_getStringFromNative(JNIEnv *env, jobject obj)
{
    return (*env)->NewStringUTF(env,"Hello JNI, I'am From Native");
}