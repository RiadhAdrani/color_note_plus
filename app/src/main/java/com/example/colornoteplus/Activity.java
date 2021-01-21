package com.example.colornoteplus;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Template class for the majority of the activity in the app.
 * Sync is performed automatically when the activity is onPause
 * @see AppCompatActivity
 * @see Sync
 */
public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("AUTO_SYNC", "Auto Sync is :" + Sync.getAutoSync());

        if (Sync.getAutoSync()) {
            Log.d("AUTO_SYNC", "Syncing ... \nAuto Sync is :" + Sync.getAutoSync());
            Sync.performSync(this, null);
            Log.d("SYNC_NOTES","Auto Sync On");
        }

    }
}
