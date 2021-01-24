package com.example.colornoteplus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
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
    private static final CollectionReference DB_USERS = FirebaseFirestore.getInstance().collection(USERS);

    private static final String USER_INFO = App.DATABASE_USER_INFO;
    private static final String USER_NOTES = App.DATABASE_USER_NOTES;
    private static final String USER_ID = App.DATABASE_USER_ID;
    private static final String USER_PASSWORD = App.DATABASE_USER_PASSWORD;
    private static final String USER_EMAIL = App.DATABASE_USER_EMAIL;
    private static final String INFO = App.DATABASE_DATA_INFO;
    private static final String INFO_DB = App.DATABASE_DATA_UPDATE_NOTES;
    private static final String INFO_APP_VERSION = App.DATABASE_DATA_APP_VERSION;
    private static final String INFO_LAST_SYNC = App.DATABASE_USER_INFO_LAST_SYNC;
    private static final String INFO_APP_COLOR = App.DATABASE_USER_COLOR;
    private static final String INFO_APP_THEME = App.DATABASE_USER_THEME;

    private static final int ERROR_USERNAME_EXIST = 0;
    private static final int ERROR_EMAIL_EXIST = 1;
    private static final int ERROR_PASSWORD_SHORT = 2;


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
     * Interface for String value retrieval
     */
    public interface OnStringRetrieval{

        /**
         * Executes on success
         * @param value retrieved data
         */
        void onSuccess(String value);

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
     * Interface for user login process
     */
    public interface OnUserLogin{

        /**
         * Executes action after user has been found in the database.
         * @param username user to be logged in
         */
        void onSuccess(String username);

        /**
         * Executes action after failing to retrieve user data.
         */
        void onFailure();

        /**
         * Executes upon failing to connect.
         */
        void onNetworkError();
    }

    /**
     * General interface for data exchange handling
     */
    public interface OnRegister {

        /**
         * On data exchange success
         */
        void onSuccess(String userID, String username, String email, String password);

        /**
         * On data exchange failure
         */
        void onFailure(int error);

        /**
         * On Network Error
         */
        void onError();
    }

    /**
     * get the modification date from the cloud server.
     * @param context calling context
     * @param onDataRetrieval post retrieval actions.
     * @see OnDataRetrieval
     */
    static void getModificationDate(Context context, OnLongRetrieval onDataRetrieval){
        DB.collection(USERS)
                .document(User.getID(context))
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
                .document(User.getID(context))
                .set(map,SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("SYNC_NOTES","Data synchronization success"))
                .addOnFailureListener(e -> Log.d("SYNC_NOTES","Data synchronization failure"));
    }

    /**
     * Get the app color theme from the cloud database
     * @see OnIntRetrieval
     * @param context calling context
     * @param onDataRetrieval post retrieval actions
     */
    static void getAppColor(Context context, OnIntRetrieval onDataRetrieval){
        DB.collection(USERS)
                .document(User.getID(context))
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
                .document(User.getID(context))
                .set(map, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("SYNC_NOTES","Data synchronization success"))
                .addOnFailureListener(e -> Log.d("SYNC_NOTES","Data synchronization failure"));
    }

    /**
     * Get the app lighting theme from the cloud database
     * @see OnIntRetrieval
     * @param context calling context
     * @param onDataRetrieval post retrieval actions
     */
    static void getAppTheme(Context context, OnIntRetrieval onDataRetrieval){
        DB.collection(USERS)
                .document(User.getID(context))
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
                .document(User.getID(context))
                .set(map, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("SYNC_NOTES","Data synchronization success"))
                .addOnFailureListener(e -> Log.d("SYNC_NOTES","Data synchronization failure"));
    }

    /**
     * Get current User data from the cloud
     * @see OnQueryDataRetrieval
     * @param context calling context
     * @param onDataRetrieval post retrieval actions
     */
    static void getUserData(Context context, OnQueryDataRetrieval onDataRetrieval){

        DB.collection(USERS)
                .document(User.getID(context))
                .collection(USER_NOTES).
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
     * @see Note
     * @param context calling context
     * @param note note to be saved
     */
    static void setNote(Context context, Note<?> note){

        DB.collection(USERS)
                .document(User.getID(context))
                .collection(USER_NOTES)
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
     * @see OnDataRetrieval
     * @param context calling context
     * @param uid note uid to be retrieved
     * @param onDataRetrieval post retrieval actions
     */
    static void getNote(Context context, String uid, OnDataRetrieval onDataRetrieval){

        DB.collection(USERS)
                .document(User.getID(context))
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
     * @see OnDataWiped
     * @param context calling context
     * @param onDataWiped post data clearing actions
     */
    static void wipeNotes(Context context, OnDataWiped onDataWiped){

        DB.collection(USERS)
                .document(User.getID(context))
                .collection(USER_NOTES)
                .get()
                .addOnSuccessListener(snapshot -> {
                    ArrayList<String> list = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshot.getDocuments()){
                        list.add(doc.getId());
                    }

                    for (String id: list){
                        DB.collection(USERS)
                                .document(User.getID(context))
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
     * @return On : true, Off : false
     */
    static boolean getAutoSync(){
        return true;
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
     * @see OnDataSynced
     * @param context calling context
     * @param onDataSynced post data syncing actions
     */
    static void performSync(Context context, OnDataSynced onDataSynced){

        getModificationDate(context, new OnLongRetrieval() {
            @Override
            public void onSuccess(Long value) {

                if (value == null) {
                    performSync(context, 0L, 1L);
                    return;
                }

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

                    getUserEmail(context, new OnStringRetrieval() {
                        @Override
                        public void onSuccess(String value) {
                            User.setEmail(context,value);
                        }

                        @Override
                        public void onFailure() {
                            Log.d("SYNC_NOTES","Unable to get user email ...");

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

    /**
     * retrieve user email from firebase firestore database.
     * @param context calling context
     * @param onRetrieval post retrieval actions
     * @see OnStringRetrieval
     */
    static void getUserEmail(Context context, OnStringRetrieval onRetrieval){
        DB_USERS.document(User.getID(context))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (onRetrieval != null) onRetrieval.onSuccess(documentSnapshot.getString(USER_EMAIL));
                })
                .addOnFailureListener(e -> {
                    if (onRetrieval != null) onRetrieval.onFailure();
                });
    }

    /**
     * Receive and store update notes from the firebase firestore database.
     * @param context calling context
     * @see DatabaseManager
     * @see App
     */
    static void getUpdateNotes(Context context){

        DB.collection(INFO).document(INFO_DB)
                .get()
                .addOnSuccessListener(snapshot -> {

                    String patchNotes = snapshot.getString(INFO_DB);

                    DatabaseManager.SaveString(patchNotes,App.KEY_PATCH_NOTES,context);

                })
                .addOnFailureListener(e -> {

                });

    }

    /**
     * Try to login user with passed parameters.
     * @see User
     * @see LoginActivity
     * @see OnUserLogin
     * @param username user name
     * @param password user password
     * @param onUserLogin interface for request handling
     */
    static void login(String username, String password, OnUserLogin onUserLogin){

        DB_USERS.get()
                .addOnSuccessListener(snapshot -> {

                    int size = snapshot.size();
                    for (int i = 0; i <= size; i++){
                        if (i == size){
                            Log.d("LOGIN","Bad combination : username not found");
                            if (onUserLogin != null) onUserLogin.onFailure();
                        }
                        else {
                            if (snapshot.getDocuments().get(i).getString(USER_ID).equals(username)
                            && snapshot.getDocuments().get(i).getString(USER_PASSWORD).equals(password)){
                                if (onUserLogin != null) onUserLogin.onSuccess(snapshot.getDocuments().get(i).getId());
                            }
                            else {
                                if (onUserLogin != null) onUserLogin.onFailure();
                            }
                            break;
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    if (onUserLogin != null) onUserLogin.onNetworkError();
                });

    }

    /**
     * Try to create a user in the cloud database
     * @see OnRegister
     * @param username new account username
     * @param password new account password
     * @param email new account email
     * @param onAction post-trial actions
     */
    static void register(String username, String password, String email, OnRegister onAction){

        if (!App.checkPassword(password)){
            if (onAction != null) onAction.onFailure(ERROR_PASSWORD_SHORT);
            Log.d("REGISTER","bad password");
            return;
        }

        DB_USERS.get()
                .addOnSuccessListener(snapshot -> {

                    for (DocumentSnapshot doc : snapshot.getDocuments()){
                        if (doc.getString(USER_ID).equals(username)){
                            if (onAction != null) onAction.onFailure(ERROR_USERNAME_EXIST);
                            Log.d("REGISTER","existing username");
                            return;
                        }
                        else if (doc.getString(USER_EMAIL).equals(email)){
                            if (onAction != null) onAction.onFailure(ERROR_EMAIL_EXIST);
                            Log.d("REGISTER","existing email");
                            return;
                        }
                    }

                    Map<String, java.lang.Object> user = new HashMap<>();
                    user.put(USER_ID,username);
                    user.put(USER_EMAIL,email);
                    user.put(USER_PASSWORD,password);
                    user.put(INFO_APP_COLOR,"0");
                    user.put(INFO_APP_THEME,"0");
                    user.put(INFO_LAST_SYNC,App.getTimeNow());

                    DB_USERS.add(user).addOnSuccessListener(documentReference -> {

                        Log.d("REGISTER","creating new user");
                        if (onAction != null) onAction.onSuccess(documentReference.getId(),username,email,password);

                        documentReference.collection(USER_NOTES);

                    });


                })
                .addOnFailureListener(e -> {
                    if (onAction != null) onAction.onError();
                });

    }

}