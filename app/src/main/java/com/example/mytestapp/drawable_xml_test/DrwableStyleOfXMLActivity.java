package com.example.mytestapp.drawable_xml_test;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mytestapp.R;

public class DrwableStyleOfXMLActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drwable_style_of_xml);
        textView = findViewById(R.id.text1);

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{Color.parseColor("#990033"), Color.parseColor("#009966")});
        drawable.setCornerRadius(20);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        textView.setBackground(drawable);

    }
}
