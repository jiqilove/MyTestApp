package com.example.mytestapp.bluetooth_BLE;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class BluetoothLEService extends Service {
    //连接
    public final static String ACTION_GATT_CONNECTED ="com.example.mytestapp.bluetooth_BLE.ACTION_GATT_CONNECTED";
    //断开连接
    public final static String ACTION_GATT_DISCONNECTED ="com.example.mytestapp.bluetooth_BLE.ACTION_GATT_DISCONNECTED";

    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.mytestapp.bluetooth_BLE.ACTION_GATT_SERVICES_DISCOVERED";

    public final static String ACTION_DATA_AVAILABLE = "com.example.mytestapp.bluetooth_BLE.ACTION_DATA_AVAILABLE";

    public final static String EXTRA_DATA = "com.example.mytestapp.bluetooth_BLE.EXTRA_DATA";


    public BleBinder bleBinder = new BleBinder();
    private BluetoothGatt gatt;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            newState =2 就是连接
//            newState =0 就是断开连接
            Log.e(TAG, "onConnectionStateChange: "+gatt+"=="+status+"==="+newState );

           if (newState== BluetoothProfile.STATE_CONNECTED){
               broadcastUpdate(ACTION_GATT_CONNECTED);
           }else if (newState ==BluetoothProfile.STATE_DISCONNECTED){
               broadcastUpdate(ACTION_GATT_DISCONNECTED);
           }


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

            }
            Log.e(TAG, "onServicesDiscovered: "+gatt+"=="+status+"==="  );
            super.onServicesDiscovered(gatt, status);
        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.e(TAG, "onCharacteristicChanged: "+gatt+"=="+characteristic );
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicRead: "+gatt+"==="+characteristic+"==="+status );
            super.onCharacteristicRead(gatt, characteristic, status);
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicWrite: "+gatt+"==="+characteristic+"==="+status );
            super.onCharacteristicWrite(gatt, characteristic, status);
        }




    };

    private void broadcastUpdate(String ation) {
        Intent a =new Intent();
        a.setAction(ation);
        sendBroadcast(a);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bleBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager bluetoothManager;

    //初始化蓝牙
    public boolean initialize() {

        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                return false;
            }
        }

        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    public boolean connect(String deviceAddress) {
        //先判断是否能连接蓝牙，地址是否为空
        if (bluetoothAdapter ==null || deviceAddress==null){
            return  false;
        }
//        // Previously connected device.  Try to reconnect.
//        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            } else {
//                return false;
//            }
//        }


        //通过设备地址来 获取远程设备，

        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        if(device==null){
            return  false;
        }
        gatt = device.connectGatt(this, false, mGattCallback);
        return  true;


    }
    public void disconnect() {
        //断开连接
        if (bluetoothAdapter == null || gatt == null) {
            return;
        }
        gatt.disconnect();
    }

    private static final String TAG = "BluetoothLEService";
    class BleBinder extends Binder {
        //连接蓝牙后需要调用的方法

        public BluetoothLEService getService() {
            Log.e(TAG, "getService: " );
            return BluetoothLEService.this;
        }


    }

}
