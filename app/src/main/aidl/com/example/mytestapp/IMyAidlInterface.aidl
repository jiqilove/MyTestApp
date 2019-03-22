// IMyAidlInterface.aidl
package com.example.mytestapp;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            //定义自己说需要的方法，显示当前服务进度的
    void  showProgress();

}
