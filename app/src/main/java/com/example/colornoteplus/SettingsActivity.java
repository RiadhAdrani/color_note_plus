package com.example.colornoteplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private int theme = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        theme = StyleManager.getAppColor(getApplicationContext());

        ApplyTheme(theme);

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();

        super.onBackPressed();
    }

    void ApplyTheme(int theme){

        setTheme(StyleManager.getTheme(theme));
        setContentView(R.layout.activity_settings);

        getWindow().setStatusBarColor(getResources().getColor(StyleManager.getColorMain(theme)));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(StyleManager.getColorMain(theme)));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener( v -> {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        });

        SwitchCompat lightSwitch = findViewById(R.id.settings_theme_switch);
        lightSwitch.setHighlightColor(getResources().getColor(StyleManager.getColorMain(theme)));

        ImageView appColor = findViewById(R.id.settings_app_color);
        appColor.setBackgroundResource(StyleManager.getBackground(theme));
        appColor.setOnClickListener( v -> buildColorPickDialog());
    }

    // build the color picker dialog
    private void buildColorPickDialog(){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,theme);

        fragment.show(getSupportFragmentManager(),Statics.TAG_FRAGMENT_COLOR_PICK);

        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {

                StyleManager.setAppColor(position,getApplicationContext());
                ApplyTheme(position);
                fragment.dismiss();
            }

            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

}