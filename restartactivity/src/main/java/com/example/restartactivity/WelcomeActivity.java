package com.example.restartactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        MyApplication.appState =1996;
        Log.e("Welcome", "onCreate:到------>home"  );
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
