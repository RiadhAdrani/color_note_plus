package com.example.colornoteplus;

import androidx.appcompat.app.AppCompatActivity;

public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        Sync.performSync(this, null);
    }
}
