package com.example.colornoteplus;

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

        if (User.getID(this).equals(App.NO_ID))
            return;

        if (Sync.getAutoSync()) {
            Sync.performSync(this, null);
        }

    }
}
