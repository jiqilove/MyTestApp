package com.example.mytestapp.drawable_xml_test;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mytestapp.R;
import com.example.mytestapp.bluetooth_BLE.MyBluetoothDevice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrwableStyleOfXMLActivity extends AppCompatActivity {

    @BindView(R.id.text1)
    TextView textView;

    @BindView(R.id.btn_layer)
    Button btn_layer;

    @BindView(R.id.btn_level)
    Button btn_level;

    @BindView(R.id.btn_transition)
    Button btn_transition;

    @BindView(R.id.btn_my_drawable)
    Button btn_my_drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drwable_style_of_xml);
        ButterKnife.bind(this);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{Color.parseColor("#990033"), Color.parseColor("#009966")});
        drawable.setCornerRadius(20);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        textView.setBackground(drawable);
        onclick();

    }

    private void onclick() {
        final Intent intent3 = new Intent(DrwableStyleOfXMLActivity.this, MyDrawableActivity.class);
        btn_my_drawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent3);
            }
        });

        final Intent intent2 = new Intent(DrwableStyleOfXMLActivity.this, TransitionDrawableActivity.class);
        btn_transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });

        final Intent intent1 = new Intent(DrwableStyleOfXMLActivity.this, LevelListDrawableActivity.class);
        btn_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent1);
            }
        });


        final Intent intent = new Intent(DrwableStyleOfXMLActivity.this, LayerDrawableActivity.class);
        btn_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }
}
