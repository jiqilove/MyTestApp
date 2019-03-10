package com.example.mytestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mytestapp.bluetooth_BLE.BluetoothActivityForBLE;
import com.example.mytestapp.bluetooth_BLE.BluetoothLEService;
import com.example.mytestapp.drawable_xml_test.DrwableStyleOfXMLActivity;
import com.example.mytestapp.service.ServiceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btn_service)
    Button btn_service;
    @BindView(R.id.btn_drawable)
    Button btn_drawable;
    @BindView(R.id.btn_blue_tooth)
    Button btn_blue_tooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //===============================服务==============================================
        final Intent intent = new Intent(this, ServiceActivity.class);
        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        //===============================蓝牙==============================================
        final Intent intent1 = new Intent(this, BluetoothLEService.class);
        btn_blue_tooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent1);
            }
        });

        //===============================drawable==============================================
        final Intent intent2 = new Intent(this, DrwableStyleOfXMLActivity.class);
        btn_drawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });

        startActivity(intent2);


    }


    private void initView() {


    }
}
