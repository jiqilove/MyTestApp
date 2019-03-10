package com.example.mytestapp.drawable_xml_test;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mytestapp.R;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class LevelListDrawableActivity extends AppCompatActivity {

    @BindView(R.id.open)
    Button btn_open;
    @BindView(R.id.close)
    Button btn_close;

    @BindView(R.id.image)
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list_drawable);
        ButterKnife.bind(this);
        img.setImageLevel(3);

        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageLevel(3);
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageLevel(7);
            }
        });


    }
}
