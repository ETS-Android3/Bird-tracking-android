package com.example.droidsutra;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class FAB_ValidateData extends AppCompatActivity {
    Databasehelper myDb;
    Bird myBird;
    private ImageButton btnAddData;
    private ImageButton btnEditData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab__validate_data);
        myDb = new Databasehelper(this);


        TextView details_entered = (TextView) findViewById(R.id.details_entered);
        String details_of_bird =  getResources().getString(R.string.name)+": " + myBird.name + "\n\n" +
                getResources().getString(R.string.size)+": " + myBird.size + "\n\n" +
                getResources().getString(R.string.clr)+": " + myBird.colour + "\n\n" +
                getResources().getString(R.string.loc)+": " +myBird.location + "\n\n" +
                getResources().getString(R.string.date1)+": " + myBird.date + "\n\n" +
                getResources().getString(R.string.time1)+": "+ myBird.time + "\n\n" +
                getResources().getString(R.string.description1)+": " + myBird.description + "\n\n" +
                getResources().getString(R.string.note)+": "+ myBird.note ;

        details_entered.setText(details_of_bird);

        btnAddData = (ImageButton)findViewById(R.id.button_add);
        AddData();


    }

    public void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean isInserted = myDb.insertData(myBird.name, myBird.location, myBird.size,
                                myBird.colour, myBird.date, myBird.time, myBird.category,
                                myBird.description, myBird.note);
                        if (isInserted == true) {
                            Intent intent = new Intent(FAB_ValidateData.this, FAB_Successful.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else {
                            Intent intent = new Intent(FAB_ValidateData.this, FAB_Unsuccessful.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                }
        );
    }


    public void EditData(View view) {
        Intent intent = new Intent(this, FAB_Attributes_1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBackPressed() {
        (new AlertDialog.Builder(this))
                .setTitle( getResources().getString(R.string.toast_confirm))
                .setMessage( getResources().getString(R.string.toast_home))
                .setPositiveButton( getResources().getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FAB_ValidateData.this, HomeScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton( getResources().getString(R.string.toast_no), null)
                .show();
    }
}