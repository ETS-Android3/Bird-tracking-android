package com.example.droidsutra;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CAT_Delete_Category extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static String m_old_category;

    Button btnDeleteCategory;
    Databasehelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat__delete__category);
        btnDeleteCategory = (Button)findViewById(R.id.btnDeleteCategory);
        myDb = new Databasehelper(this);
        Delete_Category();


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CAT_Delete_Category.this, android.R.layout.simple_list_item_1,
                HomeScreen.Categories);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(CAT_Delete_Category.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        m_old_category = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void Delete_Category() {
        btnDeleteCategory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (m_old_category.equals("Default Category") || m_old_category.equals("Standardkategorie")) {
                            Toast.makeText(CAT_Delete_Category.this, getResources().getString(R.string.toast_cat_cantdelete), Toast.LENGTH_LONG).show();
                            return;
                        }

                        if  (HomeScreen.Categories.contains(m_old_category) ){
                            HomeScreen.Categories.remove(m_old_category);
                            myDb.deleteData_CategoryTable(m_old_category);
                            Toast.makeText(CAT_Delete_Category.this,  getResources().getString(R.string.toast_cat_del), Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(CAT_Delete_Category.this,  getResources().getString(R.string.toast_cat_dontexist), Toast.LENGTH_LONG).show();
                        }

                        int i = 0;
                        StringBuffer buffer = new StringBuffer();
                        while (i <= HomeScreen.Categories.size()-1) {
                            buffer.append(HomeScreen.Categories.get(i) + "\n");
                            i = i +1;
                        }

                        String count = String.format("%s",HomeScreen.Categories.size());
                        showMessage( getResources().getString(R.string.show_msg_cat), buffer.toString());

                    }
                }
        );
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CAT_Delete_Category.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
