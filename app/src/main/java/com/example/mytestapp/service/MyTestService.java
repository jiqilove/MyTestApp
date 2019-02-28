package com.example.mytestapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyTestService extends Service {
    private static final String TAG = "MyTestService";

    //绑定服务时调用
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        //返回代理对象
        return new MyBinder();
    }

    //接触绑定服务
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: 我只是执行一次");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: 我可以一直执行");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
    }


    //代理类
    public   class MyBinder extends Binder {
        public void showSuccess() {
            Log.e(TAG, "showSuccess: ");
        }

        public void showFail() {
            Log.e(TAG, "showFail: " );
        }
    }

}
