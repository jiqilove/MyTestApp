package com.example.mypermission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mypermission.common.Ab_Permission;
import com.example.mypermission.common.CommonValues;
import com.example.mypermission.common.StoragePermission;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionResult.IPermissionResultListener {
    public  static  final  String  getWrite=Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int RC_PERMISSION = 14;
    private static final int RC_SETTINGS_SCREEN = 15;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate:");
        initView();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//==========告一段落：怎样去去一个权限，并且判断他的类型是什么，之后把它设置为一个图标===================
//                StoragePermission a =new StoragePermission();
//                a.permissionStr()

//                ApplyforPermission.apply(MainActivity.this,CommonValues.Storage_Group_Permission);
                ApplyforPermission.apply(MainActivity.this, CommonValues.Read_Permission,CommonValues.Phone_Call_Permission);

            }
        });
    }

    private void initView() {
        btn = findViewById(R.id.btn);
    }



    private static final String TAG = "MainActivity";


    /**
     * 从设置页面返回，可以再次检查权限是否已打开
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SETTINGS_SCREEN) {
            // Do something after user returned from app settings screen, like showing a Toast.

            Log.e(TAG, "权限申请结果: 从设置页面返回");
        }
    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionResult.result(MainActivity.this,requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsGranted:dddd"  );
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        for (String perm : perms) {
            Log.e(TAG, "onPermissionsDenied:444" +perm);
        }
        //弹框--获取那些全选没有通过
        //让用户去系统设置
//            Intent intent = new Intent(Settings.ACTION_SETTINGS);
//            startActivity(intent);
//            Intent mIntent = new Intent();
//            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//            mIntent.setData(Uri.fromParts("package", getPackageName(), null));
//            startActivityForResult(mIntent, RC_SETTINGS_SCREEN);
    }

}
