package com.example.mypermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 ： cnb on 2019-05-30
 * 功能 ：
 * 修改 ：
 */
public class ApplyforPermission {

    public static void apply(AppCompatActivity activity, String permission) {
        //             申请   applyfor

        if (!(ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED)) {
            //没有权限，申请权限
            String[] permissions = {permission};
            //申请权限，其中RC_PERMISSION是权限申请码，用来标志权限申请的
            ActivityCompat.requestPermissions(activity, permissions, 14);
        } else {
            //拥有权限
            Toast.makeText(activity, "拥有权限了", Toast.LENGTH_SHORT).show();

        }
    }

    public static void apply(AppCompatActivity activity, String... permission) {

        String[] permissions = permission;
        List<String> strings  =new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!(ActivityCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_GRANTED)) {
                //没有权限，申请权限
                //申请权限，其中RC_PERMISSION是权限申请码，用来标志权限申请的
                strings.add(permissions[i]);
            }
        }
        String [] denide = strings.toArray(new String[strings.size()]);

        ActivityCompat.requestPermissions(activity, denide, 14);

    }


}
