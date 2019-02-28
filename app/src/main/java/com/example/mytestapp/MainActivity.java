package com.example.mytestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mytestapp.service.ServiceActivity;

public class MainActivity extends AppCompatActivity {



    private Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

    }

    private void initListener() {

        //======================服务======================
        final Intent intent = new Intent(this, ServiceActivity.class);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });





    }

    private void initView() {
        btn1 =findViewById(R.id.btn1);

    }
}
