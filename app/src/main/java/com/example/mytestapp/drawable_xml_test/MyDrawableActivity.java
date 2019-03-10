package com.example.mytestapp.drawable_xml_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mytestapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyDrawableActivity extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_drawable);
        ButterKnife.bind(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        img.setImageDrawable(new CliCleDrawable(bitmap));
    }
}
