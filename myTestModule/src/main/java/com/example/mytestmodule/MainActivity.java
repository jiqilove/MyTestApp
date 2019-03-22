package com.example.mytestmodule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mytestmodule.mytest.RecycleViewActivity;
import com.example.mytestmodule.pull_to_refresh.PullActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_pulltorefresh)
    Button btn_pulltorefresh;
    @BindView(R.id.btn_recycle)
    Button btn_recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //===============================pull to refresh ==============================================
        final Intent intent1 = new Intent(this, PullActivity.class);
        btn_pulltorefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent1);
            }
        });
//        startActivity(intent1);
        final Intent intent2 = new Intent(this, RecycleViewActivity.class);
        btn_pulltorefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });

        startActivity(intent2);
    }
}
