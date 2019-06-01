package com.example.spanalbestringtest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.ImageSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
 //   The classes that affect character-level text formatting extend this class.  Most extend its subclass {@link MetricAffectingSpa}, but simple ones may just implement {@link UpdateAppearance}.
    private TextView tv1;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        SpannableString spannableString = new SpannableString("我真的是一个很nice的人喔");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(MainActivity.this, "我能点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }
        };
        spannableString.setSpan(clickableSpan, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(30, true);
        spannableString.setSpan(absoluteSizeSpan, 2, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        spannableString.setSpan(backgroundColorSpan, 4, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//
////        DrawableMarginSpan drawableMarginSpan = new DrawableMarginSpan(ContextCompat.getDrawable(MainActivity.this, R.drawable.bg)
////                , 200);
////
////        spannableString.setSpan(drawableMarginSpan,6,7,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        ;
        Drawable d = getResources().getDrawable(R.drawable.bg);
        d.setBounds(0, 0, 100, 100);
        ImageSpan imageSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 6, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        tv1.setText(spannableString);
        tv1.setMovementMethod(LinkMovementMethod.getInstance());


        SpannableString spannableString2 = new SpannableString("log28=3");
        spannableString2.setSpan(new SubscriptSpan(),3,4,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString2.setSpan(new AbsoluteSizeSpan(10,true),3,4,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString2.setSpan(new SuperscriptSpan(),4,5,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString2.setSpan(new AbsoluteSizeSpan(10,true),4,5,Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        tv2.setText(spannableString2);
        tv2.setMovementMethod(LinkMovementMethod.getInstance());



    }


    private void initView() {
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
    }
}
