package com.example.colornoteplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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
        EditText email = findViewById(R.id.email_field);
        EditText password = findViewById(R.id.password_field);
        Button loginButton = findViewById(R.id.login_button);
        TextView signUpText = findViewById(R.id.sign_up_text);
        TextView rememberMe = findViewById(R.id.remember_me_label);
        CheckBox rememberMeBox = findViewById(R.id.remember_me_box);

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

        loginButton.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),SplashScreen.class);
            startActivity(i);
        });

        rememberMe.setTextColor(
                getResources().getColor(Style.getNeutralTextColor(getApplicationContext()))
        );

        rememberMeBox.setChecked(DatabaseManager.LoadBoolean(App.KEY_REMEMBER_ME,getApplicationContext()));
        rememberMeBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                DatabaseManager.SaveBoolean(isChecked,App.KEY_REMEMBER_ME,getApplicationContext()));

        signUpText.setTextColor(
                getResources().getColor(Style.getNeutralTextColor(getApplicationContext()))
        );
    }
}
