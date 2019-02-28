package com.example.mytestapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mytestapp.service.ForgroundService;
import com.example.mytestapp.service.MyTestService;

public class MainActivity extends AppCompatActivity {

    Button btn_start, btn_stop;
    private MyTestService.MyBinder myBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            if (service instanceof MyTestService.MyBinder){
                myBinder = (MyTestService.MyBinder) service;
                myBinder.showSuccess();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder.showFail();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        final Intent intent = new Intent(this, MyTestService.class);
        final Intent intent1 = new Intent(this, ForgroundService.class);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                startService(intent);
//                bindService(intent, connection, BIND_AUTO_CREATE);
//                startService(new Intent(this, ForgroundService.class));
                startService(intent1);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopService(intent);
//                unbindService(connection);
                stopService(intent1);
            }
        });


    }
}
