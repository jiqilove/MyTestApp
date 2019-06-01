package com.example.mypermission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 ： cnb on 2019-05-30
 * 功能 ：
 * 修改 ：
 */
public  class PermissionResult  {


    private  IPermissionResultListener mIPermissionResultListener;
    private static final String TAG = "PermissionResult";
    private static final int RC_PERMISSION = 14;
    private static final int RC_SETTINGS_SCREEN = 15;

    public static void result(AppCompatActivity context, int requestCode, String[] permissions, int[] grantResults,IPermissionResultListener listener) {
//        if (mIPermissionResult != null) {
//            Log.e(TAG, "onRequestPermissionsResult:不不不不" );
//            mIPermissionResult.onPermissionResult(requestCode, permissions, grantResults);
//        }

//        if (requestCode == RC_PERMISSION && grantResults.length == 2
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//            Log.e(TAG, "权限申请成功");
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }



            listener.onPermissionsGranted(requestCode,granted);

            listener.onPermissionsDenied(requestCode,denied);



            Log.e(TAG, "权限申请失败");


    }


//    public interface IPermissionResultListener  extends  ActivityCompat.OnRequestPermissionsResultCallback {
//
//        void onPermissionsGranted(int requestCode, @NonNull List<String> perms);
//
//        void onPermissionsDenied(int requestCode, @NonNull List<String> perms);
//    }
//
    public interface IPermissionResultListener   {

        void onPermissionsGranted(int requestCode, @NonNull List<String> perms);

        void onPermissionsDenied(int requestCode, @NonNull List<String> perms);
    }


}
