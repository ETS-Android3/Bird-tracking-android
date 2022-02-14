package com.example.droidsutra;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;

public class FAB_Attributes_2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner user_spinner_category;
    //public static String category;

    TextView user_editText_date;
    //public static String date;

    EditText user_editText_time;
    //public static String time;

    AutoCompleteTextView user_editText_location;
    //public static String location;

    EditText user_editText_description;
    //public static String description;

    EditText user_editText_note;
    //public static String note;

    EditText etdate;
    DatePickerDialog.OnDateSetListener setListener;

    EditText timeBtn;

    public static int EditData_called = 0;

    int PLACE_PICKER_REQUEST = 1;
    TextView tvPlace;

    Bird myBird;

    ImageButton btn_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab__attributes_2);

        myBird = new Bird();

        Spinner spinner = findViewById(R.id.spinner_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FAB_Attributes_2.this, android.R.layout.simple_list_item_1,
                HomeScreen.Categories);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(FAB_Attributes_2.this);

        user_editText_location = (AutoCompleteTextView) findViewById(R.id.editText_location);
        etdate = findViewById(R.id.etdate);
        timeBtn = (EditText) findViewById(R.id.timeBtn);
        user_editText_description = (EditText)findViewById(R.id.editText_description);
        user_editText_note = (EditText)findViewById(R.id.editText_note);
        user_editText_date = (TextView) findViewById(R.id.etdate);
        user_editText_time = (EditText)findViewById(R.id.timeBtn);

        if (myBird.refresh_attributes == true) {
            user_editText_location.setText(myBird.location);
            user_editText_date.setText(myBird.date);
            user_editText_time.setText(myBird.time);
            if (!TextUtils.isEmpty(myBird.category)) {
                int spinnerPosition = adapter.getPosition(myBird.category);
                spinner.setSelection(spinnerPosition);
            }
            user_editText_description.setText(myBird.description);
            user_editText_note.setText(myBird.note);
            myBird.refresh_attributes = false;
        }



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FAB_Attributes_2.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month = month + 1;
                                String month_string;
                                String day_string;
                                if (month <= 9) {
                                    month_string = "0" + month;
                                }
                                else {
                                    month_string = "" + month;
                                }

                                if (day <= 9) {
                                    day_string = "0" + day;
                                }
                                else {
                                    day_string = "" + day;
                                }
                                String date = day_string + "/" + month_string + "/" + year;
                                myBird.date = date;
                                etdate.setText(myBird.date);
                            }
                        },year,month,day);
                datePickerDialog.show();

            }
        });


        btn_location = (ImageButton)findViewById(R.id.btn_location);

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPlacePicker();
                myBird.refresh_attributes = true;
            }
        });

    }


    public void FAB_Attribute_2toValidateData(View view) {
        attr_setText();
        myBird.location = (String) user_editText_location.getText().toString();
        if (TextUtils.isEmpty(myBird.category) || TextUtils.isEmpty(myBird.date) || TextUtils.isEmpty(myBird.time) ||
                 TextUtils.isEmpty(myBird.description) || TextUtils.isEmpty(myBird.location)) {
            Toast.makeText(this, getResources().getString(R.string.toast_enterall), Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, FAB_ValidateData.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
    }

    public void attr_setText(){
        myBird.date = (String) user_editText_date.getText().toString();
        myBird.time =  (String) user_editText_time.getText().toString();
        myBird.description = (String) user_editText_description.getText().toString();
        myBird.note = (String) user_editText_note.getText().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        myBird.category = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setTime(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(FAB_Attributes_2.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                        String hour_string;
                        String minute_string;
                        if (minute <= 9) {
                            minute_string = "0" + minute;
                        }
                        else {
                            minute_string = "" + minute;
                        }

                        if (hourOfDay <= 9) {
                            hour_string = "0" + hourOfDay;
                        }
                        else {
                            hour_string = "" + hourOfDay;
                        }

                        String time = (hour_string + ":" + minute_string);
                        myBird.time = time;
                        timeBtn.setText(myBird.time);
                    }
                }, hour, minute, true);
        timePickerDialog.show();


    }

    public void onBackPressed() {
        (new AlertDialog.Builder(this))
                .setTitle(getResources().getString(R.string.toast_confirm))
                .setMessage(getResources().getString(R.string.toast_home))
                .setPositiveButton(getResources().getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FAB_Attributes_2.this, HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.toast_no), null)
                .show();
    }


    public void goPlacePicker() {
        if (myBird.gps_persmission == true) {
            if (isLocationEnabled(this) == false) {
                Toast.makeText(FAB_Attributes_2.this, getResources().getString(R.string.toast_gps), Toast.LENGTH_LONG).show();
                return;
            } else {
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        } else {
            showMessage( getResources().getString(R.string.toast_gpsp),  getResources().getString(R.string.toast_gpsps));
        }

        attr_setText();


    }

    public void refreshLocation(View view) {
        user_editText_location.setText(myBird.location);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(FAB_Attributes_2.this, data);
                Toast.makeText(this,  getResources().getString(R.string.toast_err), Toast.LENGTH_SHORT).show();
                tvPlace.setText(place.getAddress());
            }
        }


    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public void showMessage(String title, final String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FAB_Attributes_2.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);

        builder.setPositiveButton( getResources().getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                myBird.FAB_Attributes_2_to_GPS_Perm = true;
                Intent intent = new Intent(FAB_Attributes_2.this, Settings_Select_Options.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        builder.setNegativeButton( getResources().getString(R.string.toast_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        builder.show();
    }

}


