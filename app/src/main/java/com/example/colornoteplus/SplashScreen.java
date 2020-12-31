package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        findViewById(R.id.splash_background)
                .setBackgroundColor(
                        getResources().getColor(
                                Style.getNeutralColor(getApplicationContext())));

        TextView splashText = findViewById(R.id.splash_text);
        splashText.setTextColor(getResources().getColor(
                Style.getNeutralTextColor(getApplicationContext())
        ));

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },500);
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }


}
