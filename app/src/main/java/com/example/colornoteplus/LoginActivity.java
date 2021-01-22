package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Allow user to login to his account
 * @see App
 * @see Sync
 * @see DatabaseManager
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Sync.getModificationDate(this, new Sync.OnLongRetrieval() {
            @Override
            public void onSuccess(Long value) {
                Sync.performSync(getApplicationContext(),value,DatabaseManager.getDatabaseLastModificationDate(getApplicationContext()));
            }

            @Override
            public void onFailure() {

            }
        });

        if (DatabaseManager.LoadBoolean(App.KEY_REMEMBER_ME,this)){
            skip();
        }

        initActivity();
    }

    /**
     * Initialize activity layout and views.
     * @see Style
     */
    void initActivity(){

        setContentView(R.layout.activity_login);

        ConstraintLayout background = findViewById(R.id.login_background);
        TextView title = findViewById(R.id.title_text);
        CardView loginCard = findViewById(R.id.bot_section);
        TextView loginText = findViewById(R.id.login_text);
        EditText username = findViewById(R.id.email_field);
        EditText password = findViewById(R.id.password_field);
        Button loginButton = findViewById(R.id.login_button);
        TextView signUpText = findViewById(R.id.sign_up_text);
        TextView rememberMe = findViewById(R.id.remember_me_label);
        CheckBox rememberMeBox = findViewById(R.id.remember_me_box);
        TextView errorText = findViewById(R.id.error_text);

        background.setBackgroundColor(getResources().getColor(Style.getNeutralColor(getApplicationContext())));

        title.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));

        loginCard.setCardBackgroundColor(
                getResources().getColor(
                        Style.getCustomColor(
                                getApplicationContext(),
                                3,
                                Style.COLORS.LIGHTER,
                                Style.COLORS.DARKER)));

        loginText.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));

        password.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));
        password.setBackgroundColor(getResources().getColor(Style.getNeutralColor(getApplicationContext())));
        password.setHintTextColor(getResources().getColor(
                Style.getCustomColor(
                        getApplicationContext(),
                        3,
                        Style.COLORS.LIGHT,
                        Style.COLORS.DARKER
                )
        ));

        username.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));
        username.setBackgroundColor(getResources().getColor(Style.getNeutralColor(getApplicationContext())));
        username.setHintTextColor(getResources().getColor(
                Style.getCustomColor(
                        getApplicationContext(),
                        3,
                        Style.COLORS.LIGHT,
                        Style.COLORS.DARKER
                )
        ));

        loginButton.setBackgroundColor(
                getResources().getColor(
                        Style.getCustomColor(
                                getApplicationContext(),
                                3,
                                Style.COLORS.DARK,
                                Style.COLORS.LIGHT
                        )
                )
        );

        loginButton.setTextColor(
                getResources().getColor(
                        Style.getCustomColor(
                                getApplicationContext(),
                                3,
                                Style.COLORS.WHITE,
                                Style.COLORS.WHITE
                        )
                )
        );

        loginButton.setOnClickListener(v ->
                {

                    LoadingFragment dialog = new LoadingFragment(getString(R.string.logging_in),null);
                    dialog.setCancelable(false);
                    dialog.show(getSupportFragmentManager(),"LOADING");

                    errorText.setVisibility(View.INVISIBLE);
                    Sync.login(
                            username.getText().toString(),
                            password.getText().toString(),
                            new Sync.OnUserLogin() {
                                @Override
                                public void onSuccess(String username) {
                                    DatabaseManager.wipeDatabase(getApplicationContext());
                                    User.setUsername(username, getApplicationContext());
                                    Sync.performSync(getApplicationContext(), 1L, 0L);
                                    dialog.dismiss();
                                    skip();
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(LoginActivity.this, getString(R.string.bad_combination), Toast.LENGTH_SHORT).show();
                                    errorText.setText(R.string.bad_combination);
                                    errorText.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onNetworkError() {
                                    Toast.makeText(LoginActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                                    errorText.setText(R.string.network_error);
                                    errorText.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                }
                            });
                }
        );

        loginButton.setOnLongClickListener(v -> true);

        rememberMe.setTextColor(
                getResources().getColor(Style.getNeutralTextColor(getApplicationContext()))
        );

        rememberMeBox.setChecked(DatabaseManager.LoadBoolean(App.KEY_REMEMBER_ME,getApplicationContext()));
        rememberMeBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                DatabaseManager.SaveBoolean(isChecked,App.KEY_REMEMBER_ME,getApplicationContext()));

        signUpText.setTextColor(
                getResources().getColor(Style.getNeutralTextColor(getApplicationContext()))
        );

        errorText.setVisibility(View.INVISIBLE);
    }

    /**
     * Skip this screen activity.
     * @see SplashScreen
     */
    private void skip(){
        Intent i = new Intent(getApplicationContext(), SplashScreen.class);
        startActivity(i);
        finish();
    }
}
