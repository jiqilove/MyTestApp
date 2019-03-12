package com.example.mytestapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CnbBroadCastReeiver  extends BroadcastReceiver {

//    他这个是在主线层中进行的，所以不能做太多的耗时操作
    TextView textView;
    public  CnbBroadCastReeiver(){

    }

    private static final String TAG = "CnbBroadCastReeiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent!=null){
            //接收到什么广播
            String action = intent.getAction();
            Log.e(TAG, "onReceive: "+action );

            if (TextUtils.equals("","")){

            }


        }

    }
}
