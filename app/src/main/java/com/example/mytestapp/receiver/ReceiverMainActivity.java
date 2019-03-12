package com.example.mytestapp.receiver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mytestapp.R;

public class ReceiverMainActivity extends AppCompatActivity {

    //广播不能滥用。可能会导致其他的问题
    // 在支付中，使用广播是很好的


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_main);
    }
}
