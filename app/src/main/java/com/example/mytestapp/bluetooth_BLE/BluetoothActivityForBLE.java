package com.example.mytestapp.bluetooth_BLE;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytestapp.R;

import java.util.ArrayList;
import java.util.List;

public class BluetoothActivityForBLE extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter.LeScanCallback leScanCallback;  //ble 蓝牙回调
    private TextView textView;
    private Button bleBtn;
    private Boolean isScanning = false;
    private List<BluetoothDevice> deviceList = new ArrayList<>();

    private RecyclerView recyclerView;
    private ListAdapter adapter;

    //之后在弄一个10秒扫描的线程
    private Handler mHandler = new Handler();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_for_ble);
        initView();
        initBluetooth();//初始化蓝牙
        scanBLe();//
        initRecyclerView();


    }

    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerDecoration(this));
        adapter = new ListAdapter(deviceList);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        adapter.OnItemClick(new ListAdapter.IonClickItemView() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClickItem(View view, int position) {
                Log.e("cnb", "onClickItem: " + position+"--"+deviceList.get(position).getName());



                Intent intent =new Intent(BluetoothActivityForBLE.this,BluetoothActivityForConnect.class);

                intent.putExtra("ble_address",deviceList.get(position).getAddress());
                intent.putExtra("ble_name",deviceList.get(position).getName());
                startActivity(intent);



            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void scanBLe() {
        leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                //扫描在ui线程中进行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (deviceList.indexOf(device) == -1) {
                            deviceList.add(device);
                            adapter.notifyDataSetChanged();
                        }


                    }
                });
            }
        };

        bleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isScanning) {//开启

                    bluetoothAdapter.startLeScan(leScanCallback);
                    bleBtn.setText("停止扫描");
                    isScanning = true;
                    if (isScanning) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isScanning = false;
                                bluetoothAdapter.stopLeScan(leScanCallback);
                                bleBtn.setText("开始扫描");
                            }
                        }, 10000);
                    }
                } else {
                    isScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                    bleBtn.setText("开始扫描");

                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initBluetooth() {
        //是否有蓝牙 Ble的特征 FEATURE_BLUETOOTH_LE
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText(this, "没有蓝牙ble", Toast.LENGTH_SHORT).show();
            return;
        }

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "没有蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
        textView.setText("已经开启蓝牙");
    }

    private void initView() {

        textView = findViewById(R.id.ble_text);
        bleBtn = findViewById(R.id.ble_btn_start);
    }
}
