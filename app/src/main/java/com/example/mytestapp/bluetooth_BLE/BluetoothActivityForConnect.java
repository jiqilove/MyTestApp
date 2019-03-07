package com.example.mytestapp.bluetooth_BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.BaseInputConnection;
import android.widget.TextView;

import com.example.mytestapp.R;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;

public class BluetoothActivityForConnect extends AppCompatActivity {

    private static final String TAG = "BluetoothActivityForCon";
    private BluetoothDevice device;
    private TextView tv_name, tv_address;
    private String txt_name_str;
    private String txt_address_str;

    private BluetoothGatt gatt;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_for_connect);
        initView();
        initDate();

//        connect();

    }

    private void connect() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(txt_address_str);
        gatt = device.connectGatt(BluetoothActivityForConnect.this, false, mGattCallback);
    }

    private void initView() {
        tv_name = findViewById(R.id.txt_name);
        tv_address = findViewById(R.id.txt_address);

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
}
