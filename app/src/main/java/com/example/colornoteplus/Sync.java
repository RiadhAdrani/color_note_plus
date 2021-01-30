package com.example.colornoteplus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
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

    public static final int ERROR_USERNAME_EXIST = 0;
    public static final int ERROR_EMAIL_EXIST = 1;
    public static final int ERROR_PASSWORD_BAD = 2;
    public static final int ERROR_USERNAME_BAD = 3;

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
     * Another interface for data synchronization process.
     * Allow for more flexibility.
     */
    public interface OnDataSynchronization{

        /**
         * Executes when the process connects to the firebase firestore database.
         */
        void onStart();

        /**
         * Executes when the two databases are synced correctly
         */
        void onSynced();

        /**
         * Executes when the cloud database is behind the local database
         */
        void onUploaded();

        /**
         * Executes when the cloud database is ahead of the local database
         */
        void onDownloaded(int theme, int color, String username, String email, ArrayList<Note<?>> notes);

        /**
         * Executes when the process encountered a network error
         */
        void onNetworkError();

        /**
         * Executes after a time out delay.
         */
        void onTimeOut();

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
        DatabaseManager.setBoolean(auto,App.KEY_AUTO_SYNC,context);
    }

    /**
     * performSync V3:
     * A more flexible and effective method to perform synchronization.
     * Allow to handle event for various situation.
     * @implNote onDataSynchronization.onSync executes after onUploaded and after onDownloaded too.
     * @see OnDataSynchronization
     * @param context calling context
     * @param timeout timeout period, after this delay, onDataSynchronization.onTimeout will be executed regardless of whether the process completed or not.
     * @param onDataSynchronization interface to handle various events.
     */
    static void performSync(Context context, int timeout, OnDataSynchronization onDataSynchronization ){

        if (User.getID(context).equals(App.NO_ID))
            return;

        final boolean[] done = {false};

        Handler handler=new Handler();
        handler.postDelayed(() -> {

            if (!done[0]){
                if (onDataSynchronization != null) onDataSynchronization.onTimeOut();
                return;
            }

        },timeout);

        DB_USERS.document(User.getID(context))
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (onDataSynchronization != null) onDataSynchronization.onStart();

                    Long cloud = snapshot.getLong(INFO_LAST_SYNC);
                    Long local = DatabaseManager.getModificationDate(context);

                    if (cloud == null)
                        return;

                    if (cloud.equals(local)){
                        if (onDataSynchronization != null) onDataSynchronization.onSynced();
                    }

                    if (cloud > local){

                        int color = Integer.parseInt(Objects.requireNonNull(snapshot.getString(INFO_APP_COLOR)));
                        Style.setAppColor(color,context);

                        int theme = Integer.parseInt(Objects.requireNonNull(snapshot.getString(INFO_APP_THEME)));
                        Style.setAppTheme(theme,context);

                        String email = snapshot.getString(USER_EMAIL);
                        User.setEmail(context, email);

                        String username = snapshot.getString(USER_ID);
                        User.setUsername(username,context);

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

                                DatabaseManager.setStringArray(App.getNotesAsUIDFromList(notes), App.KEY_NOTE_LIST,context);

                                DatabaseManager.setModificationDate(context, cloud);
                                if (onDataSynchronization != null) onDataSynchronization.onDownloaded(theme,color,username,email,notes);
                                if (onDataSynchronization != null) onDataSynchronization.onSynced();
                                done[0] = true;

                            }

                            @Override
                            public void onFailure() {
                                if (onDataSynchronization != null) onDataSynchronization.onNetworkError();
                                Log.d("SYNC_NOTES","Unable to sync : Firebase database is ahead of the local database ...");
                            }
                        });

                    }

                    else {

                        wipeNotes(context, () -> {
                            ArrayList<Note<?>> noteList = DatabaseManager.getAllNotes(context);
                            for (Note<?> note : noteList){
                                setNote(context,note);
                            }
                        });

                        setAppTheme(context,Style.getAppTheme(context));
                        setAppColor(context,Style.getAppColor(context));
                        setModificationDate(context, DatabaseManager.getModificationDate(context));

                        if (onDataSynchronization != null) onDataSynchronization.onUploaded();
                        if (onDataSynchronization != null) onDataSynchronization.onSynced();
                        done[0] = true;

                    }

                })
                .addOnFailureListener(e -> {
                    if (onDataSynchronization != null) onDataSynchronization.onNetworkError();
                    done[0] = true;
                });

    }

    /**
     * Perform data syncing
     * @see OnDataSynced
     * @param context calling context
     * @param onDataSynced post data syncing actions
     */
    static void performSync(Context context, OnDataSynced onDataSynced){

        if (User.getID(context).equals(App.NO_ID))
            return;

        getModificationDate(context, new OnLongRetrieval() {
            @Override
            public void onSuccess(Long value) {

                Long localSync = DatabaseManager.getModificationDate(context);

                if (value == null && localSync == 0L)
                    return;

                if (value == null) {
                    performSync(context, 0L, 1L);
                    return;
                }

                if (!localSync.equals(value)) {
                    // firebase firestore database is ahead
                    if (value > localSync){

                        Sync.getAppTheme(context, new OnIntRetrieval() {
                            @Override
                            public void onSuccess(int value) {
                                Toast.makeText(context, "App Theme Retrieved", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "App Color Retrieved", Toast.LENGTH_SHORT).show();
                                Style.setAppColor(value,context);
                            }

                            @Override
                            public void onFailure() {
                                Log.d("SYNC_NOTES","Unable to get App Color : Firebase database is ahead of the local database ...");
                            }
                        });

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

                                DatabaseManager.setStringArray(App.getNotesAsUIDFromList(notes), App.KEY_NOTE_LIST,context);

                                DatabaseManager.setModificationDate(context, value);
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
                            ArrayList<Note<?>> noteList = DatabaseManager.getAllNotes(context);
                            for (Note<?> note : noteList){
                                setNote(context,note);
                            }
                        });

                        setAppTheme(context,Style.getAppTheme(context));
                        setAppColor(context,Style.getAppColor(context));
                        setModificationDate(context, DatabaseManager.getModificationDate(context));

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
     * @deprecated
     * @param context calling context
     * @param cloudSync cloud last modification date
     * @param localSync local last modification date
     */
    static void performSync(Context context, Long cloudSync, Long localSync){

        if (User.getID(context).equals(App.NO_ID))
            return;

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

                    DatabaseManager.setStringArray(App.getNotesAsUIDFromList(notes), App.KEY_NOTE_LIST,context);

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

            DatabaseManager.setModificationDate(context, cloudSync);

        }

        // local database is ahead
        else {

            wipeNotes(context, () -> {
                ArrayList<Note<?>> noteList = DatabaseManager.getAllNotes(context);
                for (Note<?> note : noteList){
                    setNote(context,note);
                }
            });

            setAppTheme(context,Style.getAppTheme(context));
            setAppColor(context,Style.getAppColor(context));
            setModificationDate(context, DatabaseManager.getModificationDate(context));

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

                    DatabaseManager.setString(patchNotes,App.KEY_PATCH_NOTES,context);

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

                    for (int i = 0; i < size; i++){


                        String _username = snapshot.getDocuments().get(i).getString(USER_ID);
                        String _password = snapshot.getDocuments().get(i).getString(USER_PASSWORD);

                        if (_password == null || _username == null)
                            continue;

                        if (_username.equals(username) &&  _password.equals(password)){

                            Log.d("SYNC_LOGIN","user("+i+") name= "+_username);
                            Log.d("SYNC_LOGIN","user("+i+") password= "+_password);

                            if (onUserLogin != null) {

                                onUserLogin.onSuccess(snapshot.getDocuments().get(i).getId());
                                return;

                            }
                        }
                    }

                    if (onUserLogin != null) onUserLogin.onFailure();

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
            if (onAction != null) onAction.onFailure(ERROR_PASSWORD_BAD);
            Log.d("REGISTER","bad password");
            return;
        }

        if (!App.checkUsername(username)){
            if (onAction != null) onAction.onFailure(ERROR_USERNAME_BAD);
            Log.d("REGISTER","bad username");
            return;
        }

        DB_USERS.get()
                .addOnSuccessListener(snapshot -> {

                    for (DocumentSnapshot doc : snapshot.getDocuments()){
                        if (Objects.equals(doc.getString(USER_ID), username)){
                            if (onAction != null) onAction.onFailure(ERROR_USERNAME_EXIST);
                            Log.d("REGISTER","existing username");
                            return;
                        }
                    }

                    for (DocumentSnapshot doc : snapshot.getDocuments()){
                        if (Objects.equals(doc.getString(USER_EMAIL), email)){
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