package com.example.restartactivity;

import android.app.Application;
import android.util.Log;

import java.lang.invoke.ConstantCallSite;

/**
 * 作者 ： cnb on 2019-05-22
 * 功能 ：
 * 修改 ：
 */
public class MyApplication extends Application {


    public  static  int appState = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
