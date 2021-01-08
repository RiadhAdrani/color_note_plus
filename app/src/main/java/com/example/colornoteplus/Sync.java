package com.example.colornoteplus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manages and handle syncing with the Firebase Firestore database
 * @see App
 * @see DatabaseManager
 */
abstract public class Sync {

    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private static final String USERS = App.DATABASE_DATA_USERS;
    private static final String USER_INFO = App.DATABASE_USER_INFO;
    private static final String USER_NOTES = App.DATABASE_USER_NOTES;
    private static final String INFO = App.DATABASE_DATA_INFO;
    private static final String INFO_APP_VERSION = App.DATABASE_DATA_APP_VERSION;
    private static final String INFO_LAST_SYNC = App.DATABASE_USER_INFO_LAST_SYNC;
    private static final String INFO_APP_COLOR = App.DATABASE_USER_COLOR;
    private static final String INFO_APP_THEME = App.DATABASE_USER_THEME;


    /**
     * Interface for data retrieval
     */
    public interface OnDataRetrieval{

        /**
         * executes on success
         * @param snapshot retrieved data
         */
        void onSuccess(DocumentSnapshot snapshot);

        /**
         * executes when data retrieval fails
         */
        void onFailure();
    }

    /**
     * Interface for Long object retrieval
     */
    public interface OnLongRetrieval{

        /**
         * Executes on success
         * @param value retrieved data
         */
        void onSuccess(Long value);

        /**
         * executes on failure
         */
        void onFailure();
    }

    /**
     * Interface for Specific data retrieval
     */
    public interface OnQueryDataRetrieval{

        /**
         * executes on success
         * @param snapshot retrieved data
         */
        void onSuccess(QuerySnapshot snapshot);

        /**
         * executes on failure
         */
        void onFailure();
    }

    /**
     * Interface for Integer value retrieval
     */
    public interface OnIntRetrieval{

        /**
         * Executes on success
         * @param value retrieved data
         */
        void onSuccess(int value);

        /**
         * executes on failure
         */
        void onFailure();
    }

    /**
     * Interface for action post-database-deletion
     */
    public interface OnDataWiped{

        /**
         * Additional action after database clearing
         */
        void onDataWiped();
    }

    /**
     * Interface for data syncing process.
     */
    public interface OnDataSynced{

        /**
         * Additional action after data has been uploaded to the cloud server.
         * and synced properly
         */
        void onDataUploaded();

        /**
         * Additional action after data has been downloaded from the cloud server.
         * @param notes retrieved notes.
         */
        void onDataDownloaded(ArrayList<Note<?>> notes);
    }

    /**
     * get the modification date from the cloud server.
     * @param context calling context
     * @param onDataRetrieval post retrieval actions.
     * @see OnDataRetrieval
     */
    static void getModificationDate(Context context, OnLongRetrieval onDataRetrieval){
        DB.collection(USERS)
                .document(User.getCurrentUser(context).getUsername())
                .collection(USER_INFO)
                .document(INFO_LAST_SYNC)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (onDataRetrieval != null) onDataRetrieval.onSuccess(snapshot.getLong(INFO_LAST_SYNC));

                })
                .addOnFailureListener(e -> {
                    if (onDataRetrieval != null) onDataRetrieval.onFailure();
                });
    }

    /**
     * Override modification date in the cloud database.
     * @param context calling context
     * @param date new date
     */
    static void setModificationDate(Context context, Long date){

        Map<String, Long> map = new HashMap<>();
        map.put(INFO_LAST_SYNC,date);

        DB.collection(USERS)
                .document(User.getCurrentUser(context).getUsername())
                .collection(USER_INFO)
                .document(INFO_LAST_SYNC)
                .set(map,SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("SYNC_NOTES","Data synchronization success"))
                .addOnFailureListener(e -> Log.d("SYNC_NOTES","Data synchronization failure"));
    }

    /**
     * Get the app color theme from the cloud database
     * @param context calling context
     * @param onDataRetrieval post retrieval actions
     */
    static void getAppColor(Context context, OnIntRetrieval onDataRetrieval){
        DB.collection(USERS)
                .document(User.getCurrentUser(context).getUsername())
                .collection(USER_INFO)
                .document(INFO_LAST_SYNC)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (onDataRetrieval != null) onDataRetrieval.onSuccess(Integer.parseInt(Objects.requireNonNull(snapshot.get(INFO_APP_COLOR)).toString()));

                })
                .addOnFailureListener(e -> {
                    if (onDataRetrieval != null) onDataRetrieval.onFailure();
                });
    }

    /**
     * Override app color theme in the cloud database
     * @param context calling context
     * @param color new color theme
     */
    static void setAppColor(Context context, int color){

        Map<String, String> map = new HashMap<>();
        map.put(INFO_APP_COLOR,""+color);

        DB.collection(USERS)
                .document(User.getCurrentUser(context).getUsername())
                .collection(USER_INFO)
                .document(INFO_LAST_SYNC)
                .set(map, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("SYNC_NOTES","Data synchronization success"))
                .addOnFailureListener(e -> Log.d("SYNC_NOTES","Data synchronization failure"));
    }

    /**
     * Get the app lighting theme from the cloud database
     * @param context calling context
     * @param onDataRetrieval post retrieval actions
     */
    static void getAppTheme(Context context, OnIntRetrieval onDataRetrieval){
        DB.collection(USERS)
                .document(User.getCurrentUser(context).getUsername())
                .collection(USER_INFO)
                .document(INFO_LAST_SYNC)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (onDataRetrieval != null) onDataRetrieval.onSuccess(Integer.parseInt(Objects.requireNonNull(snapshot.get(INFO_APP_THEME)).toString()));

                })
                .addOnFailureListener(e -> {
                    if (onDataRetrieval != null) onDataRetrieval.onFailure();
                });
    }

    /**
     * Override app lighting theme with a new value
     * @param context calling context
     * @param theme new lighting theme
     */
    static void setAppTheme(Context context, int theme){

        Map<String, String> map = new HashMap<>();
        map.put(INFO_APP_THEME,""+theme);

        DB.collection(USERS)
                .document(User.getCurrentUser(context).getUsername())
                .collection(USER_INFO)
                .document(INFO_LAST_SYNC)
                .set(map, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("SYNC_NOTES","Data synchronization success"))
                .addOnFailureListener(e -> Log.d("SYNC_NOTES","Data synchronization failure"));
    }

    /**
     * Get current User data from the cloud
     * @param context calling context
     * @param onDataRetrieval post retrieval actions
     */
    static void getUserData(Context context, OnQueryDataRetrieval onDataRetrieval){

        DB.collection(USERS)
                .document(User.getCurrentUser(context)
                .getUsername()).
                collection(USER_NOTES).
                get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (onDataRetrieval != null) onDataRetrieval.onSuccess(queryDocumentSnapshots);
                })
                .addOnFailureListener(e -> {
                    if (onDataRetrieval != null) onDataRetrieval.onFailure();
                });
    }

    /**
     * Save a note to the cloud database
     * @param context calling context
     * @param note note to be saved
     */
    static void setNote(Context context, Note<?> note){

        DB.collection(USERS)
                .document(User.getCurrentUser(context)
                .getUsername()).collection(USER_NOTES)
                .document(note.getUid())
                .set(note.toMap())
                .addOnSuccessListener(aVoid ->
                        Log.d("SYNC","Synchronized successfully note : "+note.getUid()))
                .addOnFailureListener(e ->
                        Log.d("SYNC","Couldn't sync note : "+note.getUid())
                );

    }

    /**
     * @deprecated
     * Retrieve a note from cloud
     * @param context calling context
     * @param uid note uid to be retrieved
     * @param onDataRetrieval post retrieval actions
     */
    static void getNote(Context context, String uid, OnDataRetrieval onDataRetrieval){

        DB.collection(USERS)
                .document(User.getCurrentUser(context).getUsername())
                .collection(USER_NOTES).document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (onDataRetrieval != null) onDataRetrieval.onSuccess(documentSnapshot);
                })
                .addOnFailureListener(e -> {

                    if (onDataRetrieval != null) onDataRetrieval.onFailure();
                });
    }

    /**
     * Wipe User notes in the cloud database
     * @param context calling context
     * @param onDataWiped post data clearing actions
     */
    static void wipeNotes(Context context, OnDataWiped onDataWiped){

        DB.collection(USERS)
                .document(User.getCurrentUser(context).getUsername())
                .collection(USER_NOTES)
                .get()
                .addOnSuccessListener(snapshot -> {
                    ArrayList<String> list = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshot.getDocuments()){
                        list.add(doc.getId());
                    }

                    for (String id: list){
                        DB.collection(USERS)
                                .document(User.getCurrentUser(context).getUsername())
                                .collection(USER_NOTES)
                                .document(id)
                                .delete();
                    }

                    if (onDataWiped != null) onDataWiped.onDataWiped();

                })
                .addOnFailureListener(e -> {

                });
    }

    /**
     * get the preference of auto syncing from the local database
     * @see DatabaseManager
     * @param context calling context
     * @return On : true, Off : false
     */
    static boolean getAutoSync(Context context){
        return true;
//        return DatabaseManager.LoadBoolean(App.KEY_AUTO_SYNC,context);
    }

    /**
     * change auto syncing status
     * @param context calling context
     * @param auto new value
     */
    static void setAutoSync(Context context, boolean auto){
        DatabaseManager.SaveBoolean(auto,App.KEY_AUTO_SYNC,context);
    }

    /**
     * Perform data syncing
     * @param context calling context
     * @param onDataSynced post data syncing actions
     */
    static void performSync(Context context, OnDataSynced onDataSynced){

        getModificationDate(context, new OnLongRetrieval() {
            @Override
            public void onSuccess(Long value) {

                Long localSync = DatabaseManager.getDatabaseLastModificationDate(context);

                if (!localSync.equals(value)) {
                    // firebase firestore database is ahead
                    if (value > localSync){

                        getUserData(context, new OnQueryDataRetrieval() {
                            @Override
                            public void onSuccess(QuerySnapshot snapshot) {

                                DatabaseManager.wipeDatabase(context);

                                // get data from the FirebaseFirestore database
                                ArrayList<Note<?>> notes = new ArrayList<>();

                                for (DocumentSnapshot snap : snapshot.getDocuments()){
                                    Note<?> n = Note.fromMap(context, Objects.requireNonNull(snap.getData()));

                                    if (n != null) {
                                        n.save(context);
                                        notes.add(n);
                                    }
                                }

                                DatabaseManager.SaveStringArray(App.getNotesAsUIDFromList(notes), App.KEY_NOTE_LIST,context);

                                Sync.getAppTheme(context, new OnIntRetrieval() {
                                    @Override
                                    public void onSuccess(int value) {
                                        Style.setAppTheme(value,context);
                                    }

                                    @Override
                                    public void onFailure() {
                                        Log.d("SYNC_NOTES","Unable to get Theme : Firebase database is ahead of the local database ...");
                                    }
                                });

                                Sync.getAppColor(context, new OnIntRetrieval() {
                                    @Override
                                    public void onSuccess(int value) {
                                        Style.setAppColor(value,context);
                                    }

                                    @Override
                                    public void onFailure() {
                                        Log.d("SYNC_NOTES","Unable to get App Color : Firebase database is ahead of the local database ...");
                                    }
                                });

                                DatabaseManager.setDatabaseLastModificationDate(context, value);
                                if (onDataSynced != null) onDataSynced.onDataDownloaded(notes);

                            }

                            @Override
                            public void onFailure() {
                                Log.d("SYNC_NOTES","Unable to sync : Firebase database is ahead of the local database ...");
                            }
                        });

                    }

                    // local database is ahead
                    else {

                        wipeNotes(context, () -> {
                            ArrayList<Note<?>> noteList = DatabaseManager.LoadAllNotes(context);
                            for (Note<?> note : noteList){
                                setNote(context,note);
                            }
                        });

                        setAppTheme(context,Style.getAppTheme(context));
                        setAppColor(context,Style.getAppColor(context));
                        setModificationDate(context, DatabaseManager.getDatabaseLastModificationDate(context));

                        if (onDataSynced != null) onDataSynced.onDataUploaded();

                    }
                }

            }

            @Override
            public void onFailure() {
                Toast.makeText(context, context.getString(R.string.sync_unable), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Perform syncing between local database and cloud storage
     * @param context calling context
     * @param cloudSync cloud last modification date
     * @param localSync local last modification date
     */
    static void performSync(Context context, Long cloudSync, Long localSync){

        // both database are synced correctly
        if (cloudSync.equals(localSync)) return;

        // firebase firestore database is ahead
        if (cloudSync > localSync){

            getUserData(context, new OnQueryDataRetrieval() {
                @Override
                public void onSuccess(QuerySnapshot snapshot) {

                    DatabaseManager.wipeDatabase(context);

                    // get data from the FirebaseFirestore database
                    ArrayList<Note<?>> notes = new ArrayList<>();

                    for (DocumentSnapshot snap : snapshot.getDocuments()){
                        Note<?> n = Note.fromMap(context, Objects.requireNonNull(snap.getData()));

                        if (n != null) {
                            n.save(context);
                            notes.add(n);
                        }
                    }

                    DatabaseManager.SaveStringArray(App.getNotesAsUIDFromList(notes), App.KEY_NOTE_LIST,context);

                    Sync.getAppTheme(context, new OnIntRetrieval() {
                        @Override
                        public void onSuccess(int value) {
                            Style.setAppTheme(value,context);
                        }

                        @Override
                        public void onFailure() {
                            Log.d("SYNC_NOTES","Unable to get Theme : Firebase database is ahead of the local database ...");
                        }
                    });

                    Sync.getAppColor(context, new OnIntRetrieval() {
                        @Override
                        public void onSuccess(int value) {
                            Style.setAppColor(value,context);
                        }

                        @Override
                        public void onFailure() {
                            Log.d("SYNC_NOTES","Unable to get App Color : Firebase database is ahead of the local database ...");
                        }
                    });

                }

                @Override
                public void onFailure() {
                    Log.d("SYNC_NOTES","Unable to sync : Firebase database is ahead of the local database ...");
                }
            });

            DatabaseManager.setDatabaseLastModificationDate(context, cloudSync);

        }

        // local database is ahead
        else {

            wipeNotes(context, () -> {
                ArrayList<Note<?>> noteList = DatabaseManager.LoadAllNotes(context);
                for (Note<?> note : noteList){
                    setNote(context,note);
                }
            });

            setAppTheme(context,Style.getAppTheme(context));
            setAppColor(context,Style.getAppColor(context));
            setModificationDate(context, DatabaseManager.getDatabaseLastModificationDate(context));

        }

    }

}