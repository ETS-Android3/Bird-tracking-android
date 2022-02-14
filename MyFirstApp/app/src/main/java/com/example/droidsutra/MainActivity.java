package com.example.droidsutra;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Databasehelper myDb;
    static String language_chosen = "en";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetLanguage (language_chosen);
        setContentView(R.layout.activity_main);
    }

    public void HomeScreen(View view) {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void SetLanguage (String language_chosen) {
        String languageToLoad  = language_chosen; // your language
        if (languageToLoad == "de") {
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
