package com.example.mytestapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mytestapp.IMyAidlInterface;

public class MyTestService extends Service {
    private static final String TAG = "MyTestService";
    private int i = 0;

    //绑定服务时调用
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind: ");
        //返回代理对象
        return new IMyAidlInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

            }

            @Override
            public void showProgress() throws RemoteException {
                Log.e(TAG, "showProgress: " + i);
            }
        };
    }

    //接触绑定服务
    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }
    Thread thread;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: 我只是执行一次");
        super.onCreate();
 thread= new Thread(new Runnable() {
            @Override
            public void run() {

                for (i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(500);
                        Log.e(TAG, "run: "+i );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
thread.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: 我可以一直执行");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        thread.stop();
        super.onDestroy();
    }


    //代理类
    public class MyBinder extends Binder {
        public void showSuccess() {
            Log.e(TAG, "showSuccess: ");
        }

        public void showFail() {
            Log.e(TAG, "showFail: ");
        }
    }

}
