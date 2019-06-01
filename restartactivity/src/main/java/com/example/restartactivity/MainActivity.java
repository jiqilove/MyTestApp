package com.example.restartactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (MyApplication.appState==0){

            Log.e("MainActivity", "onCreate:"+MyApplication.appState+"-----> home" );
            protectApp();

        }




    }

    protected void protectApp() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(ConstantValues.KEY_HOME_ACTION, ConstantValues.ACTION_RESTART_APP);
        startActivity(intent);
    }


}
