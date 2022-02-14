package com.example.droidsutra;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.droidsutra.Databasehelper.dir;

public class Settings_Select_Options extends AppCompatActivity {

    Databasehelper myDb;
    Bird myBird;
    public RadioButton rb_en, rb_de;
    public ToggleButton gps_on_off;
    public int refresh_flag_en, refresh_flag_de;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SetLanguage(MainActivity.language_chosen);
        setContentView(R.layout.activity_settings__select__options);
        myDb = new Databasehelper(this);
        myBird = new Bird();


        Button btnBack;
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setVisibility(View.INVISIBLE);

        gps_on_off = (ToggleButton) findViewById(R.id.gps_on_off);

        if (myBird.FAB_Attributes_2_to_GPS_Perm == true) {
            btnBack.setVisibility(View.VISIBLE);
        }

        if (myBird.gps_persmission == true) {
            gps_on_off.setChecked(true);
        }
        else {
            gps_on_off.setChecked(false);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBird.FAB_Attributes_2_to_GPS_Perm = false;
                finish();
            }
        });




        if (refresh_flag_en == 11) {
            rb_en.setChecked(true);
        }

        if (refresh_flag_de == 11) {
            rb_de.setChecked(true);
        }

        rb_en = (RadioButton) findViewById(R.id.radioButton_en);
        rb_de = (RadioButton) findViewById(R.id.radioButton_de);


        rb_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_en.isChecked()) {
                    MainActivity.language_chosen = "en";
                    Toast.makeText(Settings_Select_Options.this,
                            MainActivity.language_chosen, Toast.LENGTH_SHORT).show();
                    refresh_flag_en = 11;
                    SetLanguage(MainActivity.language_chosen);


                }
            }
        });


        rb_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_de.isChecked()) {
                    MainActivity.language_chosen = "de";
                    Toast.makeText(Settings_Select_Options.this,
                            MainActivity.language_chosen, Toast.LENGTH_SHORT).show();
                    refresh_flag_de = 11;
                    SetLanguage(MainActivity.language_chosen);


                }
            }
        });




        ToggleButton toggle = (ToggleButton) findViewById(R.id.gps_on_off);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(Settings_Select_Options.this,
                            getResources().getString(R.string.gps_e), Toast.LENGTH_SHORT).show();
                    myBird.gps_persmission = true;
                } else {
                    Toast.makeText(Settings_Select_Options.this,
                            getResources().getString(R.string.gps_d), Toast.LENGTH_SHORT).show();
                    myBird.gps_persmission = false;
                }
            }
        });

    }







    public void Export_Observations(View view) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(Settings_Select_Options.this,  getResources().getString(R.string.no_obs_found),Toast.LENGTH_SHORT).show();
            return;
        }



        boolean isExported;
        isExported = myDb.ExportNow(view);

        if (isExported == true) {
            Toast.makeText(Settings_Select_Options.this,
                    getResources().getString(R.string.obs_exp)  +"\n" + dir, Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(Settings_Select_Options.this,
                    getResources().getString(R.string.cant_write), Toast.LENGTH_SHORT).show();
        }

    }


    public void SetLanguage (String language_chosen) {
        String languageToLoad  = language_chosen; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Intent intent = new Intent(Settings_Select_Options.this, Settings_Select_Options.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void onBackPressed() {
        if (myBird.FAB_Attributes_2_to_GPS_Perm == true) {
            finish();
        }
        else {
            Intent intent = new Intent(Settings_Select_Options.this, HomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    public void DeleteObservations(View view)
    {
        Date d = new Date();
        CharSequence current_date  = DateFormat.format("dd/MM/yyyy ", d.getTime());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        Date result = cal.getTime();
        CharSequence old_date  = DateFormat.format("dd/MM/yyyy ", result.getTime());
        showMessage( getResources().getString(R.string.del_obs),
                getResources().getString(R.string.del_obs_6months) +
                        "\n["+getResources().getString(R.string.obs_on) + old_date.toString().trim() + "]", old_date.toString());

    }


    public void showMessage(String title, final String Message, final String old_date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings_Select_Options.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);

        builder.setPositiveButton(getResources().getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Cursor res = myDb.getData_mutlipleAttr("Date <" + old_date);
                if (res.getCount() == 0) {
                    Toast.makeText(Settings_Select_Options.this, getResources().getString(R.string.no_old_obs),Toast.LENGTH_SHORT).show();
                }
                else {
                    myDb.deleteData_ObservationsTable(old_date);
                    Toast.makeText(Settings_Select_Options.this, getResources().getString(R.string.old_obs_del),Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.toast_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

}
