package com.test.Bishal_todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler=new Handler();



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("todo_pref",0);
                Boolean authentication = preferences.getBoolean("authentication", false);
                if (authentication)
                {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }

                else {
                Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(intent);
                    finish();
                }
            }
        }, 2000);




    }
}
