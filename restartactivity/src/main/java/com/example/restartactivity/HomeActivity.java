package com.example.restartactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.e("HomeActivity.......", "onCreate:" );
        findViewById(R.id.tv_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        int action = intent.getIntExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_BACK_TO_HOME);
        switch (action) {
            case ConstantValues.ACTION_KICK_OUT:
                break;
            case ConstantValues.ACTION_LOGOUT:
                break;
            case ConstantValues.ACTION_RESTART_APP:
                protectApp();
                break;
            case ConstantValues.ACTION_BACK_TO_HOME:
                break;
        }

    }

    protected void protectApp() {
        Log.e("Cnb", "protectApp:666" );
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }
}
