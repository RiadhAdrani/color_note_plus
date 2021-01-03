package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

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

        // get modification date
        Sync.getModificationDate(getApplicationContext(), new Sync.OnLongRetrieval() {
            @Override
            public void onSuccess(Long value) {

                Long localSync = DatabaseManager.getDatabaseLastModificationDate(getApplicationContext());

                Sync.performSync(getApplicationContext(),value,localSync);

                delayedStart(2000L);

            }
            @Override
            public void onFailure() {
                Log.d("SYNC_NOTES","Unable to get Data");
                delayedStart(1000L);
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

    private void performSync(){

        Sync.getUserData(this, new Sync.OnQueryDataRetrieval() {
            @Override
            public void onSuccess(QuerySnapshot snapshot) {

                // get data from the local database
                ArrayList<Note<?>> localNotes = DatabaseManager.LoadAllNotes(getApplicationContext());

                // get data from the FirebaseFirestore database
                ArrayList<Note<?>> notes = new ArrayList<>();
                for (DocumentSnapshot snap : snapshot.getDocuments()){
                    notes.add(Note.fromMap(getApplicationContext(), Objects.requireNonNull(snap.getData())));
                    Log.d("SYNC_NOTES","Snap key "+snap.getId());
                }

                // check if there is a difference is size
                if (notes.size() != localNotes.size()) Log.d("SYNC_NOTES","Notes Number Delta !");
                else {
                    // compare notes one by one
                    for (int i = 0; i < notes.size() ; i++){

                        // check if the current note exists in the local database
                        if (App.isNoteByUidInList(notes.get(i),localNotes)){

                            int index = App.getNoteIndexByUidInList(notes.get(i),localNotes);
                            if (!notes.get(i).isEqualTo(localNotes.get(index))){
                                Log.d("SYNC_NOTES","A Note Delta !");
                            }
                        }
                        // break from the iteration
                        else break;
                    }
                }

                delayedStart(500L);
            }

            @Override
            public void onFailure() {
                delayedStart(100L);
            }
        });
    }

}
