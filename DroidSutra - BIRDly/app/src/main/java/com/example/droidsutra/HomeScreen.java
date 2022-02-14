/* This application can be used to track birds which is useful for bird watchers. A user can store
citation of birds along with different attributes.
   @author   Droidsutra
   @version  1.0.1
*/
package com.example.droidsutra;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

/* The class 'HomeScreen' is used to choose between three activities.This is the first page
user view while launching the app.Here the user can select between two languages english and German.

*/


public class HomeScreen extends AppCompatActivity {

    public static ArrayList<String> Categories = new ArrayList<String>();
    /* This array stores the categories that user enters */
    Databasehelper myDb;
    Bird myBird;
    public static int Categories_Refreshed_Flag; /* This variable will be used to check if the
                                                Categories are refreshed from the Category table */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetLanguage (myBird.language_chosen);
        setContentView(R.layout.homescreen);

        myDb = new Databasehelper(this);
        Cursor res = myDb.getAllData_CategoryTable();

        if (res.getCount() == 0) {
            if  (!HomeScreen.Categories.contains("Default Category") ) {
                Categories.add("Default Category");
                myDb.insertData_CategoryTable("Default Category");
            }
        }


        if (Categories_Refreshed_Flag != 11) {
            if (res.getCount() != 0) {
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    if (!HomeScreen.Categories.contains(res.getString(0))) {
                        Categories.add(res.getString(0));
                    }
                }
                Categories_Refreshed_Flag = 11;
            }
        }
    }
    /*This method is used for transitions from Homescreen to My Observations*/

    public void Found_A_Bird(View view) {
        Intent intent = new Intent(this, FAB_Attributes_1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /*This method is used for transitions from Homescreen to My Observations*/
    public void My_Observations(View view) {
        Intent intent = new Intent(this, OBS_View_Observ.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /*This method is used for transition from Homescreen to My categories*/

    public void My_Categories(View view) {
        Intent intent = new Intent(this, CAT_Select_Options.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void Settings(View view) {
        Intent intent = new Intent(this, Settings_Select_Options.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void SetLanguage (String language_chosen) {
        String languageToLoad  = language_chosen; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
