package com.example.mytestapp.bluetooth_BLE;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class BluetoothLEService extends Service {
    //连接
    public final static String ACTION_GATT_CONNECTED = "com.example.mytestapp.bluetooth_BLE.ACTION_GATT_CONNECTED";
    //断开连接
    public final static String ACTION_GATT_DISCONNECTED = "com.example.mytestapp.bluetooth_BLE.ACTION_GATT_DISCONNECTED";

    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.mytestapp.bluetooth_BLE.ACTION_GATT_SERVICES_DISCOVERED";

    public final static String ACTION_DATA_AVAILABLE = "com.example.mytestapp.bluetooth_BLE.ACTION_DATA_AVAILABLE";

    public final static String EXTRA_DATA = "com.example.mytestapp.bluetooth_BLE.EXTRA_DATA";
    public final static String ONCHARACTERISTICCHANGED = "com.example.mytestapp.bluetooth_BLE.ONCHARACTERISTICCHANGED";
    public final static String ONCHARACTERISTICREAD = "com.example.mytestapp.bluetooth_BLE.onCharacteristicRead";


    public BleBinder bleBinder = new BleBinder();
    private BluetoothGatt mBluetoothGatt;

    public static final String SERVICE_UUID = "00002b00-0000-1000-8000-00805f9b34fb";
    private static final String NOTITE = ("00002b10-0000-1000-8000-00805f9b34fb");
    private static final String WRITE_UUID = ("00002b11-0000-1000-8000-00805f9b34fb");

    BluetoothGattCharacteristic notifation;
    BluetoothGattCharacteristic readCharacter ;
    BluetoothGattCharacteristic writeCharacter = null;
    BluetoothGattService linkLossService;

    //////=============mBluetoothGatt============
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            newState =2 就是连接
//            newState =0 就是断开连接
            Log.e(TAG, "onConnectionStateChange: " + gatt + "==" + status + "===" + newState);

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                broadcastUpdate(ACTION_GATT_CONNECTED);
                gatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                broadcastUpdate(ACTION_GATT_DISCONNECTED);
            }


        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt1, int status) {
            List<BluetoothGattService> list = mBluetoothGatt.getServices();
            for (BluetoothGattService bluetoothGattService:list){
                Log.e("onServicesDisc中中中", " ：" + bluetoothGattService.getUuid().toString());

                List<BluetoothGattCharacteristic> gattCharacteristics = bluetoothGattService.getCharacteristics();

                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    Log.e("onServicesDisc中中中", " ：" + gattCharacteristic.getUuid());
                    if(NOTITE.equals(gattCharacteristic.getUuid().toString())){
                        linkLossService=bluetoothGattService;
                        notifation =gattCharacteristic;
                        Log.e("daole", notifation.getUuid().toString());
                    }
                    if(WRITE_UUID.equals(gattCharacteristic.getUuid().toString())){
                        readCharacter =gattCharacteristic;
                        writeCharacter =gattCharacteristic;
                        Log.e("daole", readCharacter.getUuid().toString());
                    }
                }

            }
            enableNotification(notifation, mBluetoothGatt,true);//必须要有，否则接收不到数据
            enableRead(readCharacter, mBluetoothGatt);
//            enableWrite(writeCharacter, mBluetoothGatt);

            sendCommand((byte) 0x02, buildCommandContent((byte) 0x02));


//            super.onServicesDiscovered(mBluetoothGatt, status);
        }



        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] buffer = characteristic.getValue();
            Intent intent = new Intent();
            intent.putExtra("aa", buffer);
            intent.setAction(ONCHARACTERISTICCHANGED);

            sendBroadcast(intent);

            Log.e(TAG, "onCharacteristicChanged: " + gatt + "==" + characteristic);

//            super.onCharacteristicChanged(mBluetoothGatt, characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            Log.e(TAG, "readData:" +          gatt.getDevice().getName());
            Log.e(TAG, "readData:" +          gatt.getDevice().getAddress());



//

//            Intent intent = new Intent();
//            intent.putExtra("aa", buffer);
//            intent.setAction(ONCHARACTERISTICREAD);
//
//            sendBroadcast(intent);
            Log.e(TAG, "onCharacteristicRead: " + gatt + "===" + characteristic.getValue() + "===" + status);
//            super.onCharacteristicRead(mBluetoothGatt, characteristic, status);
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicWrite: " + gatt + "===" + characteristic + "===" + status);
        }


    };


    private void broadcastUpdate(String ation) {
        Intent a = new Intent();
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
        if (bluetoothAdapter == null || deviceAddress == null) {
            return false;
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
        if (device == null) {
            return false;
        }
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        return true;


    }

    public void disconnect() {
        //断开连接
        if (bluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
    }

    private static final String TAG = "BluetoothLEService";

    public void enableNotification(BluetoothGattCharacteristic characteristic,BluetoothGatt gatt,boolean enable) {
        if (gatt!=null && characteristic!=null ){
            gatt.setCharacteristicNotification(characteristic, enable);
        }
    }
    public void enableWrite(BluetoothGattCharacteristic characteristic,BluetoothGatt gatt) {
        if (gatt!=null && characteristic!=null ){
            gatt.writeCharacteristic(characteristic);
        }
    }
    public void enableRead(BluetoothGattCharacteristic characteristic,BluetoothGatt gatt) {
        if (gatt!=null && characteristic!=null ){
            gatt.readCharacteristic(characteristic);
        }
    }

    class BleBinder extends Binder {
        //连接蓝牙后需要调用的方法

        public BluetoothLEService getService() {
            Log.e(TAG, "getService: ");
            return BluetoothLEService.this;
        }


    }

    private void sendCommand(final byte command, final byte[] content) {


//        new Thread(new Runnable() {
//            @Override
//            public void run() {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //帧头
            outputStream.write((byte) 0xfd);
            outputStream.write((byte) 0x01);
            //指令
            outputStream.write(command);
            //长度
            outputStream.write(content.length);
            //数据
            outputStream.write(content);
            //校验
            outputStream.write((byte) 0x00);
            //帧尾
            outputStream.write((byte) 0x0a);

            byte[] data = outputStream.toByteArray();
            //计算校验值
            for (int i = 0; i < data.length - 2; i++) {
                data[data.length - 2] ^= data[i];
            }
            Log.e(TAG, " sendCommand:" + data.length);

            //
            for (int i = 0; i < data.length; i++) {
                Log.e(TAG, "sendCommand readData:" + i + "---- :  " + data[i]);
            }

            for (int i = 0; i < data.length; i += 20) {
                //分包发送
//                      if (semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS)) {
//                         MyBleManager.getInstance().writeData(ArrayUtil.subarray(data, i,  20));
               writeData(subarray(data, i, i + 20));
//                      }
            }
        } catch (IOException ex) {
            Log.e("BleTask", ex.getMessage());
        } catch (Exception ex) {
            Log.e("BleTask", ex.getMessage());
        }
//            }
//        }).start();

    }

    public void writeData(byte[] data){
        if(mBluetoothGatt != null && writeCharacter != null) {
            writeCharacter.setValue(data);
            mBluetoothGatt.writeCharacteristic(writeCharacter);
            SystemClock.sleep(100);
        }
    }

    public static byte[] subarray(final byte[] array, int startIndexInclusive, int endIndexExclusive) {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        final int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return new byte[0];
        }

        final byte[] subarray = new byte[newSize];
        System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
        return subarray;
    }

    private byte[] buildCommandContent(byte command) {
        int unixTime = (int) (System.currentTimeMillis() / 1000);
        switch (command) {
            case (byte) 0x02:
                //设置时间
                return new byte[]{(byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) unixTime, (byte) (unixTime >> 8), (byte) (unixTime >> 16), (byte) (unixTime >> 24), (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            case (byte) 0x00:
                return new byte[]{(byte) 0x00};
            case  (byte) 0x03:
                //根据卡号获取班级等信息,单独定制
                return null;
            case (byte) 0x04:
                return new byte[]{(byte) 0x01};
            default:
                return new byte[0];
        }
    }
}
