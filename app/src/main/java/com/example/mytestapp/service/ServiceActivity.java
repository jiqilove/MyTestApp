package com.example.mytestapp.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mytestapp.R;

public class ServiceActivity extends AppCompatActivity {


    Button btn_start, btn_stop;
    Button btn_start2, btn_stop2;
    Button btn_start_foreground;
    Button btn_stop_foreground;
    private MyTestService.MyBinder myBinder;
    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);


        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);

        btn_start2 = findViewById(R.id.btn_start2);
        btn_stop2 = findViewById(R.id.btn_stop2);
        btn_start_foreground = findViewById(R.id.btn_start_foreground);
        btn_stop_foreground = findViewById(R.id.btn_stop_foreground);

        final Intent intent = new Intent(this, MyTestService.class);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(intent);

            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });

        //=========================绑定 binder=============================================

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                if (service instanceof MyTestService.MyBinder) {
                    myBinder = (MyTestService.MyBinder) service;
                    myBinder.showSuccess();
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                myBinder.showFail();
            }
        };
        btn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(intent);
                bindService(intent, connection, BIND_AUTO_CREATE);
            }
        });

        btn_stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBinder != null) {
                    unbindService(connection);
                }

            }
        });
        //=========================前台服务  使用第一种方式=============================================

        final Intent intent1 = new Intent(this, ForegroundService.class);

        btn_start_foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(intent1);
            }
        });
        btn_stop_foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent1);
            }
        });


    }
}
