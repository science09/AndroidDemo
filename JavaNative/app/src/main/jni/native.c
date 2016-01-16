//
// Created by apple on 15/12/30.
//
#include <stdio.h>
#include <jni.h>
#include "jni.h"
#include "android/log.h"
#include "com_science09_apple_demo_JavaNative.h"

#define TAG  "JAVA_NATIVE"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL, TAG, __VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)



JNIEXPORT jstring JNICALL Java_com_science09_apple_demo_JavaNative_getStringFromNative(JNIEnv *env, jobject obj)
{
    return (*env)->NewStringUTF(env,"Hello JNI, I'am From Native");
}

//JNIEXPORT jstring JNICALL native_getStringFromNative(JNIEnv *env, jobject obj)
//{
//    return (*env)->NewStringUTF(env,"Hello JNI, I'am From Native");
//}

JNIEXPORT jint JNICALL Java_com_science09_apple_demo_JavaNative_setId
        (JNIEnv *env, jobject obj, jint id) {
    LOGD("Native============= %d\n", id);
    LOGE("Native============= %d\n", id);
    LOGW("Native============= %d\n", id);
    LOGF("Native============= %d\n", id);
    LOGI("Native============= %d\n", id);
    return id;
}
//
//JNIEXPORT jint JNICALL native_setId
//        (JNIEnv *env, jobject obj, jint id) {
//    LOGD("Native============= %d\n", id);
//    LOGE("Native============= %d\n", id);
//    LOGW("Native============= %d\n", id);
//    LOGF("Native============= %d\n", id);
//    LOGI("Native============= %d\n", id);
//    return id;
//}

JNIEXPORT jint JNICALL Java_com_science09_apple_demo_JavaNative_getInt(JNIEnv *env, jobject obj)
{
    return 11;
}

//JNIEXPORT jint JNICALL native_getInt(JNIEnv *env, jobject obj)
//{
//    return 11;
//}

#define JNIREG_CLASS "com/science09/apple/demo/JavaNative" //指定要注册的类


/**
 * 方法名称
 */
static JNINativeMethod gMethods[] = {
        {"getStringFromNative", "()Ljava/lang/String;", (void*)Java_com_science09_apple_demo_JavaNative_getStringFromNative},
        {"getInt", "()I", (void*)Java_com_science09_apple_demo_JavaNative_getInt},
        {"setId", "(I)I", (int*)Java_com_science09_apple_demo_JavaNative_setId},
};

static int registerNativeMethods(JNIEnv* env, const char* className,
                    JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;
    clazz = (*env)->FindClass(env, className);
    if(clazz == NULL){
        return JNI_FALSE;
    }
    if((*env)->RegisterNatives(env, clazz, gMethods, numMethods) < 0){
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

static int registerNatives(JNIEnv* env)
{
    if(!registerNativeMethods(env, JNIREG_CLASS, gMethods, sizeof(gMethods)/ sizeof(gMethods[0])))
        return JNI_FALSE;
    return JNI_TRUE;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved){
    JNIEnv* env = NULL;
    JNINativeMethod nm[2];
    jclass cls;
    jint  result = -1;

    LOGD("JNI_OnLoad========");
    if((*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_4)!= JNI_OK){
        LOGE("Error**************");
        return JNI_ERR;
    }

    if(!registerNatives(env)){
        return -1;
    }
    LOGD("JNI_---LOAD");
    return JNI_VERSION_1_4;
}