package com.example.droidsutra;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CAT_Modify_Category extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText user_editText_m_new_category;
    public static String m_new_category;
    public static String m_old_category;

    Button btnModifyCategory;
    Databasehelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat__modify__category);
        btnModifyCategory = (Button)findViewById(R.id.btnModifyCategory);
        myDb = new Databasehelper(this);
        Modify_Category();

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CAT_Modify_Category.this, android.R.layout.simple_list_item_1,
                HomeScreen.Categories);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(CAT_Modify_Category.this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        m_old_category = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void Modify_Category() {
        btnModifyCategory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (m_old_category.equals("Default Category") || m_old_category.equals("Standardkategorie")) {
                            Toast.makeText(CAT_Modify_Category.this, getResources().getString(R.string.toast_cat_cantmod), Toast.LENGTH_LONG).show();
                            return;
                        }
                        user_editText_m_new_category = (EditText)findViewById(R.id.editText_modify_category);
                        m_new_category = (String) user_editText_m_new_category.getText().toString();

                        if (TextUtils.isEmpty(m_new_category)) {
                            Toast.makeText(CAT_Modify_Category.this,  getResources().getString(R.string.toast_ent_newcat), Toast.LENGTH_LONG).show();
                            return;
                        }

                        if  (HomeScreen.Categories.contains(m_old_category) ){
                            int itemIndex = HomeScreen.Categories.indexOf(m_old_category);
                            if (itemIndex != -1) {
                                HomeScreen.Categories.set(itemIndex, m_new_category);
                            }
                            boolean isInserted = myDb.insertData_CategoryTable(m_new_category);
                            myDb.deleteData_CategoryTable(m_old_category);
                            Toast.makeText(CAT_Modify_Category.this, getResources().getString(R.string.toast_cat_mod), Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(CAT_Modify_Category.this,  getResources().getString(R.string.toast_cat_dontexist), Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CAT_Modify_Category.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
