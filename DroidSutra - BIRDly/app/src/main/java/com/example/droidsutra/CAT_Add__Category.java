package com.example.droidsutra;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CAT_Add__Category extends AppCompatActivity {

    EditText user_editText_new_category;
    public static String new_category;

    Button btnAddCategory;
    Databasehelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat__add___category);
        btnAddCategory = (Button)findViewById(R.id.btnAddCategory);
        myDb = new Databasehelper(this);
        Add_New_Category();

    }


    public void Add_New_Category() {
        btnAddCategory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user_editText_new_category = (EditText)findViewById(R.id.editText_new_category);
                        new_category = (String) user_editText_new_category.getText().toString();

                        if (new_category.equals("Default Category") || new_category.equals("Standardkategorie")) {
                            Toast.makeText(CAT_Add__Category.this, getResources().getString(R.string.toast_cat_cantadd), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (TextUtils.isEmpty(new_category)) {
                            Toast.makeText(CAT_Add__Category.this, getResources().getString(R.string.toast_ent_newcat), Toast.LENGTH_LONG).show();
                            return;
                        }

                        if  (HomeScreen.Categories.contains(new_category) ){
                            Toast.makeText(CAT_Add__Category.this, getResources().getString(R.string.toast_cat_exist) , Toast.LENGTH_LONG).show();
                        }
                        else {
                            HomeScreen.Categories.add(new_category);
                            boolean isInserted = myDb.insertData_CategoryTable(new_category);
                            if (isInserted == true) {
                                Toast.makeText(CAT_Add__Category.this, getResources().getString(R.string.toast_cat_added), Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(CAT_Add__Category.this, getResources().getString(R.string.toast_cat_not_added), Toast.LENGTH_LONG).show();
                            }
                        }




                        int i = 0;
                        StringBuffer buffer = new StringBuffer();

                        while (i <= HomeScreen.Categories.size()-1) {
                          buffer.append(HomeScreen.Categories.get(i) + "\n");
                          i = i +1;
                        }

                        // Show all data
                        String count = String.format("%s",HomeScreen.Categories.size());
                        showMessage( getResources().getString(R.string.show_msg_cat), buffer.toString());

                    }
                }
        );
    }


    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CAT_Add__Category.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }



}
