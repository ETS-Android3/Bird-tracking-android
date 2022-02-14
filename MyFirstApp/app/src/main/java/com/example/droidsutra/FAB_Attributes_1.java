package com.example.droidsutra;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class FAB_Attributes_1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public RadioButton rb_small, rb_medium, rb_large;
    //public static String size;

    AutoCompleteTextView user_editText_name;
    //public static String name;

    EditText user_editText_colour;
    //public static String colour;

    int PLACE_PICKER_REQUEST = 1;
    TextView tvPlace;

    ImageButton btnNameEntered;
    Databasehelper myDb;
    Bird myBird;

    String autofill_size, autofill_colour;

    static boolean autofill_option = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab__attributes_1);


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.FAB_Colours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(FAB_Attributes_1.this);

        user_editText_name = (AutoCompleteTextView)findViewById(R.id.editText_name);
        String[] birds_suggestions = getResources().getStringArray(R.array.Birds_Suggestions);
        ArrayAdapter<String> adapter_bird = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,birds_suggestions);
        user_editText_name.setAdapter(adapter_bird);


        myDb = new Databasehelper(this);
        myBird = new Bird();
        btnNameEntered = (ImageButton) findViewById(R.id.btnNameEntered);
        btnNameEntered.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myBird.name =  (String) user_editText_name.getText().toString();
                        if (TextUtils.isEmpty(myBird.name)) {
                            Toast.makeText(FAB_Attributes_1.this, getResources().getString(R.string.toast_name), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Cursor res = myDb.getNames(myBird.name);
                        if (res.getCount() == 0) {
                            showMessage(getResources().getString(R.string.toast_autofill), getResources().getString(R.string.toast_nobird_withname) + myBird.name +" "+  getResources().getString(R.string.toast_nobird_found),1);
                        }
                        else {
                            showMessage(getResources().getString(R.string.toast_autofill), getResources().getString(R.string.toast_bird_withname) + myBird.name + " " +getResources().getString(R.string.toast_bird_found),2);
                        }

                    }
                }
        );

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        myBird.colour = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void FAB_Attribute_1to2(View view) {
        rb_small = (RadioButton) findViewById(R.id.radioButton_small);
        rb_medium = (RadioButton) findViewById(R.id.radioButton_medium);
        rb_large = (RadioButton) findViewById(R.id.radioButton_large);


        if (rb_small.isChecked()) {
            myBird.size = "Small";
        }
        if (rb_medium.isChecked()) {
            myBird.size = "Medium";
        }
        if (rb_large.isChecked()) {
            myBird.size = "Large";
        }


        String[] colours_array = getResources().getStringArray(R.array.FAB_Colours);
        if (myBird.colour.equals(colours_array[0])) {
            Toast.makeText(FAB_Attributes_1.this,getResources().getString(R.string.toast_clr), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(myBird.size) || TextUtils.isEmpty(myBird.name) || TextUtils.isEmpty(myBird.colour)) {
            Toast.makeText(this, getResources().getString(R.string.toast_enterall), Toast.LENGTH_SHORT).show();
        }
        else {
                Intent intent = new Intent(this, FAB_Attributes_2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = getResources().getString(R.string.toast_place) + place.getName();
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
  }

    public void showMessage(String title, String Message, int Type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FAB_Attributes_1.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        user_editText_name.clearFocus();

        if (Type == 1) {
            builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        if (Type == 2) {
            builder.setPositiveButton(getResources().getString(R.string.yes_auto), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    autofill_option = true;
                    Cursor res = myDb.getData("Name", myBird.name);
                    while (res.moveToNext()) {
                        autofill_size = res.getString(2);
                        autofill_colour = res.getString(3);
                    }

                    rb_small = (RadioButton) findViewById(R.id.radioButton_small);
                    rb_medium = (RadioButton) findViewById(R.id.radioButton_medium);
                    rb_large = (RadioButton) findViewById(R.id.radioButton_large);

                    if (autofill_size.equals("Small") ) {
                        rb_small.setChecked(true);
                    }
                    if (autofill_size.equals("Medium")) {
                        rb_medium.setChecked(true);
                    }
                    if (autofill_size.equals("Large")) {
                        rb_large.setChecked(true);
                    }

                    Spinner spinner = findViewById(R.id.spinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FAB_Attributes_1.this,
                            R.array.FAB_Colours, android.R.layout.simple_spinner_item);
                    if (autofill_colour != null) {
                        int spinnerPosition = adapter.getPosition(autofill_colour);
                        spinner.setSelection(spinnerPosition);
                    }
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.toast_no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    autofill_option = false;
                }
            });
        }

        builder.show();
    }

    public void onBackPressed() {
        (new AlertDialog.Builder(this))
                .setTitle(getResources().getString(R.string.toast_confirm))
                .setMessage(getResources().getString(R.string.toast_home))
                .setPositiveButton(getResources().getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FAB_Attributes_1.this, HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.toast_no), null)
                .show();
    }

}
