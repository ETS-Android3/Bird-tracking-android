package com.example.droidsutra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class CAT_Select_Options extends AppCompatActivity {

    Bird myBird;
    Button btnViewCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat__select__options);

        if (TextUtils.equals(myBird.language_chosen, "en")) {
            int itemIndex = HomeScreen.Categories.indexOf("Standardkategorie");
            if (itemIndex != -1) {
                HomeScreen.Categories.set(itemIndex, "Default Category");
            }
        }
        else {
            int itemIndex = HomeScreen.Categories.indexOf("Default Category");
            if (itemIndex != -1) {
                HomeScreen.Categories.set(itemIndex, "Standardkategorie");
            }
        }

        btnViewCategories = (Button)findViewById(R.id.btnViewCategories);
        View_Categories();
    }

    public void Add_Category_Activity(View view) {
        Intent intent = new Intent(this, CAT_Add__Category.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void Modify_Category_Activity(View view) {
        Intent intent = new Intent(this, CAT_Modify_Category.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void Delete_Category_Activity(View view) {
        Intent intent = new Intent(this, CAT_Delete_Category.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public void View_Categories() {
        btnViewCategories.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        int i = 0;
                        StringBuffer buffer = new StringBuffer();
                        while (i <= HomeScreen.Categories.size()-1) {
                            buffer.append(HomeScreen.Categories.get(i) + "\n");
                            i = i +1;
                        }
                        showMessage(getResources().getString(R.string.show_msg_cat), buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CAT_Select_Options.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}


