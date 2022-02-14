package com.example.droidsutra;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.example.droidsutra.HomeScreen.Categories;

public class MainActivity extends AppCompatActivity {

    Databasehelper myDb;
    Bird myBird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDb = new Databasehelper(this);
        Cursor res = myDb.getAllData_LanguageTable();

        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                myBird.language_chosen = (res.getString(0));
            }
        }
        else {
            myBird.language_chosen = "en";
            myDb.insertData_LanguageTable("en");
        }

        SetLanguage (myBird.language_chosen);
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
