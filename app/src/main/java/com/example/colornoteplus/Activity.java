package com.example.colornoteplus;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("AUTO_SYNC", "Auto Sync is :" + Sync.getAutoSync(getApplicationContext()));

        if (Sync.getAutoSync(this)) {
            Log.d("AUTO_SYNC", "Syncing ... \nAuto Sync is :" + Sync.getAutoSync(getApplicationContext()));
            Sync.performSync(this, null);
            Log.d("SYNC_NOTES","Auto Sync On");
        }

    }
}
