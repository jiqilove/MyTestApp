package com.example.mypermission;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 作者 ： cnb on 2019-05-31
 * 功能 ：
 * 修改 ：
 */
public class DiaPermissionDialogUtil {


    public static void showPermissionDialog(Context context,String[] permission) {


        View view = LayoutInflater.from(context).inflate(R.layout.layout_permission_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


}
