package com.example.mytestapp.bluetooth_BLE;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class BluetoothLEService extends Service {
    //连接
    public final static String ACTION_GATT_CONNECTED ="com.example.mytestapp.bluetooth_BLE.ACTION_GATT_CONNECTED";
    //断开连接
    public final static String ACTION_GATT_DISCONNECTED ="com.example.mytestapp.bluetooth_BLE.ACTION_GATT_DISCONNECTED";

    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.mytestapp.bluetooth_BLE.ACTION_GATT_SERVICES_DISCOVERED";

    public final static String ACTION_DATA_AVAILABLE = "com.example.mytestapp.bluetooth_BLE.ACTION_DATA_AVAILABLE";

    public final static String EXTRA_DATA = "com.example.mytestapp.bluetooth_BLE.EXTRA_DATA";
    public final static String ONCHARACTERISTICCHANGED = "com.example.mytestapp.bluetooth_BLE.ONCHARACTERISTICCHANGED";


    private static  String uuid ="00002b00-0000-1000-8000-00805f9b34fb";
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
               gatt.discoverServices();

           }else if (newState ==BluetoothProfile.STATE_DISCONNECTED){
               broadcastUpdate(ACTION_GATT_DISCONNECTED);
           }


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt1, int status) {
            BluetoothGattCharacteristic aaa=null;
            BluetoothGattCharacteristic aaa1=null;
            BluetoothGattCharacteristic aaa2=null;
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                List<BluetoothGattService> supportedGattServices = gatt1.getServices();

                for (BluetoothGattService gattService : supportedGattServices) {
                    //得到每个Service的Characteristics
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        int charaProp = gattCharacteristic.getProperties();
                        //所有Characteristics按属性分类
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            Log.d(TAG, "gattCharacteristic的UUID为:" + gattCharacteristic.getUuid());
                            Log.d(TAG, "gattCharacteristic的属性为:  可读");
                            if (gattCharacteristic.getUuid().toString().equals("00002b10-0000-1000-8000-00805f9b34fb")){
                                Log.e(TAG, "onServicesDiscovered: read" );
                                gatt.readCharacteristic(gattCharacteristic);

                            }
//                            readUuid.add(gattCharacteristic.getUuid());
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                            Log.d(TAG, "gattCharacteristic的UUID为:" + gattCharacteristic.getUuid());
                            Log.d(TAG, "gattCharacteristic的属性为:  可写");
//                            writeUuid.add(gattCharacteristic.getUuid());
                            if (gattCharacteristic.getUuid().toString().equals("00002b11-0000-1000-8000-00805f9b34fb")){
                                Log.e(TAG, "onServicesDiscovered: write" );
                                gatt.writeCharacteristic(gattCharacteristic);

                            }

                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            Log.d(TAG, "gattCharacteristic的UUID为:" + gattCharacteristic.getUuid() + gattCharacteristic);
                            Log.d(TAG, "gattCharacteristic的属性为:  具备通知属性");
//                            notifyUuid.add(gattCharacteristic.getUuid());
                        }
                    }
                }

            }


            Log.e(TAG, "onServicesDiscovered: "+gatt+"=="+status+"==="  );
//            super.onServicesDiscovered(gatt, status);
        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
          byte [] buffer =  characteristic.getValue();
          Intent intent =new Intent();
          intent.putExtra("aa",buffer);
          intent.setAction("onCharacteristicChanged");

            sendBroadcast(intent);

            Log.e(TAG, "onCharacteristicChanged: "+gatt+"=="+characteristic );

//            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicRead: "+gatt+"==="+characteristic+"==="+status );
//            super.onCharacteristicRead(gatt, characteristic, status);
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicWrite: "+gatt+"==="+characteristic+"==="+status );
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
