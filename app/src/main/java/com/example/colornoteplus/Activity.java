package com.example.colornoteplus;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Template class for the majority of the activity in the app.
 * Sync is performed automatically when the activity is onPause
 * @see AppCompatActivity
 * @see Sync
 */
public abstract class Activity extends AppCompatActivity {

    private boolean reset = false;

    public void setReset(boolean status){
        this.reset = status;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (User.getID(this).equals(App.NO_ID))
            return;

        if (Sync.getAutoSync()) {
            Sync.performSync(
                    this,
                    5000,
                    new Sync.OnDataSynchronization() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSynced() {
                            if (reset)
                                User.resetUserData(getApplicationContext());

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

                        }

                        @Override
                        public void onTimeOut() {

                        }
                    }
            );
        }

    }
}
