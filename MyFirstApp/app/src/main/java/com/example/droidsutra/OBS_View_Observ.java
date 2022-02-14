package com.example.droidsutra;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OBS_View_Observ extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnViewObs;
    public String attr_value, name_input, category_input, location_input, where_clause_name, where_clause_category, where_clause_location;
    static String where_clause;
    static boolean name_attr_selected = false;
    static boolean category_attr_selected = false;
    static boolean location_attr_selected = false;
    static boolean view_all_obs_selected = false;

    Databasehelper myDb;
    public String attr_name;

    CheckBox checkBox_name, checkBox_category, checkBox_location, checkBox_viewAllObs;
    EditText name_attrValue, category_attrValue, location_attrValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs__view_observ);

        myDb = new Databasehelper(this);
        btnViewObs = (Button) findViewById(R.id.btnViewObs);

        viewObservations();

        name_attrValue = (EditText)findViewById(R.id.name_attrValue);
        category_attrValue = (EditText)findViewById(R.id.category_attrValue);
        location_attrValue = (EditText)findViewById(R.id.location_attrValue);

        checkBox_name = (CheckBox)findViewById(R.id.checkBox_name);
        checkBox_category = (CheckBox)findViewById(R.id.checkBox_category);
        checkBox_location = (CheckBox)findViewById(R.id.checkBox_location);
        checkBox_viewAllObs = (CheckBox)findViewById(R.id.checkBox_viewAllObs);

        checkBox_name.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBox_name.isChecked()) {
                            name_attrValue.setVisibility(View.VISIBLE);
                            name_attr_selected = true;
                            checkBox_viewAllObs.setChecked(false);
                        }
                        else {
                            name_attrValue.setVisibility(View.INVISIBLE);
                            name_attrValue.setText("");
                            name_attr_selected = false;
                        }
                    }
                }
        );

        checkBox_category.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBox_category.isChecked()) {
                            category_attrValue.setVisibility(View.VISIBLE);
                            category_attr_selected = true;
                            checkBox_viewAllObs.setChecked(false);
                        }
                        else {
                            category_attrValue.setVisibility(View.INVISIBLE);
                            category_attrValue.setText("");
                            category_attr_selected = false;
                        }
                    }
                }
        );

        checkBox_location.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBox_location.isChecked()) {
                            location_attrValue.setVisibility(View.VISIBLE);
                            location_attr_selected = true;
                            checkBox_viewAllObs.setChecked(false);
                        }
                        else {
                            location_attrValue.setVisibility(View.INVISIBLE);
                            location_attrValue.setText("");
                            location_attr_selected = false;
                        }
                    }
                }
        );

        checkBox_viewAllObs.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBox_viewAllObs.isChecked()) {
                            checkBox_name.setChecked(false);
                            checkBox_category.setChecked(false);
                            checkBox_location.setChecked(false);

                            name_attrValue.setVisibility(View.INVISIBLE);
                            name_attrValue.setText("");
                            category_attrValue.setVisibility(View.INVISIBLE);
                            category_attrValue.setText("");
                            location_attrValue.setVisibility(View.INVISIBLE);
                            location_attrValue.setText("");

                            name_attr_selected = false;
                            category_attr_selected = false;
                            location_attr_selected = false;
                            view_all_obs_selected = true;
                        }
                    }
                }
        );

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        attr_name = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OBS_View_Observ.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void viewObservations() {
        btnViewObs.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res;
                        String dialog_title;

                        if (view_all_obs_selected == true) {
                            res = myDb.getAllData();
                            dialog_title = (getResources().getString(R.string.disp_all));
                        }
                        else {
                            name_input = (String) name_attrValue.getText().toString();
                            category_input = (String) category_attrValue.getText().toString();
                            location_input = (String) location_attrValue.getText().toString();
                            if (name_attr_selected == false && category_attr_selected == false &&
                                    location_attr_selected == false) {
                                Toast.makeText(OBS_View_Observ.this,(getResources().getString(R.string.att_plz)), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if ((name_attr_selected == true && TextUtils.isEmpty(name_input))  ||
                                    (category_attr_selected == true && TextUtils.isEmpty(category_input)) ||
                                    (location_attr_selected == true && TextUtils.isEmpty(location_input)))
                            {
                                Toast.makeText(OBS_View_Observ.this,getResources().getString(R.string.plz_ent_att), Toast.LENGTH_SHORT).show();
                                return;

                            }


                            if (TextUtils.isEmpty(name_input)) {
                                where_clause_name = "";
                            }
                            else {
                                if ((TextUtils.isEmpty(category_input)) && (TextUtils.isEmpty(location_input))) {
                                    where_clause_name = "Name = '" + name_input + "'";
                                } else {
                                    where_clause_name = "Name = '" + name_input + "' AND ";
                                }
                            }

                            if (TextUtils.isEmpty(category_input)) {
                                where_clause_category = "";
                            }
                            else {
                                if (TextUtils.isEmpty(location_input)) {
                                    where_clause_category = "Category = '" + category_input + "'";
                                } else {
                                    where_clause_category = "Category = '" + category_input + "' AND ";
                                }
                            }

                            if (TextUtils.isEmpty(location_input)) {
                                where_clause_location = "";
                            }
                            else {
                                where_clause_location = "Location = '" + location_input + "'";
                            }

                            where_clause = where_clause_name + where_clause_category + where_clause_location;

                            res = myDb.getData_mutlipleAttr(where_clause);
                            dialog_title = "Observations for \n" + where_clause_name + " with value: " + where_clause_name;
                        }



                        if (res.getCount() == 0) {
                            showMessage(dialog_title, getResources().getString(R.string.found_not));
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        int count = 1;
                        int total_count = res.getCount();
                        List<String> messages = new ArrayList<String>();

                        while (res.moveToNext()) {
                            buffer.append("Observation #" + count + "\n");
                            buffer.append("Name: " + res.getString(0) + "\n");
                            buffer.append("Location: " + res.getString(1) + "\n");
                            buffer.append("Size: " + res.getString(2) + "\n");
                            buffer.append("Colour: " + res.getString(3) + "\n");
                            buffer.append("Date: " + res.getString(4) + "\n");
                            buffer.append("Time: " + res.getString(5) + "\n");
                            buffer.append("Category: " + res.getString(6) + "\n");
                            buffer.append("Link: https://en.wikipedia.org/wiki/" + res.getString(0) + "\n");
                            buffer.append("----------------------------------------------------\n\n");

                            messages.add(
                                    getResources().getString(R.string.name)+": " + res.getString(0) + "\n" +
                                            getResources().getString(R.string.loc)+": " + res.getString(1) + "\n" +
                                            getResources().getString(R.string.size) +": " + res.getString(2) + "\n" +
                                            getResources().getString(R.string.clr)+": "+ res.getString(3) + "\n"  +
                                            getResources().getString(R.string.date1)+": "+ res.getString(4) + "\n" +
                                            getResources().getString(R.string.time1)+": "+ res.getString(5) + "\n" +
                                            getResources().getString(R.string.catg)+": " + res.getString(6) + "\n" +
                                            getResources().getString(R.string.wiki)+ res.getString(0) + "\n" +
                                    "\n");

                            count = count +1;
                        }

                        CustomMessage(messages, total_count);
                    }
                }
        );
    }

    public void CustomMessage (List<String> messages, int count)
    {
        setContentView(R.layout.show_observations);

        View linearLayout =  findViewById(R.id.info);
        for (int i=0; i< count; i++)
        {
            TextView observation_count = new TextView(this);
            observation_count.setText(getResources().getString(R.string.obs)+"#"+(i+1));
            observation_count.setTypeface(observation_count.getTypeface(), Typeface.BOLD);
            observation_count.setId(i);
            observation_count.setMovementMethod(LinkMovementMethod.getInstance());
            observation_count.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            ((LinearLayout) linearLayout).addView(observation_count);
            TextView bird_details = new TextView(this);
            bird_details.setText(messages.get(i));
            bird_details.setId(i);
            bird_details.setAutoLinkMask(Linkify.ALL);
            bird_details.setMovementMethod(LinkMovementMethod.getInstance());
            bird_details.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            ((LinearLayout) linearLayout).addView(bird_details);
        }

        Button exit_observ = new Button(this);
        exit_observ.setText("Exit");
        exit_observ.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        ((LinearLayout) linearLayout).addView(exit_observ);

        exit_observ.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox_name.setSelected(false);
                        checkBox_category.setSelected(false);
                        checkBox_location.setSelected(false);
                        checkBox_viewAllObs.setSelected(false);
                        name_attr_selected = false;
                        category_attr_selected = false;
                        location_attr_selected = false;
                        view_all_obs_selected = false;
                        Intent intent = new Intent(OBS_View_Observ.this, OBS_View_Observ.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
        );

    }

}
