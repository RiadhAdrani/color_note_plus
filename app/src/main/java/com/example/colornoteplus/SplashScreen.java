package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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

        Sync.performSync(
                this,
                20000,
                new Sync.OnDataSynchronization() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSynced() {
                        Toast.makeText(SplashScreen.this, R.string.sync_success, Toast.LENGTH_SHORT).show();
                        delayedStart(1000L);
                    }

                    @Override
                    public void onUploaded() {

                    }

                    @Override
                    public void onDownloaded(int theme, int color, String username, String email, ArrayList<Note<?>> notes) {
                        Style.setAppColor(color,getApplicationContext());
                        Style.setAppTheme(theme,getApplicationContext());
                        User.setUsername(username,getApplicationContext());
                        User.setEmail(getApplicationContext(),email);
                    }

                    @Override
                    public void onNetworkError() {
                        Toast.makeText(SplashScreen.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onTimeOut() {
                        Toast.makeText(SplashScreen.this, R.string.sync_time_out_return, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
        );

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