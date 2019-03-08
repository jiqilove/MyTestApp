package com.example.mytestapp.bluetooth_BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.widget.Button;
import android.widget.TextView;

import com.example.mytestapp.R;
import com.example.mytestapp.service.MyTestService;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;

public class BluetoothActivityForConnect extends AppCompatActivity {

    private static final String TAG = "BluetoothActivityForCon";
    private BluetoothDevice device;
    private TextView tv_name, tv_address,tv_status;
    private String txt_name_str;
    private String txt_address_str;
    private Button btn_connect, btn_stop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_for_connect);
        initView();
        initDate();
        connect();

    }

    private ServiceConnection serviceConnection;
    private BluetoothLEService bluetoothLEService;

    private void connect() {

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                if (service instanceof BluetoothLEService.BleBinder) {
                    bluetoothLEService = ((BluetoothLEService.BleBinder) service).getService();
                    // 初始化蓝牙；

                    if (!bluetoothLEService.initialize()){ //不能初始化，，没有蓝牙
                        return;
                    }


                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bluetoothLEService=null;
            }
        };

         Intent gattServiceIntent = new Intent(this, BluetoothLEService.class);
        bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }
    private boolean connect =false;

    private final BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        //这边的 广播就是充当一个更新UI的中介
        @Override
        public void onReceive(Context context, Intent intent) {

            final  String action =intent.getAction();
            if (action!=null){
                if (action.equals(BluetoothLEService.ACTION_GATT_CONNECTED)){
                    connect =true;
                    Log.e("我是广播", "onReceive: 连接成功" );
                    tv_status.setText("连接成功");
                }else if (action.equals(BluetoothLEService.ACTION_GATT_DISCONNECTED)){

                    connect=false;
                    Log.e("我是广播", "onReceive: 断开连接" );
                    tv_status.setText("断开连接");
                }
            }

        }
    };

    private void initView() {
        tv_name = findViewById(R.id.txt_name);
        tv_address = findViewById(R.id.txt_address);
        tv_status = findViewById(R.id.txt_status);
        btn_connect = findViewById(R.id.btn_connect);
        btn_stop = findViewById(R.id.btn_stop);


        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " );
                //都可以，那就连接
                bluetoothLEService.connect(txt_address_str);

            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              bluetoothLEService.disconnect();

            }
        });
    }

    private void initDate() {
        Intent intent = getIntent();
        Log.e(TAG, "initDate: " + intent);
        if (intent != null) {
            txt_address_str = intent.getStringExtra("ble_address");
            txt_name_str = intent.getStringExtra("ble_name");

            tv_address.setText("设备地址:" + txt_address_str);
            tv_name.setText("设备名称:" + txt_name_str);

        }

    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLEService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, makeGattUpdateIntentFilter());
        if (bluetoothLEService != null) {
            final boolean result = bluetoothLEService.connect(txt_address_str);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothLEService!=null){

            unbindService(serviceConnection);
            bluetoothLEService = null;
        }

    }
}
