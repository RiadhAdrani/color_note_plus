package com.example.colornoteplus;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

/**
 * Activity allowing the user to change global or specific settings in the app
 * @see Style
 */
public class SettingsActivity extends Activity {

    /**
     * App theme
     */
    private int theme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        theme = Style.getAppColor(getApplicationContext());

        ApplyTheme(theme);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Sync.performSync(this,null);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();

        super.onBackPressed();
    }

    /**
     * Apply current theme and build the layout
     * @param theme current app theme
     */
    void ApplyTheme(int theme){

        // Theme & Layout
        setTheme(Style.getTheme(getApplicationContext(),theme));
        setContentView(R.layout.activity_settings);

        // Status bar
        getWindow().setStatusBarColor(getResources().getColor(Style.getColorMain(getApplicationContext(),theme)));

        // background
        ConstraintLayout background = findViewById(R.id.settings_background);
        background.setBackgroundColor(
                getResources().getColor(Style.getNeutralColor(getApplicationContext()))
        );

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
        toolbar.setBackgroundColor(getResources().getColor(Style.getColorMain(getApplicationContext(),theme)));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener( v -> {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        });

        // App Theme
        TextView lightSwitchLabel = findViewById(R.id.settings_theme_label);
        lightSwitchLabel.setTextColor(getResources().getColor(
                Style.getColorPrimaryAccent(getApplicationContext(), theme)
        ));

        SwitchCompat lightSwitch = findViewById(R.id.settings_theme_switch);
        lightSwitch.setChecked(Style.getAppTheme(getApplicationContext()) != App.DAY_THEME);
        lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                Style.setAppTheme(App.NIGHT_THEME, getApplicationContext());
            }
            else {
                Style.setAppTheme(App.DAY_THEME, getApplicationContext());
            }

            ApplyTheme(theme);
        });

        // App Color
        TextView appColorLabel = findViewById(R.id.settings_app_color_label);
        appColorLabel.setTextColor(getResources().getColor(
                Style.getColorPrimaryAccent(getApplicationContext(), theme)
        ));

        ImageView appColor = findViewById(R.id.settings_app_color);
        appColor.setBackgroundResource(Style.getBackground(getApplicationContext(), theme));
        appColor.setOnClickListener( v -> buildColorPickDialog());

        // Auto Sync
        TextView autoSyncLabel = findViewById(R.id.settings_auto_sync_label);
        autoSyncLabel.setTextColor(getResources().getColor(
                Style.getColorPrimaryAccent(getApplicationContext(), theme)
        ));

        SwitchCompat autoSyncSwitch = findViewById(R.id.settings_auto_sync_switch);
        autoSyncSwitch.setChecked(Sync.getAutoSync());
        autoSyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Sync.setAutoSync(getApplicationContext(), isChecked);
            Log.d("AUTO_SYNC", "Auto Sync is :" + Sync.getAutoSync());
            ApplyTheme(theme);
        });
    }

    /**
     * Allow the user to pick a color from a list
     * @see FragmentPickColor
     * @see Style
     */
    private void buildColorPickDialog(){

        FragmentPickColor fragment = new FragmentPickColor(new ColorAdapter(),5,theme);

        fragment.show(getSupportFragmentManager(), App.TAG_FRAGMENT_COLOR_PICK);

        fragment.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
            @Override
            public void OnClickListener(int position) {

                Style.setAppColor(position,getApplicationContext());
                ApplyTheme(position);
                fragment.dismiss();
            }

            @Override
            public void OnLongClickListener(int position) {

            }
        });

    }

}