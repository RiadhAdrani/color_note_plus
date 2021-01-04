package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

        TextView splashSyncText = findViewById(R.id.splash_sync_text);
        splashSyncText.setTextColor(getResources().getColor(
                Style.getNeutralTextColor(getApplicationContext())
        ));

        // get modification date
        Sync.getModificationDate(getApplicationContext(), new Sync.OnLongRetrieval() {
            @Override
            public void onSuccess(Long value) {

                Long localSync = DatabaseManager.getDatabaseLastModificationDate(getApplicationContext());

                Sync.performSync(getApplicationContext(),value,localSync);

                delayedStart(1000L);

            }
            @Override
            public void onFailure() {
                Log.d("SYNC_NOTES","Unable to get Data");
                delayedStart(500L);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    private void delayedStart(Long delay){
        Handler handler=new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intent);
            finish();
        },delay);
    }

}