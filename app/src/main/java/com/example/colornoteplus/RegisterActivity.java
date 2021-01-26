package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Allow user to register and sign up for the service provided by the app.
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(Style.getAppTheme(this));
        setContentView(R.layout.activity_register);
        initActivity();
    }

    private void initActivity(){

        CardView card = findViewById(R.id.bot_section);
        card.setCardBackgroundColor(
                getResources().getColor(
                        Style.getCustomColor(
                                getApplicationContext(),
                                3,
                                Style.COLORS.LIGHTER,
                                Style.COLORS.DARKER)));

        ConstraintLayout background = findViewById(R.id.register_background);
        background.setBackgroundColor(getResources().getColor(Style.getNeutralColor(getApplicationContext())));

        TextView sectionTitle = findViewById(R.id.register_text);
        sectionTitle.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));

        EditText username = findViewById(R.id.username_field);
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

        EditText email = findViewById(R.id.email_field);
        email.setTextColor(getResources().getColor(Style.getNeutralTextColor(getApplicationContext())));
        email.setBackgroundColor(getResources().getColor(Style.getNeutralColor(getApplicationContext())));
        email.setHintTextColor(getResources().getColor(
                Style.getCustomColor(
                        getApplicationContext(),
                        3,
                        Style.COLORS.LIGHT,
                        Style.COLORS.DARKER
                )
        ));

        EditText password = findViewById(R.id.password_field);
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

        TextView rememberMe = findViewById(R.id.remember_me_label);
        rememberMe.setTextColor(
                getResources().getColor(Style.getNeutralTextColor(getApplicationContext()))
        );

        CheckBox rememberMeBox = findViewById(R.id.remember_me_box);
        rememberMeBox.setChecked(DatabaseManager.getBoolean(App.KEY_REMEMBER_ME,getApplicationContext()));
        rememberMeBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                DatabaseManager.setBoolean(isChecked,App.KEY_REMEMBER_ME,getApplicationContext()));

        TextView errorText = findViewById(R.id.error_text);
        errorText.setTextColor(getResources().getColor(
                Style.getCustomColor(this,4, Style.COLORS.DARKER, Style.COLORS.LIGHTER)));
        errorText.setVisibility(View.GONE);

        TextView loginText = findViewById(R.id.login_text);
        loginText.setTextColor(
                getResources().getColor(Style.getNeutralTextColor(getApplicationContext()))
        );
        loginText.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setTextColor(
                getResources().getColor(
                        Style.getCustomColor(
                                getApplicationContext(),
                                3,
                                Style.COLORS.WHITE,
                                Style.COLORS.WHITE
                        )
                )
        );
        registerButton.setOnClickListener(v -> {
            LoadingFragment loading = new LoadingFragment(getString(R.string.signing_in),null);
            loading.show(getSupportFragmentManager(),App.TAG_DIALOG_CONFIRM);
            errorText.setVisibility(View.GONE);
            Sync.register(
                    username.getText().toString().trim(),
                    password.getText().toString().trim(),
                    email.getText().toString().trim(),
                    new Sync.OnRegister() {
                        @Override
                        public void onSuccess(String userID, String username, String email, String password) {
                            User.setID(userID,getApplicationContext());
                            User.setEmail(getApplicationContext(),email);
                            User.setUsername(username,getApplicationContext());
                            DatabaseManager.setModificationDate(getApplicationContext(),App.getTimeNow());
                            loading.dismiss();
                            Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            proceed();
                        }

                        @Override
                        public void onFailure(int error) {
                            errorText.setText(App.getRegisterException(error,getApplicationContext()));
                            errorText.setVisibility(View.VISIBLE);
                            loading.dismiss();
                            Toast.makeText(RegisterActivity.this, "Could not create account. Exception => "+error, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            loading.dismiss();
                            Toast.makeText(RegisterActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();

                        }
                    }
            );
        });

    }

    /**
     * Proceed to App service
     */
    private void proceed(){
        Intent i = new Intent(this,SplashScreen.class);
        startActivity(i);
    }
}
