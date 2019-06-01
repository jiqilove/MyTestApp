package com.example.mypermission.common;

import android.Manifest;

/**
 * 作者 ： cnb on 2019-05-30
 * 功能 ：
 * 修改 ：
 * 备注 ：你在申请任何权限组的一个子权限 都是在申请整一个权限组。
 *       可以发现在弹出框是对一个权限组的描述
 */
public class CommonValues {

//    public  static void Storage()

    //读写权限
    public static final String Storage_Group_Permission = Manifest.permission_group.STORAGE;
    public static final String Write_Permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String Read_Permission = Manifest.permission.READ_EXTERNAL_STORAGE;

    //相机
    public static final String Camera_Permission = Manifest.permission.CAMERA;

    //联系人组
    public static final String Read_Contacts_Permission = Manifest.permission.READ_CONTACTS;
    public static final String Write_Contacts_Permission = Manifest.permission.WRITE_CONTACTS;
    public static final String Get_Accounts_Permission = Manifest.permission.GET_ACCOUNTS;

    //麦克风
    public static final String Record_Audio_Permission = Manifest.permission.RECORD_AUDIO;

    //位置
    public static final String Location_Group_Permission = Manifest.permission_group.LOCATION;
    public static final String Location_Access_Fine_Permission = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String Location_Access_Coarse_Permission = Manifest.permission.ACCESS_COARSE_LOCATION;


    //电话
    public static final String Phone_Group_Permission = Manifest.permission_group.PHONE;
    public static final String Phone_Read_State_Permission = Manifest.permission.READ_PHONE_STATE;
    public static final String Phone_Call_Permission = Manifest.permission.CALL_PHONE;
    public static final String Phone_Read_Call_Log_Permission = Manifest.permission.READ_CALL_LOG;
    public static final String Phone_Write_Call_Log_Permission = Manifest.permission.WRITE_CALL_LOG;
    public static final String Phone_Use_Sip_Permission = Manifest.permission.USE_SIP;
    public static final String Phone_Process_Outgoing_Calls_Permission = Manifest.permission.PROCESS_OUTGOING_CALLS;


    //传感器
    public static final String BODY_SENSORS_Permission = Manifest.permission.BODY_SENSORS;


    //信息
    public static final String Sms_Group_Permission = Manifest.permission_group.SMS;
    public static final String Sms_Send_Permission = Manifest.permission.SEND_SMS;
    public static final String Sms_Receive_Permission = Manifest.permission.RECEIVE_SMS;
    public static final String Sms_Read_Permission = Manifest.permission.READ_SMS;
    public static final String Sms_Receive_Wap_Push_Permission = Manifest.permission.RECEIVE_WAP_PUSH;
    public static final String Sms_Receive_Mms_Permission = Manifest.permission.RECEIVE_MMS;


    public  static  void getPermissionImg(String per){



    }

}
