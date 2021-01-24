package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Splash screen allowing the synchronization of data between the local database
 * and the Firebase Firestore database.
 * @see Sync
 * @see DatabaseManager
 */
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

                if (value != null)
                {
                    Long localSync = DatabaseManager.getDatabaseLastModificationDate(getApplicationContext());
                    Sync.performSync(getApplicationContext(),value,localSync);
                }

                delayedStart(1000L);

            }
            @Override
            public void onFailure() {
                Log.d("SYNC_NOTES","Unable to get Data");
                delayedStart(100L);
            }
        });

        Sync.getUpdateNotes(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    /**
     * Start the main activity after a delay.
     * @param delay delay time in milliseconds.
     * @see MainActivity
     */
    private void delayedStart(Long delay){
        Handler handler=new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intent);
            finish();
        },delay);
    }

}